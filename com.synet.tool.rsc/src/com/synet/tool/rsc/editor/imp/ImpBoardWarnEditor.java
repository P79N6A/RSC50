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
import com.synet.tool.rsc.model.IM105BoardWarnEntity;
import com.synet.tool.rsc.model.Tb1016StatedataEntity;
import com.synet.tool.rsc.model.Tb1047BoardEntity;
import com.synet.tool.rsc.model.Tb1058MmsfcdaEntity;
import com.synet.tool.rsc.service.BoardEntityService;
import com.synet.tool.rsc.service.ImprotInfoService;
import com.synet.tool.rsc.service.MmsfcdaService;
import com.synet.tool.rsc.service.StatedataService;
import com.synet.tool.rsc.ui.TableFactory;

/**
 * 导入信息->告警与板卡关联表 树菜单编辑器。
 * @author 陈春(mailto:chench80@126.com)
 * @version 1.0, 2013-4-3
 */
public class ImpBoardWarnEditor extends ExcelImportEditor {
	
	private BoardEntityService boardEntityService;
	private MmsfcdaService mmsfcdaService;
	private StatedataService statedataService;
	
	public ImpBoardWarnEditor(Composite container, IEditorInput input) {
		super(container, input);
	}
	
	@Override
	public void init() {
		improtInfoService = new ImprotInfoService();
		map = new HashMap<String, IM100FileInfoEntity>();
		boardEntityService = new BoardEntityService();
		mmsfcdaService = new MmsfcdaService();
		statedataService = new StatedataService();
		super.init();
	}

	@Override
	public void buildUI(Composite container) {
		container.setLayout(SwtUtil.getGridLayout(2));
		
		GridData gridData = new GridData(GridData.FILL_VERTICAL);
		gridData.widthHint = 150;
		gridData.verticalSpan = 2;
		titleList = SwtUtil.createList(container, gridData);
		GridData btData = new GridData();
		btData.horizontalAlignment = SWT.RIGHT;
		btImport = SwtUtil.createPushButton(container, "导入告警", btData);
		table =TableFactory.getBoardWarnTableTable(container);
		table.getTable().setLayoutData(new GridData(GridData.FILL_BOTH));
	}
	
	protected void addListeners() {
		SwtUtil.addMenus(titleList, new DeleteFileAction(titleList, IM105BoardWarnEntity.class));
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
		List<IM105BoardWarnEntity> list = (List<IM105BoardWarnEntity>) table.getInput();
		for (IM105BoardWarnEntity entity : list) {
			if (!entity.isOverwrite()) {
				continue;
			}
			try {
				Tb1047BoardEntity tempBoard = boardEntityService.existsEntity(entity.getDevName(), entity.getBoardCode());
				if (tempBoard != null) {
					Tb1058MmsfcdaEntity tempMmsfcdaEntity = mmsfcdaService.getMmsfcdaByF1058RedAddr(entity.getDevName(),entity.getAlarmRefAddr());
					if (tempMmsfcdaEntity != null) {
						String dataCode = tempMmsfcdaEntity.getDataCode();
						if (dataCode != null) {
							Tb1016StatedataEntity statedataEntity = (Tb1016StatedataEntity) statedataService.getById(Tb1016StatedataEntity.class, dataCode);
							if (statedataEntity != null) {
								statedataEntity.setParentCode(tempBoard.getF1047Code());
								tempMmsfcdaEntity.setParentCode(tempBoard.getF1047Code());
								statedataService.update(statedataEntity);
								mmsfcdaService.update(tempMmsfcdaEntity);
								entity.setMatched(DBConstants.MATCHED_OK);
							}
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			improtInfoService.update(entity);
		}
	}

	@Override
	public void initData() {
		table.setInput(new ArrayList<>());
		List<IM100FileInfoEntity> fileInfoEntities = improtInfoService.getFileInfoEntityList(DBConstants.FILE_TYPE105);
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
		List<IM105BoardWarnEntity> list = improtInfoService.getBoardWarnEntityList(map.get(filename));
		if (list != null) {
			//冲突检查
			checkData(list);
			table.setInput(list);
		}
	}

	private void checkData(List<IM105BoardWarnEntity> list) {
		for (IM105BoardWarnEntity entity : list) {
			if (entity.getMatched() == DBConstants.MATCHED_OK) {
				entity.setConflict(DBConstants.YES);
				entity.setOverwrite(false);
				continue;
			}
			Tb1047BoardEntity tempBoard = boardEntityService.existsEntity(entity.getDevName(), entity.getBoardCode());
			if (tempBoard != null) {
				entity.setConflict(DBConstants.YES);
				entity.setOverwrite(false);
				continue;
			} else {
				Tb1058MmsfcdaEntity tempMmsfcdaEntity = mmsfcdaService.getMmsfcdaByF1058RedAddr(entity.getAlarmRefAddr());
				if (tempMmsfcdaEntity != null) {
					String dataCode = tempMmsfcdaEntity.getDataCode();
					if (dataCode != null) {
						Tb1016StatedataEntity statedataEntity = (Tb1016StatedataEntity) statedataService.getById(Tb1016StatedataEntity.class, dataCode);
						if (statedataEntity != null && statedataEntity.getParentCode() == null) {
							entity.setConflict(DBConstants.NO);
							entity.setOverwrite(true);
							continue;
						} 
					}
				}
				entity.setConflict(DBConstants.YES);
				entity.setOverwrite(false);
			}
		}
	}
}
