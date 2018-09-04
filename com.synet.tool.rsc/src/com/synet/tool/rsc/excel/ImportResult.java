package com.synet.tool.rsc.excel;

import com.synet.tool.rsc.model.IM100FileInfoEntity;

public class ImportResult {

	private IM100FileInfoEntity fileInfoEntity;
	private Object result;
	
	public ImportResult() {
		super();
	}
	public ImportResult(IM100FileInfoEntity fileInfoEntity, Object result) {
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
	public Object getResult() {
		return result;
	}
	public void setResult(Object result) {
		this.result = result;
	}
	
}
