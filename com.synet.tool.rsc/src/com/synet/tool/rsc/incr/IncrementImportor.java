package com.synet.tool.rsc.incr;

import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;

import com.synet.tool.rsc.compare.Difference;

public class IncrementImportor extends BaseIncreImportor {
	
	public IncrementImportor(IProgressMonitor monitor, List<Difference> diffs) {
		super(monitor, diffs);
	}
	
	public void handle() {
		if (diffs == null || diffs.size() < 1) {
			return;
		}
		Difference diffFirst = diffs.get(0);
		if ("Substation".equals(diffFirst.getType())) {
			IConflictHandler conflictHandler = ConflictHandlerFactory.createConflict(EnumConflict.Sta, diffFirst);
			conflictHandler.handle();
		} else {
			for (Difference diff : diffs) {
				ConflictHandlerFactory.createConflict(EnumConflict.IED, diff).handle();
			}
		}
	}
}
