/**
 * Copyright (c) 2018-2019 上海泗业技术咨询有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application.
 */
package com.synet.tool.rsc.action.imp;

import com.shrcn.found.ui.action.ConfigAction;
import com.synet.tool.rsc.util.ProjectFileManager;

 /**
 * 
 * @author 陈春(mailto:chench80@126.com)
 * @version 1.0, 2018-8-27
 */
public class BaseImportAction extends ConfigAction {

	public BaseImportAction(String title) {
		super(title);
	}
	
	@Override
	public boolean isEnabled() {
		return !ProjectFileManager.getInstance().isClosed();
	}

}

