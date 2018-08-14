/**
 * Copyright (c) 2007-2009 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based on IEC61850 SCT.
 */
package com.shrcn.business.scl.table.print;

/*  Copyright (C) 2004 by Friederich Kupzog Elektronik & Software

 This library is free software; you can redistribute it and/or
 modify it under the terms of the GNU Lesser General Public
 License as published by the Free Software Foundation; either
 version 2.1 of the License, or (at your option) any later version.

 This library is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 Lesser General Public License for more details.

 You should have received a copy of the GNU Lesser General Public
 License along with this library; if not, write to the Free Software
 Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA

 Author: Friederich Kupzog  
 fkmk@kupzog.de
 www.kupzog.de/fkmk
 */

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.PaletteData;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.printing.PrintDialog;
import org.eclipse.swt.printing.Printer;
import org.eclipse.swt.printing.PrinterData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;

import com.shrcn.found.ui.util.ImageConstants;
import com.shrcn.found.ui.util.ImgDescManager;

/**
 * This example shows how to print data from a PrintKTableModel. More information
 * about this can be found in the text that is produced by KPrintExample.java.
 * 
 * @author Friederich Kupzog
 * 
 */
public class PrintKTable {
//	private Display d;

	public PrintKTable() {

//		d = new Display();

//		PageSetup.marginStyle = PageSetup.MARGIN_SMALL;
//		
//		// create a document with default settings from PageSetup
//		PDocument doc = new PDocument("KTable printing example");

//		// put some header text on it
//		PTextBox t;
//		t = new PTextBox(doc);
//		t.setText("KTABLE PRINTING EXAMPLE");
//		new PVSpace(doc, 0.1);
//		new PHLine(doc, 0.02, SWT.COLOR_BLACK);
//		new PVSpace(doc, 0.5);

		// create the table
//		PTable table = new PTable(doc);
//		VTViewPTable table = new VTViewPTable(doc);
//		table.setModel(new ExampleTableModel());
//		table.setBoxProvider(new PTableBoxProvider());
//
//		PrintPreview pr = new PrintPreview(null, "打印预览", IconSource.PRINT, doc);
//		pr.open();
//		d.dispose();
	}

	/**
	 * This function would print the document witout the print preview.
	 * 
	 * @param doc
	 */
	public void print(PDocument doc) {
		PrintDialog dialog = new PrintDialog(null, SWT.BORDER);
		PrinterData data = dialog.open();
		if (data == null)
			return;
		if (data.printToFile) {
			data.fileName = "print.out"; // you probably want to ask the user //$NON-NLS-1$
			// for a filename
		}

		Printer printer = new Printer(data);
		GC gc = new GC(printer);
		PBox.setParameters(gc, printer, printer.getDPI(), 100);
		if (printer.startJob("DoSys Druckauftrag")) { //$NON-NLS-1$
			printer.startPage();
			doc.layout();
			doc.draw(1);
			printer.endJob();
		}
		gc.dispose();

	}

//	public static void main(String[] args) {
//		new PrintKTable();
//	}
}

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

/*
 * This feature was contributed by Onsel Armagan, Istanbul, Turkey Thanks a lot!
 */
class SWTPTable {

	protected Table table;

	protected PTableBoxProvider boxProvider;

	protected PContainer parent;

	public SWTPTable(PContainer parent) {
		this.parent = parent;
	}

	protected void fillDocument() {
		boolean abgeschnitten = false;

		calculatePageLengths();
		// Zeilen
		double width = parent.getPossibleWidth();

		for (int j = 0; j < table.getColumnCount(); j++) {
			// System.out.println(" Zeile "+j);
			int height = table.getHeaderHeight();

			int style = PBox.POS_RIGHT | PBox.ROW_ALIGN;
			if (j == 0)
				style = PBox.POS_BELOW | PBox.ROW_ALIGN;
			PBox box = boxProvider.createBox(parent, style, j, 0, table
					.getColumn(j).getWidth(), height, true, table.getColumn(j)
					.getText());
			double boxWidth = Math.max(box.minCm, parent.getPossibleWidth()
					* box.hWeight);
			width -= boxWidth;
			if (width < 0) {
				box.dispose();
				abgeschnitten = true;
				break;
			}
		}

		for (int i = 0; i < table.getItemCount(); i++) {
			// System.out.println("Spalte "+i);
			int height = table.getItemHeight();
			width = parent.getPossibleWidth();

			// Spalten
			for (int j = 0; j < table.getColumnCount(); j++) {
				// System.out.println(" Zeile "+j);
				int style = PBox.POS_RIGHT | PBox.ROW_ALIGN;
				if (j == 0)
					style = PBox.POS_BELOW | PBox.ROW_ALIGN;
				PBox box = boxProvider.createBox(parent, style, j, i, table
						.getColumn(j).getWidth(), height, false, table.getItem(
						i).getText(j));
				double boxWidth = Math.max(box.minCm, parent.getPossibleWidth()
						* box.hWeight);
				width -= boxWidth;
				if (width < 0) {
					box.dispose();
					abgeschnitten = true;
					break;
				}
			}
		}
		if (abgeschnitten)
			MsgBox.show("Tabelle ist zu breit fur die Seite\n" //$NON-NLS-1$
					+ "und wird deshalb abgeschnitten."); //$NON-NLS-1$

	}

	public void calculatePageLengths() {
		if (table != null) {
			PDocument doc = (PDocument) parent;

			double width = parent.getPossibleWidth();

			for (int j = 0; j < table.getColumnCount(); j++) {
				double boxWidth = Math.max(0,
						table.getColumn(j).getWidth() * 0.03);
				width -= boxWidth;
				if (width < 0) {
					break;
				}
			}
			if (width < 0) {
				doc.setPageHeight(PageSetup.paperWidth);
				doc.setPageWidth(PageSetup.paperHeight);
			}

		}

	}

	/**
	 * @return PTableBoxProvider
	 */
	public PTableBoxProvider getBoxProvider() {
		return boxProvider;
	}

	/**
	 * @return PrintKTableModel
	 */
	public Table getTable() {
		return table;
	}

	/**
	 * Sets the boxProvider.
	 * 
	 * @param boxProvider
	 *            The boxProvider to set
	 */
	public void setBoxProvider(PTableBoxProvider boxProvider) {
		this.boxProvider = boxProvider;
		if (this.boxProvider != null && this.table != null) {
			fillDocument();
		}
	}

	/**
	 * Sets the table.
	 * 
	 * @param table
	 *            The table to set
	 */
	public void setTable(Table table) {
		this.table = table;
		if (this.boxProvider != null && this.table != null) {
			fillDocument();
		}
	}

}

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
 * Vertical whitspace.
 * 
 * @author Friederich Kupzog
 */
class PVSpace extends PBox {

	private double cm;

	/**
	 * Creates a new Space
	 */
	public PVSpace(PContainer parent, double cm) {
		super(parent);
		this.cm = cm;
		// getBoxStyle().backColor = SWT.COLOR_CYAN;
	}

	/*
	 * overridden from superclass
	 */
	protected int getWidth() {
		// return 1;
		return 0;
	}

	/*
	 * overridden from superclass
	 */
	protected int getHeight() {
		if (forcedHeight > 0)
			return forcedHeight;
		return PBox.pixelY(cm);
	}

}

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
 * A style for printable objects that that contain text.
 * 
 * @author Friederich Kupzog
 */
class PTextStyle {

	public static final int ALIGN_LEFT = 1;

	public static final int ALIGN_RIGHT = 2;

	public static final int ALIGN_CENTER = 3;

	protected static HashMap<String, Font> fonts = new HashMap<String, Font>();

	public int fontSize;

	public String fontName;

	public int fontStyle;

	public int fontColor;

	public int textAlign;

	protected double marginLeft;

	protected double marginRight;

	protected double marginTop;

	protected double marginBottom;

	public PTextStyle() {
		fontName = "Arial"; //$NON-NLS-1$
		fontStyle = SWT.NORMAL;
		fontSize = 10;
		fontColor = SWT.COLOR_BLACK;
		textAlign = ALIGN_LEFT;

		marginLeft = 0.0;
		marginRight = 0.0;

		marginTop = 0.0;
		marginBottom = 0.0;

	}

	public static void disposeFonts() {
		for (Font element : fonts.values()) 
			element.dispose();
		fonts.clear();
	}

	public static PTextStyle getDefaultStyle() {
		return new PTextStyle();
	}

	public Font getFont() {
		int height = Math.abs(fontSize * PBox.scalingPercent / 100);
		String key = PBox.device.getDPI().x + "|" + PBox.device.getDPI().y //$NON-NLS-1$
				+ "|" + fontName + "|" + height + "|" + fontStyle; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		Font font = (Font) fonts.get(key);
		if (font != null)
			return font;
		font = new Font(PBox.device, fontName, Math.abs(fontSize
				* PBox.scalingPercent / 100), fontStyle);
		fonts.put(key, font);
		return font;
	}

	public Color getFontColor() {
		return PBox.device.getSystemColor(fontColor);
	}

	/**
	 * @return double
	 */
	public double getMarginLeft() {
		return marginLeft;
	}

	/**
	 * @return double
	 */
	public double getMarginRight() {
		return marginRight;
	}

	/**
	 * Sets the marginLeft.
	 * 
	 * @param marginLeft
	 *            The marginLeft to set
	 */
	public void setMarginLeft(double marginLeft) {
		this.marginLeft = marginLeft;
	}

	/**
	 * Sets the marginRight.
	 * 
	 * @param marginRight
	 *            The marginRight to set
	 */
	public void setMarginRight(double marginRight) {
		this.marginRight = marginRight;
	}

	/**
	 * @return double
	 */
	public double getMarginBottom() {
		return marginBottom;
	}

	/**
	 * @return double
	 */
	public double getMarginTop() {
		return marginTop;
	}

	/**
	 * Sets the marginBottom.
	 * 
	 * @param marginBottom
	 *            The marginBottom to set
	 */
	public void setMarginBottom(double marginBottom) {
		this.marginBottom = marginBottom;
	}

	/**
	 * Sets the marginTop.
	 * 
	 * @param marginTop
	 *            The marginTop to set
	 */
	public void setMarginTop(double marginTop) {
		this.marginTop = marginTop;
	}

}

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
 * A printable text label that can occupy more than one page. If you are shure
 * that the box will not be bigger than one page, you can use PLittleTextBox to
 * make the layout process quicker.
 * 
 * @author Friederich Kupzog For more details
 * @see PDocument and
 * @see PBox
 */
class PTextBox extends PBox {

	protected String text;

	protected PTextStyle textStyle;

	// multi-page
	protected ArrayList<PTextPart> pageList;

	protected ArrayList<String> textLines;

	protected int unplacedLines;

	/**
	 * Creates a non-wrapping text box with a fixed size according to its text.
	 * 
	 * @param parent
	 * @param style
	 */
	public PTextBox(PContainer parent) {
		super(parent);
		init();
	}

	/**
	 * Creates a non-wrapping text box with a fixed size according to its text.
	 * 
	 * @param parent
	 * @param style
	 */
	public PTextBox(PContainer parent, int style) {
		super(parent, style);
		init();
	}

	/**
	 * Creates a text box with wrapping capabilities if hWeight is > 0.
	 * 
	 * @param parent
	 * @param style
	 * @param hWeight
	 *            Specify -1 for a non-wrapping text box (If the text has
	 *            newlines it will be a multi-line textbox). Spezify a number
	 *            between 0 and 1 for a multiline textbox that consumes the
	 *            given fraction of the available document width.
	 * @param minWidth
	 *            This allows you to specify a minimum width for the text. The
	 *            text box will consume some space depending to hWeight or its
	 *            text if hWeight is -1, but at least the given amount of
	 *            centimeters. For a box with a fixed width for example set
	 *            hWeigth = 0 and specify a non-zero minWidth.
	 */
	public PTextBox(PContainer parent, int style, double hWeight,
			double minWidth) {
		super(parent, style, hWeight, minWidth);
		init();
	}

	private void init() {
		text = ""; //$NON-NLS-1$
		textStyle = PTextStyle.getDefaultStyle();
		pageList = new ArrayList<PTextPart>();
		textLines = new ArrayList<String>();
		unplacedLines = 0;
	}

	public void setText(String text) {
		if (text == null)
			text = ""; //$NON-NLS-1$
		this.text = text;
	}

	protected int layoutHowMuchWouldYouOccupyOf(Point spaceLeft, int page) {
		if (textLines.size() == 0)
			splitIntoLines();
		if (unplacedLines == 0)
			return 0;

		gc.setFont(textStyle.getFont());
		int lineHeight = gc.stringExtent("A").y; //$NON-NLS-1$
		// System.out.println("LineH: "+lineHeight+" Space: "+spaceLeft.y);
		int erg = 0;

		int ctr = 0;
		do {
			erg += lineHeight;
			ctr++;
			if (ctr == unplacedLines)
				break;
		} while (erg + lineHeight <= spaceLeft.y);

		if (erg > spaceLeft.y)
			return -1;
		return erg;
	}

	/*
	 * overridden from superclass
	 */
	protected boolean layoutWouldYouFinishWithin(Point spaceLeft, int page) {
		gc.setFont(textStyle.getFont());
		int lineHeight = gc.stringExtent("A").y; //$NON-NLS-1$
		return ((unplacedLines * lineHeight) <= spaceLeft.y);
	}

	/*
	 * overridden from superclass
	 */
	protected int layoutOccupy(Point origin, Point spaceLeft, int page) {
		if (textLines.size() == 0)
			splitIntoLines();
		if (unplacedLines == 0)
			return 0;
		if (this.origin.page == 0) {
			this.origin.page = page;
			this.origin.x = origin.x;
			this.origin.y = origin.y;
		}

		gc.setFont(textStyle.getFont());
		int lineHeight = gc.stringExtent("A").y; //$NON-NLS-1$
		int erg = 0;

		int ctr = 0;
		do {
			erg += lineHeight;
			ctr++;
			if (ctr == unplacedLines)
				break;
		} while (erg + lineHeight <= spaceLeft.y);

		if (erg > spaceLeft.y)
			return 0;

		PTextPart part = new PTextPart();
		part.numOfLines = ctr;
		part.origin = new Point(origin.x, origin.y);
		part.startLine = textLines.size() - unplacedLines;
		pageList.add(part);

		unplacedLines -= ctr;

		return erg;
	}

	/*
	 * overridden from superclass
	 */
	protected boolean layoutIsOnPage(int page) {
		if (page >= origin.page && page < origin.page + pageList.size())
			return true;
		return false;
	}

	/*
	 * overridden from superclass
	 */
	protected int getWidth() {
		if (grabbing)
			return grabWidth;
		if (hWeight >= 0 && hWeight <= 1 && minCm >= 0) {
			PDocument myDoc = getDocument();
			double maxWidthCm = (myDoc.pageWidth - myDoc.margins[1] - myDoc.margins[3])
					* hWeight;
			return Math.max(pixelX(maxWidthCm), pixelX(minCm));
		}

		gc.setFont(textStyle.getFont());
		if (textLines.size() == 0)
			splitIntoLines();
		int erg = 0;
		for (String element : textLines) {
			int w = gc.stringExtent(element).x;
			if (w > erg)
				erg = w;
		}
		erg += pixelX(textStyle.getMarginLeft());
		erg += pixelX(textStyle.getMarginRight());
		erg = Math.max(erg, pixelX(minCm));
		return erg;
	}

	protected void splitIntoLines() {
		textLines.clear();
		gc.setFont(textStyle.getFont());

		if ((grabWidth > 0) || (hWeight >= 0 && hWeight <= 1)) {
			PDocument myDoc = getDocument();
			int maxWidth;

			if (grabWidth > 0)
				maxWidth = grabWidth;
			else {
				double maxWidthCm = (myDoc.pageWidth - myDoc.margins[1] - myDoc.margins[3])
						* hWeight;
				maxWidth = Math.max(pixelX(maxWidthCm), pixelX(minCm));
			}
			maxWidth -= pixelX(textStyle.getMarginLeft());
			maxWidth -= pixelX(textStyle.getMarginRight());

			boolean fertig = false;
			int start = 0;
			int pos = 0;
			int lastPossibility = start;

			if (text.length() > 0) {
				while (!fertig) {
					int textLength = 0;
					while (!fertig && textLength < maxWidth) {
						if (text.charAt(pos) == ' ')
							lastPossibility = pos;
						if (text.charAt(pos) == '-')
							lastPossibility = pos;
						if (text.charAt(pos) == '\n') {
							textLines.add(text.substring(start, pos).trim());
							start = pos + 1;
							pos = start;
						}
						int testPos = pos + 1;
						if (testPos > text.length())
							testPos = text.length();
						textLength = gc.stringExtent(text.substring(start,
								testPos)).x;
						if (textLength < maxWidth)
							pos++;
						if (pos >= text.length()) {
							fertig = true;
						}
					}

					int umbruchPos = pos;
					if (lastPossibility > start && !fertig)
						umbruchPos = lastPossibility + 1;

					textLines.add(text.substring(start, umbruchPos));

					if (!fertig) {
						start = umbruchPos;
						if (start >= text.length()) {
							fertig = true;
						} else {
							pos = start;
							lastPossibility = start;
						}
					}
				}
			}

		} else {
			textLines.add(text);
		}
		unplacedLines = textLines.size();
	}

	/*
	 * overridden from superclass
	 */
	protected void layoutResetTuning() {
		super.layoutResetTuning();
		pageList.clear();
		textLines.clear();
	}

	/*
	 * overridden from superclass
	 */
	protected int getHeight(int page) {
		gc.setFont(textStyle.getFont());
		int lineHeight = gc.stringExtent("A").y; //$NON-NLS-1$

		PTextPart part = (PTextPart) pageList.get(page - origin.page);
		return part.numOfLines * lineHeight;
	}

	public void draw(int page, Point originOffset) {

		if (layoutIsOnPage(page)) {
			PTextPart part = (PTextPart) pageList.get(page - origin.page);
			this.origin = new PagePoint(part.origin, origin.page);
			super.draw(page, originOffset);
			Font font = textStyle.getFont();
			gc.setFont(font);
			gc.setForeground(textStyle.getFontColor());

			int lineHeight = gc.stringExtent("A").y; //$NON-NLS-1$

			for (int i = 0; i < part.numOfLines; i++) {

				int alignPixel = 0;
				if (textStyle.textAlign == PTextStyle.ALIGN_CENTER) {
					int textWidth = gc.stringExtent((String) textLines
							.get(part.startLine + i)).x;
					alignPixel = (getWidth()
							- pixelX(textStyle.getMarginLeft())
							- pixelX(textStyle.getMarginRight()) - textWidth) / 2;
				} else if (textStyle.textAlign == PTextStyle.ALIGN_RIGHT) {
					gc.setFont(font);
					int textWidth = gc.stringExtent((String) textLines
							.get(part.startLine + i)).x;
					alignPixel = (getWidth()
							- pixelX(textStyle.getMarginLeft())
							- pixelX(textStyle.getMarginRight()) - textWidth);
					// System.out.println("'"+(String)textLines.get(part.startLine
					// + i)+"' I="+i+" width = "+textWidth+ "
					// align="+alignPixel);
				}

				gc
						.drawText((String) textLines.get(part.startLine + i),
								part.origin.x + alignPixel + originOffset.x
										+ pixelX(textStyle.getMarginLeft()),
								part.origin.y + originOffset.y
										+ (i * lineHeight), true);

			}

		}
	}

	/**
	 * @return PTextStyle
	 */
	public PTextStyle getTextStyle() {
		return textStyle;
	}

	/**
	 * Sets the textStyle.
	 * 
	 * @param textStyle
	 *            The textStyle to set
	 */
	public void setTextStyle(PTextStyle textStyle) {
		this.textStyle = textStyle;
	}

}

class PTextPart {
	public Point origin;

	public int startLine;

	public int numOfLines;
}

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
 * A style for printable objects.
 * 
 * @author Friederich Kupzog
 */
class PStyle {

	public double[] lines;

	public int lineColor;

	public int backColor;

	public PStyle() {
		lines = new double[4];
		lines[0] = 0.0;
		lines[1] = 0.0;
		lines[2] = 0.0;
		lines[3] = 0.0;
		lineColor = SWT.COLOR_BLACK;
		backColor = SWT.COLOR_WHITE;

		// setDebugStyle();
	}

	public static PStyle getDefaultStyle() {
		return new PStyle();
	}

	public int getLineWidth(int num) {
		int pixel = 0;
		if (num == 0 || num == 2)
			pixel = PBox.pixelY(lines[num]);
		if (num == 1 || num == 3)
			pixel = PBox.pixelX(lines[num]);
		if (pixel < 0)
			return 0;
		if (pixel == 0)
			return 1;
		return pixel;
	}

	public boolean hasLine(int num) {
		return lines[num] > 0;
	}

	public Color getLineColor() {
		return PBox.device.getSystemColor(lineColor);
	}

	public Color getBackColor() {
		return PBox.device.getSystemColor(backColor);
	}

}

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
 * @author Friederich Kupzog A PTextStyle that is easy to create.
 */
class PSimpleTextStyle extends PTextStyle {
	public PSimpleTextStyle(String fontname, int size, boolean bold) {
		super();
		this.fontName = fontname;
		this.fontSize = size;
		if (bold)
			this.fontStyle = SWT.BOLD;
	}

}

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
 * A text box that shows the current page number.
 * 
 * @author Friederich Kupzog
 */
class PPageNumber extends PTextBox {
	protected static int pageNumber = 0;

	/**
	 * @param parent
	 * @param style
	 */
	public PPageNumber(PContainer parent, int style) {
		super(parent, style, -1, 0);
		setText("    "); //$NON-NLS-1$
	}

	public void draw(int page, Point originOffset) {

		if (layoutIsOnPage(page)) {
			super.draw(page, originOffset);
			gc.setFont(textStyle.getFont());
			gc.setForeground(textStyle.getFontColor());
			gc.drawText("" + pageNumber, origin.x + originOffset.x //$NON-NLS-1$
					+ pixelX(textStyle.getMarginLeft()), origin.y
					+ originOffset.y, true);
		}
	}

}

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
 * forces a page break at the current position in the tree of printable elements
 * 
 * @author Friederich Kupzog
 */
class PPageBreak extends PBox {

	public PPageBreak(PContainer parent) {
		super(parent);
	}

	/*
	 * overridden from superclass
	 */
	protected int getWidth() {
		return 0;
	}

	/*
	 * overridden from superclass
	 */
	protected int layoutHowMuchWouldYouOccupyOf(Point spaceLeft, int page) {
		return -1;
	}

	/*
	 * overridden from superclass
	 */
	public void draw(int page, Point originOffset) {
	}

}

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
 * A printable text label. If you need page breaks within the text of a box, use
 * PTextBox instead. CAUTION: A PLittleTextBox with too much text for one entire
 * page causes the layout process to hang in an endless loop.
 * 
 * @author Friederich Kupzog For more details
 * @see PDocument and
 * @see PBox
 */
class PLittleTextBox extends PBox {

	protected String text;

	protected PTextStyle textStyle;

	protected ArrayList<String> textLines;

	/**
	 * Creates a non-wrapping text box with a fixed size according to its text.
	 * 
	 * @param parent
	 * @param style
	 */
	public PLittleTextBox(PContainer parent) {
		super(parent);
		init();
	}

	/**
	 * Creates a non-wrapping text box with a fixed size according to its text.
	 * 
	 * @param parent
	 * @param style
	 */
	public PLittleTextBox(PContainer parent, int style) {
		super(parent, style);
		init();
	}

	/**
	 * Creates a text box with wrapping capabilities if hWeight is > 0.
	 * 
	 * @param parent
	 * @param style
	 * @param hWeight
	 *            Specify -1 for a non-wrapping text box (If the text has
	 *            newlines it will be a multi-line textbox). Spezify a number
	 *            between 0 and 1 for a multiline textbox that consumes the
	 *            given fraction of the available document width.
	 * @param minWidth
	 *            This allows you to specify a minimum width for the text. The
	 *            text box will consume some space depending to hWeight or its
	 *            text if hWeight is -1, but at least the given amount of
	 *            centimeters. For a box with a fixed width for example set
	 *            hWeigth = 0 and specify a non-zero minWidth.
	 */
	public PLittleTextBox(PContainer parent, int style, double hWeight,
			double minWidth) {
		super(parent, style, hWeight, minWidth);
		init();
	}

	private void init() {
		text = ""; //$NON-NLS-1$
		textStyle = PTextStyle.getDefaultStyle();
		textLines = new ArrayList<String>();
	}

	public void setText(String text) {
		if (text == null)
			text = ""; //$NON-NLS-1$
		this.text = text;
	}

	/*
	 * overridden from superclass
	 */
	protected int getWidth() {
		if (grabbing)
			return grabWidth;
		if (hWeight >= 0 && hWeight <= 1 && minCm >= 0) {
			PDocument myDoc = getDocument();
			double maxWidthCm = (myDoc.pageWidth - myDoc.margins[1] - myDoc.margins[3])
					* hWeight;
			return Math.max(pixelX(maxWidthCm), pixelX(minCm));
		}

		gc.setFont(textStyle.getFont());
		if (textLines.size() == 0)
			splitIntoLines();
		int erg = 0;
		for (String element : textLines) {
			int w = gc.stringExtent(element).x;
			if (w > erg)
				erg = w;
		}
		erg += pixelX(textStyle.getMarginLeft());
		erg += pixelX(textStyle.getMarginRight());
		erg = Math.max(erg, pixelX(minCm));
		return erg;
	}

	protected void splitIntoLines() {
		textLines.clear();
		gc.setFont(textStyle.getFont());

		if ((grabWidth > 0) || (hWeight >= 0 && hWeight <= 1)) {
			PDocument myDoc = getDocument();
			int maxWidth;

			if (grabWidth > 0)
				maxWidth = grabWidth;
			else {
				double maxWidthCm = (myDoc.pageWidth - myDoc.margins[1] - myDoc.margins[3])
						* hWeight;
				maxWidth = Math.max(pixelX(maxWidthCm), pixelX(minCm));
			}
			maxWidth -= pixelX(textStyle.getMarginLeft());
			maxWidth -= pixelX(textStyle.getMarginRight());

			boolean fertig = false;
			int start = 0;
			int pos = 0;
			int lastPossibility = start;

			if (text.length() > 0) {
				while (!fertig) {
					int textLength = 0;
					while (!fertig && textLength < maxWidth) {
						if (text.charAt(pos) == ' ')
							lastPossibility = pos;
						if (text.charAt(pos) == '-')
							lastPossibility = pos;
						if (text.charAt(pos) == '\n') {
							textLines.add(text.substring(start, pos));
							start = pos + 1;
							pos = start;
						}
						int testPos = pos + 1;
						if (testPos > text.length())
							testPos = text.length();
						textLength = gc.stringExtent(text.substring(start,
								testPos)).x;
						if (textLength < maxWidth)
							pos++;
						if (pos >= text.length()) {
							fertig = true;
						}
					}

					int umbruchPos = pos;
					if (lastPossibility > start && !fertig)
						umbruchPos = lastPossibility + 1;

					textLines.add(text.substring(start, umbruchPos));

					if (!fertig) {
						start = umbruchPos;
						if (start >= text.length()) {
							fertig = true;
						} else {
							pos = start;
							lastPossibility = start;
						}
					}
				}
			}

		} else {
			textLines.add(text);
		}
	}

	/*
	 * overridden from superclass
	 */
	protected void layoutResetTuning() {
		super.layoutResetTuning();
		textLines.clear();
	}

	/*
	 * overridden from superclass
	 */
	protected int getHeight() {
		if (forcedHeight > 0)
			return forcedHeight;
		if (textLines.size() == 0)
			splitIntoLines();
		gc.setFont(textStyle.getFont());
		int lineHeight = gc.stringExtent("A").y; //$NON-NLS-1$

		return (textLines.size() * lineHeight)
				+ pixelY(textStyle.getMarginTop() + textStyle.getMarginBottom());
	}

	public void draw(int page, Point originOffset) {

		if (layoutIsOnPage(page)) {
			super.draw(page, originOffset);
			Font font = textStyle.getFont();
			gc.setFont(font);
			gc.setForeground(textStyle.getFontColor());

			int lineHeight = gc.stringExtent("A").y; //$NON-NLS-1$

			for (int i = 0; i < textLines.size(); i++) {

				int alignPixel = 0;
				if (textStyle.textAlign == PTextStyle.ALIGN_CENTER) {
					int textWidth = gc.stringExtent((String) textLines.get(i)).x;
					alignPixel = (getWidth()
							- pixelX(textStyle.getMarginLeft())
							- pixelX(textStyle.getMarginRight()) - textWidth) / 2;
				} else if (textStyle.textAlign == PTextStyle.ALIGN_RIGHT) {
					int textWidth = gc.stringExtent((String) textLines.get(i)).x;
					alignPixel = (getWidth()
							- pixelX(textStyle.getMarginLeft())
							- pixelX(textStyle.getMarginRight()) - textWidth);
				}

				gc.drawText((String) textLines.get(i), origin.x + alignPixel
						+ originOffset.x + pixelX(textStyle.getMarginLeft()),
						origin.y + originOffset.y
								+ pixelY(textStyle.getMarginTop())
								+ (i * lineHeight), true);

			}

		}
	}

	/**
	 * @return PTextStyle
	 */
	public PTextStyle getTextStyle() {
		return textStyle;
	}

	/**
	 * Sets the textStyle.
	 * 
	 * @param textStyle
	 *            The textStyle to set
	 */
	public void setTextStyle(PTextStyle textStyle) {
		this.textStyle = textStyle;
	}

}

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
 * A printable Image label.
 * 
 * @author Friederich Kupzog For more details
 * @see PDocument
 */
class PImageBox extends PBox {

	protected String imgName;

	protected int imgDPI;

	protected Image image;

	protected Point imgOriginalSize;

	protected Point imgTargetSize;

	/**
	 * @param parent
	 * @param style
	 */
	public PImageBox(PContainer parent, int style) {
		super(parent, style);
	}

	/**
	 * Sets the Image. The size of the image will be calculated by using the dpi
	 * parameter, in which you can specify which resolution is the "native"
	 * image resulotion. If you e.g. specify 600 dpi and print on a 600 dpi
	 * printer, the image will not be resized. If you print it on an 300 dpi
	 * printer, it will be resized.
	 * 
	 * @param name
	 * @param dpi
	 */
	public void setImage(String name, int dpi) {
		this.imgName = name;
		this.imgDPI = dpi;
		image = null;
		imgOriginalSize = null;
		imgTargetSize = null;
	}

	/*
	 * overridden from superclass
	 */
	protected void layoutResetTuning() {
		super.layoutResetTuning();
		image = null;
		imgOriginalSize = null;
		imgTargetSize = null;
	}

	protected Point calcSize() {
		try {
			Class<?> clazz = new Object().getClass();
			InputStream is = clazz.getResourceAsStream(imgName);
			image = new Image(device, is);

			imgOriginalSize = new Point(0, 0);
			imgOriginalSize.x = image.getImageData().width;
			imgOriginalSize.y = image.getImageData().height;

			imgTargetSize = new Point(0, 0);
			imgTargetSize.x = (int) (imgOriginalSize.x * scalingPercent / 100 * (pixelPerInch.x / (double) imgDPI));
			imgTargetSize.y = (int) (imgOriginalSize.y * scalingPercent / 100 * (pixelPerInch.y / (double) imgDPI));

			sizeCalculatedfor = scalingPercent;

			image.getImageData().transparentPixel = -1;
			image.getImageData().maskData = null;

			return imgTargetSize;

		} catch (Exception e1) {
			System.out.println("could not open ressource " + imgName); //$NON-NLS-1$
			imgOriginalSize = new Point(10, 10);
			imgTargetSize = new Point(10, 10);
			return imgTargetSize;
		}

	}

	/*
	 * overridden from superclass
	 */
	protected int layoutHowMuchWouldYouOccupyOf(Point spaceLeft, int page) {
		if (layoutAlreadyFinished())
			return 0;
		if (sizeCalculatedfor != scalingPercent || image == null) {
			calcSize();
		}
		// System.out.println("Size: "+imgTargetSize.y+" Space: "+spaceLeft.y);
		if (imgTargetSize.y > spaceLeft.y)
			return -1;
		return imgTargetSize.y;
	}

	/*
	 * overridden from superclass
	 */
	protected int getWidth() {
		if (sizeCalculatedfor != scalingPercent || image == null) {
			calcSize();
		}
		return imgTargetSize.x;
	}

	/*
	 * overridden from superclass
	 */
	protected int getHeight() {
		if (rowAlign)
			return forcedHeight;
		if (sizeCalculatedfor != scalingPercent || image == null) {
			calcSize();
		}
		return imgTargetSize.y;
	}

	/*
	 * overridden from superclass
	 */
	protected int layoutOccupy(Point origin, Point spaceLeft, int page) {
		if (layoutAlreadyFinished())
			return 0;
		if (sizeCalculatedfor != scalingPercent || image == null) {
			calcSize();
		}
		this.origin = new PagePoint(origin, page);
		return imgTargetSize.y;
	}

	public void draw(int page, Point originOffset) {
		if (layoutIsOnPage(page)) {
			super.draw(page, originOffset);
			if (image != null) {
				gc.drawImage(image, 0, 0, imgOriginalSize.x, imgOriginalSize.y,
						origin.x + originOffset.x, origin.y + originOffset.y,
						imgTargetSize.x, imgTargetSize.y);
			}
		}
	}

}

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
 * Horizontal white space
 * 
 * @author Friederich Kupzog
 */
class PHSpace extends PBox {

	private double cm;

	/**
	 * Creates a new Space
	 */
	public PHSpace(PContainer parent, int style, double cm) {
		super(parent, style);
		this.cm = cm;
		// getBoxStyle().backColor = SWT.COLOR_GREEN;
	}

	/*
	 * overridden from superclass
	 */
	protected int getWidth() {
		if (grabbing)
			return grabWidth;
		return PBox.pixelX(cm);
	}

	protected int getHeight() {
		if (forcedHeight > 0)
			return forcedHeight;
		// return 2;
		return 0;
	}

}

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
 * A horzontal line
 * 
 * @author Friederich Kupzog
 */
class PHLine extends PBox {

	protected double thickness;

	protected int color;

	/**
	 * Creates a horizontal line with the given thickness and color.
	 */
	public PHLine(PContainer parent, double thickness, int color) {
		super(parent, POS_BELOW, 1.0, 0.0);
		this.thickness = thickness;
		boxStyle.lines = new double[] { 0.0, 0.0, 0.0, 0.0 };
		boxStyle.backColor = color;
	}

	/**
	 * Creates a black thin horizontal line.
	 */
	public PHLine(PContainer parent) {
		super(parent, POS_BELOW, 1.0, 0.0);
		this.thickness = 0.01;
		boxStyle.lines = new double[] { 0.0, 0.0, 0.0, 0.0 };
		boxStyle.backColor = SWT.COLOR_BLACK;
	}

	protected int getHeight() {
		if (forcedHeight > 0)
			return forcedHeight;

		int erg = PBox.pixelY(thickness);
		if (erg == 0)
			return 1;
		return erg;
	}

}

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
 * Abstract superclass for all printable Objects.
 * 
 * For more details
 * 
 * @see PDocument
 * @author Friederich Kupzog
 */
class PBox {
	// Styles
	/**
	 * Style flag that forces the PBox to be in a new line below the previous
	 * one Cannot be used simultaniously to POS_RIGHT.
	 */
	public static final int POS_BELOW = 1;

	/**
	 * Style flag that forces the PBox to be in line with the previous one.
	 * Cannot be used simultaniously to POS_BELOW.
	 */
	public static final int POS_RIGHT = 2;

	/**
	 * Style flag that can be used additionally to POS_BELOW/POS_RIGHT to tell
	 * the PBox that it shoul consume all available horizontal space on the
	 * page.
	 */
	public static final int GRAB = 4;

	/**
	 * Style flag that is mainly used by tables and forces all PBoxes in a line
	 * to have the same height. All PBoxes in the line should be constructed
	 * with this flag
	 */
	public static final int ROW_ALIGN = 8;

	// static parameters (set by setParameters())
	public static GC gc = null; // GC used thoughout the drawing system

	protected static Point pixelPerCm = new Point(0, 0);

	protected static Point pixelPerInch = new Point(0, 0);

	public static Device device = null; // the Device on which the GC works.

	protected static int scalingPercent = 100; // The scaling factor (used for

	// previews in different sizes)

	// member variables
	// Misc:
	protected PContainer parent;

	protected PStyle boxStyle;

	protected boolean below;

	protected boolean rowAlign;

	protected int forcedHeight; // this variable is set to a value != 0 by

	// the layout method and determines a fixed pixel height

	// Positioning:
	protected PagePoint origin;

	protected int sizeCalculatedfor;

	protected double hWeight; // -1 = fixed width, see minCm.

	protected double minCm; // -1 = occupy existing space

	protected boolean grabbing;

	protected int grabWidth; // set by the layout function

	// which is able to calculate the width
	// of grabbing poxes.

	// Constructors
	/**
	 * Constructs a Box with default size below the previous one.
	 */
	public PBox(PContainer parent) {
		this.parent = parent;
//		if (parent != null)
//			parent.addChild(this);
		boxStyle = PStyle.getDefaultStyle();

		below = true;
		rowAlign = false;
		origin = new PagePoint();
		sizeCalculatedfor = 0;
		grabbing = false;
		grabWidth = 0;

		hWeight = -1;
		minCm = 0.0;
		forcedHeight = 0;
	}

	/**
	 * Constructs a Box with default size.
	 * 
	 * @param parent
	 * @param style
	 *            Poition: POS_BELOW or POS_RIGHT.
	 */
	public PBox(PContainer parent, int style) {
		this(parent);
		below = true;
		if ((style & POS_RIGHT) > 0)
			below = false;
		if ((style & POS_BELOW) > 0)
			below = true;
		if ((style & GRAB) > 0)
			grabbing = true;
		if ((style & ROW_ALIGN) > 0)
			rowAlign = true;
	}

	/**
	 * Constructs a Box with default size.
	 * 
	 * @param parent
	 * @param style
	 *            Poition: POS_BELOW or POS_RIGHT. Also GRAB and/or ROW_ALIGN.
	 * @param hWeight
	 *            Determines, how much of the existing page width should be
	 *            occupied. 1.0 = full page. -1 = fiexed width, see minCm.
	 * @param minCm
	 *            Minimum width in cm. If > 0.0, the box will at least have this
	 *            width.
	 */
	public PBox(PContainer parent, int style, double hWeight, double minCm) {
		this(parent, style);
		this.hWeight = hWeight;
		this.minCm = minCm;
	}

	public void dispose() {
		parent.children.remove(this);
	}

	/*
	 * Sets the forced height. This means that the PBox will have this height
	 * (in pixel) regardless of how high it wants to be.
	 */
	protected void setForcedHeight(int height) {
		forcedHeight = height;
	}

	public void draw(int page, Point originOffset) {
		Point originForDrawing = new Point(this.origin.x + originOffset.x,
				this.origin.y + originOffset.y);

		if (layoutIsOnPage(page)) {
			int width = getWidth();
			int height = getHeight(page);

			gc.setBackground(boxStyle.getBackColor());
			gc.fillRectangle(originForDrawing.x, originForDrawing.y, width,
					height);

			gc.setBackground(boxStyle.getLineColor());

			if (boxStyle.hasLine(0)) {
				gc.fillRectangle(originForDrawing.x, originForDrawing.y, width,
						boxStyle.getLineWidth(0));
			}
			if (boxStyle.hasLine(1)) {
				gc.fillRectangle(originForDrawing.x + width
						- boxStyle.getLineWidth(1), originForDrawing.y,
						boxStyle.getLineWidth(1), height);
			}
			if (boxStyle.hasLine(2)) {
				gc.fillRectangle(originForDrawing.x, originForDrawing.y
						+ height - boxStyle.getLineWidth(2), width, boxStyle
						.getLineWidth(2));
			}
			if (boxStyle.hasLine(3)) {
				gc.fillRectangle(originForDrawing.x, originForDrawing.y,
						boxStyle.getLineWidth(3), height);
			}

			gc.setBackground(boxStyle.getBackColor());
		}
	}

	/**
	 * Returns the elements PDocument.
	 * 
	 * @return PDocument
	 */
	public PDocument getDocument() {
		return parent.doc;
	}

	// /////////////////////////////////////////////////////////////////
	// LAYOUT API
	// /////////////////////////////////////////////////////////////////

	/*
	 * Some elements can occupy more than one Page. Therefore this function
	 * tests if the element has a part on the given page. @param page @return
	 * boolean
	 */
	protected boolean layoutIsOnPage(int page) {
		return (page == origin.page);
	}

	/*
	 * Returns the space in y-direction the Element would occupy of the rest of
	 * the page if told so. Convention: this method can be called several times
	 * for one page, but only until layoutOccupy is called once for this page.
	 * @param spaceLeft @return int -1, if the element deciedes not to have any
	 * part on the given page
	 */
	protected int layoutHowMuchWouldYouOccupyOf(Point spaceLeft, int page) {
		if (layoutAlreadyFinished())
			return 0;
		if (getHeight() > spaceLeft.y)
			return -1;
		return getHeight();
	}

	protected boolean layoutAlreadyFinished() {
		return origin.page > 0;
	}

	/*
	 * Returns true if the box would fit or at least finish into/within the
	 * given space in y-direction. Convention: this method can be called several
	 * times for one page, but only until layoutOccupy is called once for this
	 * page. @param spaceLeft
	 */
	protected boolean layoutWouldYouFinishWithin(Point spaceLeft, int page) {
		if (getHeight() > spaceLeft.y)
			return false;
		return true;
	}

	/*
	 * Tells the element to occupy the given space on the page. Returns the
	 * space in y-direction the Element occupys of the rest of the page.
	 * Convention: this method is only called once for one page, and after this
	 * call there will be no further layoutHowMuchWouldYouOccpy-calls for this
	 * page. @param spaceLeft @return int
	 */
	protected int layoutOccupy(Point origin, Point spaceLeft, int page) {
		if (!layoutAlreadyFinished()) {
			this.origin.page = page;
			this.origin.x = origin.x;
			this.origin.y = origin.y;
		}
		return getHeight();
	}

	/*
	 * use this method to make all tuning variables unvalid and so force a
	 * recalculation of these variables.
	 */
	protected void layoutResetTuning() {
		sizeCalculatedfor = 0;
		origin.page = 0;
		forcedHeight = 0;
	}

	/*
	 * Gives the horizontal size of the element. (has only to work AFTER the
	 * layout process, is used by draw().)
	 */
	protected int getWidth() {
		if (grabbing)
			return grabWidth;
		if (hWeight < 0)
			return pixelX(minCm);
		return Math.max(pixelX(minCm), pixelX(parent.getPossibleWidth()
				* hWeight));
	}

	/*
	 * Gives the vertical size of the element. Used by all layout* functions. If
	 * multipage functionallity is needed, this mechanism does no longer work.
	 * Use/overwrite getHeight(int page) instead.
	 */
	protected int getHeight() {
		if (forcedHeight > 0)
			return forcedHeight;
		return 0;
	}

	protected int getHeight(int page) {
		if (origin.page == page) {
			if (rowAlign)
				return forcedHeight;
			return getHeight();
		}
		return 0;
	}

	// /////////////////////////////////////////////////////////////////
	// STATIC API
	// /////////////////////////////////////////////////////////////////

	protected static int pixelX(double cm) {
		long tmp = Math.round(cm * pixelPerCm.x * scalingPercent / 100);
		return (int) tmp;
	}

	protected static int pixelY(double cm) {
		long tmp = Math.round(cm * pixelPerCm.y * scalingPercent / 100);
		return (int) tmp;
	}

	/**
	 * Sets the main parameters for a document to print.
	 * 
	 * @param theGC
	 * @param theDevice
	 * @param dpi
	 */
	public static void setParameters(GC theGC, Device theDevice, Point dpi,
			int percent) {
		gc = theGC;
		device = theDevice;
		scalingPercent = percent;
		pixelPerInch = dpi;
		pixelPerCm = new Point((int) Math.round(dpi.x / 2.54), (int) Math
				.round(dpi.y / 2.54));
	}

	/**
	 * @return PStyle
	 */
	public PStyle getBoxStyle() {
		return boxStyle;
	}

	/**
	 * Sets the boxStyle.
	 * 
	 * @param boxStyle
	 *            The boxStyle to set
	 */
	public void setBoxStyle(PStyle boxStyle) {
		this.boxStyle = boxStyle;
	}

}

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
 * Used inside the KPrint implementation
 * 
 * @author Friederich Kupzog
 */
class PagePoint {

	/**
	 * 
	 */
	public int page;

	public int x, y;

	public PagePoint() {
		x = 0;
		y = 0;
		page = 0;
	}

	public PagePoint(Point p, int page) {
		x = p.x;
		y = p.y;
		this.page = page;
	}

}

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
 * A Message Box class used to display messages.
 * 
 * @author Friederich Kupzog
 */
class MsgBox {

	/**
	 * 
	 */
	private Display d;

	private Shell s;

	private Label bild, meldung;

	private Control additionalControl;

	private boolean ende;

	/**
	 * Der Text des Buttons, der vom Benutzer betatigt wurde
	 */
	public String pressedButton;

	public MsgBox(Display d, String title, String message, String buttons) {
		this.d = d;
		this.s = new Shell(d, SWT.TITLE | SWT.APPLICATION_MODAL);
		this.s.setText(title);
		additionalControl = null;
		ende = false;

		FormLayout fl = new FormLayout();
		this.s.setLayout(fl);

		bild = new Label(this.s, SWT.LEFT);
//		bild.setImage(IconSource.getImage("MsgBox"));
		bild.setImage(IconSource.MSGBOX);
		bild.setBackground(d.getSystemColor(SWT.COLOR_WHITE));

		FormData f = new FormData();
		f.top = new FormAttachment(0, 0);
		f.left = new FormAttachment(0, 0);
		f.bottom = new FormAttachment(100, 0);
		bild.setLayoutData(f);

		Label separator = new Label(this.s, SWT.SEPARATOR);

		f = new FormData();
		f.top = new FormAttachment(0, 0);
		f.left = new FormAttachment(bild, 0);
		f.bottom = new FormAttachment(100, 0);
		separator.setLayoutData(f);

		meldung = new Label(s, SWT.LEFT | SWT.WRAP);
		meldung.setText(message);

		f = new FormData();
		f.top = new FormAttachment(0, 25);
		f.left = new FormAttachment(bild, 25);
		f.right = new FormAttachment(100, -25);
		f.bottom = new FormAttachment(100, -55);
		meldung.setLayoutData(f);

		ButtonBar butBar = new ButtonBar(s, 80);
		StringTokenizer t = new StringTokenizer(buttons, ","); //$NON-NLS-1$
		boolean first = true;
		while (t.hasMoreTokens()) {
			Button but = butBar.addButton(t.nextToken(), "", //$NON-NLS-1$
					new SelectionAdapter() {
						public void widgetSelected(SelectionEvent e) {
							pressedButton = ((Button) e.getSource()).getText();
							ende = true;
						}
					});
			if (first) {
				first = false;
				s.setDefaultButton(but);
			}
		}
		f = new FormData();
		f.bottom = new FormAttachment(100, -4);
		f.left = new FormAttachment(bild, 15);
		f.right = new FormAttachment(100, -15);
		butBar.setLayoutData(f);

	}

	/**
	 * Erlaubt das Hinzufugen weiterer Steuerelemente zur MsgBox. Diese werden
	 * unter dem Text und uber der Buttonleiste engezeigt.
	 * 
	 * Benutzung: MsgBox box = new MsgBox(display,"Box","Beispiel","OK"); Text
	 * feld = new Text(box.getShell(),SWT.BORDER); box.addControl(feld);
	 * box.open(); (hier Zugriff auf feld) box.dispose();
	 * 
	 * @param c
	 *            das anzuzeigende Control.
	 */
	public void addControl(Control c) {
		// Meldung neu abstutzen
		FormData f = new FormData();
		f.top = new FormAttachment(0, 25);
		f.left = new FormAttachment(bild, 25);
		f.right = new FormAttachment(100, -25);
		// f.bottom = new FormAttachment(100,-55);
		meldung.setLayoutData(f);

		// Neues Control layouten
		f = new FormData();
		f.top = new FormAttachment(meldung, 5);
		f.left = new FormAttachment(bild, 25);
		f.right = new FormAttachment(100, -25);
		f.bottom = new FormAttachment(100, -55);
		c.setLayoutData(f);
		additionalControl = c;

	}

	public void setImage(Image newImg) {
		bild.setImage(newImg);
	}

	/**
	 * Gibt die Shell der MsgBox zuruck.
	 * 
	 * @return Shell
	 */
	public Shell getShell() {
		return s;
	}

	/**
	 * Zeigt die MsgBox an.
	 */
	public void open() {
		s.pack();
		s.setLocation((d.getBounds().width - s.getBounds().width) / 2, (d
				.getBounds().height - s.getBounds().height) / 2);

		s.open();

		if (additionalControl != null)
			additionalControl.setFocus();

		while (!ende) {
			if (!d.readAndDispatch())
				d.sleep();
		}
	}

	/**
	 * Muss nach box.open() aufgerufen werden!
	 */
	public void dispose() {
		s.close();
		s.dispose();
	}

	/**
	 * Baut eine fertige MsgBox auf und zeigt diese an.
	 * 
	 * @param d
	 * @param title
	 * @param message
	 * @param buttons
	 * @return String
	 */

	public static String show(Display d, String title, String message,
			String buttons) {
		MsgBox box = new MsgBox(d, title, message, buttons);
		box.open();
		box.dispose();
		return box.pressedButton;

	}

	/**
	 * Baut eine fertige MsgBox auf und zeigt diese an
	 * 
	 * @param d
	 * @param message
	 * @return String
	 */
	public static String show(Display d, String message) {
		return show(d, "Meldung", message, "OK"); //$NON-NLS-1$ //$NON-NLS-2$
	}

	public static String show(String title, String message, String buttons) {
		MsgBox box = new MsgBox(Display.getCurrent(), title, message, buttons);
		box.open();
		box.dispose();
		return box.pressedButton;

	}

	public static String show(String message) {
		return show("Meldung", message, "OK"); //$NON-NLS-1$ //$NON-NLS-2$
	}

}

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
 * This class is intended for subclassing.
 * 
 * It offers functionality for displaying a picture in the dialog's header and
 * for adding buttons (right- and left-adjusted) in the dialogs footer.
 * Additionally, it offers an easy tool bar creating mechanism.
 * 
 * The events generated by buttons and toolitems can be handled in the method
 * onButton and onToolItem.
 * 
 * If not changed by the customer, the main Layout guiMainLayout has a
 * gridLayout(). Overwrite createMainAreaLayout() to change that.
 * 
 * @author Friederich Kupzog
 */
class KDialog {

	/**
	 * 
	 */
	protected Shell guiShell;

	protected Display guiDisplay;

	protected Composite guiPictureArea;

	protected Composite guiToolBarArea;

	protected Composite guiMainArea;

	protected Composite guiButtonArea;

	protected Button guiLastLeftBut, guiLastRightBut;

	protected Control guiLastToolControl;

	protected Layout guiMainAreaLayout;

	protected Label guiPictureLabel;

	protected ToolBar guiToolBar;

	protected GridData guiPictureGridData;

	protected GridData guiToolBarGridData;

	/**
	 * Cretaes a new, top level dialog.
	 */
	public KDialog() {
		createShell(null);
	}

	/**
	 * Creates a new Dialog.
	 * 
	 * @param parent
	 *            The parent shell for this dialog.
	 */
	public KDialog(Shell parent) {
		createShell(parent);
	}

	/**
	 * Creates a new Dialog.
	 * 
	 * @param parent
	 *            The parent shell for this dialog.
	 * @param title
	 *            The Dialog's title
	 */
	public KDialog(Shell parent, String title) {
		this(parent);
		setTitle(title);
	}

	/**
	 * Creates a new Dialog.
	 * 
	 * @param parent
	 *            The parent shell for this dialog.
	 * @param title
	 *            The Dialog's title
	 * @param icon
	 *            The dialog's window icon.
	 */
	public KDialog(Shell parent, String title, Image icon) {
		this(parent);
		setTitle(title);
		setShellImage(icon);
	}

	/*
	 * Baut das Shell-Objekt auf und die Composits der 1. Ebene
	 */
	protected void createShell(Shell parent) {
		guiDisplay = Display.getCurrent();

		// Shell
		if (parent != null)
			guiShell = new Shell(parent, getShellStyle());
		else
			guiShell = new Shell(Display.getCurrent(), getShellStyle());

		createShellLayout();
		createShellComposits();
	}

	protected void createShellComposits() {

		// picture area
		guiPictureArea = new Composite(guiShell, SWT.NONE);
		guiPictureGridData = new GridData();
		guiPictureGridData.grabExcessHorizontalSpace = true;
		guiPictureGridData.horizontalAlignment = GridData.FILL;
		guiPictureGridData.heightHint = 0;
		guiPictureArea.setLayoutData(guiPictureGridData);

		// ToolBar area
		guiToolBarArea = new Composite(guiShell, SWT.NONE);
		guiToolBarGridData = new GridData();
		guiToolBarGridData.grabExcessHorizontalSpace = true;
		guiToolBarGridData.horizontalAlignment = GridData.FILL;
		guiToolBarGridData.heightHint = 0;
		guiToolBarArea.setLayoutData(guiToolBarGridData);

		// main area
		guiMainArea = new Composite(guiShell, SWT.NONE);
		createMainAreaLayout();
		GridData gd = new GridData();
		gd.grabExcessHorizontalSpace = true;
		gd.horizontalAlignment = GridData.FILL;
		gd.grabExcessVerticalSpace = true;
		gd.verticalAlignment = GridData.FILL;
		guiMainArea.setLayoutData(gd);

		// button area
		createButtonBar();
	}

	protected void createButtonBar() {
		// AuBeres Composite
		guiButtonArea = new Composite(guiShell, SWT.NONE);
		GridData gd = new GridData();
		gd.grabExcessHorizontalSpace = true;
		gd.horizontalAlignment = GridData.FILL;
		guiButtonArea.setLayoutData(gd);

		FormLayout butLayout = new FormLayout();
		guiButtonArea.setLayout(butLayout);

		// Trennlinie
		Label sep = new Label(guiButtonArea, SWT.SEPARATOR | SWT.HORIZONTAL);
		FormData fd = new FormData();
		fd.bottom = new FormAttachment(100, -32);
		fd.left = new FormAttachment(0, 0);
		fd.right = new FormAttachment(100, 0);
		sep.setLayoutData(fd);

	}

	protected void createMainAreaLayout() {
		guiMainAreaLayout = new GridLayout();
		((GridLayout) guiMainAreaLayout).makeColumnsEqualWidth = false;
		((GridLayout) guiMainAreaLayout).numColumns = 1;
		guiMainArea.setLayout(guiMainAreaLayout);
	}

	/**
	 * Factorymethod for pre-configured GridData objects
	 * 
	 * Configurates: grabExcessHorizontalSpace = true horizontalAlignment =
	 * GridData.BEGINNING
	 * 
	 * @return GridData
	 */
	public static GridData createGridData() {
		GridData gd = new GridData();
		gd.grabExcessHorizontalSpace = true;
		gd.horizontalAlignment = GridData.BEGINNING;
		return gd;
	}

	/**
	 * Factorymethod for pre-configured GridData objects
	 * 
	 * Configurates: grabExcessHorizontalSpace = true horizontalAlignment =
	 * GridData.BEGINNING heightHint = hint
	 * 
	 * @return GridData
	 */

	public static GridData createGridDataHHint(int hint) {
		GridData gd = new GridData();
		gd.grabExcessHorizontalSpace = true;
		gd.heightHint = hint;
		gd.horizontalAlignment = GridData.BEGINNING;
		return gd;
	}

	/**
	 * Factorymethod for pre-configured GridData objects
	 * 
	 * Configurates: grabExcessHorizontalSpace = true horizontalAlignment =
	 * GridData.FILL horizontalSpan = columns verticalAlignment = GridData.FILL
	 * 
	 * @return GridData
	 */
	public static GridData createGridDataFill(int columns) {
		GridData gd = new GridData();
		gd.grabExcessHorizontalSpace = true;
		gd.horizontalAlignment = GridData.FILL;
		gd.verticalAlignment = GridData.FILL;
		gd.horizontalSpan = columns;
		return gd;
	}

	/**
	 * Factorymethod for pre-configured GridData objects
	 * 
	 * Configurates: grabExcessHorizontalSpace = true horizontalAlignment =
	 * GridData.FILL horizontalSpan = columns verticalAlignment = GridData.FILL
	 * horizontalIndent = hIndent
	 * 
	 * @return GridData
	 */
	public static GridData createGridDataFill(int columns, int hIndent) {
		GridData gd = new GridData();
		gd.grabExcessHorizontalSpace = true;
		gd.horizontalAlignment = GridData.FILL;
		gd.verticalAlignment = GridData.FILL;
		gd.horizontalSpan = columns;
		gd.horizontalIndent = hIndent;
		return gd;
	}

	protected void createShellLayout() {
		GridLayout layout = new GridLayout(1, true);
		guiShell.setLayout(layout);
		layout.horizontalSpacing = 0;
		layout.verticalSpacing = 0;
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		guiShell.setLayout(layout);
	}

	/**
	 * Returns the style of the dialog. Is also used during shell creation.
	 * Overwrite this method to give your dialog another style.
	 * 
	 * @return int
	 */
	public int getShellStyle() {
		return SWT.CLOSE | SWT.RESIZE;
	}

	/**
	 * This method should create the dialogs GUI and is NOT called by the
	 * KDialog constructor. Overwrite this method to build add your own widgets
	 * to the dialog. Do not forget to call it in your own constructor.
	 */
	protected void createContents() {
		/*
		 * new Text(guiMainArea,SWT.BORDER); addButton("Ha!",""); addButton("Du
		 * langer Button",""); addButtonRight("Close","");
		 * addButtonRight("Komfort","",true);
		 */
	}

	/**
	 * Overwrite this method, if you whish your dioalog having a specific size
	 * (use guiShell.setSize())
	 */
	protected void doLayout() {
		guiShell.pack();
	}

	/**
	 * This method opens the dialogs and processes events until the shell is
	 * closed. You do not need to overwrite this method. Use close() to close
	 * the dialog programmatically.
	 */
	public void open() {
		doLayout();
		doPositioning();
		guiShell.open();
		while (!guiShell.isDisposed()) {
			if (!guiDisplay.readAndDispatch())
				guiDisplay.sleep();
		}
		guiShell.dispose();
	}

	public void close() {
		guiShell.dispose();
	}

	/**
	 * This method centers the dialog on the screen. Overwrite this method if
	 * you whish another position.
	 */
	protected void doPositioning() {
		guiShell.setLocation(
						(guiDisplay.getBounds().width - guiShell.getBounds().width) / 2,
						(guiDisplay.getBounds().height - guiShell.getBounds().width) / 2);
	}

	/**
	 * Sets the icon of the shell.
	 * 
	 * @param image
	 */
	public void setShellImage(Image image) {
		guiShell.setImage(image);
	}

	/**
	 * Sets the window title.
	 * 
	 * @param title
	 */
	public void setTitle(String title) {
		guiShell.setText(title);
	}

	/**
	 * Sets the image displayed in the dialogs header.
	 * 
	 * @param image
	 */
	public void setDialogImage(Image image) {
		guiPictureArea
				.setBackground(guiDisplay.getSystemColor(SWT.COLOR_WHITE));

		guiPictureGridData.heightHint = image.getBounds().height + 2;

		GridLayout layout = new GridLayout(1, true);
		guiPictureArea.setLayout(layout);
		layout.horizontalSpacing = 0;
		layout.verticalSpacing = 0;
		layout.marginHeight = 0;
		layout.marginWidth = 0;

		guiPictureLabel = new Label(guiPictureArea, SWT.NONE);
		guiPictureLabel.setImage(image);
		// guiPictureLabel.setBackground(guiDisplay.getSystemColor(SWT.COLOR_WHITE));
		GridData gd = new GridData();
		// gd.grabExcessHorizontalSpace = true;
		// gd.horizontalAlignment = GridData.FILL;
		guiPictureLabel.setLayoutData(gd);

		Label line = new Label(guiPictureArea, SWT.SEPARATOR | SWT.HORIZONTAL);
		gd = new GridData();
		gd.grabExcessHorizontalSpace = true;
		gd.horizontalAlignment = GridData.FILL;
		line.setLayoutData(gd);
	}

	/**
	 * Adds a ToolItem to the dialog's toolbar. Creates the toolbar if not
	 * already done.
	 * 
	 * @param name
	 *            The name if the ToolItem. Although this name is never
	 *            displayed to the user you can use it in onToolItem to identify
	 *            the activated ToolItem.
	 * @param tooltip
	 *            The item's tooltip
	 * @param icon
	 *            The icon to show.
	 * @return ToolItem The ToolItem created by this method.
	 */
	public ToolItem addToolItem(String name, String tooltip, Image icon) {
		if (guiToolBar == null) {
			FormLayout layout = new FormLayout();
			guiToolBarArea.setLayout(layout);
			// layout.horizontalSpacing = 0;
			// layout.verticalSpacing = 0;
			layout.marginHeight = 0;
			layout.marginWidth = 0;

			guiToolBar = new ToolBar(guiToolBarArea, SWT.FLAT);
			FormData fd = new FormData();
			fd.left = new FormAttachment(0, 0);
			fd.top = new FormAttachment(0, 0);
			guiToolBar.setLayoutData(fd);

			Label line = new Label(guiToolBarArea, SWT.SEPARATOR
					| SWT.HORIZONTAL);
			fd = new FormData();
			fd.left = new FormAttachment(0, 0);
			fd.top = new FormAttachment(guiToolBar, 1);
			fd.right = new FormAttachment(100, 0);
			line.setLayoutData(fd);

			guiLastToolControl = guiToolBar;

		}
		ToolItem ti = new ToolItem(guiToolBar, SWT.PUSH);
		ti.setImage(icon);
		ti.setToolTipText(tooltip);
		ti.setEnabled(true);
		ti.setData(name);
		ti.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				onToolItem((ToolItem) e.widget, (String) e.widget.getData());
			}
		});

		guiToolBarGridData.heightHint = guiToolBarArea.computeSize(SWT.DEFAULT,
				SWT.DEFAULT).y;
		return ti;
	}

	public void adjustToToolBar(Control c) {
		FormData fd = new FormData();
		fd.left = new FormAttachment(guiLastToolControl, 2);
		fd.top = new FormAttachment(0, 1);
		fd.bottom = new FormAttachment(0, 22);

		c.setLayoutData(fd);
		guiLastToolControl = c;
	}

	protected Button addButton(boolean rightAdjusted, String text, String tip) {
		Button erg = new Button(guiButtonArea, SWT.PUSH);
		erg.setText(text);
		erg.setToolTipText(tip);

		Point butPrefferedSize = erg.computeSize(SWT.DEFAULT, SWT.DEFAULT);

		FormData fd = new FormData();
		fd.bottom = new FormAttachment(100, -3);

		if (butPrefferedSize.x > 70)
			fd.width = butPrefferedSize.x + 4;
		else
			fd.width = 70;
		fd.height = 24;
		erg.setLayoutData(fd);

		if (rightAdjusted) {
			if (guiLastRightBut == null)
				fd.right = new FormAttachment(100, -3);
			else
				fd.right = new FormAttachment(guiLastRightBut, -3);
			guiLastRightBut = erg;
		} else {
			if (guiLastLeftBut == null)
				fd.left = new FormAttachment(0, 3);
			else
				fd.left = new FormAttachment(guiLastLeftBut, 3);
			guiLastLeftBut = erg;
		}

		erg.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent arg0) {
				onButton((Button) arg0.widget, ((Button) arg0.widget).getText());
			}
		});
		return erg;
	}

	/**
	 * Puts a Button into the button bar in the dialog's footer (right
	 * adjusted). To handle the event produced by this button see onButton().
	 * 
	 * @param text
	 * @param toolTip
	 * @return Button The Button produced by this method.
	 */
	public Button addButtonRight(String text, String toolTip) {
		return addButton(true, text, toolTip);
	}

	/**
	 * Puts a Button into the button bar in the dialog's footer (left adjusted).
	 * To handle the event produced by this button see onButton().
	 * 
	 * @param text
	 * @param toolTip
	 * @return Button The Button produced by this method.
	 */
	public Button addButton(String text, String toolTip) {
		return addButton(false, text, toolTip);
	}

	/**
	 * Puts a Button into the button bar in the dialog's footer (right
	 * adjusted). To handle the event produced by this button see onButton().
	 * 
	 * @param text
	 * @param toolTip
	 * @param isDefault
	 *            if true, this button will become the default button
	 * @return Button The Button produced by this method.
	 */
	public Button addButtonRight(String text, String toolTip, boolean isDefault) {
		Button erg = addButton(true, text, toolTip);
		if (isDefault)
			guiShell.setDefaultButton(erg);
		return erg;
	}

	/**
	 * Puts a Button into the button bar in the dialog's footer (left adjusted).
	 * To handle the event produced by this button see onButton().
	 * 
	 * @param text
	 * @param toolTip
	 * @param isDefault
	 *            if true, this button will become the default button
	 * @return Button The Button produced by this method.
	 */
	public Button addButton(String text, String toolTip, boolean isDefault) {
		Button erg = addButton(false, text, toolTip);
		if (isDefault)
			guiShell.setDefaultButton(erg);
		return erg;
	}

	/*
	 * Button-clicks call this method. Overwrite it to react on button clicks.
	 */
	protected void onButton(Button button, String buttonText) {
	}

	/*
	 * clicked ToolItems call this method. Overwrite it to react on button
	 * clicks.
	 */
	protected void onToolItem(ToolItem toolitem, String name) {
	}

}

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
 * This Class is intended for a cebtral access to all icons used throughout the
 * package.
 * 
 * Alter the getImage() function, if you use other techniques of image access.
 * 
 */

class IconSource {

	public static final Image PRINT = ImgDescManager.getImageDesc(ImageConstants.PRINT).createImage();
	public static final Image FIRST = ImgDescManager.getImageDesc(ImageConstants.FIRST).createImage();
	public static final Image PREVIOUS = ImgDescManager.getImageDesc(ImageConstants.PREVIOUS).createImage();
	public static final Image NEXT = ImgDescManager.getImageDesc(ImageConstants.NEXT).createImage();
	public static final Image LAST = ImgDescManager.getImageDesc(ImageConstants.LAST).createImage();
	public static final Image MSGBOX = ImgDescManager.getImageDesc(ImageConstants.WARN).createImage();

	/** Returns the requested Image */
	public static Image getImage(String name) {
		return loadImageResource(Display.getCurrent(), "/gfx/" + name + ".gif"); //$NON-NLS-1$ //$NON-NLS-2$
	}

	/**
	 * reads an Image as ressource (either from file system or from a jar)
	 */
	public static Image loadImageResource(Display d, String name) {
		try {

			Image ret = null;
			Class<?> clazz = new Object().getClass();
			InputStream is = clazz.getResourceAsStream(name);
			if (is != null) {
				ret = new Image(d, is);
				is.close();
			}
			if (ret == null)
				System.out.println("Error loading bitmap:\n" + name); //$NON-NLS-1$
			return ret;
		} catch (Exception e1) {
			System.out.println("Error loading bitmap:\n" + name); //$NON-NLS-1$
			return null;
		}
	}

}

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
 * Used by MsgBox.
 * 
 * @author Friederich Kupzog
 */

class ButtonBar extends Composite {

	private RowLayout myLayout;

	private ArrayList<Button> myButtons;

	private int myButtonWidth;

	/** Erzeugt neuen ButtonBar */
	public ButtonBar(Composite owner, int buttonWidth) {
		super(owner, SWT.NONE);
		myButtonWidth = buttonWidth;
		myLayout = new RowLayout();
		myLayout.justify = true;
		myLayout.type = SWT.HORIZONTAL;
		myLayout.wrap = true;
		myLayout.spacing = 4;
		this.setLayout(myLayout);
		myButtons = new ArrayList<Button>();
	}

	/**
	 * Fugt einen Button zur Leiste hinzu. Gibt eine Referenz auf den angelegten
	 * Button zuruck.
	 */
	public Button addButton(String name, String toolTip,
			SelectionListener selListener) {
		Button b = new Button(this, SWT.PUSH);
		b.setText(name);
		b.setToolTipText(toolTip);
		b.setLayoutData(new RowData(myButtonWidth, 25));
		if (selListener != null)
			b.addSelectionListener(selListener);
		myButtons.add(b);
		return b;
	}

	/**
	 * Fugt einen Button zur Leiste hinzu, und registriert ihn bei der in
	 * myShell ubergebenen Shell als DefaultButton.
	 */
	public Button addButton(String name, String toolTip, Shell myShell,
			SelectionListener selListener) {
		Button b = addButton(name, toolTip, selListener);
		myShell.setDefaultButton(b);
		return b;
	}

}

/**
 * @author Kosta, Friederich Kupzog
 */
class SWTX {
	public static final int EVENT_SWTX_BASE = 1000;

	public static final int EVENT_TABLE_HEADER = EVENT_SWTX_BASE + 1;

	public static final int EVENT_TABLE_HEADER_CLICK = EVENT_SWTX_BASE + 2;

	public static final int EVENT_TABLE_HEADER_RESIZE = EVENT_SWTX_BASE + 3;

	//
	public static final int ALIGN_HORIZONTAL_MASK = 0x0F;

	public static final int ALIGN_HORIZONTAL_NONE = 0x00;

	public static final int ALIGN_HORIZONTAL_LEFT = 0x01;

	public static final int ALIGN_HORIZONTAL_LEFT_LEFT = ALIGN_HORIZONTAL_LEFT;

	public static final int ALIGN_HORIZONTAL_LEFT_RIGHT = 0x02;

	public static final int ALIGN_HORIZONTAL_LEFT_CENTER = 0x03;

	public static final int ALIGN_HORIZONTAL_RIGHT = 0x04;

	public static final int ALIGN_HORIZONTAL_RIGHT_RIGHT = ALIGN_HORIZONTAL_RIGHT;

	public static final int ALIGN_HORIZONTAL_RIGHT_LEFT = 0x05;

	public static final int ALIGN_HORIZONTAL_RIGHT_CENTER = 0x06;

	public static final int ALIGN_HORIZONTAL_CENTER = 0x07;

	public static final int ALIGN_VERTICAL_MASK = 0xF0;

	public static final int ALIGN_VERTICAL_TOP = 0x10;

	public static final int ALIGN_VERTICAL_BOTTOM = 0x20;

	public static final int ALIGN_VERTICAL_CENTER = 0x30;

	//
	private static GC m_LastGCFromExtend;

	private static Map<String, Point> m_StringExtentCache = new HashMap<String, Point>();

	private static synchronized Point getCachedStringExtent(GC gc, String text) {
		if (m_LastGCFromExtend != gc) {
			m_StringExtentCache.clear();
			m_LastGCFromExtend = gc;
		}
		Point p = (Point) m_StringExtentCache.get(text);
		if (p == null) {
			if (text == null)
				return new Point(0, 0);
			p = gc.stringExtent(text);
			m_StringExtentCache.put(text, p);
		}
		return new Point(p.x, p.y);
	}

	public static int drawTextVerticalAlign(GC gc, String text, int textAlign,
			int x, int y, int w, int h) {
		if (text == null)
			text = ""; //$NON-NLS-1$
		Point textSize = getCachedStringExtent(gc, text);
		{
			boolean addPoint = false;
			while ((text.length() > 0) && (textSize.x >= w)) {
				text = text.substring(0, text.length() - 1);
				textSize = getCachedStringExtent(gc, text + "..."); //$NON-NLS-1$
				addPoint = true;
			}
			if (addPoint)
				text = text + "..."; //$NON-NLS-1$
			textSize = getCachedStringExtent(gc, text);
			if (textSize.x >= w) {
				text = ""; //$NON-NLS-1$
				textSize = getCachedStringExtent(gc, text);
			}
		}
		//
		if ((textAlign & ALIGN_VERTICAL_MASK) == ALIGN_VERTICAL_TOP) {
			gc.drawText(text, x, y);
			gc.fillRectangle(x, y + textSize.y, textSize.x, h - textSize.y);
			return textSize.x;
		}
		if ((textAlign & ALIGN_VERTICAL_MASK) == ALIGN_VERTICAL_BOTTOM) {
			gc.drawText(text, x, y + h - textSize.y);
			gc.fillRectangle(x, y, textSize.x, h - textSize.y);
			return textSize.x;
		}
		if ((textAlign & ALIGN_VERTICAL_MASK) == ALIGN_VERTICAL_CENTER) {
			int yOffset = (h - textSize.y) / 2;
			gc.drawText(text, x, y + yOffset);
			gc.fillRectangle(x, y, textSize.x, yOffset);
			gc.fillRectangle(x, y + yOffset + textSize.y, textSize.x, h
					- (yOffset + textSize.y));
			return textSize.x;
		}
		throw new SWTException("H: " + (textAlign & ALIGN_VERTICAL_MASK)); //$NON-NLS-1$
	}

	public static void drawTransparentImage(GC gc, Image image, int x, int y) {
		if (image == null)
			return;
		Point imageSize = new Point(image.getBounds().width,
				image.getBounds().height);
		Image img = new Image(Display.getCurrent(), imageSize.x, imageSize.y);
		GC gc2 = new GC(img);
		gc2.setBackground(gc.getBackground());
		gc2.fillRectangle(0, 0, imageSize.x, imageSize.y);
		gc2.drawImage(image, 0, 0);
		gc.drawImage(img, x, y);
		gc2.dispose();
		img.dispose();
	}

	public static void drawImageVerticalAlign(GC gc, Image image,
			int imageAlign, int x, int y, int h) {
		if (image == null)
			return;
		Point imageSize = new Point(image.getBounds().width,
				image.getBounds().height);
		//
		if ((imageAlign & ALIGN_VERTICAL_MASK) == ALIGN_VERTICAL_TOP) {
			drawTransparentImage(gc, image, x, y);
			gc.fillRectangle(x, y + imageSize.y, imageSize.x, h - imageSize.y);
			return;
		}
		if ((imageAlign & ALIGN_VERTICAL_MASK) == ALIGN_VERTICAL_BOTTOM) {
			drawTransparentImage(gc, image, x, y + h - imageSize.y);
			gc.fillRectangle(x, y, imageSize.x, h - imageSize.y);
			return;
		}
		if ((imageAlign & ALIGN_VERTICAL_MASK) == ALIGN_VERTICAL_CENTER) {
			int yOffset = (h - imageSize.y) / 2;
			drawTransparentImage(gc, image, x, y + yOffset);
			gc.fillRectangle(x, y, imageSize.x, yOffset);
			gc.fillRectangle(x, y + yOffset + imageSize.y, imageSize.x, h
					- (yOffset + imageSize.y));
			return;
		}
		throw new SWTException("H: " + (imageAlign & ALIGN_VERTICAL_MASK)); //$NON-NLS-1$
	}

	public static void drawTextImage(GC gc, String text, int textAlign,
			Image image, int imageAlign, int x, int y, int w, int h) {
		Point textSize = getCachedStringExtent(gc, text);
		Point imageSize;
		if (image != null)
			imageSize = new Point(image.getBounds().width,
					image.getBounds().height);
		else
			imageSize = new Point(0, 0);
		//
		/*
		 * Rectangle oldClipping = gc.getClipping(); gc.setClipping(x, y, w, h);
		 */
		try {
			if ((image == null)
					&& ((textAlign & ALIGN_HORIZONTAL_MASK) == ALIGN_HORIZONTAL_CENTER)) {
				Point p = getCachedStringExtent(gc, text);
				int offset = (w - p.x) / 2;
				if (offset > 0) {
					drawTextVerticalAlign(gc, text, textAlign, x + offset, y, w
							- offset, h);
					gc.fillRectangle(x, y, offset, h);
					gc
							.fillRectangle(x + offset + p.x, y, w
									- (offset + p.x), h);
				} else {
					p.x = drawTextVerticalAlign(gc, text, textAlign, x, y, w, h);
					// gc.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_YELLOW));
					gc.fillRectangle(x + p.x, y, w - (p.x), h);
					// offset = (w - p.x) / 2;
					// gc.fillRectangle(x, y, offset, h);
					// gc.fillRectangle(x + offset + p.x, y, w - (offset + p.x),
					// h);
				}
				return;
			}
			if (((text == null) || (text.length() == 0))
					&& ((imageAlign & ALIGN_HORIZONTAL_MASK) == ALIGN_HORIZONTAL_CENTER)) {
				int offset = (w - imageSize.x) / 2;
				// System.out.println("w: " + w + " imageSize" + imageSize + "
				// offset: " + offset);
				drawImageVerticalAlign(gc, image, imageAlign, x + offset, y, h);
				gc.fillRectangle(x, y, offset, h);
				gc.fillRectangle(x + offset + imageSize.x, y, w
						- (offset + imageSize.x), h);
				return;
			}
			if ((textAlign & ALIGN_HORIZONTAL_MASK) == ALIGN_HORIZONTAL_LEFT) {
				if ((imageAlign & ALIGN_HORIZONTAL_MASK) == ALIGN_HORIZONTAL_NONE) {
					textSize.x = drawTextVerticalAlign(gc, text, textAlign, x,
							y, w, h);
					gc.fillRectangle(x + textSize.x, y, w - textSize.x, h);
					return;
				}
				if ((imageAlign & ALIGN_HORIZONTAL_MASK) == ALIGN_HORIZONTAL_LEFT) {
					textSize.x = drawTextVerticalAlign(gc, text, textAlign, x
							+ imageSize.x, y, w - imageSize.x, h);
					drawImageVerticalAlign(gc, image, imageAlign, x, y, h);
					gc.fillRectangle(x + textSize.x + imageSize.x, y, w
							- (textSize.x + imageSize.x), h);
					return;
				}
				if ((imageAlign & ALIGN_HORIZONTAL_MASK) == ALIGN_HORIZONTAL_RIGHT) {
					textSize.x = drawTextVerticalAlign(gc, text, textAlign, x,
							y, w - imageSize.x, h);
					drawImageVerticalAlign(gc, image, imageAlign, x + w
							- imageSize.x, y, h);
					gc.fillRectangle(x + textSize.x, y, w
							- (textSize.x + imageSize.x), h);
					return;
				}
				if ((imageAlign & ALIGN_HORIZONTAL_MASK) == ALIGN_HORIZONTAL_RIGHT_LEFT) {
					textSize.x = drawTextVerticalAlign(gc, text, textAlign, x,
							y, w - imageSize.x, h);
					drawImageVerticalAlign(gc, image, imageAlign, x
							+ textSize.x, y, h);
					gc.fillRectangle(x + textSize.x + imageSize.x, y, w
							- (textSize.x + imageSize.x), h);
					return;
				}
				if ((imageAlign & ALIGN_HORIZONTAL_MASK) == ALIGN_HORIZONTAL_RIGHT_CENTER) {
					textSize.x = drawTextVerticalAlign(gc, text, textAlign, x,
							y, w - imageSize.x, h);
					int xOffset = (w - textSize.x - imageSize.x) / 2;
					drawImageVerticalAlign(gc, image, imageAlign, x
							+ textSize.x + xOffset, y, h);
					gc.fillRectangle(x + textSize.x, y, xOffset, h);
					gc.fillRectangle(x + textSize.x + xOffset + imageSize.x, y,
							w - (textSize.x + xOffset + imageSize.x), h);
					return;
				}
				throw new SWTException("H: " //$NON-NLS-1$
						+ (imageAlign & ALIGN_HORIZONTAL_MASK));
			} // text align left
			if ((textAlign & ALIGN_HORIZONTAL_MASK) == ALIGN_HORIZONTAL_RIGHT) {
				if ((imageAlign & ALIGN_HORIZONTAL_MASK) == ALIGN_HORIZONTAL_NONE) {
					textSize.x = drawTextVerticalAlign(gc, text, textAlign, x,
							-1000, w, h);
					drawTextVerticalAlign(gc, text, textAlign, x + w
							- textSize.x, y, w, h);
					gc.fillRectangle(x, y, w - textSize.x, h);
					return;
				}
				if ((imageAlign & ALIGN_HORIZONTAL_MASK) == ALIGN_HORIZONTAL_LEFT) {
					textSize.x = drawTextVerticalAlign(gc, text, textAlign, x,
							-1000, w - imageSize.x, h);
					drawTextVerticalAlign(gc, text, textAlign, x + w
							- textSize.x, y, w - imageSize.x, h);
					drawImageVerticalAlign(gc, image, imageAlign, x, y, h);
					gc.fillRectangle(x + imageSize.x, y, w
							- (textSize.x + imageSize.x), h);
					return;
				}
				if ((imageAlign & ALIGN_HORIZONTAL_MASK) == ALIGN_HORIZONTAL_LEFT_RIGHT) {
					textSize.x = drawTextVerticalAlign(gc, text, textAlign, x,
							-1000, w - imageSize.x, h);
					drawTextVerticalAlign(gc, text, textAlign, x + w
							- textSize.x, y, w - imageSize.x, h);
					drawImageVerticalAlign(gc, image, imageAlign, x + w
							- (textSize.x + imageSize.x), y, h);
					gc.fillRectangle(x, y, w - (textSize.x + imageSize.x), h);
					return;
				}
				if ((imageAlign & ALIGN_HORIZONTAL_MASK) == ALIGN_HORIZONTAL_LEFT_CENTER) {
					textSize.x = drawTextVerticalAlign(gc, text, textAlign, x,
							-1000, w - imageSize.x, h);
					drawTextVerticalAlign(gc, text, textAlign, x + w
							- textSize.x, y, w - imageSize.x, h);
					int xOffset = (w - textSize.x - imageSize.x) / 2;
					drawImageVerticalAlign(gc, image, imageAlign, x + xOffset,
							y, h);
					gc.fillRectangle(x, y, xOffset, h);
					gc.fillRectangle(x + xOffset + imageSize.x, y, w
							- (xOffset + imageSize.x + textSize.x), h);
					return;
				}
				if ((imageAlign & ALIGN_HORIZONTAL_MASK) == ALIGN_HORIZONTAL_RIGHT) {
					textSize.x = drawTextVerticalAlign(gc, text, textAlign, x,
							-1000, w - imageSize.x, h);
					drawTextVerticalAlign(gc, text, textAlign, x + w
							- (textSize.x + imageSize.x), y, w - imageSize.x, h);
					drawImageVerticalAlign(gc, image, imageAlign, x + w
							- imageSize.x, y, h);
					gc.fillRectangle(x, y, w - (textSize.x + imageSize.x), h);
					return;
				}
				throw new SWTException("H: " //$NON-NLS-1$
						+ (imageAlign & ALIGN_HORIZONTAL_MASK));
			} // text align right
			throw new SWTException("H: " + (textAlign & ALIGN_HORIZONTAL_MASK)); //$NON-NLS-1$
		} // trye
		finally {
			// gc.setClipping(oldClipping);
		}
	}

	public static void drawTextImage(GC gc, String text, int textAlign,
			Image image, int imageAlign, Rectangle r) {
		drawTextImage(gc, text, textAlign, image, imageAlign, r.x, r.y,
				r.width, r.height);
	}

	public static void drawButtonUp(GC gc, String text, int textAlign,
			Image image, int imageAlign, int x, int y, int w, int h,
			Color face, Color shadowHigh, Color shadowNormal, Color shadowDark,
			int leftMargin, int topMargin) {
		Color prevForeground = gc.getForeground();
		Color prevBackground = gc.getBackground();
		try {
			gc.setBackground(face);
			gc.setForeground(shadowHigh);
			gc.drawLine(x, y, x, y + h - 1);
			gc.drawLine(x, y, x + w - 2, y);
			gc.setForeground(shadowDark);
			gc.drawLine(x + w - 1, y, x + w - 1, y + h - 1);
			gc.drawLine(x, y + h - 1, x + w - 1, y + h - 1);
			gc.setForeground(shadowNormal);
			gc.drawLine(x + w - 2, y + 1, x + w - 2, y + h - 2);
			gc.drawLine(x + 1, y + h - 2, x + w - 2, y + h - 2);
			//
			gc.fillRectangle(x + 1, y + 1, leftMargin, h - 3);
			gc.fillRectangle(x + 1, y + 1, w - 3, topMargin);
			gc.setForeground(prevForeground);
			drawTextImage(gc, text, textAlign, image, imageAlign, x + 1
					+ leftMargin, y + 1 + topMargin, w - 3 - leftMargin, h - 3
					- topMargin);
		} finally {
			gc.setForeground(prevForeground);
			gc.setBackground(prevBackground);
		}
	}

	public static void drawButtonUp(GC gc, String text, int textAlign,
			Image image, int imageAlign, int x, int y, int w, int h, Color face) {
		Display display = Display.getCurrent();
		drawButtonUp(gc, text, textAlign, image, imageAlign, x, y, w, h, face,
				display.getSystemColor(SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW),
				display.getSystemColor(SWT.COLOR_WIDGET_NORMAL_SHADOW), display
						.getSystemColor(SWT.COLOR_WIDGET_DARK_SHADOW), 2, 2);
	}

	public static void drawButtonUp(GC gc, String text, int textAlign,
			Image image, int imageAlign, Rectangle r, int leftMargin,
			int topMargin) {
		Display display = Display.getCurrent();
		drawButtonUp(gc, text, textAlign, image, imageAlign, r.x, r.y, r.width,
				r.height, display.getSystemColor(SWT.COLOR_WIDGET_BACKGROUND),
				display.getSystemColor(SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW),
				display.getSystemColor(SWT.COLOR_WIDGET_NORMAL_SHADOW), display
						.getSystemColor(SWT.COLOR_WIDGET_DARK_SHADOW),
				leftMargin, topMargin);
	}

	public static void drawButtonUp(GC gc, String text, int textAlign,
			Image image, int imageAlign, int x, int y, int w, int h) {
		Display display = Display.getCurrent();
		drawButtonUp(gc, text, textAlign, image, imageAlign, x, y, w, h,
				display.getSystemColor(SWT.COLOR_WIDGET_BACKGROUND), display
						.getSystemColor(SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW),
				display.getSystemColor(SWT.COLOR_WIDGET_NORMAL_SHADOW), display
						.getSystemColor(SWT.COLOR_WIDGET_DARK_SHADOW), 2, 2);
	}

	public static void drawButtonUp(GC gc, String text, int textAlign,
			Image image, int imageAlign, Rectangle r) {
		drawButtonUp(gc, text, textAlign, image, imageAlign, r.x, r.y, r.width,
				r.height);
	}

	public static void drawButtonDown(GC gc, String text, int textAlign,
			Image image, int imageAlign, int x, int y, int w, int h,
			Color face, Color shadowNormal, int leftMargin, int topMargin) {
		Color prevForeground = gc.getForeground();
		Color prevBackground = gc.getBackground();
		try {
			gc.setBackground(face);
			gc.setForeground(shadowNormal);
			gc.drawRectangle(x, y, w - 1, h - 1);
			gc.fillRectangle(x + 1, y + 1, 1 + leftMargin, h - 2);
			gc.fillRectangle(x + 1, y + 1, w - 2, topMargin + 1);
			gc.setForeground(prevForeground);
			drawTextImage(gc, text, textAlign, image, imageAlign, x + 2
					+ leftMargin, y + 2 + topMargin, w - 3 - leftMargin, h - 3
					- topMargin);
		} finally {
			gc.setForeground(prevForeground);
			gc.setBackground(prevBackground);
		}
	}

	public static void drawButtonDown(GC gc, String text, int textAlign,
			Image image, int imageAlign, int x, int y, int w, int h) {
		Display display = Display.getCurrent();
		drawButtonDown(gc, text, textAlign, image, imageAlign, x, y, w, h,
				display.getSystemColor(SWT.COLOR_WIDGET_BACKGROUND), display
						.getSystemColor(SWT.COLOR_WIDGET_NORMAL_SHADOW), 2, 2);
	}

	public static void drawButtonDown(GC gc, String text, int textAlign,
			Image image, int imageAlign, Rectangle r) {
		drawButtonDown(gc, text, textAlign, image, imageAlign, r.x, r.y,
				r.width, r.height);
	}

	public static void drawButtonDown(GC gc, String text, int textAlign,
			Image image, int imageAlign, int x, int y, int w, int h, Color face) {
		Display display = Display.getCurrent();
		drawButtonDown(gc, text, textAlign, image, imageAlign, x, y, w, h,
				face, display.getSystemColor(SWT.COLOR_WIDGET_NORMAL_SHADOW),
				2, 2);
	}

	public static void drawButtonDeepDown(GC gc, String text, int textAlign,
			Image image, int imageAlign, int x, int y, int w, int h) {
		Display display = Display.getCurrent();
		gc.setForeground(display.getSystemColor(SWT.COLOR_BLACK));
		gc.drawLine(x, y, x + w - 2, y);
		gc.drawLine(x, y, x, y + h - 2);
		gc.setForeground(display.getSystemColor(SWT.COLOR_WHITE));
		gc.drawLine(x + w - 1, y, x + w - 1, y + h - 1);
		gc.drawLine(x, y + h - 1, x + w - 1, y + h - 1);
		gc.setForeground(display.getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));
		gc.drawLine(x + 1, y + h - 2, x + w - 2, y + h - 2);
		gc.drawLine(x + w - 2, y + h - 2, x + w - 2, y + 1);
		//
		gc.setForeground(display.getSystemColor(SWT.COLOR_WIDGET_FOREGROUND));
		gc.setBackground(display.getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));
		gc.fillRectangle(x + 2, y + 2, w - 4, 1);
		gc.fillRectangle(x + 1, y + 2, 2, h - 4);
		//
		gc.setBackground(display.getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));
		drawTextImage(gc, text, textAlign, image, imageAlign, x + 2 + 1,
				y + 2 + 1, w - 4, h - 3 - 1);
	}

	public static void drawButtonDeepDown(GC gc, String text, int textAlign,
			Image image, int imageAlign, Rectangle r) {
		drawButtonDeepDown(gc, text, textAlign, image, imageAlign, r.x, r.y,
				r.width, r.height);
	}

	public static void drawFlatButtonUp(GC gc, String text, int textAlign,
			Image image, int imageAlign, int x, int y, int w, int h,
			Color face, Color shadowLight, Color shadowNormal, int leftMargin,
			int topMargin) {
		Color prevForeground = gc.getForeground();
		Color prevBackground = gc.getBackground();
		try {
			gc.setForeground(shadowLight);
			gc.drawLine(x, y, x + w - 1, y);
			gc.drawLine(x, y, x, y + h);
			gc.setForeground(shadowNormal);
			gc.drawLine(x + w, y, x + w, y + h);
			gc.drawLine(x + 1, y + h, x + w, y + h);
			//
			gc.setBackground(face);
			gc.fillRectangle(x + 1, y + 1, leftMargin, h - 1);
			gc.fillRectangle(x + 1, y + 1, w - 1, topMargin);
			//
			gc.setBackground(face);
			gc.setForeground(prevForeground);
			drawTextImage(gc, text, textAlign, image, imageAlign, x + 1
					+ leftMargin, y + 1 + topMargin, w - 1 - leftMargin, h - 1
					- topMargin);
		} finally {
			gc.setForeground(prevForeground);
			gc.setBackground(prevBackground);
		}
	}

	public static void drawShadowImage(GC gc, Image image, int x, int y,
			int alpha) {
		Display display = Display.getCurrent();
		Point imageSize = new Point(image.getBounds().width,
				image.getBounds().height);
		//
		ImageData imgData = new ImageData(imageSize.x, imageSize.y, 24,
				new PaletteData(255, 255, 255));
		imgData.alpha = alpha;
		Image img = new Image(display, imgData);
		GC imgGC = new GC(img);
		imgGC.drawImage(image, 0, 0);
		gc.drawImage(img, x, y);
		imgGC.dispose();
		img.dispose();
	}
}