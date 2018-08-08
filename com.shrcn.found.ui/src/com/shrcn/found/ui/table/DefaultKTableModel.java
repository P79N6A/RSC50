/**
 * Copyright (c) 2007-2013 上海思弘瑞电力控制技术有限公司. All rights reserved. 
 * This program is an eclipse Rich Client Application
 * designed for IED configuration and debuging.
 */
package com.shrcn.found.ui.table;

import static de.kupzog.ktable.renderers.DefaultCellRenderer.INDICATION_CLICKED;
import static de.kupzog.ktable.renderers.DefaultCellRenderer.INDICATION_COMMENT;
import static de.kupzog.ktable.renderers.DefaultCellRenderer.INDICATION_FOCUS_ROW;
import static de.kupzog.ktable.renderers.DefaultCellRenderer.STYLE_FLAT;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.shrcn.found.ui.UIConstants;

import de.kupzog.ktable.KTableCellEditor;
import de.kupzog.ktable.KTableCellRenderer;
import de.kupzog.ktable.KTableDefaultModel;
import de.kupzog.ktable.SWTX;
import de.kupzog.ktable.editors.KTableCellEditorText;
import de.kupzog.ktable.renderers.CheckableCellRenderer;
import de.kupzog.ktable.renderers.HeaderCellRenderer;
import de.kupzog.ktable.renderers.TextCellRenderer;

/**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2014-1-2
 */
public class DefaultKTableModel extends KTableDefaultModel {

	/** 默认值 */
	// 缺省列宽
	private static final int INITIAL_COLUMN_WIDTH = 55;
	// 缺省行高
	private static final int INITIAL_ROW_HEIGHT = 23;
	
	/** 单元格显示方式 */
	// 标题
	protected HeaderCellRenderer m_fixedRenderer = 
								new HeaderCellRenderer(STYLE_FLAT | INDICATION_FOCUS_ROW);
	// 文本
	protected TextCellRenderer m_textRenderer = new TextCellRenderer(INDICATION_FOCUS_ROW);
	protected TextCellRenderer m_textRendererComm = new TextCellRenderer(INDICATION_FOCUS_ROW | INDICATION_COMMENT);
	// 复选框
	protected CheckableCellRenderer m_checkableRenderer = 
								new CheckableCellRenderer(INDICATION_CLICKED | INDICATION_FOCUS_ROW);
	// 图片
	protected ImageCellRenderer m_imageRender = new ImageCellRenderer(INDICATION_FOCUS_ROW, 16);
	protected ImageCellRenderer m_imageRenderComm = new ImageCellRenderer(INDICATION_FOCUS_ROW | INDICATION_COMMENT, 16);
	
	protected Map<String, Object> content = new HashMap<String, Object>();
	@SuppressWarnings("rawtypes")
	public List items;
	private int totalColumn = 1;
	
	public DefaultKTableModel() {
		m_fixedRenderer.setAlignment(SWTX.ALIGN_HORIZONTAL_CENTER
				| SWTX.ALIGN_VERTICAL_CENTER);
		m_checkableRenderer.setAlignment(SWTX.ALIGN_HORIZONTAL_CENTER
				| SWTX.ALIGN_VERTICAL_CENTER);
		initialize();
	}
	
	@Override
	public void initialize() {
		super.initialize();
		items = new ArrayList<Object>();
	}
	
	@Override
	public Object doGetContentAt(int col, int row) {
		return content.get(col + "/" + row); //$NON-NLS-1$
	}
	
	@Override
	public void doSetContentAt(int col, int row, Object value) {
		content.put(col + "/" + row, value); //$NON-NLS-1$
	}
	
	@Override
	public KTableCellEditor doGetCellEditor(int col, int row) {
		if (col < getFixedColumnCount() || row < getFixedRowCount())
			return null;
		else 
			return new KTableCellEditorText();
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see de.kupzog.ktable.KTableDefaultModel#doGetRowCount() 作用:KTable的行数
	 */
	public int doGetRowCount() {
		int size = items==null ? 0 : items.size();
		return size + getFixedRowCount();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.kupzog.ktable.KTableDefaultModel#doGetColumnCount() 作用:列数
	 */
	public int doGetColumnCount() {
		return totalColumn;
	}

	// Rendering
	public KTableCellRenderer doGetCellRenderer(int col, int row) {
		if (row < getFixedHeaderRowCount())
			return m_fixedRenderer;
		if (isFixedCell(col, row))
			return m_fixedRenderer;
		m_fixedRenderer.setBackground(UIConstants.getRowColor(row));
		m_textRenderer.setBackground(UIConstants.getRowColor(row));
		return m_textRenderer;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.kupzog.ktable.KTableDefaultModel#getInitialColumnWidth(int)
	 */
	public int getInitialColumnWidth(int column) {
		return INITIAL_COLUMN_WIDTH;
	}

	@Override
	public int getRowHeightMinimum() {
		return INITIAL_ROW_HEIGHT;
	}
	
	@Override
	public int getInitialRowHeight(int row) {
		return INITIAL_ROW_HEIGHT;
	}

	@Override
	public int getFixedHeaderColumnCount() {
		return 1;
	}

	@Override
	public int getFixedHeaderRowCount() {
		return 1;
	}

	@Override
	public int getFixedSelectableColumnCount() {
		return 0;
	}

	@Override
	public int getFixedSelectableRowCount() {
		return 0;
	}

	@Override
	public boolean isColumnResizable(int col) {
		return true;
	}

	@Override
	public boolean isRowResizable(int row) {
		return false;
	}
	
	public Object getItem(int row) {
		return items.get(getRowDataIndex(row));
	}
	
	public List<?> getItems() {
		return items;
	}

	public void setItems(List<?> items) {
		this.items = items;
		content.clear();
	}

	public Map<String, Object> getContent() {
		return content;
	}
	
	/**
	 * 得到行数据序号
	 * @param row
	 * @return
	 */
	public int getRowDataIndex(int row) {
		return row - getFixedRowCount();
	}
	
	@SuppressWarnings("unchecked") //$NON-NLS-1$
	public void addItem(Object item) {
		items.add(item);
		content.clear();
	}

	public void deleteItem(int row) {
		int i = getRowDataIndex(row);
		if (i < items.size())
			items.remove(i);
		content.clear();
	}

	@SuppressWarnings("unchecked")
	public void move(int row, int newRow) {
		if (newRow < getFixedRowCount() || newRow > getRowCount() - 1)
			return;
		int newIndex = getRowDataIndex(newRow);
		int index = getRowDataIndex(row);
		Object item = items.get(index);
		items.remove(index);
		items.add(newIndex, item);
		content.clear();
	}

	@SuppressWarnings("unchecked") //$NON-NLS-1$
	public void insertNewItem(int row, Object item) {
		int i = getRowDataIndex(row);
		if (i < items.size())
			items.add(i, item);
		else if (items != null)
			items.add(item);
		content.clear();
	}

	public void deleteAllItem() {
		items.clear();
		content.clear();
	}

	public boolean isColMode() {
		
		return false;
	}

	public void setColModeStart(int colModeStart) {
		
		
	}

	public void setColModeLength(int colModeLength) {
		
		
	}

	public void setCurrentColumn(int selectColumn) {
		
		
	}

	public void backupData() {
		
		
	}

	public void setNeedBackup(boolean b) {
		
		
	}

	public void setColMode(boolean b) {
		
		
	}

	public String getColumnHeader(int i) {
		
		return null;
	}
}
