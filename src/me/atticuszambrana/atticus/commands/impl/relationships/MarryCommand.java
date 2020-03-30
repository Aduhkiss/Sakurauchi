package me.atticuszambrana.atticus.commands.impl.relationships;

import java.awt.Color;
import java.sql.SQLException;
import java.util.Map;

import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.entity.user.User;
import org.javacord.api.event.message.MessageCreateEvent;

import me.atticuszambrana.atticus.commands.Command;
import me.atticuszambrana.atticus.manager.PluginManager;
import me.atticuszambrana.atticus.permissions.Rank;
import me.atticuszambrana.atticus.relationships.MarriageProposal;
import me.atticuszambrana.atticus.relationships.Relationships;

public class MarryCommand extends Command {
	
	public MarryCommand() {
		super("marry", "Propose to marry another user on this server", Rank.NONE);
	}

	@Override
	public void execute(String[] args, MessageCreateEvent event) {
		User author = event.getMessageAuthor().asUser().get();
		if(args.length == 1) {
			EmbedBuilder err = new EmbedBuilder();
			err.setColor(Color.RED);
			err.setTitle("Not enough information");
			err.setDescription("You need to mention someone to propose to!");
			event.getChannel().sendMessage(err);
			return;
		}
		User target = event.getMessage().getMentionedUsers().get(0);
		
		// Make sure this person isnt trying to marry themselfs
		if(target.getId() == author.getId()) {
			EmbedBuilder err = new EmbedBuilder();
			err.setColor(Color.RED);
			err.setTitle("Uhh.. Why?");
			err.setDescription("Unfortunately, you are unable to marry yourself. Sorry, you actually have to ask someone else to marry you lol.");
			event.getChannel().sendMessage(err);
			return;
		}
		
		Relationships rel = (Relationships) PluginManager.getPlugin(5);
		try {
			
			// Make sure, that if there is already an active proposal, we don't make another one
			for(Map.Entry<Integer, MarriageProposal> pro : rel.getProposals().entrySet()) {
				if(pro.getValue().getUser1().getId() == author.getId()) {
					EmbedBuilder err = new EmbedBuilder();
					err.setColor(Color.RED);
					err.setTitle("Oh no!");
					err.setDescription("It looks like you already have an active proposal with " + pro.getValue().getUser2().getName() + "!");
					event.getChannel().sendMessage(err);
					return;
				}
			}
			
			// Check if the user is already married on this server
			if(rel.isMarried(author, event.getServer().get())) {
				EmbedBuilder err = new EmbedBuilder();
				err.setColor(Color.RED);
				err.setTitle("Oh no!");
				err.setDescription("It looks like you are already married in this server!");
				event.getChannel().sendMessage(err);
				return;
			}
			
			// Also make sure the other person isn't already married
			if(rel.isMarried(target, event.getServer().get())) {
				EmbedBuilder err = new EmbedBuilder();
				err.setColor(Color.RED);
				err.setTitle("Oh no!");
				err.setDescription("It looks like " + target.getName() + " is already married!");
				event.getChannel().sendMessage(err);
				return;
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
			EmbedBuilder err = new EmbedBuilder();
			err.setColor(Color.RED);
			err.setTitle("Something bad happened!");
			err.setDescription("There was a problem contacting the database server! Please try again later.");
			event.getChannel().sendMessage(err);
			return;
		}
		
		
		EmbedBuilder embed = new EmbedBuilder();
		embed.setColor(Color.pink);
		embed.setTitle(author.getName() + " has proposed to " + target.getName() + "!");
		
		embed.setDescription(author.getName() + " has just asked " + target.getName() + " to marry them! I wonder what they will say...");
		embed.addField("To Accept", "Run ^acceptmarriage");
		embed.addField("To Deny", "Run ^denymarriage");
		
		event.getChannel().sendMessage(embed);
		
		// Add the marriage proposal into the system
		rel.createProposal(new MarriageProposal(author, target, event.getServer().get()));
		return;
	}

}
