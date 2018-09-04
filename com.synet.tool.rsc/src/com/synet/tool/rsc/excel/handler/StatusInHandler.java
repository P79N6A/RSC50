package com.synet.tool.rsc.excel.handler;

import java.util.Map;

import org.apache.poi.xssf.usermodel.XSSFComment;

import com.synet.tool.rsc.DBConstants;
import com.synet.tool.rsc.ExcelConstants;
import com.synet.tool.rsc.model.IM104StatusInEntity;

public class StatusInHandler extends RscSheetHandler {

	private IM104StatusInEntity entity;
	
	public StatusInHandler(int headRowNum, Map<Integer, String> excelColInfo) {
		super(headRowNum, excelColInfo);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void startRow(int rowNum) {
		super.startRow(rowNum);
		this.entity = new IM104StatusInEntity();
		this.entity.setIm104Code(rscp.nextTbCode(DBConstants.PR_STATUSIN));
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
			case ExcelConstants.IM104_DEV_NAME: 
				entity.setDevName(value);
				break;
			case ExcelConstants.IM104_PIN_REF_ADDR: 
				entity.setPinRefAddr(value);
				break;
			case ExcelConstants.IM104_PIN_DESC: 
				entity.setPinDesc(value);
				break;
			case ExcelConstants.IM104_MMS_SIGNAL: 
				entity.setMmsRefAddr(value);
				break;
			case ExcelConstants.IM104_SIGNAL_DESC: 
				entity.setMmsDesc(value);
				break;
			default:
				break;
		}
	}


}
