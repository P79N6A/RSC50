/**
 * Copyright (c) 2007-2009 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based on IEC61850 SCT.
 */
package com.shrcn.business.scl.table.print;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

import com.shrcn.found.ui.app.WrappedDialog;
import com.shrcn.found.ui.util.ImageConstants;
import com.shrcn.found.ui.util.ImgDescManager;

/**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2009-11-19
 */
/**
 * $Log: PrintTypeDialog.java,v $
 * Revision 1.2  2010/11/12 09:21:11  cchun
 * Refactor:修改IconManager类名为ImgDescManager，并移动至common项目
 *
 * Revision 1.1  2010/03/02 07:49:16  cchun
 * Add:添加重构代码
 *
 * Revision 1.3  2010/01/22 01:39:39  cchun
 * Update:重构关联检查国际化
 *
 * Revision 1.2  2010/01/21 08:48:15  gj
 * Update:完成UI插件的国际化字符串资源提取
 *
 * Revision 1.1  2009/11/19 09:33:19  cchun
 * Update:增加纸型选择打印功能
 *
 */
public class PrintTypeDialog extends WrappedDialog {
	
	Button a3Button;
	Button a4Button;
	
	/**
	 * Create the dialog
	 * @param parentShell
	 */
	public PrintTypeDialog(Shell parentShell) {
		super(parentShell);
	}

	/**
	 * Create contents of the dialog
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);
		container.setLayout(new FormLayout());

		a3Button = new Button(container, SWT.RADIO);
		final FormData fd_a3Button = new FormData();
		fd_a3Button.left = new FormAttachment(0, 32);
		fd_a3Button.right = new FormAttachment(0, 105);
		a3Button.setLayoutData(fd_a3Button);
		a3Button.setText("A3"); //$NON-NLS-1$

		Label label;
		label = new Label(container, SWT.NONE);
		final FormData fd_label = new FormData();
		fd_label.top = new FormAttachment(0, 72);
		fd_label.bottom = new FormAttachment(0, 90);
		label.setLayoutData(fd_label);
		label.setImage(ImgDescManager.getImageDesc(ImageConstants.WARN).createImage());

		CLabel label_1;
		label_1 = new CLabel(container, SWT.NONE);
		fd_label.left = new FormAttachment(label_1, -26, SWT.LEFT);
		fd_label.right = new FormAttachment(label_1, -5, SWT.LEFT);
		final FormData fd_label_1 = new FormData();
		fd_label_1.top = new FormAttachment(0, 65);
		fd_label_1.bottom = new FormAttachment(0, 100);
		fd_label_1.right = new FormAttachment(0, 340);
		fd_label_1.left = new FormAttachment(0, 55);
		label_1.setLayoutData(fd_label_1);
		label_1.setText(Messages.getString("PrintTypeDialog.config.print.info")); //$NON-NLS-1$

		a4Button = new Button(container, SWT.RADIO);
		fd_a3Button.bottom = new FormAttachment(a4Button, 16, SWT.TOP);
		fd_a3Button.top = new FormAttachment(a4Button, 0, SWT.TOP);
		final FormData fd_a4Button = new FormData();
		fd_a4Button.bottom = new FormAttachment(0, 41);
		fd_a4Button.top = new FormAttachment(0, 25);
		fd_a4Button.right = new FormAttachment(0, 223);
		fd_a4Button.left = new FormAttachment(0, 130);
		a4Button.setLayoutData(fd_a4Button);
		a4Button.setText("A4"); //$NON-NLS-1$
		//
		return container;
	}

	/**
	 * Create contents of the button bar
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, Messages.getString("OK"),
				true);
		createButton(parent, IDialogConstants.CANCEL_ID,
				Messages.getString("CANCEL"), false);
	}

	/**
	 * Return the initial size of the dialog
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(371, 184);
	}

	@Override
	protected void buttonPressed(int buttonId) {
		switch(buttonId) {
			case IDialogConstants.OK_ID:
				onSelected();
				setReturnCode(OK);
				close();
				break;
			case IDialogConstants.CANCEL_ID:
				setReturnCode(CANCEL);
				close();
				break;
		}
	}
	
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText(Messages.getString("PrintTypeDialog.select.paper.type")); //$NON-NLS-1$
	}
	
	private void onSelected() {
		if(a3Button.getSelection()) {
			PageSetup.format = "A3"; //$NON-NLS-1$
			PageSetup.rowCount = 65;
			PageSetup.marginStyle = PageSetup.MARGIN_SMALL;
		} else {
			PageSetup.format = "A4"; //$NON-NLS-1$
			PageSetup.rowCount = 29;
			PageSetup.marginStyle = PageSetup.MARGIN_SMALL;
		}
	}
}
