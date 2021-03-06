package com.synet.tool.rsc.incr.ssd;

import org.dom4j.Element;

import com.shrcn.found.xmldb.vtd.VTDXMLDBHelper;
import com.synet.tool.rsc.compare.Difference;
import com.synet.tool.rsc.compare.OP;
import com.synet.tool.rsc.compare.XmlHelperCache;
import com.synet.tool.rsc.compare.ssd.EquipmentCompare;
import com.synet.tool.rsc.incr.BaseConflictHandler;
import com.synet.tool.rsc.model.Tb1042BayEntity;
import com.synet.tool.rsc.model.Tb1043EquipmentEntity;
import com.synet.tool.rsc.service.EquipmentEntityService;

public class EqpConflictHandler extends BaseConflictHandler {

	private EquipmentEntityService eqpServ = new EquipmentEntityService();
	private Tb1042BayEntity bay;
	
	public EqpConflictHandler(Difference diff) {
		super(diff);
		this.bay = (Tb1042BayEntity) diff.getParent().getData();
	}
	
	@Override
	public void setData() {
		String name = (OP.RENAME==diff.getOp()) ? diff.getNewName() : diff.getName();
		Tb1043EquipmentEntity equipment = eqpServ.getEquipment(bay, name);
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

	@Override
	public void mergeDifference() {
		String oldName = diff.getName();
		String newName = diff.getNewName();
		XmlHelperCache cache = XmlHelperCache.getInstance();
		VTDXMLDBHelper srcXmlHelper = cache.getSrcXmlHelper();
		VTDXMLDBHelper destXmlHelper = cache.getDestXmlHelper();
		Element ndSrc = srcXmlHelper.selectSingleNode("/SCL/Substation/VoltageLevel/Bay/*[@name='" + oldName + "']");
		Element ndDest = destXmlHelper.selectSingleNode("/SCL/Substation/VoltageLevel/Bay/*[@name='" + newName + "']");
		Difference diffNew = new EquipmentCompare(null, ndSrc , ndDest, monitor).execute();
		diffNew.setName(oldName);
		diffNew.setNewName(newName);
		diffNew.setDesc(ndSrc.attributeValue("desc"));
		diffNew.setNewDesc(ndDest.attributeValue("desc"));
		diffNew.setOp(OP.RENAME);
		this.diff = diffNew;
	}
}
