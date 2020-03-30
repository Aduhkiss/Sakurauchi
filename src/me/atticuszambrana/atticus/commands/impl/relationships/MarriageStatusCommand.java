package me.atticuszambrana.atticus.commands.impl.relationships;

import java.awt.Color;
import java.sql.SQLException;

import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.entity.user.User;
import org.javacord.api.event.message.MessageCreateEvent;

import me.atticuszambrana.atticus.commands.Command;
import me.atticuszambrana.atticus.manager.PluginManager;
import me.atticuszambrana.atticus.permissions.Rank;
import me.atticuszambrana.atticus.relationships.Marriage;
import me.atticuszambrana.atticus.relationships.Relationships;

public class MarriageStatusCommand extends Command {
	
	public MarriageStatusCommand() {
		super("marriagestatus", "View your current marriage status", Rank.NONE);
	}

	@Override
	public void execute(String[] args, MessageCreateEvent event) {
		User t = null;
		
		if(args.length == 1) {
			t = event.getMessageAuthor().asUser().get();
		} else {
			t = event.getMessage().getMentionedUsers().get(0);
		}
		
		Relationships rel = (Relationships) PluginManager.getPlugin(5);
		
		try {
			
			if(rel.isMarried(t, event.getServer().get())) {
				
				Marriage m = rel.getMarriage(t, event.getServer().get());
				
				EmbedBuilder embed = new EmbedBuilder();
				embed.setColor(Color.PINK);
				embed.setTitle(t.getName() + "'s Marriage Status");
				
				embed.addField("Married?", "Yes");
				embed.addField("Spouse One", m.getSpouseOne().getName());
				embed.addField("Spouse Two", m.getSpouseTwo().getName());
				
				event.getChannel().sendMessage(embed);
				return;
				
			} else {
				EmbedBuilder embed = new EmbedBuilder();
				embed.setColor(Color.RED);
				embed.setTitle(t.getName() + "'s Marriage Status");
				
				embed.setDescription(t.getName() + " isn't married to anyone in this server.");
				
				event.getChannel().sendMessage(embed);
				return;
			}
			
		} catch(SQLException ex) {
			ex.printStackTrace();
			EmbedBuilder err = new EmbedBuilder();
			err.setColor(Color.RED);
			err.setTitle("Something bad happened!");
			err.setDescription("There was a problem contacting the database server! Please try again later.");
			event.getChannel().sendMessage(err);
			return;
		}
	}
}
