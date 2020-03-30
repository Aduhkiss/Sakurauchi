package me.atticuszambrana.atticus.commands;

import java.util.List;

public class Category {
	
	private String name;
	private List<Command> commands;
	
	public Category(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public List<Command> getCommands() {
		return commands;
	}
	
	public void addCommand(Command c) {
		commands.add(c);
	}
}
