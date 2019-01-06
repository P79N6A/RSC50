package com.synet.tool.rsc.compare;

import org.eclipse.core.runtime.IProgressMonitor;

public abstract class ObjectComparator implements IComparator {

	protected Difference diffParent;
	protected IProgressMonitor monitor;

	public ObjectComparator(Difference diffParent, IProgressMonitor monitor) {
		this.diffParent = diffParent;
		this.monitor = monitor;
	}
	
	public ObjectComparator(IProgressMonitor monitor) {
		this(null, monitor);
	}
	
}
