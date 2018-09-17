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
import com.shrcn.tool.found.das.BeanDaoService;
import com.shrcn.tool.found.das.impl.BeanDaoImpl;
import com.synet.tool.rsc.model.Tb1042BayEntity;
import com.synet.tool.rsc.model.Tb1046IedEntity;

public class BaySelectDialog extends KTableEditorDialog {

	private Tb1046IedEntity ied;
	private List<Tb1042BayEntity> allBay;
	private Combo combo;
	private String[] items;
	private String oldData;
	private BeanDaoService instance;

	public BaySelectDialog(Shell parentShell, Object item) {
		super(parentShell, item);
		ied = (Tb1046IedEntity) item;
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		initData();
		GridData gridData = new GridData(GridData.FILL_BOTH);
		Composite composite = SwtUtil.createComposite(parent, gridData, 1);
		composite.setLayout(SwtUtil.getGridLayout(2));
		SwtUtil.createLabel(composite, "屏柜选择：", SwtUtil.bt_gd);
		combo = SwtUtil.createCombo(composite,  SwtUtil.bt_hd);
		combo.setItems(items);
		combo.setText(oldData);
		return super.createDialogArea(parent);
	}
	
	@SuppressWarnings("unchecked")
	private void initData() {
		Tb1042BayEntity bay = ied.getTb1042BaysByF1042Code();
		if(bay == null) {
			oldData = "*初始值为空*";
		} else {
			oldData = bay.getF1042Name();
		}
		
		instance = BeanDaoImpl.getInstance();
		allBay = (List<Tb1042BayEntity>) instance.getAll(Tb1042BayEntity.class);
		items = new String[allBay.size()];
		for (int i = 0; i < items.length; i++) {
			items[i] = allBay.get(i).getF1042Name();
		}
		
	}
	
	
	@Override
	protected void buttonPressed(int buttonId) {
		if (buttonId == OK) {
			int selectionIndex = combo.getSelectionIndex();
			if(selectionIndex > -1) {
				Tb1042BayEntity selected = allBay.get(selectionIndex);
				ied.setF1042Code(selected.getF1042Code());
				ied.setTb1042BaysByF1042Code(selected);
				instance.update(ied);
			}
		}
		super.buttonPressed(buttonId);
	}
	
	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("间隔选择"); 
	}
	
	@Override
	protected Point getInitialSize() {
		return new Point(280, 150);
	}
	
}
