package com.synet.tool.rsc.dialog;

import java.util.List;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

import com.shrcn.found.ui.app.WrappedDialog;
import com.shrcn.found.ui.util.SwtUtil;
import com.synet.tool.rsc.model.Tb1042BayEntity;
import com.synet.tool.rsc.model.Tb1043EquipmentEntity;
import com.synet.tool.rsc.service.EquipmentEntityService;

/**
 * 选择互感器次级
 * @author Administrator
 *
 */
public class SelectEquiDialog  extends WrappedDialog{

	private Tb1042BayEntity bayEntity;
	private EquipmentEntityService service;
	private Combo combo;
	private String[] items;
	private List<Tb1043EquipmentEntity> result;
	private Tb1043EquipmentEntity select = null;
	
	protected SelectEquiDialog(Shell parentShell) {
		super(parentShell);
	}
	
	public SelectEquiDialog(Shell parentShell, Tb1042BayEntity bayEntity) {
		super(parentShell);
		this.bayEntity = bayEntity;
		this.service = new EquipmentEntityService();
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		initData();
		GridData gridData = new GridData(GridData.FILL_BOTH);
		Composite composite = SwtUtil.createComposite(parent, gridData, 1);
		composite.setLayout(SwtUtil.getGridLayout(2));
		SwtUtil.createText(composite, SwtUtil.bt_gd, "选择互感器：");
		combo = SwtUtil.createCombo(composite, SwtUtil.bt_hd);
		combo.setItems(items);
		combo.select(0);
		return composite;
	}
	
	
	private void initData() {
		result = service.getEquipmentEntitysByBayEntity(bayEntity);
		int size = result.size();
		items = new String[size];
		for (int i = 0; i < size; i++) {
			items[i] = result.get(i).getF1043Name();
		}
		
	}

	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("互感器选择");
	}
	
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, "OK", true);
		createButton(parent, IDialogConstants.CANCEL_ID, "取消", true);
	}
	
	@Override
	protected Point getInitialSize() {
		return new Point(270, 120);
	}
	
	@Override
	protected void buttonPressed(int buttonId) {
		if (IDialogConstants.OK_ID == buttonId) {
			select = result.get(combo.getSelectionIndex());
		}
		super.buttonPressed(buttonId);
	}

	public Tb1043EquipmentEntity getSelect() {
		return select;
	}
	
}
