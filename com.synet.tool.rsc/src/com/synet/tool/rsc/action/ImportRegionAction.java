/**
 * Copyright (c) 2007-2017 泗业技术咨询有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application.
 */
package com.synet.tool.rsc.action;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;

import com.shrcn.found.common.event.EventConstants;
import com.shrcn.found.common.event.EventManager;
import com.shrcn.found.common.util.StringUtil;
import com.shrcn.found.file.excel.SheetsHandler;
import com.shrcn.found.ui.action.ConfigAction;
import com.shrcn.found.ui.editor.BaseEditorInput;
import com.shrcn.found.ui.util.DialogHelper;
import com.shrcn.found.ui.util.ProgressManager;
import com.shrcn.found.ui.view.ConsoleManager;
import com.shrcn.tool.found.das.impl.BeanDaoImpl;
import com.synet.tool.rsc.RSCConstants;
import com.synet.tool.rsc.RSCProperties;
import com.synet.tool.rsc.compare.Difference;
import com.synet.tool.rsc.compare.region.RegionComparator;
import com.synet.tool.rsc.excel.handler.RegionHandler;
import com.synet.tool.rsc.model.Tb1049RegionEntity;
import com.synet.tool.rsc.service.CubicleEntityService;
import com.synet.tool.rsc.util.ExcelReaderUtil;
import com.synet.tool.rsc.util.NavgTreeFactory;

 /**
 * 模型检查
 * @author 陈春(mailto:chench80@126.com)
 * @version 1.0, 2018-8-7
 */
public class ImportRegionAction extends ConfigAction {
	
	public ImportRegionAction(String title) {
		super(title);
	}

	@Override
	public void run() {
		String xlspath = DialogHelper.selectFile(getShell(), SWT.OPEN, "*.xlsx");
		if (StringUtil.isEmpty(xlspath) || !new File(xlspath).exists()) {
			DialogHelper.showWarning("导入文件路径为空或者不存在！");
			return;
		}
		SheetsHandler handler = new RegionHandler();
		final List<Tb1049RegionEntity> result = (List<Tb1049RegionEntity>) ExcelReaderUtil.parseByHandler(xlspath, handler);
		boolean replaceMode = RSCProperties.getInstance().isReplaceMode();
		if (!replaceMode) {
			new CubicleEntityService().doImport(result);
			EventManager.getDefault().notify(EventConstants.PROJECT_RELOAD, null);
		} else {
			final List<Tb1049RegionEntity> bdsListSrc = (List<Tb1049RegionEntity>) BeanDaoImpl.getInstance().getAll(Tb1049RegionEntity.class);
			ProgressManager.execute(new IRunnableWithProgress() {
				@Override
				public void run(IProgressMonitor monitor) throws InvocationTargetException,
						InterruptedException {
					RegionComparator cmp = new RegionComparator(bdsListSrc, result, monitor);
					long t = System.currentTimeMillis();
					final List<Difference> diffs = cmp.execute();
					ConsoleManager.getInstance().append("区域屏柜对比耗时：" + (System.currentTimeMillis() - t) + "ms");
					// 打开增量处理界面
					Display.getDefault().asyncExec(new Runnable() {
						@Override
						public void run() {
							BaseEditorInput input = NavgTreeFactory.createEditInput("区域屏柜增量导入", "compare.gif", RSCConstants.ET_SCL_COMP, diffs);
							EventManager.getDefault().notify(EventConstants.OPEN_CONFIG, input);
						}});
				}
			});
		}
	}

}

