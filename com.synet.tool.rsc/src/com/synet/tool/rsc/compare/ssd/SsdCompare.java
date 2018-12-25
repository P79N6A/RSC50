package com.synet.tool.rsc.compare.ssd;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.dom4j.Element;

import com.shrcn.found.common.util.StringUtil;
import com.synet.tool.rsc.compare.CompareUtil;
import com.synet.tool.rsc.compare.Difference;
import com.synet.tool.rsc.compare.ICompare;
import com.synet.tool.rsc.compare.OP;

public class SsdCompare implements ICompare {

	private Element ssdNdSrc;
	private Element ssdNdDest;

	public SsdCompare(Element ssdNdSrc, Element ssdNdDest) {
		this.ssdNdSrc = ssdNdSrc;
		this.ssdNdDest = ssdNdDest;
	}
	
	private String getName(Element nd) {
		return nd.attributeValue("name");
	}
	
	private void compareAtts(Difference diff, Element ndSrc, Element ndDest) {
		String msg = CompareUtil.compare(ndSrc, ndDest);
		diff.setMsg(msg);
		diff.setOp(OP.UPDATE);
	}
	
	@Override
	public Difference execute() {
		if (CompareUtil.matchMd5(ssdNdSrc, ssdNdDest)) {
			return null;
		}
		Difference diffRoot = CompareUtil.addUpdateDiff(null, ssdNdSrc, ssdNdDest);
		compareAtts(diffRoot, ssdNdSrc, ssdNdDest);
		Iterator<Element> volIterator = ssdNdSrc.elementIterator("VoltageLevel");
		Map<String, Element> destChildrenMap = CompareUtil.getChildrenMapByAtt(ssdNdDest, "name");
		while(volIterator.hasNext()) {
			Element ndVolSrc = volIterator.next();
			String volName = ndVolSrc.attributeValue("name");
			Element ndVolDest = destChildrenMap.get(volName);
			if (CompareUtil.matchMd5(ndVolSrc, ndVolDest)) {
				continue;
			}
			if (ndVolDest == null) { // 已删除
				Difference diffVol = CompareUtil.addDiffByAttName(diffRoot, ndVolSrc, "name", OP.DELETE);
				fillVolDiffs(diffVol, ndVolSrc, OP.DELETE);
			} else {
				if (!CompareUtil.matchMd5(ndVolSrc, ndVolDest)) {
					Difference diffVol = CompareUtil.addUpdateDiff(diffRoot, ndVolSrc, ndVolDest);
					compareVolDiffs(diffVol, ndVolSrc, ndVolDest);
				}
				destChildrenMap.remove(volName);
			}
		}
		if (destChildrenMap.size() > 0) {
			for (Element ndVolDest : destChildrenMap.values()) {
				Difference diffVol = CompareUtil.addDiffByAttName(diffRoot, ndVolDest, "name", OP.ADD);
				fillVolDiffs(diffVol, ndVolDest, OP.ADD);
			}
		}
		return diffRoot;
	}
	
	/**
	 * 比较电压等级差异
	 * @param diffVol
	 * @param ndVolSrc
	 * @param ndVolDest
	 */
	private void compareVolDiffs(Difference diffVol, Element ndVolSrc, Element ndVolDest) {
		Iterator<Element> bayIterator = ndVolSrc.elementIterator("Bay");
		Map<String, Element> destBayMap = CompareUtil.getChildrenMapByAtt(ndVolDest, "name");
		while(bayIterator.hasNext()) {
			Element ndBaySrc = bayIterator.next();
			String bayName = ndBaySrc.attributeValue("name");
			Element ndBayDest = destBayMap.get(bayName);
			if (ndBayDest == null) {	// 已删除
				fillBayDiffs(diffVol, ndBaySrc, OP.DELETE);
			} else {
				destBayMap.remove(bayName);
				if (CompareUtil.matchMd5(ndBaySrc, ndBayDest)) {
					continue;
				}
				compareBayDiffs(diffVol, ndBaySrc, ndBayDest);
			}
		}
		if (destBayMap.size() > 0) {
			for (Element ndBayDest : destBayMap.values()) {
				fillBayDiffs(diffVol, ndBayDest, OP.ADD);
			}
		}
	}
	
	/**
	 * 比较间隔差异
	 * @param diffVol
	 * @param ndBaySrc
	 * @param ndBayDest
	 */
	private void compareBayDiffs(Difference diffVol, Element ndBaySrc, Element ndBayDest) {
		Difference diffBay = CompareUtil.addUpdateDiff(diffVol, ndBaySrc, ndBayDest);
		Map<String, Element> destBaySubMap = CompareUtil.getChildrenMapByAtt(ndBayDest, "name");
		Iterator<Element> eqpIterator = ndBaySrc.elementIterator();
		while(eqpIterator.hasNext()) {
			Element ndBaySubSrc = eqpIterator.next();
			String ndName = ndBaySubSrc.getName();
			if (!isEquipmentNode(ndName) && !isConnectivityNode(ndName)) {
				continue;
			}
			String baySubName = ndBaySubSrc.attributeValue("name");
			Element ndBaySubDest = destBaySubMap.get(baySubName);
			if (ndBaySubDest == null) {	// 已删除
				fillBaySub(diffBay, ndBaySubSrc, OP.DELETE);
			} else {
				destBaySubMap.remove(baySubName);
				if (CompareUtil.matchMd5(ndBaySubSrc, ndBaySubDest)) {
					continue;
				}
				if (isEquipmentNode(ndName)) {	// 设备
					compareBaySubDiffs(diffBay, ndBaySubSrc, ndBaySubDest);
				} else {						// 拓扑连接点
					CompareUtil.addUpdateDiff(diffBay, ndBaySubSrc, ndBaySubDest);
				}
			}
		}
		if (destBaySubMap.size() > 0) {
			for (Element ndBaySubDest : destBaySubMap.values()) {
				fillBaySub(diffBay, ndBaySubDest, OP.ADD);
			}
		}
		CompareUtil.sortChildren(diffBay);
	}
	
	private String getLNodeXpath(Element ndLNode) {
		String iedName = CompareUtil.getAttribute(ndLNode, "iedName");
		String ldInst = CompareUtil.getAttribute(ndLNode, "ldInst");
		String lnClass = CompareUtil.getAttribute(ndLNode, "lnClass"); 
		String lnInst = CompareUtil.getAttribute(ndLNode, "lnInst"); 
		String prefix = CompareUtil.getAttribute(ndLNode, "prefix");
		return "./LNode[@iedName='" + iedName + "'][@ldInst='" + ldInst + "'][@lnClass='" + lnClass + 
				"'][@lnInst='" + lnInst + "'][@prefix='" + prefix + "']";
	}
	
	private Map<String, Element> getEqpSubMap(Element ndEqp) {
		Map<String, Element> eqpSubMap = new HashMap<String, Element>();
		Iterator<Element> eqpSubIterator = ndEqp.elementIterator();
		while(eqpSubIterator.hasNext()) {
			Element ndEqpSub = eqpSubIterator.next();
			String ndEqpSubName = ndEqpSub.getName();
			if ("LNode".equals(ndEqpSubName)) {
				String xpath = getLNodeXpath(ndEqpSub);
				eqpSubMap.put(xpath, ndEqpSub);
			} else if ("Terminal".equals(ndEqpSubName)) {
				String tName = CompareUtil.getAttribute(ndEqpSub, "name");
				if (!"".endsWith(tName)) {
					eqpSubMap.put(tName, ndEqpSub);
				}
			}
		}
		return eqpSubMap;
	}
	
	private void compareBaySubDiffs(Difference diffBay, Element ndBaySubSrc, Element ndBaySubDest) {
		Difference diffEqp = CompareUtil.addUpdateDiff(diffBay, ndBaySubSrc, ndBaySubDest);
		Iterator<Element> eqpSubIterator = ndBaySubSrc.elementIterator();
		Map<String, Element> eqpSubDestMap = getEqpSubMap(ndBaySubDest);
		while(eqpSubIterator.hasNext()) {
			Element ndEqpSubSrc = eqpSubIterator.next();
			String ndEqpSubName = ndEqpSubSrc.getName();
			String key = "";
			if ("LNode".equals(ndEqpSubName)) {
				key = getLNodeXpath(ndEqpSubSrc);
				
			} else if ("Terminal".equals(ndEqpSubName)) {
				key = CompareUtil.getAttribute(ndEqpSubSrc, "name");
			}
			if ("".equals(key)) {
				continue;
			}
			Element ndEqpSubDest = eqpSubDestMap.get(key);
			if (ndEqpSubDest == null) {
				addDifference(diffEqp, ndEqpSubSrc, OP.DELETE);
			} else {
				if ("Terminal".equals(ndEqpSubName)) {
					String msg = CompareUtil.compare(ndEqpSubSrc, ndEqpSubDest, "cNodeName", "connectivityNode");
					if (!StringUtil.isEmpty(msg)) {
						new Difference(diffEqp, ndEqpSubName, key, msg, OP.UPDATE);
					}
				}
				eqpSubDestMap.remove(key);
			}
		}
		if (eqpSubDestMap.size() > 0) {
			for (Element ndEqpSubDest : eqpSubDestMap.values()) {
				addDifference(diffEqp, ndEqpSubDest, OP.ADD);
			}
		}
		CompareUtil.sortChildren(diffEqp);
	}

	private boolean isConnectivityNode(String ndName) {
		return "ConnectivityNode".equals(ndName);
	}

	private boolean isEquipmentNode(String ndName) {
		return "ConductingEquipment".equals(ndName) || "PowerTransformer".equals(ndName);
	}

	private void fillVolDiffs(Difference diffVol, Element ndVol, OP op) {
		Iterator<Element> bayIterator = ndVol.elementIterator("Bay");
		while(bayIterator.hasNext()) {
			Element ndBay = bayIterator.next();
			fillBayDiffs(diffVol, ndBay, op);
		}
	}
	
	private void fillBayDiffs(Difference diffVol, Element ndBay, OP op) {
		Difference diffBay = addDiffByName(diffVol, ndBay, op);
		Iterator<Element> eqpIterator = ndBay.elementIterator();
		while(eqpIterator.hasNext()) {
			Element ndBaySub = eqpIterator.next();
			String ndName = ndBaySub.getName();
			if (!isEquipmentNode(ndName) && !isConnectivityNode(ndName)) {
				continue;
			}
			fillBaySub(diffBay, ndBaySub, op);
		}
	}
	
	private void fillBaySub(Difference diffBay, Element ndBaySub, OP op) {
		String ndName = ndBaySub.getName();
		if (isEquipmentNode(ndName)) {			// 设备
			Difference diffEqp = addDiffByName(diffBay, ndBaySub, op);
			Iterator<Element> eqpSubIterator = ndBaySub.elementIterator();
			while(eqpSubIterator.hasNext()) {
				Element ndEqpSub = eqpSubIterator.next();
				fillEqpSub(diffEqp, ndEqpSub, op);
			}
		} else if (isConnectivityNode(ndName)){	// 拓扑连接点
			addDiffByName(diffBay, ndBaySub, op);
		}
	}
	
	private void fillEqpSub(Difference diffEqp, Element ndEqpSub, OP op) {
		String ndEqpSubName = ndEqpSub.getName();
		if ("LNode".equals(ndEqpSubName)) {
			addDifference(diffEqp, ndEqpSub, op);
		} else if ("Terminal".equals(ndEqpSubName)) {
			addDiffByName(diffEqp, ndEqpSub, op);
		}
	}
	
	private Difference addDifference(Difference diffParent, Element ndSub, OP op) {
		return CompareUtil.addDiffByAttName(diffParent, ndSub, "", op);
	}
	
	private Difference addDiffByName(Difference diffParent, Element ndSub, OP op) {
		return CompareUtil.addDiffByAttName(diffParent, ndSub, "name", op);
	}
}
