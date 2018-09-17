package com.synet.tool.rsc.excel.handler;

import java.util.Map;

import org.apache.poi.xssf.usermodel.XSSFComment;

import com.synet.tool.rsc.DBConstants;
import com.synet.tool.rsc.ExcelConstants;
import com.synet.tool.rsc.model.IM105BoardWarnEntity;

public class BoardWarnHandler extends RscSheetHandler {

	private IM105BoardWarnEntity entity;
	
	public BoardWarnHandler(int headRowNum, Map<Integer, String> excelColInfo) {
		super(headRowNum, excelColInfo);
	}
	
	@Override
	public void startRow(int rowNum) {
		super.startRow(rowNum);
		this.entity = new IM105BoardWarnEntity();
		this.entity.setIm105Code(rscp.nextTbCode(DBConstants.PR_BOARDWARN));
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
			case ExcelConstants.IM105_DEV_NAME: 
				entity.setDevName(value);
				break;
			case ExcelConstants.IM105_DEV_DESC: 
				entity.setDevDesc(value);
				break;
			case ExcelConstants.IM105_BOARD_CODE: 
				entity.setBoardCode(value);
				break;
			case ExcelConstants.IM105_ALARM_REF_ADDR: 
				entity.setAlarmRefAddr(value);
				break;
			case ExcelConstants.IM105_ALARM_DESC: 
				entity.setAlarmDesc(value);
				break;
			default:
				break;
		}
	}


}
