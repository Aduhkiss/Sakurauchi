package me.atticuszambrana.atticus.commands.impl.punishment;

import java.awt.Color;
import java.sql.SQLException;

import org.javacord.api.entity.channel.ServerTextChannel;
import org.javacord.api.entity.permission.PermissionType;
import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.user.User;
import org.javacord.api.event.message.MessageCreateEvent;

import me.atticuszambrana.atticus.commands.Command;
import me.atticuszambrana.atticus.manager.PluginManager;
import me.atticuszambrana.atticus.permissions.Rank;
import me.atticuszambrana.atticus.punishment.ModLog;
import me.atticuszambrana.atticus.util.MessageUtil;
import me.atticuszambrana.atticus.util.MessageUtil.ErrorType;

public class ModLogCommand extends Command {
	
	public ModLogCommand() {
		super("modlog", "Setup a modlog channel", Rank.NONE);
	}

	@Override
	public void execute(String[] args, MessageCreateEvent event) {
		User author = event.getMessageAuthor().asUser().get();
		Server server = event.getServer().get();
		ModLog modlog = (ModLog) PluginManager.getPlugin(9);
		
		if(!server.hasPermission(author, PermissionType.MANAGE_SERVER)) {
			event.getChannel().sendMessage(MessageUtil.message(Color.RED, "You cannot do this!", "You do not have permission to run this command."));
			return;
		}
		
		try {
			
			if(args.length == 1) {
				
				if(!modlog.hasModLog(server)) {
					event.getChannel().sendMessage(MessageUtil.message(Color.RED, "No Modlog Channel has been set", "You do not have a modlog channel setup."));
					return;
				} else {
					ServerTextChannel channel = modlog.getModLog(server);
					event.getChannel().sendMessage(MessageUtil.message(Color.GREEN, "Current Modlog Channel", "All Punishment Notifications are currently sent to: " + channel.getName()));
					return;
				}
			}
			
			ServerTextChannel channel = null;
			try {
				channel = event.getMessage().getMentionedChannels().get(0);
			} catch(IndexOutOfBoundsException ex) {
				event.getChannel().sendMessage(MessageUtil.message(Color.RED, "Please Mention a channel", "You are required to mention the channel that you would like to set."));
			}
			
			modlog.setModLog(server, channel);
			
			event.getChannel().sendMessage(MessageUtil.message(Color.GREEN, "Success!", "All Punishment Notifications will now be sent to: " + channel.getName()));
			return;
			
		} catch(SQLException ex) {
			ex.printStackTrace();
			event.getChannel().sendMessage(MessageUtil.error(ErrorType.SQL_ERROR));
			return;
		}
	}

}
