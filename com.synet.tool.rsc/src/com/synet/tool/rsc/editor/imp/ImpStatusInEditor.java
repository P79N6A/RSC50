/**
 * Copyright (c) 2007-2013 上海思弘瑞电力控制技术有限公司. All rights reserved. 
 * This program is an eclipse Rich Client Application
 * designed for IED configuration and debuging.
 */
package com.synet.tool.rsc.editor.imp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;

import com.shrcn.found.ui.editor.IEditorInput;
import com.shrcn.found.ui.util.SwtUtil;
import com.synet.tool.rsc.DBConstants;
import com.synet.tool.rsc.model.IM100FileInfoEntity;
import com.synet.tool.rsc.model.IM104StatusInEntity;
import com.synet.tool.rsc.model.Tb1058MmsfcdaEntity;
import com.synet.tool.rsc.model.Tb1062PinEntity;
import com.synet.tool.rsc.service.ImprotInfoService;
import com.synet.tool.rsc.service.MmsfcdaService;
import com.synet.tool.rsc.service.PinEntityService;
import com.synet.tool.rsc.ui.TableFactory;

/**
 * 导入信息->开入信号映射表 树菜单编辑器。
 * @author 陈春(mailto:chench80@126.com)
 * @version 1.0, 2013-4-3
 */
public class ImpStatusInEditor extends ExcelImportEditor {
	
	private PinEntityService pinEntityService;
	private MmsfcdaService mmsfcdaService;
	
	public ImpStatusInEditor(Composite container, IEditorInput input) {
		super(container, input);
	}
	
	@Override
	public void init() {
		improtInfoService = new ImprotInfoService();
		map = new HashMap<String, IM100FileInfoEntity>();
		pinEntityService = new PinEntityService();
		mmsfcdaService = new MmsfcdaService();
		super.init();
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
		Composite btComp = SwtUtil.createComposite(container, btData, 2);
		btCheck = SwtUtil.createPushButton(btComp, "冲突检查", new GridData());
		btImport = SwtUtil.createPushButton(btComp, "导入开入信号", new GridData());
		table =TableFactory.getStatusInTable(container);
		table.getTable().setLayoutData(new GridData(GridData.FILL_BOTH));
	}
	
	protected void addListeners() {
		SwtUtil.addMenus(titleList, new DeleteFileAction(titleList, IM104StatusInEntity.class));
		titleList.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String[] selects = titleList.getSelection();
				if (selects != null && selects.length > 0) {
					loadFileItems(selects[0]);
				}
			}
		});
		
		btCheck.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				//冲突检查
				checkConflict();
			}
		});
		
		btImport.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				importData();
			}
		});
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void doImport() {
		List<IM104StatusInEntity> list = (List<IM104StatusInEntity>) table.getInput();
		if (list == null || list.size() <= 0)
			return;
		for (IM104StatusInEntity entity : list) {
			if (entity.getDevName() == null) continue;
			if (entity.getPinRefAddr() != null) {
				Tb1062PinEntity pinEntity = pinEntityService.getPinEntity(entity.getDevName(), entity.getPinRefAddr());
				if (pinEntity != null) {
					pinEntity.setF1062Desc(entity.getPinDesc());
					entity.setMatched(DBConstants.MATCHED_OK);
					pinEntityService.update(pinEntity);
				} else {
					String msg = "开入虚端子不存在:" + entity.getDevDesc() + "-> " + entity.getPinRefAddr();
					appendError("导入开入信号", "虚端子检查", msg);
				}
			} else {
				String msg = "开入虚端子参引不能为空:装置[" + entity.getDevDesc()+ "]";
				appendError("导入开入信号", "虚端子检查", msg);
			}
			
			if (entity.getMmsRefAddr() == null) {
				Tb1058MmsfcdaEntity mmsfcdaEntity = mmsfcdaService.getMmsfcdaByF1058RedAddr(entity.getDevName(), entity.getMmsRefAddr());
				if (mmsfcdaEntity != null) {
					mmsfcdaEntity.setF1058Desc(entity.getMmsDesc());
					entity.setMatched(DBConstants.MATCHED_OK);
					mmsfcdaService.update(mmsfcdaEntity);
				} else {
					String msg = "MMS不存在:" + entity.getDevDesc() + "-> " + entity.getMmsRefAddr ();
					appendError("导入开入信号", "FCDA检查", msg);
				}
			} else {
				String msg = "MMS信号参引不能为空:装置[" + entity.getDevDesc() + "]";
				appendError("导入开入信号", "FCDA检查", msg);
			}
			improtInfoService.update(entity);
		}
	}

	@Override
	public void initData() {
		table.setInput(new ArrayList<>());
		List<IM100FileInfoEntity> fileInfoEntities = improtInfoService.getFileInfoEntityList(DBConstants.FILE_TYPE104);
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
		List<IM104StatusInEntity> list = improtInfoService.getStatusInEntityList(map.get(filename));
		if (list != null && list.size()> 0) {
			table.setInput(list);
		}
	}

	@SuppressWarnings("unchecked")
	protected void checkData() {
		List<IM104StatusInEntity> list = (List<IM104StatusInEntity>) table.getInput();
		if (list == null || list.size() <= 0) return;
		for (IM104StatusInEntity entity : list) {
			if (entity.getMatched() == DBConstants.MATCHED_OK) {
				entity.setConflict(DBConstants.YES);
				entity.setOverwrite(false);
				continue;
			}
			try {
				Tb1062PinEntity pinEntity = pinEntityService.getPinEntity(entity.getDevName(), entity.getPinRefAddr());
				if (pinEntity == null || pinEntity.getF1062Desc() != null) {
					entity.setConflict(DBConstants.YES);
					entity.setOverwrite(false);
					continue;
				}
				Tb1058MmsfcdaEntity mmsfcdaEntity = mmsfcdaService.getMmsfcdaByF1058RedAddr(entity.getDevName(), entity.getMmsRefAddr());
				if (mmsfcdaEntity == null || mmsfcdaEntity.getF1058Desc() != null) {
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

}
