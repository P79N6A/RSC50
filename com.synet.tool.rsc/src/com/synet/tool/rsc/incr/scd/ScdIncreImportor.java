package com.synet.tool.rsc.incr.scd;

import java.util.List;

import com.synet.tool.rsc.compare.Difference;
import com.synet.tool.rsc.incr.BaseIncreImportor;

public class ScdIncreImportor extends BaseIncreImportor {

	public ScdIncreImportor(List<Difference> diffs) {
		super(diffs);
	}
	
	public void handle() {
		if (diffs == null || diffs.size() < 1) {
			return;
		}
		Difference diffFirst = diffs.get(0);
	}
}
