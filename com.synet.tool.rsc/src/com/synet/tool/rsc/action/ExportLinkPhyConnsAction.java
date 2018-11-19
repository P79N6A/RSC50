/**
 * Copyright (c) 2007-2017 泗业技术咨询有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application.
 */
package com.synet.tool.rsc.action;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.excelutils2007.ExcelUtils;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.operation.IRunnableWithProgress;

import com.shrcn.found.common.Constants;
import com.shrcn.found.common.util.StringUtil;
import com.shrcn.found.ui.action.ConfigAction;
import com.shrcn.found.ui.model.IField;
import com.shrcn.found.ui.util.ProgressManager;
import com.shrcn.found.ui.view.ConsoleManager;
import com.shrcn.tool.found.das.impl.BeanDaoImpl;
import com.shrcn.tool.found.das.impl.HqlDaoImpl;
import com.synet.tool.rsc.dialog.ExportIedDialog;
import com.synet.tool.rsc.dialog.OracleConnDialog;
import com.synet.tool.rsc.jdbc.ConnParam;
import com.synet.tool.rsc.jdbc.ExportDataHandler;
import com.synet.tool.rsc.model.IM110LinkWarnEntity;
import com.synet.tool.rsc.model.Tb1046IedEntity;
import com.synet.tool.rsc.model.Tb1053PhysconnEntity;
import com.synet.tool.rsc.model.Tb1065LogicallinkEntity;
import com.synet.tool.rsc.model.Tb1073LlinkphyrelationEntity;
import com.synet.tool.rsc.util.DateUtils;
import com.synet.tool.rsc.util.ExcelFileManager2007;

 /**
 * 
 * @author 陈春(mailto:chench80@126.com)
 * @version 1.0, 2018-8-7
 */
public class ExportLinkPhyConnsAction extends ConfigAction {
	
	public ExportLinkPhyConnsAction(String title) {
		super(title);
	}

	@Override
	public void run() {
		ExportIedDialog dialog = new ExportIedDialog(getShell());
		if (dialog.open() == IDialogConstants.OK_ID) {
			final String filePath = dialog.getFilePath();
			final List<Tb1046IedEntity> ieds = dialog.getIeds();
			if (filePath == null) 
				return;
			ProgressManager.execute(new IRunnableWithProgress() {
				@Override
				public void run(final IProgressMonitor monitor) throws InvocationTargetException,
				InterruptedException {
					if (ieds != null && ieds.size() > 0) {
						int totalWork = ieds.size() * 2 + 2;
						monitor.beginTask("正在导出", totalWork);
						long start = System.currentTimeMillis();
						List<Object> linkList = new ArrayList<>();
						for (Tb1046IedEntity ied : ieds) {
							if (monitor.isCanceled()) {
								break;
							}
							String iedName = ied.getF1046Name();
							monitor.setTaskName("正在导出装置[" + iedName + "]数据");
							//导出逻辑链路部分
							linkList.addAll(getLinkData(ied));
							monitor.worked(1);
						}
						String dateStr = DateUtils.getDateStr(new Date(), DateUtils.DATE_DAY_PATTERN_);
						//导出逻辑链路部分
						if (linkList.size() > 0) {
							ExcelUtils.addValue("linkList", linkList);
							String fileName = filePath + "/链路与物理回路依赖关系表" + dateStr + ".xlsx";
							ExcelFileManager2007.saveExcelFile(ExportLinkPhyConnsAction.class, "com/synet/tool/rsc/excel/linkphys.xlsx", fileName);
						}
						monitor.worked(1);
						long time = (System.currentTimeMillis() - start) / 1000;
						monitor.setTaskName("导出耗时：" + time + "秒");
						monitor.done();
					}
				}
			}, true);
		}
	}

	protected List<Object> getLinkData(Tb1046IedEntity ied) {
		List<Object> links = new ArrayList<>();
		String hql = "from " + Tb1065LogicallinkEntity.class.getName() +
				" where tb1046IedByF1046CodeIedRecv=:ied";
		Map<String, Object> params = new HashMap<>();
		params.put("ied", ied);
		HqlDaoImpl hqlDao = HqlDaoImpl.getInstance();
		int count = hqlDao.getCount(hql, params);
		int pageSize = 500;
		int pageTotal = count / pageSize;
		if (count % pageSize > 0) {
			pageTotal++;
		}
		for (int p = 1; p < pageTotal + 1; p++) {
			params.clear();
			params.put("ied", ied);
			List<?> list = hqlDao.getListByHqlAndPage(hql, params, p, pageSize);
			for (Object o : list) {
				Tb1065LogicallinkEntity link = (Tb1065LogicallinkEntity) o;
				hql = "from " + Tb1073LlinkphyrelationEntity.class.getName() +
						" where tb1065LogicallinkByF1065Code=:link";
				params.clear();
				params.put("link", link);
				List<Tb1073LlinkphyrelationEntity> relations = (List<Tb1073LlinkphyrelationEntity>) hqlDao.getListByHql(hql, params);
				List<Tb1053PhysconnEntity> phyconns = new ArrayList<>();
				for (Tb1073LlinkphyrelationEntity relation : relations) {
					Tb1053PhysconnEntity phycon = (Tb1053PhysconnEntity) BeanDaoImpl.getInstance()
							.getById(Tb1053PhysconnEntity.class, relation.getTb1053PhysconnByF1053Code().getF1053Code());
					phyconns.add(phycon);
//					phyconns.add(relation.getTb1053PhysconnByF1053Code());
				}
				link.setPhyConns(phyconns);
				links.add(link);
			}
		}
		return links;
	}

}

