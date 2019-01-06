package com.synet.tool.rsc.compare.region;

import org.eclipse.core.runtime.IProgressMonitor;

import com.synet.tool.rsc.compare.CompareUtil;
import com.synet.tool.rsc.compare.Difference;
import com.synet.tool.rsc.compare.ObjectCompare;
import com.synet.tool.rsc.model.Tb1050CubicleEntity;
import com.synet.tool.rsc.util.IndexedJSONUtil;

public class CubicleCompare extends ObjectCompare {

	private Tb1050CubicleEntity srcCub; 
	private Tb1050CubicleEntity destCub;
	
	public CubicleCompare(Difference parentDiff, Tb1050CubicleEntity srcCub, Tb1050CubicleEntity destCub, IProgressMonitor monitor) {
		super(parentDiff, monitor);
		this.srcCub = srcCub;
		this.destCub = destCub;
	}
	
	@Override
	public Difference execute() {
		String jsSrc = IndexedJSONUtil.getJson(srcCub);
		String jsDest = IndexedJSONUtil.getJson(destCub);
		Difference diffCub = CompareUtil.addUpdateDiff(parentDiff, "Cubicle", srcCub.getF1050Name(), jsSrc, jsDest);
		diffCub.setDesc(srcCub.getF1050Desc());
		diffCub.setNewName(destCub.getF1050Name());
		diffCub.setNewDesc(destCub.getF1050Desc());
		return diffCub;
	}

}
