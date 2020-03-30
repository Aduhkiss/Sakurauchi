package me.atticuszambrana.atticus.commands.impl.dev;

import java.awt.Color;

import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.event.message.MessageCreateEvent;

import me.atticuszambrana.atticus.commands.Command;
import me.atticuszambrana.atticus.permissions.Rank;

public class SystemInfoCommand extends Command {
	
	public SystemInfoCommand() {
		super("systeminfo", "Display System Information", Rank.DEVELOPER);
	}

	@Override
	public void execute(String[] args, MessageCreateEvent event) {
		EmbedBuilder embed = new EmbedBuilder();
		
		embed.setColor(Color.PINK);
		embed.setTitle("System Information");
		embed.addField("OS Name", System.getProperty("os.name"));
		embed.addField("OS Version", System.getProperty("os.version"));
		embed.addField("Java Version", System.getProperty("java.version"));
		embed.addField("JRE Vendor", System.getProperty("java.vendor"));
		
		event.getChannel().sendMessage(embed);
		return;
	}

}
