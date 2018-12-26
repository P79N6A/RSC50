package com.synet.tool.rsc.incr;

import java.util.List;

import com.synet.tool.rsc.compare.Difference;

public class IncrementImportor extends BaseIncreImportor {

	public IncrementImportor(List<Difference> diffs) {
		super(diffs);
	}
	
	public void handle() {
		if (diffs == null || diffs.size() < 1) {
			return;
		}
		Difference diffFirst = diffs.get(0);
		if ("Substation".equals(diffFirst.getType())) {
			new SsdIncreImportor(diffs).handle();
		} else {
			new ScdIncreImportor(diffs).handle();
		}
	}
}
