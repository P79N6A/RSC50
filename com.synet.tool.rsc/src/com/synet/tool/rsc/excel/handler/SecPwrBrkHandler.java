package com.synet.tool.rsc.excel.handler;

import java.util.List;

import org.apache.poi.xssf.usermodel.XSSFComment;

import com.shrcn.found.common.util.StringUtil;
import com.synet.tool.rsc.DBConstants;
import com.synet.tool.rsc.model.Tb1046IedEntity;
import com.synet.tool.rsc.model.Tb1092PowerkkEntity;
import com.synet.tool.rsc.util.RscObjectUtils;

public class SecPwrBrkHandler extends RscSheetHandler {

	private Tb1092PowerkkEntity entity = null;
	
	@Override
	public void startRow(int rowNum) {
		super.startRow(rowNum);
		this.entity = RscObjectUtils.createTb1092();
	}
	
	@Override
	public void endRow(int rowNum) {
		if (rowNum <= 3) return;
		if (entity == null) {
			String error = "第" + (rowNum + 1) + "行";
			errorMsg.add(error);
		} else {
			entity.setF1092Code(rscp.nextTbCode(DBConstants.PR_POWERKK));
			result.add(entity);
		}
		super.endRow(rowNum);
	}
	
	@Override
	public void cell(String cellReference, String formattedValue,
			XSSFComment comment) {
		super.cell(cellReference, formattedValue, comment);
		if (currentRow > 3 && !isEmpty(formattedValue)) {
			saveValue(currentCol, formattedValue);
		}
	}
	
	@SuppressWarnings("unchecked")
	private void saveValue(int col, String value) {
		if (entity == null)
			return;
		switch(col) {
			case 1: 
				if (!StringUtil.isEmpty(value.trim())){
					List<Tb1046IedEntity> iedList = (List<Tb1046IedEntity>) service.getListByCriteria(Tb1046IedEntity.class, "f1046Name", value.trim());
					if (iedList != null) {
						entity.setTb1046IedByF1046Code(iedList.get(0));
						break;
					} else {
						console.append("装置[" + value + "]找不到");
					}
				}
				entity = null;
			case 3: 
				if (!StringUtil.isEmpty(value.trim())) {
					entity.setF1092Desc(value);
					break;
				}
				entity = null;
				break;
			case 4: 
				if (!StringUtil.isEmpty(value.trim())) {
					entity.setF1092KkNo(value);
					break;
				}
				break;
			default:
				break;
		}
	}

}
