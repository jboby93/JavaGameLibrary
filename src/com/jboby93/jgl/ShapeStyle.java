package com.jboby93.jgl;

import java.awt.Color;

//TODO JavaDocs
public class ShapeStyle {
	public Pen outline;
	public Color fill;
	public boolean drawOutline;
	public boolean drawFill;
	
	public ShapeStyle() {
		outline = new Pen();
		fill = Color.white;
		
		drawOutline = true;
		drawFill = true;
	}
	
	public ShapeStyle(Color fill) {
		outline = new Pen(fill);
		if(fill == null) drawFill = false; else { this.fill = fill; drawFill = true; }
		
		//drawOutline = true;
		//drawFill = true;
	}
	
	public ShapeStyle(Color fillColor, Color outlineColor) {
		if(fillColor == null) drawFill = false; else { fill = fillColor; drawFill = true; }
		if(outlineColor == null) drawOutline = false; else { outline = new Pen(outlineColor); drawOutline = true; }
		
		//drawOutline = true;
		//drawFill = true;
	}
}
