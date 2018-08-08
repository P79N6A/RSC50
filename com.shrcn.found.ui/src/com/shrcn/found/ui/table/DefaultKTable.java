/**
 * Copyright (c) 2007-2013 上海思弘瑞电力控制技术有限公司. All rights reserved. 
 * This program is an eclipse Rich Client Application
 * designed for IED configuration and debuging.
 */
package com.shrcn.found.ui.table;

import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;

import com.shrcn.found.common.util.ArrayUtil;
import com.shrcn.found.ui.UIConstants;

import de.kupzog.ktable.KTable;

/**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2014-1-2
 */
public class DefaultKTable extends KTable {

	protected DefaultKTableModel model;
	
	public DefaultKTable(Composite parent, int style) {
		this(parent, style, null);
	}
	
	/**
	 * 构造函数
	 * @param parent
	 * @param model
	 */
	public DefaultKTable(Composite parent, DefaultKTableModel model) {
		this(parent, UIConstants.Ktable_LastFill, model);
	}
	
	public DefaultKTable(Composite parent, int style, DefaultKTableModel model) {
		super(parent, style);
		setBackground(UIConstants.Content_BG);
		if (model != null) {
			setModel(model);
			this.model = model;
		}
	}
	
	/**
	 * 表增加行
	 */
	public void addItem(Object item) {
		model.addItem(item);
		redraw();
	}
	
	/**
	 * 刷新
	 */
	public void refresh() {
		if (model != null)
			model.content.clear();
		clearSelection();
		if (m_CellEditor!=null)
			m_CellEditor.close(true);
		redraw();
	}

	/**
	 * 删除表某一行
	 * 
	 * @param row 行号
	 */
	public void deleteItem(int row) {
		model.deleteItem(row);
		refresh();
	}
	
	/**
	 * 删除多行
	 * @param rows
	 */
	public void deleteItems(int[] rows) {
		java.util.Arrays.sort(rows);
		for (int i=rows.length - 1; i > -1; i--) {
			model.deleteItem(rows[i]);
		}
		refresh();
	}

	/**
	 * 删除所有行
	 */
	public void deleteAll() {
		model.deleteAllItem();
		redraw();
	}

	/**
	 * 表插入某一行
	 * 
	 * @param row行号
	 */
	public void insertNewItem(int row, Object item) {
		model.insertNewItem(row, item);
		redraw();
	}

	/**
	 * 表行移动
	 * 
	 * @param row
	 *            交换行号
	 * @param newrow
	 *            交换行号
	 */
	public void move(int row, int newrow) {
		model.move(row, newrow);
		setSelection(1, newrow, true);
		redraw();
	}
	
	/**
	 * 上一一行
	 */
	public void moveUp() {
		int[] rows = getRowSelection();
		if (ArrayUtil.isEmpty(rows))
			return;
		java.util.Arrays.sort(rows);
		if (rows[0] == model.getFixedRowCount()) // 上边界
			return;
		Point[] selections = new Point[rows.length];
		for (int i=0; i<rows.length; i++) {
			int row = rows[i];
			model.move(row, row - 1);
			selections[i] = new Point(1, row - 1);
		}
		clearSelection();
		setSelection(selections, true);
		redraw();
	}
	
	/**
	 * 下移一行
	 */
	public void moveDown() {
		int[] rows = getRowSelection();
		if (ArrayUtil.isEmpty(rows))
			return;
		java.util.Arrays.sort(rows);
		ArrayUtil.reserve(rows);
		if (rows[0] == model.getRowCount() - 1) // 下边界
			return;
		Point[] selections = new Point[rows.length];
		for (int i=0; i<rows.length; i++) {
			int row = rows[i];
			model.move(row, row + 1);
			selections[i] = new Point(1, row + 1);
		}
		clearSelection();
		setSelection(selections, true);
		redraw();
	}

	public DefaultKTableModel getTableModel() {
		return model;
	}
}
