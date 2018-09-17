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
import com.shrcn.found.ui.util.DialogHelper;
import com.shrcn.found.ui.util.SwtUtil;
import com.synet.tool.rsc.DBConstants;
import com.synet.tool.rsc.model.IM100FileInfoEntity;
import com.synet.tool.rsc.model.IM109StaInfoEntity;
import com.synet.tool.rsc.model.Tb1058MmsfcdaEntity;
import com.synet.tool.rsc.service.ImprotInfoService;
import com.synet.tool.rsc.service.MmsfcdaService;
import com.synet.tool.rsc.ui.TableFactory;

/**
 * 导入信息->监控信息点表 树菜单编辑器。
 * @author 陈春(mailto:chench80@126.com)
 * @version 1.0, 2013-4-3
 */
public class ImpStaInfoEditor extends ExcelImportEditor {
	
	private MmsfcdaService mmsfcdaService;
	
	public ImpStaInfoEditor(Composite container, IEditorInput input) {
		super(container, input);
	}
	
	@Override
	public void init() {
		improtInfoService = new ImprotInfoService();
		map = new HashMap<String, IM100FileInfoEntity>();
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
		btImport = SwtUtil.createPushButton(container, "导入监控信息", btData);
		table =TableFactory.getStaInfoTable(container);
		table.getTable().setLayoutData(new GridData(GridData.FILL_BOTH));
	}
	
	@Override
	protected void addListeners() {
		SwtUtil.addMenus(titleList, new DeleteFileAction(titleList, IM109StaInfoEntity.class));
		titleList.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String[] selects = titleList.getSelection();
				if (selects != null && selects.length > 0) {
					loadFileItems(selects[0]);
				}
			}
		});
		
		btImport.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				doImport();
				DialogHelper.showAsynInformation("导入成功！");
			}
		});
	}

	@SuppressWarnings("unchecked")
	protected void doImport() {
		List<IM109StaInfoEntity> list = (List<IM109StaInfoEntity>) table.getInput();
		if (list == null || list.size() <= 0)
			return;
		for (IM109StaInfoEntity entity : list) {
			if (!entity.isOverwrite())
				continue;
			Tb1058MmsfcdaEntity mmsfcdaEntity = mmsfcdaService.getMmsfcdaByF1058RedAddr(entity.getDevName(), entity.getMmsRefAddr());
			if (mmsfcdaEntity != null) {
				mmsfcdaEntity.setF1058Desc(entity.getMmsDesc());
			}
			improtInfoService.update(entity);
		}
	}

	@Override
	public void initData() {
		table.setInput(new ArrayList<>());
		List<IM100FileInfoEntity> fileInfoEntities = improtInfoService.getFileInfoEntityList(DBConstants.FILE_TYPE109);
		if (fileInfoEntities != null && fileInfoEntities.size() > 0) {
			List<String> items = new ArrayList<>();
			for (IM100FileInfoEntity fileInfoEntity : fileInfoEntities) {
				map.put(fileInfoEntity.getFileName(), fileInfoEntity);
				items.add(fileInfoEntity.getFileName());
			}
			if (items.size() > 0) {
				IEditorInput editinput = getInput();
				int sel = 0;
				Object data = editinput.getData();
				if (data != null && data instanceof String) {
					String filename = (String) data;
					sel = items.indexOf(filename);
				}
				titleList.setItems(items.toArray(new String[0]));
				titleList.setSelection(sel);
				loadFileItems(items.get(0));
			}
		}
	}
	
	private void loadFileItems(String filename) {
		List<IM109StaInfoEntity> list = improtInfoService.getStaInfoEntityList(map.get(filename));
		if (list != null) {
			checkData(list);
			table.setInput(list);
		}
	}

	private void checkData(List<IM109StaInfoEntity> list) {
		for (IM109StaInfoEntity entity : list) {
			if (entity.getMatched() == DBConstants.MATCHED_OK) {
				entity.setConflict(DBConstants.YES);
				entity.setOverwrite(false);
				continue;
			}
			try {
				Tb1058MmsfcdaEntity mmsfcdaEntity = mmsfcdaService.getMmsfcdaByF1058RedAddr(entity.getDevName(), entity.getMmsRefAddr());
				if (mmsfcdaEntity != null) {
					if (mmsfcdaEntity.getF1058Desc() == null || "".equals(mmsfcdaEntity.getF1058Desc())) {
						entity.setConflict(DBConstants.NO);
						entity.setOverwrite(true);
						continue;
					} 
				}
				entity.setConflict(DBConstants.YES);
				entity.setOverwrite(false);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
