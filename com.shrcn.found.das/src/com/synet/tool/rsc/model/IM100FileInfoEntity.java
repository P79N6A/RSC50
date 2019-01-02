package com.synet.tool.rsc.model;

public class IM100FileInfoEntity extends Deletable {
	
	private String im100Code;
	private String fileName;
	private String filePath;
	private Integer fileType;
	
	public String getIm100Code() {
		return im100Code;
	}
	public void setIm100Code(String im100Code) {
		this.im100Code = im100Code;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	public Integer getFileType() {
		return fileType;
	}
	public void setFileType(Integer fileType) {
		this.fileType = fileType;
	}
}
