package me.atticuszambrana.atticus.commands.impl.dev;

import java.awt.Color;

import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.event.message.MessageCreateEvent;

import me.atticuszambrana.atticus.Start;
import me.atticuszambrana.atticus.commands.Command;
import me.atticuszambrana.atticus.permissions.Rank;

public class UserInfoCommand extends Command {
	
	public UserInfoCommand() {
		super("userinfo", "Display basic information about your account", Rank.NONE);
	}

	@Override
	public void execute(String[] args, MessageCreateEvent event) {
		
		EmbedBuilder embed = new EmbedBuilder();
		
		embed.setColor(Color.BLUE);
		embed.setTitle("Your account info");
		embed.addField("Name", event.getMessageAuthor().getName());
		embed.addField("Rank", Start.getPermManager().getRank(event.getMessageAuthor().asUser().get()).getName());
		embed.addField("Bot Developer", "Atticus#6362");
		
		event.getChannel().sendMessage(embed);
		return;
	}

}
