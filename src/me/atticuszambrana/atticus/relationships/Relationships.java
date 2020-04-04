package me.atticuszambrana.atticus.relationships;

import java.awt.Color;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.user.User;
import org.javacord.api.event.message.MessageCreateEvent;
import org.javacord.api.listener.message.MessageCreateListener;

import me.atticuszambrana.atticus.Plugin;
import me.atticuszambrana.atticus.database.Database;
import me.atticuszambrana.atticus.manager.PluginManager;
import me.atticuszambrana.atticus.relationships.children.Child;
import me.atticuszambrana.atticus.relationships.enumerator.Gender;
import me.atticuszambrana.atticus.shards.TreasureShards;
import me.atticuszambrana.atticus.util.MessageUtil;

public class Relationships extends Plugin implements MessageCreateListener {
	// Database Connection
	private Connection conn;
	// Treasure Shards
	private TreasureShards shards;
	
	// Where we store marriage proposals
	private Map<Integer, MarriageProposal> Proposals = new HashMap<>();
	
	// To keep track of who is in the process of "setting up" their child
	public Map<User, Integer> ChildSetup = new HashMap<>();
	
	// To keep track of fucking admins literally killing children
	public Map<User, Integer> ChildKillings = new HashMap<>();
	public Map<User, Child> ChildToEnd = new HashMap<>();
	
	public Relationships() {
		super("Relationships");
	}

	@Override
	public void onEnable() {
		this.conn = Database.get().getConnection();
		this.shards = (TreasureShards) PluginManager.getPlugin(3);
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
	
	// Quick code to randomly generate a gender
	public Gender randomGender() {
		Random r = new Random();
		int i = r.nextInt(2);
		if(i == 0) {
			return Gender.MALE;
		} if(i == 1) {
			return Gender.FEMALE;
		} else {
			// this should never ever be ran
			return null;
		}
	}
	
	// Anything to do with Children
	public void createChild(Child child) throws SQLException {
		conn.createStatement().executeUpdate("INSERT INTO `Children` (`parent_one`, `parent_two`, `server_id`, `name`, `gender`, `birthstamp`) VALUES "
				+ "('" + child.getParentOne().getId() + "', '" + child.getParentTwo().getId() + "', '" + child.getServer().getId() + "', '"
				+ child.getName() + "', '" + child.getGender().toString() + "', '" + System.currentTimeMillis() + "');");
	}
	
	public List<Child> getChildren(User parent, Server server) throws SQLException {
		List<Child> cList = new ArrayList<>();
		ResultSet check_one = conn.createStatement().executeQuery("SELECT * FROM `Children` WHERE `parent_one` = '" + parent.getIdAsString() + "' AND `server_id` = '" + server.getIdAsString() + "';");
		while(check_one.next()) {
			Child c = new Child(check_one.getInt("id"), check_one.getLong("parent_one"), check_one.getLong("parent_two"), check_one.getLong("server_id"), check_one.getString("name"), Gender.valueOf(check_one.getString("gender")), check_one.getLong("birthstamp"));
			cList.add(c);
		}
		ResultSet check_two = conn.createStatement().executeQuery("SELECT * FROM `Children` WHERE `parent_two` = '" + parent.getIdAsString() + "' AND `server_id` = '" + server.getIdAsString() + "';");
		while(check_two.next()) {
			Child c = new Child(check_two.getInt("id"), check_two.getLong("parent_one"), check_two.getLong("parent_two"), check_two.getLong("server_id"), check_two.getString("name"), Gender.valueOf(check_two.getString("gender")), check_two.getLong("birthstamp"));
			cList.add(c);
		}
		
		return cList;
	}
	
	public boolean hasChildren(User user, Server server) throws SQLException {
		List<Child> cList = getChildren(user, server);
		if(cList.size() == 0) {
			return false;
		}
		return true;
	}
	
	public Child getChild(int id) throws SQLException {
		Child child = null;
		ResultSet check = conn.createStatement().executeQuery("SELECT * FROM `Children` WHERE `id` = '" + id + "';");
		while(check.next()) {
			child = new Child(check.getInt("id"), check.getLong("parent_one"), check.getLong("parent_two"), check.getLong("server_id"), check.getString("name"), Gender.valueOf(check.getString("gender")), check.getLong("birthstamp"));
		}
		return child;
	}
	
	public void killChild(Child child) throws SQLException {
		conn.createStatement().executeUpdate("DELETE FROM `Children` WHERE `id` = '" + child.getID() + "';");
	}
	
	public void updateChildName(Child child, String name) throws SQLException {
		System.out.print("UPDATE `Children` SET `name` = '" + name + "' WHERE `id` = '" + child.getID() + "';");
		conn.createStatement().executeUpdate("UPDATE `Children` SET `name` = '" + name + "' WHERE `id` = '" + child.getID() + "';");
	}
	
	public Map<Integer, MarriageProposal> getProposals() {
		return Proposals;
	}
	
	private Map<User, Gender> GenderStorage = new HashMap<>();

	@Override
	public void onMessageCreate(MessageCreateEvent event) {
		String m = event.getMessageContent();
		// Atticus' Bug fix
		if(ChildSetup.get(event.getMessageAuthor().asUser().get()) == null) {
			return;
		}
		if(ChildKillings.get(event.getMessageAuthor().asUser().get()) == null) {
			return;
		}
		if(m.indexOf("^") >= 0) { return; }
		
		// FOR CHILD DESTRUCTION
		if(ChildKillings.get(event.getMessageAuthor().asUser().get()) == 0) {
			if(m.equalsIgnoreCase("YES") || m.equalsIgnoreCase("YEAH")) {
				ChildKillings.remove(event.getMessageAuthor().asUser().get());
				
				// Do the thing
				Child c = this.ChildToEnd.get(event.getMessageAuthor().asUser().get());
				try {
					this.killChild(c);
				} catch (SQLException e) {
					e.printStackTrace();
					event.getChannel().sendMessage(MessageUtil.message(Color.RED, "Something went wrong.", "The car broke down before I could run over " + c.getName() + "!"));
					return;
				}
				
				event.getChannel().sendMessage(MessageUtil.message(Color.GREEN, "Done.", c.getName() + " has successfully been put down..."));
				return;
			}
			if(m.equalsIgnoreCase("NO") || m.equalsIgnoreCase("NAH")) {
				event.getChannel().sendMessage(MessageUtil.message(Color.RED, "Action Cancelled.", "OK. I have cancelled the action."));
				ChildKillings.remove(event.getMessageAuthor().asUser().get());
				return;
			}
		}
		
		// FOR CHILD CREATION
		if(ChildSetup.get(event.getMessageAuthor().asUser().get()) == 0) {
			if(m.equalsIgnoreCase("YES") || m.equalsIgnoreCase("YEAH")) {
				ChildSetup.put(event.getMessageAuthor().asUser().get(), 1);
				// Complete the transaction for the treasure shards
				try {
					shards.takeShards("[RELATIONMGR]", event.getMessageAuthor().asUser().get(), 50);
				} catch (SQLException e) {
					event.getChannel().sendMessage(MessageUtil.message(Color.RED, "Something bad happened", "Something went wrong with the transaction. Your treasure shards have not been taken, and the child creation process has been cancelled. Try again later."));
					e.printStackTrace();
					ChildSetup.remove(event.getMessageAuthor().asUser().get());
					return;
				}
				
				// Okay, then tell them the next step
				GenderStorage.put(event.getMessageAuthor().asUser().get(), this.randomGender());
				
				if(GenderStorage.get(event.getMessageAuthor().asUser().get()) == Gender.MALE) {
					event.getChannel().sendMessage(MessageUtil.message(Color.BLUE, "It's a boy!", "What do you want to name him?"));
				}
				if(GenderStorage.get(event.getMessageAuthor().asUser().get()) == Gender.FEMALE) {
					event.getChannel().sendMessage(MessageUtil.message(Color.PINK, "It's a girl!", "What do you want to name her?"));
				}
				
				return;
			}
			if(m.equalsIgnoreCase("NO") || m.equalsIgnoreCase("NAH")) {
				ChildSetup.remove(event.getMessageAuthor().asUser().get());
				event.getChannel().sendMessage(MessageUtil.message(Color.RED, "Action Cancelled", "Okay, I have cancelled the child creation process."));
				return;
			}
			
			ChildSetup.remove(event.getMessageAuthor().asUser().get());
			event.getChannel().sendMessage(MessageUtil.message(Color.RED, "Action Cancelled", "Incorrect Response. I have cancelled the child creation process."));
			return;
		}
		
		if(ChildSetup.get(event.getMessageAuthor().asUser().get()) == 1) {
			Gender g = GenderStorage.get(event.getMessageAuthor().asUser().get());
			
			try {
				// Grab the other parent
				User parent = getMarriage(event.getMessageAuthor().asUser().get(), event.getServer().get()).getOtherSpouse(event.getMessageAuthor().asUser().get());
				
				Child c = new Child(event.getMessageAuthor().getId(), parent.getId(), event.getServer().get().getId(), event.getMessageContent(), g, System.currentTimeMillis());
				
				this.createChild(c);
				
				event.getChannel().sendMessage(MessageUtil.message(c.getGender().getColor(), "Congratulations!", c.getParentOne().getName() + " and " + c.getParentTwo().getName() + " just had a baby named " + c.getName() + "!"));
				ChildSetup.remove(event.getMessageAuthor().asUser().get());
				return;
			} catch(SQLException ex) {
				ex.printStackTrace();
				ChildSetup.remove(event.getMessageAuthor().asUser().get());
				event.getChannel().sendMessage(MessageUtil.message(Color.RED, "Action Cancelled", "Something went wrong while communicating with the database. Please try again later."));
			}
		}
	}
}
