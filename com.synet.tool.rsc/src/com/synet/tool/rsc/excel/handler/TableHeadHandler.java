package com.synet.tool.rsc.excel.handler;

import java.util.HashMap;
import java.util.Map;

import org.apache.poi.xssf.usermodel.XSSFComment;

public class TableHeadHandler extends RscSheetHandler {

	private int headRowNum;
	private Map<Integer, String> entity = null;
	
	public TableHeadHandler(int headRowNum) {
		super();
		this.headRowNum = headRowNum;
	}

	@Override
	public void startRow(int rowNum) {
		super.startRow(rowNum);
		this.entity = new HashMap<Integer, String>();
	}
	
	@Override
	public void endRow(int rowNum) {
		if (rowNum == headRowNum) {
			if (entity == null) {
				String error = "表头解析异常";
				errorMsg.add(error);
			} else {
				result.add(entity);
			}
		}
		super.endRow(rowNum);
	}
	
	@Override
	public void cell(String cellReference, String formattedValue,
			XSSFComment comment) {
		super.cell(cellReference, formattedValue, comment);
		if (currentRow < 4){
			System.out.println(currentRow + "-" + currentCol + "-" + formattedValue);
		}
		if (currentRow <= headRowNum ) {
			saveValue(currentCol, formattedValue);
		}
	}
	
	private void saveValue(int col, String value) {
		if (entity == null)
			return;
		if (value != null && !"".equals(value)) {
			entity.put(col, value.trim());
		} else {
			entity.put(col, "未知列名");
		}
	}

}
