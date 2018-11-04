package com.synet.tool.rsc.excel.handler;

import java.util.Map;

import org.apache.poi.xssf.usermodel.XSSFComment;

import com.synet.tool.rsc.DBConstants;
import com.synet.tool.rsc.ExcelConstants;
import com.synet.tool.rsc.model.IM110LinkWarnEntity;

public class LinkWarnHandler extends RscSheetHandler {
	
	private IM110LinkWarnEntity entity = null;
	
	public LinkWarnHandler(int headRowNum, Map<Integer, String> excelColInfo) {
		super(headRowNum, excelColInfo);
	}

	@Override
	public void startRow(int rowNum) {
		super.startRow(rowNum);
		this.entity = new IM110LinkWarnEntity();
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
				entity.setIm110Code(rscp.nextTbCode(DBConstants.PR_LINKW));
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
		case ExcelConstants.IM110_DEV_NAME: 
			entity.setDevName(value);
			break;
		case ExcelConstants.IM110_DEV_DESC: 
			entity.setDevDesc(value);
			break;
		case ExcelConstants.IM110_DESCRIPTION: 
			entity.setMmsDesc(value);
			break;
		case ExcelConstants.IM110_REF_ADDR: 
			entity.setMmsRefAddr(value);
			break;
		case ExcelConstants.IM110_SEND_IED: 
			entity.setSendDevName(value);
			break;
		case ExcelConstants.IM110_CBREF: 
			entity.setCbRef(value);
			break;
		default:
			break;
		}
	}

}
