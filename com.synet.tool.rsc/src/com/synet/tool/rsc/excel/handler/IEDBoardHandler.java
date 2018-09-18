package com.synet.tool.rsc.excel.handler;

import java.util.Map;

import org.apache.poi.xssf.usermodel.XSSFComment;

import com.shrcn.found.common.util.StringUtil;
import com.synet.tool.rsc.DBConstants;
import com.synet.tool.rsc.ExcelConstants;
import com.synet.tool.rsc.model.IM103IEDBoardEntity;

public class IEDBoardHandler extends RscSheetHandler {
	
	private String devName;
	private String manufacturor;
	private String configVersion;
	private IM103IEDBoardEntity entity = null;
	
	public IEDBoardHandler(int headRowNum, Map<Integer, String> excelColInfo) {
		super(headRowNum, excelColInfo);
	}
	
	@Override
	public void startRow(int rowNum) {
		super.startRow(rowNum);
		this.entity = new IM103IEDBoardEntity();
		this.entity.setIm103Code(rscp.nextTbCode(DBConstants.PR_IEDBOARD));
	}
	
	@Override
	public void endRow(int rowNum) {
		if (rowNum <= headRowNum) return;
		if (entity == null) {
			String error = "第" + (rowNum + 1) + "行";
			errorMsg.add(error);
		} else {
			if (entity.getBoardIndex() != null && entity.getBoardCode() != null) { 
				entity.setMatched(DBConstants.MATCHED_NO);
				entity.setDevName(devName);
				entity.setManufacturor(manufacturor);
				entity.setConfigVersion(configVersion);
				result.add(entity);
			}
		}
		super.endRow(rowNum);
	}
	
	@Override
	public void cell(String cellReference, String formattedValue,
			XSSFComment comment) {
		super.cell(cellReference, formattedValue, comment);
		if (currentRow > headRowNum) {
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
			case ExcelConstants.IM103_DEV_TYPE:
				if (!StringUtil.isEmpty(value)) {
					devName = value;
				}
				break;
			case ExcelConstants.IM103_MANUFACTUROR:
				if (!StringUtil.isEmpty(value)) {
					manufacturor = value;
				}
				break;
			case ExcelConstants.IM103_CONFIG_VERSION:
				if (!StringUtil.isEmpty(value)) {
					configVersion = value;
				}
				break;
			case ExcelConstants.IM103_BOARD_CODE:
				entity.setBoardCode(value);
				break;
			case ExcelConstants.IM103_BOARD_INDEX:
				entity.setBoardIndex(value);
				break;
			case ExcelConstants.IM103_BOARD_MODEL:
				entity.setBoardModel(value);
				break;
			case ExcelConstants.IM103_BOARD_TYPE:
				entity.setBoardType(value);
				break;
			case ExcelConstants.IM103_PORT_NUM:
				entity.setPortNum(value);
			default:
				break;
		}
	}

}
