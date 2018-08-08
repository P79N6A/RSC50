/**
 * Copyright (c) 2007-2010 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based on IEC61850 SCT.
 */
package com.shrcn.found.ui.model;


/**
 * 
 * @author 李培宜(mailto:lpy14137@shrcn.com)
 * @version 1.0, 2012-10-8
 */
/**
 * $Log: Form.java,v $
 * Revision 1.1  2013/03/29 09:55:13  cchun
 * Add:创建
 *
 * Revision 1.2  2013/03/04 05:12:12  scy
 * Add：增加hasExd属性，是否存在不显示的信息。
 *
 * Revision 1.1  2012/10/08 09:18:43  cchun
 * Add:表单界面相关类
 *
 */
public class Form extends FieldsUI {

	private int columns;
	
	public Form(String desc, int columns, String className) {
		super();
		this.desc = desc;
		this.columns = columns;
		this.className = className;
	}
	
	public Form(String desc, int columns, String className, String tClassName) {
		this(desc, columns, className);
		this.tClassName = tClassName;
	}

	public int getColumns() {
		return columns;
	}

	public void setColumns(int columns) {
		this.columns = columns;
	}
}
