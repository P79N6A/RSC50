/**
 * Copyright (c) 2007-2013 上海思弘瑞电力控制技术有限公司. All rights reserved. 
 * This program is an eclipse Rich Client Application
 * designed for IED configuration and debuging.
 */
package com.shrcn.found.ui.table.action;

import com.shrcn.found.ui.model.IField;
import com.shrcn.found.ui.table.XOTable;
import com.shrcn.found.ui.util.ImageConstants;
import com.shrcn.found.ui.util.ImgDescManager;

/**
 * 
 * @author 曹兴春(mailto:cxc14375@shrcn.com)
 * @version 1.0, 2014-2-17
 */
/**
 * $Log$
 */
public class CellSynchAction extends TableAction {
	
	public CellSynchAction(XOTable table) {
		super(table);
		setText("列同步(&S)");
		setImageDescriptor(ImgDescManager.getImageDesc(ImageConstants.COPY));
	}
	
	@Override
	public boolean isEnabled() {
		// 不允许编辑的列不能同步
		int colNum = table.getSelectColNum();
		if (colNum < 0 || colNum >= table.getFields().length)
			return false;
		IField field = table.getFields()[colNum];
		return field.isEditAble();
	}
	
	@Override
	public void run() {
		table.synchColumnValues();
	}

}
