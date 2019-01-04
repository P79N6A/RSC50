package com.synet.tool.rsc.compare.ssd;

import java.util.Iterator;
import java.util.Map;

import org.dom4j.Element;
import org.eclipse.core.runtime.IProgressMonitor;

import com.shrcn.found.common.util.StringUtil;
import com.synet.tool.rsc.compare.CompareUtil;
import com.synet.tool.rsc.compare.Difference;
import com.synet.tool.rsc.compare.OP;

/**
 * 比较电压等级差异
 * @author chunc
 *
 */
public class VolCompare extends SSDSubCompare {

	public VolCompare(Difference diffParent, Element ndSrc, Element ndDest, IProgressMonitor monitor) {
		super(diffParent, ndSrc, ndDest, monitor);
	}
	
	@Override
	public Difference execute() {
		Difference diffVol = CompareUtil.addUpdateDiff(diffParent, ndSrc, ndDest);
		String volSrc = getVol(ndSrc);
		String volDest = getVol(ndDest);
		String msg = diffVol.getMsg();
		if (!StringUtil.isEmpty(msg)) {
			msg += ",";
		} else {
			msg = "";
		}
		msg += "vol:" + volSrc + "->" + volDest;
		diffVol.setMsg(msg);
		Iterator<Element> bayIterator = ndSrc.elementIterator("Bay");
		Map<String, Element> destBayMap = CompareUtil.getChildrenMapByAtt(ndDest, "name");
		while(bayIterator.hasNext()) {
			Element ndBaySrc = bayIterator.next();
			String bayName = ndBaySrc.attributeValue("name");
			Element ndBayDest = destBayMap.get(bayName);
			if (ndBayDest == null) {	// 已删除
				fillBayDiffs(diffVol, ndBaySrc, OP.DELETE);
			} else {
				destBayMap.remove(bayName);
				if (CompareUtil.matchMd5(ndBaySrc, ndBayDest)) {
					continue;
				}
				new BayCompare(diffVol, ndBaySrc, ndBayDest, monitor).execute();
			}
		}
		if (destBayMap.size() > 0) {
			for (Element ndBayDest : destBayMap.values()) {
				fillBayDiffs(diffVol, ndBayDest, OP.ADD);
			}
		}
		return null;
	}

}
