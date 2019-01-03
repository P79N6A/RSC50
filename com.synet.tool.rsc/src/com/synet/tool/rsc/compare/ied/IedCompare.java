package com.synet.tool.rsc.compare.ied;

import java.util.Iterator;
import java.util.Map;

import org.dom4j.Element;

import com.shrcn.found.common.util.StringUtil;
import com.synet.tool.rsc.compare.CompareUtil;
import com.synet.tool.rsc.compare.Difference;
import com.synet.tool.rsc.compare.ICompare;
import com.synet.tool.rsc.compare.OP;

public class IedCompare implements ICompare {
	
	private Element iedNdSrc;
	private Element iedNdDest;
	private Difference diffRoot;

	public IedCompare(Element iedNdSrc, Element iedNdDest) {
		this.iedNdSrc = iedNdSrc;
		this.iedNdDest = iedNdDest;
		this.diffRoot = CompareUtil.addUpdateDiff(diffRoot, iedNdSrc, iedNdDest);
	}

	@Override
	public Difference execute() {
		if (matchMd5(iedNdSrc, iedNdDest)) {
			return diffRoot;
		}
		String msg = CompareUtil.compare(iedNdSrc, iedNdDest);
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
	 * FCDA全部删除或者全部新增
	 * @param diffParent
	 * @param ndParent
	 * @param op
	 */
	private void fillChildrenByFCDAs(Difference diffParent, Element ndParent, OP op) {
		Iterator<Element> iterator = ndParent.elementIterator("FCDA");
		while(iterator.hasNext()) {
			Element fcda = iterator.next();
			CompareUtil.addDiffByAttName(diffParent, fcda, "ref", op);
		}
	}
	
	/**
	 * 比较FCDA
	 * @param diffParent
	 * @param srcNd
	 * @param destNd
	 */
	private void compareFCDAs(Difference diffParent, Element srcNd, Element destNd) {
		Iterator<Element> iterator = srcNd.elementIterator();
		Map<String, Element> destChildrenMap = CompareUtil.getChildrenMapByAtt(destNd, "ref");
		while (iterator.hasNext()) {
			Element ndFCDASrc = iterator.next();
			String ref = ndFCDASrc.attributeValue("ref");
			Element ndFCDADest = destChildrenMap.get(ref);
			if (ndFCDADest == null) { // 已删除
				CompareUtil.addDiffByAttName(diffParent, ndFCDASrc, "ref", OP.DELETE);
			} else {
				String msg = CompareUtil.compare(ndFCDASrc, ndFCDADest);
				if (!StringUtil.isEmpty(msg)) {	// 修改
					Difference diff = new Difference(diffParent, "FCDA", ref, msg, OP.UPDATE);
					diff.setDesc(CompareUtil.getAttribute(ndFCDASrc, "desc"));
					diff.setNewName(CompareUtil.getAttribute(ndFCDADest, "ref"));
					diff.setNewDesc(CompareUtil.getAttribute(ndFCDADest, "desc"));
				}
				destChildrenMap.remove(ref);
			}
		}
		if (destChildrenMap.size() > 0) {
			for (Element ndFCDADest : destChildrenMap.values()) { // 增加
				String ref = ndFCDADest.attributeValue("ref");
				CompareUtil.addDiffByAttName(diffParent, ndFCDADest, "ref", OP.ADD);
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
		Map<String, Element> destNdMap = CompareUtil.getChildrenMapByAtt(ndPinsDest, "ref");
		while (iterator.hasNext()) {
			Element ndPinSrc = iterator.next();
			String ref = ndPinSrc.attributeValue("ref");
			Element ndPinDest = destNdMap.get(ref);
			if (ndPinDest  == null) { // 已删除
				CompareUtil.addDiffByAttName(diffPin, ndPinSrc, "ref", OP.DELETE);
			} else {
				String descSrc = ndPinSrc.attributeValue("desc");
				String descDest = ndPinDest.attributeValue("desc");
				if (!descSrc.equals(descDest)) {
					String msg = CompareUtil.compare(ndPinSrc, ndPinDest);
					Difference diff = new Difference(diffPin, "Pin", ref, msg, OP.UPDATE);
					diff.setDesc(CompareUtil.getAttribute(ndPinSrc, "desc"));
					diff.setNewName(CompareUtil.getAttribute(ndPinDest, "ref"));
					diff.setNewDesc(CompareUtil.getAttribute(ndPinDest, "desc"));
				}
				destNdMap.remove(ref);
			}
		}
		for (Element ndPinDest : destNdMap.values()) {
			CompareUtil.addDiffByAttName(diffPin, ndPinDest, "ref", OP.ADD);
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
		Map<String, Element> destChildrenMap = CompareUtil.getChildrenMapByAtt(ndCbTypeDest, keyAtt);
		while (iterator.hasNext()) {
			Element ndCBSrc = iterator.next();
			String cbRef = ndCBSrc.attributeValue(keyAtt);
			Element ndCBDest = destChildrenMap.get(cbRef);
			if (ndCBDest == null) { // 已删除
				Difference diffCB = CompareUtil.addDiffByAttName(diffType, ndCBSrc, keyAtt, OP.DELETE);
				fillChildrenByFCDAs(diffCB, ndCBSrc, OP.DELETE);
			} else {
				if (!matchMd5(ndCBSrc, ndCBDest)) {
					String msg = CompareUtil.compare(ndCBSrc, ndCBDest);
					Difference diffCB = new Difference(diffType, cbName, cbRef, msg, OP.UPDATE);
					diffCB.setDesc(CompareUtil.getAttribute(ndCBSrc, "dsDesc"));
					diffCB.setNewName(cbRef);
					diffCB.setNewDesc(CompareUtil.getAttribute(ndCBDest, "dsDesc"));
					compareFCDAs(diffCB, ndCBSrc, ndCBDest);
				}
				destChildrenMap.remove(cbRef);
			}
		}
		if (destChildrenMap.size() > 0) {
			for (Element ndCBDest : destChildrenMap.values()) {
				Difference diffCB = CompareUtil.addDiffByAttName(diffType, ndCBDest, keyAtt, OP.ADD);
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
		Map<String, Element> destChildrenMap = CompareUtil.getChildrenMapByAtt(ndInputsDest, "intAddr");
		while (iterator.hasNext()) {
			Element ndInputSrc = iterator.next();
			String intAddrSrc = ndInputSrc.attributeValue("intAddr");
			Element ndInputDest = destChildrenMap.get(intAddrSrc);
			if (ndInputDest == null) { // 删除
				CompareUtil.addDiffByAttName(diffType, ndInputSrc, "intAddr", OP.DELETE);
			} else {
				String msg = CompareUtil.compare(ndInputSrc, ndInputDest);
				if (!StringUtil.isEmpty(msg)) {
					Difference diff = new Difference(diffType, "ExtRef", intAddrSrc, msg, OP.UPDATE);
					diff.setDesc(CompareUtil.getAttribute(ndInputSrc, "desc"));
					diff.setNewName(CompareUtil.getAttribute(ndInputDest, "intAddr"));
					diff.setNewDesc(CompareUtil.getAttribute(ndInputDest, "desc"));
				}
				destChildrenMap.remove(intAddrSrc);
			}
		}
		if (destChildrenMap.size() > 0) {
			for (Element ndInputDest : destChildrenMap.values()) {
				CompareUtil.addDiffByAttName(diffType, ndInputDest, "intAddr", OP.ADD);
			}
		}
	}
	
	/**
	 * 比较两个xml节点文本md5码是否一致
	 * @param ndSrc
	 * @param ndDest
	 * @return
	 */
	private boolean matchMd5(Element ndSrc, Element ndDest) {
		String srcMd5 = ndSrc.attributeValue("md5");
		String destMd5 = ndDest.attributeValue("md5");
		if (srcMd5.equals(destMd5)) {
			return true;
		}
		return false;
	}
}
