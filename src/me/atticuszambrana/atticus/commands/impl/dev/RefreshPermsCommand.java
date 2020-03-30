package me.atticuszambrana.atticus.commands.impl.dev;

import java.awt.Color;

import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.event.message.MessageCreateEvent;

import me.atticuszambrana.atticus.Start;
import me.atticuszambrana.atticus.commands.Command;
import me.atticuszambrana.atticus.permissions.Rank;
import me.atticuszambrana.atticus.util.LogUtil;

public class RefreshPermsCommand extends Command {
	
	public RefreshPermsCommand() {
		super("refreshperms", "Refresh Permissions Matrix from the database", Rank.DEVELOPER);
	}

	@Override
	public void execute(String[] args, MessageCreateEvent event) {
		// Ok
		LogUtil.info("Permissions", "Refreshing Permissions Matrix due to command from " + event.getMessageAuthor().getName() + ".");
		Start.getPermManager().setupPermissions();
		
		EmbedBuilder embed = new EmbedBuilder();
		
		embed.setColor(Color.GREEN);
		embed.setTitle("Success!");
		embed.setDescription("You have successfully refreshed the Permissions Matrix! This action has been logged.");
		
		event.getChannel().sendMessage(embed);
		
		return;
	}

}
