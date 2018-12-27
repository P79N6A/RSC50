package com.synet.tool.rsc.incr;

import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;

import com.shrcn.tool.found.das.BeanDaoService;
import com.shrcn.tool.found.das.HqlDaoService;
import com.shrcn.tool.found.das.impl.BeanDaoImpl;
import com.shrcn.tool.found.das.impl.HqlDaoImpl;
import com.synet.tool.rsc.compare.Difference;

public abstract class BaseIncreImportor implements IncreImportor {

	protected List<Difference> diffs;
	
	private IProgressMonitor monitor;
	
	public BaseIncreImportor(IProgressMonitor monitor, List<Difference> diffs) {
		this.monitor = monitor;
		this.diffs = diffs;
	}
	
}
