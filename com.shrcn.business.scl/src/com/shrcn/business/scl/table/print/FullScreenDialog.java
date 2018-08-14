/**
 * Copyright (c) 2007-2009 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based on IEC61850 SCT.
 */
package com.shrcn.business.scl.table.print;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;

/**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2009-12-7
 */
/**
 * $Log: FullScreenDialog.java,v $
 * Revision 1.1  2010/03/02 07:49:12  cchun
 * Add:添加重构代码
 *
 * Revision 1.1  2009/12/07 03:04:58  cchun
 * Update:添加信号关联全屏查看功能
 *
 */
public class FullScreenDialog extends KDialog {

	/**
	 * Creates a new Dialog.
	 * 
	 * @param parent
	 *            The parent shell for this dialog.
	 */
	public FullScreenDialog(Shell parent) {
		createShell(parent);
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
	public FullScreenDialog(Shell parent, String title, Image icon) {
		this(parent);
		setTitle(title);
		setShellImage(icon);
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
	}
}