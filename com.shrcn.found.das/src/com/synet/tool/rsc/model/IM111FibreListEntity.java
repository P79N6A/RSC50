package com.synet.tool.rsc.model;


/**
 *光缆清册
 */
public class IM111FibreListEntity {
	
	private String im111Code;
	private String cableCode;			//光缆编号
	private String coreCode;			//纤芯编号
	private String connType;			//连接线类型
	private String devNameA;			//端口A装置Name
	private String devDescA;			//端口A装置名称
	private String boardCodeA;			//端口A板卡编号
	private String portCodeA;			//端口A端口编号
	private String devNameB;
	private String devDescB;
	private String boardCodeB;
	private String portCodeB;
	private Integer matched;
	private IM100FileInfoEntity fileInfoEntity;
	
	private int conflict = 2; // 是否冲突：1-是，2-否
	private boolean overwrite; // 是否覆盖
	
	public String getIm111Code() {
		return im111Code;
	}


	public void setIm111Code(String im111Code) {
		this.im111Code = im111Code;
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


	public String getConnType() {
		return connType;
	}


	public void setConnType(String connType) {
		this.connType = connType;
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


	public String getBoardCodeB() {
		return boardCodeB;
	}


	public void setBoardCodeB(String boardCodeB) {
		this.boardCodeB = boardCodeB;
	}


	public String getPortCodeB() {
		return portCodeB;
	}


	public void setPortCodeB(String portCodeB) {
		this.portCodeB = portCodeB;
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


	@Override
	public String toString() {
		return "cableCode=" + cableCode + ", coreCode="
				+ coreCode + ", connType=" + connType + ", devNameA="
				+ devNameA + ", devDescA=" + devDescA + ", boardCodeA="
				+ boardCodeA + ", portCodeA=" + portCodeA + ", devNameB="
				+ devNameB + ", devDescB=" + devDescB + ", boardCodeB="
				+ boardCodeB + ", portCodeB=" + portCodeB;
	}

}
