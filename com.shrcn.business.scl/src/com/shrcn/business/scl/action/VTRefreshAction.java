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
 * $Log: VTRefreshAction.java,v $
 * Revision 1.1  2010/05/20 11:06:56  cchun
 * Add:修改表格菜单
 *
 */
public class VTRefreshAction extends Action {
	private VTViewTable table = null;
	
	public VTRefreshAction(VTViewTable table) {
		setText("刷新(&R)");
		this.table = table;
	}
	
	@Override
	public void run() {
		table.refreshTB();
	}
}
