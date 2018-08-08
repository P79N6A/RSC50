/**
 * Copyright (c) 2007-2015 上海思源弘瑞自动化有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based on IEC61850 SCT.
 */
package com.shrcn.found.xmldb.vtd.fun;

import java.util.List;
import java.util.Map;

import com.shrcn.found.xmldb.IFunction;
import com.shrcn.found.xmldb.XMLDBHelper;
import com.ximpleware.AutoPilot;
import com.ximpleware.NavException;
import com.ximpleware.VTDNav;
import com.ximpleware.XPathEvalException;
import com.ximpleware.XPathParseException;

 /**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2015-10-7
 * @deprecated
 */
public class OuterDataSetFunction extends QueryVTDFunction {
	
	public OuterDataSetFunction(AutoPilot ap, VTDNav vn, Map<String, Object> params) {
		super(ap, vn, params);
	}
	
	@Override
	public Object exec() {
		String currIedName = (String) params.get("iedName");
		String ndName = (String) params.get("ndName");
		try {
			List<String> outIeds = XMLDBHelper.getAttributeValues("/SCL/IED[@name!='" + currIedName+ "']/@name");
			String strResult = "";
			for (String outIed : outIeds) {
				String outIedXpath = "/SCL/IED[@name='" + outIed + "']";
				ap.selectXPath(outIedXpath);
				if (ap.evalXPath()!=-1) {
					String outIedDesc = getAttVal("desc");
					String strIedSub = "";
					
					List<String> outIedAps = XMLDBHelper.getAttributeValues(outIedXpath + "/AccessPoint/@name");
					for (String outIedAp : outIedAps) {
						String outApXpath = outIedXpath + "/AccessPoint[@name='" + outIedAp + "']";
						ap.selectXPath(outApXpath);
						if (ap.evalXPath()!=-1) {
							String outApDesc = getAttVal("desc");
							List<String> outIedLds = XMLDBHelper.getAttributeValues(outApXpath + "/Server/LDevice/@inst");
							for (String outIedLd : outIedLds) {
								String outLdXpath = outApXpath + "/Server/LDevice[@inst='" + outIedLd + "']";
								ap.selectXPath(outLdXpath);
								if (ap.evalXPath()!=-1) {
									String outLdDesc = getAttVal("desc");
									String cbXpath = outLdXpath + "/*/" + ndName;
									ap.selectXPath(cbXpath);
									while (ap.evalXPath()!=-1) {
										String cbName = getAttVal("name");
										String cbDesc = getAttVal("desc");
										String datName = getAttVal("datSet");
										String datDesc = XMLDBHelper.getAttributeValue(outLdXpath + "/*/DataSet[@name='" + datName + "']/@desc");
										strIedSub += "<DataSet cbName=\"" + cbName +
												"\" cbDesc=\"" + cbDesc +
												"\" datName=\"" + datName +
												"\" datDesc=\"" + datDesc +
												"\"/>";
									}
									if (!"".equals(strIedSub)) {
										strIedSub = "<LDevice inst=\"" + outIedLd +
												"\" desc=\"" + outLdDesc +
												"\">" + strIedSub + "</LDevice>";
									}
								}
							}
							if (!"".equals(strIedSub)) {
								strIedSub = "<AP name=\"" + outIedAp +
										"\" desc=\"" + outApDesc +
										"\">" + strIedSub + "</AP>";
							}
						}
					}
					
					if(!"".equals(strIedSub)) {
						strResult += "<IED name=\"" + outIed + "\"  desc=\"" + outIedDesc + "\">";
						strResult += strIedSub;
						strResult += "</IED>";
					}
				}
			}
			ap.resetXPath();
			if (null != strResult && !"".equals(strResult))
				return XMLDBHelper.getNodesFromResult(strResult);
		} catch (XPathParseException | XPathEvalException | NavException e) {
			throw new RuntimeException(e);
		}
		return null;
	}

}

