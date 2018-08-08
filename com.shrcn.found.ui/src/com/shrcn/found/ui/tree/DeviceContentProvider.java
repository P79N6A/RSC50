/**
 * Copyright (c) 2007-2013 上海思弘瑞电力控制技术有限公司. All rights reserved. 
 * This program is an eclipse Rich Client Application
 * designed for IED configuration and debuging.
 */
package com.shrcn.found.ui.tree;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

/**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2013-6-8
 */
/**
 * $Log: DeviceContentProvider.java,v $
 * Revision 1.1  2013/06/08 10:37:13  cchun
 * Update:增加编辑功能
 *
 */
public class DeviceContentProvider implements ITreeContentProvider {

	private ConfigTreeAdapter adapter;
	
	public DeviceContentProvider(ConfigTreeAdapter adapter) {
		this.adapter = adapter;
	}
	
	@Override
	public Object[] getChildren(Object parentElement) {
		return adapter.getChildren(parentElement);
	}

	@Override
	public Object getParent(Object element) {
		return adapter.getParent(element);
	}

	@Override
	public boolean hasChildren(Object element) {
		return adapter.hasChildren(element);
	}

	@Override
	public Object[] getElements(Object inputElement) {
		return adapter.getElements(inputElement);
	}

	@Override
	public void dispose() {
	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
	}

}
