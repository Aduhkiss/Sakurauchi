package me.atticuszambrana.atticus.config;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class ConfigHelper {
	
	private static final String fileName = "config.json";
	
	public static boolean exists() {
		String line = null;
		String data = null;
		boolean found = true;
		
		try {
			FileReader reader = new FileReader(fileName);
			BufferedReader bufferedReader = new BufferedReader(reader);
			
			try {
				while((line = bufferedReader.readLine()) != null) {
					data = line;
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			bufferedReader.close();
		} catch(FileNotFoundException ex) {
			found = false;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return found;
	}
	
	public static void write(String data) {
		try {
			FileWriter writer = new FileWriter(fileName);
			BufferedWriter bufferedWriter = new BufferedWriter(writer);
			
			bufferedWriter.write(data);
			
			bufferedWriter.close();
		} catch(IOException ex) {
			ex.printStackTrace();
		}
	}
	
	public static String get() {
		String line = null;
		String data = null;
		boolean found = true;
		
		try {
			FileReader reader = new FileReader(fileName);
			BufferedReader bufferedReader = new BufferedReader(reader);
			
			try {
				while((line = bufferedReader.readLine()) != null) {
					data = line;
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			bufferedReader.close();
		} catch(FileNotFoundException ex) {
			found = false;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return data;
	}
}
