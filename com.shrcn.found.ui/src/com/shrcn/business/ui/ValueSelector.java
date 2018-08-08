/**
 * Copyright (c) 2007-2013 上海思弘瑞电力控制技术有限公司. All rights reserved. 
 * This program is an eclipse Rich Client Application
 * designed for IED configuration and debuging.
 */
package com.shrcn.business.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

import com.shrcn.found.ui.util.SwtUtil;

/**
 * 数据选择器
 * 
 * @author 周泉(mailto:zq@shrcn.com)
 * @version 1.0, 2014-8-20
 */
/**
 * $Log$
 */
public class ValueSelector extends AbstractValueSelector {
	
	protected Text txtValue;
	protected Button btSel;

	public ValueSelector(Composite parent, String pointVal) {
		super(parent);
		
		setLayout(SwtUtil.getGridLayout(2));
		txtValue = SwtUtil.createText(this, SWT.BORDER | SWT.READ_ONLY, SwtUtil.hf_gd);
		btSel = SwtUtil.createButton(this, null, SWT.PUSH, "...");
		addListeners();
	}

	private void addListeners() {
		btSel.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				selectAction();
			}
		});
	}
	
	public void addModifyListener(ModifyListener listener) {
		if (txtValue != null && !txtValue.isDisposed() && listener != null)
			txtValue.addModifyListener(listener);
	}

	protected void selectAction() {
		
	}

}
