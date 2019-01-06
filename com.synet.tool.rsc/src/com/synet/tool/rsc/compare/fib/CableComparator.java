package com.synet.tool.rsc.compare.fib;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.IProgressMonitor;

import com.synet.tool.rsc.compare.CompareUtil;
import com.synet.tool.rsc.compare.Difference;
import com.synet.tool.rsc.compare.OP;
import com.synet.tool.rsc.compare.ObjectComparator;
import com.synet.tool.rsc.model.Tb1051CableEntity;
import com.synet.tool.rsc.util.IndexedJSONUtil;

public class CableComparator extends ObjectComparator {
	
	private List<Tb1051CableEntity> cableListSrc;
	private List<Tb1051CableEntity> cableListDest;
	
	public CableComparator(Difference diffParent, List<Tb1051CableEntity> cableListSrc, List<Tb1051CableEntity> cableListDest, IProgressMonitor monitor) {
		super(diffParent, monitor);
		this.cableListSrc = cableListSrc;
		this.cableListDest = cableListDest;
	}

	@Override
	public List<Difference> execute() {
		List<Difference> result = new ArrayList<>();
		Iterator<Tb1051CableEntity> iterator = cableListSrc.iterator();
		Map<String, Object> destMap = CompareUtil.getChildrenMapByAtt(cableListDest, "f1051Name");
		while (iterator.hasNext()) {
			Tb1051CableEntity bdSrc = iterator.next();
			String regName = bdSrc.getF1051Name();
			Tb1051CableEntity bdDest = (Tb1051CableEntity) destMap.get(regName);
			if (bdDest == null) {
				Difference diff = CompareUtil.addDiffByAttName(null, "Cable", bdSrc, "f1051Name", "f1051Desc", OP.DELETE);
				result.add(diff);
			} else {
				destMap.remove(regName);
				String jsSrc = IndexedJSONUtil.getJson(bdSrc);
				String jsDest = IndexedJSONUtil.getJson(bdDest);
				if (CompareUtil.matchMd5(jsSrc, jsDest)) {
					continue;
				}
				CompareUtil.addUpdateDiff(diffParent, "Cable", regName, jsSrc, jsDest);
			}
		}
		if (destMap.size() > 0) {
			for (Object obj : destMap.values()) {
				Tb1051CableEntity bdDest = (Tb1051CableEntity) obj;
				Difference diff = CompareUtil.addDiffByAttName(null, "Cable", bdDest, "f1051Name", "f1051Desc", OP.ADD);
				result.add(diff);
			}
		}
		return result;
	}

}
