/**
 * Copyright (c) 2007-2013 上海思源弘瑞自动化有限公司. All rights reserved. 
 * This program is an eclipse Rich Client Application
 * designed for IED configuration and debuging.
 */
package com.shrcn.found.ui.dialog;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.shrcn.found.ui.app.WrappedDialog;
import com.shrcn.found.ui.util.SwtUtil;

/**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2013-9-24
 */
/**
 * $Log: FileInputDialog.java,v $
 * Revision 1.1  2013/09/25 01:19:54  cchun
 * Add:文件选择对话框
 *
 */
public class FileInputDialog extends WrappedDialog {

	private Text txtPath;
	
	private String filePath;
	private String title;
	private String[] extensions;
	
	/**
	 * Create the dialog
	 * @param parentShell
	 */
	public FileInputDialog(Shell parentShell, String filePath, String title, String...extensions) {
		super(parentShell);
		this.filePath = filePath;
		this.title = title;
		this.extensions = extensions;
	}

	/**
	 * Create contents of the dialog
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		getShell().setText(title);
		Composite container = (Composite) super.createDialogArea(parent);
		this.txtPath = SwtUtil.createFileSelector(container, "路径：", extensions);
		if (filePath != null)
			txtPath.setText(filePath);
		return container;
	}
	
	@Override
	protected void okPressed() {
		this.filePath = txtPath.getText();
		super.okPressed();
	}

	public String getFilePath() {
		return filePath;
	}

	/**
	 * Create contents of the button bar
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL,
				true);
		createButton(parent, IDialogConstants.CANCEL_ID,
				IDialogConstants.CANCEL_LABEL, false);
	}

	/**
	 * Return the initial size of the dialog
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(520, 150);
	}

}
