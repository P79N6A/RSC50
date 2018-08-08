/**
 * Copyright (c) 2007-2013 上海思弘瑞电力控制技术有限公司. All rights reserved. 
 * This program is an eclipse Rich Client Application
 * designed for IED configuration and debuging.
 */
package com.synet.tool.rsc.util;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.ui.progress.IElementCollector;

import com.shrcn.found.ui.model.IEDEntry;
import com.shrcn.found.ui.model.ITreeEntry;

/**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2013-4-5
 */
/**
 * $Log: BayIEDEntry.java,v $
 * Revision 1.3  2013/11/12 05:53:12  cchun
 * Update:添加属性
 *
 * Revision 1.2  2013/05/17 06:26:04  cxc
 * Refactor：更改布局
 *
 * Revision 1.1  2013/04/06 05:27:46  cchun
 * Add:IED配置项
 *
 */
public class BayIEDEntry extends IEDEntry {
	
	private boolean syned;

	public BayIEDEntry(ITreeEntry parent, String name, String desc, String icon) {
		super(parent, name, desc, icon);
	}

	@Override
	public void fetchDeferredChildren(Object object,
			IElementCollector collector, IProgressMonitor monitor) {
		NavgTreeFactory.getInstance().fillCfgItems((ITreeEntry) object, collector, monitor);
	}

	public boolean isSyned() {
		return syned;
	}

	public void setSyned(boolean syned) {
		this.syned = syned;
	}
}
