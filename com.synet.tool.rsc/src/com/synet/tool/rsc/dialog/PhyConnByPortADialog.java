package com.synet.tool.rsc.dialog;

import java.util.List;

import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

import com.shrcn.found.ui.table.KTableEditorDialog;
import com.synet.tool.rsc.model.Tb1048PortEntity;
import com.synet.tool.rsc.model.Tb1053PhysconnEntity;
import com.synet.tool.rsc.service.PhyscialAreaService;
import com.synet.tool.rsc.ui.TableFactory;
import com.synet.tool.rsc.ui.table.DevKTable;

public class PhyConnByPortADialog extends KTableEditorDialog {
	
	private DevKTable table;
	private PhyscialAreaService service;

	public PhyConnByPortADialog(Shell parentShell, Object item) {
		super(parentShell, item);
	}
	
	@Override
	protected Control createDialogArea(Composite parent) {
		table = TableFactory.getPortTable(parent);
		table.getTable().setLayoutData(new GridData(GridData.FILL_BOTH));
		initData();
		return super.createDialogArea(parent);
	}
	
	@SuppressWarnings("unchecked")
	private void initData() {
		service = new PhyscialAreaService();
		List<Tb1048PortEntity> list = (List<Tb1048PortEntity>) service.getAll(Tb1048PortEntity.class);
		if (list != null) {
			table.setInput(list);
		}
	}
	
	
	@Override
	protected void buttonPressed(int buttonId) {
		if (buttonId == OK) {
			if (table.getSelection() != null) {
				Tb1048PortEntity entity = (Tb1048PortEntity) table.getSelection();
				if (item instanceof Tb1053PhysconnEntity) {
					((Tb1053PhysconnEntity)item).setTb1048PortByF1048CodeA(entity);
				} 
				service.saveTableData(item);
			}
		}
		super.buttonPressed(buttonId);
	}
	
	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("端口列表"); //$NON-NLS-1$
	}
	
	@Override
	protected Point getInitialSize() {
		return new Point(480, 410);
	}
	
}
