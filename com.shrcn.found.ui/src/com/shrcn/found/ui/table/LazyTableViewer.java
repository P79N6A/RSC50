/**
 * Copyright (c) 2007-2009 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based on IEC61850 SCT.
 */
package com.shrcn.found.ui.table;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import com.shrcn.found.common.util.ObjectUtil;
import com.shrcn.found.ui.model.IField;

/**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2010-3-4
 */
/**
 * $Log: LazyTableViewer.java,v $
 * Revision 1.2  2013/04/06 05:32:56  cchun
 * Refactor:统一使用newInstance()
 *
 * Revision 1.1  2013/03/29 09:54:53  cchun
 * Add:创建
 *
 * Revision 1.3  2012/11/09 01:43:18  cchun
 * Update:去掉update标记
 *
 * Revision 1.2  2012/11/07 11:58:56  cchun
 * Update:增加构造函数
 *
 * Revision 1.1  2012/11/05 07:51:44  cchun
 * Add:懒加载表格
 *
 * Revision 1.2  2011/08/22 02:16:03  cchun
 * Update:清理注释
 *
 * Revision 1.1  2011/01/04 09:25:58  cchun
 * Refactor:将table框架移动到common项目中
 *
 * Revision 1.5  2010/04/16 06:51:22  cchun
 * Update:修改对visible的处理
 *
 * Revision 1.4  2010/04/13 09:13:14  cchun
 * Update:为表格字段配置添加是否可见属性
 *
 * Revision 1.3  2010/04/06 01:34:45  cchun
 * Update
 *
 * Revision 1.2  2010/03/11 08:24:25  cchun
 * Update:完善RTU表格
 *
 * Revision 1.1  2010/03/09 07:37:48  cchun
 * Add:添加远动配置插件
 *
 */
public class LazyTableViewer extends RTableViewer {

	public LazyTableViewer(Composite parent, IField[] fields, Class<?> modelClass) {
		super(parent, SWT.FULL_SELECTION|SWT.MULTI|SWT.BORDER|SWT.VIRTUAL);
		this.fields = fields;
		setLabelProvider(new RTableLabelProvider(fields));
		setContentProvider(new ClassContentProvider(this));
		setUseHashlookup(true);
		initTable();
	}
	
	public LazyTableViewer(Composite parent, IField[] fields, Class<?> providerClass, Class<?> modelClass) {
		super(parent, SWT.FULL_SELECTION|SWT.MULTI|SWT.BORDER|SWT.VIRTUAL);
		this.fields = fields;
		setLabelProvider(new RTableLabelProvider(fields));
		ClassContentProvider provider = (ClassContentProvider) ObjectUtil.newInstance(providerClass, 
				new Class[]{LazyTableViewer.class, Class.class},  this, modelClass);
		setContentProvider(provider);
		setUseHashlookup(true);
		initTable();
	}

}
