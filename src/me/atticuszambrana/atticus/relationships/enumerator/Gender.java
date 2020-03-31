package me.atticuszambrana.atticus.relationships.enumerator;

import java.awt.Color;

public enum Gender {
	
	MALE("Boy", Color.BLUE),
	FEMALE("Girl", Color.PINK);
	
	private String name;
	private Color color;
	
	Gender(String name, Color color) {
		this.name = name;
		this.color = color;
	}
	
	public String getName() {
		return name;
	}
	
	public Color getColor() {
		return color;
	}
}
