package com.synet.tool.rsc.model;


/** 
 * 
 * 装置板卡端口描述
 *
 */
public class IM103IEDBoardEntity {

	private String im103Code;
	private String devName; 	//装置Name
	private String devDesc;		//装置名称
	private String manufacturor;//制造厂家
//	private String devModel;	//保护型号
	private String configVersion;//软件版本
	private String boardIndex;  //板卡序号
	private String boardModel;	//板卡型号
	private String boardType;	//板卡类别/用途
	private String boardCode;	//板卡编号
	private String portNum;		//端口数量
	private String portCode;	//端口编号
	private Integer matched;
	private IM100FileInfoEntity fileInfoEntity;
	
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
	public String getManufacturor() {
		return manufacturor;
	}
	public void setManufacturor(String manufacturor) {
		this.manufacturor = manufacturor;
	}
//	public String getDevModel() {
//		return devModel;
//	}
//	public void setDevModel(String devModel) {
//		this.devModel = devModel;
//	}
	public String getConfigVersion() {
		return configVersion;
	}
	public void setConfigVersion(String configVersion) {
		this.configVersion = configVersion;
	}
	public String getBoardIndex() {
		return boardIndex;
	}
	public void setBoardIndex(String boardIndex) {
		this.boardIndex = boardIndex;
	}
	public String getBoardModel() {
		return boardModel;
	}
	public void setBoardModel(String boardModel) {
		this.boardModel = boardModel;
	}
	public String getBoardType() {
		return boardType;
	}
	public void setBoardType(String boardType) {
		this.boardType = boardType;
	}
	public String getBoardCode() {
		return boardCode;
	}
	public void setBoardCode(String boardCode) {
		this.boardCode = boardCode;
	}
	public String getPortNum() {
		return portNum;
	}
	public void setPortNum(String portNum) {
		this.portNum = portNum;
	}
	public Integer getMatched() {
		return matched;
	}
	public void setMatched(Integer matched) {
		this.matched = matched;
	}
	public String getPortCode() {
		return portCode;
	}
	public void setPortCode(String portCode) {
		this.portCode = portCode;
	}
	public IM100FileInfoEntity getFileInfoEntity() {
		return fileInfoEntity;
	}
	public void setFileInfoEntity(IM100FileInfoEntity fileInfoEntity) {
		this.fileInfoEntity = fileInfoEntity;
	}

}
