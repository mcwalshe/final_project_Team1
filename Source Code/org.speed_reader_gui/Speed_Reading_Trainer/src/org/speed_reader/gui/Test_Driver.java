package org.speed_reader.gui;

import javax.swing.BoxLayout; // TEMPORARY
import javax.swing.JFrame; // TEMPORARY
import javax.swing.JScrollPane; // TEMPORARY
import java.util.Scanner; // TEMPORARY
import java.awt.Toolkit; // TEMPORARY
import java.io.File; // TEMPORARY
import java.io.FileNotFoundException; // TEMPORARY

import org.speed_reader.data.User;

public class Test_Driver {

	public static void main(String[] args) {
		
		// Test password-hashing function
		System.out.println("Testing password-hashing function . . .");
		String hashTestStr = "Test string";
		System.out.println("\tInput:  " + hashTestStr);
		System.out.println("\tOutput: " + User.hashString(hashTestStr));
		
		// Instantiate GUI
/*		MainGUI newGUI;
		newGUI = new MainGUI();*/
		
		// Test repainting
/*		for(int i = 0; i < 10; i++){
			try {
				Thread.sleep(2000);
			} catch(InterruptedException e){
				e.printStackTrace();
			}
			newGUI.updateDocList();
		}*/
		
		// Test highlighting
		try {
			Scanner testScanner = new Scanner(new File("src/org/speed_reader/gui/test2.txt"));
			String testDocStr = testScanner.useDelimiter("\\Z").next();
			testScanner.close();
			TextHighlighter testFormat = new TextHighlighter(testDocStr);
//			System.out.println(testDocStr);
			JFrame testFrame = new JFrame("Highlight Test");
			testFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			testFrame.setSize(DPIScaling.scaleInt(800), DPIScaling.scaleInt(800));
			testFrame.setLayout(new BoxLayout(testFrame.getContentPane(), BoxLayout.Y_AXIS));
			testFrame.add(new JScrollPane(testFormat.getTextPane()), null);
			testFrame.setVisible(true);
			testFormat.highlightFirstWord();
			long msOld = System.currentTimeMillis();
			long msNew;
			while(true){
				try {
					msNew = System.currentTimeMillis();
					try {
						Thread.sleep(120 - (int)(msNew - msOld)); // 500 WPM
					} catch(InterruptedException e){
						e.printStackTrace();
					}
					msOld = System.currentTimeMillis();
					testFormat.highlightNextWord();
				} catch(IndexOutOfBoundsException e){
					break;
				}
			}
		} catch(FileNotFoundException e){
			System.out.println("Error: file not found.");
		}
	}
	
	// Testing an efficient string operation to move the selection without reallocating the string.
	
}