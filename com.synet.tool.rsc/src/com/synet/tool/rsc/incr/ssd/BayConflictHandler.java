package com.synet.tool.rsc.incr.ssd;

import java.util.Map;

import org.dom4j.Element;

import com.shrcn.found.xmldb.vtd.VTDXMLDBHelper;
import com.synet.tool.rsc.compare.CompareUtil;
import com.synet.tool.rsc.compare.Difference;
import com.synet.tool.rsc.compare.XmlHelperCache;
import com.synet.tool.rsc.compare.ssd.BayCompare;
import com.synet.tool.rsc.incr.BaseConflictHandler;
import com.synet.tool.rsc.model.Tb1041SubstationEntity;
import com.synet.tool.rsc.model.Tb1042BayEntity;
import com.synet.tool.rsc.service.BayEntityService;

public class BayConflictHandler extends BaseConflictHandler {
	
	private BayEntityService bayServ = new BayEntityService();

	public BayConflictHandler(Difference diff) {
		super(diff);
	}

	@Override
	public void setData() {
		Tb1042BayEntity bay = (Tb1042BayEntity) getByName(Tb1042BayEntity.class, "f1042Name");
		diff.setData(bay);
	}
	
	@Override
	public void handleAdd() {
		Difference diffVol = diff.getParent();
		Map<String, String> updateInfo = CompareUtil.getUpdateInfo(diffVol.getMsg());
		int ivol = Integer.parseInt(updateInfo.get("vol"));
		Tb1041SubstationEntity station = (Tb1041SubstationEntity) diffVol.getParent().getData();
		bayServ.addBay(station , ivol, diff.getName(), diff.getDesc());
	}

	@Override
	public void handleDelete() {
		bayServ.deleteBay(diff.getName());
	}

	@Override
	public void handleUpate() {
		bayServ.updateBay(diff.getName(), getDisInfo());
	}
	
	@Override
	public void mergeDifference() {
		String oldName = diff.getName();
		String newName = diff.getNewName();
		XmlHelperCache cache = XmlHelperCache.getInstance();
		VTDXMLDBHelper srcXmlHelper = cache.getSrcXmlHelper();
		VTDXMLDBHelper destXmlHelper = cache.getDestXmlHelper();
		Element ndSrc = srcXmlHelper.selectSingleNode("/Substation/VoltageLevel/Bay[@name='" + oldName + "']");
		Element ndDest = destXmlHelper.selectSingleNode("/Substation/VoltageLevel/Bay[@name='" + newName + "']");
		Difference diffNew = new BayCompare(diff.getParent(), ndSrc , ndDest, monitor).execute();
		diffNew.setName(oldName);
		diffNew.setNewName(newName);
		diffNew.setDesc(ndSrc.attributeValue("desc"));
		diffNew.setNewDesc(ndDest.attributeValue("desc"));
		diff.getParent().getChildren().remove(diff);
		this.diff = diffNew;
	}

}
