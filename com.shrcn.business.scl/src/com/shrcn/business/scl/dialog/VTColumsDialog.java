/**
 * Copyright (c) 2007-2010 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based on IEC61850 SCT.
 */
package com.shrcn.business.scl.dialog;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Shell;

import com.shrcn.business.scl.SCLConstants;
import com.shrcn.business.scl.table.VTTableField;
import com.shrcn.found.ui.app.WrappedDialog;
import com.shrcn.found.ui.util.UIPreferences;

/**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2010-5-20
 */
/**
 * $Log: VTColumsDialog.java,v $
 * Revision 1.2  2013/08/07 06:38:28  cchun
 * Update:修改对话框布局
 *
 * Revision 1.1  2013/06/28 08:45:25  cchun
 * Update:添加模型检查和虚端子关联查看
 *
 * Revision 1.3  2010/09/03 03:35:49  cchun
 * Update:将列定制统一管理
 *
 * Revision 1.2  2010/05/21 03:32:21  cchun
 * Update:完善“全屏”、“打印”列定制功能
 *
 * Revision 1.1  2010/05/20 11:07:20  cchun
 * Add:列调整菜单项
 *
 */
public class VTColumsDialog extends WrappedDialog {

	private Group group;
	private List<Integer> selNums;
	
	private VTTableField[] fields = SCLConstants.VTTABLE_FIELDS;
	private UIPreferences sctPref=UIPreferences.newInstance();
	/**
	 * Create the dialog
	 * @param parentShell
	 */
	public VTColumsDialog(Shell parentShell, List<Integer> fields) {
		super(parentShell);
		this.selNums = fields;
	}

	/**
	 * Create contents of the dialog
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);
		container.setLayout(new FillLayout());

		group = new Group(container, SWT.NONE);
		group.setText("可选列");
		final GridLayout gridLayout = new GridLayout(2, true);
		group.setLayout(gridLayout);
		//
		initControls();
		return container;
	}
	
	private void initControls() {
		int fLen = fields.length; 
		int rowNum = (fLen % 2 == 0) ? fLen/2 : (fLen/2 + 1);
		
		for(int i=0; i<rowNum; i++) {
			createButton(i);
			int index = rowNum + i;
			if (index < fLen)
				createButton(index);
		}
	}

	private void createButton(int index) {
		VTTableField field = fields[index];
		Button button = new Button(group, SWT.CHECK);
		button.setText(" " + index + " " + field.getHead());
		button.setData(field);
		button.setSelection(selNums.contains(field.getValNum()));
	}
	
	@Override
	protected void buttonPressed(int buttonId) {
		if (buttonId == OK) {
			List<VTTableField> cfgFields = new ArrayList<VTTableField>();
			for (Control ctrl : group.getChildren()) {
				if (((Button) ctrl).getSelection()) {
					VTTableField selectedField = (VTTableField) ctrl.getData();
					cfgFields.add(selectedField);
				}
			}
			Collections.sort(cfgFields, new Comparator<VTTableField>() {
				@Override
				public int compare(VTTableField f1, VTTableField f2) {
					return f1.getValNum() - f2.getValNum();
				}
			});
			StringBuffer selected = new StringBuffer();
			for (VTTableField f : cfgFields) {
				selected.append(f.getValNum() + ",");
			}
			selected.deleteCharAt(selected.length() - 1);
			sctPref.setInfo(SCLConstants.PREF_VTMODEL_FIELDS, selected.toString());
			this.fields = cfgFields.toArray(new VTTableField[0]);
		}
		super.buttonPressed(buttonId);
	}

	/**
	 * Create contents of the button bar
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, "确定", true);
		createButton(parent, IDialogConstants.CANCEL_ID, "取消", false);
	}

	/**
	 * Return the initial size of the dialog
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(324, 360);
	}
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("配置表格列");
	}

	public VTTableField[] getFields() {
		return fields;
	}

}
