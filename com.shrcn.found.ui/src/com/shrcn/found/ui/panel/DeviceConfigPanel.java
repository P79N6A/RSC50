/**
 * Copyright (c) 2007-2010 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based on IEC61850 SCT.
 */
package com.shrcn.found.ui.panel;

import java.lang.reflect.Field;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import com.shrcn.found.common.event.EventConstants;
import com.shrcn.found.common.event.EventManager;
import com.shrcn.found.common.util.ObjectUtil;
import com.shrcn.found.ui.model.Form;
import com.shrcn.found.ui.model.IField;
import com.shrcn.found.ui.util.FormFieldUtil;

/**
 * 
 * @author 孙春颖(scy@shrcn.com)
 * @version 1.0, 2013 4 17
 */
/**
 * $Log: DeviceConfigPanel.java,v $
 * Revision 1.3  2013/11/05 13:58:57  cchun
 * 修改界面切换，不修改也提示保存对话框的问题
 *
 * Revision 1.2  2013/04/22 01:56:53  scy
 * Update：实现界面数据保存
 *
 * Revision 1.1  2013/04/17 07:30:39  scy
 * Add：创建
 *
 */
public class DeviceConfigPanel extends ConfigPanel {

	public DeviceConfigPanel(Composite parent, Form form) {
		super(parent, form);
	}

	@Override
	protected void saveField(Control control, IField field) {
		if (obj == null)
			return;
		String newValue = FormFieldUtil.getControlValue(control, field);
		String fieldName = field.getName();
		Object oldV = ObjectUtil.getProperty(obj, fieldName);
		Object newV = null;
		
		Class<?> objClass = obj.getClass();
		Field f = ObjectUtil.getField(objClass, fieldName);
		if (f == null)
			return;
		Class<?> type = f.getType();
		if (type == String.class) {
			String oldValue = (String) oldV;
			newV = newValue;
			if (oldValue != null && oldValue.equals(newValue))
				return;
		} else if (type == Integer.TYPE || type == Integer.class) {
			Integer oldValue = (Integer) oldV;
			newV = Integer.parseInt(newValue);
			if (oldValue == (Integer)newV)
				return;
		}
		if (newV!=null && oldV!=null &&
				!checkValue(field, newV.toString(), oldV.toString())) {
			FormFieldUtil.setControlValue(control, field, oldV.toString());
			return;
		}
		if (newV != null) {
			ObjectUtil.setProperty(obj, fieldName, newV);
			EventManager.getDefault().notify(EventConstants.DEVICE_EDIT, null);
		}
	}
}
