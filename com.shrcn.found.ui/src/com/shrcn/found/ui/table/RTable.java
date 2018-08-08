/**
 * Copyright (c) 2007-2009 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based on IEC61850 SCT.
 */
package com.shrcn.found.ui.table;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;

import com.shrcn.found.common.dict.DictManager;
import com.shrcn.found.ui.UIConstants;
import com.shrcn.found.ui.enums.EnumCellEditor;
import com.shrcn.found.ui.model.IField;
import com.shrcn.found.ui.model.TableConfig;
import com.shrcn.found.ui.util.SwtUtil;

/**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2010-3-5
 */
/**
 * $Log: RTUTable.java,v $
 * Revision 1.2  2013/04/06 05:32:55  cchun
 * Refactor:统一使用newInstance()
 *
 * Revision 1.1  2013/03/29 09:54:52  cchun
 * Add:创建
 *
 * Revision 1.14  2013/03/07 06:48:53  cchun
 * Update:添加代码混淆
 *
 * Revision 1.13  2012/11/28 06:09:07  cchun
 * Refactor:优化菜单处理
 *
 * Revision 1.12  2012/11/26 08:40:36  cchun
 * Update:参数使用泛型
 *
 * Revision 1.11  2012/11/14 08:46:49  cchun
 * Update:添加clearSelections()
 *
 * Revision 1.10  2012/11/14 07:38:38  cchun
 * Update:使用setCellValue()给对象赋值
 *
 * Revision 1.9  2012/11/13 12:50:30  cchun
 * Refactor:为addBlank()添加模式参数
 *
 * Revision 1.8  2012/11/13 11:21:38  cchun
 * Update:添加addRows()
 *
 * Revision 1.7  2012/11/13 09:09:37  cchun
 * Update:修改initData()，上下移动，选择处理
 *
 * Revision 1.6  2012/11/12 01:30:12  cchun
 * Update:添加contentClass
 *
 * Revision 1.5  2012/11/08 12:30:46  cchun
 * Update:增加addMenus()和getSelection()
 *
 * Revision 1.4  2012/11/05 07:52:45  cchun
 * Update:重构以便扩展懒加载表格
 *
 * Revision 1.3  2012/10/29 08:55:46  cchun
 * Update:增加removeSelected()
 *
 * Revision 1.2  2012/10/25 11:24:11  cchun
 * Update:使其适应Object
 *
 * Revision 1.1  2012/10/08 09:33:01  cchun
 * Add:远动表格
 *
 * Revision 1.15  2012/09/14 02:31:25  cchun
 * Update:修改initTable()，将initEnv提前
 *
 * Revision 1.14  2012/09/13 11:35:56  cchun
 * Update:增加initEnv()
 *
 * Revision 1.13  2012/09/10 02:23:00  cchun
 * Update:initContextMenu()改成protected
 *
 * Revision 1.12  2012/09/06 08:04:59  cchun
 * Update:改成添加多个空行接口，并增加行选择接口
 *
 * Revision 1.11  2012/08/06 10:37:55  cchun
 * Update:将getSelectRowNum()返回值改成行号
 *
 * Revision 1.10  2011/12/13 02:35:13  cchun
 * Update:为表格增加枚举编辑器
 *
 * Revision 1.9  2011/11/23 09:31:46  cchun
 * Update:修改initCellEditors()使用新的字典处理方式
 *
 * Revision 1.8  2011/08/22 06:33:13  cchun
 * Update:清理注释
 *
 * Revision 1.7  2011/08/03 02:34:07  cchun
 * Update:按单双行分色显示
 *
 * Revision 1.6  2011/05/30 09:50:12  cchun
 * Update:清理引用
 *
 * Revision 1.5  2011/05/27 05:38:54  cchun
 * Update:多选不返回空
 *
 * Revision 1.4  2011/05/26 06:17:13  cchun
 * Refactor:重构单元格值校验接口
 *
 * Revision 1.3  2011/05/13 07:04:03  cchun
 * Update:修改字典字段显示逻辑
 *
 * Revision 1.2  2011/03/21 07:07:32  cchun
 * Update:清理引用
 *
 * Revision 1.1  2011/01/04 09:25:58  cchun
 * Refactor:将table框架移动到common项目中
 *
 * Revision 1.31  2010/12/16 02:02:11  cchun
 * Update:增加字典操作方法
 *
 * Revision 1.30  2010/12/13 02:12:13  cchun
 * Update:去掉注释
 *
 * Revision 1.29  2010/12/10 02:03:15  cchun
 * Update:增加editor属性
 *
 * Revision 1.28  2010/11/30 12:18:49  cchun
 * Update:添加addBlank()
 *
 * Revision 1.27  2010/11/23 06:58:48  cchun
 * Update:添加select row长度为0处理
 *
 * Revision 1.26  2010/11/08 07:33:47  cchun
 * Fix bug: NullPointerException
 *
 * Revision 1.25  2010/10/09 03:01:09  cchun
 * Update:扩充接口
 *
 * Revision 1.24  2010/09/25 05:23:53  cchun
 * Update:添加延时加载标记
 *
 * Revision 1.23  2010/08/27 07:31:28  cchun
 * Update:添加SE定值点导入功能
 *
 * Revision 1.21  2010/08/20 09:41:06  cchun
 * Update:清理引用
 *
 * Revision 1.20  2010/06/17 06:39:04  cchun
 * Update:表格没有默认值的属性自动设为空
 *
 * Revision 1.19  2010/06/02 06:04:34  cchun
 * Refactor:修改方法名
 *
 * Revision 1.18  2010/05/17 05:41:34  cchun
 * Update:增加分割线action
 *
 * Revision 1.17  2010/04/28 05:22:54  cchun
 * Update:添加单元格输入数据校验
 *
 * Revision 1.16  2010/04/21 06:56:41  cchun
 * Update:修改初始值设置逻辑
 *
 * Revision 1.15  2010/04/16 06:51:38  cchun
 * Update:添加visibleFields属性
 *
 * Revision 1.14  2010/04/15 03:27:57  cchun
 * Update:修改getSelection()
 *
 * Revision 1.13  2010/04/14 12:50:10  cchun
 * Update:完善删除功能
 *
 * Revision 1.12  2010/04/14 06:11:51  cchun
 * Update:添加handleDelete()
 *
 * Revision 1.11  2010/04/13 09:13:14  cchun
 * Update:为表格字段配置添加是否可见属性
 *
 * Revision 1.10  2010/04/13 03:26:29  cchun
 * Update:为表格添加右键菜单定制扩展功能
 *
 * Revision 1.9  2010/04/08 12:09:16  cchun
 * Update:单元格修改数据后，保存
 *
 * Revision 1.8  2010/04/08 08:30:41  cchun
 * Update:增加reloadData()
 *
 * Revision 1.7  2010/04/07 07:47:09  cchun
 * Update:完善DPA点表添加功能
 *
 * Revision 1.6  2010/04/06 01:34:44  cchun
 * Update
 *
 * Revision 1.5  2010/03/30 08:30:54  cchun
 * Update:添加系统对话框保存事件
 *
 * Revision 1.4  2010/03/29 11:01:50  cchun
 * Update:添加getDefaultRow(),getSelection()
 *
 * Revision 1.3  2010/03/29 02:37:29  cchun
 * Update:提交
 *
 * Revision 1.2  2010/03/11 08:24:24  cchun
 * Update:完善RTU表格
 *
 * Revision 1.1  2010/03/09 07:37:47  cchun
 * Add:添加远动配置插件
 *
 */
public class RTable extends XOTable {
	
	protected RTableViewer tbViewer;
	protected Table table;
	
	private boolean check;
	
	/**
	 * 构造方法
	 * @param parent
	 * @param tableName
	 * @param tableDesc
	 * @param fields
	 */
	public RTable(Composite parent, TableConfig config) {
		super(parent, config);
		this.check = false;
	}
	
	/**
	 * 构造方法
	 * @param parent
	 * @param tableName
	 * @param tableDesc
	 * @param fields
	 */
	public RTable(Composite parent, TableConfig config, boolean check) {
		super(parent, config);
		this.check = check;
	}
	
	protected void initUI(){
		int style = UIConstants.TABLE_STYLE;
		if (check)
			style = style | SWT.CHECK;
		this.tbViewer = new RTableViewer(parent, visibleFields, style);
	
		// 初始化表格
		table = tbViewer.getTable();
		table.setFont(UIConstants.FONT_CONTENT);
	}

	/**
	 * 上下文菜单
	 */
	protected void initContextMenu() {
		if(actions.size() > 0) {
			SwtUtil.addMenus(table, actions.toArray(new Action[0]));
		}
	}
	
	/**
	 * 初始化列属性名
	 */
	protected void initColumnProperties() {
		int length = visibleFields.length;
		String[] properties = new String[length];
		for(int i=0; i<length; i++)
			properties[i] = visibleFields[i].getName();
		tbViewer.setColumnProperties(properties);
	}
	
	/**
	 * 初始化表格编辑工具
	 */
	protected void initCellEditors() {
		int length = visibleFields.length;
		CellEditor[] cellEditor = new CellEditor[length];
		DictManager dictMgr = DictManager.getInstance();
		for(int i=0; i<length; i++) {
			String editor = visibleFields[i].getEditor();
			String dict = visibleFields[i].getDictType();
			if(!visibleFields[i].isEditAble())
				continue;
			if (dict != null) {
				String[] items = dictMgr.getDictNames(dict);
				cellEditor[i] = EnumCellEditor.COMBO.createEditor(table, items);
			} else {
				cellEditor[i] = EnumCellEditor.resovleByType(editor).createEditor(table, null);
			}
		}
		tbViewer.setCellEditors(cellEditor);
		setCellModifier();
	}

	protected void setCellModifier() {
		tbViewer.setCellModifier(new RCellModifier(this));
	}
	
	/* (non-Javadoc)
	 * @see com.shrcn.found.ui.table.XOTable#clear()
	 */
	@Override
	public void clear() {
		List<Object> input = getInput();
		if(null == input)
			input = new ArrayList<Object>();
		else
			input.clear();
		tbViewer.setInput(input);
	}
	
	/* (non-Javadoc)
	 * @see com.shrcn.found.ui.table.XOTable#getItemCount()
	 */
	@Override
	public int getItemCount() {
		return getTable().getItemCount();
	}
	
	/* (non-Javadoc)
	 * @see com.shrcn.found.ui.table.XOTable#setItemCount(int)
	 */
	@Override
	public void setItemCount(int count) {
		getTable().setItemCount(count);
	}
	
	/* (non-Javadoc)
	 * @see com.shrcn.found.ui.table.XOTable#getSelectRowNums()
	 */
	@Override
	public int[] getSelectRowNums() {
		int[] nums = tbViewer.getTable().getSelectionIndices();
		Arrays.sort(nums);
		return nums;
	}
	
	/* (non-Javadoc)
	 * @see com.shrcn.found.ui.table.XOTable#getInput()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Object> getInput() {
		return (List<Object>)tbViewer.getInput();
	}
	
	/* (non-Javadoc)
	 * @see com.shrcn.found.ui.table.XOTable#setInput(java.util.List)
	 */
	@Override
	public void setInput(List<?> input) {
		tbViewer.setInput(input);
		SwtUtil.setTableItemBgColors(tbViewer.getTable());
	}
	
	/* (non-Javadoc)
	 * @see com.shrcn.found.ui.table.XOTable#refresh()
	 */
	@Override
	public void refresh() {
		tbViewer.refresh();
		SwtUtil.setTableItemBgColors(tbViewer.getTable());
	}
	
	@Override
	public void redraw() {
		tbViewer.getTable().redraw();
		SwtUtil.setTableItemBgColors(tbViewer.getTable());
	}

	/* (non-Javadoc)
	 * @see com.shrcn.found.ui.table.XOTable#getFieldEditor(com.shrcn.found.ui.model.IField)
	 */
	@Override
	public CellEditor getFieldEditor(IField field) {
		CellEditor[] editors = tbViewer.getCellEditors();
		int index = java.util.Arrays.asList(visibleFields).indexOf(field);
		return editors[index];
	}
	
	/* (non-Javadoc)
	 * @see com.shrcn.found.ui.table.XOTable#getSelection()
	 */
	@Override
	public Object getSelection() {
		int index = table.getSelectionIndex();
		if (index > -1 && index < table.getItemCount())
			return table.getItem(index).getData();
		else
			return null;
	}
	
	/* (non-Javadoc)
	 * @see com.shrcn.found.ui.table.XOTable#getSelections()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Object> getSelections() {
		StructuredSelection selection = (StructuredSelection)tbViewer.getSelection();
		return (List<Object>)selection.toList();
	}
	
	/* (non-Javadoc)
	 * @see com.shrcn.found.ui.table.XOTable#setSelection(int)
	 */
	@Override
	public void setSelection(int index) {
		List<Object> input = getInput();
		if (index > -1 && index<input.size())
			setSelection(input.get(index));
	}
	
	/* (non-Javadoc)
	 * @see com.shrcn.found.ui.table.XOTable#setSelection(java.lang.Object)
	 */
	@Override
	public void setSelection(Object row) {
		StructuredSelection selection = new StructuredSelection(row);
		tbViewer.setSelection(selection, false);
	}
	
	/* (non-Javadoc)
	 * @see com.shrcn.found.ui.table.XOTable#setSelections(java.util.List)
	 */
	@Override
	public void setSelections(List<?> rows) {
		StructuredSelection selection = new StructuredSelection(rows);
		tbViewer.setSelection(selection, false);
	}
	
	/* (non-Javadoc)
	 * @see com.shrcn.found.ui.table.XOTable#setSelections(java.util.List, boolean)
	 */
	@Override
	public void setSelections(List<?> rows, boolean reveal) {
		StructuredSelection selection = new StructuredSelection(rows);
		tbViewer.setSelection(selection, reveal);
	}
	
	/* (non-Javadoc)
	 * @see com.shrcn.found.ui.table.XOTable#clearSelections()
	 */
	@Override
	public void clearSelections() {
		StructuredSelection selection = new StructuredSelection(new ArrayList<Object>());
		tbViewer.setSelection(selection, false);
	}

	/* (non-Javadoc)
	 * @see com.shrcn.found.ui.table.XOTable#getTableViewer()
	 */
	public RTableViewer getTableViewer() {
		return this.tbViewer;
	}
	
	/* (non-Javadoc)
	 * @see com.shrcn.found.ui.table.XOTable#getTable()
	 */
	public Table getTable() {
		return this.tbViewer.getTable();
	}
	
	public RTableViewer getTbViewer() {
		return tbViewer;
	}
	
	public void del(Object obj) {
		getInput().remove(obj);
	}
}
