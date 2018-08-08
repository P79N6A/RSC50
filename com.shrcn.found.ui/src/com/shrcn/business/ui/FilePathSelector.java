/**
 * Copyright (c) 2007-2013 上海思源弘瑞自动化有限公司. All rights reserved. 
 * This program is an eclipse Rich Client Application
 * designed for IED configuration and debuging.
 */
package com.shrcn.business.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

import com.shrcn.found.common.util.StringUtil;
import com.shrcn.found.ui.util.DialogHelper;
import com.shrcn.found.ui.util.SwtUtil;

/**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2014-10-22
 */
/**
 * $Log$
 */
public class FilePathSelector extends Composite {
	
	private Text txtPath;
	private Button btSelect;
	
	private String title;
	private String[] exts;
	
	public FilePathSelector(Composite parent, String title, String...exts) {
		super(parent, SWT.NONE);
		this.title = title;
		this.exts = exts;
		createContents();
	}
	
	private void createContents() {
		setLayout(new GridLayout(3, false));
		
		SwtUtil.createLabel(this, title + "：", null);
		txtPath = SwtUtil.createText(this, SWT.READ_ONLY | SWT.BORDER, "", new GridData(GridData.FILL_HORIZONTAL));
		btSelect = SwtUtil.createPushButton(this, "...", null);
		btSelect.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				final String fileName = DialogHelper.selectFile(getShell(), SWT.OPEN, exts);
				if (!StringUtil.isEmpty(fileName))
					txtPath.setText(fileName);
			}
		});
	}
	
	public String getFilePath() {
		return txtPath.getText();
	}
	
}
