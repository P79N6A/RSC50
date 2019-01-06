package com.synet.tool.rsc.incr.board;

import com.synet.tool.rsc.compare.Difference;
import com.synet.tool.rsc.incr.BaseConflictHandler;
import com.synet.tool.rsc.model.Tb1046IedEntity;
import com.synet.tool.rsc.service.IedEntityService;

public class IEDBoardConflictHandler extends BaseConflictHandler {
	
	private IedEntityService iedServ = new IedEntityService();

	public IEDBoardConflictHandler(Difference diff) {
		super(diff);
	}

	@Override
	public void setData() {
		Tb1046IedEntity ied = iedServ.getIedEntityByDevName(diff.getName());
		diff.setData(ied);
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
