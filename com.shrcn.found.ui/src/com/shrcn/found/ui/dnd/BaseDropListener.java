/**
 * Copyright (c) 2007-2009 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based on IEC61850 SCT.
 */
package com.shrcn.found.ui.dnd;

import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.DropTargetListener;

/**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2010-3-8
 */
/**
 * $Log: BaseDropListener.java,v $
 * Revision 1.1  2013/08/14 03:01:49  cchun
 * Add:添加
 *
 * Revision 1.2  2011/03/11 01:27:12  cchun
 * Update:一般形式的拖拽监听器
 *
 * Revision 1.1  2010/03/09 07:38:07  cchun
 * Add:添加远动配置插件
 *
 */
public abstract class BaseDropListener implements DropTargetListener {
	@Override
	public void dragEnter(DropTargetEvent event) {
		if (event.detail == DND.DROP_DEFAULT) {
			if ((event.operations & DND.DROP_COPY) != 0) {
				event.detail = DND.DROP_COPY;
			} else {
				event.detail = DND.DROP_NONE;
			}
		}
	}

	@Override
	public void dragLeave(DropTargetEvent event) {
	}

	@Override
	public void dragOperationChanged(DropTargetEvent event) {
		if (event.detail == DND.DROP_DEFAULT) {
			if ((event.operations & DND.DROP_COPY) != 0) {
				event.detail = DND.DROP_COPY;
			} else {
				event.detail = DND.DROP_NONE;
			}
		}
	}

	@Override
	public void dragOver(DropTargetEvent event) {
		event.feedback = DND.FEEDBACK_SELECT | DND.FEEDBACK_SCROLL;
	}

//	@Override
//	public void drop(DropTargetEvent event) {
//		
//	}
//
	@Override
	public void dropAccept(DropTargetEvent event) {
		
	}
}
