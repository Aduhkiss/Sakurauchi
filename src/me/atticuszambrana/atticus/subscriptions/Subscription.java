package me.atticuszambrana.atticus.subscriptions;

import java.awt.Color;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.entity.server.Server;
import org.javacord.api.event.server.ServerJoinEvent;
import org.javacord.api.event.server.ServerLeaveEvent;
import org.javacord.api.listener.server.ServerJoinListener;
import org.javacord.api.listener.server.ServerLeaveListener;

import me.atticuszambrana.atticus.Plugin;
import me.atticuszambrana.atticus.database.Database;
import me.atticuszambrana.atticus.util.LogUtil;

public class Subscription extends Plugin implements ServerJoinListener, ServerLeaveListener {
	
	private Connection conn;
	
	public Subscription() {
		super("Subscription Service");
	}

	@Override
	public void onEnable() {
		conn = Database.get().getConnection();
	}
	
	public boolean hasPremium(Server server) throws SQLException {
		ResultSet result = conn.createStatement().executeQuery("SELECT * FROM `Servers` WHERE `server_id` = '" + server.getIdAsString() + "' AND `subscription` = '" + "PREMIUM" + "';");
		while(result.next()) {
			return true;
		}
		return false;
	}
	
	public boolean isValid(Server server) throws SQLException {
		ResultSet result = conn.createStatement().executeQuery("SELECT * FROM `Servers` WHERE `server_id` = '" + server.getIdAsString() + "' AND `subscription` = '" + "PREMIUM" + "';");
		while(result.next()) {
			long expires = result.getLong("expires");
			if(expires == 42069) {
				LogUtil.info("Subscription Service", server.getName() + " has UNLIMITED Premium.");
				return true;
			}
			
			long now = System.currentTimeMillis();
			if(now >= expires) {
				
				// Why not just revert them back to FREE here?
				conn.createStatement().executeUpdate("UPDATE `Servers` SET `subscription` = 'FREE' WHERE `server_id` = '" + server.getIdAsString() + "';");
				LogUtil.info("Subscription Service", server.getName() + "'s Premium has expired. Reverting back to FREE...");
				
				return false;
			}
			
			return true;
		}
		return false;
	}

	@Override
	public void onServerJoin(ServerJoinEvent event) {
		new Thread() {
			public void run() {
				try {
					
					if(hasPremium(event.getServer())) {
						// Send them a message, telling them they still have premium for their server
						EmbedBuilder embed = new EmbedBuilder();
						embed.setTitle("Welcome back!");
						embed.setDescription("Your server, " + event.getServer().getName() + " still has an active Premium status! Welcome back!");
						embed.setColor(Color.PINK);
						event.getServer().getOwner().sendMessage(embed);
						LogUtil.info("Subscription Service", "I have been added to a new server, that has premium. (" + event.getServer().getName() + ")");
						return;
					}
					
					// Add this new server into the database so we can give it premium from the
					// website if the owner ever buys
					conn.createStatement().executeUpdate("INSERT INTO `Servers` (`owner`, `server_id`, `subscription`, `expires`) VALUES ('" + event.getServer().getOwner().getId() + "', '" + event.getServer().getId() + "', 'FREE', '0');");
					LogUtil.info("Subscription Service", "I have been added to a new server. (" + event.getServer().getName() + ")");
					return;
					
				} catch(SQLException ex) {
					ex.printStackTrace();
				}
			}
		}.start();
	}
	
	@Override
	public void onServerLeave(ServerLeaveEvent event) {
		// Check if the server currently has premium, if they do, we dont want to remove the value
		// If they do not, remove the value from the database
		new Thread() {
			public void run() {
				try {
					
					if(!hasPremium(event.getServer())) {
						LogUtil.info("Subscription Service", "I have left a server. (" + event.getServer().getName() + ")");
						
						// Remove from the database
						conn.createStatement().executeUpdate("DELETE FROM `Servers` WHERE `server_id` = '" + event.getServer().getId() + "';");
						
					} else {
						LogUtil.info("Subscription Service", "I have left a premium server. (" + event.getServer().getName() + ")");
					}
					
				} catch(SQLException ex) {
					ex.printStackTrace();
				}
			}
		}.start();
	}
	
}
