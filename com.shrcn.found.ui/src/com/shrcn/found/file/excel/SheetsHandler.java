package com.shrcn.found.file.excel;

import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.eventusermodel.XSSFSheetXMLHandler.SheetContentsHandler;
import org.apache.poi.xssf.usermodel.XSSFComment;

public abstract class SheetsHandler implements SheetContentsHandler {

	protected int currentRow = -1;
	protected int currentCol = -1;
	protected List<Object> result = new ArrayList<>();

	@Override
	public void startRow(int rowNum) {
        currentRow = rowNum;  
        currentCol = -1;
	}

	@Override
	public void endRow(int rowNum) {
	}
	
	@Override
	public void cell(String cellReference, String formattedValue,  
            XSSFComment comment) {
		int thisCol = (new CellReference(cellReference)).getCol();
		currentCol = thisCol;
	}
	
	@Override
	public void headerFooter(String text, boolean isHeader, String tagName) {
	}

	protected boolean isEmpty(String v) {
		return v == null || v.trim().length() < 1;
	}

	public List<?> getResult() {
		return result;
	}

}
