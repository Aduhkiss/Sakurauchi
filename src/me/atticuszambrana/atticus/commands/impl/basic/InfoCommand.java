package me.atticuszambrana.atticus.commands.impl.basic;

import java.awt.Color;

import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.event.message.MessageCreateEvent;

import me.atticuszambrana.atticus.Start;
import me.atticuszambrana.atticus.commands.Command;
import me.atticuszambrana.atticus.permissions.Rank;

public class InfoCommand extends Command {
	
	public InfoCommand() {
		super("info", "Display information about the bot.", Rank.NONE);
	}

	@Override
	public void execute(String[] args, MessageCreateEvent event) {
		EmbedBuilder embed = new EmbedBuilder();
		
		embed.setTitle("Bot Information");
		embed.setColor(Color.YELLOW);
		
		embed.addField("Version", Start.getBuild());
		
		event.getChannel().sendMessage(embed);
		return;
	}

}
