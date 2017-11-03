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
		
		// Test repainting
		for(int i = 0; i < 10; i++){
			try {
				Thread.sleep(2000);
			} catch(InterruptedException e){
				e.printStackTrace();
			}
			newGUI.updateDocList();
		}
	}
}
