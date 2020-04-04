package me.atticuszambrana.atticus.commands.impl.punishment;

import java.awt.Color;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.user.User;
import org.javacord.api.event.message.MessageCreateEvent;

import me.atticuszambrana.atticus.commands.Command;
import me.atticuszambrana.atticus.database.Database;
import me.atticuszambrana.atticus.permissions.Rank;
import me.atticuszambrana.atticus.util.MessageUtil;

public class ListKicksCommand extends Command {
	
	public ListKicksCommand() {
		super("listkicks", "Display all kicks for a user", Rank.NONE);
	}

	@Override
	public void execute(String[] args, MessageCreateEvent event) {
		User author = event.getMessageAuthor().asUser().get();
		Server server = event.getServer().get();
		
		// Permission Check
		if(!server.canKickUsers(author)) {
			event.getChannel().sendMessage(MessageUtil.message(Color.RED, "You cannot do this!", "You require kick user permissions to run this."));
			return;
		}
		
		if(args.length == 1) {
			event.getChannel().sendMessage(MessageUtil.message(Color.RED, "Oh no!", "Please mention the user you would like to see."));
			return;
		}
		
		//TODO: Accept Discord User ID's as well as pinging users
		
		User target = event.getMessage().getMentionedUsers().get(0);
		
		Thread t = new Thread() {
			public void run() {
				try {
					Connection conn = Database.get().getConnection();
					boolean found = false;
					ResultSet result = conn.createStatement().executeQuery("SELECT * FROM `Punish_Kicks` WHERE `target` = '" + target.getId() + "' AND `server_id` = '" + server.getId() + "';");
					
					while(result.next()) {
						found = true;
						EmbedBuilder e = new EmbedBuilder();
						User t = server.getMemberById(result.getLong("target")).get();
						User m = server.getMemberById(result.getLong("caller")).get();
						e.setColor(Color.RED);
						e.setTitle("Punishment ID: " + result.getInt("id"));
						e.addInlineField("Target", t.getName());
						e.addInlineField("Moderator", m.getName());
						e.addInlineField("Reason", result.getString("reason"));
						// Has a lot of bugs
						//AtticusTime time = new AtticusTime(result.getLong("timestamp"));
						//e.addInlineField("Time", time.getFullFromDate());
						
						event.getChannel().sendMessage(e);
					}
					
					if(!found) {
						EmbedBuilder err = new EmbedBuilder();
						err.setColor(Color.RED);
						err.setTitle("None Found!");
						err.setDescription(target.getName() + " has no kicks on this server!");
						event.getChannel().sendMessage(err);
						return;
					}
					return;
				} catch(SQLException ex) {
					ex.printStackTrace();
				}
			}
		};
		t.start();
	}

}
