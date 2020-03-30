package me.atticuszambrana.atticus.activity;

import org.javacord.api.entity.activity.ActivityType;
import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.user.User;

import me.atticuszambrana.atticus.Plugin;
import me.atticuszambrana.atticus.Start;

public class Activity extends Plugin {
	
	public Activity() {
		super("Activity");
		update();
	}
	
	private int activityTick = 0;
	
	private void update() {
		new Thread() {
			public void run() {
				String prefix = Start.getCommandManager().getPrefix();
				if(activityTick == 0) {
					Start.getDiscord().updateActivity(ActivityType.PLAYING, prefix + "help for commands!");
					//System.out.println("Slide 1");
					activityTick++;
				}
				else if(activityTick == 1) {					
					Start.getDiscord().updateActivity(ActivityType.PLAYING, "How are you doing today?");
					
					//System.out.println("Slide 2");
					activityTick++;
				}
				else if(activityTick == 2) {
					
					// Get the number of currently active servers that the bot is on
					int i = 0;
					for(Server server : Start.getDiscord().getServers()) {
						i++;
					}
					
					Start.getDiscord().updateActivity(ActivityType.PLAYING, "I am on " + i + " servers.");
					activityTick++;
				}
				else if(activityTick == 3) {
					// Get all the servers that we are on right now
					int i = 0;
					for(Server server : Start.getDiscord().getServers()) {
						for(User user : server.getMembers()) {
							// Don't count bots
							if(!user.isBot()) {
								i++;
							}
						}
					}
					
					Start.getDiscord().updateActivity(ActivityType.PLAYING, "Watching " + i + " users.");
					activityTick = 0;
				}
				
				try {
					Thread.sleep((1000 * 60));
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
				update();
			}
		}.start();
	}
	
	@Override
	public void onEnable() {
	}
}
