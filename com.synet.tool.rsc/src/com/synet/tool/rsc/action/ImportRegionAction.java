/**
 * Copyright (c) 2007-2017 泗业技术咨询有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application.
 */
package com.synet.tool.rsc.action;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;

import com.shrcn.found.common.event.EventConstants;
import com.shrcn.found.common.event.EventManager;
import com.shrcn.found.common.util.StringUtil;
import com.shrcn.found.file.excel.SheetsHandler;
import com.shrcn.found.ui.action.ConfigAction;
import com.shrcn.found.ui.util.DialogHelper;
import com.shrcn.tool.found.das.BeanDaoService;
import com.shrcn.tool.found.das.impl.BeanDaoImpl;
import com.synet.tool.rsc.excel.handler.RegionHandler;
import com.synet.tool.rsc.model.Tb1049RegionEntity;
import com.synet.tool.rsc.model.Tb1050CubicleEntity;
import com.synet.tool.rsc.util.ExcelReaderUtil;

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
		List<Tb1049RegionEntity> result = (List<Tb1049RegionEntity>) ExcelReaderUtil.parseByHandler(xlspath, handler);
		List<Tb1050CubicleEntity> cubicles = new ArrayList<>();
		for (Tb1049RegionEntity reg : result) {
			cubicles.addAll(reg.getTb1050CubiclesByF1049Code());
		}
		BeanDaoService beanDao = BeanDaoImpl.getInstance();
		beanDao.insertBatch(result);
		beanDao.insertBatch(cubicles);
		EventManager.getDefault().notify(EventConstants.PROJECT_RELOAD, null);
	}

}

