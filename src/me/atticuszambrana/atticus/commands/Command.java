package me.atticuszambrana.atticus.commands;

import org.javacord.api.event.message.MessageCreateEvent;

import me.atticuszambrana.atticus.permissions.Rank;

public abstract class Command {
	
	private String name;
	private String description;
	private Rank rankRequired;
	
	public Command(String name, Rank rankRequired) {
		this.name = name;
		this.description = "[None Provided]";
		this.rankRequired = rankRequired;
	}
	
	public Command(String name, String description, Rank rankRequired) {
		this.name = name;
		this.description = description;
		this.rankRequired = rankRequired;
	}
	
	public String getName() {
		return name;
	}
	
	public String getDescription() {
		return description;
	}
	
	public Rank getRankRequired() {
		return rankRequired;
	}
	
	public abstract void execute(String[] args, MessageCreateEvent event);
}
