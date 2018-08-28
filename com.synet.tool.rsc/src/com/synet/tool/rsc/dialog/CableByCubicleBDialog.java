package com.synet.tool.rsc.dialog;

import java.util.List;

import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

import com.shrcn.found.ui.table.KTableEditorDialog;
import com.synet.tool.rsc.model.Tb1050CubicleEntity;
import com.synet.tool.rsc.model.Tb1051CableEntity;
import com.synet.tool.rsc.service.PhyscialAreaService;
import com.synet.tool.rsc.ui.TableFactory;
import com.synet.tool.rsc.ui.table.DevKTable;

public class CableByCubicleBDialog extends KTableEditorDialog {
	
	private DevKTable table;
	private PhyscialAreaService service;

	public CableByCubicleBDialog(Shell parentShell, Object item) {
		super(parentShell, item);
	}
	
	@Override
	protected Control createDialogArea(Composite parent) {
		table = TableFactory.getCubicleTable(parent);
		table.getTable().setLayoutData(new GridData(GridData.FILL_BOTH));
		initData();
		return super.createDialogArea(parent);
	}
	
	@SuppressWarnings("unchecked")
	private void initData() {
		service = new PhyscialAreaService();
		List<Tb1050CubicleEntity> list = (List<Tb1050CubicleEntity>) service.getAll(Tb1050CubicleEntity.class);
		if (list != null) {
			table.setInput(list);
		}
	}
	
	
	@Override
	protected void buttonPressed(int buttonId) {
		if (buttonId == OK) {
			if (table.getSelection() != null) {
				Tb1050CubicleEntity entity = (Tb1050CubicleEntity) table.getSelection();
				if (item instanceof Tb1051CableEntity) {
					((Tb1051CableEntity)item).setTb1050CubicleByF1050CodeB(entity);
				} 
				service.saveTableData(item);
			}
		}
		super.buttonPressed(buttonId);
	}
	
	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("屏柜列表"); //$NON-NLS-1$
	}
	
	@Override
	protected Point getInitialSize() {
		return new Point(480, 410);
	}
}
