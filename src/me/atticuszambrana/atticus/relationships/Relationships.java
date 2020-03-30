package me.atticuszambrana.atticus.relationships;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.user.User;
import org.javacord.api.event.message.MessageCreateEvent;
import org.javacord.api.listener.message.MessageCreateListener;

import me.atticuszambrana.atticus.Plugin;
import me.atticuszambrana.atticus.database.Database;

public class Relationships extends Plugin implements MessageCreateListener {
	// Database Connection
	private Connection conn;
	
	// Where we store marriage proposals
	private Map<Integer, MarriageProposal> Proposals = new HashMap<>();
	
	public Relationships() {
		super("Relationships");
	}

	@Override
	public void onEnable() {
		this.conn = Database.get().getConnection();
	}
	
	public boolean isMarried(User user, Server server) throws SQLException {
		int results = 0;
		ResultSet check_one = conn.createStatement().executeQuery("SELECT * FROM `Marriages` WHERE `spouse_one` = '" + user.getIdAsString() + "' AND `server_id` = '" + server.getIdAsString() + "';");
		ResultSet check_two = conn.createStatement().executeQuery("SELECT * FROM `Marriages` WHERE `spouse_two` = '" + user.getIdAsString() + "' AND `server_id` = '" + server.getIdAsString() + "';");
		
		while(check_one.next()) {
			results++;
		}
		if(results == 0) {
			while(check_two.next()) {
				results++;
			}
		}
		
		if(results == 0) {
			return false;
		}
		return true;
	}
	
	public Marriage getMarriage(User user, Server server) throws SQLException {
		if(!isMarried(user, server)) {
			return null;
		}
		boolean found = false;
		Marriage m = null;
		ResultSet one = conn.createStatement().executeQuery("SELECT * FROM `Marriages` WHERE `spouse_one` = '" + user.getIdAsString() + "' AND `server_id` = '" + server.getIdAsString() + "';");
		while (one.next()) {
			found = true;
			m = new Marriage(server, one.getLong("spouse_one"), one.getLong("spouse_two"), one.getInt("id"));
			return m;
		}

		ResultSet two = conn.createStatement().executeQuery("SELECT * FROM `Marriages` WHERE `spouse_two` = '" + user.getIdAsString() + "' AND `server_id` = '" + server.getIdAsString() + "';");

		while(two.next()) {
			m = new Marriage(server, two.getLong("spouse_one"), two.getLong("spouse_two"), two.getInt("id"));
		}
		
		return m;
	}
	
	public void deleteMarriage(Marriage m) throws SQLException {
		conn.createStatement().executeUpdate("DELETE FROM `Marriages` WHERE `id` = '" + m.getID() + "';");
	}
	
	public int createProposal(MarriageProposal pro) {
		Random r = new Random();
		int id = r.nextInt(1000000);
		if(getProposal(id) != null) {
			return createProposal(pro);
		}
		
		Proposals.put(id, pro);
		return id;
	}
	
	public void marry(MarriageProposal proposal, Server server) throws SQLException {
		conn.createStatement().executeUpdate("INSERT INTO `Marriages` (`spouse_one`, `spouse_two`, `server_id`) VALUES ('" + proposal.getUser1().getIdAsString() + "', '" + proposal.getUser2().getIdAsString() + "', '" + server.getIdAsString() + "');");
	}
	
	public void killProposal(int id) {
		Proposals.remove(id);
	}
	
	public MarriageProposal getProposal(int id) {
		return Proposals.get(id);
	}
	
	public Map<Integer, MarriageProposal> getProposals() {
		return Proposals;
	}

	@Override
	public void onMessageCreate(MessageCreateEvent event) {
	}
}
