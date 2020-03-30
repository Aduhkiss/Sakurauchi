package me.atticuszambrana.atticus.time;

import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class AtticusTime {
	
	// Will give you an object for the time of its creation
	// Author: Atticus Zambrana
	
	private LocalTime time;
	private long milli;
	private TimeType type;
	
	public AtticusTime() {
		// Because the server is being hosted in this region
		time = LocalTime.now(ZoneId.of("America/Los_Angeles"));
		milli = System.currentTimeMillis();
	}
	
	public String getDay() {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
		return time.format(formatter);
	}
	
	public String getTime() {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm:ss");
		return time.format(formatter);
	}
	
	public long getMilli() {
		return milli;
	}
	
	public TimeType getType() {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
		int hour = Integer.valueOf(time.format(formatter).substring(0, 2));
		if(hour >= 13) {
			return TimeType.PM;
		}
		else {
			return TimeType.AM;
		}
	}
	
	public enum TimeType {
		AM,
		PM,
	}
}
