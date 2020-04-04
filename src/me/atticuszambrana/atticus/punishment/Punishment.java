package me.atticuszambrana.atticus.punishment;

import java.awt.Color;
import java.sql.Connection;
import java.sql.SQLException;

import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.user.User;

import me.atticuszambrana.atticus.Plugin;
import me.atticuszambrana.atticus.database.Database;
import me.atticuszambrana.atticus.manager.PluginManager;

public class Punishment extends Plugin {
	
	// While Sandbox mode is active, no punishments will actually be filed
	private boolean SANDBOX = false;
	
	// Database Connection
	private Connection conn;
	
	public Punishment() {
		super("Punishment System");
	}

	@Override
	public void onEnable() {
		conn = Database.get().getConnection();
	}
	
	/*
	 * Will return whether the punishment was a success or not
	 */
	public boolean kick(User caller, User target, String reason, Server server) {
		ModLog modlog = (ModLog) PluginManager.getPlugin(9);
		if(!SANDBOX) {
			// Maybe add this to the database so we can get it later?
			try {
				conn.createStatement().executeUpdate("INSERT INTO `Punish_Kicks` (`caller`, `target`, `server_id`, `reason`, `timestamp`) VALUES ('" + caller.getId() + "', '" + target.getId() + "', '" + server.getId() + "', '" + reason + "', '" + System.currentTimeMillis() + "');");
			} catch(SQLException ex) {
				ex.printStackTrace();
				return false;
			}

//			// Send a message to the banned user
//			EmbedBuilder embed = new EmbedBuilder();
//			embed.setColor(Color.RED);
//			embed.setTitle("You have been kicked!");
//			embed.setDescription("You have been kicked from " + server.getName() + "!");
//			embed.addInlineField("Moderator", caller.getName());
//			embed.addInlineField("Reason", reason);
//			
//			target.sendMessage(embed);
			
			try {
				if(modlog.hasModLog(server)) {
					EmbedBuilder punish = new EmbedBuilder();
					punish.setColor(Color.RED);
					punish.setTitle("Kick Alert");
					punish.addInlineField("Target", target.getName());
					punish.addInlineField("Moderator", caller.getName());
					punish.addInlineField("Reason", reason);
					
					modlog.getModLog(server).sendMessage(punish);
				}
			} catch(SQLException ex) {
				ex.printStackTrace();
			}
			
			server.kickUser(target, reason);
			
			return true;
		}
		return true;
	}
	
	public boolean ban(User caller, User target, String reason, Server server) {
		ModLog modlog = (ModLog) PluginManager.getPlugin(9);
		if(!SANDBOX) {
			// Maybe add this to the database so we can get it later?
			try {
				conn.createStatement().executeUpdate("INSERT INTO `Punish_Bans` (`caller`, `target`, `server_id`, `reason`, `timestamp`) VALUES ('" + caller.getId() + "', '" + target.getId() + "', '" + server.getId() + "', '" + reason + "', '" + System.currentTimeMillis() + "');");
			} catch(SQLException ex) {
				ex.printStackTrace();
				return false;
			}
			
			try {
				if(modlog.hasModLog(server)) {
					EmbedBuilder punish = new EmbedBuilder();
					punish.setColor(Color.RED);
					punish.setTitle("Ban Alert");
					punish.addInlineField("Target", target.getName());
					punish.addInlineField("Moderator", caller.getName());
					punish.addInlineField("Reason", reason);
					
					modlog.getModLog(server).sendMessage(punish);
				}
			} catch(SQLException ex) {
				ex.printStackTrace();
			}

			server.banUser(target, 7, reason);
			
			return true;
		}
		return true;
	}

}
