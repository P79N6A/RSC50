/**
 * Copyright (c) 2007-2013 上海思弘瑞电力控制技术有限公司. All rights reserved. 
 * This program is an eclipse Rich Client Application
 * designed for IED configuration and debuging.
 */
package com.synet.tool.rsc.editor.imp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;

import com.shrcn.found.common.util.StringUtil;
import com.shrcn.found.ui.editor.IEditorInput;
import com.shrcn.found.ui.util.SwtUtil;
import com.shrcn.found.ui.view.Problem;
import com.synet.tool.rsc.DBConstants;
import com.synet.tool.rsc.model.IM100FileInfoEntity;
import com.synet.tool.rsc.model.IM110LinkWarnEntity;
import com.synet.tool.rsc.model.Tb1058MmsfcdaEntity;
import com.synet.tool.rsc.model.Tb1065LogicallinkEntity;
import com.synet.tool.rsc.service.ImprotInfoService;
import com.synet.tool.rsc.service.LogicallinkEntityService;
import com.synet.tool.rsc.service.MmsfcdaService;
import com.synet.tool.rsc.ui.TableFactory;

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
	
	class EditorSelectListener extends SelectionAdapter {
		@Override
		public void widgetSelected(SelectionEvent e) {
			Object obj = e.getSource();
			if (obj == titleList) {
				String[] selects = titleList.getSelection();
				if (selects != null && selects.length > 0) {
					loadFileItems(selects[0]);
				}
			} else if (obj == btAdd) {
				addItemByTable(DBConstants.FILE_TYPE104);
			} else if (obj == btDelete) {
				deleteItemsByTable();
			} else if (obj == btExport) {
				exportExcel();
			} else if (obj == btCheck) {
				//冲突检查
				checkConflict();
			} else if (obj == btImport) {
				importData();
			} else if (obj == btExportCfgData) {
				exportProcessorData();
			}
			
		}
	}
	
	protected void addListeners() {
		SwtUtil.addMenus(titleList, new DeleteFileAction(titleList, IM110LinkWarnEntity.class));
		SelectionListener listener = new EditorSelectListener();
		titleList.addSelectionListener(listener);
		btAdd.addSelectionListener(listener);
		btDelete.addSelectionListener(listener);
		btExport.addSelectionListener(listener);
		btCheck.addSelectionListener(listener);
		btImport.addSelectionListener(listener);
		btExportCfgData.addSelectionListener(listener);
	}
	
	protected void exportExcel() {
		table.exportExcel2007();
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
				logicLink = logicallinkEntityService.getBySendIedAndRef(sendDevName, cbRef);
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
					String msg = "MMS不存在:" + devDesc + "-> " + mmsRefAddr;
					String ref =  devName + "-> " + mmsRefAddr;
					appendError(title, "FCDA检查", ref, msg);
				}
			} else {
				String msg = "MMS信号参引不能为空:装置[" + devDesc + "]";
				appendError(title, "FCDA检查", devName, msg);
			}
		}
	}

	@Override
	public void initData() {
		table.setInput(new ArrayList<>());
		List<IM100FileInfoEntity> fileInfoEntities = improtInfoService.getFileInfoEntityList(DBConstants.FILE_TYPE110);
		if (fileInfoEntities != null && fileInfoEntities.size() > 0) {
			List<String> items = new ArrayList<>();
			for (IM100FileInfoEntity fileInfoEntity : fileInfoEntities) {
				map.put(fileInfoEntity.getFileName(), fileInfoEntity);
				items.add(fileInfoEntity.getFileName());
			}
			if (items.size() > 0) {
				titleList.setItems(items.toArray(new String[0]));
				titleList.setSelection(0);
				loadFileItems(items.get(0));
			}
		}
	}
	
	private void loadFileItems(String filename) {
		List<IM110LinkWarnEntity> list = improtInfoService.getLinkWarnEntityList(map.get(filename));
		if (list != null && list.size()> 0) {
			table.setInput(list);
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
				Tb1065LogicallinkEntity logicLink = logicallinkEntityService.getBySendIedAndRef(entity.getSendDevName(), entity.getCbRef());
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
