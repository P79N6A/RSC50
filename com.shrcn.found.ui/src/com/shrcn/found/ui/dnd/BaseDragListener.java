/**
 * Copyright (c) 2007-2009 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based on IEC61850 SCT.
 */
package com.shrcn.found.ui.dnd;

import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.DragSourceListener;
import org.eclipse.swt.dnd.Transfer;

/**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2010-3-11
 */
/**
 * $Log: BaseDragListener.java,v $
 * Revision 1.1  2013/08/14 03:01:49  cchun
 * Add:添加
 *
 * Revision 1.2  2011/03/11 01:27:12  cchun
 * Update:一般形式的拖拽监听器
 *
 * Revision 1.1  2010/03/11 08:24:19  cchun
 * Update:完善RTU表格
 *
 */
public abstract class BaseDragListener implements DragSourceListener {

	private Transfer transfer;
	
	public BaseDragListener(Transfer transfer) {
		this.transfer = transfer;
	}
	
	@Override
	public void dragFinished(DragSourceEvent event) {
		if (!event.doit)
			return;
	}
	
	@Override
	public void dragSetData(DragSourceEvent event) {
		if (transfer.isSupportedType(event.dataType)) {
			event.data = getDragData();
		}
	}

	protected abstract Object getDragData();
}
