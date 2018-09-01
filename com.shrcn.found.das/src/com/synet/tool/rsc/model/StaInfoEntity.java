package com.synet.tool.rsc.model;

/**
 * 
 * 监控信息点表
 *
 */
public class StaInfoEntity {
	
	private String im109Code;
	private Integer index; 		//序号
	private String description; //描述
	private String refAddr;		//参引
	private Integer matched;     //是否匹配
	private FileInfoEntity fileInfoEntity;
	
	public String getIm109Code() {
		return im109Code;
	}
	public void setIm109Code(String im109Code) {
		this.im109Code = im109Code;
	}
	public Integer getIndex() {
		return index;
	}
	public void setIndex(Integer index) {
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
	public Integer getMatched() {
		return matched;
	}
	public void setMatched(Integer matched) {
		this.matched = matched;
	}
	public FileInfoEntity getFileInfoEntity() {
		return fileInfoEntity;
	}
	public void setFileInfoEntity(FileInfoEntity fileInfoEntity) {
		this.fileInfoEntity = fileInfoEntity;
	}

}
