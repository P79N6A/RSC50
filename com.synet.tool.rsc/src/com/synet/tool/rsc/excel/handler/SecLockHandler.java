package com.synet.tool.rsc.excel.handler;

import java.util.List;

import org.apache.poi.xssf.usermodel.XSSFComment;

import com.synet.tool.rsc.model.Tb1046IedEntity;
import com.synet.tool.rsc.model.Tb1091IotermEntity;
import com.synet.tool.rsc.util.RscObjectUtils;

public class SecLockHandler extends RscSheetHandler {

	private Tb1091IotermEntity entity = null;

	@Override
	public void startRow(int rowNum) {
		super.startRow(rowNum);
		this.entity = RscObjectUtils.createTb1091();
	}
	
	@Override
	public void endRow(int rowNum) {
		if (rowNum <= 5) return;
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
		if (currentRow > 5 && !isEmpty(formattedValue)) {
			saveValue(currentCol, formattedValue);
		}
	}
	
	@SuppressWarnings("unchecked")
	private void saveValue(int col, String value) {
		if (entity == null)
			return;
		switch(col) {
			case 1: 
				if (value != null){
					List<Tb1046IedEntity> iedList = (List<Tb1046IedEntity>) service.getListByCriteria(Tb1046IedEntity.class, "f1046Name", value.trim());
					if (iedList != null) {
						entity.setTb1046IedByF1046Code(iedList.get(0));
						console.append("装置[" + value + "]找不到");
						break;
					}
				}
				entity = null;
			case 3: 
				if (value != null) {
					entity.setF1091Desc(value);
					break;
				}
				entity = null;
				break;
			case 4: 
				if (value != null) {
					entity.setF1091TermNo(value);
					break;
				}
				entity = null;
				break;
			case 5: 
				if (value != null) {
					entity.setF1091CircNo(value);
					break;
				}
				entity = null;
				break;
		}
	}

}
