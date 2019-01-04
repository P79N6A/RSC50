package com.synet.tool.rsc.incr.scd;

import com.synet.tool.rsc.compare.Difference;
import com.synet.tool.rsc.incr.BaseConflictHandler;
import com.synet.tool.rsc.model.Tb1046IedEntity;
import com.synet.tool.rsc.model.Tb1054RcbEntity;
import com.synet.tool.rsc.service.RcbEntityService;

public class RcbConflictHandler extends BaseConflictHandler {

	private RcbEntityService rcbServ = new RcbEntityService();
	private Tb1046IedEntity ied;
	private String cbRef;
	
	public RcbConflictHandler(Difference diff) {
		super(diff);
		this.ied = (Tb1046IedEntity) diff.getParent().getParent().getData();
		this.cbRef = diff.getName();
	}

	@Override
	public void setData() {
		String setName = diff.getName();
		Tb1054RcbEntity rcb = rcbServ.getRcbEntity(ied, setName);
		diff.setData(rcb);
	}

	@Override
	public void handleAdd() {
		rcbServ.addCbEntity(ied, cbRef, getDisInfo());
	}

	@Override
	public void handleDelete() {
		rcbServ.deleteCbEntity(ied, cbRef);
	}

	@Override
	public void handleUpate() {
		rcbServ.updateCbEntity(ied, cbRef, getUpdateInfo());
	}

}
