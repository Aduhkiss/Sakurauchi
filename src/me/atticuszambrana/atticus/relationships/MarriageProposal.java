package me.atticuszambrana.atticus.relationships;

import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.user.User;

public class MarriageProposal {
	
	private User user_one;
	private User user_two;
	private Server server;
	
	public MarriageProposal(User user_one, User user_two, Server server) {
		this.user_one = user_one;
		this.user_two = user_two;
		this.server = server;
	}
	
	public User getUser1() {
		return user_one;
	}
	
	public User getUser2() {
		return user_two;
	}
	
	public Server getServer() {
		return server;
	}
}
