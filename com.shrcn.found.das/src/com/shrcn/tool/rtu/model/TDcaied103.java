/**
 * Copyright (c) 2007-2010 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based on IEC61850 SCT.
 */
package com.shrcn.tool.rtu.model;

import java.util.HashSet;
import java.util.Set;

import com.shrcn.found.common.util.ObjectUtil;

/**
 * 
 * @author 孙春颖
 * @version 1.0, 2013 7 17
 */
/**
 * $Log: TDcaied103.java,v $
 * Revision 1.6  2013/09/02 00:56:19  cxc
 * update:修改copy方法
 *
 * Revision 1.5  2013/07/24 12:08:09  cxc
 * Update：修改
 *
 * Revision 1.4  2013/07/23 10:29:27  cxc
 * Update：修改
 *
 * Revision 1.3  2013/07/22 09:18:20  cxc
 * Add:添加TDca
 *
 * Revision 1.2  2013/07/18 03:03:43  scy
 * Update：增加iedAddr
 *
 * Revision 1.1  2013/07/17 09:17:47  scy
 * Add：创建装置
 *
 */
public class TDcaied103 extends BaseDcaied implements java.io.Serializable {

	private static final long serialVersionUID = -8698126465342109211L;

	private String iedAddr;
	private String linkAddr;
	private String totalTime;
	private int groupOffset;
	private int groupNumber;
    
	private Set<TDcaGroup> groups = new HashSet<TDcaGroup>();

	public TDcaied103 copy() {
		TDcaied103 ied = (TDcaied103) ObjectUtil.duplicate(this);
		ied.setGroups(null);
		return ied;
	}

	public Set<TDcaGroup> getGroups() {
		return groups;
	}

	public void setGroups(Set<TDcaGroup> groups) {
		this.groups = groups;
	}

	public void addGroup(TDcaGroup group) {
		getGroups().add(group);
		group.setIed(this);
	}

	public String getLinkAddr() {
		return linkAddr;
	}

	public void setLinkAddr(String linkAddr) {
		this.linkAddr = linkAddr;
	}

	public String getTotalTime() {
		return totalTime;
	}

	public void setTotalTime(String totalTime) {
		this.totalTime = totalTime;
	}

	public int getGroupOffset() {
		return groupOffset;
	}

	public void setGroupOffset(int groupOffset) {
		this.groupOffset = groupOffset;
	}

	public int getGroupNumber() {
		return groupNumber;
	}

	public void setGroupNumber(int groupNumber) {
		this.groupNumber = groupNumber;
	}

	public String getIedAddr() {
		return iedAddr;
	}

	public void setIedAddr(String iedAddr) {
		this.iedAddr = iedAddr;
	}
}
