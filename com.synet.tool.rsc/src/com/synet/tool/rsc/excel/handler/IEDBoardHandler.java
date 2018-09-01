package com.synet.tool.rsc.excel.handler;

import org.apache.poi.xssf.usermodel.XSSFComment;

import com.synet.tool.rsc.model.IEDBoardEntity;

public class IEDBoardHandler extends RscSheetHandler {

	private IEDBoardEntity entity = null;
	
	@Override
	public void startRow(int rowNum) {
		super.startRow(rowNum);
		this.entity = new IEDBoardEntity();
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
			case 9: 
				entity.setDevName(value);
				break;
			case 5: 
				entity.setDevDesc(value);
				break;
			case 32:
				entity.setBoardCode(value);
				break;
			case 35:
				if (value != null && value.contains("端口")) {
					int index = value.indexOf(":");
					if (index == -1) {
						index = value.indexOf("：");
					}
					if (index != -1) {
						String newValue = value.substring(index + 1);
						entity.setPortCode(newValue);
					}
					
				}
			default:
				break;
		}
	}

}
