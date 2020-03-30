package me.atticuszambrana.atticus.permissions;

/*
 * Bot permission system
 * Author: Atticus Zambrana
 */

public enum Rank {
	
	NONE("None", 0),
	ADMIN("Admin", 10),
	DEVELOPER("Developer", 15);
	
	private String name;
	private int power;
	
	Rank(String name, int power) {
		this.name = name;
		this.power = power;
	}
	
	public String getName() {
		return name;
	}
	
	public int getPower() {
		return power;
	}
}
