/**
 * Copyright (c) 2007-2010 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based on IEC61850 SCT.
 */
package com.shrcn.found.ui.table.action;

import com.shrcn.found.ui.table.XOTable;
import com.shrcn.found.ui.util.ImageConstants;
import com.shrcn.found.ui.util.ImgDescManager;

/**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2012-11-8
 */
public class PasteAction extends TableAction {
	

	public PasteAction(XOTable table) {
		super(table);

		setText("粘贴(&P)");
		setImageDescriptor(ImgDescManager.getImageDesc(ImageConstants.PASTE));
	}
	
	@Override
	public boolean isEnabled() {
		return table.hasCopied();
	}
	
	@Override
	public void run() {
		table.paste();
	}
}
