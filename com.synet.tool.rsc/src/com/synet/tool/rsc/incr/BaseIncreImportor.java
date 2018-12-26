package com.synet.tool.rsc.incr;

import java.util.List;

import com.synet.tool.rsc.compare.Difference;

public abstract class BaseIncreImportor implements IncreImportor {

	protected List<Difference> diffs;
	
	public BaseIncreImportor(List<Difference> diffs) {
		this.diffs = diffs;
	}
	
}
