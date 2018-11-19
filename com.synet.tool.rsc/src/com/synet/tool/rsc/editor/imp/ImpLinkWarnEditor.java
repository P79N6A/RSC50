/**
 * Copyright (c) 2007-2013 上海思弘瑞电力控制技术有限公司. All rights reserved. 
 * This program is an eclipse Rich Client Application
 * designed for IED configuration and debuging.
 */
package com.synet.tool.rsc.editor.imp;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;

import com.shrcn.found.common.dict.DictManager;
import com.shrcn.found.common.util.StringUtil;
import com.shrcn.found.ui.editor.IEditorInput;
import com.shrcn.found.ui.model.IField;
import com.shrcn.found.ui.util.ProgressManager;
import com.shrcn.found.ui.util.SwtUtil;
import com.shrcn.found.ui.view.ConsoleManager;
import com.shrcn.found.ui.view.Problem;
import com.shrcn.tool.found.das.impl.HqlDaoImpl;
import com.synet.tool.rsc.DBConstants;
import com.synet.tool.rsc.dialog.ExportIedDialog;
import com.synet.tool.rsc.model.IM100FileInfoEntity;
import com.synet.tool.rsc.model.IM110LinkWarnEntity;
import com.synet.tool.rsc.model.Tb1016StatedataEntity;
import com.synet.tool.rsc.model.Tb1046IedEntity;
import com.synet.tool.rsc.model.Tb1058MmsfcdaEntity;
import com.synet.tool.rsc.model.Tb1061PoutEntity;
import com.synet.tool.rsc.model.Tb1065LogicallinkEntity;
import com.synet.tool.rsc.service.ImprotInfoService;
import com.synet.tool.rsc.service.LogicallinkEntityService;
import com.synet.tool.rsc.service.MmsfcdaService;
import com.synet.tool.rsc.service.PoutEntityService;
import com.synet.tool.rsc.ui.TableFactory;
import com.synet.tool.rsc.util.DateUtils;

/**
 * 导入信息->告警与链路关联表 树菜单编辑器。
 * @author 陈春(mailto:chench80@126.com)
 * @version 1.0, 2013-4-3
 */
public class ImpLinkWarnEditor extends ExcelImportEditor {

	private String title = "导入开入信号";
	private MmsfcdaService mmsfcdaService;
	private LogicallinkEntityService logicallinkEntityService;
	
	public ImpLinkWarnEditor(Composite container, IEditorInput input) {
		super(container, input);
	}
	
	@Override
	public void init() {
		super.init();
		improtInfoService = new ImprotInfoService();
		map = new HashMap<String, IM100FileInfoEntity>();
		mmsfcdaService = new MmsfcdaService();
		logicallinkEntityService = new LogicallinkEntityService();
	}

	@Override
	public void buildUI(Composite container) {
		super.buildUI(container);
		container.setLayout(SwtUtil.getGridLayout(2));
		GridData gridData = new GridData(GridData.FILL_VERTICAL);
		gridData.widthHint = 150;
		gridData.verticalSpan = 2;
		titleList = SwtUtil.createList(container, gridData);
		GridData btData = new GridData();
		btData.horizontalAlignment = SWT.RIGHT;
		Composite btComp = SwtUtil.createComposite(container, btData, 6);
		btAdd = SwtUtil.createPushButton(btComp, "添加", new GridData());
		btDelete = SwtUtil.createPushButton(btComp, "删除", new GridData());
		btExport = SwtUtil.createPushButton(btComp, "导出原始数据", new GridData());
		btExportCfgData  = SwtUtil.createPushButton(btComp, "导出配置数据", new GridData());
		btCheck = SwtUtil.createPushButton(btComp, "冲突检查", new GridData());
		btImport = SwtUtil.createPushButton(btComp, "导入链路告警", new GridData());
		table = TableFactory.getLinkWarnTable(container);
		table.getTable().setLayoutData(new GridData(GridData.FILL_BOTH));
	}
	
	protected void exportExcel() {
		ExportIedDialog dialog = new ExportIedDialog(getShell());
		if (dialog.open() == IDialogConstants.OK_ID) {
			final String filePath = dialog.getFilePath();
			final List<Tb1046IedEntity> ieds = dialog.getIeds();
			if (filePath == null) return;
			ProgressManager.execute(new IRunnableWithProgress() {
				
				@Override
				public void run(final IProgressMonitor monitor) throws InvocationTargetException,
				InterruptedException {
					IField[] vfields = getExportFields();
					if (ieds != null && ieds.size() > 0) {
						int totalWork = ieds.size() * 2 + 2;
						monitor.beginTask("正在导出", totalWork);
						long start = System.currentTimeMillis();
						ConsoleManager console = ConsoleManager.getInstance();
						List<Object> linkList = new ArrayList<>();
						List<Object> mmsList = new ArrayList<>();
						for (Tb1046IedEntity ied : ieds) {
							if (monitor.isCanceled()) {
								break;
							}
							String iedName = ied.getF1046Name();
							monitor.setTaskName("正在导出装置[" + iedName + "]数据");
							//导出逻辑链路部分
							linkList.addAll(getLinkData(ied));
							monitor.worked(1);
							//导出告警信号部分
							mmsList.addAll(getWarnData(ied));
							monitor.worked(1);
						}
						String dateStr = DateUtils.getDateStr(new Date(), DateUtils.DATE_DAY_PATTERN_);
						//导出逻辑链路部分
						if (linkList.size() > 0) {
							String fileName = filePath + "/告警与链路关联表(逻辑链路部分)" + dateStr + ".xlsx";
							exportTemplateExcel(fileName, "逻辑链路信息表", vfields, linkList);
						} else {
							console.append("所选装置模型中不存在逻辑链路信息！");
						}
						monitor.worked(1);
						//导出告警信号部分
						if (mmsList.size() > 0) {
							String fileName = filePath + "/告警与链路关联表(告警信号部分)" + dateStr + ".xlsx";
							exportTemplateExcel(fileName, "链路告警信号表", vfields, mmsList);
						} else {
							console.append("所选装置模型中不存在链路告警信号！");
						}
						monitor.worked(1);
						long time = (System.currentTimeMillis() - start) / 1000;
						console.append("导出耗时：" + time + "秒");
						monitor.done();
					}
				}
			}, true);
		}
	}
	
	/**
	 * 102,103
	 * @param ied
	 * @return
	 */
	protected List<Object> getWarnData(Tb1046IedEntity ied) {
		List<Object> warns = new ArrayList<>();
		HqlDaoImpl hqlDao = HqlDaoImpl.getInstance();
		String hql = "from " + Tb1016StatedataEntity.class.getName() + 
				" where f1011No in (102, 103) and tb1046IedByF1046Code=:ied";
		Map<String, Object> params = new HashMap<>();
		params.put("ied", ied);
		int count = hqlDao.getCount(hql, params);
		int pageSize = 500;
		int pageTotal = count / pageSize;
		if (count % pageSize > 0) {
			pageTotal++;
		}
		DictManager dict = DictManager.getInstance();
		for (int p = 1; p < pageTotal + 1; p++) {
			List<?> list = hqlDao.getListByHqlAndPage(hql, params, p, pageSize);
			for (Object o : list) {
				Tb1016StatedataEntity st = (Tb1016StatedataEntity) o;
				IM110LinkWarnEntity warn = new IM110LinkWarnEntity();
				warn.setDevName(ied.getF1046Name());
				warn.setDevDesc(ied.getF1046Desc());
				warn.setMmsDesc(st.getF1016Desc());
				warn.setMmsRefAddr(st.getF1016AddRef());
				warn.setF1011No(dict.getNameById("F1011_NO", st.getF1011No()));
				warns.add(warn);
			}
		}
		return warns;
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
			List<?> list = hqlDao.getListByHqlAndPage(hql, params, p, pageSize);
			for (Object o : list) {
				Tb1065LogicallinkEntity link = (Tb1065LogicallinkEntity) o;
				IM110LinkWarnEntity warn = new IM110LinkWarnEntity();
				warn.setDevName(ied.getF1046Name());
				warn.setDevDesc(ied.getF1046Desc());
				warn.setSendDevName(link.getTb1046IedByF1046CodeIedSend().getF1046Name());
				warn.setCbRef(link.getBaseCbByCdCode().getCbId());
				links.add(warn);
			}
		}
		return links;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void doImport(IProgressMonitor monitor) {
		List<IM110LinkWarnEntity> list = (List<IM110LinkWarnEntity>) table.getInput();
		if (list == null || list.size() <= 0)
			return;
		for (IM110LinkWarnEntity entity : list) {
			String devName = entity.getDevName();
			if (devName == null || !entity.isOverwrite()) {
				continue;
			}
			String sendDevName = entity.getSendDevName();
			String cbRef = entity.getCbRef();
			String devDesc = entity.getDevDesc();
			devDesc = devName + ":" + devDesc;
			Tb1065LogicallinkEntity logicLink = null;
			if (!StringUtil.isEmpty(cbRef)) {
				logicLink = logicallinkEntityService.getBySendIedAndRef(devName, sendDevName, cbRef);
				if (logicLink == null) {
					String msg = "逻辑链路不存在:" + sendDevName + "-> " + cbRef;
					String ref = sendDevName + "->" + cbRef;
					appendError(title, "逻辑链路检查", ref, msg);
					continue;
				}
			} else {
				String msg = "逻辑链路参引不能为空:装置[" + sendDevName + "]";
				appendError(title, "逻辑链路检查", sendDevName, msg);
				continue;
			}
			String mmsRefAddr = entity.getMmsRefAddr();
			if (!StringUtil.isEmpty(mmsRefAddr)) {
				Tb1058MmsfcdaEntity mmsfcdaEntity = mmsfcdaService.getMmsfcdaByF1058RedAddr(devName, mmsRefAddr);
				if (mmsfcdaEntity != null) {
					mmsfcdaService.updateWarnParent(mmsfcdaEntity, logicLink.getF1065Code());
					entity.setMatched(DBConstants.MATCHED_OK);
				} else {
					PoutEntityService poutEntityService = new PoutEntityService();
					Tb1061PoutEntity poutEntity = poutEntityService.getPoutEntity(devName, mmsRefAddr);
					if (poutEntity != null) {
						mmsfcdaService.updateWarnParent(poutEntity, logicLink.getF1065Code());
						entity.setMatched(DBConstants.MATCHED_OK);
					} else {
						String msg = "告警信号不存在:" + devDesc + "-> " + mmsRefAddr;
						String ref =  devName + "-> " + mmsRefAddr;
						appendError(title, "FCDA检查", ref, msg);
					}
				}
			} else {
				String msg = "MMS信号参引不能为空:装置[" + devDesc + "]";
				appendError(title, "FCDA检查", devName, msg);
			}
		}
	}

	@SuppressWarnings("unchecked")
	protected void checkData() {
		List<IM110LinkWarnEntity> list = (List<IM110LinkWarnEntity>) table.getInput();
		if (list == null || list.size() <= 0) return;
		for (IM110LinkWarnEntity entity : list) {
			if (entity.getMatched() == DBConstants.MATCHED_OK) {
				entity.setConflict(DBConstants.YES);
				entity.setOverwrite(false);
				continue;
			}
			try {
				Tb1065LogicallinkEntity logicLink = logicallinkEntityService.getBySendIedAndRef(entity.getDevName(), entity.getSendDevName(), entity.getCbRef());
				if (logicLink == null) {
					entity.setConflict(DBConstants.YES);
					entity.setOverwrite(false);
					continue;
				}
				Tb1058MmsfcdaEntity mmsfcdaEntity = mmsfcdaService.getMmsfcdaByF1058RedAddr(entity.getDevName(), entity.getMmsRefAddr());
				if (mmsfcdaEntity == null) {
					entity.setConflict(DBConstants.YES);
					entity.setOverwrite(false);
					continue;
				}
				entity.setConflict(DBConstants.NO);
				entity.setOverwrite(true);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	protected Object locate(Problem problem) {
		List<IM110LinkWarnEntity> list = (List<IM110LinkWarnEntity>) table.getInput();
		if (problem != null && list != null && list.size() > 0) {
			String ptitle = problem.getIedName();
			if (title.equals(ptitle)) {
				String ref = problem.getRef();
				if (ref != null) {
					if (ref.contains("->")) {
						String[] refs = ref.split("->");
						if (refs.length == 2) {
							String devName = refs[0];
							String refStr = refs[1];
							for (IM110LinkWarnEntity entity : list) {
								if ((devName.equals(entity.getDevName()) && refStr.equals(entity.getMmsRefAddr()) 
										|| (devName.equals(entity.getSendDevName()) && refStr.equals(entity.getCbRef())))) {
									return entity;
								}
							}
						}
					} else {
						for (IM110LinkWarnEntity entity : list) {
							if (ref.equals(entity.getDevName())) {
								return entity;
							}
						}
					}
				}
			}
		}
		return null;
	}

}
