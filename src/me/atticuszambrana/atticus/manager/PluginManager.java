package me.atticuszambrana.atticus.manager;

import java.util.HashMap;
import java.util.Map;

import me.atticuszambrana.atticus.Plugin;
import me.atticuszambrana.atticus.activity.Activity;
import me.atticuszambrana.atticus.chatbot.Chatbot;
import me.atticuszambrana.atticus.relationships.Relationships;
import me.atticuszambrana.atticus.shards.TreasureShards;

public class PluginManager {
	
	// Static class to keep track of our plugins in the code
	
	private static Map<Integer, Plugin> Plugins = new HashMap<>();
	
	public static void registerPlugins() {
		// Void called by the main class to register all of the plugins
		Plugins.put(1, new Activity());
		//Plugins.put(2, ); // ID 2 HAS BEEN RETIRED AND WILL NOT BE USED ANYMORE
		Plugins.put(3, new TreasureShards());
		Plugins.put(4, new Chatbot());
		// Keep this disabled until I actually care about working on it
		Plugins.put(5, new Relationships());
	}
	
	public static Plugin getPlugin(int id) {
		return Plugins.get(id);
	}
	
	public static Map<Integer, Plugin> getPlugins() {	
		return Plugins;
	}
}
