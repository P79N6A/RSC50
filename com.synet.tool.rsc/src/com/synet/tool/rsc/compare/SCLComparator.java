package com.synet.tool.rsc.compare;

import org.eclipse.core.runtime.IProgressMonitor;

import com.shrcn.found.xmldb.vtd.VTDXMLDBHelper;

public abstract class SCLComparator implements IComparator {

	protected String srcPath;
	protected String destPath;
	protected IProgressMonitor monitor;
	protected VTDXMLDBHelper srcXmlHelper;
	protected VTDXMLDBHelper destXmlHelper;
	
	public SCLComparator(String srcPath, String destPath, IProgressMonitor monitor) {
		this.srcPath = srcPath;
		this.destPath = destPath;
		this.monitor = monitor;
		init();
	}
	
	private void init() {
		srcXmlHelper = new VTDXMLDBHelper();
		destXmlHelper = new VTDXMLDBHelper();
		srcXmlHelper.loadDocument("", srcPath);
		destXmlHelper.loadDocument("", destPath);
	}
}
