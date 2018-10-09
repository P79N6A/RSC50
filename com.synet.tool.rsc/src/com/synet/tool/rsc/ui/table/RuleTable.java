package com.synet.tool.rsc.ui.table;

import org.eclipse.swt.widgets.Composite;

import com.shrcn.found.ui.model.TableConfig;
import com.shrcn.found.ui.table.DefaultKTable;
import com.shrcn.found.ui.table.RKTable;

public class RuleTable extends RKTable{

	public RuleTable(Composite parent, TableConfig config) {
		super(parent, config);
	}

	protected void initUI() {
		tablemodel = new RuleTableModel(this, config);
		table = new DefaultKTable(parent, tablemodel);	
	}
}
