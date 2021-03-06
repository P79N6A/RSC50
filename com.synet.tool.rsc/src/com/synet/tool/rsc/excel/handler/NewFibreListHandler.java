package com.synet.tool.rsc.excel.handler;

import java.util.Map;

import org.apache.poi.xssf.usermodel.XSSFComment;

import com.synet.tool.rsc.DBConstants;
import com.synet.tool.rsc.ExcelConstants;
import com.synet.tool.rsc.model.IM102FibreListEntity;

public class NewFibreListHandler extends RscSheetHandler {

	public NewFibreListHandler(int headRowNum, Map<Integer, String> excelColInfo) {
		super(headRowNum, excelColInfo);
	}

	private IM102FibreListEntity entity = null;
	
	@Override
	public void startRow(int rowNum) {
		super.startRow(rowNum);
		this.entity = new IM102FibreListEntity();
//		this.entity.setIm102Code(rscp.nextTbCode(DBConstants.PR_FIBRELIST));
	}
	
	@Override
	public void endRow(int rowNum) {
		if (rowNum <= headRowNum) return;
		if (entity == null) {
			String error = "第" + (rowNum + 1) + "行";
			errorMsg.add(error);
		} else {
			if (entity.getBoardCodeA() != null && entity.getPortCodeA() != null) {
				entity.setIm102Code(rscp.nextTbCode(DBConstants.PR_FIBRELIST));
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
		if (entity == null)
			return;
		String fieldName = excelColInfo.get(col);
		if (fieldName == null) return;
		switch(fieldName) {
			case ExcelConstants.IM102_CABLE_CODE: //只处理光缆部分，不处理跳缆部分
				if (value == null || "".equals(value)) {
					entity = null;
					break;
				}
				entity.setCableCode(value);
				break;
			case ExcelConstants.IM102_CORE_CODE: 
				entity.setCoreCode(value);
				entity.setCoreCodeA(value);
				entity.setCoreCodeB(value);
				break;
			case ExcelConstants.IM102_DEV_CODEA: 
				entity.setDevCodeA(value);
				break;
			case ExcelConstants.IM102_DEV_NAMEA: 
				entity.setDevNameA(value);
				break;
			case ExcelConstants.IM102_DEV_DESCA: 
				entity.setDevDescA(value);
				break;
			case ExcelConstants.IM102_BOARD_CODEA: 
				entity.setBoardCodeA(value);
				break;
			case ExcelConstants.IM102_PORT_CODEA: 
				entity.setPortCodeA(value);
				break;
			case ExcelConstants.IM102_CUBICLE_CODEA: 
				entity.setCubicleCodeA(value);
				break;
			case ExcelConstants.IM102_CUBICLE_DESCA: 
				entity.setCubicleDescA(value);
				break;
			case ExcelConstants.IM102_CORE_CODEA: 
				entity.setCoreCodeA(value);
				break;
			case ExcelConstants.IM102_DISTRIBFRAME_CODEA: 
				entity.setDistribFrameCodeA(value);
				break;
			case ExcelConstants.IM102_DISTRIBFRAME_PORT_NOA:
				entity.setDistribFramePortNoA(value);
				break;
			case ExcelConstants.IM102_DEV_CODEB: 
				entity.setDevCodeB(value);
				break;
			case ExcelConstants.IM102_DEV_NAMEB: 
				entity.setDevNameB(value);
				break;
			case ExcelConstants.IM102_DEV_DESCB: 
				entity.setDevDescB(value);
				break;
			case ExcelConstants.IM102_BOARD_CODEB: 
				entity.setBoardCodeB(value);
				break;
			case ExcelConstants.IM102_PORT_CODEB: 
				entity.setPortCodeB(value);
				break;
			case ExcelConstants.IM102_CUBICLE_CODEB: 
				entity.setCubicleCodeB(value);
				break;
			case ExcelConstants.IM102_CUBICLE_DESCB: 
				entity.setCubicleDescB(value);
				break;
			case ExcelConstants.IM102_CORE_CODEB: 
				entity.setCoreCodeB(value);
				break;
			case ExcelConstants.IM102_DISTRIBFRAME_CODEB: 
				entity.setDistribFrameCodeB(value);
				break;
			case ExcelConstants.IM102_DISTRIBFRAME_PORT_NOB:
				entity.setDistribFramePortNoB(value);
				break;
			default:
				break;
		}
	}

}
