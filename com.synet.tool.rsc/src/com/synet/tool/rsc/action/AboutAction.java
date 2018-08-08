/**
 * Copyright (c) 2007-2017 泗业技术咨询有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application.
 */
package com.synet.tool.rsc.action;

import org.eclipse.swt.widgets.Display;

import com.shrcn.found.ui.action.ConfigAction;
import com.shrcn.found.ui.dialog.AboutDialog;
import com.shrcn.found.ui.util.UIProperties;


/**
* 
* @author 陈春(mailto:chench80@126.com)
* @version 1.0, 2018-8-7
*/
public class AboutAction extends ConfigAction {

	public AboutAction(String title) {
		super(title);
	}

	@Override
	public void run() {
		shell = Display.getDefault().getActiveShell();
		AboutDialog dialog = new AboutDialog(shell, "RS-5000系统配置工具   " + 
				UIProperties.getInstance().getVersion() + "\n\n");
		dialog.open();
	}
}
