package com.synet.tool.rsc.incr;

import java.util.List;

import com.synet.tool.rsc.compare.Difference;

public class SsdIncreImportor extends BaseIncreImportor {

	public SsdIncreImportor(List<Difference> diffs) {
		super(diffs);
	}
	
	public void handle() {
		if (diffs == null || diffs.size() < 1) {
			return;
		}
		Difference diffRoot = diffs.get(0);
	}
}
