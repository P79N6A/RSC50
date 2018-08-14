/**
 * Copyright (c) 2007-2009 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based on IEC61850 SCT.
 */
package com.shrcn.business.scl.table.print;

import org.eclipse.swt.SWT;

/**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2009-11-18
 */
/**
 * $Log: PTableBoxProvider.java,v $
 * Revision 1.1  2010/03/02 07:49:13  cchun
 * Add:添加重构代码
 *
 * Revision 1.1  2009/11/19 08:28:52  cchun
 * Update:完成信号关联打印功能
 *
 */
/*
 * Copyright (C) 2004 by Friederich Kupzog Elektronik & Software
 * 
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 * 
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, write to the Free Software Foundation, Inc.,
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 * 
 * Author: Friederich Kupzog fkmk@kupzog.de www.kupzog.de/fkmk
 */

/**
 * Used by PTable to print KTables. Comparable to KTableCellRenderer. Creates a
 * PBox for a Table cell. It gets width and height information from the model
 * which are pixel values fro screen view. It may use these values as a
 * guideline.
 * 
 * @author Friederich Kupzog
 */
public class PTableBoxProvider {
	
	public static final int FIX_FONTSIZE = 6;
	public static final int FONTSIZE = 5;

	public PBox createBox(PContainer parent, int style, int col, int row,
			int widthFromModel, int heightFromModel, boolean fixed,
			Object content) {
		// create a text box
		PLittleTextBox box = new PLittleTextBox(parent, style, 0,
				widthFromModel * 0.03);

		// set its border lines
		PStyle boxStyle = PStyle.getDefaultStyle();
		boxStyle.lines = new double[] { 0.005, 0.01, 0.005, 0.0 };
		if (row == 0)
			boxStyle.lines[0] = 0.02;
		if (col == 0)
			boxStyle.lines[3] = 0.01;
		box.setBoxStyle(boxStyle);

		// set the font
		PTextStyle textStyle = PTextStyle.getDefaultStyle();
		if (fixed) // Header row / column
		{
			textStyle.setMarginLeft(0.08);
			textStyle.setMarginRight(0.08);
			textStyle.setMarginTop(0.1);
			textStyle.setMarginBottom(0.1);
			textStyle.fontSize = FIX_FONTSIZE;//9;
			textStyle.fontStyle = SWT.BOLD;
			textStyle.textAlign = PTextStyle.ALIGN_LEFT;
		} else // normal cell
		{
			textStyle.setMarginLeft(0.08);
			textStyle.setMarginRight(0.08);
			textStyle.setMarginTop(0.1);
			textStyle.setMarginBottom(0.1);
			textStyle.fontSize = FONTSIZE;//9;
			textStyle.fontStyle = SWT.NORMAL;
			textStyle.textAlign = PTextStyle.ALIGN_LEFT;
		}
		box.setTextStyle(textStyle);

		// set the text
		box.setText(content.toString());

		// ready
		return box;
	}

}
