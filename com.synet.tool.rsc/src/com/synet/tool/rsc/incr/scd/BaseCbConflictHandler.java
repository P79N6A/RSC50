package com.synet.tool.rsc.incr.scd;

import com.synet.tool.rsc.compare.Difference;
import com.synet.tool.rsc.incr.BaseConflictHandler;
import com.synet.tool.rsc.model.BaseCbEntity;
import com.synet.tool.rsc.model.Tb1046IedEntity;
import com.synet.tool.rsc.service.BaseCbEntityService;

public class BaseCbConflictHandler extends BaseConflictHandler {

	private BaseCbEntityService bcbServ = new BaseCbEntityService();
	private Tb1046IedEntity ied;
	private String cbId;
	
	public BaseCbConflictHandler(Difference diff) {
		super(diff);
		this.ied = (Tb1046IedEntity) diff.getParent().getParent().getData();
		this.cbId = diff.getName();
	}

	@Override
	public void setData() {
		BaseCbEntity cb = bcbServ.getCbEntity(ied, cbId);
		diff.setData(cb);
	}

	@Override
	public void handleAdd() {
		bcbServ.addCbEntity(ied, cbId, getDisInfo(), diff.getType());
	}

	@Override
	public void handleDelete() {
		bcbServ.deleteCbEntity(ied, cbId);
	}

	@Override
	public void handleUpate() {
		bcbServ.updateCbEntity(ied, cbId, getUpdateInfo(), diff.getType());
	}

}
