/**
 * Copyright (c) 2007, 2008 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application 
 * based Visual Device Develop System.
 */

package com.shrcn.found.ui.table;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;

import com.shrcn.found.common.util.StringUtil;
import com.shrcn.found.ui.UIConstants;

import de.kupzog.ktable.KTableModel;
import de.kupzog.ktable.renderers.TextCellRenderer;

/**
 * 值颜色不同显示红色, 相同显示白色.
 * 
 * @author 周 泉
 * @version 1.0, 2011-5-18
 */
public class PercentRenderer extends TextCellRenderer {
	
	/**
	 * 颜色绘制器
	 */
	public PercentRenderer() {
		super(TextCellRenderer.INDICATION_FOCUS_ROW);
	}

	@Override
	public void drawCell(GC gc, Rectangle rect, int col, int row,
			Object content, boolean focus, boolean fixed, boolean clicked,
			KTableModel model) {
		applyFont(gc);
		
		Display display = Display.getCurrent();
		
		if (focus && (m_Style & INDICATION_FOCUS_ROW)!=0) {
            rect = drawDefaultSolidCellLine(gc, rect, COLOR_BGROWFOCUS, COLOR_BGROWFOCUS);
            // draw content:
            drawCellContent(gc, rect, "", null, COLOR_FGROWFOCUS, COLOR_BGROWFOCUS);
            
        } else {
            rect = drawDefaultSolidCellLine(gc, rect, COLOR_LINE_LIGHTGRAY, COLOR_LINE_LIGHTGRAY);
            // draw content:
            drawCellContent(gc, rect, "", null, getForeground(), getBackground());
        }
		
		String value = (String)model.getContentAt(col, row);
		int percent = StringUtil.isEmpty(value) ? 0 : Integer.parseInt(value) ;
		Color background = gc.getBackground();
		if (percent > 0) {
			gc.setForeground(new Color(null, 49,106,197));
			gc.setBackground(UIConstants.WHITE);
			
			int width = (rect.width) * percent / 100;
			
			gc.fillGradientRectangle(rect.x, rect.y, width,
					rect.height, true);
			Rectangle rect2 = new Rectangle(rect.x, rect.y,
					width, rect.height);
			
			gc.drawRectangle(rect2);
			gc.setForeground(display.getSystemColor(SWT.COLOR_LIST_FOREGROUND));
		}
		
		String text = percent + "%";
		Point size = gc.textExtent(text);
		
		int offset = Math.max(0, (rect.height - size.y) / 2);
		gc.drawText(text, rect.x + 2, rect.y + offset, true);
		
		gc.setForeground(background);
		gc.setBackground(display.getSystemColor(SWT.COLOR_WHITE));

		if ((m_Style & INDICATION_COMMENT) != 0)
			drawCommentSign(gc, rect);

		resetFont(gc);
	}
}
