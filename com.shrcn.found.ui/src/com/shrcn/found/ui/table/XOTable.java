/**
 * Copyright (c) 2007-2010 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based on IEC61850 SCT.
 */
package com.shrcn.found.ui.table;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import net.sf.excelutils.ExcelUtils;

import org.dom4j.Element;
import org.eclipse.jface.action.Action;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import com.shrcn.found.common.util.ObjectUtil;
import com.shrcn.found.common.util.StringUtil;
import com.shrcn.found.file.excel.ExcelFileManager;
import com.shrcn.found.ui.UICommonConstants;
import com.shrcn.found.ui.model.IField;
import com.shrcn.found.ui.model.TableConfig;
import com.shrcn.found.ui.util.DialogHelper;
import com.shrcn.found.ui.util.FileDialogHelper;

/**
 * 
 * @author 孙春颖(mailto:scy@shrcn.com)
 * @version 1.0, 2014-05-26
 */
public class XOTable implements IXOTable {

	// 加行方式
	public static final int ADD_MODE = 0;
	public static final int INSERT_MODE = 1;
	public static final int REP_MODE = 2;
		
	protected Composite parent;
	
	protected TableConfig config;
	protected String tableName;
	protected String tableDesc;
	protected Class<?> contentClass;
	protected IField[] fields;
	protected IField[] visibleFields;
	protected List<Action> actions = new ArrayList<Action>();
	
	protected boolean loaded = false;
	protected List<Object> copiedValues = null;

	public XOTable(String name, String desc) {
		this.tableName = name;
		this.tableDesc = desc;
	}
	
	public XOTable(Composite parent, TableConfig config){
		this(config.getName(), config.getDesc());
		this.parent = parent;
		this.config = config;
		this.contentClass = ObjectUtil.getClassByName(getClass(), config.getClassName());
		this.fields = config.getFields();
		this.visibleFields = getVisibleFields(fields);
		initUI();
		initTable();
	}

	/**
	 * 用于处理表格绘制构造的其他数据
	 */
	protected void initUI() {
	}
	
	/**
	 * 初始化表格
	 */
	protected void initTable() {
		initEnv();
		initData();
		initColumnProperties();
		initCellEditors();
		initOthers();
		initContextMenu();
	}

	/**
	 * 获取可见列数组
	 * @param fields
	 * @return
	 */
	protected IField[] getVisibleFields(IField[] fields) {
		List<IField> visibleFields = new ArrayList<IField>();
		for (IField field : fields) {
			if (field.isVisible()) {
				visibleFields.add(field);
			}
		}
		return visibleFields.toArray(new IField[visibleFields.size()]);
	}

	protected void initEnv() {}
	
	protected void initData() {
		setInput(new ArrayList<Object>());
	}
	
	protected void initColumnProperties() { }
	protected void initCellEditors() {}
	protected void initOthers() {}
	/**
	 * 上下文菜单
	 */
	protected void initContextMenu() {}

	
	@Override
	public int getSelectRowNum() {
		int[] nums = getSelectRowNums();
		if (nums.length == 0)
			return -1;
		return nums[0];
	}
	
	@Override
	public int getSelectColNum() {
		return -1;
	}

	@Override
	public int[] getSelectRowNums() {
		return null;
	}

	@Override
	public Object getSelection() {
		return null;
	}

	@Override
	public List<Object> getSelections() {
		return null;
	}

	@Override
	public void setSelection(int index) {
	}

	@Override
	public void setSelection(Object row) {
	}

	@Override
	public void setSelections(List<?> rows) {
	}

	@Override
	public void setSelections(List<?> rows, boolean reveal) {
	}

	@Override
	public void clearSelections() {
	}

	@Override
	public boolean isLoaded() {
		return loaded;
	}

	@Override
	public void setLoaded(boolean loaded) {
		this.loaded = loaded;
	}

	/**
	 * 判断用户所选行是否连续
	 * @return
	 */
	protected boolean isContinuous() {
		int[] nums = getSelectRowNums();
		for (int i=0; i<nums.length - 1; i++) {
			if (nums[i] + 1 != nums[i + 1])
				return false;
		}
		return true;
	}

	@Override
	public void refresh() {
	}

	@Override
	public void redraw() {
	}

	@Override
	public int handleDelete() {
		return 0;
	}

	@Override
	public int handleAdd() {
		return 0;
	}

	@Override
	public void reloadData() {
	}

	@Override
	public void clear() {
	}

	@Override
	public String checkCellValue(Object row, String property, Object newValue) {
		return null;
	}

	@Override
	public void setCellValue(Object element, String property, Object newValue) {
		if (element instanceof Map) {
			((Map)element).put(property, newValue);
		} if (element instanceof Element) {
			((Element)element).addAttribute(property, newValue.toString());
		} else {
			ObjectUtil.setProperty(element, property, newValue);
		}
	}

	@Override
	public void moveUp(int step) {
	}

	@Override
	public void moveDown(int step) {
	}

	@Override
	public void addBlank(int num, int mode) {
		List<Object> list = new ArrayList<>();
		for (int i=0; i<num; i++)
			list.add(getDefaultRow());
		addRows(list, mode);
	}

	@Override
	public void addRows(List<?> list, int mode) {
		// 判定不符合添加的条件
		int size = list.size();
		if (size < 1)
			return;
		int[] sels = getSelectRowNums();
		if (mode == REP_MODE && sels.length != size) {
			DialogHelper.showWarning("待替换点数目不一致，操作失败！");
			return;
		}
		
		// 执行操作
		int pos = getSelectRowNum() - 1;
		List<Object> oldData = getTInput();
		if (mode == ADD_MODE || pos < 0) {		// 追加
			oldData.addAll(list);
		} else if (mode == REP_MODE) {		// 替换
			java.util.Arrays.sort(sels);
			List<Object> repPoints = new ArrayList<Object>();
			for (int i = sels.length - 1; i > -1; i--) {
				int index = sels[i] - 1;
				repPoints.add(oldData.get(index));
				oldData.remove(index);
			}
			pos = sels[0] - 1;
			oldData.addAll(pos, list);
		} else if (mode == INSERT_MODE){
			if (sels.length!=1){
				DialogHelper.showWarning("只能选择一行进行插入！");
				return;
			}
			for (int i = 0; i < list.size(); i++) {
				oldData.add(pos + i, list.get(i));
			}
		}
		setInput(oldData);
		refresh();
	}

	@Override
	public Object getDefaultRow() {
		Object row = ObjectUtil.newInstance(contentClass);
		for (IField field : fields) {
			String defaultValue = field.getDefaultValue();
			String fieldName = field.getName();
			if (null != defaultValue && ObjectUtil.existProperty(row, fieldName))
				ObjectUtil.setProperty(row, fieldName, defaultValue);
		}
		return row;
	}

	@Override
	public void setDefaultValues(Object row) {
		for (IField field : fields) {
			String property = field.getName();
			Object defaultValue = field.getDefaultValue();
			Object mapValue = ObjectUtil.getProperty(row, property);
			if(null != mapValue) {
				ObjectUtil.setProperty(row, property, mapValue);
			} else {
				ObjectUtil.setProperty(row, property, defaultValue);
			}
		}
	}

	@Override
	public void removeSelected() {
		List<Object> objs = getSelections();
		for (Object obj : objs) {
			getInput().remove(obj);
		}
		refresh();
	}
	
	@Override
	public int getItemCount() {
		return 0;
	}

	@Override
	public void setItemCount(int count) {
	}

	@Override
	public List<?> getInput() {
		return null;
	}
	
	@SuppressWarnings("unchecked")
	protected List<Object> getTInput() {
		return (List<Object>) getInput();
	}

	@Override
	public void setInput(List<?> input) {
	}

	@Override
	public String getTableName() {
		return tableName;
	}

	@Override
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	@Override
	public String getTableDesc() {
		return tableDesc;
	}

	@Override
	public void setTableDesc(String tableDesc) {
		this.tableDesc = tableDesc;
	}
	
	public List<Action> getActions() {
		return actions;
	}
	
	@Override
	public TableConfig getTableConfig() {
		return config;
	}

	@Override
	public IField[] getFields() {
		return fields;
	}
	
	@Override
	public IField getField(int col) {
		return fields[col];
	}

	@Override
	public IField getField(String fieldName) {
		for (IField f : fields) {
			if (fieldName.equals(f.getName()))
				return f;
		}
		return null;
	}

	@Override
	public Object getFieldEditor(IField field) {
		return null;
	}
	
	protected IField[] getExportFields() {
		return getFields();
	}

	/* (non-Javadoc)
	 * @see com.shrcn.found.ui.table.XOKTable#exportExcel(java.lang.String)
	 */
	@Override
	public void exportExcel(String title) {
		Shell shell = Display.getDefault().getActiveShell();
		final String fileName = FileDialogHelper.selectExcelFile(shell);
		if (fileName == null)
			return;
		Map<String, String> mapTitle = new LinkedHashMap<String, String>();
		mapTitle.put("key0", title);
		Map<String, String> mapSep = new LinkedHashMap<String, String>();
		mapSep.put("key0", "");
		Map<String, String> mapTime = new LinkedHashMap<String, String>();
		mapTime.put("printDate", StringUtil.getCurrentTime("yyyy年MM月dd日HH点mm分ss秒"));
		
		IField[] vfields = getExportFields();
		int fLen = vfields.length;
		String[] fields = new String[fLen];
		for (int i = 0; i < fields.length; i++) {
			IField f = vfields[i];
			fields[i] = f.getTitle();
		}
		
		List<String[]> exportData = new ArrayList<String[]>();
		if (getInput() != null) {
			int index = 1;
			for (Object o : getInput()) {
				String[] row = new String[fLen];
				for (int i=0; i<fLen; i++) {
					IField f = vfields[i];
					String fieldName = f.getName();
					if ("index".equals(fieldName))
						row[i] = "" + index;
					else
						row[i] = "" + ObjectUtil.getProperty(o, fieldName);
				}
				exportData.add(row);
				index++;
			}
		}
		
		ExcelUtils.addValue("title", mapTitle);
		ExcelUtils.addValue("sep", mapSep);
		ExcelUtils.addValue("time", mapTime);
		ExcelUtils.addValue("width", fLen - 1);
		ExcelUtils.addValue("fields", fields);
		ExcelUtils.addValue("data", exportData);
		ExcelFileManager.saveExcelFile(getClass(), UICommonConstants.EXCEL_COMM_EXPORT, fileName);
	}

	@Override
	public void saveCellValue(Object data, String property, Object value) {
	}

	@Override
	public void addRow(Object obj) {
		List<Object> list = new ArrayList<>();
		list.add(obj);
		addRows(list, ADD_MODE);
	}

	@Override
	public void insertRow(Object obj) {
		List<Object> list = new ArrayList<>();
		list.add(obj);
		addRows(list, INSERT_MODE);
	}

	@Override
	public void copy() {
		copiedValues = getSelections();
	}

	@Override
	public boolean hasCopied() {
		return copiedValues!=null && copiedValues.size() > 0;
	}
	
	/**
	 * 获取复制对象（注意：对于非基本类型的属性，只传引用而不复制。
	 * 如需深度复制，可覆盖此方法。）
	 * @return
	 */
	protected List<Object> getDuplicated() {
		List<Object> list = new ArrayList<>();
		for (Object obj : copiedValues) {
			list.add(ObjectUtil.duplicate(obj));
		}
		return list;
	}

	@Override
	public void paste() {
		addRows(getDuplicated(), ADD_MODE);
	}

	@Override
	public void pasteInsert() {
		addRows(getDuplicated(), INSERT_MODE);
	}
	
	/**
	 * 同步列信息.
	 */
	public List<Object> synchColumnValues() {
		List<Object> points = getSelections();
		int psize = points.size();
		if (psize < 1)
			return null;
		IField[] fields = getFields();
		String property = fields[getSelectColNum()].getName();
		Object obj = null;
		if (points.size() > 0) {
			obj = ObjectUtil.getProperty(points.get(0), property);
		}
		for (Object pointobj : points) {
			ObjectUtil.setProperty(pointobj, property, obj);
		}
		refresh();
		return points;
	}
	
	/**
	 * 列递增，分三种情况处理：
	 * 1、选中一个单元格，其后所有单元格的值依次加值为1；
	 * 2、选中二个单元格，其后所有单元格的值依次加值为这两个单元格差值；
	 * 3、选中二个以上单元格，所选中的单元格值按等差增长，增长值为前两个单元格差值。
	 */
	public List<Object> incrsColumnValues() {
		List<Object> points = getSelections();
		int psize = points.size();
		if (psize < 1)
			return null;
		IField[] fields = getFields();
		int selectRow = getSelectRowNum();
		int selectCol = getSelectColNum();
		String property = fields[selectCol].getName();
		Object obj = ObjectUtil.getProperty(points.get(0), property);
		int startInt = Integer.MIN_VALUE, d = 1;
		String prefix = "";
		boolean isStr = (obj instanceof String);
		boolean isInt = false;
		String strValue = obj.toString();
		if (obj instanceof Integer) {	// 整数
			startInt = (Integer) obj;
			isInt = true;
		} else  if (isStr && strValue.matches("^.*\\d+$")) {	// 以数字结尾
			int numIndex = getNumIndex(strValue);
			if (numIndex > -1) {
				if (numIndex > 0)
					prefix = strValue.substring(0, numIndex);
				startInt = Integer.parseInt(strValue.substring(numIndex));
			}
		}
		if (psize > 1) {
			Object obj2 = ObjectUtil.getProperty(points.get(1), property);
			String strValue2 = obj2.toString();
			if (isInt) {
				d = ((Integer)obj2) - startInt;
			} else  if (isStr && strValue2.matches("^.*\\d+$")) {	// 以数字结尾
				int numIndex = getNumIndex(strValue2);
				d = Integer.parseInt(strValue2.substring(numIndex)) - startInt;
			}
			d = (d < 1) ? 1 : d;
		}
		if (Integer.MIN_VALUE != startInt) {
			List<?> rows = points;//(psize < 3) ? getInput() : points;
//			for (int i=selectRow-1; i<rows.size(); i++) {
			for (int i=0; i<rows.size(); i++) {
				Object point = rows.get(i);
				int num = (startInt + i*d);
				Object newValue = isStr ? prefix + num : num;
				ObjectUtil.setProperty(point, property, newValue);
			}
		}

//		if (Integer.MIN_VALUE != startInt) {
//			for (int i=0; i<points.size(); i++) {
//				Object point = points.get(i);
//				int num = (startInt + i*d);
//				ObjectUtil.setProperty(point, property,
//						isStr ? prefix + num : num);
//			}
//		}
		refresh();
		return points;
	}

	private int getNumIndex(String strValue) {
		int numIndex = -1;
		for (int k=strValue.length()-1; k>-1; k--) {
			char charAt = strValue.charAt(k);
			if (charAt<48 || charAt>57) {	// 数字字符
				break;
			} else {
				numIndex = k;
			}
		}
		return numIndex;
	}
}
