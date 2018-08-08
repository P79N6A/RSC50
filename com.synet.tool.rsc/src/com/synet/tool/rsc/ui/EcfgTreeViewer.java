/**
 * Copyright (c) 2007-2013 上海思源弘瑞自动化有限公司. All rights reserved. 
 * This program is an eclipse Rich Client Application
 * designed for IED configuration and debuging.
 */
package com.synet.tool.rsc.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import com.shrcn.found.ui.tree.DeviceTreeViewer;
import com.shrcn.found.ui.tree.TreeViewerBuilder;

/**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2013-11-12
 */
/**
 * $Log: EcfgTreeViewer.java,v $
 * Revision 1.1  2013/11/12 05:54:00  cchun
 * Add:导航树类
 *
 */
public class EcfgTreeViewer extends DeviceTreeViewer {

	private static final int style = SWT.VIRTUAL | SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL;
	
	public EcfgTreeViewer(Composite parent, TreeViewerBuilder treeBuilder) {
		super(parent, style, treeBuilder, new EcfgTreeAdapter());
		
        
//      setSorter(new ViewerSorter() {
//			@Override
//			public int compare(Viewer viewer, Object e1, Object e2) {
//				if(!(e1 instanceof ITreeEntry) || !(e2 instanceof ITreeEntry)) {
//					return e1 instanceof ITreeEntry ? -1: (e2 instanceof ITreeEntry ? 1 : 0);
//				}
//				boolean isDev = (e1 instanceof BayIEDEntry) && (e2 instanceof BayIEDEntry);
//				ITreeEntry entry1 = (ITreeEntry) e1;
//				ITreeEntry entry2 = (ITreeEntry) e2;
//				int idx1 = entry1.getIndex();
//				int idx2 = entry2.getIndex();
//				if (idx1 == idx2 || isDev)
//					return StringUtil.compare(entry1.getName(), entry2.getName());
//				return new Integer(idx1).compareTo(idx2);
//		}
//		});

	}

}
