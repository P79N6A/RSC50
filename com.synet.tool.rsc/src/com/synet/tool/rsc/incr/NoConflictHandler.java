package com.synet.tool.rsc.incr;

import com.synet.tool.rsc.compare.Difference;
import com.synet.tool.rsc.incr.BaseConflictHandler;

public class NoConflictHandler extends BaseConflictHandler {

	public NoConflictHandler(Difference diff) {
		super(diff);
		
	}

	@Override
	public void handleAdd() {
	}

	@Override
	public void handleDelete() {
	}

	@Override
	public void handleUpate() {
	}


}
