package me.atticuszambrana.atticus.commands.impl.dev;

import org.javacord.api.entity.channel.ServerTextChannel;
import org.javacord.api.event.message.MessageCreateEvent;

import me.atticuszambrana.atticus.commands.Command;
import me.atticuszambrana.atticus.permissions.Rank;
import me.atticuszambrana.atticus.util.StringUtil;

public class SayCommand extends Command {
	
	public SayCommand() {
		super("say", "Say something as the bot", Rank.DEVELOPER);
	}

	@Override
	public void execute(String[] args, MessageCreateEvent event) {
		if(args.length == 1) {
			event.getChannel().sendMessage("no");
		}
		
		ServerTextChannel c = event.getMessage().getMentionedChannels().get(0);
		
		String message = StringUtil.combine(args, 2);
		
		c.sendMessage(message);
		event.deleteMessage();
		return;
	}

}
