package org.speed_reader.gui;

import java.security.NoSuchAlgorithmException;

import org.speed_reader.data.User;

public class Test_Driver {

	public static void main(String[] args) {
		
		// Test password-hashing function
		System.out.println("Testing password-hashing function . . .");
		String hashTestStr = "Test string";
		try {
			System.out.println("\tInput:  " + hashTestStr);
			System.out.println("\tOutput: " + User.hashString(hashTestStr));
		} catch(NoSuchAlgorithmException e){
			e.printStackTrace();
		}
		
		// Instantiate GUI
		MainGUI newGUI;
	    newGUI = new MainGUI();
	}
}
