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
public class ImpBrkCfmEditor extends BaseConfigEditor {
	
	private ImprotInfoService improtInfoService;
	private Map<String, IM100FileInfoEntity> map;
	private org.eclipse.swt.widgets.List titleList;
	private Button btImport;
	
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
		btImport = SwtUtil.createPushButton(container, "导入跳合闸反校", btData);
		table =TableFactory.getBrkCfmTable(container);
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
						List<IM108BrkCfmEntity> list = improtInfoService.getBrkCfmEntityList(fileInfoEntity);
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
						}
						Tb1061PoutEntity poutEntity2 = poutEntityService.getPoutEntity(entity.getDevName(), entity.getCmdOutVpRefAddr());
						if (poutEntity2 != null) {
							circuitEntity.setTb1061PoutByF1061CodeConvChk2(poutEntity1);
							entity.setMatched(DBConstants.MATCHED_OK);
						}
						circuitEntityService.save(circuitEntity);
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
				
				List<IM108BrkCfmEntity> list = improtInfoService.getBrkCfmEntityList(map.get(items.get(0)));
				if (list != null && list.size()> 0) {
					//冲突检查
					checkData(list);
					table.setInput(list);
				}
			}
		}
	}

	private void checkData(List<IM108BrkCfmEntity> list) {
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
