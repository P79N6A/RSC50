/**
 * Copyright (c) 2007-2015 上海思源弘瑞自动化有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based on IEC61850 SCT.
 */
package com.shrcn.found.xmldb.vtd.fun;

import java.util.Map;

import com.shrcn.found.common.util.StringUtil;
import com.shrcn.found.file.xml.DOM4JNodeHelper;
import com.ximpleware.AutoPilot;
import com.ximpleware.VTDNav;
import com.ximpleware.XMLModifier;

/**
* 
* @author 孙春颖(mailto:scy@shrcn.com)
* @version 1.0, 2015-11-4
*/
public class UpdateCRCFunction extends UpdateVTDFunction {
	
	private String xpath;
	private String desc;
	private String value;

	public UpdateCRCFunction(AutoPilot ap, VTDNav vn, XMLModifier xm, Map<String, Object> params) {
		super(ap, vn, xm, params);
		xpath = DOM4JNodeHelper.clearPrefix((String) params.get("xpath"));
		desc = (String) params.get("desc");
		value = (String) params.get("value");
	}

	@Override
	public Object exec() {
		String crcNode = selectSingleNode(xpath + "/Private[@type='" + desc + "']");
		if (StringUtil.isEmpty(crcNode)){
			insertAsFirst(xpath, "<Private type=\"" + desc + "\">" + value + "</Private>");
		} else {
			replaceNode(xpath + "/Private[@type='" + desc + "']", "<Private type=\"" + desc + "\">" + value + "</Private>");
		}
		return true;
	}

}
