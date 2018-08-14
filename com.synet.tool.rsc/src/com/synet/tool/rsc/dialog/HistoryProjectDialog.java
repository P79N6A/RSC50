/**
 * Copyright (c) 2007, 2008 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based Visual Device Develop System.
 */
package com.synet.tool.rsc.dialog;

import java.util.ArrayList;

import org.eclipse.swt.widgets.Shell;

import com.shrcn.found.common.Constants;
import com.shrcn.found.common.util.URLUtil;
import com.shrcn.found.ui.dialog.HistoryListDialog;
import com.synet.tool.rsc.das.ProjectManager;
import com.synet.tool.rsc.util.ProjectFileManager;

public class HistoryProjectDialog extends HistoryListDialog {
	
	/**
	 * Create the dialog
	 * @param parentShell
	 */
	public HistoryProjectDialog(Shell shell) {
		super(shell, "历史工程");
	}
	
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("历史工程"); //$NON-NLS-1$
	}
	
	@Override
	protected String[] getHistoryItems() {
		java.util.List<String> names = ProjectFileManager.getInstance().getHistoryItems();
		java.util.List<String> hisList = new ArrayList<String>();
		for(String name : names) {
			if(!name.equals(Constants.DEFAULT_PRJ_NAME)
					&& !name.equals(Constants.CURRENT_PRJ_NAME)){
				name = URLUtil.url2String(name);
				if (name != null)
					hisList.add(name);
			}
		}
		return hisList.toArray(new String[hisList.size()]);
	}

	@Override
	protected void deleteFromDB(String item) {
		ProjectManager.getInstance().removeDb(item);
		ProjectFileManager.getInstance().removeProject(item);
	}
}
