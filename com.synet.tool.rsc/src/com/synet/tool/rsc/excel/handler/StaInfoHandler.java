package com.synet.tool.rsc.excel.handler;

import org.apache.poi.xssf.usermodel.XSSFComment;

import com.synet.tool.rsc.entity.StaInfo;

public class StaInfoHandler extends RscSheetHandler {

	private StaInfo entity = null;
	
	@Override
	public void startRow(int rowNum) {
		super.startRow(rowNum);
		this.entity = new StaInfo();
	}
	
	@Override
	public void endRow(int rowNum) {
		if (rowNum <= 0) return;
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
		if (currentRow > 0 && !isEmpty(formattedValue)) {
			saveValue(currentCol, formattedValue);
		}
	}
	
	private void saveValue(int col, String value) {
		if (entity == null)
			return;
		switch(col) {
			case 0: 
				if (value != null && !"".equals(value)) {
					entity.setIndex(Integer.valueOf(value.trim()));
					break;
				}
				entity = null;
				break;
			case 1: 
				entity.setDescription(value);
				break;
			case 2:
				entity.setRefAddr(value);
				break;
			default:
				break;
		}
	}

}
