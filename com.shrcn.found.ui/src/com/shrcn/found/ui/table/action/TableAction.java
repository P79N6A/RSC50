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
 * @version 1.0, 2015-2-9
 */
/**
 * $Log$
 */
public class TableAction extends Action {

	protected XOTable table;

	public TableAction(XOTable table) {
		this.table = table;
	}
}
