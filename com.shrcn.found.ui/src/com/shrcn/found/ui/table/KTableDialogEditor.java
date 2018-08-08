/**
 * Copyright (c) 2007-2013 上海思源弘瑞自动化有限公司. All rights reserved. 
 * This program is an eclipse Rich Client Application
 * designed for IED configuration and debuging.
 */
package com.shrcn.found.ui.table;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.widgets.Shell;

import com.shrcn.found.common.util.ObjectUtil;

import de.kupzog.ktable.editors.TableCellEditorDialog;

/**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2014-10-21
 */
/**
 * $Log$
 */
public class KTableDialogEditor extends TableCellEditorDialog {

	private KTableEditorDialog dialog;
	private Class<?> dlgClass;
	
	public KTableDialogEditor(Class<?> dlgClass) {
		this.dlgClass = dlgClass;
	}
	
	@Override
	public Dialog getDialog(Shell shell) {
		dialog = (KTableEditorDialog) ObjectUtil.newInstance(dlgClass, 
				new Class<?>[]{Shell.class, Object.class}, shell, getRowItem());
		return dialog;
	}

	private Object getRowItem() {
		return ((DefaultKTableModel) m_Model).getItem(m_Row);
	}
	
	@Override
	public void setupShellProperties(Shell dialogShell) {
	}
	
	@Override
	public void close(boolean save) {
		if (save) {
			// 刷新表格数据
			((DefaultKTableModel)m_Model).getContent().clear();
        	((DefaultKTable)m_Table).clearSelection();
        	m_Table.redraw();
		}
		super.close(save);
	}
}