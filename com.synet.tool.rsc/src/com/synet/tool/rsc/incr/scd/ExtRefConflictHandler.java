package com.synet.tool.rsc.incr.scd;

import java.util.Map;

import com.synet.tool.rsc.compare.Difference;
import com.synet.tool.rsc.incr.BaseConflictHandler;
import com.synet.tool.rsc.model.Tb1046IedEntity;
import com.synet.tool.rsc.model.Tb1061PoutEntity;
import com.synet.tool.rsc.model.Tb1062PinEntity;
import com.synet.tool.rsc.service.CircuitEntityService;
import com.synet.tool.rsc.service.PinEntityService;
import com.synet.tool.rsc.service.PoutEntityService;

public class ExtRefConflictHandler extends BaseConflictHandler {

	private PinEntityService pinServ = new PinEntityService();
	private PoutEntityService poutServ = new PoutEntityService();
	private CircuitEntityService circuitServ = new CircuitEntityService();
	private Tb1046IedEntity ied;
	
	public ExtRefConflictHandler(Difference diff) {
		super(diff);
		this.ied = (Tb1046IedEntity) diff.getParent().getParent().getData();
	}

	@Override
	public void handleAdd() {
		Map<String, String> disInfo = getDisInfo();
		String extIedName = disInfo.get("iedName");
		String poutRef = disInfo.get("fcdaRef");
		Tb1061PoutEntity poutEntity = getPoutEntity(extIedName, poutRef);
		Tb1062PinEntity pinEntity = getPinEntity();
		circuitServ.addCircuitEntity(pinEntity, poutEntity);
	}

	@Override
	public void handleDelete() {
		Tb1062PinEntity pinEntity = getPinEntity();
		circuitServ.deleteCircuitEntity(pinEntity);
	}
	
	private String getIecRev(String ref, String fc) {
		ref = ref.replace('.', '$');
		int p = ref.indexOf('$');
		return ref.substring(0, p) + "$" + fc + "$" + ref.substring(p + 1);
	}

	private Tb1062PinEntity getPinEntity() {
		String pinRef = diff.getName();
		Tb1062PinEntity pinEntity = pinServ.getPin(ied, getIecRev(pinRef, "ST"));
		if (pinEntity == null) {
			pinEntity = pinServ.getPin(ied, getIecRev(pinRef, "MX"));
		}
		return pinEntity;
	}
	
	@Override
	public void handleUpate() {
		Map<String, String> updateInfo = getUpdateInfo();
		String poutRef = updateInfo.get("fcdaRef");
		Tb1062PinEntity pinEntity = getPinEntity();
		if (poutRef != null) {
			String extIedName = updateInfo.get("iedName");
			Tb1061PoutEntity poutEntity = getPoutEntity(extIedName, poutRef);
			circuitServ.updateCircuitEntity(pinEntity, poutEntity);
		}
	}

	private Tb1061PoutEntity getPoutEntity(String extIedName, String poutRef) {
		Tb1061PoutEntity poutEntity = poutServ.getPoutEntity(extIedName, getIecRev(poutRef, "ST"));
		if (poutEntity == null) {
			poutEntity = poutServ.getPoutEntity(extIedName, getIecRev(poutRef, "MX"));
		}
		return poutEntity;
	}

}
