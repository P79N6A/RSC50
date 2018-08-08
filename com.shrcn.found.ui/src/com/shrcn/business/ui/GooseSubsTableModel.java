/**
 * Copyright (c) 2007-2013 上海思弘瑞电力控制技术有限公司. All rights reserved. 
 * This program is an eclipse Rich Client Application
 * designed for IED configuration and debuging.
 */
package com.shrcn.business.ui;

import java.util.List;

import com.shrcn.found.ui.model.IField;
import com.shrcn.found.ui.model.TableConfig;
import com.shrcn.found.ui.table.RKTable;
import com.shrcn.found.ui.table.RKTableModel;

/**
 * 
 * @author 孙春颖(mailto:scy@shrcn.com)
 * @version 1.0, 2013-11-5
 */
/**
 * $Log: GooseSubsTableModel.java,v $
 * Revision 1.1  2013/11/05 01:39:27  scy
 * Add：创建
 *
 */
public class GooseSubsTableModel extends RKTableModel {

	public GooseSubsTableModel(RKTable table, TableConfig config) {
		super(table, config);
	}

	@Override
	public void doSetContentAt(int col, int row, Object value) {
		if (row > 0 && col == 1) {// count为1的互斥操作
			IField maxField = fields[col + 1];
			if (maxField.getWidth() == 1) {
				List<?> input = table.getInput();
				int i = 1;
				for (Object obj : input) {
					GooseSubsNet net = (GooseSubsNet) obj;
					if (net.getSelect() && (i != row)) {
						net.setSelect(false);
					}
					i++;
				}
				table.refresh();
			}
		}
		super.doSetContentAt(col, row, value);
	}
}
