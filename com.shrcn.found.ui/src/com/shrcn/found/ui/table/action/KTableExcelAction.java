/**
 * Copyright (c) 2007-2013 上海思源弘瑞自动化有限公司. All rights reserved. 
 * This program is an eclipse Rich Client Application
 * designed for IED configuration and debuging.
 */
package com.shrcn.found.ui.table.action;

import org.eclipse.jface.action.Action;

import com.shrcn.found.ui.table.XOTable;

/**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2013-10-18
 */
/**
 * $Log: KTableExcelAction.java,v $
 * Revision 1.1  2013/10/18 06:21:18  cchun
 * Add:添加excel导出菜单
 *
 */
public class KTableExcelAction extends TableAction {
	
	private String title;

	public KTableExcelAction(XOTable table, String title) {
		super(table);

		this.title = title;
		setText("导出Excel(&E)");
//		setImageDescriptor(ImgDescManager.getImageDesc(ImageConstants.EXCEL));
	}
	
	public KTableExcelAction(XOTable table) {
		super(table);

		this.title = table.getTableDesc();
		setText("导出Excel(&E)");
//		setImageDescriptor(ImgDescManager.getImageDesc(ImageConstants.EXCEL));
	}
	
	@Override
	public void run() {
		table.exportExcel(title);
	}
}