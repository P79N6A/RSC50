/**
 * Copyright (c) 2007-2015 上海思源弘瑞自动化有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based on IEC61850 SCT.
 */
package com.shrcn.business.scl.check;

import org.eclipse.swt.graphics.Image;

import com.shrcn.found.ui.util.IconsManager;
import com.shrcn.found.ui.util.ImageConstants;

 /**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2015-8-19
 */
public enum LEVEL {
	OK("正常"), WARNING("警告"), ERROR("错误");
	private String desc;
	IconsManager iconMgr = IconsManager.getInstance();
	LEVEL(String desc) {
		this.desc = desc;
	}
	public Image getImage() {
		if (this == WARNING)
			return iconMgr.getImage(ImageConstants.WARN);
		if (this == ERROR)
			return iconMgr.getImage(ImageConstants.ERROR);
		return null;
	}
	public String getDesc() {
		return desc;
	}
	public String getPrefix() {
		return "\n" + getDesc() + "：";
	}
	public String getIcon() {
		if (this == WARNING)
			return ImageConstants.WARN;
		if (this == ERROR)
			return ImageConstants.ERROR;
		return null;
	}
}