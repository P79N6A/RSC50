package com.synet.tool.rsc.excel.handler;

import org.apache.poi.xssf.usermodel.XSSFComment;

import com.shrcn.found.common.util.StringUtil;
import com.synet.tool.rsc.DBConstants;
import com.synet.tool.rsc.model.Tb1067CtvtsecondaryEntity;
import com.synet.tool.rsc.model.Tb1093VoltagekkEntity;
import com.synet.tool.rsc.service.CtvtsecondaryService;

public class SecProBrkHandler extends RscSheetHandler {

	private Tb1093VoltagekkEntity entity = null;
	private CtvtsecondaryService ctvtsecondaryService = new CtvtsecondaryService();
	
	@Override
	public void startRow(int rowNum) {
		super.startRow(rowNum);
		this.entity = new Tb1093VoltagekkEntity();
	}
	
	@Override
	public void endRow(int rowNum) {
		if (rowNum <= 3) return;
		if (entity == null) {
			String error = "第" + (rowNum + 1) + "行";
			errorMsg.add(error);
		} else {
			entity.setF1093Code(rscp.nextTbCode(DBConstants.PR_VOLTAGEKK));
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
	
	private void saveValue(int col, String value) {
		if (entity == null)
			return;
		switch(col) {
			case 4: 
				if (!StringUtil.isEmpty(value.trim()) && StringUtil.isNumeric(value.trim())) {
					int index = Integer.valueOf(value.trim());
					Tb1067CtvtsecondaryEntity ctv = ctvtsecondaryService.getCtvtsecondaryEntitiesByIndex(index);
					if (ctv != null) {
						entity.setTb1067CtvtsecondaryByF1067Code(ctv);
						break;
					} else {
						console.append("互感器次级[" + value + "]找不到");
					}
				}
				entity = null;
				break;
			case 5: 
				if (!StringUtil.isEmpty(value.trim())) {
					entity.setF1093Desc(value);
					break;
				}
				entity = null;
				break;
			case 6: 
				if (!StringUtil.isEmpty(value.trim())) {
					entity.setF1093KkNo(value);
					break;
				}
				break;
		}
	}

}
