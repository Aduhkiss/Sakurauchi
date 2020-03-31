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
import me.atticuszambrana.atticus.shards.TreasureShards;

public class ProcreateCommand extends Command {
	
	public ProcreateCommand() {
		//TODO: Change the required rank from Admin to none, when the command is finished
		super("procreate", "Make a child with your spouse", Rank.NONE);
	}

	@Override
	public void execute(String[] args, MessageCreateEvent event) {
		Relationships rel = (Relationships) PluginManager.getPlugin(5);
		TreasureShards shards = (TreasureShards) PluginManager.getPlugin(3);
		
		User author = event.getMessageAuthor().asUser().get();
		
		try {
			if(!rel.isMarried(author, event.getServer().get())) {
				EmbedBuilder err = new EmbedBuilder();
				err.setColor(Color.RED);
				err.setTitle("Oh no!");
				err.setDescription("You need to be married to someone to have children!");
				event.getChannel().sendMessage(err);
				return;
			}
			
			if(shards.getShards(author) < 50) {
				EmbedBuilder err = new EmbedBuilder();
				err.setColor(Color.RED);
				err.setTitle("Oh no!");
				err.setDescription("You don't have enough shards to have a child! You require 50!");
				event.getChannel().sendMessage(err);
				return;
			}
			
			EmbedBuilder embed = new EmbedBuilder();
			embed.setColor(Color.PINK);
			embed.setTitle("You want to have a child?");
			embed.setDescription("Are you sure you want to have a child? It costs 50 treasure shards. Type either Yes, or No in the chat.");
			event.getChannel().sendMessage(embed);
			
			// Do something
			rel.ChildSetup.put(author, 0);
			
			return;
		} catch(SQLException ex) {
			ex.printStackTrace();
			EmbedBuilder err = new EmbedBuilder();
			err.setColor(Color.RED);
			err.setTitle("Oh no!");
			err.setDescription("There was an error while communicating with the database. Please try again later.");
			event.getChannel().sendMessage(err);
			return;
		}
	}

}
