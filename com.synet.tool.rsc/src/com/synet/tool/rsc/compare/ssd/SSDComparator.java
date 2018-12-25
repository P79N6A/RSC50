package com.synet.tool.rsc.compare.ssd;

import java.util.ArrayList;
import java.util.List;

import org.dom4j.Element;
import org.eclipse.core.runtime.IProgressMonitor;

import com.synet.tool.rsc.compare.Difference;
import com.synet.tool.rsc.compare.SCLComparator;

public class SSDComparator extends SCLComparator {

	public SSDComparator(String srcPath, String destPath,
			IProgressMonitor monitor) {
		super(srcPath, destPath, monitor);
	}

	@Override
	public List<Difference> execute() {
		Element subSrc = srcXmlHelper.selectSingleNode("/SCL/Substation");
		Element subDest = destXmlHelper.selectSingleNode("/SCL/Substation");
		List<Difference> results = new ArrayList<Difference>();
		results.add(new SsdCompare(subSrc, subDest).execute());
		return results;
	}

}
