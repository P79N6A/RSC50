/**
 * Copyright (c) 2007-2013 上海思弘瑞电力控制技术有限公司. All rights reserved. 
 * This program is an eclipse Rich Client Application
 * designed for IED configuration and debuging.
 */
package com.shrcn.business.ui;

import org.eclipse.swt.widgets.Composite;

import com.shrcn.found.ui.model.TableConfig;
import com.shrcn.found.ui.table.DefaultKTable;
import com.shrcn.found.ui.table.RKTable;

/**
 * 
 * @author 孙春颖(mailto:scy@shrcn.com)
 * @version 1.0, 2013-11-5
 */
/**
 * $Log: GooseSubsTable.java,v $
 * Revision 1.1  2013/11/05 01:39:27  scy
 * Add：创建
 *
 */
public class GooseSubsTable extends RKTable {

	public GooseSubsTable(Composite parent, TableConfig config) {
		super(parent, config);
	}
	
	protected void initUI() {
		tablemodel = new GooseSubsTableModel(this, config);
		table = new DefaultKTable(parent, tablemodel);
	}
}
