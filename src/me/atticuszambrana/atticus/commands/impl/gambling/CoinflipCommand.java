package me.atticuszambrana.atticus.commands.impl.gambling;

import java.awt.Color;
import java.sql.SQLException;
import java.util.Random;

import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.event.message.MessageCreateEvent;

import me.atticuszambrana.atticus.commands.Command;
import me.atticuszambrana.atticus.manager.PluginManager;
import me.atticuszambrana.atticus.permissions.Rank;
import me.atticuszambrana.atticus.shards.TreasureShards;

public class CoinflipCommand extends Command {
	
	public CoinflipCommand() {
		super("coinflip", "Flip a coin", Rank.NONE);
	}

	@Override
	public void execute(String[] args, MessageCreateEvent event) {
		if(args.length == 1 || args.length == 2) {
			EmbedBuilder err = new EmbedBuilder();
			err.setColor(Color.RED);
			err.setTitle("You didn't provide enough information!");
			err.setDescription("You need to provide the bet amount, and the bet (heads or tails)");
			event.getChannel().sendMessage(err);
			return;
		}
		
		// Okay so we should have all the information we need
		// So lets do the thing
		
		// First lets check to make sure the bet amount they provided is a valid int
		int amount = 0;
		try {
			amount = Integer.valueOf(args[1]);
		} catch(IllegalArgumentException ex) {
			EmbedBuilder err = new EmbedBuilder();
			err.setColor(Color.RED);
			err.setTitle("That is not a number!");
			err.setDescription("The bet amount you provided was not a number.");
			event.getChannel().sendMessage(err);
			return;
		}
		
		Bet bet = null;
		
		if(args[2].equalsIgnoreCase("heads") || args[2].equalsIgnoreCase("h")) {
			bet = Bet.HEADS;
		}
		if(args[2].equalsIgnoreCase("tails") || args[2].equalsIgnoreCase("t")) {
			bet = Bet.TAILS;
		}
		
		// Grab an instance of the treasure shards system because we will need it later
		TreasureShards shards = (TreasureShards) PluginManager.getPlugin(3);
		
		// MAKE SURE YOU HAVE ENOUGH SHARDS TO ACTUALLY BET
		try {
			if(shards.getShards(event.getMessageAuthor().asUser().get()) < amount) {
				EmbedBuilder err = new EmbedBuilder();
				err.setColor(Color.RED);
				err.setTitle("You don't have enough shards!");
				err.setDescription("Sorry, but you do not have enough shards to make this bet!");
				event.getChannel().sendMessage(err);
				return;
			}
		} catch (SQLException e1) {
			e1.printStackTrace();
			EmbedBuilder err = new EmbedBuilder();
			err.setColor(Color.RED);
			err.setTitle("Oh no!");
			err.setDescription("Something happened when trying to contact the database!");
			event.getChannel().sendMessage(err);
			return;
		}
		
		Bet result = null;
		
		try {
			shards.takeShards("[ATTICUSCASINO]", event.getMessageAuthor().asUser().get(), (amount));
		} catch (SQLException e) {
			e.printStackTrace();
			EmbedBuilder err = new EmbedBuilder();
			err.setColor(Color.RED);
			err.setTitle("Something bad happened!");
			err.setDescription("There was a problem contacting the database server! Tell Atticus#6362");
			event.getChannel().sendMessage(err);
			return;
		}
		
		// Then lets flip the coin
		Random r = new Random();
		int flip = r.nextInt(2);
		if(flip == 0) { result = Bet.HEADS; }
		if(flip == 1) { result = Bet.TAILS; }
		
		// Then lets see if what the user bet, was the result
		if(bet == result) {
			
			int toGive = (amount * 2);
			
			// It was, so give them double their money
			try {
				shards.giveShards("[ATTICUSCASINO]", event.getMessageAuthor().asUser().get(), toGive);
			} catch (SQLException e) {
				e.printStackTrace();
				EmbedBuilder err = new EmbedBuilder();
				err.setColor(Color.RED);
				err.setTitle("Something bad happened!");
				err.setDescription("There was a problem contacting the database server! Tell Atticus#6362");
				event.getChannel().sendMessage(err);
				return;
			}
			
			EmbedBuilder err = new EmbedBuilder();
			err.setColor(Color.YELLOW);
			err.setTitle("You win the bet!");
			err.setDescription("You won! You have been given **" + toGive + "** treasure shards as a reward!");
			event.getChannel().sendMessage(err);
			return;
			
		} else {
			// They lost!
			
			EmbedBuilder err = new EmbedBuilder();
			err.setColor(Color.YELLOW);
			err.setTitle("Oops, looks like you lost the bet!");
			err.setDescription("Sorry but you lost the bet, and you lost **" + (amount) + "** treasure shards!");
			event.getChannel().sendMessage(err);
			return;
		}
	}
	
	public enum Bet {
		HEADS,
		TAILS,
	}

}
