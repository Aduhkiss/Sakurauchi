package me.atticuszambrana.atticus.commands.impl.children;

import java.awt.Color;
import java.sql.SQLException;

import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.entity.user.User;
import org.javacord.api.event.message.MessageCreateEvent;

import me.atticuszambrana.atticus.commands.Command;
import me.atticuszambrana.atticus.manager.PluginManager;
import me.atticuszambrana.atticus.permissions.Rank;
import me.atticuszambrana.atticus.relationships.Relationships;
import me.atticuszambrana.atticus.relationships.children.Child;
import me.atticuszambrana.atticus.util.MessageUtil;

public class KillCommand extends Command {
	
	public KillCommand() {
		super("kill", "Will literally fucking kill the child you give it", Rank.ADMIN);
	}

	@Override
	public void execute(String[] args, MessageCreateEvent event) {

		User author = event.getMessageAuthor().asUser().get();
		
		if(args.length == 1) {
			event.getChannel().sendMessage(MessageUtil.message(Color.RED, "Not enough information!", "You need to provide the ID of the child you would like to kill"));
			return;
		}
		
		int id = 0;
		
		try {
			id = Integer.valueOf(args[1]);
		} catch(IllegalArgumentException ex) {
			event.getChannel().sendMessage(MessageUtil.message(Color.RED, "Oh no!", "That was not a number! The ID of the child has to be a number! (Psst... do ^listchildren to get the ID!)"));
			return;
		}

		Relationships rel = (Relationships) PluginManager.getPlugin(5);
		
		// Grab an instance of the child that we are about to literally murder
		Child c = null;
		try {
			c = rel.getChild(id);
		} catch(SQLException ex) {
			ex.printStackTrace();
			EmbedBuilder err = new EmbedBuilder();
			err.setColor(Color.RED);
			err.setTitle("Oh no!");
			err.setDescription("There was an error while communicating with the database. Please try again later.");
			event.getChannel().sendMessage(err);
			return;
		}
		
		rel.ChildKillings.put(author, 0);
		rel.ChildToEnd.put(author, c);
		
		EmbedBuilder embed = new EmbedBuilder();
		embed.setColor(Color.PINK);
		embed.setTitle("Are you sure?");
		embed.setDescription("You are about to literally run over " + c.getName() + " with a car. ARE YOU SURE YOU WANT TO? (This action is irreversible.)");
		event.getChannel().sendMessage(embed);
		return;
	}
}
