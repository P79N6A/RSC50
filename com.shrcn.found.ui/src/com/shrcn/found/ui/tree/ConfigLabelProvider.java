/**
 * Copyright (c) 2007-2013 上海思弘瑞电力控制技术有限公司. All rights reserved. 
 * This program is an eclipse Rich Client Application
 * designed for IED configuration and debuging.
 */
package com.shrcn.found.ui.tree;

import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.swt.graphics.Image;

/**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2013-4-2
 */
/**
 * $Log: ConfigLabelProvider.java,v $
 * Revision 1.1  2013/04/03 00:34:38  cchun
 * Update:转移tree控件至found工程
 *
 * Revision 1.1  2013/04/02 13:27:04  cchun
 * Add:界面类
 *
 */
public class ConfigLabelProvider extends ColumnLabelProvider {
	
	private ConfigTreeAdapter adapter;
	
	public ConfigLabelProvider(ConfigTreeAdapter adapter) {
		this.adapter = adapter;
	}
	
	@Override
	public Image getImage(Object element) {
		return adapter.getImage(element);
	}
	
	@Override
	public String getText(Object element) {
		return adapter.getText(element);
	}
	
	@Override
	public String getToolTipText(Object element) {
		return adapter.getToolTipText(element);
	}
}
