package me.atticuszambrana.atticus;

import me.atticuszambrana.atticus.util.LogUtil;

public abstract class Plugin {
	
	private String name = "Default";
	
	public Plugin(String name) {
		this.name = name;
		LogUtil.info(name, "Starting " + name + "...");
		onEnable();
	}
	
	public abstract void onEnable();
}
