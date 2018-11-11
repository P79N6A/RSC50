package com.synet.tool.rsc.action;

import com.shrcn.found.ui.action.ConfigAction;
import com.synet.tool.rsc.dialog.FuncClassDialog;

public class FuncConfigAction  extends ConfigAction {
	
	public FuncConfigAction(String title) {
		super(title);
	}

	@Override
	public void run() {
		FuncClassDialog dialog = new FuncClassDialog(getShell());
		dialog.open();
	}

}
