/**
 * Copyright (c) 2007-2010 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based on IEC61850 SCT.
 */
package com.shrcn.found.ui.table;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.excelutils.ExcelException;
import net.sf.excelutils.ExcelUtils;
import net.sf.excelutils.WorkbookUtils;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.eclipse.jface.action.Action;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import com.shrcn.found.common.util.ObjectUtil;
import com.shrcn.found.common.util.StringUtil;
import com.shrcn.found.file.excel.ExcelFileManager;
import com.shrcn.found.file.excel.ExcelManager2007;
import com.shrcn.found.file.excel.ExcelUtil;
import com.shrcn.found.ui.UICommonConstants;
import com.shrcn.found.ui.UIConstants;
import com.shrcn.found.ui.model.IField;
import com.shrcn.found.ui.model.TableConfig;
import com.shrcn.found.ui.util.DialogHelper;
import com.shrcn.found.ui.util.FileDialogHelper;
import com.shrcn.found.ui.util.SwtUtil;

import de.kupzog.ktable.KTableCellEditor;

/**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2012-11-28
 */
/**
 * $Log: RKTable.java,v $
 * Revision 1.15  2013/11/01 07:42:32  cchun
 * Update:更新excel模板，去掉自动合并单元格
 *
 * Revision 1.14  2013/10/18 06:20:47  cchun
 * Update:添加exportExcel()
 *
 * Revision 1.13  2013/09/11 07:56:56  cchun
 * Update:完成行添加、删除
 *
 * Revision 1.12  2013/07/22 05:46:04  cchun
 * Update:实现moveUp(),moveDown()
 *
 * Revision 1.11  2013/07/11 00:47:50  scy
 * Update：对默认值设置条件修改
 *
 * Revision 1.10  2013/06/19 12:26:40  cchun
 * Update:去掉addMenus()
 *
 * Revision 1.9  2013/06/07 09:41:23  cchun
 * Update：修改tablemodel类型
 *
 * Revision 1.8  2013/05/20 03:14:18  cchun
 * Refactor:增加添加、删除行数handleDelete()
 *
 * Revision 1.7  2013/05/13 06:33:24  cxc
 * Update:修改菜单状态
 *
 * Revision 1.6  2013/04/18 00:05:34  scy
 * Update：增加getTablemodel方法
 *
 * Revision 1.4  2013/04/12 09:13:03  scy
 * Update：增加处理添加方法
 *
 * Revision 1.3  2013/04/07 12:27:35  cchun
 * Update:完成基础界面框架
 *
 * Revision 1.2  2013/04/06 05:32:55  cchun
 * Refactor:统一使用newInstance()
 *
 * Revision 1.1  2013/03/29 09:54:55  cchun
 * Add:创建
 *
 * Revision 1.5  2013/03/07 06:48:53  cchun
 * Update:添加代码混淆
 *
 * Revision 1.4  2012/12/06 04:49:04  cchun
 * Update:getDefaultRow()排除ref列
 *
 * Revision 1.3  2012/12/06 02:43:01  cchun
 * Fix Bug:修复点表行选择方法setSelections()缺陷
 *
 * Revision 1.2  2012/11/29 13:31:46  cchun
 * Update:去掉resize监听
 *
 * Revision 1.1  2012/11/28 12:04:02  cchun
 * Add:ktable复用类
 *
 */
public class RKTable extends XOTable {
	
	protected DefaultKTable table;
	protected RKTableModel tablemodel;
	
	/**
	 * 构造方法
	 * @param parent
	 * @param tableName
	 * @param tableDesc
	 * @param fields
	 */
	public RKTable(Composite parent, TableConfig config) {
		super(parent, config);
	}

	protected void initUI() {
		tablemodel = new RKTableModel(this, config);
		table = new DefaultKTable(parent, tablemodel);	
	}
	
	/**
	 * 上下文菜单
	 */
	protected void initContextMenu() {
		if (actions.size() > 0) {
			SwtUtil.addMenus(table, actions.toArray(new Action[0]));
		}
	}

	protected void initEnv() {}
	protected void initOthers() {}
	
	
	/* (non-Javadoc)
	 * @see com.shrcn.found.ui.table.XOKTable#handleDelete()
	 */
	@Override
	public int handleDelete() {
		int[] sels = getSelectRowNums();
		if (sels.length > 0) {
			int index = 0;
			for (int i=sels.length - 1; i>-1; i--) {
				index = sels[i];
				table.deleteItem(index);
			}
			DefaultKTableModel model = (DefaultKTableModel) table.getModel();
			int rowCount = model.getRowCount();
			if (index < rowCount) {
				table.setSelection(1, index , true);
			} else if (rowCount > 1) {
				table.setSelection(1, rowCount - 1, true);
			}
			return sels.length;
		}
		return 0;
	}
	
	/* (non-Javadoc)
	 * @see com.shrcn.found.ui.table.XOKTable#handleAdd()
	 */
	@Override
	public int handleAdd() {
		table.addItem(getDefaultRow());
		table.setSelection(1, table.getModel().getRowCount(), true);
		return 1;
	}
	
	/* (non-Javadoc)
	 * @see com.shrcn.found.ui.table.XOKTable#clear()
	 */
	@Override
	public void clear() {
		tablemodel.getItems().clear();
		tablemodel.getContent().clear();
		table.clearSelection();
		table.redraw();
	}
	/* (non-Javadoc)
	 * @see com.shrcn.found.ui.table.XOKTable#moveUp(int)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void moveUp(int step) {
		if (step < 0)
			return;
		if (!isContinuous()) {
			DialogHelper.showWarning("移动项必须连续！");
			return;
		}
		List<Object> inputData = (List<Object>)getInput();
		int[] selNums = getSelectRowNums();
		int length = selNums.length;
		if (length == 0) // 未选择
			return;
		int selFirst = selNums[0] - 1;
		if (selFirst == 0
				|| selFirst-step < 0) {  // 非法移动
			DialogHelper.showWarning("无效移动！");
			return;
		}
		List<Object> nextObjs = getMoveObjects(selFirst, length);
		for (int i=0; i<length; i++) {
			Object nextObj = nextObjs.get(i);
			inputData.add(selFirst - step + i, nextObj);
		}
		refresh();
		List<Point> selections = new ArrayList<Point>();
		for (int i=selFirst-step; i< selFirst-step + length; i++) {
			selections.add(new Point(2, i+1));
		}
		table.setSelection(selections.toArray(new Point[0]), true);
	}
	
	/* (non-Javadoc)
	 * @see com.shrcn.found.ui.table.XOKTable#moveDown(int)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void moveDown(int step) {
		if (step < 0)
			return;
		if (!isContinuous()) {
			DialogHelper.showWarning("移动项必须连续！");
			return;
		}
		List<Object> inputData = (List<Object>) getInput();
		int[] selNums = getSelectRowNums();
		int length = selNums.length;
		if (length == 0) // 未选择
			return;
		int selFirst = selNums[0] - 1;
		int selLast = selNums[length - 1] - 1;
		int maxIndex = inputData.size() - 1;
		if (selLast == maxIndex
				|| selLast+step > maxIndex) { // 非法移动
			DialogHelper.showWarning("无效移动！");
			return;
		}
		List<Object> nextObjs = getMoveObjects(selFirst, length);
		for (int i=0; i<length; i++) {
			Object nextObj = nextObjs.get(i);
			inputData.add(selFirst + step + i, nextObj);
		}
		refresh();
		List<Point> selections = new ArrayList<Point>();
		for (int i=selFirst+step; i< selFirst+step + length; i++) {
			selections.add(new Point(2, i+1));
		}
		table.setSelection(selections.toArray(new Point[0]), true);
	}
	
	private List<Object> getMoveObjects(int offset, int num) {
		List<?> inputData = getInput();
		List<Object> nextObjs = new ArrayList<Object>();
		for (int i=0; i<num; i++) {
			Object nextRow = inputData.get(offset);
			inputData.remove(nextRow);
			nextObjs.add(nextRow);
		}
		return nextObjs;
	}
	
	/* (non-Javadoc)
	 * @see com.shrcn.found.ui.table.XOKTable#getSelectRowNums()
	 */
	@Override
	public int[] getSelectRowNums() {
		return isCellSel() ? getSelectCellRows() : getRowSelectNums();
	}

	/**
	 * @return
	 */
	private int[] getRowSelectNums() {
		int[] nums = table.getRowSelection();
		Arrays.sort(nums);
		return nums;
	}
	
	/**
	 * 判断用户所选行是否连续
	 * @return
	 */
	protected boolean isContinuous() {
		int[] nums = isCellSel() ? getSelectCellRows() : getSelectRowNums();

		for (int i = 0; i < nums.length - 1; i++) {
			if (nums[i] + 1 != nums[i + 1])
				return false;
		}
		return true;
	}

	/**
	 * 是否为单元格选择(true：单元格，FALSE：行选).
	 * 
	 * @return
	 */
	protected boolean isCellSel() {
		return UIConstants.KTABLE_CELL_STYLE == table.getStyle();
	}

	@Override
	public int getSelectColNum() {
		Point[] pointRows = table.getCellSelection();
		if (pointRows == null)
			return -1;
		List<Integer> nums = new ArrayList<Integer>();
		for (Point point : pointRows) {
			nums.add(point.x);
		}
		if (nums.size() == 0)
			return -1;
		Integer[] rows = nums.toArray(new Integer[0]);
		return rows[0];
	}
	
	//按列选择取得用户选择的行号
	private int[] getSelectCellRows() {
		Set<Integer> nums = new HashSet<Integer>();
		Point[] pointRows = table.getCellSelection();
		for (Point point : pointRows) {
			nums.add(point.y);
		}
		Integer[] iRows = nums.toArray(new Integer[0]);
		int[] rows = new int[iRows.length];
		int i = 0;
		for (int j : iRows) {
			rows[i++] = j;
		}
		Arrays.sort(rows);
		return rows;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.shrcn.found.ui.table.XOKTable#getInput()
	 */
	@Override
	public List<?> getInput() {
		return tablemodel.getItems();
	}
	
	/* (non-Javadoc)
	 * @see com.shrcn.found.ui.table.XOKTable#setInput(java.util.List)
	 */
	@Override
	public void setInput(List<?> input) {
		tablemodel.setItems(input);
		refresh();
	}
	
	/* (non-Javadoc)
	 * @see com.shrcn.found.ui.table.XOKTable#refresh()
	 */
	@Override
	public void refresh() {
		tablemodel.getContent().clear();
		table.clearSelection();
		table.redraw();
	}
	
	@Override
	public void redraw(){
		tablemodel.getContent().clear();
		table.redraw();
	}
	
	/**
	 * 根据field获取相应的editor
	 * @param field
	 * @return
	 */
	@Override
	public KTableCellEditor getFieldEditor(IField field) {
		int index = java.util.Arrays.asList(visibleFields).indexOf(field);
		return tablemodel.doGetCellEditor(index, 2);
	}
	
	/* (non-Javadoc)
	 * @see com.shrcn.found.ui.table.XOKTable#getSelection()
	 */
	@Override
	public Object getSelection() {
		List<Object> selections = getSelections();
		if (selections.size()>0)
			return selections.get(0);
		return null;
	}
	
	/* (non-Javadoc)
	 * @see com.shrcn.found.ui.table.XOKTable#getSelections()
	 */
	@Override
	public List<Object> getSelections() {
		return isCellSel() ? getCellSelections() : getRowSelections();
	}

	/**
	 * @return
	 */
	private List<Object> getRowSelections() {
		int[] rows = getSelectRowNums();
		int headRowNum = tablemodel.getFixedRowCount();
		List<Object> selections = new ArrayList<Object>();
		for (int i=0; i<rows.length; i++) {
			int index = rows[i]-headRowNum;
			if (index > -1)
				selections.add(tablemodel.getItems().get(index));
		}
		return selections;
	}
	
	private List<Object> getCellSelections() {
		int[] rows = getSelectCellRows();
		int headRowNum = tablemodel.getFixedRowCount();
		List<Object> selections = new ArrayList<Object>();
		for (int i=0; i<rows.length; i++) {
			selections.add(tablemodel.getItems().get(rows[i]-headRowNum));
		}
		return selections;
	}
	
	/* (non-Javadoc)
	 * @see com.shrcn.found.ui.table.XOKTable#setSelection(int)
	 */
	@Override
	public void setSelection(int index) {
		table.setSelection(2, index, false);
	}
	
	/* (non-Javadoc)
	 * @see com.shrcn.found.ui.table.XOKTable#setSelection(java.lang.Object)
	 */
	@Override
	public void setSelection(Object row) {
		setSelection(getRowIndex(row) + 1);
	}
	
	protected int getRowIndex(Object row) {
		return tablemodel.getItems().indexOf(row);
	}
	
	/* (non-Javadoc)
	 * @see com.shrcn.found.ui.table.XOKTable#setSelections(java.util.List)
	 */
	@Override
	public void setSelections(List<?> rows) {
		List<Point> selections = new ArrayList<Point>();
		for (Object row : rows) {
			selections.add(new Point(2, getRowIndex(row) + tablemodel.getFixedRowCount()));
		}
		table.setSelection(selections.toArray(new Point[0]), true);
	}
	
	/* (non-Javadoc)
	 * @see com.shrcn.found.ui.table.XOKTable#clearSelections()
	 */
	@Override
	public void clearSelections() {
		table.clearSelection();
	}
	
	/* (non-Javadoc)
	 * @see com.shrcn.found.ui.table.XOTable#getItemCount()
	 */
	@Override
	public int getItemCount() {
		return getInput().size();
	}
	
	public DefaultKTable getTable() {
		return table;
	}

	public DefaultKTableModel getTablemodel() {
		return tablemodel;
	}
	
	/**
	 * 设置KTBLE的风格
	 * @param tableStyle
	 */
	public void setTableStyle(int tableStyle){
		table.setStyle(tableStyle);
	}

	public void importExcel() {
		Shell shell = Display.getDefault().getActiveShell();
		final String fileName = FileDialogHelper.selectExcelFile2007(shell,SWT.OPEN | SWT.SINGLE);
		if (fileName == null)
			return;
		if (fileName.endsWith(".xls")){//excel97格式
			HSSFWorkbook book;
			try {
				book = WorkbookUtils.openWorkbook(fileName);
				if (book==null){
					return;
				}
				HSSFSheet sheet = book.getSheetAt(0);
				int maxRow = sheet.getPhysicalNumberOfRows();
				int maxCol = visibleFields.length;
				for (int i = 0; i < maxRow; i++) {
					Object obj = getDefaultRow();
					HSSFRow row = sheet.getRow(i);
					for (int j = 0; j < maxCol - 1; j++) {
						HSSFCell cell = row.getCell((short) (j));
						String value = ExcelUtil.getCellValue(cell);
						if (value.equals("")){
							continue;
						}
						ObjectUtil.setProperty(obj, visibleFields[j+1].getName(), value);
					}
					getTInput().add(obj);
				}
			} catch (ExcelException e) {
				e.printStackTrace();
			}
			
		} else if (fileName.endsWith(".xlsx")){//excel2007格式
			XSSFWorkbook book = ExcelManager2007.getWorkbook(fileName);
			if (book==null){
				return;
			}
			XSSFSheet sheet = ExcelManager2007.getSheet(book, 0);
			int maxRow = sheet.getPhysicalNumberOfRows();
			int maxCol = visibleFields.length;
			for (int i = 0; i < maxRow; i++) {
				Object obj = getDefaultRow();
				for (int j = 0; j < maxCol -1; j++) {
					String value = ExcelManager2007.getStringValue(sheet, j, i);
					if (value.equals("")){
						continue;
					}
					ObjectUtil.setProperty(obj, visibleFields[j+1].getName(), value);
				}
				getTInput().add(obj);
			}
		}
		table.redraw();
	}

	public void importExcel(int rowStart ,int colStart) {
		Shell shell = Display.getDefault().getActiveShell();
		final String fileName = FileDialogHelper.selectExcelFile2007(shell,SWT.OPEN | SWT.SINGLE);
		if (fileName == null)
			return;
		if (fileName.endsWith(".xls")){//excel97格式
			HSSFWorkbook book;
			try {
				book = WorkbookUtils.openWorkbook(fileName);
				if (book==null){
					return;
				}
				HSSFSheet sheet = book.getSheetAt(0);
				int maxRow = sheet.getPhysicalNumberOfRows();
				int maxCol = visibleFields.length;
				for (int i = rowStart ; i < maxRow; i++) {
					Object obj = getDefaultRow();
					HSSFRow row = sheet.getRow(i);
					for (int j = colStart-1 ; j < maxCol + 1; j++) {
						HSSFCell cell = row.getCell((short) (j));
						String value = ExcelUtil.getCellValue(cell);
						if (value.equals("")){
							continue;
						}
						ObjectUtil.setProperty(obj, visibleFields[j-1].getName(), value);
					}
					getTInput().add(obj);
				}
			} catch (ExcelException e) {
				e.printStackTrace();
			}
			
		} else if (fileName.endsWith(".xlsx")){//excel2007格式
			XSSFWorkbook book = ExcelManager2007.getWorkbook(fileName);
			if (book==null){
				return;
			}
			XSSFSheet sheet = ExcelManager2007.getSheet(book, 0);
			int maxRow = sheet.getPhysicalNumberOfRows();
			int maxCol = visibleFields.length;
			for (int i = rowStart; i < maxRow; i++) {
				Object obj = getDefaultRow();
				for (int j = colStart-1; j < maxCol +1; j++) {
					String value = ExcelManager2007.getStringValue(sheet, j, i);
					if (value.equals("")){
						continue;
					}
					ObjectUtil.setProperty(obj, visibleFields[j-1].getName(), value);
				}
				getTInput().add(obj);
			}
		}
		table.redraw();
	}
	
	/**
	 * 设置表格序号是否从0开始
	 * @param isFromZero
	 */
	public void setFromZero(boolean isFromZero) {
		tablemodel.setFromZero(isFromZero);
	}
}
