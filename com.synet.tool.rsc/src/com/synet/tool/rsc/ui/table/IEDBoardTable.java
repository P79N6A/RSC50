package com.synet.tool.rsc.ui.table;

import org.eclipse.swt.widgets.Composite;

import com.shrcn.found.ui.model.TableConfig;
import com.shrcn.found.ui.table.DefaultKTable;

public class IEDBoardTable extends DevKTable {

	public IEDBoardTable(Composite parent, TableConfig config) {
		super(parent, config);
	}

	protected void initUI() {
		tablemodel = new IEDBoardTableModel(this, config);
		table = new DefaultKTable(parent, tablemodel);	
	}
}
