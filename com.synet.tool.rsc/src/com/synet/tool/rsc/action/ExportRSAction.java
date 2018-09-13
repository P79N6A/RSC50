/**
 * Copyright (c) 2007-2017 泗业技术咨询有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application.
 */
package com.synet.tool.rsc.action;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.operation.IRunnableWithProgress;

import com.shrcn.found.ui.action.ConfigAction;
import com.shrcn.found.ui.util.ProgressManager;
import com.shrcn.found.ui.view.ConsoleManager;
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
			final ConnParam connParam = dialog.getConnParam();
			ProgressManager.execute(new IRunnableWithProgress() {
				@Override
				public void run(IProgressMonitor monitor) throws InvocationTargetException,
						InterruptedException {
					boolean b = new ExportDataHandler().exportData(connParam, monitor);
					if (b) {
						ConsoleManager.getInstance().append("工程配置导出完成！");
					}
				}
			});
		}
	}

}

