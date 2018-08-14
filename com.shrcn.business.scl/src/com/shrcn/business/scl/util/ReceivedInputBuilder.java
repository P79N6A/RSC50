/**
 * Copyright (c) 2007-2010 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based on IEC61850 SCT.
 */
/**
 * 
 */
package com.shrcn.business.scl.util;

import java.util.ArrayList;
import java.util.List;

import com.shrcn.business.scl.model.navgtree.DoNode;
import com.shrcn.business.scl.model.navgtree.LNInfo;

/**
 * 
 * @author zhouhuiming(mailto:zhm.3119@shrcn.com)
 * @version 1.0, 2010-6-1
 */
/**
 * $Log: ReceivedInputBuilder.java,v $
 * Revision 1.1  2013/03/29 09:36:26  cchun
 * Add:创建
 *
 * Revision 1.1  2011/11/21 09:46:20  cchun
 * Refactor:移动到common
 *
 * Revision 1.3  2011/08/10 06:33:14  cchun
 * Update:整理格式
 *
 * Revision 1.2  2010/09/03 03:45:58  cchun
 * Refactor:使用getter,setter
 *
 * Revision 1.1  2010/06/18 09:46:00  cchun
 * Update:设置初始菜单禁用
 *
 */
public class ReceivedInputBuilder {
	private DoNode doNode;
	private String prePortion;
	private String lDeviceInst;
	private LNInfo lnInfo;
	/*
	 * path格式:
	 * LDeviceInst/prefix+lnClass+lnInst.DO.DA
	 */
	private String path;
	private String desc;
	
	public ReceivedInputBuilder(String path, String desc) {
		this.path = path;
		this.desc = desc;
	}
	
	/**
	 * 解析数据项或虚端子参引
	 */
	public void parse(){
		int slashPos = path.indexOf("/");
		lDeviceInst = path.substring(0, slashPos);
		String input = path;
		int start = input.indexOf(".");
		prePortion = input.substring(slashPos + 1, start);

		String[] tmp = SCLUtil.getLNInfo(prePortion);
		lnInfo = new LNInfo();
		lnInfo.setPrefix(tmp[0]);
		lnInfo.setLnClass(tmp[1]);
		lnInfo.setInst(tmp[2]);

		int endDo = input.indexOf(".", start + 1);

		doNode = new DoNode();
		String[] das = null;
		if (endDo != -1) {
			das = input.substring(endDo + 1).split("\\.");
			doNode.setName(input.substring(start + 1, endDo));

			List<String> lstDas = new ArrayList<String>();
			for (String str : das) {
				lstDas.add(str);
			}

			doNode.setOtherDAName(lstDas);
		} else {
			doNode.setName(input.substring(start + 1, input.length()));
		}

		doNode.setDesc(desc);
	}
	
	public DoNode getDoNode(){
		return doNode;
	}
	
	public LNInfo getLNInfo(){
		return lnInfo;
	}
	
	public String getInst(){
		return lDeviceInst;
	}
}
