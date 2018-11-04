package com.synet.tool.rsc.dialog;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

import com.shrcn.found.ui.table.KTableEditorDialog;
import com.synet.tool.rsc.model.Tb1063CircuitEntity;
import com.synet.tool.rsc.model.Tb1065LogicallinkEntity;
import com.synet.tool.rsc.ui.TableFactory;
import com.synet.tool.rsc.ui.table.DevKTable;

public class CircuitsViewDialog extends KTableEditorDialog {
	
	private DevKTable table;
	private Tb1065LogicallinkEntity logicLink;

	public CircuitsViewDialog(Shell parentShell, Object item) {
		super(parentShell, item);
		this.logicLink = (Tb1065LogicallinkEntity) item;
	}
	
	@Override
	protected Control createDialogArea(Composite parent) {
		table = TableFactory.getCircuitsViewTable(parent);
		table.getTable().setLayoutData(new GridData(GridData.FILL_BOTH));
		initData();
		return parent;
	}
	
	private void initData() {
		Set<Tb1063CircuitEntity> circuits = logicLink.getTb1063CircuitsByF1065Code();
		if (circuits != null && circuits.size() > 0) {
			List<Tb1063CircuitEntity> list = new ArrayList<>();
			list.addAll(circuits);
			table.setInput(list);
		}
	}
	
	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("虚回路列表"); //$NON-NLS-1$
	}
	
	@Override
	protected Point getInitialSize() {
		return new Point(880, 510);
	}

}
