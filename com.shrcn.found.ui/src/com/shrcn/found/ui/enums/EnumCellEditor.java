/**
 * Copyright (c) 2007-2010 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based on IEC61850 SCT.
 */
package com.shrcn.found.ui.enums;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.CheckboxCellEditor;
import org.eclipse.jface.viewers.ComboBoxCellEditor;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Table;

import com.shrcn.found.ui.table.CellEditorCCombo;
import com.shrcn.found.ui.table.CellEditorCheckbox;

import de.kupzog.ktable.KTableCellEditor;
import de.kupzog.ktable.editors.KTableCellEditorText;

/**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2010-12-9
 */
/**
 * $Log: EnumCellEditor.java,v $
 * Revision 1.2  2013/04/25 07:32:59  zsy
 * Update:增加enum类型、contains()方法
 *
 * Revision 1.1  2013/03/29 09:37:33  cchun
 * Add:创建
 *
 * Revision 1.4  2012/11/28 12:02:44  cchun
 * Update:添加createKTableEditor(0
 *
 * Revision 1.3  2011/12/13 02:36:02  cchun
 * Update:修改resovleByType()，默认返回Text
 *
 * Revision 1.2  2011/11/23 09:29:37  cchun
 * Update:增加isSame()
 *
 * Revision 1.1  2011/01/04 09:25:59  cchun
 * Refactor:将table框架移动到common项目中
 *
 * Revision 1.2  2010/12/16 02:01:19  cchun
 * Update:避免null
 *
 * Revision 1.1  2010/12/10 02:03:16  cchun
 * Update:增加editor属性
 *
 */
public enum EnumCellEditor {
	
	NONE("none"),
	TEXT("text"),
	ENUM("enum"),
	DICT("dict"),
	COMBO("combobox"),
	CHECK("checkbox");
	
	private String type;
	
	EnumCellEditor(String type) {
		this.type = type;
	}

	public String getType() {
		return type;
	}
	
	public static boolean contains(String type) {
		for (EnumCellEditor editor : values()) {
			if (editor.type.equals(type))
				return true;
		}
		return false;
	}
	
	/**
	 * 根据类型名获得枚举对象
	 * @param type
	 * @return
	 */
	public static EnumCellEditor resovleByType(String type) {
		for (EnumCellEditor editor : values()) {
			if (editor.type.equals(type))
				return editor;
		}
		return TEXT;
	}
	
	public boolean isSame(String t) {
		return type.equals(t);
	}
	
	/**
	 * 创建editor
	 * @param table
	 * @param items
	 * @return
	 */
	public CellEditor createEditor(Table table, String[] items) {
		CellEditor editor = null;
		switch (this) {
			case TEXT:
				editor = new TextCellEditor(table);
				break;
			case CHECK:
				editor = new CheckboxCellEditor(table);
				break;
			case COMBO:
				if (items == null)
					items = new String[]{""};
				editor = new ComboBoxCellEditor(table, items, SWT.READ_ONLY);
				break;
			default:
				break;
		}
		return editor;
	}
	
	/**
	 * 创建ktable编辑控件。
	 * @param items
	 * @return
	 */
	public KTableCellEditor createKTableEditor(String[] items) {
		KTableCellEditor editor = null;
		switch (this) {
			case TEXT:
				editor = new KTableCellEditorText();
				break;
			case CHECK:
				editor = new CellEditorCheckbox();
				break;
			case COMBO:
				if (items == null)
					items = new String[]{""};
				CellEditorCCombo combold = new CellEditorCCombo();
				combold.setItems(items);
				return combold;
			default:
				break;
		}
		return editor;
	}
}
