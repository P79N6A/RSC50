/**
 * Copyright (c) 2007-2013 上海思弘瑞电力控制技术有限公司. All rights reserved. 
 * This program is an eclipse Rich Client Application
 * designed for IED configuration and debuging.
 */
package com.shrcn.found.ui.table.action;

import org.eclipse.jface.action.Action;

import com.shrcn.found.ui.table.XOTable;
import com.shrcn.found.ui.util.ImageConstants;
import com.shrcn.found.ui.util.ImgDescManager;

/**
 * 
 * @author 曹兴春(mailto:cxc14375@shrcn.com)
 * @version 1.0, 2013-12-5
 */
/**
 * $Log: RefreshAction.java,v $
 * Revision 1.2  2013/12/06 08:16:32  cchun
 * Udpate:添加菜单快捷键
 *
 * Revision 1.1  2013/12/05 10:45:47  cxc
 * update:表格中增加按照点号进行刷新的功能
 *
 */
public class RefreshAction extends TableAction {

	public RefreshAction(XOTable table) {
		super(table);

		setText("刷新(&R)");
		setImageDescriptor(ImgDescManager.getImageDesc(ImageConstants.REFRESH));
	}

   @Override
	public void run() {
		table.refresh();
	}
}
