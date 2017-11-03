package org.speed_reader.data;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Scanner;


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
			//first check for .txt extension, then open file
			String checkTxt = path.substring(path.lastIndexOf('.'));
			
			if (checkTxt.compareTo(".txt") != 0) 
			{
				//we only use .txt files, so a document will not be created
				System.out.println("Error: Invalid document type. Only text files are allowed.");
			} 
			else 
			{
				//set title and path, init text body and word count
				int lastSlash = path.lastIndexOf('\\');
				if (lastSlash == -1) {
					this.title = path.substring(0, path.lastIndexOf('.'));
				} else {
					this.title = path.substring(path.lastIndexOf('\\') + 1, path.lastIndexOf('.'));
				}
				this.setPath(path);
				this.textBody = "";
				this.wordCount = 0;
				
				File doc = new File(path);
				Scanner docReader;
				try {
					docReader = new Scanner(doc);
					//read text from file
					while ( docReader.hasNext() ) {
						this.textBody += docReader.nextLine();
					}
					docReader.close();
					//evaluate number of words
	 				String[] words = this.textBody.split(" ");
	 				this.currWord = words[0];
	 				for (String s: words) {
	 					this.wordCount +=1;
	 				}
				} 
				catch (FileNotFoundException e) 
				{
					e.printStackTrace();
				}
			}
		}
		
		public void incrementWord() {
			String[] words = this.textBody.split(" ");
			for (int i = 0; i < words.length; i++) {
				if (this.currWord.compareTo(words[i]) == 0) {
					this.currWord = words[i+1];
					break;
				}
			}
		}
		
		//saves Document to serialized file
		public static void saveDocument(Document d) {
			FileOutputStream fileOut = null;
			ObjectOutputStream objOut= null;

			try 
			{
				fileOut = new FileOutputStream(d.getPath() + ".ser");
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

