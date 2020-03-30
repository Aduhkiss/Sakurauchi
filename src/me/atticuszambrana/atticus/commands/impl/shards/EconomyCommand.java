package me.atticuszambrana.atticus.commands.impl.shards;

import java.awt.Color;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.event.message.MessageCreateEvent;

import me.atticuszambrana.atticus.commands.Command;
import me.atticuszambrana.atticus.database.Database;
import me.atticuszambrana.atticus.permissions.Rank;

public class EconomyCommand extends Command {
	
	public EconomyCommand() {
		super("economy", "View how much money is currently in the economy", Rank.ADMIN);
	}

	@Override
	public void execute(String[] args, MessageCreateEvent event) {
		Connection conn = Database.get().getConnection();
		try {
			int money = 0;
			
			ResultSet result = conn.createStatement().executeQuery("SELECT * FROM `Shards`;");
			
			while(result.next()) {
				money = money + result.getInt("shard_count");
			}
			
			EmbedBuilder embed = new EmbedBuilder();
			embed.setColor(Color.GREEN);
			embed.setTitle("Success");
			embed.setDescription("There are currently " + money + " treasure shards in the economy.");
			
			event.getChannel().sendMessage(embed);
			return;
			
		} catch(SQLException ex) {
			EmbedBuilder err = new EmbedBuilder();
			err.setColor(Color.RED);
			err.setTitle("Something bad happened!");
			err.setDescription("There was a problem while communicating with the database! Try again later!");
			event.getChannel().sendMessage(err);
			return;
		}
	}

}
