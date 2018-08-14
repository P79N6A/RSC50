/**
 * Copyright (c) 2007-2010 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based on IEC61850 SCT.
 */
package com.shrcn.business.scl.action;

import org.eclipse.jface.action.Action;

import com.shrcn.business.scl.table.VTViewTable;

/**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2010-5-20
 */
/**
 * $Log: VTPrintAction.java,v $
 * Revision 1.1  2013/06/28 08:45:26  cchun
 * Update:添加模型检查和虚端子关联查看
 *
 * Revision 1.1  2010/05/20 11:06:56  cchun
 * Add:修改表格菜单
 *
 */
public class VTPrintAction extends Action {
	private VTViewTable table = null;
	
	public VTPrintAction(VTViewTable table) {
		setText("打印(&P)");
		this.table = table;
	}
	
	@Override
	public void run() {
		table.print();
	}
}
