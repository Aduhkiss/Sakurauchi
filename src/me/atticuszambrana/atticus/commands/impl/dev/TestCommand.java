package me.atticuszambrana.atticus.commands.impl.dev;

import org.javacord.api.event.message.MessageCreateEvent;

import me.atticuszambrana.atticus.commands.Command;
import me.atticuszambrana.atticus.permissions.Rank;
import me.atticuszambrana.atticus.util.LogUtil;

public class TestCommand extends Command {
	
	public TestCommand() {
		super("test", "A simple testing command", Rank.DEVELOPER);
	}

	@Override
	public void execute(String[] args, MessageCreateEvent event) {
		LogUtil.info("Testing", "It works!");
		
		for(String arg : args) {
			LogUtil.info("Test", arg);
		}
		return;
	}

}
