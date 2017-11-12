package org.speed_reader.data;

import java.awt.Color;

import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;

import org.speed_reader.gui.DPIScaling;

public class Pointer {
	
	private StyleContext sc;
	private Style baseStyle;
	private Style highlight;
	
	public Pointer(){
		sc = StyleContext.getDefaultStyleContext();
		baseStyle = sc.addStyle("Base Style", null);
		baseStyle.addAttribute(StyleConstants.Size, DPIScaling.scaleInt(12));
		highlight = sc.addStyle("Highlight", null);
		highlight.addAttribute(StyleConstants.Background, Color.blue);
		highlight.addAttribute(StyleConstants.Foreground, Color.white);
	}
	
	public StyleContext getStyleContext(){
		return sc;
	}
	
	public Style getBaseStyle(){
		return baseStyle;
	}
	
	public Style getHighlight(){
		return highlight;
	}

}
