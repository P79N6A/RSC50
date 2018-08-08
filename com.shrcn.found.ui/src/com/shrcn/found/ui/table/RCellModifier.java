/**
 * Copyright (c) 2007-2009 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based on IEC61850 SCT.
 */
package com.shrcn.found.ui.table;

import java.util.List;

import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.widgets.TableItem;

import com.shrcn.found.common.dict.DictManager;
import com.shrcn.found.common.util.ObjectUtil;
import com.shrcn.found.ui.enums.EnumCellEditor;
import com.shrcn.found.ui.model.IField;
import com.shrcn.found.ui.util.DialogHelper;
import com.shrcn.found.ui.util.UICheckUtil;

public class RCellModifier implements ICellModifier {
	
	protected RTable table;
	protected RTableViewer tv;
	
	public RCellModifier(RTable table) {
		this.table = table;
		this.tv = table.tbViewer;
	}
	
	/**
	 * 根据属性名查找相应的field对象
	 */
	protected IField getFieldByAttName(String property) {
		IField[] fields = (IField[]) tv.getFields();
		IField field = null;
		for (IField f : fields) {
			if (property.equals(f.getName())) {
				field = f;
				break;
			}
		}
		return field;
	}

	@Override
	public boolean canModify(Object element, String property) {
		return true;
	}
	
	/**
	 * 根据列表名称获取相应的序号
	 * @param value
	 * @return
	 */
	protected int getComboIndex(IField field, String value) {
		CCombo combo = (CCombo)table.getFieldEditor(field).getControl();
		List<String> items = java.util.Arrays.asList(combo.getItems());
		String dictName = field.getDictType();
		String text = (dictName == null) ?  value :
			DictManager.getInstance().getNameById(dictName, value);
		return items.indexOf(text);
	}
	
	/**
	 * 根据序号获取列表值
	 * @param field
	 * @param index
	 * @return
	 */
	protected String getComboText(IField field, int index) {
		CCombo combo = (CCombo) table.getFieldEditor(field).getControl();
		String text = combo.getItem(index);
		String dictName = field.getDictType();
		return (dictName == null) ? text : 
			DictManager.getInstance().getIdByName(dictName, text);
	}

	@Override
	public Object getValue(Object element, String property) {
		Object value = ObjectUtil.getProperty(element, property);
		if (value == null) {
			if (ObjectUtil.isInteger(element, property))
				value = 0;
			else
				value = "";
		}
		IField field = getFieldByAttName(property);
		String dictName = field.getDictType();
		String editor = field.getEditor();
		if (dictName != null || EnumCellEditor.COMBO.isSame(editor)) {    // ComboBox
			return getComboIndex(field, (String) value);
		} else {
			if (EnumCellEditor.CHECK.isSame(editor)) {                   // CheckBox
				return value == null ? false : Boolean.valueOf(value.toString());
			}
			return value.toString();
		}
	}

	@Override
	public void modify(Object element, final String property, Object value) {
		TableItem item = (TableItem)element;
		if(item == null || null == value)
			return;
		final Object data = item.getData();
		final Object oldValue = ObjectUtil.getProperty(data, property);
		Object newValue = null;
		IField field = getFieldByAttName(property);
		String dictName = field.getDictType();
		String editor = field.getEditor();
		String msg = UICheckUtil.checkDataType(field, value.toString());
		if (EnumCellEditor.TEXT.isSame(editor) && msg != null) {
			DialogHelper.showWarning(msg);
			return;
		}
		if((dictName != null || EnumCellEditor.COMBO.isSame(editor))
				&& value instanceof Integer) {			// ComboBox
			Integer intValue = (Integer)value;
			int id = intValue.intValue();
			if(id < 0)
				return;
			newValue = getComboText(field, id);
		} else {
			if (value instanceof Boolean)				// CheckBox
				newValue = "" + value;
			else
				newValue = value.toString();
		}
		
		if (oldValue != null && oldValue.equals(newValue))
			return;
		msg = table.checkCellValue(data, property, newValue);
		if (msg != null) {
			DialogHelper.showWarning(msg);
			return;
		}
		table.setCellValue(data, property, newValue);
		tv.update(data, null);
		table.setSelection(data);

		updateValue(data, property);
	}

	public void updateValue(Object data, String property){
	}
}
