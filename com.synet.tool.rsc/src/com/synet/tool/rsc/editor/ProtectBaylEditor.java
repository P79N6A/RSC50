/**
 * Copyright (c) 2007-2013 上海思弘瑞电力控制技术有限公司. All rights reserved. 
 * This program is an eclipse Rich Client Application
 * designed for IED configuration and debuging.
 */
package com.synet.tool.rsc.editor;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
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
import com.synet.tool.rsc.DBConstants;
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
		textGridData.widthHint = 120;
		comboDevType = SwtUtil.createCombo(comp, textGridData, true);
		textDesc = SwtUtil.createText(comp, new GridData(300, SWT.DEFAULT));
		textDesc.setMessage("名称、描述、厂商、型号");
		btnSearch = SwtUtil.createButton(comp, SwtUtil.bt_gd, SWT.BUTTON1, RSCConstants.SEARCH);
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
					doSearch();
				} else if(source == comboDevType) {
					int comboCurSel = comboDevType.getSelectionIndex();
					if(comboPreSel == comboCurSel || comboCurSel<0) {
						return;
					}
					comboPreSel = comboCurSel;
					initTableData();
				}
			}
		};
		btnSearch.addSelectionListener(listener);
		comboDevType.addSelectionListener(listener);
		textDesc.addKeyListener(new KeyListener() {
			@Override
			public void keyPressed(KeyEvent e) {
			}
			@Override
			public void keyReleased(KeyEvent e) {
				if (e.keyCode == 13) {
					doSearch();
				}
			}});
	}

	@Override
	public void initData() {
		EditorConfigData editorConfigData = (EditorConfigData) getInput().getData();
		bayEntity = (Tb1042BayEntity) editorConfigData.getData();
		
		String bayName = (bayEntity==null) ? DBConstants.BAY_ALL : bayEntity.getF1042Name();
		String[] comboItems = null;
		if (DBConstants.BAY_MOT.equals(bayName)) {
			comboItems = new String[]{RSCConstants.DEV_TYPE_MOT};
		} else if (DBConstants.BAY_ALL.equals(bayName)) {
			comboItems = new String[]{RSCConstants.DEV_TYPE_ALL, RSCConstants.DEV_TYPE_PRO,
					RSCConstants.DEV_TYPE_TER, RSCConstants.DEV_TYPE_UNIT, RSCConstants.DEV_TYPE_UNIT_TER,
					RSCConstants.DEV_TYPE_MOT, RSCConstants.DEV_TYPE_SWC, RSCConstants.DEV_TYPE_ODF, RSCConstants.DEV_TYPE_GAT};
		} else {
			comboItems = new String[]{RSCConstants.DEV_TYPE_ALL, RSCConstants.DEV_TYPE_PRO,
					RSCConstants.DEV_TYPE_TER, RSCConstants.DEV_TYPE_UNIT, RSCConstants.DEV_TYPE_UNIT_TER};
		}
		if (comboItems != null) {
			comboDevType.setItems(comboItems);
			comboDevType.select(0);
		}
		
		iedEntityAll = iedEntityService.getIedEntityByBay(bayEntity);
		table.setInput(iedEntityAll);
		super.initData();
	}

	private void initTableData() {
		EnumIedType typeDev = EnumIedType.getTypeByDesc(comboDevType.getText());
		if (typeDev == null) {
			table.setInput(iedEntityAll);
		} else {
			int[] devTypes = typeDev.getTypes();
			List<Tb1046IedEntity> iedEntityByTypes = iedEntityService.getIedByTypesAndBay(devTypes, bayEntity);
			table.setInput(iedEntityByTypes);
		}
	}

	private void doSearch() {
		String desc = textDesc.getText().trim();
		List<Tb1046IedEntity> searchRes = new ArrayList<>();
		if(desc.isEmpty()) {
			searchRes = iedEntityAll;
		} else {
			for (Tb1046IedEntity tb1046IedEntity : iedEntityAll) {
				if(tb1046IedEntity.getF1046Desc().contains(desc)
						|| tb1046IedEntity.getF1046Name().contains(desc)
						|| tb1046IedEntity.getF1046Manufacturor().contains(desc)
						|| tb1046IedEntity.getF1046Model().contains(desc)) {
					searchRes.add(tb1046IedEntity);
				}
			}
		}
		table.setInput(searchRes);
	}
}
