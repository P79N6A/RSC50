/**
 * Copyright (c) 2007-2013 上海思弘瑞电力控制技术有限公司. All rights reserved. 
 * This program is an eclipse Rich Client Application
 * designed for IED configuration and debuging.
 */
package com.shrcn.found.ui.tree;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;

import com.shrcn.found.common.util.StringUtil;
import com.shrcn.found.ui.model.ITreeEntry;

/**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2013-5-17
 */
/**
 * $Log: EntryIndexSorter.java,v $
 * Revision 1.2  2013/05/30 11:02:34  cchun
 * Fix Bug:修复导航树乱序bug
 *
 * Revision 1.1  2013/05/17 08:33:04  cchun
 * Update:添加节点排序
 *
 */
public class EntryIndexSorter extends ViewerSorter {
	@Override
	public int compare(Viewer viewer, Object e1, Object e2) {
		if(!(e1 instanceof ITreeEntry) || !(e2 instanceof ITreeEntry)) {
			return e1 instanceof ITreeEntry ? -1: (e2 instanceof ITreeEntry ? 1 : 0);
		}
		ITreeEntry entry1 = (ITreeEntry) e1;
		ITreeEntry entry2 = (ITreeEntry) e2;
		int idx1 = entry1.getIndex();
		int idx2 = entry2.getIndex();
		if (idx1 != idx2)
			return new Integer(idx1).compareTo(idx2);
		return StringUtil.compare(entry1.getName(), entry2.getName());
	}
}
