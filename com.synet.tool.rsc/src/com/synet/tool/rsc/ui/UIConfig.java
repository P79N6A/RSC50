/**
 * Copyright (c) 2007-2010 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based on IEC61850 SCT.
 */
package com.synet.tool.rsc.ui;

import com.shrcn.found.ui.model.AUIConfig;
import com.shrcn.found.ui.model.TableManager;

public class UIConfig extends AUIConfig {
	
	private static UIConfig inst = null;
	
	public static UIConfig getInstance() {
		if (inst == null) {
			inst = new UIConfig();
		}
		return inst;
	}
	
	private UIConfig() {
		this.tableManager = new TableManager(UIConfig.class, UIConfig.class.getPackage().getName() + ".uiconfig");
	}
}
