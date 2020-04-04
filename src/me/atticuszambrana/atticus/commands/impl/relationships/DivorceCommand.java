package me.atticuszambrana.atticus.commands.impl.relationships;

import java.awt.Color;
import java.sql.SQLException;

import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.user.User;
import org.javacord.api.entity.user.UserStatus;
import org.javacord.api.event.message.MessageCreateEvent;

import me.atticuszambrana.atticus.commands.Command;
import me.atticuszambrana.atticus.manager.PluginManager;
import me.atticuszambrana.atticus.permissions.Rank;
import me.atticuszambrana.atticus.relationships.Marriage;
import me.atticuszambrana.atticus.relationships.Relationships;
import me.atticuszambrana.atticus.util.MessageUtil;

public class DivorceCommand extends Command {
	
	public DivorceCommand() {
		super("divorce", "Leave your current partner", Rank.NONE); 
	}

	@Override
	public void execute(String[] args, MessageCreateEvent event) {
		
		Relationships rel = (Relationships) PluginManager.getPlugin(5);
		User author = event.getMessageAuthor().asUser().get();
		Server server = event.getServer().get();
		
		try {
			if(!rel.isMarried(event.getMessageAuthor().asUser().get(), event.getServer().get())) {
				// Not married idiot
				EmbedBuilder err = new EmbedBuilder();
				err.setColor(Color.RED);
				err.setTitle("Oh no!");
				err.setDescription("It looks like you are not married to anyone!");
				event.getChannel().sendMessage(err);
				return;
			}
			
			Marriage m = rel.getMarriage(event.getMessageAuthor().asUser().get(), event.getServer().get());
			
			EmbedBuilder embed = new EmbedBuilder();
			embed.setColor(Color.RED);
			embed.setTitle(event.getMessageAuthor().getName() + " just divorced their partner!");
			
			embed.setDescription("It looks like " + m.getSpouseOne().getName() + " and " + m.getSpouseTwo().getName() + "'s Marriage has come to an end...");
			event.getChannel().sendMessage(embed);
			
			// If the partner is offline, then DM them to let them know their spouse has left them
			User partner = m.getOtherSpouse(author);
			
			if(partner.getDesktopStatus() == UserStatus.OFFLINE) {
				partner.sendMessage(MessageUtil.message(Color.RED, "I'm Sorry", "It looks like your spouse on " + server.getName() + ", " + author.getName() + " has decided to divorce you."));
			}
			
			rel.deleteMarriage(m);
			return;
			
		} catch(SQLException ex) {
			ex.printStackTrace();
			EmbedBuilder err = new EmbedBuilder();
			err.setColor(Color.RED);
			err.setTitle("Something bad happened!");
			err.setDescription("There was a problem while communicating with the database! Try again later!");
			event.getChannel().sendMessage(err);
			return;
		}
	}

}
