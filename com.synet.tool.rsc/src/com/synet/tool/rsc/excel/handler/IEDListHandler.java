package com.synet.tool.rsc.excel.handler;

import java.util.Map;

import org.apache.poi.xssf.usermodel.XSSFComment;

import com.synet.tool.rsc.DBConstants;
import com.synet.tool.rsc.ExcelConstants;
import com.synet.tool.rsc.model.IM101IEDListEntity;

public class IEDListHandler extends RscSheetHandler {

	private IM101IEDListEntity entity;
	
	public IEDListHandler(int headRowNum, Map<Integer, String> excelColInfo) {
		super(headRowNum, excelColInfo);
	}
	
	@Override
	public void startRow(int rowNum) {
		super.startRow(rowNum);
		this.entity = new IM101IEDListEntity();
		this.entity.setIm101Code(rscp.nextTbCode(DBConstants.PR_IEDLIST));
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
			case ExcelConstants.IM101_DEV_NAME: 
				entity.setDevName(value);
				break;
			case ExcelConstants.IM101_DEV_DESC: 
				entity.setDevDesc(value);
				break;
			case ExcelConstants.IM101_BAY: 
				entity.setBay(value);
				break;
			case ExcelConstants.IM101_CUBICLE: 
				entity.setCubicle(value);
				break;
//			case ExcelConstants.IM101_MANUFACTUROR: 
//				entity.setManufacturor(value);
//				break;
//			case ExcelConstants.IM101_DEV_TYPE: 
//				entity.setDevType(value);
//				break;
//			case ExcelConstants.IM101_DEV_VERSION: 
//				entity.setDevVersion(value);
//				break;
//			case ExcelConstants.IM101_A_OR_B: 
//				entity.setAorB(value);;
//				break;
//			case ExcelConstants.IM101_PROT_CLASSIFY: 
//				entity.setProtClassify(value);
//				break;
//			case ExcelConstants.IM101_PROT_MODEL: 
//				entity.setProtModel(value);
//				break;
//			case ExcelConstants.IM101_PROT_TYPE: 
//				entity.setProtType(value);
//				break;
			case ExcelConstants.IM101_DATE_SERVICE: 
				entity.setDateService(value);
				break;
			case ExcelConstants.IM101_DATE_PRODUCT: 
				entity.setDateProduct(value);
				break;
			case ExcelConstants.IM101_PRODUCT_CODE: 
				entity.setProductCode(value);
				break;
			case ExcelConstants.IM101_DATA_COLLECT_TYPE: 
				entity.setDataCollectType(value);
				break;
			case ExcelConstants.IM101_OUT_TYPE: 
				entity.setOutType(value);
				break;
//			case ExcelConstants.IM101_BOARD_NUM: 
//				entity.setBoardNum(value);
//				break;
			default:
				break;
		}
	}


}
