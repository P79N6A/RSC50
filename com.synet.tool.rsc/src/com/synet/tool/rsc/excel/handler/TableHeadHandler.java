package com.synet.tool.rsc.excel.handler;

import java.util.HashMap;
import java.util.Map;

import org.apache.poi.xssf.usermodel.XSSFComment;

public class TableHeadHandler extends RscSheetHandler {

	private int headRowNum;
	private Map<Integer, String> entity = null;
	private Map<Integer, String> tempMap;
	private int maxCol = 0;//表头行最大列
	
	public TableHeadHandler(int headRowNum) {
		super();
		this.headRowNum = headRowNum;
		this.tempMap = new HashMap<>();
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
				//处理为读到的合并行的数据（往前读取）
				for (int i = 0; i <= (maxCol + 1); i++) {
					if (!entity.containsKey(i)) {
						if (tempMap.containsKey(i)) {
							entity.put(i, tempMap.get(i));
						}
					}
				}
			}
		}
		super.endRow(rowNum);
	}
	
	@Override
	public void cell(String cellReference, String formattedValue,
			XSSFComment comment) {
		super.cell(cellReference, formattedValue, comment);
		if (currentRow <= headRowNum ) {
			System.out.println(currentRow + ":" +currentCol + "-" + formattedValue);
			saveValue(currentCol, formattedValue);
		}
	}
	
	private void saveValue(int col, String value) {
		if (entity == null)
			return;
		maxCol = col;
		if (currentRow < headRowNum){
			tempMap.put(col, value);
		} else if (currentRow == headRowNum) {
			if (value != null && !"".equals(value)) {
				entity.put(col, value.trim());
			} else {
				value = tempMap.get(col);
				if (value != null) {
					entity.put(col,value);
				} else {
					entity.put(col,"");
				}
			}
		}
	}

}
