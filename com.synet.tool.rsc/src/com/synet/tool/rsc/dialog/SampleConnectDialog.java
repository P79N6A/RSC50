package com.synet.tool.rsc.dialog;

import org.eclipse.swt.widgets.Shell;

import com.shrcn.found.ui.app.WrappedTitleAreaDialog;

public class SampleConnectDialog extends WrappedTitleAreaDialog {

	private String curEntryName;
	
	public SampleConnectDialog(Shell parentShell) {
		super(parentShell);
	}

	public SampleConnectDialog(Shell defaultShell, String curEntryName) {
		super(defaultShell);
		this.curEntryName = curEntryName;
	}

}
