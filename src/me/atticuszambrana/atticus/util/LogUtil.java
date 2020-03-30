package me.atticuszambrana.atticus.util;

import me.atticuszambrana.atticus.Start;
import me.atticuszambrana.atticus.time.AtticusTime;

public class LogUtil {
	public static void info(String caller, String message) {
		AtticusTime time = new AtticusTime();
		//System.out.println("[" + time.getDay() + " - " + time.getTime() + "] " + caller + "> " + message);
		System.out.println("[" + time.getTime() + " " + time.getType().toString() + "] " + caller + "> " + message);
	}
	
	public static void debug(String caller, String message) {
		AtticusTime time = new AtticusTime();
		//System.out.println("[" + time.getDay() + " - " + time.getTime() + "] " + caller + "> " + message);
		if(Start.showDebugMessages()) {
			System.out.println("[" + time.getTime() + " " + time.getType().toString() + "] " + caller + "> " + message);
		}
	}
}
