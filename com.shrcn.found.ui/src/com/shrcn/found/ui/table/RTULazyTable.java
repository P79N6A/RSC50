/**
 * Copyright (c) 2007-2010 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based on IEC61850 SCT.
 */
package com.shrcn.found.ui.table;

import org.eclipse.swt.widgets.Composite;

import com.shrcn.found.ui.model.TableConfig;

/**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2012-11-2
 */
/**
 * $Log: RTULazyTable.java,v $
 * Revision 1.1  2013/03/29 09:54:58  cchun
 * Add:创建
 *
 * Revision 1.3  2012/11/12 00:56:07  cchun
 * Update:添加getClassByName()
 *
 * Revision 1.2  2012/11/07 11:59:50  cchun
 * Update:修改构造函数
 *
 * Revision 1.1  2012/11/05 07:51:44  cchun
 * Add:懒加载表格
 *
 */
public class RTULazyTable extends RTable {
	
	private Class<?> providerClass;

	public RTULazyTable(Composite parent, TableConfig config, Class<?> providerClass) {
		super(parent, config);
		this.providerClass = providerClass;
	}

	protected void initUI(){
		this.tbViewer = new LazyTableViewer(parent, visibleFields, providerClass, contentClass);
	}
}
