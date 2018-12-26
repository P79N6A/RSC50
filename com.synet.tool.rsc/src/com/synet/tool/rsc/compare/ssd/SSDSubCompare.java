package com.synet.tool.rsc.compare.ssd;

import org.dom4j.Element;
import org.eclipse.core.runtime.IProgressMonitor;

import com.synet.tool.rsc.compare.BaseCompare;
import com.synet.tool.rsc.compare.Difference;

public abstract class SSDSubCompare extends BaseCompare {

	protected Difference diffParent;

	public SSDSubCompare(Difference diffParent, Element ndSrc, Element ndDest, IProgressMonitor monitor) {
		super(ndSrc, ndDest, monitor);
		this.diffParent = diffParent;
	}

}
