/**
 * Copyright (c) 2007-2009 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based on IEC61850 SCT.
 */
package com.shrcn.business.scl.table.print;

import java.util.List;
import org.eclipse.swt.graphics.Point;

import com.shrcn.found.ui.model.IField;
import com.shrcn.found.ui.model.TableConfig;
import com.shrcn.found.ui.table.RKTableModel;
import com.shrcn.found.ui.table.XOTable;

/**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2009-11-19
 */

public class RKPrintTableModel extends RKTableModel implements PrintKTableModel {

	public RKPrintTableModel(XOTable table, TableConfig config) {
		super(table, config);
		init(table.getInput(),table.getFields());
	}

	private void init(List<?> input, IField[] fields) {
		this.items = input;
		this.fields = fields;
		this.content.clear();
	}

	@Override
	public Point doBelongsToCell(int col, int row) {
		return null;
	}
	protected int getValueIndex(int modelIdx) {
		int index = modelIdx;
		return index;
	}
	@Override
	public int getInitialColumnWidth(int column) {
//		column = fields[column].getValNum();
		int width = fields[column].getWidth();
		return width;
	}

	public int getRowHeight() {
		return 12;
	}

	public int getFirstRowHeight() {
		return 12;
	}
}
