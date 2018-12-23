package com.synet.tool.rsc.compare;

public enum Status {

	Parsed("已分析"), UnParsed("已分析");
	
	private String desc;

	Status(String desc) {
		this.desc = desc;
	}

	public String getDesc() {
		return desc;
	}
	
	
}
