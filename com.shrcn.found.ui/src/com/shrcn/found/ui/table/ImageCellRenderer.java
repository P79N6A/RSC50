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
package com.shrcn.found.ui.table;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;

import de.kupzog.ktable.KTableModel;
import de.kupzog.ktable.SWTX;
import de.kupzog.ktable.renderers.DefaultCellRenderer;

/**
 * @author Friederich Kupzog
 */
public class ImageCellRenderer extends DefaultCellRenderer {

	private int imageWidth;

	public ImageCellRenderer(int style, int imageWidth) {
		super(style);
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
		if (content == null)
			return;
		// draw focus sign:
        if (focus && (m_Style & INDICATION_FOCUS)!=0) {
            rect = drawDefaultSolidCellLine(gc, rect, COLOR_LINE_LIGHTGRAY, COLOR_LINE_LIGHTGRAY);
            drawCellImage(gc, rect, content, COLOR_BGFOCUS);
            gc.drawFocus(rect.x, rect.y, rect.width, rect.height);
        } else if (focus && (m_Style & INDICATION_FOCUS_ROW)!=0) {
            rect = drawDefaultSolidCellLine(gc, rect, COLOR_BGROWFOCUS, COLOR_BGROWFOCUS);
            drawCellImage(gc, rect, content, COLOR_BGROWFOCUS);
        } else {
            rect = drawDefaultSolidCellLine(gc, rect, COLOR_LINE_LIGHTGRAY, COLOR_LINE_LIGHTGRAY);
            drawCellImage(gc, rect, content, getBackground());
        }
        
        if ((m_Style & INDICATION_COMMENT)!=0)
            drawCommentSign(gc, rect);
	}
	
	protected void drawCellImage(GC gc, Rectangle rect, Object content, Color bgColor) {
		// clear background:
	    gc.setBackground(bgColor);
	    gc.fillRectangle(rect);
		
		// 画边框（右边和下边）
		Color borderColor = m_Display.getSystemColor(SWT.COLOR_WIDGET_BACKGROUND);
		gc.setForeground(borderColor);
		gc.drawLine(rect.x, rect.y + rect.height, 
				rect.x + rect.width, rect.y + rect.height);
		gc.drawLine(rect.x + rect.width, rect.y, 
				rect.x + rect.width, rect.y + rect.height);
		
		Image image = (Image)content;
		SWTX.drawTextImage(
				gc,
				null,
				SWTX.ALIGN_HORIZONTAL_LEFT | SWTX.ALIGN_VERTICAL_TOP,
				image,
				SWTX.ALIGN_HORIZONTAL_CENTER | SWTX.ALIGN_VERTICAL_CENTER,
				rect.x+3,
				rect.y,
				rect.width-3,
				rect.height
				);
	}
}
