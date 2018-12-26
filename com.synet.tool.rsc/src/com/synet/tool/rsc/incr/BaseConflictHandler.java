package com.synet.tool.rsc.incr;

import com.synet.tool.rsc.compare.Difference;

public abstract class BaseConflictHandler implements IConflictHandler {

	protected Difference diff;
	protected Difference destDiff;
	
	public BaseConflictHandler(Difference diff) {
		this(diff, null);
	}

	public BaseConflictHandler(Difference diff, Difference destDiff) {
		this.diff = diff;
		this.destDiff = destDiff;
	}
	
//	public void handle() {
////		List<Difference> brothers = diff.getParent().getChildren();
//		System.out.println(diff.getName() + "->" + destDiff.getName());
//	}
}
