package com.synet.tool.rsc.excel.handler;

import java.util.Map;

import org.apache.poi.xssf.usermodel.XSSFComment;

import com.synet.tool.rsc.DBConstants;
import com.synet.tool.rsc.ExcelConstants;
import com.synet.tool.rsc.model.IM107TerStrapEntity;

public class TerStrapHandler extends RscSheetHandler {

	private IM107TerStrapEntity entity;
	
	public TerStrapHandler(int headRowNum, Map<Integer, String> excelColInfo) {
		super(headRowNum, excelColInfo);
	}
	
	@Override
	public void startRow(int rowNum) {
		super.startRow(rowNum);
		this.entity = new IM107TerStrapEntity();
		this.entity.setIm107Code(rscp.nextTbCode(DBConstants.PR_TERSTRAP));
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
			case ExcelConstants.IM107_DEV_NAME: 
				entity.setDevName(value);
				break;
			case ExcelConstants.IM107_STRAP_REF_ADDR: 
				entity.setStrapRefAddr(value);
				break;
			case ExcelConstants.IM107_STRAP_DESC: 
				entity.setStrapDesc(value);
				break;
			case ExcelConstants.IM107_STRAP_TYPE: 
				entity.setStrapType(value);
				break;
			case ExcelConstants.IM107_VP_REF_ADDR: 
				entity.setVpRefAddr(value);
				break;
			case ExcelConstants.IM107_VP_DESC: 
				entity.setVpDesc(value);
				break;
			case ExcelConstants.IM107_VP_TYPE: 
				entity.setVpType(value);
				break;
			default:
				break;
		}
	}


}
