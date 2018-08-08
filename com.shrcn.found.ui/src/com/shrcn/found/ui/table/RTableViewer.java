/**
 * Copyright (c) 2007-2009 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based on IEC61850 SCT.
 */
package com.shrcn.found.ui.table;

import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

import com.shrcn.found.ui.UIConstants;
import com.shrcn.found.ui.model.IField;

/**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2010-3-4
 */
/**
 * $Log: RTableViewer.java,v $
 * Revision 1.1  2013/03/29 09:37:34  cchun
 * Add:创建
 *
 * Revision 1.4  2012/11/09 05:36:29  cchun
 * Update:表格标题居中
 *
 * Revision 1.3  2012/11/02 09:36:55  cchun
 * Refactor:重构构造函数
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
public class RTableViewer extends TableViewer {

	protected IField[] fields = null;
	protected TableLayout layout = null;
	protected Table table = null;
	
	public RTableViewer(Composite parent, int style) {
		super(parent, style);
	}
	
	public RTableViewer(Composite parent, IField[] fields, int style) {
		this(parent, style);
		this.fields = fields;
		setContentProvider(new RTableContentProvider());
		setLabelProvider(new RTableLabelProvider(fields));
		initTable();
	}
	
	protected void initTable() {
		this.layout = new TableLayout();
		this.table = getTable();
		table.setHeaderVisible(true); // 显示表头
		table.setLinesVisible(true); // 显示表格线
		table.setLayout(layout);
		for(IField field : fields) {
			int w = field.getWidth();
			w = w<UIConstants.Col_W_Min ? UIConstants.Col_W_Default : w;
			layout.addColumnData(new ColumnWeightData(w)); // 列宽
			new TableColumn(table, SWT.CENTER).setText(field.getTitle());
		}
	}

	public IField[] getFields() {
		return fields;
	}
}
