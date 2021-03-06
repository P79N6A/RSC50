/**
 * Copyright (c) 2007-2013 上海思弘瑞电力控制技术有限公司. All rights reserved. 
 * This program is an eclipse Rich Client Application
 * designed for IED configuration and debuging.
 */
package com.synet.tool.rsc.editor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;

import com.shrcn.found.common.dict.DictManager;
import com.shrcn.found.ui.editor.IEditorInput;
import com.shrcn.found.ui.util.DialogHelper;
import com.shrcn.found.ui.util.SwtUtil;
import com.synet.tool.rsc.DBConstants;
import com.synet.tool.rsc.RSCConstants;
import com.synet.tool.rsc.excel.ImportInfoParser;
import com.synet.tool.rsc.model.Tb1046IedEntity;
import com.synet.tool.rsc.model.Tb1067CtvtsecondaryEntity;
import com.synet.tool.rsc.model.Tb1093VoltagekkEntity;
import com.synet.tool.rsc.service.CtvtsecondaryService;
import com.synet.tool.rsc.service.SecProBrkService;
import com.synet.tool.rsc.ui.TableFactory;
import com.synet.tool.rsc.util.RscObjectUtils;

/**
 * 安措->保护电压回路空开树菜单编辑器。
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2013-4-3
 */
public class SecProBrkEditor extends SafetyMeasureEditor {
	
//	private Combo cmbDevType;
//	private Combo cmbDevName;
//	private Button btnSearch;
//	private Button btnImport;
//	private Button btnExport;
//	private Button btnAdd;
//	private Button btnDelete;
	private SecProBrkService secProBrkService;
	private CtvtsecondaryService ctvtsecondaryService;
	
	public SecProBrkEditor(Composite container, IEditorInput input) {
		super(container, input);
	}
	
	@Override
	public void init() {
		secProBrkService = new SecProBrkService();
		ctvtsecondaryService = new CtvtsecondaryService();
		super.init();
	}

	@Override
	public void buildUI(Composite container) {
		super.buildUI(container);
		container.setLayout(SwtUtil.getGridLayout(1));
		Composite topComp = SwtUtil.createComposite(container, new GridData(GridData.FILL_HORIZONTAL), 9);
		cmbDevType = SwtUtil.createCombo(topComp, SwtUtil.bt_hd,true);
		cmbDevName = SwtUtil.createCombo(topComp, SwtUtil.bt_hd,true);
		btnSearch = SwtUtil.createButton(topComp, SwtUtil.bt_gd, SWT.BUTTON1, RSCConstants.SEARCH);
		SwtUtil.createLabel(topComp, "", SwtUtil.bt_hd); 
		btnAutoData = SwtUtil.createButton(topComp, SwtUtil.bt_gd, SWT.BUTTON1, "自动生成数据");
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
				} else if(evnet == btnAutoData) {
					autoData();
				}
				super.widgetSelected(e);
			}
		};
		btnSearch.addSelectionListener(listener);
		btnImport.addSelectionListener(listener);
		btnExport.addSelectionListener(listener);
		btnAdd.addSelectionListener(listener);
		btnDelete.addSelectionListener(listener);
		btnAutoData.addSelectionListener(listener);
	}

	@SuppressWarnings("unchecked")
	protected void autoData() {
		if (table.getInput().size() > 0) {
			if (!DialogHelper.showConfirm("该操作会覆盖当前数据，是否继续执行？")) {
				return;
			}
		}
		List<Tb1067CtvtsecondaryEntity>  ctvList = ctvtsecondaryService.getCtvtList();
		if (ctvList != null && ctvList.size() > 0) {
			List<Tb1093VoltagekkEntity> list = new ArrayList<>();
			int i = 1;
			for (Tb1067CtvtsecondaryEntity ctv : ctvList) {
				Tb1093VoltagekkEntity entity = new Tb1093VoltagekkEntity();
				entity.setF1093Code(rscp.nextTbCode(DBConstants.PR_VOLTAGEKK));
				entity.setTb1067CtvtsecondaryByF1067Code(ctv);
				entity.setF1093Desc("保护电压回路空开" + i++);
//				entity.setF1093KkNo("0");
				list.add(entity);
			}
			if (list.size() > 0) {
				secProBrkService.deleteBatch((List<Tb1093VoltagekkEntity>)table.getInput());
				table.setInput(list);
				secProBrkService.saveBatch(list);
			}
		}
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
				nameItems.add(ied.getF1046Name());
			}
		}
		String[] devTypes = DictManager.getInstance().getDictNames("IED_TYPE");
		typeItems.addAll(Arrays.asList(devTypes));
		cmbDevType.setItems(typeItems.toArray(new String[0]));
		cmbDevName.setItems(nameItems.toArray(new String[0]));
		cmbDevType.select(0);
		cmbDevName.select(0);
		super.initData();
	}
	
	private void search() {
		String f1046Type = cmbDevType.getText().trim();
		String f1046Name = cmbDevName.getText().trim();
		if (DEV_TYPE_TITLE.equals(f1046Type)) {
			f1046Type = null;
		} else {
			f1046Type = DictManager.getInstance().getIdByName("IED_TYPE", f1046Type);
		}
		if (DEV_NAME_TITLE.equals(f1046Name)) {
			f1046Name = null;
		}
		List<Tb1093VoltagekkEntity> list = secProBrkService.getIotermListByIedParams(f1046Type, f1046Name);
		if (list != null) {
			table.setInput(list);
		}
	}
	
	private void importData() {
		String filePath = DialogHelper.getSaveFilePath("文件", "", new String[]{"*.xlsx"});
		if (filePath == null || "".equals(filePath)){
			return;
		}
		List<Tb1093VoltagekkEntity> list = new ImportInfoParser().getVoltagekkList(filePath);
		if (list != null) {
			secProBrkService.saveBatch(list);
			table.setInput(list);
		}
	}

	private void add() {
		table.addRow(RscObjectUtils.createTb1093());
	}
	
	private void delete() {
		List<Object> list = table.getSelections();
		if (list != null && list.size() > 0) {
			for (Object obj : list) {
				secProBrkService.delete(obj);
			}
		}
		table.removeSelected();
	}
	
}
