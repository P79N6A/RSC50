package com.synet.tool.rsc.model;

/**
 * 
 * 跳合闸反校关联
 *
 */
public class BrkCfmEntity {
	
	private String im108Code;
	private String devName;				//装置Name
	private String pinRefAddr;			//开入虚端子参引
	private String pinDesc;				//开入虚端子描述
	private String cmdAckVpRefAddr;		//命令确认虚端子参引
	private String cmdAckVpDesc;		//命令确认虚端子描述
	private String cmdOutVpRefAddr;		//命令出口虚端子参引
	private String cmdOutVpDesc;		//命令出口虚端子描述
	private Integer matched;			//是否匹配
	private FileInfoEntity fileInfoEntity;
	
	public String getIm108Code() {
		return im108Code;
	}
	public void setIm108Code(String im108Code) {
		this.im108Code = im108Code;
	}
	public String getDevName() {
		return devName;
	}
	public void setDevName(String devName) {
		this.devName = devName;
	}
	public String getPinRefAddr() {
		return pinRefAddr;
	}
	public void setPinRefAddr(String pinRefAddr) {
		this.pinRefAddr = pinRefAddr;
	}
	public String getPinDesc() {
		return pinDesc;
	}
	public void setPinDesc(String pinDesc) {
		this.pinDesc = pinDesc;
	}
	public String getCmdAckVpRefAddr() {
		return cmdAckVpRefAddr;
	}
	public void setCmdAckVpRefAddr(String cmdAckVpRefAddr) {
		this.cmdAckVpRefAddr = cmdAckVpRefAddr;
	}
	public String getCmdAckVpDesc() {
		return cmdAckVpDesc;
	}
	public void setCmdAckVpDesc(String cmdAckVpDesc) {
		this.cmdAckVpDesc = cmdAckVpDesc;
	}
	public String getCmdOutVpRefAddr() {
		return cmdOutVpRefAddr;
	}
	public void setCmdOutVpRefAddr(String cmdOutVpRefAddr) {
		this.cmdOutVpRefAddr = cmdOutVpRefAddr;
	}
	public String getCmdOutVpDesc() {
		return cmdOutVpDesc;
	}
	public void setCmdOutVpDesc(String cmdOutVpDesc) {
		this.cmdOutVpDesc = cmdOutVpDesc;
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
