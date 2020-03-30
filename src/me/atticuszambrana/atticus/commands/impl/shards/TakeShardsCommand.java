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

public class TakeShardsCommand extends Command {
	
	public TakeShardsCommand() {
		super("takeshards", "Take shards from a user's account", Rank.ADMIN);
	}

	@Override
	public void execute(String[] args, MessageCreateEvent event) {
		if(args.length == 1 && args.length == 2) {
			EmbedBuilder err = new EmbedBuilder();
			err.setColor(Color.RED);
			err.setDescription("You are required to mention a user, and an amount to take.");
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
		
		// Do the thing
		TreasureShards shards = (TreasureShards) PluginManager.getPlugin(3);
		
		try {
			shards.takeShards(target, amount);
		} catch(SQLException ex) {
			EmbedBuilder err = new EmbedBuilder();
			err.setColor(Color.RED);
			err.setTitle("Eww. something went wrong!");
			err.setDescription("There was an error while communicating with the SQL Server. Tell Atticus: ===== [TreasShard-TakeShrdComm]");
			event.getChannel().sendMessage(err);
			return;
		}
		
		EmbedBuilder embed = new EmbedBuilder();
		
		embed.setColor(Color.GREEN);
		embed.setTitle("Transaction Complete");
		embed.setDescription(amount + " treasure shards have been successfully taken from " + target.getName() + "'s account.");
		
		event.getChannel().sendMessage(embed);
		return;
	}

}
