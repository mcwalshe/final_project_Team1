package org.speed_reader.gui;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Style;

import org.speed_reader.data.Pointer;

public class TextHighlighter {
	
	private Style baseStyle;
	private Style highlight;
	private DefaultStyledDocument doc;
	private JTextPane textPane;
	private ArrayList<int[]> wordDelimiters;
	private int[] docBounds;
	private int wordIdx;
	
	public TextHighlighter(String docStr, Pointer pointer){
		docBounds = new int[2];
		docBounds[0] = 0;
		docBounds[1] = docStr.length();
		// Prevent excessive reallocation by estimating word count.
		wordDelimiters = new ArrayList<int[]>(docStr.length() / 6);
		Pattern p = Pattern.compile("[\\w'’\\-]+");
		Matcher m = p.matcher(docStr);
		int lastWordStart = 0;
		int lastWordEnd = 0;
		while(m.find(lastWordEnd)){
			lastWordStart = m.start();
			lastWordEnd = m.end();
			int[] bounds = {lastWordStart, lastWordEnd - lastWordStart};
			wordDelimiters.add(bounds);
		}
		wordIdx = 0;
		
		doc = new DefaultStyledDocument();
		baseStyle = pointer.getBaseStyle();
		highlight = pointer.getHighlight();
		try {
			doc.insertString(0, docStr, baseStyle);
		} catch(BadLocationException e){}
		textPane = new JTextPane(doc);
	}
	
	private void moveHighlight(int[] oldBounds, int[] newBounds){
		doc.setCharacterAttributes(oldBounds[0], oldBounds[1], baseStyle, true);
		doc.setCharacterAttributes(newBounds[0], newBounds[1], highlight, false);
		textPane.revalidate();
		textPane.repaint();
	}
	
	public void highlightWord(int newIdx) throws IndexOutOfBoundsException {
		moveHighlight(wordDelimiters.get(wordIdx), wordDelimiters.get(newIdx));
		wordIdx = newIdx;
	}
	
	public void highlightFirstWord(){
		highlightWord(0);
	}
	
	public void highlightNextWord() throws IndexOutOfBoundsException {
		highlightWord(wordIdx + 1);
	}
	
	public void startReading(int wpm){
		int msPerWord = 60000 / wpm; // (60000 ms/min) / (wpm words/min) = 60000/wpm ms/word
		if(msPerWord <= 0) throw new IllegalArgumentException();
		highlightFirstWord();
		long msOld = System.currentTimeMillis();
		long msNew;
		while(true){
			try {
				msNew = System.currentTimeMillis();
				try {
					Thread.sleep(msPerWord - (int)(msNew - msOld));
				} catch(InterruptedException e){
					e.printStackTrace();
				} catch(IllegalArgumentException e){
					// (msPerWord - (int)(msNew - msOld)) was negative. Don't sleep at all. 
				}
				msOld = System.currentTimeMillis();
				highlightNextWord();
			} catch(IndexOutOfBoundsException e){
				break;
			}
		}
	}
	
	public JTextPane getTextPane(){
		return textPane;
	}

}
