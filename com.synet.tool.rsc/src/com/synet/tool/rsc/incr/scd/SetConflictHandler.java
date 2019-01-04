package com.synet.tool.rsc.incr.scd;

import com.synet.tool.rsc.compare.Difference;
import com.synet.tool.rsc.incr.BaseConflictHandler;
import com.synet.tool.rsc.model.Tb1046IedEntity;
import com.synet.tool.rsc.model.Tb1057SgcbEntity;
import com.synet.tool.rsc.service.SgcbEntityService;

public class SetConflictHandler extends BaseConflictHandler {

	private SgcbEntityService sgcbServ = new SgcbEntityService();
	private Tb1046IedEntity ied;
	private String cbRef;
	
	public SetConflictHandler(Difference diff) {
		super(diff);
		this.ied = (Tb1046IedEntity) diff.getParent().getParent().getData();
		this.cbRef = diff.getName();
	}

	@Override
	public void setData() {
		String setName = diff.getName();
		Tb1057SgcbEntity sgcb = sgcbServ.getSgcbEntity(ied, setName);
		diff.setData(sgcb);
	}

	@Override
	public void handleAdd() {
		sgcbServ.addCbEntity(ied, cbRef, getDisInfo());
	}

	@Override
	public void handleDelete() {
		sgcbServ.deleteCbEntity(ied, cbRef);
	}

	@Override
	public void handleUpate() {
		sgcbServ.updateCbEntity(ied, cbRef, getUpdateInfo());
	}

}
