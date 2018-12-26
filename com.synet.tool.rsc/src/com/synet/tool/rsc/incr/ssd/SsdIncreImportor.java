package com.synet.tool.rsc.incr.ssd;

import java.util.List;

import com.synet.tool.rsc.compare.Difference;
import com.synet.tool.rsc.incr.BaseIncreImportor;

public class SsdIncreImportor extends BaseIncreImportor {

	public SsdIncreImportor(List<Difference> diffs) {
		super(diffs);
	}
	
	public void handle() {
		if (diffs == null || diffs.size() < 1) {
			return;
		}
		Difference diffRoot = diffs.get(0);
//		new ConflictHandler(diff, diffDest).handle();
//		ConsoleManager.getInstance().append("重命名操作已完成！");
		
	}
}
