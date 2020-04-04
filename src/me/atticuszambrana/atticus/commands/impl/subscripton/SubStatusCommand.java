package me.atticuszambrana.atticus.commands.impl.subscripton;

import java.awt.Color;
import java.sql.SQLException;

import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.event.message.MessageCreateEvent;

import me.atticuszambrana.atticus.commands.Command;
import me.atticuszambrana.atticus.manager.PluginManager;
import me.atticuszambrana.atticus.permissions.Rank;
import me.atticuszambrana.atticus.subscriptions.Subscription;
import me.atticuszambrana.atticus.util.MessageUtil;
import me.atticuszambrana.atticus.util.MessageUtil.ErrorType;

public class SubStatusCommand extends Command {
	
	public SubStatusCommand() {
		super("substatus", "Display the current subscription status of the current server.", Rank.NONE);
	}

	@Override
	public void execute(String[] args, MessageCreateEvent event) {
		Subscription ss = (Subscription) PluginManager.getPlugin(6);
		
		try {
			boolean sub = ss.hasPremium(event.getServer().get());
			EmbedBuilder embed = new EmbedBuilder();
			
			embed.setTitle(event.getServer().get().getName() + "'s Premium Status");
			
			if(sub) {
				embed.setColor(Color.GREEN);
			} else {
				embed.setColor(Color.RED);
			}
			
			if(sub) {
				embed.addInlineField("Has Premium", "Yes");
			} else {
				embed.addInlineField("Has Premium", "No");
			}
			
			event.getChannel().sendMessage(embed);
			return;
			
		} catch(SQLException ex) {
			ex.printStackTrace();
			event.getChannel().sendMessage(MessageUtil.error(ErrorType.SQL_ERROR));
		}
		
	}

}
