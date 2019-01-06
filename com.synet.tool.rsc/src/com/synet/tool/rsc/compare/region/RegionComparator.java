package com.synet.tool.rsc.compare.region;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.IProgressMonitor;

import com.synet.tool.rsc.compare.CompareUtil;
import com.synet.tool.rsc.compare.Difference;
import com.synet.tool.rsc.compare.OP;
import com.synet.tool.rsc.compare.ObjectComparator;
import com.synet.tool.rsc.model.Tb1049RegionEntity;
import com.synet.tool.rsc.util.IndexedJSONUtil;

public class RegionComparator extends ObjectComparator {
	
	private List<Tb1049RegionEntity> bdsListSrc;
	private List<Tb1049RegionEntity> bdsListDest; 

	public RegionComparator(List<Tb1049RegionEntity> bdsListSrc, List<Tb1049RegionEntity> bdsListDest, IProgressMonitor monitor) {
		super(monitor);
		this.bdsListSrc = bdsListSrc;
		this.bdsListDest = bdsListDest;
	}
	
	@Override
	public List<Difference> execute() {
		monitor.beginTask("开始比较区域屏柜...", bdsListSrc.size() + 1);
		List<Difference> result = new ArrayList<>();
		Iterator<Tb1049RegionEntity> iterator = bdsListSrc.iterator();
		Map<String, Object> destMap = CompareUtil.getChildrenMapByAtt(bdsListDest, "f1049Name");
		while (iterator.hasNext()) {
			Tb1049RegionEntity bdSrc = iterator.next();
			String regName = bdSrc.getF1049Name();
			monitor.setTaskName("正在比较区域" + regName);
			Tb1049RegionEntity bdDest = (Tb1049RegionEntity) destMap.get(regName);
			if (bdDest == null) {
				Difference diff = CompareUtil.addDiffByAttName(null, "Region", bdSrc, "f1049Name", "f1049Desc", OP.DELETE);
				result.add(diff);
			} else {
				destMap.remove(regName);
				if (CompareUtil.matchMd5(IndexedJSONUtil.getRegionJson(bdSrc), IndexedJSONUtil.getRegionJson(bdDest))) {
					continue;
				}
				Difference diff = new RegionCompare(null, bdSrc, bdDest, monitor).execute();
				result.add(diff);
			}
			monitor.worked(1);
		}
		if (destMap.size() > 0) {
			for (Object obj : destMap.values()) {
				Tb1049RegionEntity bdDest = (Tb1049RegionEntity) obj;
				Difference diff = CompareUtil.addDiffByAttName(null, "Region", bdDest, "f1049Name", "f1049Desc", OP.ADD);
				result.add(diff);
			}
		}
		monitor.done();
		return result;
	}

}
