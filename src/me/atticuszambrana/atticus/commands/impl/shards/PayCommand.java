package me.atticuszambrana.atticus.commands.impl.shards;

import java.awt.Color;
import java.sql.SQLException;

import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.entity.user.User;
import org.javacord.api.event.message.MessageCreateEvent;

import me.atticuszambrana.atticus.commands.Command;
import me.atticuszambrana.atticus.manager.PluginManager;
import me.atticuszambrana.atticus.permissions.Rank;
import me.atticuszambrana.atticus.shards.TreasureShards;

public class PayCommand extends Command {
	
	public PayCommand() {
		super("pay", "Give another user treasure shards", Rank.NONE);
	}

	@Override
	public void execute(String[] args, MessageCreateEvent event) {
		User author = event.getMessageAuthor().asUser().get();
		if(args.length == 1 || args.length == 2) {
			EmbedBuilder err = new EmbedBuilder();
			err.setColor(Color.RED);
			err.setTitle("Not enough information");
			err.setDescription("Make sure to put who you want to pay, and how much you want to pay them.");
			event.getChannel().sendMessage(err);
			return;
		}
		
		User target = event.getMessage().getMentionedUsers().get(0);
		int amount = 0;
		
		try {
			amount = Integer.valueOf(args[2]);
		} catch(IllegalArgumentException ex) {
			EmbedBuilder err = new EmbedBuilder();
			err.setColor(Color.RED);
			err.setTitle("That is not a number!");
			err.setDescription("The inputted value was not a number!");
			event.getChannel().sendMessage(err);
			return;
		}
		
		TreasureShards shards = (TreasureShards) PluginManager.getPlugin(3);
		
		// Make sure they have enough shards
		try {
			if(shards.getShards(author) < amount) {
				EmbedBuilder err = new EmbedBuilder();
				err.setColor(Color.RED);
				err.setTitle("Not Enough Shards");
				err.setDescription("You do not have enough shards to complete this transaction.");
				event.getChannel().sendMessage(err);
				return;
			}
			
			shards.takeShards(author, amount);
			shards.giveShards(target, amount);
			
			EmbedBuilder err = new EmbedBuilder();
			err.setColor(Color.GREEN);
			err.setTitle("Transaction Complete!");
			err.setDescription(author.getName() + " has just gifted " + target.getName() + " " + amount + " treasure shards!");
			event.getChannel().sendMessage(err);
			return;
			
		} catch (SQLException e) {
			e.printStackTrace();
			EmbedBuilder err = new EmbedBuilder();
			err.setColor(Color.RED);
			err.setTitle("Something bad happened!");
			err.setDescription("There was a problem while communicating with the database. Try again later!");
			event.getChannel().sendMessage(err);
			return;
		}
	}

}
