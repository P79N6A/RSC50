package com.synet.tool.rsc.excel.handler;

import java.util.ArrayList;

import org.apache.poi.xssf.usermodel.XSSFComment;

import com.shrcn.found.common.dict.DictManager;
import com.shrcn.found.common.util.StringUtil;
import com.synet.tool.rsc.model.Tb1049RegionEntity;
import com.synet.tool.rsc.model.Tb1050CubicleEntity;
import com.synet.tool.rsc.util.RscObjectUtils;

public class RegionHandler extends RscSheetHandler {

	private Tb1049RegionEntity regionEntity;
	private Tb1050CubicleEntity cubicleEntity;
	private String region;
	
	public RegionHandler() {
		this.errorMsg = new ArrayList<>();
	}
	
	@Override
	public void startRow(int rowNum) {
		super.startRow(rowNum);
		if (rowNum > 0) {
			this.cubicleEntity = RscObjectUtils.createCubicle();
		}
	}
	
	@Override
	public void endRow(int rowNum) {
		if (rowNum < 1) {
			return;
		}
		if (cubicleEntity == null) {
			String error = "第" + (rowNum + 1) + "行";
			errorMsg.add(error);
		} else {
			regionEntity.getTb1050CubiclesByF1049Code().add(cubicleEntity);
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
	
	@SuppressWarnings("unchecked")
	private void saveValue(int col, String value) {
		switch(col) {
			case 0: 
				if (region == null || !value.equals(region)) {
					regionEntity = RscObjectUtils.createRegion();
					regionEntity.setF1049Name(value);
					regionEntity.setF1049Desc(value);
					result.add(regionEntity);
				}
				break;
			case 1:
				String area = DictManager.getInstance().getIdByName("AREA_TYPE", value);
				if (!StringUtil.isEmpty(area)) {
					regionEntity.setF1049Area(Integer.parseInt(area));
				} else {
					regionEntity.setF1049Area(0);
				}
				break;
			case 2:
				cubicleEntity.setTb1049RegionByF1049Code(regionEntity);
				cubicleEntity.setF1050Name(value);
				cubicleEntity.setF1050Desc(value);
				break;
			default:
				break;
		}
	}

}
