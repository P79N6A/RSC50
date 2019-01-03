/**
 * Copyright (c) 2007-2017 泗业技术咨询有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application.
 */
package com.synet.tool.rsc.action.imp;

import java.io.File;
import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.swt.SWT;

import com.shrcn.found.common.event.EventConstants;
import com.shrcn.found.common.event.EventManager;
import com.shrcn.found.common.util.StringUtil;
import com.shrcn.found.ui.util.DialogHelper;
import com.shrcn.found.ui.util.ProgressManager;
import com.synet.tool.rsc.RSCProperties;
import com.synet.tool.rsc.compare.ied.SCDComparator;
import com.synet.tool.rsc.das.ProjectManager;
import com.synet.tool.rsc.io.OnlySCDImporter;

 /**
 * 
 * @author 陈春(mailto:chench80@126.com)
 * @version 1.0, 2018-8-7
 */
public class ImportSCDAction extends BaseImportAction {
	
	public ImportSCDAction(String title) {
		super(title);
	}

	@Override
	public void run() {
		final String path = DialogHelper.selectFile(getShell(), SWT.OPEN, "*.scd;*.SCD");
		if (path != null) {
			boolean replaceMode = RSCProperties.getInstance().isReplaceMode();
			String srcpath = ProjectManager.getInstance().getProjectScdPath();
			boolean notExists = StringUtil.isEmpty(srcpath) || !new File(srcpath).exists();
			if (replaceMode && notExists) {
				if (DialogHelper.showConfirm("当前工程未曾导入过SCD，确定用起始模式导入吗？")) {
					replaceMode = false;
				} else {
					return;
				}
			}
			if (!replaceMode) {
				ProgressManager.execute(new IRunnableWithProgress() {
					@Override
					public void run(IProgressMonitor monitor) throws InvocationTargetException,
							InterruptedException {
						new OnlySCDImporter(path).execute(monitor); 
						DialogHelper.showAsynInformation("SCD导入成功！");
						EventManager.getDefault().notify(EventConstants.PROJECT_RELOAD, null);
					}
				});
			} else {
				compareImportFile(SCDComparator.class, srcpath, path);
			}
		}
	}

}

