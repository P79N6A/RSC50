package com.synet.tool.rsc.editor;

import static com.synet.tool.rsc.DBConstants.IED_CJQ;
import static com.synet.tool.rsc.DBConstants.IED_GP;
import static com.synet.tool.rsc.DBConstants.IED_JHJ;

import java.util.ArrayList;
import java.util.HashMap;
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
import com.shrcn.found.ui.model.IField;
import com.shrcn.found.ui.util.DialogHelper;
import com.shrcn.found.ui.util.SwtUtil;
import com.synet.tool.rsc.DBConstants;
import com.synet.tool.rsc.RSCConstants;
import com.synet.tool.rsc.model.Tb1042BayEntity;
import com.synet.tool.rsc.model.Tb1046IedEntity;
import com.synet.tool.rsc.service.IedEntityService;
import com.synet.tool.rsc.ui.TableFactory;

/**
 * 公共间隔
 * @author Administrator
 *
 */
public class ProtectBayPubEditor extends BaseConfigEditor{

	private Button btnSearch;
	private Combo comboDevType;
	private int comboPreSel = 0;
	private IedEntityService iedEntityService;
	private Text textDesc;
	private Tb1042BayEntity bayEntity;
	private List<Tb1046IedEntity> iedEntityAll;
	private Button btnAdd;
	private Button btnDel;
	private HashMap<Integer, Integer> typeMap;

	public ProtectBayPubEditor(Composite container, IEditorInput input) {
		super(container, input);
	}

	@Override
	public void init() {
		super.init();
		iedEntityService = new IedEntityService();
		typeMap = new HashMap<>();
		typeMap.put(1, IED_JHJ);
		typeMap.put(2, IED_GP);
		typeMap.put(3, IED_CJQ);
	}
	
	@Override
	public void buildUI(Composite container) {
		super.buildUI(container);
		GridData gridData = new GridData(GridData.FILL_BOTH);
		Composite comp = SwtUtil.createComposite(container, gridData, 1);
		comp.setLayout(SwtUtil.getGridLayout(5));
		GridData textGridData = new GridData();
		textGridData.heightHint = 25;
		textGridData.widthHint = 80;
		String[] comboItems = new String[]{RSCConstants.DEV_TYPE_ALL,
				RSCConstants.DEV_TYPE_SWC, RSCConstants.DEV_TYPE_ODF, RSCConstants.DEV_TYPE_GAT};
		comboDevType = SwtUtil.createCombo(comp, textGridData, true);
		comboDevType.setItems(comboItems);
		comboDevType.select(0);
		textDesc = SwtUtil.createText(comp, SwtUtil.bt_hd);
		textDesc.setMessage(RSCConstants.DESCRIPTION);
		btnSearch = SwtUtil.createButton(comp, SwtUtil.bt_gd, SWT.BUTTON1, RSCConstants.SEARCH);
		btnAdd = SwtUtil.createButton(comp, SwtUtil.bt_gd, SWT.BUTTON1, "添加");
		btnDel = SwtUtil.createButton(comp, SwtUtil.bt_gd, SWT.BUTTON1, "删除");
		SwtUtil.createLabel(comp, "			", new GridData(SWT.DEFAULT,10));
		GridData gdSpan_5 = new GridData(GridData.FILL_BOTH);
		gdSpan_5.horizontalSpan = 5;
		table = TableFactory.getProtectIntervalTable(comp);
		table.getTable().setLayoutData(gdSpan_5);
	}
	
	protected void addListeners() {
		SelectionListener listener = new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Object source = e.getSource();
				if(source == btnSearch) {
					String desc = textDesc.getText().trim();
					List<Tb1046IedEntity> searchRes = new ArrayList<>();
					if(desc.isEmpty()) {
						searchRes = iedEntityAll;
					} else {
						for (Tb1046IedEntity tb1046IedEntity : iedEntityAll) {
							if(tb1046IedEntity.getF1046Desc().contains(desc)) {
								searchRes.add(tb1046IedEntity);
							}
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
				} else if(source == btnAdd) {
					int selIdx = comboDevType.getSelectionIndex();
					if(selIdx <= 0) {
						DialogHelper.showAsynInformation("请选择类型");
						return;
					}
					int type = typeMap.get(selIdx);
					String desc = textDesc.getText().trim();
					if(desc.isEmpty()) {
						DialogHelper.showAsynInformation("名称不能为空");
						return;
					}
					String code = rscp.nextTbCode(DBConstants.PR_IED);
					Tb1046IedEntity iedEntity = new Tb1046IedEntity(code, desc, type);
					if (desc.indexOf(comboDevType.getText()) < 0) {
						desc += comboDevType.getText();
						iedEntity.setF1046Desc(desc);
					}
					iedEntity.setF1042Code(bayEntity.getF1042Code());
					iedEntity.setTb1042BaysByF1042Code(bayEntity);
					beandao.insert(iedEntity);
					table.addRow(iedEntity);
					table.getTable().layout();
				} else if(source == btnDel) {
					if (!DialogHelper.showConfirm("确定删除？", "是", "否")) {
						return;
					}
					List<Object> selections = table.getSelections();
					if(selections == null) {
						return;
					}
					for (Object o : selections) {
						iedEntityService.deleteTb1046IedEntity((Tb1046IedEntity) o);
					}
					table.removeSelected();
				}
			}
		};
		btnAdd.addSelectionListener(listener);
		btnDel.addSelectionListener(listener);
		btnSearch.addSelectionListener(listener);
		comboDevType.addSelectionListener(listener);
	}

	@Override
	public void initData() {
		EditorConfigData editorConfigData = (EditorConfigData) getInput().getData();
		bayEntity = (Tb1042BayEntity) editorConfigData.getData();
		int[] types = new int[]{IED_JHJ, IED_GP, IED_CJQ};
		iedEntityAll = iedEntityService.getIedByTypesAndBay(types, null);
		IField[] fields = table.getFields();
		for (IField field : fields) {
			String title = field.getTitle();
			if ("保护分类".equals(title)) {
				field.setEditor("combo");
			} else if (!"间隔".equals(title) && !"屏柜".equals(title)) {
				field.setEditor("text");
			}
		}
		table.setInput(iedEntityAll);
		super.initData();
	}

	private void initTableData(int comboIdx) {
		if(comboIdx == 0) {
			table.setInput(iedEntityAll);
		} else {
			int[] devTypes = new int[]{typeMap.get(comboIdx)};
			List<Tb1046IedEntity> iedEntityByTypes = iedEntityService.getIedByTypesAndBay(devTypes, bayEntity);
			table.setInput(iedEntityByTypes);
			table.getTable().layout();
		}
	}
}
