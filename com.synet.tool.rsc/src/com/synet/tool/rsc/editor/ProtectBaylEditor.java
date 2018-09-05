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
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

import com.shrcn.found.ui.editor.EditorConfigData;
import com.shrcn.found.ui.editor.IEditorInput;
import com.shrcn.found.ui.util.SwtUtil;
import com.synet.tool.rsc.RSCConstants;
import com.synet.tool.rsc.model.Tb1042BayEntity;
import com.synet.tool.rsc.model.Tb1046IedEntity;
import com.synet.tool.rsc.service.EnumIedType;
import com.synet.tool.rsc.service.IedEntityService;
import com.synet.tool.rsc.ui.TableFactory;

/**
 * 保护信息模型->间隔树菜单编辑器。
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2013-4-3
 */
public class ProtectBaylEditor extends BaseConfigEditor {
	
	private Button btnSearch;
	private Combo comboDevType;
	private int comboPreSel = 0;
	private IedEntityService iedEntityService;
	private Text textDesc;
	private Tb1042BayEntity bayEntity;
	private List<Tb1046IedEntity> iedEntityAll;

	public ProtectBaylEditor(Composite container, IEditorInput input) {
		super(container, input);
	}

	@Override
	public void init() {
		super.init();
		iedEntityService = new IedEntityService();
	}
	
	@Override
	public void buildUI(Composite container) {
		super.buildUI(container);
		GridData gridData = new GridData(GridData.FILL_BOTH);
		Composite comp = SwtUtil.createComposite(container, gridData, 1);
		comp.setLayout(SwtUtil.getGridLayout(3));
		GridData textGridData = new GridData();
		textGridData.heightHint = 25;
		textGridData.widthHint = 80;
		String[] comboItems = new String[]{RSCConstants.DEV_TYPE_ALL, RSCConstants.DEV_TYPE_PRO,
				RSCConstants.DEV_TYPE_TER, RSCConstants.DEV_TYPE_UNIT, RSCConstants.DEV_TYPE_UNIT_TER};
		comboDevType = SwtUtil.createCombo(comp, textGridData, true);
		comboDevType.setItems(comboItems);
		comboDevType.select(0);
		textDesc = SwtUtil.createText(comp, SwtUtil.bt_hd);
		textDesc.setMessage(RSCConstants.DESCRIPTION);
		btnSearch = SwtUtil.createButton(comp, SwtUtil.bt_gd, SWT.BUTTON1, RSCConstants.SEARCH);
		SwtUtil.createLabel(comp, "			", new GridData(SWT.DEFAULT,10));
		GridData gdSpan_3 = new GridData(GridData.FILL_BOTH);
		gdSpan_3.horizontalSpan = 3;
		table = TableFactory.getProtectIntervalTable(comp);
		table.getTable().setLayoutData(gdSpan_3);
	}
	
	protected void addListeners() {
		SelectionListener listener = new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Object source = e.getSource();
				if(source == btnSearch) {
					String desc = textDesc.getText().trim();
					List<Tb1046IedEntity> searchRes = new ArrayList<>();
					@SuppressWarnings("unchecked")
					List<Tb1046IedEntity> iedEntityByTypes = (List<Tb1046IedEntity>) table.getInput();
					for (Tb1046IedEntity tb1046IedEntity : iedEntityByTypes) {
						if(tb1046IedEntity.getF1046Desc().contains(desc)) {
							searchRes.add(tb1046IedEntity);
						}
					}
					table.setInput(searchRes);
					table.refresh();
				} else if(source == comboDevType) {
					int comboCurSel = comboDevType.getSelectionIndex();
					if(comboPreSel == comboCurSel) {
						return;
					}
					comboPreSel = comboCurSel;
					initTableData(comboCurSel);
				}
			}
		};
		
		btnSearch.addSelectionListener(listener);
		comboDevType.addSelectionListener(listener);
	}

	@Override
	public void initData() {
		EditorConfigData editorConfigData = (EditorConfigData) getInput().getData();
		bayEntity = (Tb1042BayEntity) editorConfigData.getData();
		iedEntityAll = iedEntityService.getIedEntityByBay(bayEntity);
		table.setInput(iedEntityAll);
		super.initData();
	}

	private void initTableData(int comboIdx) {
		if(comboIdx == 0) {
			table.setInput(iedEntityAll);
		} else {
			int[] devTypes = EnumIedType.values()[comboIdx-1].getTypes();
			List<Tb1046IedEntity> iedEntityByTypes = iedEntityService.getIedByTypesAndBay(devTypes, bayEntity);
			table.setInput(iedEntityByTypes);
		}
		table.getTable().layout();
	}
}
