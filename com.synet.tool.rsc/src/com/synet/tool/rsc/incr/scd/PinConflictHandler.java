package com.synet.tool.rsc.incr.scd;

import com.synet.tool.rsc.compare.Difference;
import com.synet.tool.rsc.incr.BaseConflictHandler;
import com.synet.tool.rsc.model.Tb1046IedEntity;
import com.synet.tool.rsc.service.PinEntityService;

public class PinConflictHandler extends BaseConflictHandler {

	private PinEntityService pinServ = new PinEntityService();
	private Tb1046IedEntity ied;
	
	public PinConflictHandler(Difference diff) {
		super(diff);
		this.ied = (Tb1046IedEntity) diff.getParent().getParent().getData();
	}

	@Override
	public void handleAdd() {
		String pinRef = diff.getName();
		String doDesc = diff.getDesc();
		pinServ.addPin(ied, pinRef, doDesc);
	}

	@Override
	public void handleDelete() {
		String pinRef = diff.getName();
		pinServ.deletePin(ied, pinRef);
	}

	@Override
	public void handleUpate() {
		String pinRef = diff.getName();
		String doDesc = diff.getNewDesc();
		pinServ.updatePin(ied, pinRef, doDesc);
	}
}
