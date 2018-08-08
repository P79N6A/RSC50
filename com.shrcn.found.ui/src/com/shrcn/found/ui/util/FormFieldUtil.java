package com.shrcn.found.ui.util;

import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;

import com.shrcn.business.ui.IValueSelector;
import com.shrcn.business.ui.NetPortUtil;
import com.shrcn.found.common.dict.DictManager;
import com.shrcn.found.ui.model.IField;

/**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2012-10-29
 */
/**
 * $Log: FormFieldUtil.java,v $
 * Revision 1.4  2013/09/26 12:54:38  cchun
 * Update:支持combobox
 *
 * Revision 1.3  2013/07/22 05:45:26  cchun
 * Refactor:定义"netSelector"常量
 *
 * Revision 1.2  2013/07/18 13:39:13  cchun
 * Udpate:增加panel编辑方式
 *
 * Revision 1.1  2013/03/29 09:55:14  cchun
 * Add:创建
 *
 * Revision 1.3  2012/10/29 08:54:00  cchun
 * Update:添加注释
 *
 */
public class FormFieldUtil {

	/**
	 * 根据field得出Control是何种类型，如Text,checkbox...,再Control的值读出，留作保存使用
	 * @param control
	 * @param field
	 * @return
	 */
	public static String getControlValue(Control control, IField field) {
		String input = field.getEditor();
		if (field.isEnum()) {
			if ("checkbox".equalsIgnoreCase(input)) {
				String enumType = field.getDictType();
				return DictManager.getInstance().getNameById(enumType, String.valueOf(((Button) control).getSelection()));
			} else {
				return ((Combo) control).getText();
			}
		} else if (field.isDict()) {
			String dict = field.getDictType();
			if ("checkbox".equalsIgnoreCase(input)) {
				return DictManager.getInstance().getNameById(dict, String.valueOf(((Button) control).getSelection()));
			} else {
				String text = ((Combo) control).getText();
				return DictManager.getInstance().getIdByName(dict, text);
			}
		} else {
			if ("text".equalsIgnoreCase(input) || "password".equalsIgnoreCase(input)) {
				return ((Text) control).getText();
			} else if ("combo".equalsIgnoreCase(input)
					|| "combobox".equalsIgnoreCase(input)) {
				return ((Combo) control).getText();
			} else if (input.startsWith(NetPortUtil.NET_SELECTOR) || input.startsWith("valueSelector")) {
				if (control instanceof IValueSelector) {
					IValueSelector selector = (IValueSelector) control;
					Object value = selector.getValue();
					if (value != null)
						return value.toString();
				}
			} else if ("checkbox".equalsIgnoreCase(input)) {
				return "" + ((Button) control).getSelection();
			}
		}
		return "";
	}

	/**
	 * 根据field得出Control是何种类型，如Text,checkbox...,再将值写入Control，在界面上显示出来
	 * @param control
	 * @param field
	 * @param value
	 */
	public static void setControlValue(Control control, IField field, String value) {
		if(value == null)
			return;
		String input = field.getEditor();
		if (field.isEnum()) {
			if ("checkbox".equalsIgnoreCase(input)) {
				String enumType = field.getDictType();
				((Button) control).setSelection(DictManager.getInstance().getBoolIdByName(enumType, value));
			} else {
				((Combo) control).setText(value);
			}
		} else  if (field.isDict()) {
			String dict = field.getDictType();
			if ("checkbox".equalsIgnoreCase(input)) {
				((Button) control).setSelection(DictManager.getInstance().getBoolIdByName(dict, value));
			} else {
				((Combo) control).setText(DictManager.getInstance().getNameById(dict, value));
			}
		} else {
			if ("text".equalsIgnoreCase(input) || "password".equalsIgnoreCase(input)) {
				((Text) control).setText(value);
			} else if ("combo".equalsIgnoreCase(input) || 
				"combobox".equalsIgnoreCase(input)) {
				((Combo) control).setText(value);
			} else if (input.startsWith(NetPortUtil.NET_SELECTOR) || input.startsWith("valueSelector")) {
				((IValueSelector) control).setValue(value);
			} else if ("checkbox".equalsIgnoreCase(input)) {
				((Button) control).setSelection(Boolean.valueOf(value));
			}
		}
	}
}
