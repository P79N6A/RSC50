/**
 * Copyright (c) 2007-2010 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based on IEC61850 SCT.
 */
package com.shrcn.business.scl.model.navgtree;

import java.util.List;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;


/**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2010-8-12
 */
/**
 * $Log: TreeNodeContentProvider.java,v $
 * Revision 1.1  2013/03/29 09:35:23  cchun
 * Add:创建
 *
 * Revision 1.1  2011/09/15 08:40:28  cchun
 * Refactor:修改归属包
 *
 * Revision 1.1  2010/08/12 07:29:31  cchun
 * Add:添加provider类
 *
 */
public class TreeNodeContentProvider implements ITreeContentProvider {

	/**
	 * 在界面中单击某节点时，由此方法决定被单击节点应该显示哪些子节点
	 */
	public Object[] getChildren(Object parentElement) {
		TreeNode entry = (TreeNode) parentElement;
		List<?> list = entry.getChildren();
		if (list == null) {
			return new Object[0];
		}
		return list.toArray();
	}

	public Object getParent(Object arg0) {
		return null;
	}

	/**
	 * 
	 * 判断参数element节点是否有子节点 返回true表示element有子节点，则其前面会显示有"+"图标
	 */
	public boolean hasChildren(Object element) {
		TreeNode entry = (TreeNode) element;
		List<?> list = entry.getChildren();
		return !(list == null || list.isEmpty());
	}

	/**
	 * 由此方法决定树的"第一级"节点显示哪些对象。
	 */
	public Object[] getElements(Object inputElement) {
		if (inputElement instanceof List) {
			return ((List<?>) inputElement).toArray();
		} else if(inputElement.getClass().isArray()) {
			return (Object[])inputElement;
		}
		return new Object[0];// 空数组
	}

	public void dispose() {
	}

	public void inputChanged(Viewer arg0, Object arg1, Object arg2) {
	}
}
