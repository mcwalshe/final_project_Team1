package org.speed_reader.gui;

import java.awt.Toolkit;

public class DPIScaling {
	
	public static final int REF_HEIGHT = 1080;
	public static final double SCALE_FACTOR = Toolkit.getDefaultToolkit().getScreenSize().getHeight() / REF_HEIGHT;
	
	public static int scaleInt(double base){
		return (int)(base * SCALE_FACTOR);
	}
	
	public static double scaleDouble(double base){
		return (double)(base * SCALE_FACTOR);
	}
	
}
