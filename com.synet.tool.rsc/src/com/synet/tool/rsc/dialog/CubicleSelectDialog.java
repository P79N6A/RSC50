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
import com.synet.tool.rsc.model.Tb1046IedEntity;
import com.synet.tool.rsc.model.Tb1050CubicleEntity;

public class CubicleSelectDialog extends KTableEditorDialog {

	private Tb1046IedEntity ied;
	private Combo combo;
	private String[] items;
	private List<Tb1050CubicleEntity> allCubicle;
	private String oldData;
	private BeanDaoService instance;

	public CubicleSelectDialog(Shell parentShell, Object item) {
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
		Tb1050CubicleEntity cubicle = ied.getTb1050CubicleEntity();
		if(cubicle == null) {
			oldData = "*初始值为空*";
		} else {
			oldData = cubicle.getF1050Name();
		}
		instance = BeanDaoImpl.getInstance();
		allCubicle = (List<Tb1050CubicleEntity>) instance.getAll(Tb1050CubicleEntity.class);
		items = new String[allCubicle.size()];
		for (int i = 0; i < items.length; i++) {
			items[i] = allCubicle.get(i).getF1050Name();
		}
	}
	
	
	@Override
	protected void buttonPressed(int buttonId) {
		if (buttonId == OK) {
			int selectionIndex = combo.getSelectionIndex();
			if(selectionIndex > -1) {
				Tb1050CubicleEntity selected = allCubicle.get(selectionIndex);
				ied.setF1050Code(selected.getF1050Code());
				ied.setTb1050CubicleEntity(selected);
				instance.update(ied);
			}
		}
		super.buttonPressed(buttonId);
	}
	
	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("屏柜选择"); 
	}
	
	@Override
	protected Point getInitialSize() {
		return new Point(280, 150);
	}
	
}
