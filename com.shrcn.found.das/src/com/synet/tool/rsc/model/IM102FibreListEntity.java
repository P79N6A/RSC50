package com.synet.tool.rsc.model;

/**
 *光缆清册
 */
public class IM102FibreListEntity {
	
	private String im102Code;
	private String cableCode;			//光缆编号
	private String coreCode;			//纤芯编号
	private String devCodeA;			//端口A装置代码
	private String devNameA;			//端口A装置Name
	private String devDescA;			//端口A装置名称
	private String boardCodeA;			//端口A板卡编号
	private String portCodeA;			//端口A端口编号
	private String cubicleCodeA;		//端口A屏柜代码
	private String cubicleDescA;		//端口A屏柜名称
	private String coreCodeA;         	//端口A芯线编号
	private String distribFrameCodeA;   //端口A光配架端口编号
	private String devCodeB;
	private String devNameB;
	private String devDescB;
	private String portCodeB;
	private String boardCodeB;
	private String cubicleCodeB;
	private String cubicleDescB;
	private String coreCodeB;
	private String distribFrameCodeB;
	private Integer matched;
	private IM100FileInfoEntity fileInfoEntity;
	
	private int conflict; // 是否冲突：1-是，2-否
	private boolean overwrite; // 是否覆盖
	
	public String getIm102Code() {
		return im102Code;
	}
	public void setIm102Code(String im102Code) {
		this.im102Code = im102Code;
	}
	public String getCableCode() {
		return cableCode;
	}
	public void setCableCode(String cableCode) {
		this.cableCode = cableCode;
	}
	public String getCoreCode() {
		return coreCode;
	}
	public void setCoreCode(String coreCode) {
		this.coreCode = coreCode;
	}
	public String getDevCodeA() {
		return devCodeA;
	}
	public void setDevCodeA(String devCodeA) {
		this.devCodeA = devCodeA;
	}
	public String getDevNameA() {
		return devNameA;
	}
	public void setDevNameA(String devNameA) {
		this.devNameA = devNameA;
	}
	public String getDevDescA() {
		return devDescA;
	}
	public void setDevDescA(String devDescA) {
		this.devDescA = devDescA;
	}
	public String getBoardCodeA() {
		return boardCodeA;
	}
	public void setBoardCodeA(String boardCodeA) {
		this.boardCodeA = boardCodeA;
	}
	public String getPortCodeA() {
		return portCodeA;
	}
	public void setPortCodeA(String portCodeA) {
		this.portCodeA = portCodeA;
	}
	public String getCubicleCodeA() {
		return cubicleCodeA;
	}
	public void setCubicleCodeA(String cubicleCodeA) {
		this.cubicleCodeA = cubicleCodeA;
	}
	public String getCubicleDescA() {
		return cubicleDescA;
	}
	public void setCubicleDescA(String cubicleDescA) {
		this.cubicleDescA = cubicleDescA;
	}
	public String getCoreCodeA() {
		return coreCodeA;
	}
	public void setCoreCodeA(String coreCodeA) {
		this.coreCodeA = coreCodeA;
	}
	public String getDistribFrameCodeA() {
		return distribFrameCodeA;
	}
	public void setDistribFrameCodeA(String distribFrameCodeA) {
		this.distribFrameCodeA = distribFrameCodeA;
	}
	public String getDevCodeB() {
		return devCodeB;
	}
	public void setDevCodeB(String devCodeB) {
		this.devCodeB = devCodeB;
	}
	public String getDevNameB() {
		return devNameB;
	}
	public void setDevNameB(String devNameB) {
		this.devNameB = devNameB;
	}
	public String getDevDescB() {
		return devDescB;
	}
	public void setDevDescB(String devDescB) {
		this.devDescB = devDescB;
	}
	public String getPortCodeB() {
		return portCodeB;
	}
	public void setPortCodeB(String portCodeB) {
		this.portCodeB = portCodeB;
	}
	public String getBoardCodeB() {
		return boardCodeB;
	}
	public void setBoardCodeB(String boardCodeB) {
		this.boardCodeB = boardCodeB;
	}
	public String getCubicleCodeB() {
		return cubicleCodeB;
	}
	public void setCubicleCodeB(String cubicleCodeB) {
		this.cubicleCodeB = cubicleCodeB;
	}
	public String getCubicleDescB() {
		return cubicleDescB;
	}
	public void setCubicleDescB(String cubicleDescB) {
		this.cubicleDescB = cubicleDescB;
	}
	public String getCoreCodeB() {
		return coreCodeB;
	}
	public void setCoreCodeB(String coreCodeB) {
		this.coreCodeB = coreCodeB;
	}
	public String getDistribFrameCodeB() {
		return distribFrameCodeB;
	}
	public void setDistribFrameCodeB(String distribFrameCodeB) {
		this.distribFrameCodeB = distribFrameCodeB;
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
	public int getConflict() {
		return conflict;
	}
	public void setConflict(int conflict) {
		this.conflict = conflict;
	}
	public boolean isOverwrite() {
		return overwrite;
	}
	public void setOverwrite(boolean overwrite) {
		this.overwrite = overwrite;
	}
	
}
