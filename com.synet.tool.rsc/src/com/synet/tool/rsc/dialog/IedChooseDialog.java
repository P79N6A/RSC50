package com.synet.tool.rsc.dialog;

import java.util.List;

import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

import com.shrcn.found.ui.table.KTableEditorDialog;
import com.synet.tool.rsc.model.Tb1046IedEntity;
import com.synet.tool.rsc.model.Tb1090LineprotfiberEntity;
import com.synet.tool.rsc.model.Tb1091IotermEntity;
import com.synet.tool.rsc.model.Tb1092PowerkkEntity;
import com.synet.tool.rsc.service.DefaultService;
import com.synet.tool.rsc.ui.TableFactory;
import com.synet.tool.rsc.ui.table.DevKTable;

public class IedChooseDialog extends KTableEditorDialog {
	
	private DevKTable table;
	private DefaultService defaultService;

	public IedChooseDialog(Shell parentShell, Object item) {
		super(parentShell, item);
	}
	
	@Override
	protected Control createDialogArea(Composite parent) {
		table = TableFactory.getIEDChooseTable(parent);
		table.getTable().setLayoutData(new GridData(GridData.FILL_BOTH));
		initData();
		return super.createDialogArea(parent);
	}
	
	private void initData() {
		defaultService = new DefaultService();
		List<Tb1046IedEntity> list = defaultService.getIedList();
		if (list != null) {
			table.setInput(list);
		}
	}
	
	
	@Override
	protected void buttonPressed(int buttonId) {
		if (buttonId == OK) {
			if (table.getSelection() != null) {
				Tb1046IedEntity entity = (Tb1046IedEntity) table.getSelection();
				if (item instanceof Tb1090LineprotfiberEntity) {
					((Tb1090LineprotfiberEntity)item).setTb1046IedByF1046Code(entity);
				} else if (item instanceof Tb1091IotermEntity) {
					((Tb1091IotermEntity)item).setTb1046IedByF1046Code(entity);
				} else if (item instanceof Tb1092PowerkkEntity) {
					((Tb1092PowerkkEntity)item).setTb1046IedByF1046Code(entity);
				}
				defaultService.saveTableData(item);
			}
		}
		super.buttonPressed(buttonId);
	}
	
	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("IED设备列表"); //$NON-NLS-1$
	}
	
	@Override
	protected Point getInitialSize() {
		return new Point(480, 410);
	}

}
