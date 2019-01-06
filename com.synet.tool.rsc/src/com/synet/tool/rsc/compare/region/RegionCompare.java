package com.synet.tool.rsc.compare.region;

import java.util.Iterator;
import java.util.Map;

import org.eclipse.core.runtime.IProgressMonitor;

import com.synet.tool.rsc.compare.CompareUtil;
import com.synet.tool.rsc.compare.Difference;
import com.synet.tool.rsc.compare.OP;
import com.synet.tool.rsc.compare.ObjectCompare;
import com.synet.tool.rsc.model.Tb1049RegionEntity;
import com.synet.tool.rsc.model.Tb1050CubicleEntity;
import com.synet.tool.rsc.util.IndexedJSONUtil;

public class RegionCompare extends ObjectCompare {

	private Tb1049RegionEntity srcRegion; 
	private Tb1049RegionEntity destRegion;
	
	public RegionCompare(Difference parentDiff, Tb1049RegionEntity srcRegion, Tb1049RegionEntity destRegion, IProgressMonitor monitor) {
		super(parentDiff, monitor);
		this.srcRegion = srcRegion;
		this.destRegion = destRegion;
	}
	
	@Override
	public Difference execute() {
		String jsSrc = IndexedJSONUtil.getRegionJson(srcRegion);
		String jsDest = IndexedJSONUtil.getRegionJson(destRegion);
		Difference diffRegion = CompareUtil.addUpdateDiff(parentDiff, "Region", srcRegion.getF1049Name(), jsSrc, jsDest);
		diffRegion.setDesc(srcRegion.getF1049Desc());
		diffRegion.setNewName(destRegion.getF1049Name());
		diffRegion.setNewDesc(destRegion.getF1049Desc());
		Iterator<Tb1050CubicleEntity> iterator = srcRegion.getTb1050CubiclesByF1049Code().iterator();
		Map<String, Object> destCubicleMap = CompareUtil.getChildrenMapByAtt(destRegion.getTb1050CubiclesByF1049Code(), "f1049Name");
		while (iterator.hasNext()) {
			Tb1050CubicleEntity cubSrc = iterator.next();
			String cbName = cubSrc.getF1050Name();
			Tb1050CubicleEntity cubDest = (Tb1050CubicleEntity) destCubicleMap.get(cbName);
			if (cubDest == null) {
				CompareUtil.addDiffByAttName(diffRegion, "Cubicle", cubSrc, "f1049Name", "f1049Desc", OP.DELETE);
			} else {
				destCubicleMap.remove(cbName);
				if (CompareUtil.matchMd5(cubSrc, cubDest)) {
					continue;
				}
				new CubicleCompare(diffRegion, cubSrc, cubDest, monitor).execute();
			}
		}
		if (destCubicleMap.size() > 0) {
			for (Object obj : destCubicleMap.values()) {
				Tb1050CubicleEntity cubDest = (Tb1050CubicleEntity) obj;
				CompareUtil.addDiffByAttName(diffRegion, "Cubicle", cubDest, "f1049Name", "f1049Desc", OP.ADD);
			}
		}
		return diffRegion;
	}

}
