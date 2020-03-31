package me.atticuszambrana.atticus.relationships.children;

import java.util.concurrent.ExecutionException;

import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.user.User;

import me.atticuszambrana.atticus.Start;
import me.atticuszambrana.atticus.relationships.enumerator.Gender;

public class Child {
	
	private User parent_one;
	private User parent_two;
	
	private Server server;
	
	// Values that define what a Child actually is
	private int id;
	private String name;
	private Gender gender;
	private long birthstamp;
	
	public Child(int id, long parent_one_id, long parent_two_id, long server_id, String name, Gender gender, long birthstamp) {
		try {
			this.parent_one = Start.getDiscord().getUserById(parent_one_id).get();
			this.parent_two = Start.getDiscord().getUserById(parent_two_id).get();
			this.server = Start.getDiscord().getServerById(server_id).get();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		
		this.id = id;
		this.name = name;
		this.gender = gender;
		this.birthstamp = birthstamp;
	}
	
	public Child(long parent_one_id, long parent_two_id, long server_id, String name, Gender gender, long birthstamp) {
		try {
			this.parent_one = Start.getDiscord().getUserById(parent_one_id).get();
			this.parent_two = Start.getDiscord().getUserById(parent_two_id).get();
			this.server = Start.getDiscord().getServerById(server_id).get();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		
		this.name = name;
		this.gender = gender;
		this.birthstamp = birthstamp;
	}
	
	public User getParentOne() {
		return parent_one;
	}
	
	public User getParentTwo() {
		return parent_two;
	}
	
	public Server getServer() {
		return server;
	}
	
	public Gender getGender() {
		return gender;
	}
	
	public String getName() {
		return name;
	}
	
	public long getBirthstamp() {
		return birthstamp;
	}
	
	public int getID() {
		return id;
	}
}
