/**
 * Copyright (c) 2007-2013 上海思源弘瑞自动化有限公司. All rights reserved. 
 * This program is an eclipse Rich Client Application
 * designed for IED configuration and debuging.
 */
package com.shrcn.found.ui.action;

import org.eclipse.jface.action.Action;
import org.eclipse.swt.custom.StyledText;

import com.shrcn.found.ui.util.ImageConstants;
import com.shrcn.found.ui.util.ImgDescManager;

/**
 * @author 曹兴春(mailto:cxc.14375@shrcn.com)
 * @version 1.0, 2013-6-24
 */
/**
 * $log$
 */
public class ItClearAction extends Action {
	
	private StyledText text;
	
	public ItClearAction(StyledText text) {
		setImageDescriptor(ImgDescManager
				.getImageDesc(ImageConstants.CLEAR_CO));
		setText("清空");
		this.text = text;
	}

	@Override
	public void run() {
		text.setText("");
	}
}
