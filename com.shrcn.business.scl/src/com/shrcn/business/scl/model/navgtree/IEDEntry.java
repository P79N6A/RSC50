/**
 * Copyright (c) 2007, 2008 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based Visual Device Develop System.
 */
package com.shrcn.business.scl.model.navgtree;

import com.shrcn.business.scl.model.IED;
import com.shrcn.business.scl.model.SCL;
import com.shrcn.business.scl.ui.SCTEditorInput;
import com.shrcn.found.common.util.StringUtil;
import com.shrcn.found.ui.util.ImageConstants;

/**
 * 
 * @author 黄钦辉(mailto:huangqinhui@shrcn.com)
 * @version 1.0, 2009-5-4
 */
/*
 * 修改历史
 * $Log: IEDEntry.java,v $
 * Revision 1.1  2013/03/29 09:35:25  cchun
 * Add:创建
 *
 * Revision 1.2  2011/08/24 08:16:20  cchun
 * Update:清理注释
 *
 * Revision 1.1  2009/08/27 02:22:32  cchun
 * Refactor:重构导航树模型包路径
 *
 * Revision 1.1  2009/05/04 12:27:47  hqh
 * 加ied实体类
 *
 */
public class IEDEntry extends TreeEntryImpl {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public IEDEntry(IED ied, String editorID){
		String iedName = ied.getName();
		String label = StringUtil.getLabel(iedName, ied.getDesc());
		String icon = "VIRTUAL_IED".equals(ied.getType()) ? ImageConstants.IED_V : ImageConstants.IED;
		String tooltip = "名称：" + iedName + "    描述：" + ied.getDesc() +
				"\n型号：" + ied.getType() + "    厂商：" + ied.getManufacturer() +
				"\n版本：" + ied.getConfigVersion();
		this.name = label;
		this.xpath = SCL.XPATH_IED + "[@name='" + iedName + "']";
		this.iconName = icon;
		this.toolTip = tooltip;
		if(null != editorID  && !"".equals(editorID)) {
			this.editorInput = new SCTEditorInput(this, editorID);
		}
	}

	public IEDEntry(String name, String xpath, String iconName, String toolTip,
			String editorID) {
		super(name, xpath, iconName, toolTip, editorID);
	}

	public void setIconName(String iconName){
		this.iconName = iconName;
	}
}
