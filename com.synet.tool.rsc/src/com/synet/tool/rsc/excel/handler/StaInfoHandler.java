package com.synet.tool.rsc.excel.handler;

import java.util.Map;

import org.apache.poi.xssf.usermodel.XSSFComment;

import com.synet.tool.rsc.DBConstants;
import com.synet.tool.rsc.ExcelConstants;
import com.synet.tool.rsc.model.IM109StaInfoEntity;

public class StaInfoHandler extends RscSheetHandler {
	
	private IM109StaInfoEntity entity = null;
	
	public StaInfoHandler(int headRowNum, Map<Integer, String> excelColInfo) {
		super(headRowNum, excelColInfo);
	}

	@Override
	public void startRow(int rowNum) {
		super.startRow(rowNum);
		this.entity = new IM109StaInfoEntity();
		this.entity.setMatched(DBConstants.MATCHED_NO);
	}
	
	@Override
	public void endRow(int rowNum) {
		if (rowNum <= headRowNum) return;
		if (entity == null) {
			String error = "第" + (rowNum + 1) + "行";
			errorMsg.add(error);
		} else {
			if (entity.getDevName() != null && entity.getMmsRefAddr() != null) {
				entity.setIm109Code(rscp.nextTbCode(DBConstants.PR_STAINFO));
				result.add(entity);
			}
		}
		super.endRow(rowNum);
	}
	
	@Override
	public void cell(String cellReference, String formattedValue,
			XSSFComment comment) {
		super.cell(cellReference, formattedValue, comment);
		if (currentRow > 0 && !isEmpty(formattedValue)) {
			saveValue(currentCol, formattedValue);
		}
	}
	
	private void saveValue(int col, String value) {
		if (entity == null)
			return;
		String fieldName = excelColInfo.get(col);
		if (fieldName == null) return;
		switch(fieldName) {
		case ExcelConstants.IM109_DEV_NAME: 
			entity.setDevName(value);
			break;
		case ExcelConstants.IM109_DEV_DESC: 
			entity.setDevDesc(value);
			break;
		case ExcelConstants.IM109_DESCRIPTION: 
			entity.setMmsDesc(value);
			break;
		case ExcelConstants.IM109_REF_ADDR: 
			entity.setMmsRefAddr(value);
			break;
		default:
			break;
		}
	}

}
