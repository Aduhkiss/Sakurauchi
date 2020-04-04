package me.atticuszambrana.atticus.commands.impl.punishment;

import java.awt.Color;

import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.user.User;
import org.javacord.api.event.message.MessageCreateEvent;

import me.atticuszambrana.atticus.commands.Command;
import me.atticuszambrana.atticus.manager.PluginManager;
import me.atticuszambrana.atticus.permissions.Rank;
import me.atticuszambrana.atticus.punishment.Punishment;
import me.atticuszambrana.atticus.util.LogUtil;
import me.atticuszambrana.atticus.util.MessageUtil;
import net.atticusllc.atticus.strings.StringUtil;

public class KickCommand extends Command {
	
	public KickCommand() {
		super("kick", "Remove a user from your server", Rank.NONE);
	}

	@Override
	public void execute(String[] args, MessageCreateEvent event) {
		User author = event.getMessageAuthor().asUser().get();
		Server server = event.getServer().get();
		
		// Permission Check
		if(!server.canKickUsers(author)) {
			event.getChannel().sendMessage(MessageUtil.message(Color.RED, "You cannot do this!", "You do not have permission to kick users on this server."));
			return;
		}
		
		if(args.length == 1) {
			event.getChannel().sendMessage(MessageUtil.message(Color.RED, "Oh no!", "Please mention the user you would like to kick."));
			return;
		}
		
		//TODO: Accept Discord User ID's as well as pinging users
		
		User target = event.getMessage().getMentionedUsers().get(0);
		
		// ^kick @Atticus Lol
		// 1     2        3
		
		// Also get the reason
		String reason = null;
		if(args.length == 2) {
			reason = "No Reason Given";
		} else {
			reason = StringUtil.combine(args, 2);
		}
		
		LogUtil.debug("Punishments", "Set Reason to: " + reason);
		
		// Check if the user you are trying to punish, can be punished by you
		if(!server.canKickUser(author, target)) {
			event.getChannel().sendMessage(MessageUtil.message(Color.RED, "Oh no!", "You cannot punish members that are a higher role then you."));
			return;
		}
		
		// Talk to the punishment system to get this handled
		Punishment p = (Punishment) PluginManager.getPlugin(7);
		
		if(p.kick(author, target, reason, server)) {
			//event.getMessage().delete();
			event.getChannel().sendMessage(MessageUtil.message(Color.CYAN, "Success", author.getName() + " has kicked " + target.getName() + "!"));
			return;
		}
		else {
			event.getChannel().sendMessage(MessageUtil.message(Color.RED, "Oh no!", "Something went wrong while trying to punish this user. Please try again later."));
			return;
		}
	}

}
