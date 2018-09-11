/**
 * Copyright (c) 2007-2013 上海思弘瑞电力控制技术有限公司. All rights reserved. 
 * This program is an eclipse Rich Client Application
 * designed for IED configuration and debuging.
 */
package com.synet.tool.rsc.editor.imp;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

import com.shrcn.found.ui.editor.IEditorInput;
import com.shrcn.found.ui.util.SwtUtil;
import com.synet.tool.rsc.DBConstants;
import com.synet.tool.rsc.editor.BaseConfigEditor;
import com.synet.tool.rsc.model.IM100FileInfoEntity;
import com.synet.tool.rsc.model.IM101IEDListEntity;
import com.synet.tool.rsc.model.IM109StaInfoEntity;
import com.synet.tool.rsc.service.ImprotInfoService;
import com.synet.tool.rsc.ui.TableFactory;

/**
 * 导入信息->设备台账树菜单编辑器。
 * @author 陈春(mailto:chench80@126.com)
 * @version 1.0, 2013-4-3
 */
public class ImpIEDListEditor extends BaseConfigEditor {
	
	private Combo cmbDevType;
	private Text txtDevName;//装置中文名称
	private Button btnSearch;
	private ImprotInfoService improtInfoService;
	
	public ImpIEDListEditor(Composite container, IEditorInput input) {
		super(container, input);
	}
	
	@Override
	public void init() {
		improtInfoService = new ImprotInfoService();
		super.init();
	}

	@Override
	public void buildUI(Composite container) {
		super.buildUI(container);
		container.setLayout(SwtUtil.getGridLayout(5));
		GridData noneGridData = new GridData();
		noneGridData.widthHint = 20;
		cmbDevType = SwtUtil.createCombo(container, SwtUtil.bt_hd, true);
		cmbDevType.setItems(new String[]{DEV_TYPE_TITLE});
		cmbDevType.select(0);
		SwtUtil.createLabel(container, "", noneGridData);
		txtDevName = SwtUtil.createText(container, SwtUtil.bt_hd);
		txtDevName.setMessage("装置中文名称");
		SwtUtil.createLabel(container, "", noneGridData);
		btnSearch = SwtUtil.createButton(container, SwtUtil.bt_gd, SWT.BUTTON1, "查询");
		
		GridData tableGridData = new GridData(GridData.FILL_BOTH);
		tableGridData.horizontalSpan = 5;
		table =TableFactory.getIEDListTable(container);
		table.getTable().setLayoutData(tableGridData);
	}
	
	protected void addListeners() {
		btnSearch.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				search();
			}
		});
	}

	@Override
	public void initData() {
		List<IM100FileInfoEntity> fileInfoEntities = improtInfoService.getFileInfoEntityList(DBConstants.FILE_TYPE102);
		List<IM109StaInfoEntity> inputs = new ArrayList<>();
		if (fileInfoEntities != null && fileInfoEntities.size() > 0) {
			for (IM100FileInfoEntity fileInfoEntity : fileInfoEntities) { 
				List<IM109StaInfoEntity> list = improtInfoService.getStaInfoEntityList(fileInfoEntity);
				if (list != null && list.size()> 0) {
					inputs.addAll(list);
				}
			}
			if (inputs != null && inputs.size() > 0) {
				table.setInput(inputs);
			}
		}
	}
	
	//手动过滤
	private void search() {
		List<IM100FileInfoEntity> fileInfoEntities = improtInfoService.getFileInfoEntityList(DBConstants.FILE_TYPE102);
		List<IM101IEDListEntity> inputs = new ArrayList<>();
		if (fileInfoEntities != null && fileInfoEntities.size() > 0) {
			for (IM100FileInfoEntity fileInfoEntity : fileInfoEntities) { 
				List<IM101IEDListEntity> list = improtInfoService.getIEDListEntityList(fileInfoEntity);
				if (list != null && list.size()> 0) {
					inputs.addAll(list);
				}
			}
			if (inputs != null && inputs.size() > 0) {
				List<IM101IEDListEntity> temp = new ArrayList<>();
				String devType = cmbDevType.getText().trim();
				String devName = txtDevName.getText().trim();
				for (IM101IEDListEntity entity : inputs) {
					if (DEV_TYPE_TITLE.equals(devType)) {
						if ("".equals(devName)) {
							return;
						} else {
							if (devName.equals(entity.getDevDesc())) {
								temp.add(entity);
							}
						}
					} else {
						if (devType.equals(entity.getDevType())) {
							if ("".equals(devName)) {
								return;
							} else {
								if (devName.equals(entity.getDevDesc())) {
									temp.add(entity);
								}
							}
						}
					}
				}
				table.setInput(temp);
			}
		}
	}
}
