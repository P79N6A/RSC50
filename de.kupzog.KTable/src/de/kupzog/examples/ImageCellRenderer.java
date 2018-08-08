/*
 * Copyright (C) 2004 by Friederich Kupzog Elektronik & Software
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
    
    Author: Friederich Kupzog  
    fkmk@kupzog.de
    www.kupzog.de/fkmk
*/
package de.kupzog.examples;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;

import de.kupzog.ktable.KTableCellRenderer;
import de.kupzog.ktable.KTableModel;

/**
 * @author Friederich Kupzog
 */
public class ImageCellRenderer implements KTableCellRenderer {

	protected Display m_Display;
	private int imageWidth;
	
	public ImageCellRenderer(int imageWidth) {
		m_Display = Display.getCurrent();
		this.imageWidth = imageWidth;
	}
	
	@Override
	public int getOptimalWidth(
		GC gc, 
		int col, 
		int row, 
		Object content, 
		boolean fixed, 
		KTableModel model)
	{
		return imageWidth;
	}
	
	@Override
	public void drawCell(GC gc, 
		Rectangle rect, 
		int col, 
		int row, 
		Object content, 
		boolean focus, 
		boolean fixed,
		boolean clicked, 
		KTableModel model)
	{
		Color borderColor;
		Image image = (Image)content;

		borderColor = m_Display.getSystemColor(SWT.COLOR_WIDGET_BACKGROUND);
		
		// ???±?
		gc.setForeground(borderColor);
		gc.drawLine(rect.x, rect.y + rect.height, 
				rect.x + rect.width, rect.y + rect.height);
		gc.drawLine(rect.x + rect.width, rect.y, 
				rect.x + rect.width, rect.y + rect.height);
	
		if (col == 0) {
			gc.setBackground(m_Display.getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));
			gc.drawImage((image),rect.x, rect.y);
		}
	}
}
