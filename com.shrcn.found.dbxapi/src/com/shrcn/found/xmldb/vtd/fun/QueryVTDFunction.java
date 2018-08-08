/**
 * Copyright (c) 2007-2015 上海思源弘瑞自动化有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based on IEC61850 SCT.
 */
package com.shrcn.found.xmldb.vtd.fun;

import java.util.Map;

import com.ximpleware.AutoPilot;
import com.ximpleware.VTDNav;

/**
* 
* @author 孙春颖(mailto:scy@shrcn.com)
* @version 1.0, 2015-11-4
*/
public abstract class QueryVTDFunction extends DefaultVTDFunction {

	public QueryVTDFunction(AutoPilot ap, VTDNav vn, Map<String, Object> params) {
		super(ap, vn, params);
	}
}
