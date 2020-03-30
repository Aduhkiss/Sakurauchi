package me.atticuszambrana.atticus.manager;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.javacord.api.DiscordApi;
import org.javacord.api.entity.user.User;

import me.atticuszambrana.atticus.Start;
import me.atticuszambrana.atticus.database.Database;
import me.atticuszambrana.atticus.permissions.Rank;
import me.atticuszambrana.atticus.util.LogUtil;

public class PermissionsManager {
	
	private Database db;
	
	public PermissionsManager(Database db) {
		this.db = db;
		// Empty lol
		LogUtil.info("Permissions", "Starting Permissions Manager...");
		setupPermissions();
		LogUtil.info("Permissions", "Ready!");
	}
	
	private Map<String, Rank> PermissionsMatrix = new HashMap<>();
	
	public Rank getRank(User user) {
		if(PermissionsMatrix.get(user.getIdAsString()) == null) {
			return Rank.NONE;
		} else {
			return PermissionsMatrix.get(user.getIdAsString());
		}
	}
	
	public void setupPermissions() {
		// Also, yes.. run this on a different thread so we dont block anything else lmao
		new Thread() {
			public void run() {
				DiscordApi api = Start.getDiscord();
				
				// For every user that the bot has access to, we want to do a simple
				// lookup on the MySQL for what rank they have, then
				// assign that rank in our hashmap
				try {
					for(User user : api.getCachedUsers()) {
						ResultSet result = db.getConnection()
								.createStatement()
								.executeQuery("SELECT * FROM `NetworkRanks` WHERE `USER_ID` = '" + user.getIdAsString() + "';");
						
						while(result.next()) {
							// I am not going to write a try catch here, because there should never be an instance where there is a value in the database
							// that is not in the code either.
							// I feel like I am going to regret that decision soon though...
							Rank rank = Rank.valueOf(result.getString("RANK"));
							
							PermissionsMatrix.put(result.getString("USER_ID"), rank);
							LogUtil.info("Permissions", "Applied the rank " + rank.getName() + " to " + result.getString("USER_ID"));
						}
					}
				} catch(SQLException ex) {
					// Fuck.. something bad happened while doing something with the database
					// flip the fuck out
					LogUtil.info("Database", "Something really bad happened! Here, I'll give you a copy of the stack trace!");
					ex.printStackTrace();
					// idk
				}
			}
		}.start();
	}
}
