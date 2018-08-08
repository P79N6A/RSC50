/**
 * Copyright (c) 2007-2010 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based on IEC61850 SCT.
 */
package com.shrcn.found.ui.util;

import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;

/**
 * 双击展开用户所选节点的子节点。
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2010-12-24
 */
/**
 * $Log: ExpandTreeListener.java,v $
 * Revision 1.2  2013/04/02 13:28:35  cchun
 * Refactor:提取SwtUtil.changeExpandStatus()
 *
 * Revision 1.1  2013/03/29 09:36:51  cchun
 * Add:创建
 *
 * Revision 1.1  2010/12/24 06:39:28  cchun
 * Add:鼠标双击展开树节点监听器
 *
 */
public class ExpandTreeListener extends MouseAdapter {
	private TreeViewer treeViewer;
	
	public ExpandTreeListener(TreeViewer treeViewer) {
		this.treeViewer = treeViewer;
	}
	
	@Override
	public void mouseDoubleClick(MouseEvent e) {
		SwtUtil.changeExpandStatus(treeViewer);
	}
}
