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
	
	public static EmbedBuilder error(ErrorType type) {
		EmbedBuilder embed = new EmbedBuilder();
		embed.setColor(Color.RED);
		if(type == ErrorType.SQL_ERROR) {
			embed.setTitle("Something went wrong");
			embed.setDescription("There was a problem while communicating with the database. Please try again later.");
		}
		
		return embed;
	}
	
	public enum ErrorType {
		SQL_ERROR,
		
	}
}
