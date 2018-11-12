package com.synet.tool.rsc.excel;

import java.util.List;

import com.synet.tool.rsc.model.IM100FileInfoEntity;

public class ImportResult {

	private IM100FileInfoEntity fileInfoEntity;
	private List<?> result; // list
	
	public ImportResult() {
		super();
	}
	public ImportResult(IM100FileInfoEntity fileInfoEntity, List<?> result) {
		super();
		this.fileInfoEntity = fileInfoEntity;
		this.result = result;
	}
	public IM100FileInfoEntity getFileInfoEntity() {
		return fileInfoEntity;
	}
	public void setFileInfoEntity(IM100FileInfoEntity fileInfoEntity) {
		this.fileInfoEntity = fileInfoEntity;
	}
	public List<?> getResult() {
		return result;
	}
	public void setResult(List<?> result) {
		this.result = result;
	}
	
}
