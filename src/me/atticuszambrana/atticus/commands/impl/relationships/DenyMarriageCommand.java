package me.atticuszambrana.atticus.commands.impl.relationships;

import java.awt.Color;
import java.util.Map;

import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.event.message.MessageCreateEvent;

import me.atticuszambrana.atticus.commands.Command;
import me.atticuszambrana.atticus.manager.PluginManager;
import me.atticuszambrana.atticus.permissions.Rank;
import me.atticuszambrana.atticus.relationships.MarriageProposal;
import me.atticuszambrana.atticus.relationships.Relationships;

public class DenyMarriageCommand extends Command {
	
	public DenyMarriageCommand() {
		super("denymarriage", "Deny your latest proposal", Rank.NONE);
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
		
		// Then also delete the proposal from the system
		rel.killProposal(id);
		
		EmbedBuilder embed = new EmbedBuilder();
		embed.setColor(Color.RED);
		embed.setTitle(proposal.getUser1().getName() + "'s Proposal was denied!");
		embed.setDescription("Sorry, " + proposal.getUser1().getName() + ", but " + proposal.getUser2().getName() + " doesn't want to marry you.");
		
		event.getChannel().sendMessage(embed);
		return;
	}

}
