package me.atticuszambrana.atticus.time;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class AtticusTime {
	
	// Will give you an object for the time of its creation
	// Author: Atticus Zambrana
	
	private LocalTime time;
	private LocalDate date;
	private long milli;
	private TimeType type;
	
	public AtticusTime() {
		// Because the server is being hosted in this region
		time = LocalTime.now(ZoneId.of("America/Los_Angeles"));
		milli = System.currentTimeMillis();
	}
	
	public AtticusTime(long milli) {
		this.milli = milli;
		LocalDateTime localDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(milli), ZoneId.systemDefault());
		this.date = localDateTime.toLocalDate();
	}
	
	public String getDayFromTime() {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
		return time.format(formatter);
	}
	
	public String getTimeFromTime() {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm:ss");
		return time.format(formatter);
	}
	
	public String getFullFromDate() {
		//TODO: FIX THIS YOU DUMB
		DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
		DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("hh:mm:ss");
		//return date.format(dateFormatter) + "" + date.format(timeFormatter) + "" + getType().toString();
		return date.format(dateFormatter);
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
