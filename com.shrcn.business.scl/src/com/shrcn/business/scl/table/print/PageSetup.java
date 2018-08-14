/**
 * Copyright (c) 2007-2009 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based on IEC61850 SCT.
 */
package com.shrcn.business.scl.table.print;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

/**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2009-11-18
 */
/**
 * $Log: PageSetup.java,v $
 * Revision 1.1  2010/03/02 07:49:16  cchun
 * Add:添加重构代码
 *
 * Revision 1.2  2010/01/21 08:48:17  gj
 * Update:完成UI插件的国际化字符串资源提取
 *
 * Revision 1.1  2009/11/19 08:28:51  cchun
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
 * A static data storage and a GUI dialog to change the data. PDocument uses the
 * settings of the static variables in PageSetup. You can open a Dialog to
 * changes these values by creating a PageSetup and call the open() function.
 * 
 * @author Friederich Kupzog
 */
public class PageSetup extends KDialog {
	protected Composite root;

	private Combo combFormat, cmbScalierung, cmbMargin;

	private Button butPortrait, butLandscape;

	public final static int MARGIN_SMALL = 0;

	public final static int MARGIN_MEDIUM = 1;

	public final static int MARGIN_HUGE = 2;

	public final static String[] formatNames = { "A3", "A4", "A5" }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

	public final static String[] scalings = { "100%", "90%", "80%", "70%", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
			"60%", "50%" }; //$NON-NLS-1$ //$NON-NLS-2$

	public static double paperHeight = 20.6;// 29.6;

	public static double paperWidth = 29.6;// 20.6;

	public static String format = "A4"; //$NON-NLS-1$

	public static boolean portrait = true;

	public static int scaling = 100;

	public static int marginStyle = MARGIN_MEDIUM;

	public static int rowCount = 29;

	public static boolean isTable = true;

	public PageSetup(Shell parent) {
		super(parent, "Seite einrichten", IconSource.PRINT); //$NON-NLS-1$
		createContents();

		// setDialogImage(IconSource.getImage("SeiteEinrichten"));
		setDialogImage(IconSource.PRINT);
		addButtonRight("OK", "", true); //$NON-NLS-1$ //$NON-NLS-2$
		addButtonRight("Abbrechen", ""); //$NON-NLS-1$ //$NON-NLS-2$
		combFormat.setText(format);
	}

	public int getShellStyle() {
		return SWT.CLOSE | SWT.APPLICATION_MODAL;
	}

	protected void createContents() {
		guiMainArea.setLayout(new FillLayout());
		root = new Composite(guiMainArea, SWT.NONE);
		final GridLayout gridLayout = new GridLayout();
		gridLayout.verticalSpacing = 10;
		gridLayout.numColumns = 2;
		root.setLayout(gridLayout);

		{
			final Label l = new Label(root, SWT.NONE);
			l.setText("Papierformat:"); //$NON-NLS-1$
			final GridData gridData_2 = new GridData();
			gridData_2.widthHint = 80;
			l.setLayoutData(gridData_2);
		}
		{
			combFormat = new Combo(root, SWT.BORDER | SWT.READ_ONLY);
			combFormat
					.setToolTipText("Bestimmt die PapiergroBe. Diese muss mit der Druckereinstellung ubereinstimmen."); //$NON-NLS-1$
			for (int i = 0; i < formatNames.length; i++) {
				combFormat.add(formatNames[i]);
			}
			combFormat.setText(format);

			final GridData gridData_1 = new GridData(GridData.FILL_HORIZONTAL);
			gridData_1.widthHint = 180;
			combFormat.setLayoutData(gridData_1);
		}
		{
			final Label label = new Label(root, SWT.NONE);
			label.setText("Seitenrander:"); //$NON-NLS-1$
			label.setLayoutData(new GridData(GridData.FILL_BOTH));
		}
		{
			cmbMargin = new Combo(root, SWT.READ_ONLY);
			cmbMargin.setToolTipText("Bestimmt die Breite der Rander."); //$NON-NLS-1$
			cmbMargin.add("Schmale Rander"); //$NON-NLS-1$
			cmbMargin.add("Normale Rander"); //$NON-NLS-1$
			cmbMargin.add("Breite Rander"); //$NON-NLS-1$
			cmbMargin.select(marginStyle);
			cmbMargin.setLayoutData(new GridData(GridData.FILL_BOTH));
		}
		{
			final Label label = new Label(root, SWT.NONE);
			final GridData gridData = new GridData(
					GridData.VERTICAL_ALIGN_BEGINNING);
			gridData.horizontalSpan = 1;
			label.setLayoutData(gridData);
			label.setText("Ausrichtung:"); //$NON-NLS-1$
		}
		{
			butPortrait = new Button(root, SWT.RADIO);
			butPortrait
					.setToolTipText("Bestimmt, ob das Papier hochkant oder Breit bedruckt werden soll. \nDiese Einstellung muss mit der des Druckers ubereinstimmen"); //$NON-NLS-1$
			butPortrait.setLayoutData(new GridData(
					GridData.VERTICAL_ALIGN_BEGINNING));
			butPortrait.setText("Hochformat"); //$NON-NLS-1$
			butPortrait.setSelection(portrait);
		}
		{
			butLandscape = new Button(root, SWT.RADIO);
			butLandscape.setLayoutData(new GridData(
					GridData.VERTICAL_ALIGN_BEGINNING));
			butLandscape.setText("Breitformat"); //$NON-NLS-1$
			butLandscape.setSelection(!portrait);
			butLandscape
					.setToolTipText("Bestimmt, ob das Papier hochkant oder quer bedruckt werden soll. \nDiese Einstellung muss mit der des Druckers ubereinstimmen"); //$NON-NLS-1$
		}
		{
			final Label label = new Label(root, SWT.NONE);
			label.setText("Skalierung:"); //$NON-NLS-1$
			label.setLayoutData(new GridData(GridData.FILL_BOTH));
		}
		{
			cmbScalierung = new Combo(root, SWT.READ_ONLY);
			cmbScalierung.setItems(scalings);
			cmbScalierung.select(10 - (scaling / 10));
			cmbScalierung.setLayoutData(new GridData(GridData.FILL_BOTH));
			cmbScalierung
					.setToolTipText("Hiermit konnen Sie dir GroBe des Ausdrucks veringern, so daB mehr auf eine Seite passt."); //$NON-NLS-1$
		}

	}

	/*
	 * overridden from superclass
	 */
	protected void onButton(Button button, String buttonText) {
		if (buttonText.equals("OK")) { //$NON-NLS-1$
			saveSettings();
		}
		close();
	}

	protected void saveSettings() {
		format = combFormat.getText();
		scaling = 100 - 10 * (cmbScalierung.getSelectionIndex());
		marginStyle = cmbMargin.getSelectionIndex();

		portrait = butPortrait.getSelection();

		if (portrait) {
			paperHeight = getPaperHeightInCm(format);
			paperWidth = getPaperWidthInCm(format);
		} else {
			paperWidth = getPaperHeightInCm(format);
			paperHeight = getPaperWidthInCm(format);
		}

	}

	public static double getPaperHeightInCm(String formatName) {
		if (formatName.equals("A5")) { //$NON-NLS-1$
			return 20.8;
		} else if (formatName.equals("A4")) { //$NON-NLS-1$
			return 29.6;
		} else if (formatName.equals("A3")) { //$NON-NLS-1$
			return 41.6;
		}
		return 1.0;
	}

	public static double getPaperWidthInCm(String formatName) {
		if (formatName.equals("A5")) { //$NON-NLS-1$
			return 14.8;
		} else if (formatName.equals("A4")) { //$NON-NLS-1$
			return 20.6;
		} else if (formatName.equals("A3")) { //$NON-NLS-1$
			return 29.6;
		}
		return 1.0;
	}

}
