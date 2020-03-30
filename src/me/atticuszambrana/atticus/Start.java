package me.atticuszambrana.atticus;


import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.javacord.api.entity.activity.ActivityType;

import com.google.gson.Gson;

import me.atticuszambrana.atticus.config.Config;
import me.atticuszambrana.atticus.config.ConfigHelper;
import me.atticuszambrana.atticus.database.Database;
import me.atticuszambrana.atticus.manager.CommandCenter;
import me.atticuszambrana.atticus.manager.PermissionsManager;
import me.atticuszambrana.atticus.manager.PluginManager;
import me.atticuszambrana.atticus.relationships.Relationships;
import me.atticuszambrana.atticus.shards.TreasureShards;
import me.atticuszambrana.atticus.util.LogUtil;
import me.atticuszambrana.atticus.util.UtilTime;
import me.atticuszambrana.atticus.util.UtilTime.TimeUnit;

public class Start {
	
	private static String BUILD = "v1.2";
	
	private static DiscordApi api;
	private static boolean debugMessages = true;
	
	// Stuff
	private static PermissionsManager permissions;
	private static CommandCenter commandManager;
	
	// The Currently active config file
	private static Config activeConfig;
	
	 public static void main(String[] args) {
		 LogUtil.info("System", "Welcome to the Riko Sakurauchi Discord Bot!");
		 LogUtil.info("System", "This Discord bot is developed by Atticus Zambrana");
		 
		 // Grab the time as of now
		 long epoch = System.currentTimeMillis();
		 
		 String TOKEN = null;
		 
		 LogUtil.info("System", "Logging into Discord...");
		 
		 
		 // Before we do anything, we need to access the config file
		 // If we dont have one currently, lets make it, then shut the bot down
		 Gson g = new Gson();
		 System.out.println("Looking for a config file...");
		 if(ConfigHelper.exists()) {
			 System.out.println("Found it! Now mounting...");
			 activeConfig = g.fromJson(ConfigHelper.get(), Config.class);
		 } else {
			 System.out.println("None was found. Creating a new one from default settings...");
			 Config defaultConfig = new Config("put the token here", "put the app id here", "^");
			 ConfigHelper.write(g.toJson(defaultConfig));
			 System.out.println("Done. Now Shutting the bot down for you to setup.");
			 System.exit(0);
			 return;
		 }
		 
		 // Get all of the discord api stuff ready for work
		 try {
			 api = new DiscordApiBuilder()
					 .setToken(getConfig().getToken())
					 .login()
					 .join();
		 } catch(IllegalStateException ex) {
			 LogUtil.info("Discord API", "Something really bad happened while trying to login to Discord! Heres the error: " + ex.getMessage());
			 LogUtil.info("System", "A critical error has been detected. Dispatching Server Support team and shutting down machine...");
			 
			 //TODO: Send notification to entire administration team to let them know the bot had an issue while connecting to the API
			 
			 System.exit(0);
		 }
		
		// Give console an invite link
		System.out.println("[INVITE] " + "https://discordapp.com/oauth2/authorize?client_id=" + getConfig().getID() + "&scope=bot&permissions=8");
		
		// Make the Activity display something about starting up
		Start.getDiscord().updateActivity(ActivityType.PLAYING, "Starting please wait...");
		
		// Setup the Database object
		Database db = Database.get();
		
		// Setup the Permissions Manager
		permissions = new PermissionsManager(db);
		
		// Start the Discord message listener so we can handle our commands, then hook it to the Command Manager
		LogUtil.info("Command Manager Hook", "Starting Command Manager...");
		commandManager = new CommandCenter(permissions);
		commandManager.setup();
		api.addMessageCreateListener(commandManager);
		
		// Start the plugins with the plugin manager
		LogUtil.info("Plugin Manager", "Starting Plugin Manager...");
		PluginManager.registerPlugins();
		
		// Add the Treasure Shard System as a listener
		api.addMessageCreateListener((TreasureShards) PluginManager.getPlugin(3));
		// Add the Relationship System
		api.addMessageCreateListener((Relationships) PluginManager.getPlugin(5));
		
		LogUtil.info("System", "All Done! Successfully started in " + UtilTime.convertString(System.currentTimeMillis() - epoch, 1, TimeUnit.FIT) + ".");
	 }
	 
	 public static DiscordApi getDiscord() {
		 return api;
	 }
	 public static PermissionsManager getPermManager() {
		 return permissions;
	 }
	 public static CommandCenter getCommandManager() {
		 return commandManager;
	 }
	 public static Config getConfig() {
		 return activeConfig;
	 }
	 public static boolean showDebugMessages() {
		 return debugMessages;
	 }
	 public static String getBuild() {
		 return BUILD;
	 }
}
