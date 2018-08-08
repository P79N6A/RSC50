/*
 * Copyright (C) 2004 by Friederich Kupzog Elektronik & Software
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html

Authors: 
Friederich Kupzog,  fkmk@kupzog.de, www.kupzog.de/fkmk
Lorenz Maierhofer, lorenz.maierhofer@logicmindguide.com

*/
package de.kupzog.ktable.renderers;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;

import de.kupzog.ktable.KTableModel;
import de.kupzog.ktable.SWTX;
import de.kupzog.ktable.renderers.HeaderCellRenderer;

/**
 * Class that provides an easy way of rendering a fixed cell.
 * <p>
 * Provided styles are at the moment: 
 * 
 * <ul>
 * <li><b>STYLE_FLAT</b> for a flat look.</li>
 * <li><b>STYLE_PUSH</b> for a button-like look.</li>
 * <li><b>INDICATION_SORT</b> if a sort indicator should be painted. Has only an effect 
 * when the <code>KTableModel</code> used is an
 * instance of <code>KTableSortedModel</code>. <br> 
 * Can be combined with <code>STYLE_FLAT</code> or <code>STYLE_PUSH</code> by or-ing.</li>
 * <li><b>INDICATION_FOCUS</b> when a focus bit should be colored differently. Combine by or-ing.
 * Note that the focus is only set to fixed cells when <code>
 * KTable.setHighlightSelectionInHeader(true)</code> is set.</li>
 * <li><b>INDICATION_FOCUS_ROW</b> when row-selection mode is on and the row highlighting should
 * be present in the fixed cell on the left. </li>
 * <li><b>INDICATION_CLICKED</b> shows a visible feedback when the user clicks on the cell.<br>
 * Only applicable if STYLE_PUSH is used.</li>
 * <li><b>SWT.BOLD</b> Makes the renderer draw bold text.</li>
 * <li><b>SWT.ITALIC</b> Makes the renderer draw italic text</li>
 * </ul>
 * 
 * @author Lorenz Maierhofer <lorenz.maierhofer@logicmindguide.com>
 */
public class HeaderCheckRenderer extends HeaderCellRenderer {
    /** 
     * Style bit that forces that the renderer paints images 
     * instead of directly painting. The images used for painting
     * can be found in the folder /icons/ and are named 
     * checked.gif, unchecked.gif, checked_clicked.gif and unchecked_clicked.gif.
     * <p>
     * Note that when using images, drawing is 2-3 times slower than when 
     * using direct painting. It might be visible if many cells should be 
     * rendered that way. So this is not default.
     */ 
    public static final int SIGN_IMAGE = 1<<31;
    
    /**
     * Makes the renderer draw an X as the symbol that signals the value
     * is true. This has only an effect if the style SIGN_IMAGE is not active.
     */
    public static final int SIGN_X = 1<<30;
    /**
     * Makes the renterer draw a check sign as the symbol that signals the value
     * true. THIS IS DEFAULT.
     */
    public static final int SIGN_CHECK = 1<<29;
    
    /** Indicator for a checked entry / true boolean decision */
    public static final Image IMAGE_CHECKED = SWTX.loadImageResource(
            Display.getCurrent(), 
            "/icons/checkbox_selected.png");
    
    /** Indicator for an unchecked entry / false boolean decision */
    public static final Image IMAGE_UNCHECKED = SWTX.loadImageResource(
            Display.getCurrent(), 
    		"/icons/checkbox_normal.png");
    
    /** Indicator for an checked entry / true boolean decision that is currently clicked.*/
    public static final Image IMAGE_CHECKED_CLICKED = SWTX.loadImageResource(
            Display.getCurrent(), 
   			"/icons/checkbox_rollover_selected.png");
    
    /** Indicator for an unchecked entry / false boolean decision that is currently clicked.*/
    public static final Image IMAGE_UNCHECKED_CLICKED = SWTX.loadImageResource(
            Display.getCurrent(), 
    		"/icons/checkbox_rollover.png");
    
    public static final Color COLOR_FILL = new Color(Display.getDefault(), 206, 206, 206);
    public static final Color BORDER_DARK = new Color(Display.getDefault(), 90, 90, 57);
    public static final Color BORDER_LIGHT = new Color(Display.getDefault(), 156, 156, 123);
    /**
     * A constructor that lets the caller specify the style.
     * @param style The style that should be used to paint.<p>
     * - Use SWT.FLAT for a flat look.<br>
     * - Use SWT.PUSH for a button-like look. (default)<p>
     * The following additional indications can be activated: <br>
     * - INDICATION_FOCUS changes the background color if the fixed cell has focus.<br>
     * - INDICATION_FOCUS_ROW changes the background color so that it machtes with normal cells in rowselection mode.<br>
     * - INDICATION_SORT shows the sort direction when using a KTableSortedModel.<br>
     * - INDICATION_CLICKED shows a click feedback, if STYLE_PUSH is specified.
     * For text styles, the styles SWT.BOLD and SWT.ITALIC can be given.
     */
    public HeaderCheckRenderer(int style) {
        super(style);
        m_Style |= STYLE_PUSH;
    }
    
    /** 
     * Paint a box with or without a checked symbol.
     * 
     * @see de.kupzog.ktable.KTableCellRenderer#drawCell(GC, Rectangle, int, int, Object, boolean, boolean, boolean, KTableModel)
     */
    public void drawCell(GC gc, Rectangle rect, int col, int row, Object content, 
            boolean focus, boolean fixed, boolean clicked, KTableModel model) {
        applyFont(gc);
        
        // set up the colors:
        Color bgColor = getBackground();
        Color bottomBorderColor = COLOR_LINE_DARKGRAY;
        Color rightBorderColor = COLOR_LINE_DARKGRAY;
        Color fgColor = getForeground();
        if (focus && (m_Style & INDICATION_FOCUS)!=0) { 
            bgColor = COLOR_FIXEDHIGHLIGHT;
            bottomBorderColor = COLOR_TEXT;
            rightBorderColor = COLOR_TEXT;
        }
        if (focus && (m_Style & INDICATION_FOCUS_ROW) !=0) {
            bgColor = COLOR_BGROWFOCUS;
            bottomBorderColor = COLOR_BGROWFOCUS;
            rightBorderColor = COLOR_BGROWFOCUS;
            fgColor = COLOR_FGROWFOCUS;
        }        
               
        // STYLE_FLAT:
        if ((m_Style & STYLE_FLAT)!=0) {
            rect = drawDefaultSolidCellLine(gc, rect, bottomBorderColor, rightBorderColor);
            
            // draw content:
            drawCellContent(gc, rect, col, row, content, model, bgColor, fgColor);
            
        } else { // STYLE_PUSH
        	rect = drawDefaultSolidCellLine(gc, rect, COLOR_LINE_LIGHTGRAY, COLOR_LINE_LIGHTGRAY);
        	drawCellContent(gc, rect, col, row, "", model, bgColor, fgColor);
            drawCheckableImage(gc, rect, content, COLOR_BGFOCUS, clicked);
        }
        resetFont(gc);
    }

    protected void drawCheckableImage(GC gc, Rectangle rect, Object content, Color bgColor, boolean clicked) {
        // draw content as image:
        if ((m_Style & SIGN_IMAGE)!=0) {
            if (!(content instanceof Boolean)) {
                if (content.toString().equalsIgnoreCase("true"))
                    content = new Boolean(true);
                else if (content.toString().equalsIgnoreCase("false"))
                    content = new Boolean(false);
            }
            if (!(content instanceof Boolean))
                drawCellContent(gc, rect, "", null, getForeground(), bgColor);
            else {
                boolean checked = ((Boolean)content).booleanValue();
                if (checked) {
                    if (clicked && (m_Style & INDICATION_CLICKED)!=0)
                        drawImage(gc, rect, IMAGE_CHECKED_CLICKED, bgColor);
                    else
                        drawImage(gc, rect, IMAGE_CHECKED, bgColor);
                }
                else {
                    if (clicked && (m_Style & INDICATION_CLICKED)!=0)
                        drawImage(gc, rect, IMAGE_UNCHECKED_CLICKED, bgColor);
                    else
                        drawImage(gc, rect, IMAGE_UNCHECKED, bgColor);
                }
            }
        }
        else { // draw image directly:
            if (!(content instanceof Boolean)) {
                if (content.toString().equalsIgnoreCase("true"))
                    content = new Boolean(true);
                else if (content.toString().equalsIgnoreCase("false"))
                    content = new Boolean(false);
            }
            if (!(content instanceof Boolean))
                drawCellContent(gc, rect, "", null, getForeground(), bgColor);
            else { 
                boolean checked = ((Boolean)content).booleanValue();
                if (clicked && (m_Style & INDICATION_CLICKED)!=0)
                    drawCheckedSymbol(gc, rect, checked, bgColor, COLOR_FILL);
                else
                    drawCheckedSymbol(gc, rect, checked, bgColor, bgColor);
            }
        }
    }
    /**
     * 
     * @param gc
     * @param rect
     * @param image
     * @param backgroundColor
     */
    protected void drawImage(GC gc, Rectangle rect, Image image, Color backgroundColor) {
        gc.setBackground(backgroundColor);
        gc.setForeground(backgroundColor);
        gc.fillRectangle(rect);
        SWTX.drawTextImage(gc, "", getAlignment(), 
                image, getAlignment(), rect.x + 3, rect.y,
                rect.width - 3, rect.height);
    }
	/**
	 * Manually paints the checked or unchecked symbol. This provides a fast replacement
	 * for the variant that paints the images defined in this class. <p>
	 * The reason for this is that painting manually is 2-3 times faster than painting the
	 * image - which is very notable if you have a completely filled table! (see example!)
	 * @param gc The GC to use when dawing
	 * @param rect The cell ara where the symbol should be painted into.
	 * @param checked Wether the symbol should be the checked or unchecked
	 * @param bgColor The background color of the cell.
	 * @param fillColor The color of the box drawn (with of without checked mark). Used
	 * when a click indication is desired.
	 */
	protected void drawCheckedSymbol(GC gc, Rectangle rect, boolean checked, Color bgColor, Color fillColor) {
	    // clear background:
   
	    // paint rectangle:
		Rectangle bound = getAlignedLocation(rect, IMAGE_CHECKED);

		gc.setForeground(BORDER_LIGHT);
		gc.drawLine(bound.x, bound.y, bound.x + bound.width, bound.y);
		gc.drawLine(bound.x, bound.y, bound.x, bound.y + bound.height);
		gc.setForeground(BORDER_DARK);
		gc.drawLine(bound.x + bound.width, bound.y + 1, bound.x + bound.width,
				bound.y + bound.height - 1);
		gc.drawLine(bound.x, bound.y + bound.height, bound.x + bound.width,
				bound.y + bound.height);

		if (!bgColor.equals(fillColor)) {
			gc.setBackground(fillColor);
			gc.fillRectangle(bound.x + 1, bound.y + 1, bound.width - 1,
					bound.height - 1);
		}
	    
	    if (checked) // draw a check symbol:	        
	        drawCheckSymbol(gc, bound);
	}
	/**
	 * Draws a X as a sign that the cell value is true.
     * @param gc The gc to use when painting
     * @param bound
     */
    private void drawCheckSymbol(GC gc, Rectangle bound) {
    	
        if ((m_Style & SIGN_X)!=0) { // Draw a X
            gc.setForeground(BORDER_LIGHT);
            
            gc.drawLine(bound.x+3, bound.y+2, bound.x-2+bound.width, bound.y-3+bound.height);
            gc.drawLine(bound.x+2, bound.y+3, bound.x-3+bound.width, bound.y-2+bound.height);
            
            gc.drawLine(bound.x+3, bound.y-2+bound.height, bound.x-2+bound.width, bound.y+3);
            gc.drawLine(bound.x+2, bound.y-3+bound.height, bound.x-3+bound.width, bound.y+2);
            
            gc.setForeground(COLOR_TEXT);
            
            gc.drawLine(bound.x+2, bound.y+2, bound.x-2+bound.width, bound.y-2+bound.height);
            gc.drawLine(bound.x+2, bound.y-2+bound.height, bound.x-2+bound.width, bound.y+2);
        } else { // Draw a check sign
            gc.setForeground(getForeground());
            
            gc.drawLine(bound.x+2, bound.y+bound.height-4, bound.x+4, bound.y+bound.height-2);
            gc.drawLine(bound.x+2, bound.y+bound.height-5, bound.x+5, bound.y+bound.height-3);
            gc.drawLine(bound.x+2, bound.y+bound.height-6, bound.x+4, bound.y+bound.height-4);
            
            for (int i=1; i<4; i++)
                gc.drawLine(bound.x+2+i, bound.y+bound.height-3, bound.x+bound.width-2, bound.y+1+i);
        }
    }

    /**
	 * Returns the location where the checked symbol should be painted.<p>
	 * Note that this is only a subarea of the area covered by an image, since
	 * the image contains a border area that is not needed here.
	 * @param rect The cell area
	 * @param img The image to take the size of the checked symbol from.
	 * @return Returns the area that should be filled with a checked/unchecked symbol.
	 */
	protected Rectangle getAlignedLocation(Rectangle rect, Image img) {
	    Rectangle bounds = img.getBounds();
	    bounds.x-=2;
	    bounds.y-=2;
	    bounds.height-=4;
	    bounds.width-=4;
	    
	    if ((getAlignment() & SWTX.ALIGN_HORIZONTAL_CENTER)!=0)
	        bounds.x = rect.x+(rect.width-bounds.width)/2;
	    else if ((getAlignment() & SWTX.ALIGN_HORIZONTAL_RIGHT)!=0)
	        bounds.x = rect.x+rect.width-bounds.width-2;
	    else
	        bounds.x = rect.x+2;
	    
	    if ((getAlignment() & SWTX.ALIGN_VERTICAL_CENTER)!=0)
	        bounds.y = rect.y+(rect.height-bounds.height)/2;
	    else if ((getAlignment() & SWTX.ALIGN_VERTICAL_BOTTOM)!=0)
	        bounds.y = rect.y+rect.height-bounds.height-2;
	    else
	        bounds.y = rect.y+2;
	    
	    return bounds;
	}
    /**
     * @return returns the currently set background color.
     * If none was set, the default value is returned.
     */
    public Color getBackground() {
        if (m_bgColor!=null)
            return m_bgColor;
        return COLOR_FIXEDBACKGROUND;
    }
}
