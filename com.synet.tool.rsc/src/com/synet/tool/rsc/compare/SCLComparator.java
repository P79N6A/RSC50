package com.synet.tool.rsc.compare;

import org.eclipse.core.runtime.IProgressMonitor;

import com.shrcn.found.xmldb.vtd.VTDXMLDBHelper;
import com.synet.tool.rsc.io.ied.Context;

public abstract class SCLComparator implements IComparator {

	protected String srcPath;
	protected String destPath;
	protected IProgressMonitor monitor;
	protected VTDXMLDBHelper srcXmlHelper;
	protected VTDXMLDBHelper destXmlHelper;
	protected Context srcContext;
	protected Context destContext;
	
	public SCLComparator(String srcPath, String destPath, IProgressMonitor monitor) {
		this.srcPath = srcPath;
		this.destPath = destPath;
		this.monitor = monitor;
		init();
	}
	
	private void init() {
		XmlHelperCache cache = XmlHelperCache.getInstance();
		cache.loadSrcXml(srcPath);
		cache.loadDestXml(destPath);
		srcXmlHelper = cache.getSrcXmlHelper();
		destXmlHelper = cache.getDestXmlHelper();
		srcContext = cache.getSrcContext();
		destContext = cache.getDestContext();
	}
}
