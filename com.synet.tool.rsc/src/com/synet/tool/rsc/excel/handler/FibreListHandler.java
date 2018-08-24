package com.synet.tool.rsc.excel.handler;

import org.apache.poi.xssf.usermodel.XSSFComment;

import com.synet.tool.rsc.entity.FibreList;
import com.synet.tool.rsc.model.Tb1046IedEntity;
import com.synet.tool.rsc.model.Tb1090LineprotfiberEntity;

public class FibreListHandler extends RscSheetHandler {

	private FibreList entity = null;
	
	@Override
	public void startRow(int rowNum) {
		super.startRow(rowNum);
		this.entity = new FibreList();
	}
	
	@Override
	public void endRow(int rowNum) {
		if (rowNum <= 2) return;
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
		if (currentRow > 2 && !isEmpty(formattedValue)) {
			saveValue(currentCol, formattedValue);
		}
	}
	
	private void saveValue(int col, String value) {
		if (entity == null)
			return;
		switch(col) {
			case 0: 
				if (value != null) {
					String[] strs = null;
					if (value.contains("/")) {
						 strs = value.split("/");
					} else if (value.contains("-")) {
						strs = value.split("-");
					}
					if (strs != null && strs.length >= 2) {
						entity.setBoardCodeA(strs[0]);
						entity.setPortCodeA(strs[1]);
					}
					break;
				}
				entity = null;
				break;
			case 1: 
				entity.setDevCodeA(value);
				break;
			case 2: 
				entity.setDevNameA(value);
				break;
			case 3: 
				entity.setDevDescA(value);
				break;
			case 4: 
				entity.setCubicleCodeA(value);
				break;
			case 5: 
				entity.setCubicleDescA(value);
				break;
			case 7: 
				entity.setDistribFrameCodeA(value);
				entity.setDistribFrameCodeB(value);
				break;
			case 10:
				entity.setCoreCode(value);
				entity.setCoreCodeA(value);
				entity.setCoreCodeB(value);
				break;
			case 11:
				if (value != null) {
					if(value.contains(" ")) {
						String[] strs = value.split(" ");
						if (strs != null && strs.length >= 2) {
							entity.setCableCode(strs[1]);
						}
					}
					break;
				}
				entity = null;
				break;
			case 15:
				entity.setDevCodeB(value);
				break;
			case 16: 
				entity.setDevNameB(value);
				break;
			case 17: 
				entity.setDevDescB(value);
				break;
			case 18: 
				entity.setCubicleCodeB(value);
				break;
			case 19: 
				entity.setCubicleDescB(value);
				break;
			case 20:
				if (value != null) {
					String[] strs = null;
					if (value.contains("/")) {
						 strs = value.split("/");
					} else if (value.contains("-")) {
						strs = value.split("-");
					}
					if (strs != null && strs.length >= 2) {
						entity.setBoardCodeB(strs[0]);
						entity.setPortCodeB(strs[1]);
					}
					break;
				}
				entity = null;
				break;
			default:
				break;
		}
	}

}
