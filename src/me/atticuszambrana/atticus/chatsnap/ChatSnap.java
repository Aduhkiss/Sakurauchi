package me.atticuszambrana.atticus.chatsnap;

import org.javacord.api.event.message.MessageCreateEvent;
import org.javacord.api.listener.message.MessageCreateListener;

import me.atticuszambrana.atticus.Plugin;

public class ChatSnap extends Plugin implements MessageCreateListener {
	public ChatSnap() {
		super("ChatSnap");
	}

	@Override
	public void onEnable() {
	}

	@Override
	public void onMessageCreate(MessageCreateEvent event) {
	}
}
