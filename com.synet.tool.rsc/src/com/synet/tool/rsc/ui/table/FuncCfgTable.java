package com.synet.tool.rsc.ui.table;

import org.eclipse.swt.widgets.Composite;

import com.shrcn.found.ui.model.TableConfig;
import com.shrcn.found.ui.table.DefaultKTable;
import com.shrcn.tool.found.das.BeanDaoService;
import com.shrcn.tool.found.das.impl.BeanDaoImpl;

public class FuncCfgTable extends DevKTable {

	private BeanDaoService beanDao;
	
	public FuncCfgTable(Composite parent, TableConfig config) {
		super(parent, config);
		beanDao = BeanDaoImpl.getInstance();
	}

	protected void initUI() {
		tablemodel = new DevKTableModel(this, config);
		table = new DefaultKTable(parent, tablemodel);	
	}
	
	@Override
	public void removeSelected() {
		beanDao.deleteBatch(getSelections());
		super.removeSelected();
	}
	
}
