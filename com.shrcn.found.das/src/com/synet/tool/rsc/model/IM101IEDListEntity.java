package com.synet.tool.rsc.model;

/**
 * 
 * 设备台账
 *
 */
public class IM101IEDListEntity {

	private String im101Code;
	private Integer index;					//序号
	private String devName;				//装置Name
	private String devDesc;				//装置名称
	private String bay;					//所属间隔
	private String cubicle;				//所属屏柜
	private String manufacturor;		//生产厂家
	private String devType;				//装置型号
	private String devVersion;			//装置版本号
	private String AorB;				//A/B套
	private String protClassify;		//保护分类（国产、进口）
	private String protModel;			//保护型号
	private String protType;			//保护类型
	private String dateService;			//投运日期
	private String dateProduct;			//出厂日期
	private String productCode;			//出厂编号
	private String dataCollectType;		//数据采集方式
	private String outType;				//出口方式
	private int boardNum;				//板卡数量
	private String matchedIedCode;		//匹配装置代码
	private Integer matched;			//是否匹配
	private IM100FileInfoEntity fileInfoEntity;
	
	public String getIm101Code() {
		return im101Code;
	}
	public void setIm101Code(String im101Code) {
		this.im101Code = im101Code;
	}
	public Integer getIndex() {
		return index;
	}
	public void setIndex(Integer index) {
		this.index = index;
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
	public String getBay() {
		return bay;
	}
	public void setBay(String bay) {
		this.bay = bay;
	}
	public String getCubicle() {
		return cubicle;
	}
	public void setCubicle(String cubicle) {
		this.cubicle = cubicle;
	}
	public String getManufacturor() {
		return manufacturor;
	}
	public void setManufacturor(String manufacturor) {
		this.manufacturor = manufacturor;
	}
	public String getDevType() {
		return devType;
	}
	public void setDevType(String devType) {
		this.devType = devType;
	}
	public String getDevVersion() {
		return devVersion;
	}
	public void setDevVersion(String devVersion) {
		this.devVersion = devVersion;
	}
	public String getAorB() {
		return AorB;
	}
	public void setAorB(String aorB) {
		AorB = aorB;
	}
	public String getProtClassify() {
		return protClassify;
	}
	public void setProtClassify(String protClassify) {
		this.protClassify = protClassify;
	}
	public String getProtModel() {
		return protModel;
	}
	public void setProtModel(String protModel) {
		this.protModel = protModel;
	}
	public String getProtType() {
		return protType;
	}
	public void setProtType(String protType) {
		this.protType = protType;
	}
	public String getDateService() {
		return dateService;
	}
	public void setDateService(String dateService) {
		this.dateService = dateService;
	}
	public String getDateProduct() {
		return dateProduct;
	}
	public void setDateProduct(String dateProduct) {
		this.dateProduct = dateProduct;
	}
	public String getProductCode() {
		return productCode;
	}
	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}
	public String getDataCollectType() {
		return dataCollectType;
	}
	public void setDataCollectType(String dataCollectType) {
		this.dataCollectType = dataCollectType;
	}
	public String getOutType() {
		return outType;
	}
	public void setOutType(String outType) {
		this.outType = outType;
	}
	public int getBoardNum() {
		return boardNum;
	}
	public void setBoardNum(int boardNum) {
		this.boardNum = boardNum;
	}
	public String getMatchedIedCode() {
		return matchedIedCode;
	}
	public void setMatchedIedCode(String matchedIedCode) {
		this.matchedIedCode = matchedIedCode;
	}
	public Integer getMatched() {
		return matched;
	}
	public void setMatched(Integer matched) {
		this.matched = matched;
	}
	public IM100FileInfoEntity getFileInfoEntity() {
		return fileInfoEntity;
	}
	public void setFileInfoEntity(IM100FileInfoEntity fileInfoEntity) {
		this.fileInfoEntity = fileInfoEntity;
	}
	
}
