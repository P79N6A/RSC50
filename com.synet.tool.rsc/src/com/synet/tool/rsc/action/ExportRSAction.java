/**
 * Copyright (c) 2007-2017 泗业技术咨询有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application.
 */
package com.synet.tool.rsc.action;

import org.eclipse.jface.dialogs.IDialogConstants;

import com.shrcn.found.ui.action.ConfigAction;
import com.shrcn.found.ui.util.DialogHelper;
import com.synet.tool.rsc.dialog.OracleConnDialog;
import com.synet.tool.rsc.jdbc.ConnParam;
import com.synet.tool.rsc.jdbc.ExportDataHandler;

 /**
 * 
 * @author 陈春(mailto:chench80@126.com)
 * @version 1.0, 2018-8-7
 */
public class ExportRSAction extends ConfigAction {
	
	public ExportRSAction(String title) {
		super(title);
	}

	@Override
	public void run() {
		OracleConnDialog dialog = new OracleConnDialog(getShell());
		if (dialog.open() == IDialogConstants.OK_ID) {
			ConnParam connParam = dialog.getConnParam();
			boolean b = new ExportDataHandler().exportData(connParam);
			if (b) {
				DialogHelper.showAsynInformation("工程配置导出完成！");
			}
		}
	}

}

