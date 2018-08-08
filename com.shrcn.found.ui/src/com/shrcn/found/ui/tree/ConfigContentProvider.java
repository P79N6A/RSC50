/**
 * Copyright (c) 2007-2013 上海思弘瑞电力控制技术有限公司. All rights reserved. 
 * This program is an eclipse Rich Client Application
 * designed for IED configuration and debuging.
 */
package com.shrcn.found.ui.tree;

import org.eclipse.jface.viewers.AbstractTreeViewer;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.ui.progress.DeferredTreeContentManager;

/**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2013-4-2
 */
/**
 * $Log: ConfigContentProvider.java,v $
 * Revision 1.2  2013/04/06 05:34:18  cchun
 * Add:导航树基础类
 *
 * Revision 1.1  2013/04/03 00:34:37  cchun
 * Update:转移tree控件至found工程
 *
 * Revision 1.1  2013/04/02 13:27:05  cchun
 * Add:界面类
 *
 */
public class ConfigContentProvider implements ITreeContentProvider {

	private DeferredTreeContentManager manager = null;
	private ConfigTreeAdapter adapter;
	
	public ConfigContentProvider(ConfigTreeAdapter adapter) {
		this.adapter = adapter;
	}
	
	@Override
	public Object[] getChildren(Object parentElement) {
		return manager.getChildren(parentElement);
	}

	@Override
	public Object getParent(Object element) {
		return null;
	}

	@Override
	public boolean hasChildren(Object element) {
		return manager.mayHaveChildren(element);
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
		if (viewer instanceof AbstractTreeViewer) {
			manager = new DeferredTreeContentManager(this, (AbstractTreeViewer) viewer);
		}
	}

}
