/**
 * Copyright (c) 2007-2013 上海思弘瑞电力控制技术有限公司. All rights reserved. 
 * This program is an eclipse Rich Client Application
 * designed for IED configuration and debuging.
 */
package com.synet.tool.rsc.editor;

import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;

import com.shrcn.found.ui.editor.IEditorInput;
import com.shrcn.found.ui.util.SwtUtil;
import com.synet.tool.rsc.model.Tb1092PowerkkEntity;
import com.synet.tool.rsc.service.SecProBrkService;
import com.synet.tool.rsc.service.SecPwrBrkService;
import com.synet.tool.rsc.ui.TableFactory;

/**
 * 安措->装置电源空开树菜单编辑器。
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2013-4-3
 */
public class SecPwrBrkEditor extends BaseConfigEditor {
	private Combo cmbDevType;
	private Combo cmbDevName;
	private Button btnSearch;
	private Button btnImport;
	private Button btnExport;
	private Button btnAdd;
	private Button btnDelete;
	private SecPwrBrkService secPwrBrkService;
	
	public SecPwrBrkEditor(Composite container, IEditorInput input) {
		super(container, input);
	}
	
	@Override
	public void init() {
		secPwrBrkService = new SecPwrBrkService();
		super.init();
	}

	@Override
	public void buildUI(Composite container) {
		super.buildUI(container);

		editArea.setLayout(SwtUtil.getGridLayout(1));
		
		Composite topComp = SwtUtil.createComposite(container, new GridData(GridData.FILL_HORIZONTAL), 8);
		GridData textGridData = new GridData();
		textGridData.heightHint = 25;
		textGridData.widthHint = 80;
		GridData btnGridData = new GridData();
		btnGridData.heightHint = 25;
		btnGridData.widthHint = 40;
		cmbDevType = SwtUtil.createCombo(topComp, textGridData, true);
		cmbDevType.setItems(new String[]{"装置类型"});
		cmbDevType.select(0);
		cmbDevName = SwtUtil.createCombo(topComp, textGridData, true);
		cmbDevName.setItems(new String[]{"装置名称"});
		cmbDevName.select(0);
		btnSearch = SwtUtil.createButton(topComp, btnGridData, SWT.NONE, "查询");
		SwtUtil.createLabel(topComp, "", textGridData); 
		btnImport = SwtUtil.createButton(topComp, btnGridData, SWT.NONE, "导入");
		btnExport = SwtUtil.createButton(topComp, btnGridData, SWT.NONE, "导出");
		btnAdd = SwtUtil.createButton(topComp, btnGridData, SWT.NONE, "添加");
		btnDelete = SwtUtil.createButton(topComp, btnGridData, SWT.NONE, "删除");
		
		Composite centerComp = SwtUtil.createComposite(container, new GridData(GridData.FILL_BOTH), 1);
		table = TableFactory.getPowerKKTable(centerComp);
		table.getTable().setLayoutData(new GridData(GridData.FILL_BOTH));
	}
	
	protected void addListeners() {
	}

	@Override
	public void initData() {
		List<Tb1092PowerkkEntity> list = secPwrBrkService.getPowerkkList();
		if (list != null){
			table.setInput(list);
		}
		super.initData();
	}
}
