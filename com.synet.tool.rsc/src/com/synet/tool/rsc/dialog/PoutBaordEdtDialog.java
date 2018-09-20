package com.synet.tool.rsc.dialog;

import java.util.List;

import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

import com.shrcn.found.ui.table.KTableEditorDialog;
import com.shrcn.found.ui.util.SwtUtil;
import com.synet.tool.rsc.model.Tb1046IedEntity;
import com.synet.tool.rsc.model.Tb1061PoutEntity;
import com.synet.tool.rsc.model.Tb1064StrapEntity;
import com.synet.tool.rsc.service.StrapEntityService;

public class PoutBaordEdtDialog extends KTableEditorDialog{

	private Tb1061PoutEntity poutEntity;
	private Combo combo;
	private String[] items;
	private String oldData;
	private StrapEntityService strapEntityService;
	private List<Tb1064StrapEntity> staEntities;

	public PoutBaordEdtDialog(Shell parentShell, Object item) {
		super(parentShell, item);
		poutEntity = (Tb1061PoutEntity) item;
		
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		initData();
		GridData gridData = new GridData(GridData.FILL_BOTH);
		Composite composite = SwtUtil.createComposite(parent, gridData, 1);
		composite.setLayout(SwtUtil.getGridLayout(2));
		SwtUtil.createLabel(composite, "板卡选择：", SwtUtil.bt_gd);
		combo = SwtUtil.createCombo(composite,  SwtUtil.bt_hd);
		combo.setItems(items);
		combo.setText(oldData);
		return super.createDialogArea(parent);
	}
	
	private void initData() {
		Tb1046IedEntity iedEntity = poutEntity.getTb1046IedByF1046Code();
		strapEntityService = new StrapEntityService();
		staEntities = strapEntityService.getByIed(iedEntity);
		int size = staEntities.size();
		items = new String[size];
		for (int i = 0; i < size; i++) {
			items[i] = staEntities.get(i).getF1064Desc();
		}
		Tb1064StrapEntity strapEntity = poutEntity.getTb1064StrapByF1064Code();
		if(strapEntity == null) {
			oldData = "*初始值为空*";
		} else {
			oldData = strapEntity.getF1064Desc();
		}
	}
	
	
	@Override
	protected void buttonPressed(int buttonId) {
		if (buttonId == OK) {
			int selectionIndex = combo.getSelectionIndex();
			if(selectionIndex > -1) {
				Tb1064StrapEntity strapEntity = staEntities.get(selectionIndex);
				poutEntity.setTb1064StrapByF1064Code(strapEntity);
				strapEntityService.update(poutEntity);
			}
		}
		super.buttonPressed(buttonId);
	}
	
	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("开出虚端子板卡配置"); 
	}
	
	@Override
	protected Point getInitialSize() {
		return new Point(280, 150);
	}
	
}
