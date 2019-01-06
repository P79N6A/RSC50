package com.synet.tool.rsc.incr.ssd;

import java.util.Map;

import org.dom4j.Element;

import com.shrcn.found.xmldb.vtd.VTDXMLDBHelper;
import com.synet.tool.rsc.compare.CompareUtil;
import com.synet.tool.rsc.compare.Difference;
import com.synet.tool.rsc.compare.OP;
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
		String name = (OP.RENAME==diff.getOp()) ? diff.getNewName() : diff.getName();
		Tb1042BayEntity bay = bayServ.getBayEntityByName(name);
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
		bayServ.updateBay(diff.getName(), getUpdateInfo());
	}
	
	@Override
	public void mergeDifference() {
		String oldName = diff.getName();
		String newName = diff.getNewName();
		XmlHelperCache cache = XmlHelperCache.getInstance();
		VTDXMLDBHelper srcXmlHelper = cache.getSrcXmlHelper();
		VTDXMLDBHelper destXmlHelper = cache.getDestXmlHelper();
		Element ndSrc = srcXmlHelper.selectSingleNode("/SCL/Substation/VoltageLevel/Bay[@name='" + oldName + "']");
		Element ndDest = destXmlHelper.selectSingleNode("/SCL/Substation/VoltageLevel/Bay[@name='" + newName + "']");
		Difference diffNew = new BayCompare(null, ndSrc , ndDest, monitor).execute();
		diffNew.setName(oldName);
		diffNew.setNewName(newName);
		diffNew.setDesc(ndSrc.attributeValue("desc"));
		diffNew.setNewDesc(ndDest.attributeValue("desc"));
		diffNew.setOp(OP.RENAME);
		this.diff = diffNew;
	}

}
