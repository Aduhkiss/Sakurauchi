package me.atticuszambrana.atticus.util;

import java.awt.Color;

import org.javacord.api.entity.message.embed.EmbedBuilder;

public class MessageUtil {
	public static EmbedBuilder message(Color color, String title, String message) {
		EmbedBuilder embed = new EmbedBuilder();
		embed.setColor(color);
		embed.setTitle(title);
		embed.setDescription(message);
		return embed;
	}
}
