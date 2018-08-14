/**
 * Copyright (c) 2007, 2008 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based Visual Device Develop System.
 */
package com.shrcn.business.scl.ui;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;

import com.shrcn.found.ui.table.DefaultKTable;
import com.shrcn.found.ui.table.DefaultKTableModel;

import de.kupzog.ktable.KTableCellSelectionAdapter;

/**
 * 
 * @author 黄钦辉(mailto:huangqinhui@shrcn.com)
 * @version 1.0, 2009-4-27
 */
/*
 * 修改历史 $Log: CIDTable.java,v $
 * 修改历史 Revision 1.1  2013/06/28 08:45:24  cchun
 * 修改历史 Update:添加模型检查和虚端子关联查看
 * 修改历史
 * 修改历史 Revision 1.6  2011/11/24 11:45:53  cchun
 * 修改历史 Refactor:使用统一的基础类
 * 修改历史
 * 修改历史 Revision 1.5  2011/09/20 06:31:56  cchun
 * 修改历史 Update:增加selectByName()
 * 修改历史
 * 修改历史 Revision 1.4  2011/07/27 07:46:18  cchun
 * 修改历史 Refactor: 修改setSelectAll()访问权限
 * 修改历史
 * 修改历史 Revision 1.3  2011/01/07 09:59:35  cchun
 * 修改历史 Refactor:重构ied选择表格接口
 * 修改历史
 * 修改历史 Revision 1.2  2010/12/22 02:50:12  cchun
 * 修改历史 Update:修改cid导出选择方式
 * 修改历史
 * 修改历史 Revision 1.1  2010/03/02 07:49:37  cchun
 * 修改历史 Add:添加重构代码
 * 修改历史
 * 修改历史 Revision 1.1  2009/04/28 00:35:53  hqh
 * 修改历史 添加蹈入cid模型
 * 修改历史
 */
public class CIDTable extends DefaultKTable {

	public CIDTable(DefaultKTableModel model, Composite parent) {
		super(parent, model);
		addPopMenu();
		registerCellSelection();
	}
	
	/**
	 * 获取用户选择序号
	 * @return
	 */
	private int[] getRowChecked() {
		List<Integer> rows = new ArrayList<Integer>();
		for (int row=1; row<model.getRowCount(); row++) {
			boolean flag = (Boolean) super.model.getContentAt(CIDModel.IED_CHECK_COL, row);
			if (flag)
				rows.add(row);
		}
		int[] checkedRows = new int[rows.size()];
		for (int i = 0; i < rows.size(); i++) {
			checkedRows[i] = rows.get(i);
		}
		return checkedRows;
	}
	
	/**
	 * 得到用户选择的所有IED名称
	 * @return
	 */
	public List<String> getSelectedIEDs() {
		int[] rows = getRowChecked();
		int length = rows.length;
		List<String> ieds = new ArrayList<String>(length);
		for (int i = 0; i < length; i++) {
			ieds.add((String) model.getContentAt(CIDModel.IED_NAME_COL, rows[i]));
		}
		return ieds;
	}

	/**
	 * 添加右键菜单
	 */
	private void addPopMenu() {
		Menu menu = new Menu(this);
		setMenu(menu);
		MenuItem select = new MenuItem(menu, 0);
		select.setText("全选");
		select.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				setSelectAll(true);
			}
		});
		MenuItem deSelect = new MenuItem(menu, 0);
		deSelect.setText("清空");
		deSelect.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				setSelectAll(false);
			}
		});
	}
	
	/**
	 * 注册标题栏全选事件
	 */
	private void registerCellSelection() {
		this.addCellSelectionListener(new KTableCellSelectionAdapter() {
			@Override
			public void fixedCellSelected(int col, int row, int statemask) {
				if (col == CIDModel.IED_CHECK_COL && row == 0) {
					reverseSelection();
				}
			}
		});
	}
	
	/**
	 * 设置表格选择状态
	 * @param status
	 */
	public void setSelectAll(boolean status) {
		for (int row = 1; row < model.getRowCount(); row++) {
			model.setContentAt(CIDModel.IED_CHECK_COL, row, status);
		}
		redraw();
	}
	
	public void selectByName(String iedName) {
		for (int row = 1; row < model.getRowCount(); row++) {
			String curName = (String) model.doGetContentAt(CIDModel.IED_NAME_COL, row);
			if (iedName.equals(curName)) {
				model.setContentAt(CIDModel.IED_CHECK_COL, row, true);
				break;
			}
		}
		redraw();
	}
	
	/**
	 * 反选
	 */
	private void reverseSelection() {
		for (int row = 1; row < model.getRowCount(); row++) {
			boolean status = (Boolean) model.getContentAt(1, row);
			model.setContentAt(CIDModel.IED_CHECK_COL, row, !status);
		}
		redraw();
	}
}
