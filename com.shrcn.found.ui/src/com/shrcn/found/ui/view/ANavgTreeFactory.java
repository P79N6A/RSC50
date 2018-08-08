/**
 * Copyright (c) 2007-2009 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based on IEC61850 SCT.
 */
package com.shrcn.found.ui.view;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.ui.progress.IElementCollector;

import com.shrcn.found.ui.model.ITreeEntry;
import com.shrcn.found.ui.tree.TreeViewerBuilder;

/**
 * 
 * @author 孙春颖(mailto:scy@shrcn.com)
 * @version 1.0, 2014-05-27
 */
public abstract class ANavgTreeFactory {
	
	protected List<ITreeEntry> data = new ArrayList<ITreeEntry>();
	protected TreeViewerBuilder treeBuilder;
	
	public TreeViewerBuilder getTreeBuilder() {
		return treeBuilder;
	}

	public List<ITreeEntry> getProjectData() {
		return data;
	}
	
	public void loadProject(){
		data.clear();
	}
	
	public void fillCfgItems(ITreeEntry treeEntry, IElementCollector collector, IProgressMonitor monitor) {
		treeBuilder.fillCfgItems(treeEntry, collector, monitor);
	}
}
