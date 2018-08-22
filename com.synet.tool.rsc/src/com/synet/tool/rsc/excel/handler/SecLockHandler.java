package com.synet.tool.rsc.excel.handler;

import org.apache.poi.xssf.usermodel.XSSFComment;

import com.synet.tool.rsc.model.Tb1046IedEntity;
import com.synet.tool.rsc.model.Tb1091IotermEntity;

public class SecLockHandler extends RscSheetHandler {

	private Tb1091IotermEntity entity = null;
	
	@Override
	public void startRow(int rowNum) {
		super.startRow(rowNum);
		this.entity = new Tb1091IotermEntity();
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
				if (value != null) {
					Tb1091IotermEntity temp = 
							(Tb1091IotermEntity) service.getById(Tb1091IotermEntity.class, value.trim());
					if (temp == null) {
						entity.setF1091Code(value);
						break;
					}
				}
				entity = null;
				break;
			case 1: 
				if (value != null) {
					String id = value.trim();
					Tb1046IedEntity ied = (Tb1046IedEntity) service.getById(Tb1046IedEntity.class, id);
					if (ied != null) {
						entity.setTb1046IedByF1046Code(ied);
						break;
					}
				}
				entity = null;
				break;
			case 2: 
				if (!entity.getTb1046IedByF1046Code().getF1046Name().equals(value.trim())) {
					entity = null;
				}
				break;
			case 3: 
				if (!entity.getTb1046IedByF1046Code().getF1046Desc().equals(value.trim())) {
					entity = null;
				}
				break;
			case 4: 
				if (value != null) {
					entity.setF1091Desc(value);
					break;
				}
				entity = null;
				break;
			case 5: 
				if (value != null) {
					entity.setF1091TermNo(value);
					break;
				}
				entity = null;
				break;
			case 6: 
				if (value != null) {
					entity.setF1091CircNo(value);
					break;
				}
				entity = null;
				break;
		}
	}

}
