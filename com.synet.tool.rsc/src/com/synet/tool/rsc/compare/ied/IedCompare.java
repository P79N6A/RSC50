package com.synet.tool.rsc.compare.ied;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.dom4j.Attribute;
import org.dom4j.Element;

import com.shrcn.found.common.util.StringUtil;
import com.synet.tool.rsc.compare.Difference;
import com.synet.tool.rsc.compare.OP;

public class IedCompare {
	
	private String iedName;
	private Element iedNdSrc;
	private Element iedNdDest;
	private Difference diffRoot;

	public IedCompare(String iedName, Element iedNdSrc, Element iedNdDest) {
		this.iedName = iedName;
		this.iedNdSrc = iedNdSrc;
		this.iedNdDest = iedNdDest;
		this.diffRoot = new Difference("IED", iedName);
	}
	
	private boolean matchMd5(Element ndSrc, Element ndDest) {
		String srcMd5 = ndSrc.attributeValue("md5");
		String destMd5 = ndDest.attributeValue("md5");
		if (srcMd5.equals(destMd5)) {
			return true;
		}
		return false;
	}

	public Difference execute() {
		if (matchMd5(iedNdSrc, iedNdDest)) {
			return diffRoot;
		}
		String msg = compare(iedNdSrc, iedNdDest);
		diffRoot.setMsg(msg);
		diffRoot.setOp(OP.UPDATE);
		compareRcbs();
		compareGooses();
		compareSmvs();
		compareInputs();
		comparePins();
		compareDsSettings();
		compareDsParams();
		return diffRoot;
	}
	
	/**
	 * 以指定属性为主键将子节点转成Map，便于查找
	 * @param ndParent
	 * @param att
	 * @return
	 */
	private Map<String, Element> getChildrenMapByAtt(Element ndParent, String att) {
		Map<String, Element> map = new HashMap<>();
		Iterator<Element> iterator = ndParent.elementIterator();
		while (iterator.hasNext()) {
			Element nd = iterator.next();
			String ref = nd.attributeValue(att);
			map.put(ref, nd);
		}
		return map;
	}
	
	/**
	 * 获取FCDA除ref之外的属性值
	 * @param fcda
	 * @return
	 */
	private String getFCDAMsg(Element fcda) {
		return getAttsMsg(fcda, "ref");
	}
	
	/**
	 * 获取任意节点除指定属性名之外的属性值
	 * @param fcda
	 * @param except
	 * @return
	 */
	private String getAttsMsg(Element fcda, String except) {
		String msg = "";
		Iterator<Attribute> attributeIterator = fcda.attributeIterator();
		while(attributeIterator.hasNext()) {
			Attribute att = attributeIterator.next();
			String name = att.getName();
			if (!except.equals(name) && !"md5".equals(name)) {
				if (!"".equals(msg)) {
					msg += ",";
				}
				msg += name + ":" + att.getValue();
			}
		}
		return msg;
	}
	
	/**
	 * FCDA全部删除或者全部新增
	 * @param diffParent
	 * @param ndParent
	 * @param op
	 */
	private void fillChildrenByFCDAs(Difference diffParent, Element ndParent, OP op) {
		Iterator<Element> iterator = ndParent.elementIterator("FCDA");
		while(iterator.hasNext()) {
			Element fcda = iterator.next();
			String ref = fcda.attributeValue("ref");
			new Difference(diffParent, "FCDA", ref, getFCDAMsg(fcda), op);
		}
	}
	
	/**
	 * 比较FCDA
	 * @param ndParent
	 * @param srcNd
	 * @param destNd
	 */
	private void compareFCDAs(Difference ndParent, Element srcNd, Element destNd) {
		Iterator<Element> iterator = srcNd.elementIterator();
		Map<String, Element> destChildrenMap = getChildrenMapByAtt(destNd, "ref");
		while (iterator.hasNext()) {
			Element ndFCDASrc = iterator.next();
			String ref = ndFCDASrc.attributeValue("ref");
			Element ndFCDADest = destChildrenMap.get(ref);
			if (ndFCDADest == null) { // 已删除
				new Difference(ndParent, "FCDA", ref, getFCDAMsg(ndFCDASrc), OP.DELETE);
			} else {
				String msg = compare(ndFCDASrc, ndFCDADest);
				if (!StringUtil.isEmpty(msg)) {	// 修改
					new Difference(ndParent, "FCDA", ref, msg, OP.UPDATE);
				}
				destChildrenMap.remove(ref);
			}
		}
		if (destChildrenMap.size() > 0) {
			for (Element ndFCDADest : destChildrenMap.values()) { // 增加
				String ref = ndFCDADest.attributeValue("ref");
				new Difference(ndParent, "FCDA", ref , getFCDAMsg(ndFCDADest), OP.ADD);
			}
		}
	}
	
	/**
	 * 比较接收虚端子
	 */
	private void comparePins() {
		Element ndPinsSrc = iedNdSrc.element("Pins");
		Element ndPinsDest = iedNdDest.element("Pins");
		if (matchMd5(ndPinsSrc, ndPinsDest)) {
			return;
		}
		Difference diffPin = new Difference(diffRoot, "输入虚端子", "", "", OP.UPDATE);
		Iterator<Element> iterator = ndPinsSrc.elementIterator();
		Map<String, Element> destNdMap = getChildrenMapByAtt(ndPinsDest, "ref");
		while (iterator.hasNext()) {
			Element ndPinSrc = iterator.next();
			String ref = ndPinSrc.attributeValue("ref");
			Element ndPinDest = destNdMap.get(ref);
			if (ndPinDest  == null) { // 已删除
				new Difference(diffPin, "Pin", ref, getAttsMsg(ndPinSrc, "ref"), OP.DELETE);
			} else {
				String descSrc = ndPinSrc.attributeValue("desc");
				String descDest = ndPinDest.attributeValue("desc");
				if (!descSrc.equals(descDest)) {
					String msg = compare(ndPinSrc, ndPinDest);
					new Difference(diffPin, "Pin", ref, msg, OP.UPDATE);
				}
				destNdMap.remove(ref);
			}
		}
		for (Element ndPinDest : destNdMap.values()) {
			String ref = ndPinDest.attributeValue("ref");
			new Difference(diffPin, "Pin", ref, getAttsMsg(ndPinDest, "ref"), OP.ADD);
		}
	}
	
	/**
	 * 比较CB
	 * @param typeName
	 * @param typeDesc
	 * @param cbName
	 * @param keyAtt
	 */
	private void compareCbs(String typeName, String typeDesc, String cbName, String keyAtt) {
		Element ndCbTypeSrc = iedNdSrc.element(typeName);
		Element ndCbTypeDest = iedNdDest.element(typeName);
		if (matchMd5(ndCbTypeSrc, ndCbTypeDest)) {
			return;
		}
		Difference diffType = new Difference(diffRoot, typeDesc, "", "", OP.UPDATE);
		Iterator<Element> iterator = ndCbTypeSrc.elementIterator();
		Map<String, Element> destChildrenMap = getChildrenMapByAtt(ndCbTypeDest, keyAtt);
		while (iterator.hasNext()) {
			Element ndCBSrc = iterator.next();
			String cbRef = ndCBSrc.attributeValue(keyAtt);
			Element ndCBDest = destChildrenMap.get(cbRef);
			if (ndCBDest == null) { // 已删除
				String msg = getAttsMsg(ndCBSrc, keyAtt);
				Difference diffCB = new Difference(diffType, cbName, cbRef, msg, OP.DELETE);
				fillChildrenByFCDAs(diffCB, ndCBSrc, OP.DELETE);
			} else {
				if (!matchMd5(ndCBSrc, ndCBDest)) {
					String msg = compare(ndCBSrc, ndCBDest);
					Difference diffCB = new Difference(diffType, cbName, cbRef, msg, OP.UPDATE);
					compareFCDAs(diffCB, ndCBSrc, ndCBDest);
				}
				destChildrenMap.remove(cbRef);
			}
		}
		if (destChildrenMap.size() > 0) {
			for (Element ndCBDest : destChildrenMap.values()) {
				String cbRef = ndCBDest.attributeValue(keyAtt);
				String msg = getAttsMsg(ndCBDest, keyAtt);
				Difference diffCB = new Difference(diffType, cbName, cbRef, msg, OP.ADD);
				fillChildrenByFCDAs(diffCB, ndCBDest, OP.ADD);
			}
		}
		if (diffType.getChildren().size() < 1) {
			diffRoot.getChildren().remove(diffType);
		}
	}
	
	/**
	 * 比较定制控制块
	 */
	private void compareDsSettings() {
		compareCbs("DsSettings", "定值", "SGCB", "cbRef");
	}
	
	/**
	 * 比较参数数据集
	 */
	private void compareDsParams() {
		compareCbs("DsParams", "参数", "Param", "name");
	}
	
	private void compareRcbs() {
		compareCbs("Rcbs", "报告控制块", "Rcb", "cbRef");
	}
	
	private void compareGooses() {
		compareCbs("Gooses", "Goose控制块", "Goose", "cbRef");
	}
	
	private void compareSmvs() {
		compareCbs("Smvs", "Smv控制块", "Smv", "cbRef");
	}
	
	private void compareInputs() {
		Element ndInputsSrc = iedNdSrc.element("Inputs");
		Element ndInputsDest = iedNdDest.element("Inputs");
		if (matchMd5(ndInputsSrc, ndInputsDest)) {
			return;
		}
		Difference diffType = new Difference(diffRoot, "虚回路", "", "", OP.UPDATE);
		Iterator<Element> iterator = ndInputsSrc.elementIterator();
		Map<String, Element> destChildrenMap = getChildrenMapByAtt(ndInputsDest, "intAddr");
		while (iterator.hasNext()) {
			Element ndInputSrc = iterator.next();
			String intAddrSrc = ndInputSrc.attributeValue("intAddr");
			Element ndInputDest = destChildrenMap.get(intAddrSrc);
			if (ndInputDest == null) { // 删除
				String msg = getAttsMsg(ndInputSrc, "intAddr");
				new Difference(diffType, "ExtRef", intAddrSrc, msg, OP.DELETE);
			} else {
				String msg = compare(ndInputSrc, ndInputDest);
				if (!StringUtil.isEmpty(msg)) {
					new Difference(diffType, "ExtRef", intAddrSrc, msg, OP.UPDATE);
				}
				destChildrenMap.remove(intAddrSrc);
			}
		}
		if (destChildrenMap.size() > 0) {
			for (Element ndInputDest : destChildrenMap.values()) {
				String intAddrDest = ndInputDest.attributeValue("intAddr");
				String msg = getAttsMsg(ndInputDest, "intAddr");
				new Difference(diffType, "ExtRef", intAddrDest, msg, OP.ADD);
			}
		}
	}
	
	private String compare(Element ndSrc, Element ndDest) {
		String msg = "";
		Iterator<Attribute> iterator = ndSrc.attributeIterator();
		while (iterator.hasNext()) {
			Attribute att = iterator.next();
			String name = att.getName();
			if ("md5".equals(name)) {
				continue;
			} else {
				String valueSrc = att.getValue();
				String valueDest = ndDest.attributeValue(name);
				if (!valueSrc.equals(valueDest)) {
					if (!"".equals(msg)) {
						msg += ",";
					} else {
						msg += name + ":" + valueSrc + "->" + valueDest;
					}
				}
			}
		}
		return msg;
	}
}
