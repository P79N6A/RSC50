package com.synet.tool.rsc.dialog;

import java.util.List;

import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

import com.shrcn.found.ui.table.KTableEditorDialog;
import com.synet.tool.rsc.model.Tb1067CtvtsecondaryEntity;
import com.synet.tool.rsc.model.Tb1093VoltagekkEntity;
import com.synet.tool.rsc.service.DefaultService;
import com.synet.tool.rsc.ui.TableFactory;
import com.synet.tool.rsc.ui.table.DevKTable;

public class CtvtChooseDialog extends KTableEditorDialog {
	
	private DevKTable table;
	private DefaultService defaultService;

	public CtvtChooseDialog(Shell parentShell, Object item) {
		super(parentShell, item);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	protected Control createDialogArea(Composite parent) {
		table = TableFactory.getCtvtChooseTable(parent);
		table.getTable().setLayoutData(new GridData(GridData.FILL_BOTH));
		initData();
		return super.createDialogArea(parent);
	}
	
	private void initData() {
		defaultService = new DefaultService();
		List<Tb1067CtvtsecondaryEntity> list = defaultService.getCtvtList();
		if (list != null) {
			table.setInput(list);
		}
	}
	
	
	@Override
	protected void buttonPressed(int buttonId) {
		if (buttonId == OK) {
			if (table.getSelection() != null) {
				Tb1067CtvtsecondaryEntity entity = (Tb1067CtvtsecondaryEntity) table.getSelection();
				if (item instanceof Tb1093VoltagekkEntity) {
					((Tb1093VoltagekkEntity)item).setTb1067CtvtsecondaryByF1067Code(entity);
				}
				defaultService.saveTableData(item);
			}
		}
		super.buttonPressed(buttonId);
	}
	
	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("互感器列表"); //$NON-NLS-1$
	}
	
	@Override
	protected Point getInitialSize() {
		return new Point(480, 410);
	}

}
