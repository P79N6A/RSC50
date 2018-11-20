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
import com.synet.tool.rsc.model.Tb1063CircuitEntity;
import com.synet.tool.rsc.service.PoutEntityService;

public class ConvChk2Dialog extends KTableEditorDialog {

	private Combo combo;
	private String[] items;
	private String oldData;
	private List<Tb1061PoutEntity> poutEntitys;
	private Tb1063CircuitEntity circuit;
	private PoutEntityService service;

	public ConvChk2Dialog(Shell parentShell, Object item) {
		super(parentShell, item);
		circuit = (Tb1063CircuitEntity) item;
		
	}
	
	@Override
	protected Control createDialogArea(Composite parent) {
		initData();
		GridData gridData = new GridData(GridData.FILL_BOTH);
		Composite composite = SwtUtil.createComposite(parent, gridData, 1);
		composite.setLayout(SwtUtil.getGridLayout(2));
		SwtUtil.createLabel(composite, "出口信号：", SwtUtil.bt_gd);
		combo = SwtUtil.createCombo(composite,  SwtUtil.bt_hd);
		combo.setItems(items);
		combo.setText(oldData);
		return super.createDialogArea(parent);
	}
	
	private void initData() {
		Tb1046IedEntity iedRecv = circuit.getTb1046IedByF1046CodeIedRecv();
		service = new PoutEntityService();
		poutEntitys = service.getByIed(iedRecv);
		int size = poutEntitys.size();
		items = new String[size];
		for (int i = 0; i < size; i++) {
			items[i] = poutEntitys.get(i).getF1061Desc();
		}
		Tb1061PoutEntity poutEntity = circuit.getTb1061PoutByF1061CodeConvChk2();
		if(poutEntity == null) {
			oldData = "*初始值为空*";
		} else {
			oldData = poutEntity.getF1061Desc();
		}
	}
	
	
	@Override
	protected void buttonPressed(int buttonId) {
		if (buttonId == OK) {
			int selectionIndex = combo.getSelectionIndex();
			if(selectionIndex > -1) {
				Tb1061PoutEntity poutEntity = poutEntitys.get(selectionIndex);
				circuit.setTb1061PoutByF1061CodeConvChk2(poutEntity);
				service.update(circuit);
			}
		}
		super.buttonPressed(buttonId);
	}
	
	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("命令出口信号"); 
	}
	
	@Override
	protected Point getInitialSize() {
		return new Point(280, 150);
	}
	
}
