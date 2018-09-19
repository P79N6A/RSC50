package com.synet.tool.rsc.excel.handler;

import java.util.Map;

import org.apache.poi.xssf.usermodel.XSSFComment;

import com.synet.tool.rsc.DBConstants;
import com.synet.tool.rsc.ExcelConstants;
import com.synet.tool.rsc.model.IM108BrkCfmEntity;

public class BrkCfmHandler extends RscSheetHandler {

	private IM108BrkCfmEntity entity;
	
	public BrkCfmHandler(int headRowNum, Map<Integer, String> excelColInfo) {
		super(headRowNum, excelColInfo);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void startRow(int rowNum) {
		super.startRow(rowNum);
		this.entity = new IM108BrkCfmEntity();
		this.entity.setIm108Code(rscp.nextTbCode(DBConstants.PR_BRKCFM));
	}
	
	@Override
	public void endRow(int rowNum) {
		if (rowNum <= headRowNum) return;
		if (entity == null) {
			String error = "第" + (rowNum + 1) + "行";
			errorMsg.add(error);
		} else {
			if (entity.getDevName() != null && entity.getCmdAckVpRefAddr() != null 
					&& entity.getCmdOutVpRefAddr() != null) {
				entity.setMatched(DBConstants.MATCHED_NO);
				result.add(entity);
			}
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
			case ExcelConstants.IM108_DEV_NAME:
				entity.setDevName(value);
				break;
			case ExcelConstants.IM108_DEV_DESC:
				entity.setDevDesc(value);
				break;
			case ExcelConstants.IM108_PIN_REF_ADDR:
				entity.setPinRefAddr(value);
				break;
			case ExcelConstants.IM108_PIN_DESC: 
				entity.setPinDesc(value);
				break;
			case ExcelConstants.IM108_CMDACKVP_REF_ADDR: 
				entity.setCmdAckVpRefAddr(value);
				break;
			case ExcelConstants.IM108_CMDACKVP_DESC: 
				entity.setCmdAckVpDesc(value);
				break;
			case ExcelConstants.IM108_CMDOUTVP_REF_ADDR:
				entity.setCmdOutVpRefAddr(value);
				break;
			case ExcelConstants.IM108_CMDOUTVP_DESC: 
				entity.setCmdOutVpDesc(value);
				break;
			default:
				break;
		}
	}


}
