/**
 * Copyright (c) 2007-2010 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based on IEC61850 SCT.
 */
package com.shrcn.found.ui.dnd;

import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSourceListener;
import org.eclipse.swt.dnd.DropTargetListener;
import org.eclipse.swt.dnd.Transfer;

/**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2011-3-10
 */
/**
 * $Log: DragDropUtil.java,v $
 * Revision 1.1  2013/08/14 03:01:49  cchun
 * Add:添加
 *
 * Revision 1.1  2011/03/11 01:26:58  cchun
 * Add:拖拽工具类
 *
 */
public class DragDropUtil {
	
	private DragDropUtil() {}
	
	/**
	 * 添加拖监听
	 * @param viewer
	 * @param listener
	 * @param transfer
	 */
	public static void addDragSupport(StructuredViewer viewer, DragSourceListener listener,
			Transfer...transfer) {
		viewer.addDragSupport(DND.DROP_MOVE | DND.DROP_COPY | DND.DROP_DEFAULT, 
				transfer, listener);
	}
	
	/**
	 * 添加放监听
	 * @param viewer
	 * @param listener
	 * @param transfer
	 */
	public static void addDropSupport(StructuredViewer viewer, DropTargetListener listener,
			Transfer...transfer) {
		viewer.addDropSupport(DND.DROP_MOVE | DND.DROP_COPY | DND.DROP_DEFAULT, 
				transfer, listener);
	}
}
