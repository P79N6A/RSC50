package com.synet.tool.rsc.compare.ssd;

import java.util.Iterator;
import java.util.Map;

import org.dom4j.Element;
import org.eclipse.core.runtime.IProgressMonitor;

import com.shrcn.found.file.xml.DOM4JNodeHelper;
import com.synet.tool.rsc.compare.BaseCompare;
import com.synet.tool.rsc.compare.CompareUtil;
import com.synet.tool.rsc.compare.Difference;
import com.synet.tool.rsc.compare.OP;

public class SsdCompare extends BaseCompare {

	public SsdCompare(Element ndSrc, Element ndDest, IProgressMonitor monitor) {
		super(ndSrc, ndDest, monitor);
	}
	
	private void compareAtts(Difference diff, Element ndSrc, Element ndDest) {
		String msg = CompareUtil.compare(ndSrc, ndDest);
		diff.setMsg(msg);
		diff.setOp(OP.UPDATE);
	}
	
	@Override
	public Difference execute() {
		if (CompareUtil.matchMd5(ndSrc, ndDest)) {
			return null;
		}
		int volCount = DOM4JNodeHelper.countNodes(ndSrc, "/SCL/Substation/VoltageLevel");
		monitor.beginTask("开始比较SSD信息", volCount + 1);
		Difference diffRoot = CompareUtil.addUpdateDiff(null, ndSrc, ndDest);
		compareAtts(diffRoot, ndSrc, ndDest);
		Iterator<Element> volIterator = ndSrc.elementIterator("VoltageLevel");
		Map<String, Element> destChildrenMap = CompareUtil.getChildrenMapByAtt(ndDest, "name");
		while(volIterator.hasNext()) {
			Element ndVolSrc = volIterator.next();
			String volName = ndVolSrc.attributeValue("name");
			Element ndVolDest = destChildrenMap.get(volName);
			monitor.setTaskName("正在比较电压等级" + volName);
			if (CompareUtil.matchMd5(ndVolSrc, ndVolDest)) {
				continue;
			}
			if (ndVolDest == null) { // 已删除
				Difference diffVol = CompareUtil.addDiffByAttName(diffRoot, ndVolSrc, "name", OP.DELETE);
				fillVolDiffs(diffVol, ndVolSrc, OP.DELETE);
			} else {
				if (!CompareUtil.matchMd5(ndVolSrc, ndVolDest)) {
					new VolCompare(diffRoot, ndVolSrc, ndVolDest, monitor).execute();
				}
				destChildrenMap.remove(volName);
			}
			monitor.worked(1);
		}
		if (destChildrenMap.size() > 0) {
			for (Element ndVolDest : destChildrenMap.values()) {
				Difference diffVol = CompareUtil.addDiffByAttName(diffRoot, ndVolDest, "name", OP.ADD);
				fillVolDiffs(diffVol, ndVolDest, OP.ADD);
			}
		}
		monitor.done();
		return diffRoot;
	}
	
	private void fillVolDiffs(Difference diffVol, Element ndVol, OP op) {
		Iterator<Element> bayIterator = ndVol.elementIterator("Bay");
		while(bayIterator.hasNext()) {
			Element ndBay = bayIterator.next();
			fillBayDiffs(diffVol, ndBay, op);
		}
	}
}
