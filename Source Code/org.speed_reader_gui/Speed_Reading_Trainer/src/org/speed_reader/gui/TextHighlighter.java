/**
 * 
 */
package org.speed_reader.gui;

import java.awt.Color;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;

/**
 * @author Matthias Guenther
 *
 */
public class TextHighlighter {
	
	private StyleContext sc;
	private Style baseStyle;
	private Style highlight;
	private DefaultStyledDocument doc;
	private JTextPane textPane;
	private ArrayList<int[]> wordDelimiters;
	private int[] docBounds;
	private int wordIdx;
	
	public TextHighlighter(String docStr){
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
		
		sc = StyleContext.getDefaultStyleContext();
		doc = new DefaultStyledDocument();
		baseStyle = sc.addStyle("Base Style", null);
		baseStyle.addAttribute(StyleConstants.Size, (int)(Toolkit.getDefaultToolkit().getScreenSize().getHeight()/1080 * 12));
		highlight = sc.addStyle("Highlight", null);
		highlight.addAttribute(StyleConstants.Background, Color.blue);
		highlight.addAttribute(StyleConstants.Foreground, Color.white);
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
	
	public void highlightWord(int newIdx){
		moveHighlight(wordDelimiters.get(wordIdx), wordDelimiters.get(newIdx));
		wordIdx = newIdx;
	}
	
	public void highlightFirstWord(){
		highlightWord(0);
	}
	
	public void highlightNextWord(){
		highlightWord(wordIdx + 1);
	}
	
	public JTextPane getTextPane(){
		return textPane;
	}

}
