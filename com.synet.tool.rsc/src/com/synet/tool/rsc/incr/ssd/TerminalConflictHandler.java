package com.synet.tool.rsc.incr.ssd;

import com.synet.tool.rsc.compare.Difference;
import com.synet.tool.rsc.incr.BaseConflictHandler;
import com.synet.tool.rsc.model.Tb1043EquipmentEntity;
import com.synet.tool.rsc.service.TerminalEntityService;

public class TerminalConflictHandler extends BaseConflictHandler {

	private TerminalEntityService tmServ = new TerminalEntityService();
	private Tb1043EquipmentEntity equipment; 
	
	public TerminalConflictHandler(Difference diff) {
		super(diff);
		this.equipment = (Tb1043EquipmentEntity) diff.getParent().getData();
	}
	
	@Override
	public void handleAdd() {
		tmServ.addTerminal(equipment, diff.getName(), getDisInfo());
	}

	@Override
	public void handleDelete() {
		tmServ.deleteTerminal(equipment, diff.getName());
	}

	@Override
	public void handleUpate() {
		tmServ.updateTerminal(equipment, diff.getName(), getUpdateInfo());
	}

}
