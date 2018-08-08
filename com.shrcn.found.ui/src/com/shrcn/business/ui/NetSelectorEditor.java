/**
 * Copyright (c) 2007-2013 上海思源弘瑞自动化有限公司. All rights reserved. 
 * This program is an eclipse Rich Client Application
 * designed for IED configuration and debuging.
 */
package com.shrcn.business.ui;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.widgets.Shell;

import de.kupzog.ktable.editors.TableCellEditorDialog;

/**
 * @author 周思韵(mailto:zsy.14193@shrcn.com)
 * @version 1.0, 2013-4-25
 */
/**
 * $log$
 */
public class NetSelectorEditor extends TableCellEditorDialog {
	
	private int pNums;
	private int count;
	private int netCol;
	private GooseSubsNetDialog dialog;
	
	public NetSelectorEditor(int pNums, int count, int netCol) {
		this.pNums = pNums;
		this.count = count;
		this.netCol = netCol;
	}
	
	@Override
	public Dialog getDialog(Shell shell) {
		String netNums = (String) m_Model.getContentAt(m_Col, m_Row);
		dialog = new GooseSubsNetDialog(shell, netNums, pNums);
		dialog.setCount(count);
		if (netCol > -1) {
			String existPorts = (String) m_Model.getContentAt(netCol, m_Row);
			dialog.setExistPorts(existPorts);
		}
		dialog.setHasPorts(true);
		return dialog;
	}
	
	public void close(boolean save) {
        if (save){
        	String netNums = dialog.getNetPorts();
        	m_Model.setContentAt(m_Col, m_Row, netNums);
         }
        super.close(save);
    }

	@Override
	public void setupShellProperties(Shell dialogShell) {
	}

}
