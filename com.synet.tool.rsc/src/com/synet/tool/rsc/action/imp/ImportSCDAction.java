/**
 * Copyright (c) 2007-2017 泗业技术咨询有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application.
 */
package com.synet.tool.rsc.action.imp;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.swt.SWT;

import com.shrcn.found.common.Constants;
import com.shrcn.found.ui.action.ConfigAction;
import com.shrcn.found.ui.util.DialogHelper;
import com.shrcn.found.ui.util.ProgressManager;
import com.shrcn.tool.found.das.impl.BeanDaoImpl;
import com.synet.tool.rsc.io.SCDImporter;
import com.synet.tool.rsc.model.Tb1065LogicallinkEntity;
import com.synet.tool.rsc.util.ProjectFileManager;

 /**
 * 
 * @author 陈春(mailto:chench80@126.com)
 * @version 1.0, 2018-8-7
 */
public class ImportSCDAction extends ConfigAction {
	
	public ImportSCDAction(String title) {
		super(title);
	}

	@Override
	public void run() {
		//TODO 未打开工程或者创建工程时不能导入，数据库未初始化；
		//导入后需要刷新导航树，重新生成导航树节点
		final String path = DialogHelper.selectFile(getShell(), SWT.OPEN, "*.scd;*.SCD");
		if (path != null) {
			ProgressManager.execute(new IRunnableWithProgress() {
				
				@Override
				public void run(IProgressMonitor monitor) throws InvocationTargetException,
						InterruptedException {
					new SCDImporter(path).execute(); 
					ProjectFileManager.getInstance().renameScd(Constants.CURRENT_PRJ_NAME, path);
					@SuppressWarnings("unchecked")
					List<Tb1065LogicallinkEntity> cbs = (List<Tb1065LogicallinkEntity>) BeanDaoImpl.getInstance().getAll(Tb1065LogicallinkEntity.class);
					if (cbs.size() > 0) {
						DialogHelper.showAsynInformation("SCD导入成功！");
					}
				}
			});
		}
	}

}

