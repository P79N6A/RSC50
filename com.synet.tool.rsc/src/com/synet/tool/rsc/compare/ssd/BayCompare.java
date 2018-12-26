package com.synet.tool.rsc.compare.ssd;

import java.util.Iterator;
import java.util.Map;

import org.dom4j.Element;
import org.eclipse.core.runtime.IProgressMonitor;

import com.synet.tool.rsc.compare.CompareUtil;
import com.synet.tool.rsc.compare.Difference;
import com.synet.tool.rsc.compare.OP;

/**
 * 比较间隔差异
 * @author chunc
 *
 */
public class BayCompare extends SSDSubCompare {

	public BayCompare(Difference diffParent, Element ndSrc, Element ndDest, IProgressMonitor monitor) {
		super(diffParent, ndSrc, ndDest, monitor);
		
	}
	
	@Override
	public Difference execute() {
		Difference diffBay = CompareUtil.addUpdateDiff(diffParent, ndSrc, ndDest);
		Map<String, Element> destBaySubMap = CompareUtil.getChildrenMapByAtt(ndDest, "name");
		Iterator<Element> eqpIterator = ndSrc.elementIterator();
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
					new EquipmentCompare(diffBay, ndBaySubSrc, ndBaySubDest, monitor).execute();
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
		return diffBay;
	}
}
