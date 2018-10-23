package com.synet.tool.rsc.util;

import com.shrcn.found.common.util.StringUtil;

public class Rule {
	private int id;
	private String name;
	private String datSet;
	private String lnName;
	private String doName;
	private String doDesc;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDatSet() {
		return datSet;
	}
	public void setDatSet(String datSet) {
		this.datSet = datSet;
	}
	public String getLnName() {
		return lnName;
	}
	public void setLnName(String lnName) {
		this.lnName = lnName;
	}

	public String getDoName() {
		return doName;
	}

	public void setDoName(String doName) {
		this.doName = doName;
	}

	public String getDoDesc() {
		return doDesc;
	}

	public void setDoDesc(String doDesc) {
		this.doDesc = doDesc;
	}
	
	public boolean isEmpty() {
		return StringUtil.isEmpty(datSet) &&
				StringUtil.isEmpty(lnName) && 
				StringUtil.isEmpty(doName) && 
				StringUtil.isEmpty(doDesc);
	}
}
