/**
 * Copyright (c) 2007, 2008 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application 
 * based Visual Device Develop System.
 */
package com.shrcn.business.scl.model.intree;

import java.util.List;

import org.eclipse.jface.viewers.AbstractTreeViewer;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.ui.progress.DeferredTreeContentManager;


/**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2009-5-6
 */
/*
 * 修改历史
 * $Log: InTreeContentProvider.java,v $
 * Revision 1.1  2013/03/29 09:38:17  cchun
 * Add:创建
 *
 * Revision 1.1  2011/09/15 08:40:24  cchun
 * Refactor:修改归属包
 *
 * Revision 1.2  2010/09/21 01:05:20  cchun
 * Update:清理注释
 *
 * Revision 1.1  2010/03/02 07:48:55  cchun
 * Add:添加重构代码
 *
 * Revision 1.3  2010/01/21 08:47:59  gj
 * Update:完成UI插件的国际化字符串资源提取
 *
 * Revision 1.2  2009/05/07 03:04:04  cchun
 * Update:修改懒加载实现
 *
 * Revision 1.1  2009/05/06 06:40:10  cchun
 * Add:添加内部信号树视图
 *
 */
public class InTreeContentProvider implements ITreeContentProvider {

	private DeferredTreeContentManager manager = null;
	
	// 在界面中单击某节点时，由此方法决定被单击节点应该显示哪些子节点
	public Object[] getChildren(Object parentElement) {
		return manager.getChildren(parentElement);
	}

	public Object getParent(Object arg0) {
		return null;
	}

	/**
	 * 
	 * 判断参数element节点是否有子节点 返回true表示element有子节点，则其前面会显示有"+"图标
	 */
	public boolean hasChildren(Object element) {
		return manager.mayHaveChildren(element);
	}

	/**
	 * 由此方法决定树的" 第一级"节点显示哪些对象。
	 */
	public Object[] getElements(Object inputElement) {
		if (inputElement instanceof List) {
			List<?> input = (List<?>) inputElement;
			return input.toArray();
		}
		return new Object[0];// 空数组
	}

	public void dispose() {
	}

	public void inputChanged(Viewer viewer, Object oldData, Object newData) {
		if (viewer instanceof AbstractTreeViewer) {
			manager = new DeferredTreeContentManager(this, (AbstractTreeViewer) viewer);
		}
	}

}
