package com.synet.tool.rsc.incr;

import java.util.List;

import com.synet.tool.rsc.compare.Difference;

public class ConflictHandler {

	private Difference srcDiff;
	private Difference destDiff;
	
	public ConflictHandler(Difference srcDiff, Difference destDiff) {
		this.srcDiff = srcDiff;
		this.destDiff = destDiff;
	}
	
	public void handle() {
//		List<Difference> brothers = srcDiff.getParent().getChildren();
		System.out.println(srcDiff.getName() + "->" + destDiff.getName());
	}
}
