package com.synet.tool.rsc.compare.board;

import org.eclipse.core.runtime.IProgressMonitor;

import com.synet.tool.rsc.compare.CompareUtil;
import com.synet.tool.rsc.compare.Difference;
import com.synet.tool.rsc.compare.ObjectCompare;
import com.synet.tool.rsc.model.Tb1048PortEntity;
import com.synet.tool.rsc.util.IndexedJSONUtil;

public class PortCompare extends ObjectCompare {

	private Tb1048PortEntity srcPort; 
	private Tb1048PortEntity destPort;
	
	public PortCompare(Difference parentDiff, Tb1048PortEntity srcPort, Tb1048PortEntity destPort, IProgressMonitor monitor) {
		super(parentDiff, monitor);
		this.srcPort = srcPort;
		this.destPort = destPort;
	}
	
	@Override
	public Difference execute() {
		String jsSrc = IndexedJSONUtil.getJson(srcPort);
		String jsDest = IndexedJSONUtil.getJson(destPort);
		Difference diffPort = CompareUtil.addUpdateDiff(parentDiff, "Board", srcPort.getF1048No(), jsSrc, jsDest);
		diffPort.setDesc(srcPort.getF1048Desc());
		diffPort.setNewName(destPort.getF1048No());
		diffPort.setNewDesc(destPort.getF1048Desc());
		return diffPort;
	}

}
