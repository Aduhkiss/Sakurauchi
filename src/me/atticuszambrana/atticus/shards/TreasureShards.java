package me.atticuszambrana.atticus.shards;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.javacord.api.entity.user.User;
import org.javacord.api.event.message.MessageCreateEvent;
import org.javacord.api.listener.message.MessageCreateListener;

import me.atticuszambrana.atticus.Plugin;
import me.atticuszambrana.atticus.Start;
import me.atticuszambrana.atticus.database.Database;
import me.atticuszambrana.atticus.manager.PluginManager;
import me.atticuszambrana.atticus.util.LogUtil;

public class TreasureShards extends Plugin implements MessageCreateListener {
	
	private Connection conn;
	
	public TreasureShards() {
		super("Treasure Shards System");
	}

	@Override
	public void onEnable() {
		// Setup the database so we can communicate
		conn = Database.get().getConnection();
		
		// Then when the bot starts, go through all the cached users, check if we have values for them, and if we dont, make them values of 0
		for(User user : Start.getDiscord().getCachedUsers()) {
			if(!user.isBot()) {
				check(user);
			}
		}
	}
	
	public int getShards(User user) throws SQLException {
		check(user);
		int i = 0;
		int shardCount = 0;
		ResultSet result = conn.createStatement().executeQuery("SELECT * FROM `Shards` WHERE `user_id` = '" + user.getIdAsString() + "';");
		while(result.next()) {
			i++;
			shardCount = result.getInt("shard_count");
		}
		
		if(i == 0) {
			return 0;
		}
		return shardCount;
	}
	
	public void setShards(User user, int amount) throws SQLException {
		check(user);
		conn.createStatement().executeUpdate("UPDATE `Shards` SET `shard_count` = '" + amount + "' WHERE `user_id` = '" + user.getIdAsString() + "';");
		return;
	}
	
	public void giveShards(String caller, User target, int amount) throws SQLException {
		check(target);
		int old = 0;
		try {
			old = getShards(target);
		} catch(SQLException ex) {}
		
		setShards(target, old + amount);
		
		//TODO: Record this transaction in the database
		if(caller.equals("[NOCOUNT]")) {
			return;
		}
		
		new Thread() {
			public void run() {
				try {
					conn.createStatement().executeUpdate("INSERT INTO `EcoLogs` (`target`, `caller`, `amount`, `type`) VALUES ('" + target.getDiscriminatedName() + "', '" + caller + "', '" + amount + "', '" + "<--');");
				} catch(SQLException ex) {
					LogUtil.info("Economy Logger", "There was a problem logging this transaction: " + ex.getMessage());
				}
			}
		}.start();
	}
	
	public void takeShards(String caller, User target, int amount) throws SQLException {
		check(target);
		int old = 0;
		try {
			old = getShards(target);
		} catch(SQLException ex) {}
		
		setShards(target, old - amount);
		// again, will use the above set method
		
		//TODO: Record this transaction in the database
		if(caller.equals("[NOCOUNT]")) {
			return;
		}
		
//		new Thread() {
//			public void run() {
//				try {
//					conn.createStatement().executeUpdate("INSERT INTO `EcoLogs` (`target`, `caller`, `amount`, `type`) VALUES ('" + target.getDiscriminatedName() + "', '" + caller + "', '" + amount + "', '" + "-->');");
//				} catch(SQLException ex) {
//					LogUtil.info("Economy Logger", "There was a problem logging this transaction: " + ex.getMessage());
//				}
//			}
//		}.start();
	}
	
	/**
	 * When given a user, will check if the user exists in the system, if they dont, we create them a value, if they do, nothing happens
	 * @param user
	 */
	private void check(User user) {
		new Thread() {
			public void run() {
				try {
					int i = 0;
					ResultSet result = Database.get().getConnection().createStatement().executeQuery("SELECT * FROM `Shards` WHERE `user_id` = '" + user.getIdAsString() + "';");
					while(result.next()) {
						i++;
					}
					
					if(i == 0) {
						//System.out.println("INSERT INTO `Shards` (`user_id`, `shard_count`) VALUES ('" + user.getIdAsString() + "', 0);");
						conn.createStatement().executeUpdate("INSERT INTO `Shards` (`user_id`, `shard_count`) VALUES ('" + user.getIdAsString() + "', 0);");
					}
				} catch(SQLException ex) {
					ex.printStackTrace();
				}
			}
		}.start();
	}
	
	private Map<User, Integer> msgs = new HashMap<>();

	@Override
	public void onMessageCreate(MessageCreateEvent event) {
		if(event.isPrivateMessage()) {
			return;
		}
		
		if(msgs.get(event.getMessageAuthor().asUser().get()) == null) {
			msgs.put(event.getMessageAuthor().asUser().get(), 1);
			return;
		}
		
		if(msgs.get(event.getMessageAuthor().asUser().get()) >= 5) {
			// Award them a shard
			TreasureShards shards = (TreasureShards) PluginManager.getPlugin(3);
			try {
				shards.giveShards("[NOCOUNT]", event.getMessageAuthor().asUser().get(), 1);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			msgs.put(event.getMessageAuthor().asUser().get(), 1);
			return;
			
		} else {
			// Just bring their level up by one
			int old = msgs.get(event.getMessageAuthor().asUser().get());
			msgs.put(event.getMessageAuthor().asUser().get(), old + 1);
			return;
		}
	}

}
