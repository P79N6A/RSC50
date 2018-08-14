/**
 * Copyright (c) 2007-2015 上海思源弘瑞自动化有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based on IEC61850 SCT.
 */
package com.shrcn.business.scl.check;

import java.sql.Timestamp;


 /**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2015-8-22
 */
public class Problem {

	private int id;
	private LEVEL level;
	private String iedName;
	private String subType;
	private String ref;
	private String desc;
	private Timestamp time;
	private String detailInfo;
	
	public Problem() {}
	
	public Problem(int id, LEVEL level, String iedName, String subType,
			String ref, String desc) {
		this.id = id;
		this.level = level;
		this.iedName = iedName;
		this.subType = subType;
		this.ref = ref;
		this.desc = desc;
	}

	public Problem(int id, LEVEL level, String iedName, String subType,
			String ref, String desc, String detailInfo) {
		this(id, level, iedName, subType, ref, desc);
		this.detailInfo = detailInfo;
	}
	
	public static Problem create(LEVEL level, String iedName, String subType,
			String ref, String desc) {
		return new Problem(0, level, iedName, subType, ref, desc);
	}
	
	public static Problem create(LEVEL level, String iedName, String subType,
			String ref, String desc, String detailInfo) {
		return new Problem(0, level, iedName, subType, ref, desc, detailInfo);
	}
	
	public static Problem createError(String iedName, String subType,
			String ref, String desc) {
		return create(LEVEL.ERROR, iedName, subType, ref, desc);
	}
	
	public static Problem createWarning(String iedName, String subType,
			String ref, String desc) {
		return create(LEVEL.WARNING, iedName, subType, ref, desc);
	}

	public static Problem createError(String iedName, String subType,
			String ref, String desc, String detailInfo) {
		return create(LEVEL.ERROR, iedName, subType, ref, desc, detailInfo);
	}
	
	public static Problem createWarning(String iedName, String subType,
			String ref, String desc, String detailInfo) {
		return create(LEVEL.WARNING, iedName, subType, ref, desc, detailInfo);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public LEVEL getLevel() {
		return level;
	}

	public void setLevel(LEVEL level) {
		this.level = level;
	}

	public String getIcon() {
		return level.getIcon();
	}

	public String getIedName() {
		return iedName;
	}

	public void setIedName(String iedName) {
		this.iedName = iedName;
	}

	public String getSubType() {
		return subType;
	}

	public void setSubType(String subType) {
		this.subType = subType;
	}

	public String getRef() {
		return ref;
	}

	public void setRef(String ref) {
		this.ref = ref;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public Timestamp getTime() {
		return time;
	}

	public void setTime(Timestamp time) {
		this.time = time;
	}
	
	public String getDetailInfo() {
		return detailInfo;
	}
	
	public void setDetailInfo(String detailInfo) {
		this.detailInfo = detailInfo;
	}

	@Override
	public String toString() {
		return level + "\t" + iedName + "\t" + subType + "\t" + ref + "\t" + desc;
	}
}