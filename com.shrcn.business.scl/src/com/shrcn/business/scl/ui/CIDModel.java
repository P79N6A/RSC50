/**
 * Copyright (c) 2007, 2008 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application 
 * based Visual Device Develop System.
 */

package com.shrcn.business.scl.ui;

import java.util.List;

import org.dom4j.Element;

import com.shrcn.business.scl.das.IEDDAO;
import com.shrcn.found.ui.UIConstants;
import com.shrcn.found.ui.table.DefaultKTableModel;

import de.kupzog.ktable.KTableCellEditor;
import de.kupzog.ktable.KTableCellRenderer;
import de.kupzog.ktable.editors.KTableCellEditorCheckbox;
import de.kupzog.ktable.renderers.CheckableCellRenderer;

/**
 * CID模型
 * 
 * @author 黄钦辉
 * @version 1.0, 2008-9-1
 */
public class CIDModel extends DefaultKTableModel {

	/** 表头对应的字段 */
	private static final String[] head = {"序号", "选择", "名称", "类型", "描述", "厂商" };
	/** 表列数 */
	private static final int COLUMN_COUNT = head.length;
	public static final int IED_CHECK_COL = 1;
	public static final int IED_NAME_COL = 2;
	
	private CheckableCellRenderer m_CheckableRenderer = new CheckableCellRenderer(
			CheckableCellRenderer.INDICATION_CLICKED | CheckableCellRenderer.INDICATION_FOCUS);

	/**
	 * 构造函数
	 * @param name 名称
	 */
	public CIDModel(List<?> items) {
		this.items = items;
	}
	
	public CIDModel() {
		items = IEDDAO.getALLIED();
	}

	@Override
	public KTableCellEditor doGetCellEditor(int col, int row) {
		if (row == -1) {
			return null;
		} else if (row > 0 && col == IED_CHECK_COL) {
			return new KTableCellEditorCheckbox();
		}
		return null;
	}
	
	@Override
	public KTableCellRenderer doGetCellRenderer(int col, int row) {
		if (isFixedCell(col, row))
			return m_fixedRenderer;
		if (col == IED_CHECK_COL) {
			m_CheckableRenderer.setBackground(UIConstants.getRowColor(row));
			return m_CheckableRenderer;
		}
		return super.doGetCellRenderer(col, row);
	}

	@Override
	public int getFixedHeaderColumnCount() {
		return 1;
	}

	@Override
	public int getInitialColumnWidth(int column) {
		if (column == 0 || column == 1) {
			return 50;
		} else if (column == 2 || column == 3) {
			return 100;
		} else if (column == 4) {
			return 180;
		}
		return 120;
	}

	@Override
	public int doGetColumnCount() {
		return COLUMN_COUNT;
	}

	@Override
	public Object doGetContentAt(int col, int row) {
		// 第一行是标题行
		if (row == 0) {
			return head[col];
		} else {
			Object erg = content.get(col + "/" + row); //$NON-NLS-1$
			if (erg != null) {
				return erg;
			}
			if ((row >= 1) && (row < items.size() + 1)) {
				return getValue(col, row);
			} else {
				return ""; //$NON-NLS-1$
			}
		}
	}

	/**
	 * 获取表格值，显示表格
	 * @param col
	 * @param row
	 * @return
	 */
	private Object getValue(int col, int row) {
		Element item = (Element) items.get(row - 1);
		String name = item.attributeValue("name"); //$NON-NLS-1$
		String desc = item.attributeValue("desc"); //$NON-NLS-1$
		String manufacturer = item.attributeValue("manufacturer"); //$NON-NLS-1$
		String type = item.attributeValue("type"); //$NON-NLS-1$
		switch (col) {
			case 0:
				return "" + row;
			case 1:
				return false;
			case 2:
				return name == null ? "" : name; //$NON-NLS-1$
			case 3:
				return type == null ? "" : type; //$NON-NLS-1$
			case 4:
				return desc == null ? "" : desc; //$NON-NLS-1$
			case 5:
				return manufacturer == null ? "" : manufacturer; //$NON-NLS-1$
			default:
				return ""; //$NON-NLS-1$
		}
	}

	@Override
	public void doSetContentAt(int col, int row, Object value) {
		super.doSetContentAt(col, row, value);

	}

	public int doGetRowCount() {
		return items.size() + 1;
	}
}
