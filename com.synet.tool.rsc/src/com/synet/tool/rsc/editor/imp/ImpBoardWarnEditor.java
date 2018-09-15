/**
 * Copyright (c) 2007-2013 上海思弘瑞电力控制技术有限公司. All rights reserved. 
 * This program is an eclipse Rich Client Application
 * designed for IED configuration and debuging.
 */
package com.synet.tool.rsc.editor.imp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

import com.shrcn.found.ui.editor.IEditorInput;
import com.shrcn.found.ui.util.DialogHelper;
import com.shrcn.found.ui.util.SwtUtil;
import com.synet.tool.rsc.DBConstants;
import com.synet.tool.rsc.editor.BaseConfigEditor;
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
public class ImpBoardWarnEditor extends BaseConfigEditor {
	
	private ImprotInfoService improtInfoService;
	private Map<String, IM100FileInfoEntity> map;
	private org.eclipse.swt.widgets.List titleList;
	private Button btImport;
	
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
		titleList.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String[] selects = titleList.getSelection();
				if (selects != null && selects.length > 0) {
					IM100FileInfoEntity fileInfoEntity = map.get(selects[0]);
					if (fileInfoEntity == null) {
						DialogHelper.showAsynError("文名错误！");
					} else {
						List<IM105BoardWarnEntity> list = improtInfoService.getBoardWarnEntityList(fileInfoEntity);
						if (list != null && list.size()> 0) {
							table.setInput(list);
						}
					}
					System.out.println(selects[0]);
				}
				super.widgetSelected(e);
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
					Tb1058MmsfcdaEntity tempMmsfcdaEntity = mmsfcdaService.getMmsfcdaByF1058RedAddr(entity.getAlarmRefAddr());
					if (tempMmsfcdaEntity != null) {
						String dataCode = tempMmsfcdaEntity.getDataCode();
						if (dataCode != null) {
							Tb1016StatedataEntity statedataEntity = (Tb1016StatedataEntity) statedataService.getById(Tb1016StatedataEntity.class, dataCode);
							if (statedataEntity != null) {
								statedataEntity.setParentCode(tempBoard.getF1047Code());
								statedataService.save(statedataEntity);
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
				
				List<IM105BoardWarnEntity> list = improtInfoService.getBoardWarnEntityList(map.get(items.get(0)));
				if (list != null && list.size()> 0) {
					//冲突检查
					checkData(list);
					table.setInput(list);
				}
			}
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
