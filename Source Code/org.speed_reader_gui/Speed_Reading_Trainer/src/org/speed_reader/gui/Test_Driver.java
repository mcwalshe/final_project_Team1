package org.speed_reader.gui;

import org.speed_reader.data.User;

public class Test_Driver {

	public static void main(String[] args) {
		
		// Test password-hashing function
		System.out.println("Testing password-hashing function . . .");
		String hashTestStr = "Test string";
		System.out.println("\tInput:  " + hashTestStr);
		System.out.println("\tOutput: " + User.hashString(hashTestStr));
		
		// Instantiate GUI
		MainGUI newGUI;
	    newGUI = new MainGUI();
	}
}
