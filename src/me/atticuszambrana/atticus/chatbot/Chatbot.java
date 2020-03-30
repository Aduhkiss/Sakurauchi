package me.atticuszambrana.atticus.chatbot;

import org.javacord.api.event.message.MessageCreateEvent;
import org.javacord.api.listener.message.MessageCreateListener;

import me.atticuszambrana.atticus.Plugin;

public class Chatbot extends Plugin implements MessageCreateListener {
	
	public Chatbot() {
		super("Chatbot");
	}

	@Override
	public void onEnable() {
	}
	
	public String getReply(String in) {
		String input = in.toLowerCase();
		if(input.indexOf("hi") >=0 ) {
			return "Hello there";
		}
		
		return "I have nothing to say.";
	}

	@Override
	public void onMessageCreate(MessageCreateEvent event) {
		if(!event.isPrivateMessage()) {
			return;
		}
		
		String input = event.getMessageContent();
		System.out.println("mhm");
		event.getChannel().sendMessage(getReply(input));
	}

}
