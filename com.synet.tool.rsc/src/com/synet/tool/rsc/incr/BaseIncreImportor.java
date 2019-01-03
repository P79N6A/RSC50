package com.synet.tool.rsc.incr;

import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;

import com.synet.tool.rsc.compare.Difference;

public abstract class BaseIncreImportor implements IncreImportor {

	protected List<Difference> diffs;
	
	protected IProgressMonitor monitor;
	
	public BaseIncreImportor(IProgressMonitor monitor, List<Difference> diffs) {
		this.monitor = monitor;
		this.diffs = diffs;
	}
	
}
