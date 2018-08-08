/**
 * Copyright (c) 2007-2013 上海思弘瑞电力控制技术有限公司. All rights reserved. 
 * This program is an eclipse Rich Client Application
 * designed for IED configuration and debuging.
 */
package com.shrcn.business.ui;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

import com.shrcn.found.common.util.StringUtil;
import com.shrcn.found.ui.util.SwtUtil;

/**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2013-7-14
 */
/**
 * $Log: NetSelector.java,v $
 * Revision 1.2  2013/11/06 00:40:54  cchun
 * 增加网口选择的修改事件
 *
 * Revision 1.1  2013/07/18 13:39:13  cchun
 * Udpate:增加panel编辑方式
 *
 */
public class NetSelector extends AbstractValueSelector {

	private Text txtNet;
	private Button btSel;
	
	private int total;
	private int max;
	private String value;
	
	/**
	 * 网口选择控件
	 * @param parent
	 * @param style
	 * @param name 例如：netSelector_8_1（8代表网口最大数，1表示允许选择的最大网口数）
	 */
	public NetSelector(Composite parent, String name) {
		super(parent);
		String[] arr = name.split("_");
		total = Integer.parseInt(arr[1]);
		max = Integer.parseInt(arr[2]);
		setLayout(SwtUtil.getGridLayout(2));
		txtNet = SwtUtil.createText(this, SWT.BORDER | SWT.READ_ONLY, SwtUtil.hf_gd);
		btSel = SwtUtil.createButton(this, null, SWT.PUSH, "...");
		addListeners();
	}
	
	public void setEnabled(boolean enabled) {
		btSel.setEnabled(enabled);
	}

	private void addListeners() {
		SelectionAdapter btnListener = new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String netName = txtNet.getText();
				GooseSubsNetDialog dialog = new GooseSubsNetDialog(SwtUtil.getDefaultShell(), netName, total);
				dialog.setHasPorts(true);
				dialog.setCount(max);
				if (dialog.open() == IDialogConstants.OK_ID) {
					value = NetPortUtil.getValue(dialog.getNetPorts(), total);
					netName = NetPortUtil.getNetName(value, total);
					txtNet.setText(netName);
				}
			}
		};
		btSel.addSelectionListener(btnListener);
	}
	
	public void addModifyListener(ModifyListener listener) {
		if (txtNet!=null && !txtNet.isDisposed() && listener!=null)
			txtNet.addModifyListener(listener);
	}

	public String getValue() {
		return value;
	}

	public void setValue(Object val) {
		this.value = (String) val;
		String netName = NetPortUtil.getNetName(value, total);
		txtNet.setText(netName);
	}
	
}
