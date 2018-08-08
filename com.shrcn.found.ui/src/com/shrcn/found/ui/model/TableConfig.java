/**
 * Copyright (c) 2007-2009 上海思弘瑞电力控制技术有限公司. All rights reserved. 
 * This program is an eclipse Rich Client Application
 * designed for IED configuration and debuging.
 */
package com.shrcn.found.ui.model;

import com.shrcn.found.common.util.ObjectUtil;

/**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2014-1-2
 */
public class TableConfig extends FieldsUI {
	
	private boolean ordered;
	private int sortDirection;
	private Class<?> contentClass;
	
	public TableConfig() {
		super();
	}

	public TableConfig(String name, String desc, String className, String tClassName) {
		super();
		setName(name);
		setDesc(desc);
		setClassName(className);
		settClassName(tClassName);
		this.contentClass = ObjectUtil.getClassByName(getClass(), className);
	}

	public boolean isOrdered() {
		return ordered;
	}

	public void setOrdered(boolean ordered) {
		this.ordered = ordered;
	}

	public int getSortDirection() {
		return sortDirection;
	}

	public void setSortDirection(int sortDirection) {
		this.sortDirection = sortDirection;
	}

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
}
