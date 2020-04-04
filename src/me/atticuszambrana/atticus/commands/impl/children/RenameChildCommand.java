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
import me.atticuszambrana.atticus.shards.TreasureShards;
import me.atticuszambrana.atticus.util.MessageUtil;

public class RenameChildCommand extends Command {
	public RenameChildCommand() {
		super("renamechild", "Rename one of your children, costs 150 shards.", Rank.NONE);
	}

	@Override
	public void execute(String[] args, MessageCreateEvent event) {
		User author = event.getMessageAuthor().asUser().get();
		
		if(args.length == 1 || args.length == 2) {
			event.getChannel().sendMessage(MessageUtil.message(Color.RED, "Not enough information!", "You need to provide the ID of the child you would like to rename, and their new name! (Psst... do ^listchildren to get the ID!)"));
			return;
		}
		
		int id = 0;
		String name = args[2];
		
		try {
			id = Integer.valueOf(args[1]);
		} catch(IllegalArgumentException ex) {
			event.getChannel().sendMessage(MessageUtil.message(Color.RED, "Oh no!", "That was not a number! The ID of the child has to be a number! (Psst... do ^listchildren to get the ID!)"));
			return;
		}

		Relationships rel = (Relationships) PluginManager.getPlugin(5);
		TreasureShards shards = (TreasureShards) PluginManager.getPlugin(3);
		
		// Bug fix
		if(name.indexOf("'") >= 0) {
			event.getChannel().sendMessage(MessageUtil.message(Color.RED, "Oh no!", "It looks like that name has an illegal character. We only allow the use of letters and numbers."));
			return;
		}
		
		try {
			
			// Ask the relationships API to pull the child's information from the database
			// And make sure the message author actually is a parent of the child LMAO
			// IMAGINE IF I FORGOT THIS PART!
			
			Child c = rel.getChild(id);
			
			if(c.getParentOne().getId() == author.getId() || c.getParentTwo().getId() == author.getId()) {
				
				// Complete the transaction
				if(shards.getShards(event.getMessageAuthor().asUser().get()) < 150) {
					event.getChannel().sendMessage(MessageUtil.message(Color.RED, "Not Enough Treasure Shards!", "You do not have enough treasure shards to complete this transaction. You require 150!"));
					return;
				}
				
				// If they have enough, then take 150
				shards.takeShards("[CHILDMGR]", event.getMessageAuthor().asUser().get(), 150);
				
				// Then actually rename the child with the Relationships API
				rel.updateChildName(rel.getChild(id), name);
				
				// Show a message to tell the user that their child was successfully renamed
				
				EmbedBuilder embed = new EmbedBuilder();
				embed.setColor(Color.GREEN);
				embed.setTitle("Success!");
				embed.setDescription(c.getName() + "'s name has been switched to " + name + "!");
				
				event.getChannel().sendMessage(embed);
				return;
				
			} else {
				event.getChannel().sendMessage(MessageUtil.message(Color.RED, "Oh no!", "You are not one of " + c.getName() + "'s parents!"));
				return;
			}
		} catch(SQLException ex) {
			ex.printStackTrace();
			event.getChannel().sendMessage(MessageUtil.message(Color.RED, "Oh no!", "There was an error while communicating with the database. Please try again later."));
			return;
		}
	}
}
