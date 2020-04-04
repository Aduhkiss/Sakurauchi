package me.atticuszambrana.atticus.punishment;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.javacord.api.entity.channel.ServerTextChannel;
import org.javacord.api.entity.server.Server;

import me.atticuszambrana.atticus.Plugin;
import me.atticuszambrana.atticus.database.Database;

public class ModLog extends Plugin {
	
	private Connection conn;
	
	public ModLog() {
		super("ModLog Manager");
		// 9
	}

	@Override
	public void onEnable() {
		conn = Database.get().getConnection();
	}
	
	public boolean hasModLog(Server server) throws SQLException {
		boolean f = false;
		ResultSet result = conn.createStatement().executeQuery("SELECT * FROM `ModlogChannels` WHERE `server_id` = '" + server.getId() + "';");
		while(result.next()) {
			f = true;
		}
		return f;
	}
	
	public ServerTextChannel getModLog(Server server) throws SQLException {
		if(!hasModLog(server)) { return null; }
		ServerTextChannel channel = null;
		ResultSet result = conn.createStatement().executeQuery("SELECT * FROM `ModlogChannels` WHERE `server_id` = '" + server.getId() + "';");
		while(result.next()) {
			channel = server.getTextChannelById(result.getLong("channel")).get();
		}
		return channel;
	}
	
	public void setModLog(Server server, ServerTextChannel channel) throws SQLException {
		if(hasModLog(server)) {
			conn.createStatement().executeUpdate("UPDATE `ModlogChannels` SET `channel` = '" + channel.getId() + "' WHERE `server_id` = '" + server.getId() + "';");
		} else {
			conn.createStatement().executeUpdate("INSERT INTO `ModlogChannels` (`channel`, `server_id`) VALUES ('" + channel.getId() + "', '" + server.getId() + "');");
		}
	}

}
