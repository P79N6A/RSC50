package com.synet.tool.rsc.incr.ssd;

import com.synet.tool.rsc.compare.Difference;
import com.synet.tool.rsc.incr.BaseConflictHandler;
import com.synet.tool.rsc.model.Tb1042BayEntity;
import com.synet.tool.rsc.model.Tb1043EquipmentEntity;
import com.synet.tool.rsc.service.BayEntityService;
import com.synet.tool.rsc.service.EquipmentEntityService;

public class EqpConflictHandler extends BaseConflictHandler {

	private EquipmentEntityService eqpServ = new EquipmentEntityService();
	private BayEntityService bayServ = new BayEntityService();
	private Tb1042BayEntity bay;
	
	public EqpConflictHandler(Difference diff) {
		super(diff);
		this.bay = bayServ.getBayEntityByName(diff.getParent().getName());
	}
	
	@Override
	public void setData() {
		Tb1043EquipmentEntity equipment = eqpServ.getEquipment(bay, diff.getName());
		diff.setData(equipment);
	}

	@Override
	public void handleAdd() {
		eqpServ.addEquip(bay, diff.getType(), diff.getName(), getDisInfo());
	}

	@Override
	public void handleDelete() {
		eqpServ.deleteEquip(bay, diff.getName());		
	}

	@Override
	public void handleUpate() {
		eqpServ.udpateEquip(bay, diff.getType(), diff.getName(), getUpdateInfo());
	}

}
