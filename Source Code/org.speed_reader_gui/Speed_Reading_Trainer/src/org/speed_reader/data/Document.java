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
			this.setTitle("");
			this.setPath("");
			this.setWordCount(0);
			this.setTextBody("");
			this.setCurrWord("");
		}
		
		//constructor with file path as argument?
		//TODO: open file from path and use that to fill out the constructor
		public Document(String path) {
			
			this.setTitle("");
			this.setPath(path);
			this.setWordCount(0);
			this.setTextBody("");
			this.setCurrWord("");
		}
		
		public void incrementWord() {
			
		}
		
		//saves Document to serialized file
		public static void saveDocument(Document d) {
			FileOutputStream fileOut = null;
			ObjectOutputStream objOut= null;

			try 
			{
				fileOut = new FileOutputStream(d.getPath());
				objOut = new ObjectOutputStream(fileOut);
				objOut.writeObject(d);
				objOut.close();
				fileOut.close();
		     }	
			
			catch(IOException i)
		    {
				i.printStackTrace();
		    }
		}
		
		//loads document from serialized file
		public static Document loadDocument(String docPath) {
			FileInputStream fileIn = null;
			ObjectInputStream objIn = null;
			Document d = null;
				
			try
			{
				fileIn = new FileInputStream(docPath);
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
		
		//Getters and Setters

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}

		public String getPath() {
			return fileLoc;
		}

		public void setPath(String fileLoc) {
			this.fileLoc = fileLoc;
		}

		public int getWordCount() {
			return wordCount;
		}

		public void setWordCount(int wordCount) {
			this.wordCount = wordCount;
		}

		public String getTextBody() {
			return textBody;
		}

		public void setTextBody(String textBody) {
			this.textBody = textBody;
		}

		public String getCurrWord() {
			return currWord;
		}

		public void setCurrWord(String currWord) {
			this.currWord = currWord;
		}
}

