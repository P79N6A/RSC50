package com.synet.tool.rsc.incr.ssd;

import com.synet.tool.rsc.compare.Difference;
import com.synet.tool.rsc.incr.BaseConflictHandler;
import com.synet.tool.rsc.model.Tb1043EquipmentEntity;
import com.synet.tool.rsc.service.LNodeEntityService;

public class LNodeConflictHandler extends BaseConflictHandler {

	private LNodeEntityService lnodeServ = new LNodeEntityService();
	private Tb1043EquipmentEntity equipment; 
	
	public LNodeConflictHandler(Difference diff) {
		super(diff);
		this.equipment = (Tb1043EquipmentEntity) diff.getParent().getData();
	}
	
	@Override
	public void setData() {
	}

	@Override
	public void handleAdd() {
		lnodeServ.addLNode(equipment, getDisInfo());
	}

	@Override
	public void handleDelete() {
		lnodeServ.deleteLNode(getDisInfo());
	}

	@Override
	public void handleUpate() {
	}

}
