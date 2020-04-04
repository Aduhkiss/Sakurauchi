package me.atticuszambrana.atticus.commands.impl.subscripton;

import java.awt.Color;
import java.sql.SQLException;
import java.util.UUID;

import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.event.message.MessageCreateEvent;

import me.atticuszambrana.atticus.commands.Command;
import me.atticuszambrana.atticus.manager.PluginManager;
import me.atticuszambrana.atticus.permissions.Rank;
import me.atticuszambrana.atticus.token.Tokens;
import me.atticuszambrana.atticus.util.MessageUtil;
import me.atticuszambrana.atticus.util.MessageUtil.ErrorType;

public class RedeemCommand extends Command {
	
	public RedeemCommand() {
		super("redeem", "Redeem a premium code for this server", Rank.NONE);
	}

	@Override
	public void execute(String[] args, MessageCreateEvent event) {
		if(args.length == 1) {
			event.getChannel().sendMessage(MessageUtil.message(Color.RED, "Not Enough Information", "You are required to send the token you would like to redeem."));
			return;
		}
		
		UUID token = null;
		try {
			token = UUID.fromString(args[1]);
		} catch(Exception ex) {
			ex.printStackTrace();
			event.getChannel().sendMessage(MessageUtil.message(Color.RED, "Error", "The Token you gave is invalid."));
			return;
		}
		
		Tokens tokens = (Tokens) PluginManager.getPlugin(8);
		
		try {
			if(!tokens.exists(token)) {
				event.getChannel().sendMessage(MessageUtil.message(Color.RED, "Error", "The token you gave is invalid."));
				return;
			}
			if(!tokens.isActive(token)) {
				event.getChannel().sendMessage(MessageUtil.message(Color.RED, "Error", "The token you gave has already been used."));
				return;
			}
			
			tokens.redeemToken(token, event.getServer().get());
			
			EmbedBuilder embed = new EmbedBuilder();
			embed.setColor(Color.PINK);
			embed.setTitle("Congratulations!");
			embed.setDescription("You have successfully redeemed 1 month of premium for " + event.getServer().get().getName() + "!");
			
			event.getChannel().sendMessage(embed);
			
			return;
			
			
		} catch(SQLException ex) {
			ex.printStackTrace();
			event.getChannel().sendMessage(MessageUtil.error(ErrorType.SQL_ERROR));
			return;
		}
	}

}
