/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.icons;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;

import javax.swing.Icon;

public class SlideShowIcon implements Icon
{
	public int getIconHeight()
	{
		return HEIGHT;
	}

	public int getIconWidth()
	{
		return WIDTH;
	}
	public void paintIcon(Component c, Graphics g, int x, int y)
	{
		g.setColor(new Color(237, 185, 0));
		g.fillOval(x, y , WIDTH, HEIGHT);
		g.setColor(Color.BLACK);
		g.drawOval(x, y , WIDTH, HEIGHT);
	}
	
	protected static final int WIDTH = 14;
	protected static final int HEIGHT = 14;
}
