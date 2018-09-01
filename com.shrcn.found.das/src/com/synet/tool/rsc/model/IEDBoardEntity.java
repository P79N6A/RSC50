package com.synet.tool.rsc.model;


/** 
 * 
 * 装置板卡端口描述
 *
 */
public class IEDBoardEntity {

	private String im103Code;
	private String devName; 	//装置Name
	private String devDesc;		//装置名称
	private String boardCode;	//板卡编号
	private String portCode;	//端口编号
	private Integer matched;
	private FileInfoEntity fileInfoEntity;
	
	public String getIm103Code() {
		return im103Code;
	}
	public void setIm103Code(String im103Code) {
		this.im103Code = im103Code;
	}
	public String getDevName() {
		return devName;
	}
	public void setDevName(String devName) {
		this.devName = devName;
	}
	public String getDevDesc() {
		return devDesc;
	}
	public void setDevDesc(String devDesc) {
		this.devDesc = devDesc;
	}
	public String getBoardCode() {
		return boardCode;
	}
	public void setBoardCode(String boardCode) {
		this.boardCode = boardCode;
	}
	public String getPortCode() {
		return portCode;
	}
	public void setPortCode(String portCode) {
		this.portCode = portCode;
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
