package me.atticuszambrana.atticus.commands.impl.children;

import org.javacord.api.event.message.MessageCreateEvent;

import me.atticuszambrana.atticus.commands.Command;
import me.atticuszambrana.atticus.permissions.Rank;

public class KillCommand extends Command {
	
	public KillCommand() {
		super("kill", "Will literally fucking kill the child you give it", Rank.ADMIN);
	}

	@Override
	public void execute(String[] args, MessageCreateEvent event) {
	}
}
