package com.synet.tool.rsc.entity;

/**
 * 
 * 监控信息点表
 *
 */
public class StaInfo {
	
	private int index; 			//序号
	private String description; //描述
	private String refAddr;		//参引
	
	public int getIndex() {
		return index;
	}
	public void setIndex(int index) {
		this.index = index;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getRefAddr() {
		return refAddr;
	}
	public void setRefAddr(String refAddr) {
		this.refAddr = refAddr;
	}

}
