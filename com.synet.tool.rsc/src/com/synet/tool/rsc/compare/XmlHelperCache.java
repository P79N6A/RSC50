package com.synet.tool.rsc.compare;

import org.dom4j.Element;

import com.shrcn.business.scl.model.SCL;
import com.shrcn.found.xmldb.vtd.VTDXMLDBHelper;
import com.synet.tool.rsc.io.ied.Context;

public class XmlHelperCache {

	private VTDXMLDBHelper srcXmlHelper;
	private VTDXMLDBHelper destXmlHelper;
	private Context srcContext;
	private Context destContext;
	
	private static XmlHelperCache inst = new XmlHelperCache();
	
	private XmlHelperCache() {
		this.srcXmlHelper = new VTDXMLDBHelper();
		this.destXmlHelper = new VTDXMLDBHelper();
	}
	
	public static XmlHelperCache getInstance() {
		return inst;
	}
	
	public void loadSrcXml(String srcPath) {
		srcXmlHelper.loadDocument("", srcPath);
		Element commEl = srcXmlHelper.selectSingleNode(SCL.XPATH_COMMUNICATION);
		Element dtTypeNd = srcXmlHelper.selectSingleNode(SCL.XPATH_DATATYPETEMPLATES);
		this.srcContext = new Context(commEl, dtTypeNd);
	}

	public void loadDestXml(String destPath) {
		destXmlHelper.loadDocument("", destPath);
		Element commEl = destXmlHelper.selectSingleNode(SCL.XPATH_COMMUNICATION);
		Element dtTypeNd = destXmlHelper.selectSingleNode(SCL.XPATH_DATATYPETEMPLATES);
		this.destContext = new Context(commEl, dtTypeNd);
	}

	public VTDXMLDBHelper getSrcXmlHelper() {
		return srcXmlHelper;
	}

	public VTDXMLDBHelper getDestXmlHelper() {
		return destXmlHelper;
	}

	public Context getSrcContext() {
		return srcContext;
	}

	public Context getDestContext() {
		return destContext;
	}
	
}
