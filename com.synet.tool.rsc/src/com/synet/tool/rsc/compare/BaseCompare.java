package com.synet.tool.rsc.compare;

import java.util.Iterator;

import org.dom4j.Element;
import org.eclipse.core.runtime.IProgressMonitor;


public abstract class BaseCompare implements ICompare {

	protected Element ndSrc;
	protected Element ndDest;
	protected IProgressMonitor monitor;

	public BaseCompare(Element ndSrc, Element ndDest, IProgressMonitor monitor) {
		this.ndSrc = ndSrc;
		this.ndDest = ndDest;
		this.monitor = monitor;
	}
	
	protected String getVol(Element volNd) {
		return volNd.element("Voltage").getTextTrim();
	}
	
	protected void fillBayDiffs(Difference diffVol, Element ndBay, OP op) {
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
	
	protected void fillBaySub(Difference diffBay, Element ndBaySub, OP op) {
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

	protected void fillEqpSub(Difference diffEqp, Element ndEqpSub, OP op) {
		String ndEqpSubName = ndEqpSub.getName();
		if ("LNode".equals(ndEqpSubName)) {
			addDifference(diffEqp, ndEqpSub, op);
		} else if ("Terminal".equals(ndEqpSubName)) {
			addDiffByName(diffEqp, ndEqpSub, op);
		}
	}
	
	protected boolean isConnectivityNode(String ndName) {
		return "ConnectivityNode".equals(ndName);
	}

	protected boolean isEquipmentNode(String ndName) {
		return "ConductingEquipment".equals(ndName) || "PowerTransformer".equals(ndName);
	}

	protected Difference addDifference(Difference diffParent, Element ndSub, OP op) {
		return CompareUtil.addDiffByAttName(diffParent, ndSub, "", op);
	}
	
	protected Difference addDiffByName(Difference diffParent, Element ndSub, OP op) {
		return CompareUtil.addDiffByAttName(diffParent, ndSub, "name", op);
	}
}
