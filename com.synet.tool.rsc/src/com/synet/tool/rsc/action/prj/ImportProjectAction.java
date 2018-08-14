/**
 * Copyright (c) 2007-2017 泗业技术咨询有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application.
 */
package com.synet.tool.rsc.action.prj;

import com.shrcn.found.common.event.EventConstants;
import com.shrcn.found.common.event.EventManager;
import com.shrcn.found.ui.action.ConfigAction;

 /**
 * 
 * @author 陈春(mailto:chench80@126.com)
 * @version 1.0, 2018-8-7
 */
public class ImportProjectAction extends ConfigAction {
	
	public ImportProjectAction(String title) {
		super(title);
	}

	@Override
	public void run() {
		EventManager.getDefault().notify(EventConstants.PROJECT_OPEN_IMP, null);
	}

}

