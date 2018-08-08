/**
 * Copyright (c) 2007-2010 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based on IEC61850 SCT.
 */
package com.shrcn.found.ui.table.action;

import com.shrcn.found.ui.table.XOTable;
import com.shrcn.found.ui.util.DialogHelper;
import com.shrcn.found.ui.util.ImageConstants;
import com.shrcn.found.ui.util.ImgDescManager;

/**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2012-11-8
 */
/**
 * $Log: DeleteAction.java,v $
 * Revision 1.2  2013/04/07 12:28:10  cchun
 * Update:删除前提示
 *
 * Revision 1.1  2012/11/28 12:03:19  cchun
 * Refactor:转移位置
 *
 * Revision 1.1  2012/11/08 12:38:32  cchun
 * Refactor:修改包名
 *
 */
public class DeleteAction extends TableAction {
	

	public DeleteAction(XOTable table) {
		super(table);

		setText("删除(&D)");
		setImageDescriptor(ImgDescManager.getImageDesc(ImageConstants.EDIT_DELETE));
	}
	
	@Override
	public boolean isEnabled() {
		return table.getSelection() != null;
	}
	
	@Override
	public void run() {
		if (DialogHelper.showConfirm("确定要删除吗？"))
			table.handleDelete();
	}
}
