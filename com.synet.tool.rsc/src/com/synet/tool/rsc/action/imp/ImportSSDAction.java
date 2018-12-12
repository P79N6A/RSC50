/**
 * Copyright (c) 2007-2017 泗业技术咨询有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application.
 */
package com.synet.tool.rsc.action.imp;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.swt.SWT;

import com.shrcn.found.common.event.EventConstants;
import com.shrcn.found.common.event.EventManager;
import com.shrcn.found.ui.util.DialogHelper;
import com.shrcn.found.ui.util.ProgressManager;
import com.synet.tool.rsc.io.SSDImporter;


 /**
 * 
 * @author 陈春(mailto:chench80@126.com)
 * @version 1.0, 2018-8-7
 */
public class ImportSSDAction extends BaseImportAction {
	
	public ImportSSDAction(String title) {
		super(title);
	}

	@Override
	public void run() {
		final String path = DialogHelper.selectFile(getShell(), SWT.OPEN, "*.ssd;*.SSD");
		if (path != null) {
			ProgressManager.execute(new IRunnableWithProgress() {
				@Override
				public void run(IProgressMonitor monitor) throws InvocationTargetException,
						InterruptedException {
					new SSDImporter(path).execute(monitor);
					DialogHelper.showAsynInformation("SSD导入成功！");
					EventManager.getDefault().notify(EventConstants.PROJECT_RELOAD, null);
				}
			});
		}
	}

}

