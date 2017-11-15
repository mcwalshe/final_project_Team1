package org.speed_reader.gui;

import org.speed_reader.data.*;

public class DataTestDriver {
	public static void main(String[] args) {
		//Testing Document
		Document d = new Document("C:\\Users\\maria\\Documents\\School\\Fall 2017\\ECE373\\ECE373_Team_Project_Team1\\Source Code\\org.speed_reader_gui\\Speed_Reading_Trainer\\src\\org\\speed_reader\\gui\\test.txt");
		//test proper document construction
		System.out.println("Expected: test Actual: " + d.getTitle());
		System.out.println("Expected: This is a test file. I think testing things is fun.  Actual: " + d.getTextBody());
		System.out.println("Expected: This Actual: " +d.getCurrWord());
		System.out.println("Expected: C:\\Users\\maria\\Documents\\School\\Fall 2017\\ECE373\\ECE373_Team_Project_Team1\\Source Code\\org.speed_reader_gui\\Speed_Reading_Trainer\\src\\org\\speed_reader\\gui\\test.txt\r\n Actual: " +d.getPath());
		System.out.println("Expected: 11 Actual: "+ d.getWordCount());
		//text document functions
		Document.saveDocument(d);
		d.incrementWord();
		System.out.println("Expected: is Actual: "+ d.getCurrWord());
		//check serialization success
		d = Document.loadDocument("C:\\\\Users\\\\maria\\\\Documents\\\\School\\\\Fall 2017\\\\ECE373\\\\ECE373_Team_Project_Team1\\\\Source Code\\\\org.speed_reader_gui\\\\Speed_Reading_Trainer\\\\src\\\\org\\\\speed_reader\\\\gui\\\\test.txt.ser");
		System.out.println("Expected: This Actual: "+d.getCurrWord());
		
		//Testing Users
		User u = new User("Maria");
		u.addDoc(d);
		//test getters and setters
		System.out.println("Expected: Maria Actual: " + u.getName());
		System.out.println("Expected: test Actual: " + u.getDoc(0).getTitle());
		System.out.println("Expected: 0 Actual: " + u.getCurrWPM());
		System.out.println("Expected: 0 Actual: " + u.getRecordTrainingSec());
		System.out.println("Expected: 0 Actual: " + u.getRecordWPM());
		u.setCurrWPM(777);
		u.setRecordTrainingSec(334);
		u.setRecordWPM(778);
		u.removeDoc(0);
		u.addDoc(new Document());
		System.out.println("Expected: 777 Actual: " + u.getCurrWPM());
		System.out.println("Expected: 334 Actual: " +u.getRecordTrainingSec());
		System.out.println("Expected: 778 Actual: " +u.getRecordWPM());
		System.out.println("Expected: Actual: " +u.getDoc(0).getTitle());
		
		//testing password
		if (!u.hasPassword()) {
			u.setPassword("ready-2_GRADu@t3");
			if (u.checkPassword("ready-2_GRADu@te")) {
				System.out.println("Error: wrong pass should return false");
			} else {
				System.out.println("Success! Failed to validate incorrect password");
			}
			if (u.checkPassword("")) {
				System.out.println("Error: blank pass should return false");
			} else {
				System.out.println("Success! Failed to validate Blank Password");
			}
			if (u.checkPassword("ready-2_GRADu@t3")) {
				System.out.println("Success: password matches.");
			} else {
				System.out.println("Error: Password should've matched");
			}
		} else {
			System.out.println("Error: password should be null.");
		}
		
	}
}
