
package me.atticuszambrana.atticus.manager;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.event.message.MessageCreateEvent;
import org.javacord.api.listener.message.MessageCreateListener;

import me.atticuszambrana.atticus.Start;
import me.atticuszambrana.atticus.commands.Command;
import me.atticuszambrana.atticus.commands.impl.basic.HelpCommand;
import me.atticuszambrana.atticus.commands.impl.basic.InfoCommand;
import me.atticuszambrana.atticus.commands.impl.children.KillCommand;
import me.atticuszambrana.atticus.commands.impl.children.ListChildrenCommand;
import me.atticuszambrana.atticus.commands.impl.children.ProcreateCommand;
import me.atticuszambrana.atticus.commands.impl.children.RenameChildCommand;
import me.atticuszambrana.atticus.commands.impl.dev.RefreshPermsCommand;
import me.atticuszambrana.atticus.commands.impl.dev.RestartCommand;
import me.atticuszambrana.atticus.commands.impl.dev.SayCommand;
import me.atticuszambrana.atticus.commands.impl.dev.UpdateRankCommand;
import me.atticuszambrana.atticus.commands.impl.gambling.CoinflipCommand;
import me.atticuszambrana.atticus.commands.impl.punishment.BanCommand;
import me.atticuszambrana.atticus.commands.impl.punishment.KickCommand;
import me.atticuszambrana.atticus.commands.impl.punishment.ListKicksCommand;
import me.atticuszambrana.atticus.commands.impl.punishment.ModLogCommand;
import me.atticuszambrana.atticus.commands.impl.relationships.AcceptMarriageCommand;
import me.atticuszambrana.atticus.commands.impl.relationships.DenyMarriageCommand;
import me.atticuszambrana.atticus.commands.impl.relationships.DivorceCommand;
import me.atticuszambrana.atticus.commands.impl.relationships.MarriageStatusCommand;
import me.atticuszambrana.atticus.commands.impl.relationships.MarryCommand;
import me.atticuszambrana.atticus.commands.impl.shards.BalCommand;
import me.atticuszambrana.atticus.commands.impl.shards.EconomyCommand;
import me.atticuszambrana.atticus.commands.impl.shards.GiveShardsCommand;
import me.atticuszambrana.atticus.commands.impl.shards.PayCommand;
import me.atticuszambrana.atticus.commands.impl.shards.TakeShardsCommand;
import me.atticuszambrana.atticus.commands.impl.subscripton.RedeemCommand;
import me.atticuszambrana.atticus.commands.impl.subscripton.SubStatusCommand;
import me.atticuszambrana.atticus.permissions.Rank;
import me.atticuszambrana.atticus.util.LogUtil;
import me.atticuszambrana.atticus.util.StringUtil;

public class CommandCenter implements MessageCreateListener {
	
	private PermissionsManager permissions;
	
	public CommandCenter(PermissionsManager perms) {
		this.permissions = perms;
	}
	
	private Map<String, Command> Commands = new HashMap<>();
	private final String PREFIX = "^";
	
	// Method run by the main class to get all commands registered, I will be sorting them into their individual groups with comments
	public void setup() {
		// Developer Commands
		register(new RefreshPermsCommand());
		register(new UpdateRankCommand());
		register(new RestartCommand());
		register(new SayCommand());
		
		// Basic Commands
		register(new HelpCommand());
		register(new InfoCommand());
		
		// Treasure Shards System
		register(new BalCommand());
		register(new GiveShardsCommand());
		register(new TakeShardsCommand());
		register(new EconomyCommand());
		register(new PayCommand());
		
		// Gambling System
		register(new CoinflipCommand());
		
		// Relationship System
		register(new MarriageStatusCommand());
		register(new MarryCommand());
		register(new AcceptMarriageCommand());
		register(new DenyMarriageCommand());
		register(new DivorceCommand());
		
		// Child System
		register(new ProcreateCommand());
		register(new ListChildrenCommand());
		register(new RenameChildCommand());
		register(new KillCommand());
		
		// Premium Subscription Commands
		register(new SubStatusCommand());
		register(new RedeemCommand());
		
		// Punishment System Commands
		register(new ListKicksCommand());
		register(new KickCommand());
		register(new BanCommand());
		register(new ModLogCommand());
	}
	

	@Override
	public void onMessageCreate(MessageCreateEvent event) {
		
		// I actually want to block any commands from being ran, unless they are in a server.
		if(event.isPrivateMessage() || event.isGroupMessage()) {
			return;
		}
		
		if(!event.getMessageContent().startsWith(PREFIX)) {
			return;
		}
		
		for(Map.Entry<String, Command> cmd : Commands.entrySet()) {
			if(event.getMessageContent().startsWith(PREFIX + cmd.getKey())) {
				
				// Permission check system
				Rank myRank = Start.getPermManager().getRank(event.getMessageAuthor().asUser().get());
				if(myRank.getPower() >= cmd.getValue().getRankRequired().getPower()) {
					
					cmd.getValue().execute(StringUtil.toArray(event.getMessageContent()), event);
					
					// Then log it
					LogUtil.info("Command Center", event.getMessage().getAuthor().getName() + " ran " + event.getMessageContent());
					return;
				} else {
					LogUtil.info("Command Center", event.getMessageAuthor().getName() + " tried to run " + event.getMessageContent() + " however doesnt have permission.");
					
					EmbedBuilder embed = new EmbedBuilder();
					
					embed.setColor(Color.RED);
					embed.setTitle("You cannot do this!");
					embed.setDescription("This action requires Bot Permission Rank [" + cmd.getValue().getRankRequired().getName().toUpperCase() + "].");
					
					event.getChannel().sendMessage(embed);
					
					return;
				}
			}
		}
	}
	
	private void register(Command cmd) {
		Commands.put(cmd.getName(), cmd);
	}
	
	public Command getCommand(String name) {
		return Commands.get(name);
	}
	
	public Map<String, Command> getCommands() {
		return Commands;
	}
	
	public String getPrefix() {
		return PREFIX;
	}

}
