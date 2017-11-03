package org.speed_reader.gui;

import org.speed_reader.data.*;

public class DocumentTestDriver {
	public static void main(String[] args) {
		Document d = new Document("C:\\Users\\maria\\Documents\\School\\Fall 2017\\ECE373\\ECE373_Team_Project_Team1\\Source Code\\org.speed_reader_gui\\Speed_Reading_Trainer\\src\\org\\speed_reader\\gui\\test.txt");
		//test proper document construction
		System.out.println(d.getTitle());
		System.out.println(d.getTextBody());
		System.out.println(d.getCurrWord());
		System.out.println(d.getPath());
		System.out.println(d.getWordCount());
		//text document functions
		Document.saveDocument(d);
		d.incrementWord();
		System.out.println(d.getCurrWord());
		d = Document.loadDocument("C:\\\\Users\\\\maria\\\\Documents\\\\School\\\\Fall 2017\\\\ECE373\\\\ECE373_Team_Project_Team1\\\\Source Code\\\\org.speed_reader_gui\\\\Speed_Reading_Trainer\\\\src\\\\org\\\\speed_reader\\\\gui\\\\test.txt.ser");
		System.out.println(d.getCurrWord());
	}
}
