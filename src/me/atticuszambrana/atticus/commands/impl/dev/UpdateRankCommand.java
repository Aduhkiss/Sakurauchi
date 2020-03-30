package me.atticuszambrana.atticus.commands.impl.dev;

import java.awt.Color;
import java.sql.SQLException;

import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.entity.user.User;
import org.javacord.api.event.message.MessageCreateEvent;

import me.atticuszambrana.atticus.Start;
import me.atticuszambrana.atticus.commands.Command;
import me.atticuszambrana.atticus.database.Database;
import me.atticuszambrana.atticus.permissions.Rank;
import me.atticuszambrana.atticus.util.LogUtil;

public class UpdateRankCommand extends Command {
	
	public UpdateRankCommand() {
		super("updaterank", "Update the rank of the given user in the bots database", Rank.ADMIN);
	}

	@Override
	public void execute(String[] args, MessageCreateEvent event) {
		if(args.length != 3) {
			EmbedBuilder err = new EmbedBuilder();
			err.setColor(Color.RED);
			err.setTitle("Error");
			err.setDescription("You provided too many, or not enough arguments for this command.");
			event.getChannel().sendMessage(err);
			return;
		}
		
		// First, lets get the arguments that the user passed us, then save them to their own variables
		User target = event.getMessage().getMentionedUsers().get(0);
		//String rank = args[2];
		
		// Then make sure this rank exists
		Rank testFor;
		try {
			testFor = Rank.valueOf(args[2]);
		} catch(IllegalArgumentException ex) {
			EmbedBuilder err = new EmbedBuilder();
			err.setColor(Color.RED);
			err.setTitle("Error");
			err.setDescription("You gave an invalid rank name. Please check with a bot developer for rank names.");
			event.getChannel().sendMessage(err);
			return;
		}
		
		// Make sure that the person granting this rank, cannot grant a rank higher then their own
		Rank myRank = Start.getPermManager().getRank(event.getMessageAuthor().asUser().get());
		
		if(testFor.getPower() > myRank.getPower()) {
			EmbedBuilder error = new EmbedBuilder();
			error.setColor(Color.RED);
			error.setTitle("You cannot grant this rank!");
			error.setDescription("You cannot grant a rank that is higher then your own!");
			event.getChannel().sendMessage(error);
			return;
		}
		
		// Then lets try pushing this to the database and sending a success message
		try {
			
			Database db = Database.get();
			// Delete their current rank...
			db.getConnection().createStatement().executeUpdate("DELETE FROM `NetworkRanks` WHERE `USER_ID` = '" + target.getIdAsString() + "';");
			
			// Then apply the new one
			db.getConnection().createStatement().executeUpdate("INSERT INTO `NetworkRanks` (`ID`, `USER_ID`, `RANK`) VALUES (NULL, '" + target.getIdAsString() + "', '" + testFor.toString().toUpperCase() + "');");
			
			EmbedBuilder embed = new EmbedBuilder();
			embed.setColor(Color.CYAN);
			embed.setTitle("Success!");
			embed.setDescription("You have updated " + target.getName() + "'s rank to " + testFor.getName() + "!");
			
			event.getChannel().sendMessage(embed);
		} catch(SQLException ex) {
			EmbedBuilder err = new EmbedBuilder();
			err.setColor(Color.RED);
			err.setTitle("Error");
			err.setDescription("A database error occured when we tried to update this rank. Please notify a bot developer immediately.");
			event.getChannel().sendMessage(err);
			LogUtil.info("Database", "There was an error while trying to update account data: " + ex.getMessage());
			ex.printStackTrace();
		}
		
		// Refresh the permissions matrix thingy
		Start.getPermManager().setupPermissions();
		
		return;
	}
	
	

}
