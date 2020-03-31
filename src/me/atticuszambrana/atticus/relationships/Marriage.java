package me.atticuszambrana.atticus.relationships;

import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.user.User;

public class Marriage {
	
	private int id;
	private Server server;
	private User spouse_one;
	private User spouse_two;
	
	public Marriage(Server server, long spouse_one_id, long spouse_two_id, int id) {
		this.server = server;
		this.spouse_one = server.getMemberById(spouse_one_id).get();
		this.spouse_two = server.getMemberById(spouse_two_id).get();
		this.id = id;
	}
	
	public int getID() {
		return id;
	}
	
	public Server getServer() {
		return server;
	}
	
	public User getSpouseOne() {
		return spouse_one;
	}
	
	public User getSpouseTwo() {
		return spouse_two;
	}
	
	public User getOtherSpouse(User user) {
		if(getSpouseOne().getId() == user.getId()) {
			return getSpouseTwo();
		} else {
			return getSpouseOne();
		}
	}
}
