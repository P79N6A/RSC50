/**
 * Copyright (c) 2007-2010 上海思源弘瑞自动化有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based on IEC61850 SCT.
 */
package com.shrcn.found.common.dict;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2010-12-10
 */
/**
 * $Log: DictItem.java,v $
 * Revision 1.1  2013/03/29 09:38:04  cchun
 * Add:创建
 *
 * Revision 1.1  2011/01/04 09:26:01  cchun
 * Refactor:将table框架移动到common项目中
 *
 * Revision 1.1  2010/12/13 02:11:49  cchun
 * Update:数据字典改用xml格式
 *
 */
public class DictItem {
	
	public static final String DICT_TYPE = "DICT";
	public static final String ENUM_TYPE = "ENUM";
	
	private String id;
	private String name;
	private String type;
	private List<DictItem> items;
	
	public DictItem(String id, String name) {
		this.id = id;
		this.name = name;
	}
	
	public DictItem(String id, String name, String type) {
		this.id = id;
		this.name = name;
		this.type = type;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<DictItem> getItems() {
		return items;
	}

	public void setItems(List<DictItem> items) {
		this.items = items;
	}
	
	public void addItem(DictItem item) {
		if (items == null)
			items = new ArrayList<DictItem>();
		items.add(item);
	}
}
