package com.synet.tool.rsc.compare.ssd;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.dom4j.Element;
import org.eclipse.core.runtime.IProgressMonitor;

import com.shrcn.found.common.util.StringUtil;
import com.synet.tool.rsc.compare.CompareUtil;
import com.synet.tool.rsc.compare.Difference;
import com.synet.tool.rsc.compare.OP;

/**
 * 比较设备差异
 * @author chunc
 *
 */
public class EquipmentCompare extends SSDSubCompare {


	public EquipmentCompare(Difference diffParent, Element ndSrc, Element ndDest, IProgressMonitor monitor) {
		super(diffParent, ndSrc, ndDest, monitor);
	}
	
	@Override
	public Difference execute() {
		Difference diffEqp = CompareUtil.addUpdateDiff(diffParent, ndSrc, ndDest);
		Iterator<Element> eqpSubIterator = ndSrc.elementIterator();
		Map<String, Element> eqpSubDestMap = getEqpSubMap(ndDest);
		while(eqpSubIterator.hasNext()) {
			Element ndEqpSubSrc = eqpSubIterator.next();
			String ndEqpSubName = ndEqpSubSrc.getName();
			String key = "";
			if ("LNode".equals(ndEqpSubName)) {
				key = getLNodeXpath(ndEqpSubSrc);
			} else if ("Terminal".equals(ndEqpSubName)) {
				key = CompareUtil.getAttribute(ndEqpSubSrc, "name");
			} else if ("TransformerWinding".equals(ndEqpSubName)) {
				String trw = CompareUtil.getAttribute(ndEqpSubSrc, "name");
				ndEqpSubSrc = ndEqpSubSrc.element("Terminal");
				ndEqpSubName = ndEqpSubSrc.getName();
				key = trw + "/" + CompareUtil.getAttribute(ndEqpSubSrc, "name");
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
		return diffEqp;
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
			} else if ("TransformerWinding".equals(ndEqpSubName)) {
				String trw = CompareUtil.getAttribute(ndEqpSub, "name");
				ndEqpSub = ndEqpSub.element("Terminal");
				String key = trw + "/" + CompareUtil.getAttribute(ndEqpSub, "name");
				if (!"".endsWith(key)) {
					eqpSubMap.put(key, ndEqpSub);
				}
			}
		}
		return eqpSubMap;
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
	
}
