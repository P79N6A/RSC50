package com.shrcn.found.ui.table;

import java.util.List;

import com.shrcn.found.ui.model.IField;
import com.shrcn.found.ui.model.TableConfig;

public interface IXOTable {

	/**
	 * 获取选中的最小的行号
	 * 
	 * @return
	 */
	public int getSelectRowNum();

	/**
	 * 获取当前选择列号（单元格选择方式下）
	 * @return
	 */
	public int getSelectColNum();

	/**
	 * 得到用户选中的所有行号
	 * @return
	 */
	public int[] getSelectRowNums();
	/**
	 * 得到当前表格选择行数据
	 * @return
	 */
	public Object getSelection();

	/**
	 * 得到当前表格所有选择行数据
	 * @return
	 */
	public List<Object> getSelections();

	/**
	 * 选择指定序号行
	 * @param index
	 */
	public void setSelection(int index);

	/**
	 * 选择一行
	 * @param row
	 */
	public void setSelection(Object row);

	/**
	 * 选择多行
	 * @param rows
	 */
	public void setSelections(List<?> rows);

	/**
	 * 选择多行，并显示。
	 * @param rows
	 * @param reveal
	 */
	public void setSelections(List<?> rows, boolean reveal);

	public void clearSelections();

	public boolean isLoaded();

	public void setLoaded(boolean loaded);

	public void refresh();

	public void redraw();
	
	
	
	// 表格数据处理
	/**
	 * 删除行
	 */
	public int handleDelete();

	/**
	 * 添加行
	 */
	public int handleAdd();
	
	/**
	 * 刷新表格数据
	 */
	public void reloadData();


	public void clear();

	/**
	 * 检查当前输入的单元格数据是否合法
	 * @param newValue
	 * @return
	 */
	public String checkCellValue(Object row, String property,
			Object newValue);

	/**
	 * 保存单元格输入数据
	 * @param row
	 * @param property
	 * @param newValue
	 */
	public void setCellValue(Object row, String property,
			Object newValue);

	/**
	 * 上移
	 */
	public void moveUp(int step);

	/**
	 * 下移
	 */
	public void moveDown(int step);

	/**
	 * 添加空行
	 */
	public void addBlank(int num, int mode);

	/**
	 * 向表格添加数据。
	 * @param data
	 * @param mode
	 */
	public void addRows(List<?> list, int mode);
	
	/**
	 * 添加行对象
	 * 
	 * @param obj
	 */
	public void addRow(Object obj);
	
	/**
	 * 插入行对象
	 * @param obj
	 */
	public void insertRow(Object obj);

	/**
	 * 获取以初始值构造的一行数据
	 */
	public Object getDefaultRow();

	/**
	 * 为行数据设置初始值
	 * @param row
	 */
	public void setDefaultValues(Object row);

	/**
	 * 删除选中记录。
	 */
	public void removeSelected();

	/**
	 * 得到记录行数。
	 * @return
	 */
	public int getItemCount();

	public void setItemCount(int count);

	/**
	 * 获取表格数据
	 * @return
	 */
	public List<?> getInput();

	/**
	 * 设置表格数据
	 * @param input
	 */
	public void setInput(List<?> input);

	public String getTableName();

	public void setTableName(String tableName);

	public String getTableDesc();

	public void setTableDesc(String tableDesc);

	public TableConfig getTableConfig();
	
	public IField[] getFields();

	/**
	 * 根据字段名获取字段对象
	 * @param fieldName
	 * @return
	 */
	public IField getField(String fieldName);
	
	public IField getField(int col);

	/**
	 * 根据field获取相应的editor
	 * @param field
	 * @return
	 */
	public Object getFieldEditor(IField field);

	public void exportExcel(String title);
	
	public void saveCellValue(Object data, String property, Object value);

	/**
	 * 复制
	 */
	void copy();

	/**
	 * 是否为已复制状态
	 * @return
	 */
	boolean hasCopied();

	/**
	 * 粘贴
	 */
	void paste();

	/**
	 * 插入粘贴
	 */
	void pasteInsert();
	
	List<Object> synchColumnValues();
	
	List<Object> incrsColumnValues();

}