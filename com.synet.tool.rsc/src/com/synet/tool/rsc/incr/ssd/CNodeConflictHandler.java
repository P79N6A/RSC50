package com.synet.tool.rsc.incr.ssd;

import com.synet.tool.rsc.compare.Difference;
import com.synet.tool.rsc.incr.BaseConflictHandler;
import com.synet.tool.rsc.model.Tb1045ConnectivitynodeEntity;
import com.synet.tool.rsc.service.CNodeEntityService;

public class CNodeConflictHandler extends BaseConflictHandler {

	private CNodeEntityService cnodeServ = new CNodeEntityService();
	
	public CNodeConflictHandler(Difference diff) {
		super(diff);
	}
	
	@Override
	public void setData() {
		diff.setData(getByName(Tb1045ConnectivitynodeEntity.class, "f1045Name"));
	}

	@Override
	public void handleAdd() {
		cnodeServ.addCNode(diff.getName(), getDisInfo());
	}

	@Override
	public void handleDelete() {
		cnodeServ.deleteCNode(diff.getName());
	}

	@Override
	public void handleUpate() {
		cnodeServ.updateCNode(diff.getName(), getUpdateInfo());
	}

}
