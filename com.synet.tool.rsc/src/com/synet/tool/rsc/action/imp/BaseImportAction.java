/**
 * Copyright (c) 2018-2019 上海泗业技术咨询有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application.
 */
package com.synet.tool.rsc.action.imp;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.swt.widgets.Display;

import com.shrcn.found.common.event.EventConstants;
import com.shrcn.found.common.event.EventManager;
import com.shrcn.found.common.util.ObjectUtil;
import com.shrcn.found.file.util.FileManipulate;
import com.shrcn.found.ui.action.ConfigAction;
import com.shrcn.found.ui.editor.BaseEditorInput;
import com.shrcn.found.ui.util.ProgressManager;
import com.shrcn.found.ui.view.ConsoleManager;
import com.synet.tool.rsc.RSCConstants;
import com.synet.tool.rsc.compare.Difference;
import com.synet.tool.rsc.compare.SCLComparator;
import com.synet.tool.rsc.das.ProjectManager;
import com.synet.tool.rsc.util.NavgTreeFactory;
import com.synet.tool.rsc.util.ProjectFileManager;

 /**
 * 
 * @author 陈春(mailto:chench80@126.com)
 * @version 1.0, 2018-8-27
 */
public class BaseImportAction extends ConfigAction {

	public BaseImportAction(String title) {
		super(title);
	}
	
	@Override
	public boolean isEnabled() {
		return !ProjectFileManager.getInstance().isClosed();
	}

	protected void compareImportFile(final Class<?> cmpClass, final String srcpath, final String destpath) {
		String tempPath = null;
		if (srcpath.toLowerCase().endsWith(".ssd")) {
			tempPath = ProjectManager.getInstance().getProjectSsdTempPath();
		} else {
			tempPath = ProjectManager.getInstance().getProjectScdTempPath();
		}
		FileManipulate.copyByChannel(destpath, tempPath);
		EventManager.getDefault().notify(EventConstants.CLEAR_CONFIG, null);
		ProgressManager.execute(new IRunnableWithProgress() {
			@Override
			public void run(IProgressMonitor monitor) throws InvocationTargetException,
					InterruptedException {
				SCLComparator cmp = (SCLComparator) ObjectUtil.newInstance(cmpClass, 
						new Class<?>[] {String.class, String.class, IProgressMonitor.class}, 
						new Object[] {srcpath, destpath, monitor});
				long t = System.currentTimeMillis();
				final List<Difference> diffs = cmp.execute();
				ConsoleManager.getInstance().append("SCL对比耗时：" + (System.currentTimeMillis() - t) + "ms");
				// 打开增量处理界面
				Display.getDefault().asyncExec(new Runnable() {
					@Override
					public void run() {
						BaseEditorInput input = NavgTreeFactory.createEditInput("SCL增量导入", "compare.gif", RSCConstants.ET_SCL_COMP, diffs);
						EventManager.getDefault().notify(EventConstants.OPEN_CONFIG, input);
					}});
			}
		});
	}
}

