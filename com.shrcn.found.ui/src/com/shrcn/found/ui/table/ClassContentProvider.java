/**
 * Copyright (c) 2007-2010 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based on IEC61850 SCT.
 */
package com.shrcn.found.ui.table;

import org.eclipse.jface.viewers.ILazyContentProvider;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

/**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2012-11-2
 */
/**
 * $Log: ClassContentProvider.java,v $
 * Revision 1.1  2013/03/29 09:54:57  cchun
 * Add:创建
 *
 * Revision 1.4  2012/11/29 08:44:38  cchun
 * Update:每页数据改成50条
 *
 * Revision 1.3  2012/11/13 11:22:05  cchun
 * Refactor:统一序号属性名为seqnum
 *
 * Revision 1.2  2012/11/09 01:43:19  cchun
 * Update:去掉update标记
 *
 * Revision 1.1  2012/11/07 11:57:30  cchun
 * Refactor:修改类名
 *
 * Revision 1.1  2012/11/05 07:51:44  cchun
 * Add:懒加载表格
 *
 */
public class ClassContentProvider 
			implements IStructuredContentProvider, ILazyContentProvider {

	protected LazyTableViewer viewer;
	
	protected Object[] elements;
	protected int currPage = 0;
	protected final int PAGE_SIZE = 50;
	
	public ClassContentProvider(LazyTableViewer viewer) {
		this.viewer = viewer;
	}
	
	@Override
	public void updateElement(int index) {
		int page = index / PAGE_SIZE;
		if (page != currPage || elements == null) {
			currPage = page;
			elements = createModel(index, PAGE_SIZE);
		}
		int start = page * PAGE_SIZE;
		int end = start + PAGE_SIZE;
		end = Math.min(end, viewer.getTable().getItemCount());
		for (int i = start - (PAGE_SIZE*page); i < end - (PAGE_SIZE*page); i++) {
			viewer.replace(elements[i], i + (PAGE_SIZE*page));
		}
	}

	@Override
	public Object[] getElements(Object inputElement) {
		return elements;
	}
	
	protected Object[] createModel(int start, int pageSize) {
		
		return new Object[]{};
	}

	@Override
	public void dispose() {
	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		if (newInput == null)
			return;
	}

}
