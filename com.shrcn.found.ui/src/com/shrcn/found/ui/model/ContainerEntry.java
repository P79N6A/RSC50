/**
 * Copyright (c) 2007-2013 上海思弘瑞电力控制技术有限公司. All rights reserved. 
 * This program is an eclipse Rich Client Application
 * designed for IED configuration and debuging.
 */
package com.shrcn.found.ui.model;

import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.jobs.ISchedulingRule;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.progress.IDeferredWorkbenchAdapter;
import org.eclipse.ui.progress.IElementCollector;

import com.shrcn.found.common.util.StringUtil;
import com.shrcn.found.ui.util.IconsManager;

/**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2013-4-6
 */
/**
 * $Log: ContainerEntry.java,v $
 * Revision 1.1  2013/04/06 05:33:40  cchun
 * Add:导航树基础类
 *
 */
public abstract class ContainerEntry extends BaseTreeEntry implements IDeferredWorkbenchAdapter {

	public ContainerEntry(ITreeEntry parent, String name, String desc,
			String icon) {
		super(parent, name, desc, icon);
	}
	
	@Override
	public void fetchDeferredChildren(Object object,
			IElementCollector collector, IProgressMonitor monitor) {
		if (!(object instanceof ITreeEntry))
			return;
		ITreeEntry entry = (ITreeEntry) object;
		List<ITreeEntry> children = entry.getChildren();
		int size = children.size();
		monitor.beginTask("Loading", size);
		for(ITreeEntry ent:children) {
			collector.add(ent, monitor);
			monitor.worked(1);
			if (monitor.isCanceled()) {
				break;
			}
		}
		monitor.done();
	}

	@Override
	public ISchedulingRule getRule(Object object) {
		return null;
	}

	@Override
	public boolean isContainer() {
		return true;
	}

	@Override
	public Object[] getChildren(Object o) {
		return children.toArray();
	}

	@Override
	public ImageDescriptor getImageDescriptor(Object object) {
		return IconsManager.getInstance().getImageDescriptor(icon);
	}

	@Override
	public String getLabel(Object o) {
		return StringUtil.getLabel(name, desc);
	}

	@Override
	public Object getParent(Object o) {
		return parent;
	}

}
