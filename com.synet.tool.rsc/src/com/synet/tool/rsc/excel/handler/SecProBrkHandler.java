package com.synet.tool.rsc.excel.handler;

import org.apache.poi.xssf.usermodel.XSSFComment;

import com.synet.tool.rsc.model.Tb1067CtvtsecondaryEntity;
import com.synet.tool.rsc.model.Tb1093VoltagekkEntity;

public class SecProBrkHandler extends RscSheetHandler {

	private Tb1093VoltagekkEntity entity = null;
	
	@Override
	public void startRow(int rowNum) {
		super.startRow(rowNum);
		this.entity = new Tb1093VoltagekkEntity();
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
					Tb1093VoltagekkEntity temp = 
							(Tb1093VoltagekkEntity) service.getById(Tb1093VoltagekkEntity.class, value.trim());
					if (temp == null) {
						entity.setF1093Code(value);
						break;
					}
				}
				entity = null;
				break;
			case 1: 
				if (value != null) {
					String id = value.trim();
					Tb1067CtvtsecondaryEntity ctv = (Tb1067CtvtsecondaryEntity) service.getById(Tb1067CtvtsecondaryEntity.class, id);
					if (ctv != null) {
						entity.setTb1067CtvtsecondaryByF1067Code(ctv);
						break;
					}
				}
				entity = null;
				break;
			case 2: 
				if (!entity.getTb1067CtvtsecondaryByF1067Code().getTb1043EquipmentByF1043Code().getF1043Name().equals(value.trim())) {
					entity = null;
				}
				break;
			case 3: 
				if (!entity.getTb1067CtvtsecondaryByF1067Code().getTb1043EquipmentByF1043Code().getF1043Desc().equals(value.trim())) {
					entity = null;
				}
				break;
			case 4: 
				if (!entity.getTb1067CtvtsecondaryByF1067Code().getF1067Code().equals(value.trim())) {
					entity = null;
				}
				break;
			case 5: 
				if (value != null) {
					entity.setF1093Desc(value);
					break;
				}
				entity = null;
				break;
			case 6: 
				if (value != null) {
					entity.setF1093KkNo(value);
					break;
				}
				entity = null;
				break;
		}
	}

}
