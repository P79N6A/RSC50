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
import org.eclipse.swt.widgets.Composite;

import com.shrcn.found.ui.editor.IEditorInput;
import com.shrcn.found.ui.util.DialogHelper;
import com.shrcn.found.ui.util.SwtUtil;
import com.shrcn.tool.found.das.impl.HqlDaoImpl;
import com.synet.tool.rsc.DBConstants;
import com.synet.tool.rsc.model.IM100FileInfoEntity;
import com.synet.tool.rsc.model.IM106PortLightEntity;
import com.synet.tool.rsc.model.Tb1006AnalogdataEntity;
import com.synet.tool.rsc.model.Tb1048PortEntity;
import com.synet.tool.rsc.model.Tb1058MmsfcdaEntity;
import com.synet.tool.rsc.service.AnalogdataService;
import com.synet.tool.rsc.service.ImprotInfoService;
import com.synet.tool.rsc.service.MmsfcdaService;
import com.synet.tool.rsc.service.PortEntityService;
import com.synet.tool.rsc.ui.TableFactory;

/**
 * 导入信息->光强与端口关联表 树菜单编辑器。
 * @author 陈春(mailto:chench80@126.com)
 * @version 1.0, 2013-4-3
 */
public class ImpPortLightEditor extends ExcelImportEditor {
	
	private PortEntityService portEntityService;
	private MmsfcdaService mmsfcdaService;
	private AnalogdataService analogdataService;
	
	public ImpPortLightEditor(Composite container, IEditorInput input) {
		super(container, input);
	}
	
	@Override
	public void init() {
		improtInfoService = new ImprotInfoService();
		map = new HashMap<String, IM100FileInfoEntity>();
		portEntityService = new PortEntityService();
		mmsfcdaService = new MmsfcdaService();
		analogdataService = new AnalogdataService();
		super.init();
	}

	@Override
	public void buildUI(Composite container) {
		super.buildUI(container);
		container.setLayout(SwtUtil.getGridLayout(2));
		
		GridData gridData = new GridData(GridData.FILL_VERTICAL);
		gridData.widthHint = 150;
		gridData.verticalSpan = 3;
		titleList = SwtUtil.createList(container, gridData);
		GridData btData = new GridData();
		btData.horizontalAlignment = SWT.RIGHT;
		btImport = SwtUtil.createPushButton(container, "导入光强与端口", btData);
		table =TableFactory.getPortLightTable(container);
		table.getTable().setLayoutData(new GridData(GridData.FILL_BOTH));
	}
	
	protected void addListeners() {
		SwtUtil.addMenus(titleList, new DeleteFileAction(titleList, IM106PortLightEntity.class));
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
		List<IM106PortLightEntity> list = (List<IM106PortLightEntity>) table.getInput();
		if (list == null || list.size() <= 0)
			return;
		try {
			HqlDaoImpl hqlDao = HqlDaoImpl.getInstance();
			Map<String, Object> params = new HashMap<>();
			for (IM106PortLightEntity entity : list) {
				if (!entity.isOverwrite()) {
					continue;
				}
				Tb1048PortEntity portEntity = portEntityService.getPortEntity(entity.getDevName(), 
						entity.getBoardCode(), entity.getPortCode());
				if (portEntity != null) {
					params.clear();
					params.put("f1046Name", entity.getDevName());
					params.put("f1058RefAddr", entity.getOpticalRefAddr());
					String hql = "from " + Tb1058MmsfcdaEntity.class.getName() + " where tb1046IedByF1046Code.f1046Name=:f1046Name and f1058RefAddr=:f1058RefAddr";
					List<?> mmsList = hqlDao.getListByHql(hql, params);
					if (mmsList!=null && mmsList.size()>0) {
						Tb1058MmsfcdaEntity mmsfcdaEntity = (Tb1058MmsfcdaEntity) mmsList.get(0);
						if (mmsfcdaEntity != null) {
							Tb1006AnalogdataEntity analogdataEntity = (Tb1006AnalogdataEntity) 
									analogdataService.getById(Tb1006AnalogdataEntity.class, mmsfcdaEntity.getDataCode());
							if (analogdataEntity != null) {
								analogdataEntity.setParentCode(portEntity.getF1048Code());
								analogdataService.update(analogdataEntity);
							}
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void initData() {
		table.setInput(new ArrayList<>());
		List<IM100FileInfoEntity> fileInfoEntities = improtInfoService.getFileInfoEntityList(DBConstants.FILE_TYPE106);
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
		List<IM106PortLightEntity> list = improtInfoService.getPortLightEntityList(map.get(filename));
		if (list != null && list.size()> 0) {
			checkData(list);
			table.setInput(list);
		}
	}

	private void checkData(List<IM106PortLightEntity> list) {
		for (IM106PortLightEntity entity : list) {
			if (entity.getMatched() == DBConstants.MATCHED_OK) {
				entity.setConflict(DBConstants.YES);
				entity.setOverwrite(false);
				continue;
			}
			try {
				Tb1048PortEntity portEntity = portEntityService.getPortEntity(entity.getDevName(), 
						entity.getBoardCode(), entity.getPortCode());
				if (portEntity != null) {
					Tb1058MmsfcdaEntity mmsfcdaEntity = mmsfcdaService.getMmsfcdaByF1058RedAddr(entity.getOpticalRefAddr());
					if (mmsfcdaEntity != null) {
						Tb1006AnalogdataEntity analogdataEntity = (Tb1006AnalogdataEntity) analogdataService.getById(Tb1006AnalogdataEntity.class,
								mmsfcdaEntity.getDataCode());
						if (analogdataEntity != null && analogdataEntity.getParentCode() == null) {
							entity.setConflict(DBConstants.NO);
							entity.setOverwrite(true);
							continue;
						} 
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
