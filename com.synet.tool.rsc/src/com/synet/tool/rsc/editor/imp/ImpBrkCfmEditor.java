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
import com.synet.tool.rsc.model.IM108BrkCfmEntity;
import com.synet.tool.rsc.model.Tb1061PoutEntity;
import com.synet.tool.rsc.model.Tb1062PinEntity;
import com.synet.tool.rsc.model.Tb1063CircuitEntity;
import com.synet.tool.rsc.service.CircuitEntityService;
import com.synet.tool.rsc.service.ImprotInfoService;
import com.synet.tool.rsc.service.PinEntityService;
import com.synet.tool.rsc.service.PoutEntityService;
import com.synet.tool.rsc.ui.TableFactory;

/**
 * 导入信息->跳合闸反校关联表 树菜单编辑器。
 * @author 陈春(mailto:chench80@126.com)
 * @version 1.0, 2013-4-3
 */
public class ImpBrkCfmEditor extends ExcelImportEditor {
	
	private PinEntityService pinEntityService;
	private PoutEntityService poutEntityService;
	private CircuitEntityService circuitEntityService;
	
	public ImpBrkCfmEditor(Composite container, IEditorInput input) {
		super(container, input);
	}
	
	@Override
	public void init() {
		improtInfoService = new ImprotInfoService();
		map = new HashMap<String, IM100FileInfoEntity>();
		pinEntityService = new PinEntityService();
		poutEntityService = new PoutEntityService();
		circuitEntityService = new CircuitEntityService();
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
		btImport = SwtUtil.createPushButton(btComp, "导入跳合闸反校", new GridData());
		
		table =TableFactory.getBrkCfmTable(container);
		table.getTable().setLayoutData(new GridData(GridData.FILL_BOTH));
	}
	
	protected void addListeners() {
		SwtUtil.addMenus(titleList, new DeleteFileAction(titleList, IM108BrkCfmEntity.class));
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
		List<IM108BrkCfmEntity> list = (List<IM108BrkCfmEntity>) table.getInput();
		if(list == null || list.size() <= 0) return;
		for (IM108BrkCfmEntity entity : list) {
			if (!entity.isOverwrite()){
				continue;
			}
			try {
				Tb1062PinEntity pinEntity = pinEntityService.getPinEntity(entity.getDevName(), entity.getPinRefAddr());
				if (pinEntity != null) {
					Tb1063CircuitEntity circuitEntity = circuitEntityService.getCircuitEntity(pinEntity);
					if (circuitEntity != null) {
						Tb1061PoutEntity poutEntity1 = poutEntityService.getPoutEntity(entity.getDevName(), entity.getCmdAckVpRefAddr());
						if (poutEntity1 != null) {
							circuitEntity.setTb1061PoutByF1061CodeConvChk1(poutEntity1);
							entity.setMatched(DBConstants.MATCHED_OK);
						} else {
							String msg = "命令确认虚端子不存在：" + entity.getDevName() + "->" + entity.getCmdAckVpRefAddr();
							appendError("导入跳合闸反校", "虚端子检查", msg);
						}
						Tb1061PoutEntity poutEntity2 = poutEntityService.getPoutEntity(entity.getDevName(), entity.getCmdOutVpRefAddr());
						if (poutEntity2 != null) {
							circuitEntity.setTb1061PoutByF1061CodeConvChk2(poutEntity2);
							entity.setMatched(DBConstants.MATCHED_OK);
						} else {
							String msg = "命令出口虚端子不存在：" + entity.getDevName() + "->" + entity.getCmdOutVpRefAddr();
							appendError("导入跳合闸反校", "虚端子检查", msg);
						}
						circuitEntityService.save(circuitEntity);
					} else {
						String msg = "开入虚端子虚回路不存在：" + entity.getDevName() + "->" + entity.getPinRefAddr();
						appendError("导入跳合闸反校", "虚回路检查", msg);
					}
				} else {
					String msg = "开入虚端子不存在：" + entity.getDevName() + "->" + entity.getPinRefAddr();
					appendError("导入跳合闸反校", "虚端子检查", msg);
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
		List<IM100FileInfoEntity> fileInfoEntities = improtInfoService.getFileInfoEntityList(DBConstants.FILE_TYPE108);
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
		List<IM108BrkCfmEntity> list = improtInfoService.getBrkCfmEntityList(map.get(filename));
		if (list != null) {
			table.setInput(list);
		}
	}

	@SuppressWarnings("unchecked")
	protected void checkData() {
		List<IM108BrkCfmEntity> list = (List<IM108BrkCfmEntity>) table.getInput();
		if (list == null || list.size() <= 0) return;
		try {
			for (IM108BrkCfmEntity entity : list) {
				if (entity.getMatched() == DBConstants.MATCHED_OK) {
					entity.setConflict(DBConstants.YES);
					entity.setOverwrite(false);
					continue;
				}
					Tb1062PinEntity pinEntity = pinEntityService.getPinEntity(entity.getDevName(), entity.getPinRefAddr());
					if (pinEntity != null) {
						Tb1063CircuitEntity circuitEntity = circuitEntityService.getCircuitEntity(pinEntity);
						if (circuitEntity != null) {
							if (circuitEntity.getTb1061PoutByF1061CodeConvChk1() == null 
									|| circuitEntity.getTb1061PoutByF1061CodeConvChk2() == null) {
								entity.setConflict(DBConstants.NO);
								entity.setOverwrite(true);
								continue;
							} 
						}
					}
				entity.setConflict(DBConstants.YES);
				entity.setOverwrite(false);
			}
		} catch (Exception e) {
			e.printStackTrace();
			console.append("数据异常");
		}
	}
}
