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
public class QuerySMVFunction extends QueryVTDFunction {

	public QuerySMVFunction(AutoPilot ap, VTDNav vn, Map<String, Object> params) {
		super(ap, vn, params);
	}

	@Override
	public Object exec() {
		List<Element> iedGses = new ArrayList<>();
		try {
			ap.selectXPath("/SCL/IED");
			while (ap.evalXPath() != -1) {
				String iedName = getSubString(vn, "@name");
				vn.push();
				AutoPilot apAP = createAP(vn, "AccessPoint");
				while (apAP.evalXPath() != -1) {
					String apName = vn.toString(vn.getAttrVal("name"));
					vn.push();
					AutoPilot apLD = createAP(vn, "Server/LDevice");
					while (apLD.evalXPath() != -1) {
						String ldInst = vn.toString(vn.getAttrVal("inst"));
						vn.push();
						AutoPilot apSMV = createAP(vn, "LN0/SampledValueControl");
						while (apSMV.evalXPath() != -1) {
							String smvName = vn.toString(vn.getAttrVal("name"));
							String smvID = vn.toString(vn.getAttrVal("smvID"));
							Element iedGse = DOM4JNodeHelper.createSCLNode("SampledValueControl");
							iedGse.addAttribute("iedName", iedName);
							iedGse.addAttribute("apName", apName);
							iedGse.addAttribute("ldInst", ldInst);
							iedGse.addAttribute("name", smvName);
							iedGse.addAttribute("smvID", smvID);
							iedGses.add(iedGse);
						}
						apSMV.resetXPath();
						vn.pop();
					}
					apLD.resetXPath();
					vn.pop();
				}
				apAP.resetXPath();
				vn.pop();
			}
			ap.resetXPath();
		} catch (XPathParseException | XPathEvalException | NavException e) {
			throw new RuntimeException(e);
		}
		return iedGses;
	}

}

