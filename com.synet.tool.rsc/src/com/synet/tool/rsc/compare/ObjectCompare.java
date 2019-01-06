package com.synet.tool.rsc.compare;

import java.util.Iterator;

import org.dom4j.Element;
import org.eclipse.core.runtime.IProgressMonitor;

public abstract class ObjectCompare implements ICompare {
	
	protected Difference parentDiff;
	protected IProgressMonitor monitor;
	
	public ObjectCompare(Difference parentDiff, IProgressMonitor monitor) {
		this.parentDiff = parentDiff;
		this.monitor = monitor;
	}

	protected void addObjectDiff(Difference diffParent, Object object, OP op) {
		
//		Difference diff = new Difference(diffParent, ndSubName, subName, CompareUtil.getAttsMsg(ndSub, attName), op);
//		
//		Difference diffBay = addDiffByName(diffVol, ndBay, op);
//		Iterator<Element> eqpIterator = ndBay.elementIterator();
//		while(eqpIterator.hasNext()) {
//			Element ndBaySub = eqpIterator.next();
//			String ndName = ndBaySub.getName();
//			if (!isEquipmentNode(ndName) && !isConnectivityNode(ndName)) {
//				continue;
//			}
//			fillBaySub(diffBay, ndBaySub, op);
//		}
	}
}
