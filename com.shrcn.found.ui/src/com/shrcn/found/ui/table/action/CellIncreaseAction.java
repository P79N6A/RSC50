/**
 * Copyright (c) 2007-2014 上海思源弘瑞自动化有限公司.
 * All rights reserved. 
 * This program is an arithmetic for taxis of figure.
 */

package com.shrcn.found.ui.table.action;

import com.shrcn.found.common.util.StringUtil;
import com.shrcn.found.ui.model.IField;
import com.shrcn.found.ui.table.XOTable;
import com.shrcn.found.ui.util.ImageConstants;
import com.shrcn.found.ui.util.ImgDescManager;

/**
 * 
 *
 * @author 孙春颖
 * @version 1.0, 2014-4-1
 */
/**
 * $Log$
 */
public class CellIncreaseAction extends TableAction {
	
	public CellIncreaseAction(XOTable table) {
		super(table);
		setText("列递增(&A)");
		setImageDescriptor(ImgDescManager.getImageDesc(ImageConstants.COPY));
	}
	
	@Override
	public boolean isEnabled() {
		// 不允许编辑的列不能递增
		int colNum = table.getSelectColNum();
		if (colNum < 0 || colNum >= table.getFields().length)
			return false;
		IField field = table.getFields()[colNum];
		String editor = field.getEditor();
		return field.isEditAble() && !StringUtil.isEmpty(editor) && editor.equals("text");
	}
	
	@Override
	public void run() {
		table.incrsColumnValues();
	}
}
