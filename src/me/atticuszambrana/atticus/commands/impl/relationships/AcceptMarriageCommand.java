package me.atticuszambrana.atticus.commands.impl.relationships;

import java.awt.Color;
import java.sql.SQLException;
import java.util.Map;

import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.event.message.MessageCreateEvent;

import me.atticuszambrana.atticus.commands.Command;
import me.atticuszambrana.atticus.manager.PluginManager;
import me.atticuszambrana.atticus.permissions.Rank;
import me.atticuszambrana.atticus.relationships.MarriageProposal;
import me.atticuszambrana.atticus.relationships.Relationships;

public class AcceptMarriageCommand extends Command {
	
	public AcceptMarriageCommand() {
		super("acceptmarriage", "Accept your latest proposal", Rank.NONE);
	}

	@Override
	public void execute(String[] args, MessageCreateEvent event) {
		Relationships rel = (Relationships) PluginManager.getPlugin(5);
		MarriageProposal proposal = null;
		int id = 0;
		// Check to make sure you have a proposal
		for(Map.Entry<Integer, MarriageProposal> ent : rel.getProposals().entrySet()) {
			MarriageProposal pro = ent.getValue();
			id = ent.getKey();
			if(event.getMessageAuthor().asUser().get().getId() == pro.getUser2().getId()) {
				proposal = pro;
			}
		}
		
		if(proposal == null) {
			EmbedBuilder err = new EmbedBuilder();
			err.setColor(Color.RED);
			err.setTitle("Oh no!");
			err.setDescription("I was unable to find any active proposals for you!");
			event.getChannel().sendMessage(err);
			return;
		}
		
		// Call upon the API to actually make these 2 people married
		try {
			rel.marry(proposal, event.getServer().get());
		} catch (SQLException e) {
			e.printStackTrace();
			EmbedBuilder err = new EmbedBuilder();
			err.setColor(Color.RED);
			err.setTitle("Something bad happened!");
			err.setDescription("There was a problem while communicating with the database! Try again later!");
			event.getChannel().sendMessage(err);
			return;
		}
		
		// Then also delete the proposal from the system
		rel.killProposal(id);
		
		EmbedBuilder embed = new EmbedBuilder();
		embed.setColor(Color.PINK);
		embed.setTitle("Congratulations!");
		embed.setDescription(proposal.getUser1().getName() + " and " + proposal.getUser2().getName() + " just got married! Congratulations!!");
		
		event.getChannel().sendMessage(embed);
		return;
	}

}
