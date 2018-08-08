/**
 * Copyright (c) 2007-2013 上海思弘瑞电力控制技术有限公司. All rights reserved. 
 * This program is an eclipse Rich Client Application
 * designed for IED configuration and debuging.
 */
package com.shrcn.business.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.widgets.Composite;

/**
 * 数据选择器抽象类
 * 
 * @author 周泉(mailto:zq@shrcn.com)
 * @version 1.0, 2014-8-21
 */
/**
 * $Log$
 */
public class AbstractValueSelector extends Composite implements IValueSelector {
	
	public AbstractValueSelector(Composite parent) {
		super(parent, SWT.NONE);
	}
	
	public void addModifyListener(ModifyListener listener) {
		
	}

	@Override
	public Object getValue() {
		return null;
	}

	@Override
	public void setValue(Object value) {
		
	}
}
