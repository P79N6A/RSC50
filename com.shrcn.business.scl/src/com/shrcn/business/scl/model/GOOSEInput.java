/**
 * Copyright (c) 2007, 2008 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based Visual Device Develop System.
 */
package com.shrcn.business.scl.model;

import org.dom4j.Element;

import com.shrcn.found.file.xml.DOM4JNodeHelper;

/**
 * <ExtRef iedName="IM5001" prefix="MGD4" doName="Pos" lnInst="1" 
 * lnClass="XSWI" daName="t" ldInst="RPIT" intAddr="GOLD/Q0GGIO1.DPCSO6.t" />
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2009-4-24
 */
/*
 * 修改历史
 * $Log: GOOSEInput.java,v $
 * Revision 1.1  2013/03/29 09:36:39  cchun
 * Add:创建
 *
 * Revision 1.9  2011/12/12 09:28:14  cchun
 * Update:添加needsOutSignal(),needsInSignal()
 *
 * Revision 1.8  2011/11/30 11:12:32  cchun
 * Update:统一使用DOM4JNodeHelper.getAttribute()，去掉未使用方法getInputsXPath()
 *
 * Revision 1.7  2009/12/07 03:59:38  cchun
 * Update:增加是否关联判断函数
 *
 * Revision 1.6  2009/09/01 09:37:46  cchun
 * Fix Bug:修改删除多个关联信号bug
 *
 * Revision 1.5  2009/08/05 05:51:21  cchun
 * Update:合并代码
 *
 * Revision 1.4.2.1  2009/08/05 03:41:11  cchun
 * Update:添加getOutAddr()
 *
 * Revision 1.4  2009/07/15 02:58:36  cchun
 * 添加信号关联调整功能
 *
 * Revision 1.3  2009/05/20 03:28:08  cchun
 * Update:添加getIEDName()
 *
 * Revision 1.2  2009/05/18 05:53:37  cchun
 * Update:添加getInSignalDesc()
 *
 * Revision 1.1  2009/04/27 03:45:01  cchun
 * Add:添加goose类
 *
 */
public class GOOSEInput {
	
	private String outSignalDesc;
	private String inSignalDesc;
	private Element data;
	
	public GOOSEInput(String outDesc, String inDesc, Element data) {
		this.outSignalDesc = outDesc;
		this.inSignalDesc = inDesc;
		this.data = data;
	}

	public String getIEDName() {
		return DOM4JNodeHelper.getAttribute(data, SCL.EXTREF_IEDNAME);
	}
	
	public String getLDInst() {
		return DOM4JNodeHelper.getAttribute(data, SCL.EXTREF_LDINST);
	}
	
	public String getLNPrefix() {
		return DOM4JNodeHelper.getAttribute(data, SCL.EXTREF_LNPREFIX);
	}
	
	public String getLNClass() {
		return DOM4JNodeHelper.getAttribute(data, SCL.EXTREF_LNCALSS);
	}
	
	public String getLNInst() {
		return DOM4JNodeHelper.getAttribute(data, SCL.EXTREF_LNINST);
	}
	
	public String getDOName() {
		return DOM4JNodeHelper.getAttribute(data, SCL.EXTREF_DONAME);
	}
	
	public String getDAName() {
		return DOM4JNodeHelper.getAttribute(data, SCL.EXTREF_DANAME);
	}
	
	public String getIntAddr() {
		return DOM4JNodeHelper.getAttribute(data, SCL.EXTREF_INTADDR);
	}
	
	public String getInSignalDesc() {
		return inSignalDesc;
	}

	public void setInSignalDesc(String inSignalDesc) {
		this.inSignalDesc = inSignalDesc;
	}

	public Element getData() {
		return data;
	}

	public void setData(Element data) {
		this.data = data;
	}

	public String getOutSignalDesc() {
		return outSignalDesc;
	}

	public void setOutSignalDesc(String outSignalDesc) {
		this.outSignalDesc = outSignalDesc;
	}
	
	public GOOSEInput copy() {
		return new GOOSEInput(outSignalDesc, inSignalDesc, data.createCopy());
	}
	
	/**
	 * 获取外部信号路径
	 * @return
	 */
	public String getOutAddr() {
		return SCL.getNodeRef(data);
	}
	
	/**
	 * 判断是否已经完成关联
	 * @return
	 */
	public boolean isCompleted() {
		String intAddr = DOM4JNodeHelper.getAttribute(data, SCL.EXTREF_INTADDR);
		String ldInst = DOM4JNodeHelper.getAttribute(data, SCL.EXTREF_LDINST);
		return !"".equals(intAddr) && !"".equals(ldInst);
	}
	
	public boolean needsOutSignal() {
		String intAddr = DOM4JNodeHelper.getAttribute(data, SCL.EXTREF_INTADDR);
		String ldInst = DOM4JNodeHelper.getAttribute(data, SCL.EXTREF_LDINST);
		return !"".equals(intAddr) && "".equals(ldInst);
	}
	
	public boolean needsInSignal() {
		String intAddr = DOM4JNodeHelper.getAttribute(data, SCL.EXTREF_INTADDR);
		String ldInst = DOM4JNodeHelper.getAttribute(data, SCL.EXTREF_LDINST);
		return "".equals(intAddr) && !"".equals(ldInst);
	}
}
