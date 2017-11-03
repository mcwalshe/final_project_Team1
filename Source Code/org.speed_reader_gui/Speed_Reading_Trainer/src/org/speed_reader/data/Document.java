package org.speed_reader.data;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class Document implements Serializable {
		private String title;
		private String fileLoc;
		private int wordCount;
		private String textBody;
		private String currWord;
		
		//no arg constructor
		public Document() {
			this.title = "";
			this.fileLoc = "";
			this.wordCount = 0;
			this.textBody = "";
			this.currWord = "";
		}
		
		//constructor with file path as argument?
		
		public Document(String path) {
			this.title = "";
			this.fileLoc = path;
			this.wordCount = 0;
			this.textBody = "";
			this.currWord = "";
		}
		
		public void incrementWord() {
			
		}
		
		//loads document from serialized file
		public static Document loadDocument() {
			FileInputStream fileIn = null;
			ObjectInputStream objIn = null;
			Document d = null;
				
			try
			{
				fileIn = new FileInputStream(".ser");
				objIn = new ObjectInputStream(fileIn);
				d = (Document) objIn.readObject();
				objIn.close();
				fileIn.close();
			}
			catch(IOException i)
			{
				i.printStackTrace();
			} 
			catch (ClassNotFoundException e) 
			{
				e.printStackTrace();
			}  
			return d;
		}
}

