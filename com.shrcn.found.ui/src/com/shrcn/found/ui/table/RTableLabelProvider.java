/**
 * Copyright (c) 2007-2009 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based on IEC61850 SCT.
 */
package com.shrcn.found.ui.table;

import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.swt.graphics.Image;

import com.shrcn.found.common.dict.DictManager;
import com.shrcn.found.ui.enums.EnumDataType;
import com.shrcn.found.ui.model.IField;

/**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2010-3-4
 */
/**
 * $Log: RTableLabelProvider.java,v $
 * Revision 1.1  2013/03/29 09:37:31  cchun
 * Add:创建
 *
 * Revision 1.4  2012/11/02 09:36:27  cchun
 * Update:dattype大小写区分
 *
 * Revision 1.3  2011/11/23 09:30:28  cchun
 * Update:修改getColumnText()使用新的字典处理方式
 *
 * Revision 1.2  2011/05/13 07:03:23  cchun
 * Update:修改字典字段显示逻辑
 *
 * Revision 1.1  2011/01/04 09:25:59  cchun
 * Refactor:将table框架移动到common项目中
 *
 * Revision 1.1  2010/03/09 07:37:46  cchun
 * Add:添加远动配置插件
 *
 */
public class RTableLabelProvider implements ITableLabelProvider {

	private IField[] fields = null;
	
	public RTableLabelProvider(IField[] fields) {
		this.fields = fields;
	}
	
	@Override
	public Image getColumnImage(Object element, int columnIndex) {
		return fields[columnIndex].getImageValue(element);
	}

	@Override
	public String getColumnText(Object element, int columnIndex) {
		IField field = (IField) fields[columnIndex];
		String value = field.getTextValue(element);
		if(null != value && "datType".equalsIgnoreCase(field.getName())) {
			return EnumDataType.getTypeById(value);
		} else {
			DictManager dictMgr = DictManager.getInstance();
			String dict = field.getDictType();
			return (dict == null) ? value : dictMgr.getNameById(dict, value);
		}
	}

	@Override
	public void addListener(ILabelProviderListener listener) {
	}

	@Override
	public void dispose() {
	}

	@Override
	public boolean isLabelProperty(Object element, String property) {
		return false;
	}

	@Override
	public void removeListener(ILabelProviderListener listener) {
	}
}
