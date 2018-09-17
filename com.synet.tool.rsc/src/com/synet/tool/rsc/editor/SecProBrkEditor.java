/**
 * Copyright (c) 2007-2013 上海思弘瑞电力控制技术有限公司. All rights reserved. 
 * This program is an eclipse Rich Client Application
 * designed for IED configuration and debuging.
 */
package com.synet.tool.rsc.editor;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;

import com.shrcn.found.ui.editor.IEditorInput;
import com.shrcn.found.ui.util.DialogHelper;
import com.shrcn.found.ui.util.SwtUtil;
import com.synet.tool.rsc.RSCConstants;
import com.synet.tool.rsc.model.Tb1046IedEntity;
import com.synet.tool.rsc.model.Tb1093VoltagekkEntity;
import com.synet.tool.rsc.service.SecProBrkService;
import com.synet.tool.rsc.ui.TableFactory;
import com.synet.tool.rsc.util.RscObjectUtils;

/**
 * 安措->保护电压回路空开树菜单编辑器。
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2013-4-3
 */
public class SecProBrkEditor extends BaseConfigEditor {
	
	private Combo cmbDevType;
	private Combo cmbDevName;
	private Button btnSearch;
	private Button btnImport;
	private Button btnExport;
	private Button btnAdd;
	private Button btnDelete;
	private SecProBrkService secProBrkService;
	
	public SecProBrkEditor(Composite container, IEditorInput input) {
		super(container, input);
	}
	
	@Override
	public void init() {
		secProBrkService = new SecProBrkService();
		super.init();
	}

	@Override
	public void buildUI(Composite container) {
		super.buildUI(container);
		container.setLayout(SwtUtil.getGridLayout(1));
		Composite topComp = SwtUtil.createComposite(container, new GridData(GridData.FILL_HORIZONTAL), 8);
		cmbDevType = SwtUtil.createCombo(topComp, SwtUtil.bt_hd,true);
		cmbDevName = SwtUtil.createCombo(topComp, SwtUtil.bt_hd,true);
		btnSearch = SwtUtil.createButton(topComp, SwtUtil.bt_gd, SWT.BUTTON1, RSCConstants.SEARCH);
		SwtUtil.createLabel(topComp, "", SwtUtil.bt_hd); 
		btnImport = SwtUtil.createButton(topComp, SwtUtil.bt_gd, SWT.BUTTON1, "导入");
		btnExport = SwtUtil.createButton(topComp, SwtUtil.bt_gd, SWT.BUTTON1, "导出");
		btnAdd = SwtUtil.createButton(topComp, SwtUtil.bt_gd, SWT.BUTTON1, "添加");
		btnDelete = SwtUtil.createButton(topComp, SwtUtil.bt_gd, SWT.BUTTON1, "删除");
		
		Composite centerComp = SwtUtil.createComposite(container, new GridData(GridData.FILL_BOTH), 1);
		table = TableFactory.getVoltageKKTable(centerComp);
		table.getTable().setLayoutData(new GridData(GridData.FILL_BOTH));
	}
	
	protected void addListeners() {
		
		SelectionAdapter listener = new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Object evnet = e.getSource();
				if (evnet == btnSearch) {
					search();
				} else if (evnet == btnImport) {
					importData();
				} else if (evnet == btnExport) {
					exportData();
				} else if (evnet == btnAdd) {
					add();
				} else if (evnet == btnDelete) {
					delete();
				}
				super.widgetSelected(e);
			}
		};
		btnSearch.addSelectionListener(listener);
		btnImport.addSelectionListener(listener);
		btnExport.addSelectionListener(listener);
		btnAdd.addSelectionListener(listener);
		btnDelete.addSelectionListener(listener);
	}

	@Override
	public void initData() {
		List<Tb1093VoltagekkEntity> list = secProBrkService.getProBrkList();
		if (list != null){
			table.setInput(list);
		}
		List<Tb1046IedEntity> ieds = secProBrkService.getIedList();
		List<String> typeItems = new ArrayList<>();
		List<String> nameItems = new ArrayList<>();
		typeItems.add(DEV_TYPE_TITLE);
		nameItems.add(DEV_NAME_TITLE);
		if (ieds != null && !ieds.isEmpty()) {
			for (Tb1046IedEntity ied : ieds) {
				typeItems.add(ied.getF1046Model());
				nameItems.add(ied.getF1046Name());
			}
		}
		cmbDevType.setItems(typeItems.toArray(new String[0]));
		cmbDevName.setItems(nameItems.toArray(new String[0]));
		cmbDevType.select(0);
		cmbDevName.select(0);
		super.initData();
	}
	
	private void search() {
		String f1046Model = cmbDevType.getText().trim();
		String f1046Name = cmbDevName.getText().trim();
		if (DEV_TYPE_TITLE.equals(f1046Model)) {
			f1046Model = null;
		}
		if (DEV_NAME_TITLE.equals(f1046Name)) {
			f1046Name = null;
		}
		List<Tb1093VoltagekkEntity> list = secProBrkService.getIotermListByIedParams(f1046Model, f1046Name);
		if (list != null) {
			table.setInput(list);
		}
	}
	
	private void importData() {
		String filePath = DialogHelper.getSaveFilePath("文件", "", new String[]{"*.xlsx"});
		if (filePath == null || "".equals(filePath)){
			DialogHelper.showAsynError("请选择要导入文件路径");
		}
		List<Tb1093VoltagekkEntity> list = secProBrkService.importData(filePath);
		if (list != null) {
			table.setInput(list);
		}
	}
	
	private void exportData() {
		try {
			table.exportExcel(table.getTableDesc());
			DialogHelper.showAsynInformation("导出成功");
		} catch (Exception e) {
			DialogHelper.showAsynError("导出失败！");
		}
	}
	
	private void add() {
		table.addRow(RscObjectUtils.createTb1093());
	}
	
	private void delete() {
		Tb1093VoltagekkEntity entity = (Tb1093VoltagekkEntity) table.getSelection();
		secProBrkService.delete(entity);
		table.removeSelected();
	}
	
}
