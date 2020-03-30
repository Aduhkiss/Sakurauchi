package me.atticuszambrana.atticus.config;

public class Config {
	
	// Bot Config Stuff
	public String TOKEN;
	public String APP_ID;
	public String PREFIX;
	
	// Database Stuff
	public String SQL_HOST;
	public String SQL_DB;
	public String SQL_USERNAME;
	public String SQL_PASSWORD;
	
	public Config(String TOKEN, String APP_ID, String PREFIX) {
		this.TOKEN = TOKEN;
		this.APP_ID = APP_ID;
		this.PREFIX = PREFIX;
		
		this.SQL_HOST = "Insert Hostname";
		this.SQL_DB = "Insert DB Name";
		this.SQL_USERNAME = "Insert Username";
		this.SQL_PASSWORD = "Insert Password";
	}
	
	public String getToken() {
		return TOKEN;
	}
	public String getID() {
		return APP_ID;
	}
	public String getPrefix() {
		return PREFIX;
	}
	public String getHost() {
		return SQL_HOST;
	}
	public String getDB() {
		return SQL_DB;
	}
	public String getUsername() {
		return SQL_USERNAME;
	}
	public String getPassword() {
		return SQL_PASSWORD;
	}
}

