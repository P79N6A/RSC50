/**
 * Copyright (c) 2007-2013 上海思源弘瑞自动化有限公司. All rights reserved. 
 * This program is an eclipse Rich Client Application
 * designed for IED configuration and debuging.
 */
package com.shrcn.found.ui.table;

import org.eclipse.swt.widgets.Shell;

import com.shrcn.found.ui.app.WrappedDialog;

/**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2014-10-21
 */
/**
 * $Log$
 */
public class KTableEditorDialog extends WrappedDialog {
	
	protected Object item;

	public KTableEditorDialog(Shell parentShell, Object item) {
		super(parentShell);
		this.item = item;
	}

	public Object getItem() {
		return item;
	}

}
