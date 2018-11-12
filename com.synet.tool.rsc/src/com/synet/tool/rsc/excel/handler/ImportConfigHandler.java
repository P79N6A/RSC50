package com.synet.tool.rsc.excel.handler;

import java.util.ArrayList;
import java.util.List;

import org.apache.poi.xssf.usermodel.XSSFComment;

import com.shrcn.found.common.util.ObjectUtil;
import com.shrcn.found.file.excel.SheetsHandler;
import com.synet.tool.rsc.DBConstants;
import com.synet.tool.rsc.excel.ImportChecker;
import com.synet.tool.rsc.excel.ImportConfig;
import com.synet.tool.rsc.excel.ImportField;

public class ImportConfigHandler extends SheetsHandler {

	private int headRowNum;
	private ImportConfig config;
	private List<String> errorMsg;

	private Object entity;
	
	public ImportConfigHandler(int headRowNum, ImportConfig config) {
		this.headRowNum = headRowNum;
		this.config = config;
		this.result = new ArrayList<>();
		this.errorMsg = new ArrayList<>();
	}
	
	@Override
	public void startRow(int rowNum) {
		super.startRow(rowNum);
		if (rowNum >= headRowNum) {
			this.entity = config.createEntity();
		}
	}
	
	@Override
	public void endRow(int rowNum) {
		if (rowNum <= headRowNum)
			return;
		if (entity == null) {
			String error = "第" + (rowNum + 1) + "行";
			errorMsg.add(error);
		} else if (ImportChecker.isValid(entity)) {
			ObjectUtil.setProperty(entity, "matched", DBConstants.MATCHED_NO);
			result.add(entity);
		}
		super.endRow(rowNum);
	}
	
	@Override
	public void cell(String cellReference, String formattedValue,
			XSSFComment comment) {
		super.cell(cellReference, formattedValue, comment);
		if (currentRow > headRowNum && !isEmpty(formattedValue)) {
			saveValue(currentCol, formattedValue);
		}
	}
	
	private void saveValue(int col, String value) {
		ImportField field = config.getEntityPropties().get(col);
		if (entity == null || field == null) {
			return;
		}
		ObjectUtil.setProperty(entity, field.getFieldName(), value);
	}
	
}
