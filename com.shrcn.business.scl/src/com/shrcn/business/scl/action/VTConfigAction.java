/**
 * Copyright (c) 2007-2010 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based on IEC61850 SCT.
 */
package com.shrcn.business.scl.action;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.action.Action;

import com.shrcn.business.scl.dialog.VTColumsDialog;
import com.shrcn.business.scl.table.VTViewTable;
import com.shrcn.business.scl.table.VTViewTableModel;
import com.shrcn.found.common.util.StringUtil;

/**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2010-5-20
 */
/**
 * $Log: VTConfigAction.java,v $
 * Revision 1.1  2013/06/28 08:45:26  cchun
 * Update:添加模型检查和虚端子关联查看
 *
 * Revision 1.3  2011/08/28 06:28:43  cchun
 * Update:整理代码
 *
 * Revision 1.2  2010/09/03 03:26:13  cchun
 * Update:增加表格列定制保存
 *
 * Revision 1.1  2010/05/20 11:07:21  cchun
 * Add:列调整菜单项
 *
 */
public class VTConfigAction extends Action {
	private VTViewTable table;
	private VTViewTableModel model;
	
	public VTConfigAction(VTViewTable table) {
		setText("列选择(&O)");
		this.table = table;
		this.model = (VTViewTableModel)table.getModel();
	}
	
	@Override
	public void run() {
		List<Integer> selNums = new ArrayList<Integer>();
		String visibleFields = model.getVisibleFieldStr();
		if (visibleFields == null)
			return;
		String[] nums = visibleFields.split(",");
		for (String num : nums) {
			if (num == null || num.trim().length() == 0)
				continue;
			int vnum = StringUtil.str2int(num);
			if (vnum == -1)
				continue;
			selNums.add(vnum);
		}
		VTColumsDialog dlg = new VTColumsDialog(table.getShell(), selNums);
		if (VTColumsDialog.OK == dlg.open()) {
			model.setFields(dlg.getFields());
			table.redraw();
		}
	}
}
