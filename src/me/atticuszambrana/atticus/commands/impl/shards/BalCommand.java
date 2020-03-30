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

public class BalCommand extends Command {
	
	public BalCommand() {
		super("bal", "View a user's current balance", Rank.NONE);
	}

	@Override
	public void execute(String[] args, MessageCreateEvent event) {
		if(args.length == 1) {
			
			User author = event.getMessageAuthor().asUser().get();
			
			EmbedBuilder embed = new EmbedBuilder();
			embed.setColor(Color.BLUE);
			embed.setTitle(author.getName() + "'s Balance");
			
			// Get the count
			TreasureShards shards = (TreasureShards) PluginManager.getPlugin(3);
			
			int amount = 0;
			try {
				amount = shards.getShards(author);
			} catch(SQLException ex) {
				ex.printStackTrace();
				EmbedBuilder err = new EmbedBuilder();
				err.setColor(Color.RED);
				err.setTitle("Oops! Something bad happened!");
				err.setDescription("Please forward this message to Atticus#6362: " + ex.getMessage() + "===== [TreasShard-BalComm]");
				event.getChannel().sendMessage(err);
				return;
			}
			
			embed.setDescription(author.getName() + " currently has **" + amount + "** treasure shards.");
			
			event.getChannel().sendMessage(embed);
			return;
			
		} else {
			// idk
			User author = event.getMessage().getMentionedUsers().get(0);
			
			EmbedBuilder embed = new EmbedBuilder();
			embed.setColor(Color.BLUE);
			embed.setTitle(author.getName() + "'s Balance");
			
			// Get the count
			TreasureShards shards = (TreasureShards) PluginManager.getPlugin(3);
			
			int amount = 0;
			try {
				amount = shards.getShards(author);
			} catch(SQLException ex) {
				ex.printStackTrace();
				EmbedBuilder err = new EmbedBuilder();
				err.setColor(Color.RED);
				err.setTitle("Oops! Something bad happened!");
				err.setDescription("Please forward this message to Atticus#6362: " + ex.getMessage() + "===== [TreasShard-BalComm]");
				event.getChannel().sendMessage(err);
				return;
			}
			
			embed.setDescription(author.getName() + " currently has **" + amount + "** treasure shards.");
			
			event.getChannel().sendMessage(embed);
			return;
		}
	}

}
