/**
 * Copyright (c) 2007, 2008 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based Visual Device Develop System.
 */
package com.shrcn.business.scl.model.navgtree;

import com.shrcn.business.scl.common.DefaultInfo;

/**
 * 
 * @author 刘静(mailto:lj6061@shrcn.com)
 * @version 1.0, 2009-5-15
 */
/*
 * 修改历史 $Log: LNodeEntry.java,v $
 * 修改历史 Revision 1.1  2013/03/29 09:35:21  cchun
 * 修改历史 Add:创建
 * 修改历史
 * 修改历史 Revision 1.6  2012/03/22 03:05:58  cchun
 * 修改历史 Refactor:统一LNodeEntry初始化方法
 * 修改历史
 * 修改历史 Revision 1.5  2010/02/09 03:18:22  cchun
 * 修改历史 Update:修改方法名，并增加属性
 * 修改历史
 * 修改历史 Revision 1.4  2010/02/08 10:41:04  cchun
 * 修改历史 Refactor:完成第一阶段重构
 * 修改历史
 * 修改历史 Revision 1.3  2010/02/03 07:35:32  hqh
 * 修改历史 添加属性变量
 * 修改历史
 * 修改历史 Revision 1.2  2009/09/22 05:33:12  hqh
 * 修改历史 方法移动到逻辑节点
 * 修改历史
 * 修改历史 Revision 1.1  2009/08/27 02:22:41  cchun
 * 修改历史 Refactor:重构导航树模型包路径
 * 修改历史
 * 修改历史 Revision 1.6  2009/05/26 06:17:36  lj6061
 * 修改历史 默认菜单新建SSD
 * 修改历史
 * 修改历史 Revision 1.5  2009/05/26 03:11:48  hqh
 * 修改历史 删除逻辑节点属性
 * 修改历史
 * 修改历史 Revision 1.4  2009/05/22 11:46:30  hqh
 * 修改历史 添加全局变量
 * 修改历史 修改历史 Revision 1.3 2009/05/22 09:43:11 hqh 修改历史
 * 添加map 修改历史 修改历史 Revision 1.2 2009/05/22 03:03:58 lj6061 修改历史 修改节点属性添加优先级 修改历史
 * 修改历史 Revision 1.1 2009/05/18 07:08:11 lj6061 修改历史 导入1次信息 修改历史
 */
public class LNodeEntry extends TreeEntryImpl {

	private static final long serialVersionUID = -3809778406206574955L;
	private String iedName;
	private String ldInst;
	private String lnType;
	private String prefix;
	private String lnClass;
	private String lnInst;
	
	public LNodeEntry(){
	}
	
	public LNodeEntry(String name, String xpath, String iconName, int priority) {
		super(name, xpath, iconName, priority);
	}
	
	public String getLnClass() {
		return lnClass;
	}

	public void setLnClass(String lnClass) {
		this.lnClass = lnClass;
	}

	public String getLnInst() {
		return lnInst;
	}

	public void setLnInst(String lnInst) {
		this.lnInst = lnInst;
	}

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public String getLdInst() {
		return ldInst;
	}

	public void setLdInst(String ldInst) {
		this.ldInst = ldInst;
	}

	public String getIedName() {
		return iedName;
	}

	public void setIedName(String iedName) {
		this.iedName = iedName;
	}

	public String getLnType() {
		return lnType;
	}

	public void setLnType(String lnType) {
		this.lnType = lnType;
	}
	
	/**
	 * 设置LNode值。
	 * @param iedName
	 * @param ldInst
	 * @param prefix
	 * @param lnClass
	 * @param lnInst
	 * @param lnType
	 */
	public void setValues(String iedName, String ldInst, String prefix, String lnClass, String lnInst, String lnType) {
		this.iedName = iedName;
		this.ldInst = ldInst;
		this.prefix = prefix;
		this.lnClass = lnClass;
		this.lnInst = lnInst;
		this.lnType = lnType;
		setName(iedName + "/" + ldInst + "." + prefix
				+ lnClass + lnInst + ":" + lnType);
	}
	
	/**
	 * 设置缺省值。
	 * @param lnClass
	 * @param lnInst
	 */
	public void setDefaults(String lnClass, String lnInst) {
		this.iedName = DefaultInfo.IED_NAME;
		this.ldInst = DefaultInfo.UNREL_LNODE_LDINST;
		this.prefix = DefaultInfo.UNREL_LNODE_PREFIX;
		this.lnType = DefaultInfo.LNTYPE;
		this.lnClass = lnClass;
		this.lnInst = lnInst;
		setName(iedName + "/" + ldInst + "." + prefix
				+ lnClass + lnInst + ":" + lnType);
	}
}
