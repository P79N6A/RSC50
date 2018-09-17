package com.synet.tool.rsc.excel.handler;

import java.util.Map;

import org.apache.poi.xssf.usermodel.XSSFComment;

import com.synet.tool.rsc.DBConstants;
import com.synet.tool.rsc.ExcelConstants;
import com.synet.tool.rsc.model.IM106PortLightEntity;

public class PortLightHandler extends RscSheetHandler {

	private IM106PortLightEntity entity;
	
	public PortLightHandler(int headRowNum, Map<Integer, String> excelColInfo) {
		super(headRowNum, excelColInfo);
	}
	
	@Override
	public void startRow(int rowNum) {
		super.startRow(rowNum);
		this.entity = new IM106PortLightEntity();
		this.entity.setIm106Code(rscp.nextTbCode(DBConstants.PR_PORTLIGHT));
		this.entity.setMatched(DBConstants.MATCHED_NO);
	}
	
	@Override
	public void endRow(int rowNum) {
		if (rowNum <= headRowNum) return;
		if (entity == null) {
			String error = "第" + (rowNum + 1) + "行";
			errorMsg.add(error);
		} else {
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
		if (entity == null || excelColInfo == null)
			return;
		String fieldName = excelColInfo.get(col);
		if (fieldName == null)
			return;
		switch(fieldName) {
			case ExcelConstants.IM106_DEV_NAME: 
				entity.setDevName(value);
				break;
			case ExcelConstants.IM106_DEV_DESC: 
				entity.setDevDesc(value);
				break;
			case ExcelConstants.IM106_OPTICAL_REF_ADDR: 
				entity.setOpticalRefAddr(value);
				break;
			case ExcelConstants.IM106_OPTICAL_DESC: 
				entity.setOpticalDesc(value);
				break;
			case ExcelConstants.IM106_BOARD_CODE: 
				entity.setBoardCode(value);
				break;
			case ExcelConstants.IM106_PORT_CODE: 
				entity.setPortCode(value);
				break;
			default:
				break;
		}
	}


}
