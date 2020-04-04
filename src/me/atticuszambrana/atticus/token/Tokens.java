package me.atticuszambrana.atticus.token;

import java.awt.Color;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.entity.server.Server;

import me.atticuszambrana.atticus.Plugin;
import me.atticuszambrana.atticus.database.Database;

public class Tokens extends Plugin {
	
	private Connection conn;
	
	public Tokens() {
		super("Token System");
	}

	@Override
	public void onEnable() {
		conn = Database.get().getConnection();
		
		
		// ONLY USE THIS TO PUT NEW TOKENS INTO THE SYSTEM
		
//		new Thread() {
//			public void run() {
//				List<UUID> tokens = getTokens(80);
//				
//				for(UUID t : tokens) {
//					try {
//						conn.createStatement().executeUpdate("INSERT INTO `Tokens` (`token`, `active`) VALUES ('" + t.toString() + "', 'true');");
//					} catch (SQLException e) {e.printStackTrace();}
//				}
//			}
//		}.start();
	}
	
	public List<UUID> getTokens(int amount) {
		List<UUID> tokens = new ArrayList<>();
		for(int i = 0; i < amount; i++) {
			tokens.add(UUID.randomUUID());
		}
		return tokens;
	}
	
	public boolean isActive(UUID uuid) throws SQLException {
		ResultSet result = conn.createStatement().executeQuery("SELECT * FROM `Tokens` WHERE `token` = '" + uuid.toString() + "';");
		while(result.next()) {
			return Boolean.valueOf(result.getString("active"));
		}
		return false;
	}
	
	public void redeemToken(UUID uuid, Server server) throws SQLException {
		conn.createStatement().executeUpdate("UPDATE `Tokens` SET `active` = 'false' WHERE `token` = '" + uuid.toString() + "';");
		
		long now = System.currentTimeMillis();
		long expires = now + 2592000000l;
		
		conn.createStatement().executeUpdate("UPDATE `Servers` SET `subscription` = 'PREMIUM' WHERE `server_id` = '" + server.getId() + "';");
		conn.createStatement().executeUpdate("UPDATE `Servers` SET `expires` = '" + expires + "' WHERE `server_id` = '" + server.getId() + "';");
		
		// Send a message to the server owner as well because why not
		EmbedBuilder embed = new EmbedBuilder();
		embed.setColor(Color.PINK);
		embed.setTitle(server.getName() + " now has 1 month of premium!");
		embed.setDescription("You or someone else, has just redeemed 1 month of premium status for your server: " + server.getName() + "! Thank you for the purchase!");
		
		server.getOwner().sendMessage(embed);
	}
	
	public boolean exists(UUID uuid) throws SQLException {
		boolean f = false;
		ResultSet result = conn.createStatement().executeQuery("SELECT * FROM `Tokens` WHERE `token` = '" + uuid.toString() + "';");
		while(result.next()) {
			f = true;
		}
		return f;
	}
}
