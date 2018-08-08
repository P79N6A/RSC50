/**
 * Copyright (c) 2007-2015 上海思源弘瑞自动化有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based on IEC61850 SCT.
 */
package com.shrcn.found.xmldb.vtd.fun;

import java.io.UnsupportedEncodingException;
import java.util.Map;

import com.ximpleware.AutoPilot;
import com.ximpleware.ModifyException;
import com.ximpleware.NavException;
import com.ximpleware.VTDNav;
import com.ximpleware.XMLModifier;
import com.ximpleware.XPathEvalException;
import com.ximpleware.XPathParseException;

/**
* 
* @author 孙春颖(mailto:scy@shrcn.com)
* @version 1.0, 2015-11-4
*/
public abstract class UpdateVTDFunction extends DefaultVTDFunction {

	protected XMLModifier xm;
	
	public UpdateVTDFunction(AutoPilot ap, VTDNav vn, XMLModifier xm, Map<String, Object> params) {
		super(ap, vn, params);
		this.xm = xm;
	}
	
	protected synchronized void insertAsFirst(String select, String content) {
		try {
			XMLModifier xm = getXMLModifier();
			selectXPath(select);
			if (ap.evalXPath() != -1) {
				xm.insertAfterHead("\n" + content);
			} else {
				throw new RuntimeException("找不到目标节点" + select);
			}
			ap.resetXPath();
		} catch (ModifyException | XPathParseException | XPathEvalException | NavException | UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}
	
	protected synchronized void replaceNode(String select, String node) {
		try {
			XMLModifier xm = getXMLModifier();
			selectXPath(select);
			if (ap.evalXPath() != -1) {
				xm.remove();
				xm.insertBeforeElement(node);
			}
			ap.resetXPath();
		} catch (ModifyException | XPathParseException | XPathEvalException | NavException | UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}
	
	protected synchronized void update(String select, String value) {
		try {
			XMLModifier xm = getXMLModifier();
			selectXPath(select + "/text()");
			int i = ap.evalXPath();
			if (i != -1) {
				xm.updateToken(i, value);
			} else {
				throw new RuntimeException("找不到目标节点" + select);
			}
			ap.resetXPath();
		} catch (ModifyException | XPathParseException | XPathEvalException | NavException | UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}

	private XMLModifier getXMLModifier() throws ModifyException {
		if (xm == null)
			this.xm = new XMLModifier(this.vn);
		return this.xm;
	}
}
