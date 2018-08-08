/**
 * Copyright (c) 2007-2015 上海思源弘瑞自动化有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based on IEC61850 SCT.
 */
package com.shrcn.found.xmldb.vtd.fun;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.dom4j.Element;

import com.shrcn.found.file.xml.DOM4JNodeHelper;
import com.ximpleware.AutoPilot;
import com.ximpleware.NavException;
import com.ximpleware.VTDNav;
import com.ximpleware.XPathEvalException;
import com.ximpleware.XPathParseException;

 /**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2015-10-13
 */
public class QueryMMSFunction extends QueryVTDFunction {

	public QueryMMSFunction(AutoPilot ap, VTDNav vn, Map<String, Object> params) {
		super(ap, vn, params);
	}

	@Override
	public Object exec() {
		List<Element> iedAps = new ArrayList<>();
		try {
			ap.selectXPath("/SCL/IED");
			while (ap.evalXPath() != -1) {
				String iedName = getSubString(vn, "@name");
				vn.push();
				AutoPilot ap1 = createAP(vn, "AccessPoint[count(./Server/LDevice/*/ReportControl)>0]");
				while (ap1.evalXPath() != -1) {
					String apName = vn.toString(vn.getAttrVal("name"));
					Element iedAp = DOM4JNodeHelper.createSCLNode("IED");
					iedAp.addAttribute("iedName", iedName);
					iedAp.addAttribute("apName", apName);
					iedAps.add(iedAp);
				}
				ap1.resetXPath();
				vn.pop();
			}
			ap.resetXPath();
		} catch (XPathParseException | XPathEvalException | NavException e) {
			throw new RuntimeException(e);
		}
		return iedAps;
	}
}

