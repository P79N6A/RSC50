/*
 * Copyright 2003-2005 ExcelUtils http://excelutils.sourceforge.net
 * Created on 2005-6-18
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.sf.excelutils2007;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * <p>
 * <b>WorkbookUtils </b>is a helper of Microsoft Excel,it's based on POI project
 * </p>
 * 
 * @author rainsoft
 * @version $Revision: 1.1 $ $Date: 2011/07/19 06:15:22 $
 */
public class WorkbookUtils {

	public WorkbookUtils() {
	}

	/**
	 * Open an excel file by real fileName
	 * @param fileName
	 * @return XSSFWorkbook
	 * @throws ExcelException
	 */
	public static XSSFWorkbook openWorkbook(String fileName) throws ExcelException {
		InputStream in = null;
		XSSFWorkbook wb = null;
		try {
			in = new FileInputStream(fileName);
			wb = new XSSFWorkbook(in);
		} catch (Exception e) {
			throw new ExcelException("File" + fileName + "not found" + e.getMessage());
		} finally {
			try {
				in.close();
			} catch (Exception e) {				
			}
		}
		return wb;
	}
	
	/**
	 * Open an excel from InputStream
	 * @param in
	 * @return　XSSFWorkbook
	 * @throws ExcelException
	 */
	public static XSSFWorkbook openWorkbook(InputStream in) throws ExcelException {
		XSSFWorkbook wb = null;
		try {
			wb = new XSSFWorkbook(in);
		} catch (Exception e) {
			throw new ExcelException(e.getMessage());
		}
		return wb;
	}

	/**
	 * Save the Excel to OutputStream
	 * 
	 * @param wb XSSFWorkbook
	 * @param out OutputStream
	 * @throws ExcelException
	 */
	public static void SaveWorkbook(XSSFWorkbook wb, OutputStream out) throws ExcelException {
		try {
			wb.write(out);
		} catch (Exception e) {
			throw new ExcelException(e.getMessage());
		}
	}

	/**
	 * Set value of the cell
	 * 
	 * @param sheet XSSFSheet
	 * @param rowNum int
	 * @param colNum int
	 * @param value String
	 */
	public static void setCellValue(XSSFSheet sheet, int rowNum, int colNum, String value) {
		XSSFRow row = getRow(rowNum, sheet);
		XSSFCell cell = getCell(row, colNum);
//		cell.setEncoding(XSSFWorkbook.ENCODING_UTF_16);
		cell.setCellValue(value);
	}

	/**
	 * get value of the cell
	 * 
	 * @param sheet XSSFSheet
	 * @param rowNum int
	 * @param colNum int
	 * @return String
	 */
	public static String getStringCellValue(XSSFSheet sheet, int rowNum, int colNum) {
		XSSFRow row = getRow(rowNum, sheet);
		XSSFCell cell = getCell(row, colNum);
		return cell.getStringCellValue();
	}

	/**
	 * set value of the cell
	 * 
	 * @param sheet XSSFSheet
	 * @param rowNum int
	 * @param colNum int
	 * @param value String
	 * @param encoding short
	 */
	public static void setCellValue(XSSFSheet sheet, int rowNum, int colNum, String value, short encoding) {
		XSSFRow row = getRow(rowNum, sheet);
		XSSFCell cell = getCell(row, colNum);
		if (encoding >= 0) {
//			cell.setEncoding(encoding);
		}
		cell.setCellValue(value);
	}

	/**
	 * set value of the cell
	 * 
	 * @param sheet XSSFSheet
	 * @param rowNum int
	 * @param colNum int
	 * @param value double
	 */
	public static void setCellValue(XSSFSheet sheet, int rowNum, int colNum, double value) {
		XSSFRow row = getRow(rowNum, sheet);
		XSSFCell cell = getCell(row, colNum);
		cell.setCellValue(value);
	}

	/**
	 * get value of the cell
	 * 
	 * @param sheet XSSFSheet
	 * @param rowNum int
	 * @param colNum int
	 * @return double
	 */
	public static double getNumericCellValue(XSSFSheet sheet, int rowNum, int colNum) {
		XSSFRow row = getRow(rowNum, sheet);
		XSSFCell cell = getCell(row, colNum);
		return cell.getNumericCellValue();
	}

	/**
	 * set value of the cell
	 * 
	 * @param sheet XSSFSheet
	 * @param rowNum int
	 * @param colNum int
	 * @param value Date
	 */
	public static void setCellValue(XSSFSheet sheet, int rowNum, int colNum, Date value) {
		XSSFRow row = getRow(rowNum, sheet);
		XSSFCell cell = getCell(row, colNum);
		cell.setCellValue(value);
	}

	/**
	 * get value of the cell
	 * 
	 * @param sheet XSSFSheet
	 * @param rowNum int
	 * @param colNum int
	 * @return Date
	 */
	public static Date getDateCellValue(XSSFSheet sheet, int rowNum, int colNum) {
		XSSFRow row = getRow(rowNum, sheet);
		XSSFCell cell = getCell(row, colNum);
		return cell.getDateCellValue();
	}

	/**
	 * set value of the cell
	 * 
	 * @param sheet XSSFSheet
	 * @param rowNum int
	 * @param colNum int
	 * @param value boolean
	 */
	public static void setCellValue(XSSFSheet sheet, int rowNum, int colNum, boolean value) {
		XSSFRow row = getRow(rowNum, sheet);
		XSSFCell cell = getCell(row, colNum);
		cell.setCellValue(value);
	}

	/**
	 * get value of the cell
	 * 
	 * @param sheet
	 * @param rowNum
	 * @param colNum
	 * @return boolean value
	 */
	public static boolean getBooleanCellValue(XSSFSheet sheet, int rowNum, int colNum) {
		XSSFRow row = getRow(rowNum, sheet);
		XSSFCell cell = getCell(row, colNum);
		return cell.getBooleanCellValue();
	}

	/**
	 * get Row, if not exists, create
	 * 
	 * @param rowCounter int
	 * @param sheet XSSFSheet
	 * @return XSSFRow
	 */
	public static XSSFRow getRow(int rowCounter, XSSFSheet sheet) {
		XSSFRow row = sheet.getRow((short) rowCounter);
		if (row == null) {
			row = sheet.createRow((short) rowCounter);
		}
		return row;
	}

	/**
	 * get Cell, if not exists, create
	 * 
	 * @param row XSSFRow
	 * @param column int
	 * @return XSSFCell
	 */
	public static XSSFCell getCell(XSSFRow row, int column) {
		XSSFCell cell = row.getCell((short) column);

		if (cell == null) {
			cell = row.createCell((short) column);
		}
		return cell;
	}

	/**
	 * get cell, if not exists, create
	 * 
	 * @param sheet XSSFSheet
	 * @param rowNum int
	 * @param colNum int
	 * @return XSSFCell
	 */
	public static XSSFCell getCell(XSSFSheet sheet, int rowNum, int colNum) {
		XSSFRow row = getRow(rowNum, sheet);
		XSSFCell cell = getCell(row, colNum);
		return cell;
	}

	/**
	 * copy row
	 * 
	 * @param sheet
	 * @param from begin of the row
	 * @param to destination fo the row
	 * @param count count of copy
	 */
	public static void copyRow(XSSFSheet sheet, int from, int to, int count) {

		for (int rownum = from; rownum < from + count; rownum++) {
			XSSFRow fromRow = sheet.getRow(rownum);
			XSSFRow toRow = getRow(to + rownum - from, sheet);
			if (null == fromRow)
				return;
			toRow.setHeight(fromRow.getHeight());
			toRow.setHeightInPoints(fromRow.getHeightInPoints());
			for (int i = fromRow.getFirstCellNum(); i <= fromRow.getLastCellNum() && i<256 && i >= 0; i++) {
				XSSFCell fromCell = getCell(fromRow, i);
				XSSFCell toCell = getCell(toRow, i);
//				toCell.setEncoding(fromCell.getEncoding());
				toCell.setCellStyle(fromCell.getCellStyle());
				toCell.setCellType(fromCell.getCellType());
				switch (fromCell.getCellType()) {
				case XSSFCell.CELL_TYPE_BOOLEAN:
					toCell.setCellValue(fromCell.getBooleanCellValue());
					break;
				case XSSFCell.CELL_TYPE_FORMULA:
					toCell.setCellFormula(fromCell.getCellFormula());
					break;
				case XSSFCell.CELL_TYPE_NUMERIC:
					toCell.setCellValue(fromCell.getNumericCellValue());
					break;
				case XSSFCell.CELL_TYPE_STRING:
					toCell.setCellValue(fromCell.getStringCellValue());
					break;
				default:
				}
			}
		}

		// copy merged region
		List<CellRangeAddress> shiftedRegions = new ArrayList<CellRangeAddress>();
		for (int i = 0; i < sheet.getNumMergedRegions(); i++) {
			CellRangeAddress r = sheet.getMergedRegion(i);
			if (r.getFirstRow() >= from && r.getLastRow() < from + count) {
//				Region n_r = new Region();
//				n_r.setRowFrom(r.getRowFrom() + to - from);
//				n_r.setRowTo(r.getRowTo() + to - from);
//				n_r.setColumnFrom(r.getColumnFrom());
//				n_r.setColumnTo(r.getColumnTo());
				CellRangeAddress n_r = new CellRangeAddress(r.getFirstRow() + to - from, r.getLastRow() + to - from,
						r.getFirstColumn(), r.getLastColumn());
				shiftedRegions.add(n_r);				
			}
		}
		
		// readd so it doesn't get shifted again
		Iterator<CellRangeAddress> iterator = shiftedRegions.iterator();
		while (iterator.hasNext()) {
			CellRangeAddress region = iterator.next();
			sheet.addMergedRegion(region);
		}		
	}

	public static void shiftCell(XSSFSheet sheet, XSSFRow row, XSSFCell beginCell, int shift, int rowCount) {

		if (shift == 0)
			return;

		// get the from & to row
		int fromRow = row.getRowNum();
		int toRow = row.getRowNum()+rowCount-1;
		for (int i = 0; i < sheet.getNumMergedRegions(); i++) {
			CellRangeAddress r = sheet.getMergedRegion(i);
			if (r.getFirstRow() == row.getRowNum()) {
				if (r.getLastRow() > toRow) {
					toRow = r.getLastRow();
				}
				if (r.getFirstRow() < fromRow) {
					fromRow = r.getFirstRow();
				}
			}
		}

		for (int rownum = fromRow; rownum <= toRow; rownum++) {
			XSSFRow curRow = WorkbookUtils.getRow(rownum, sheet);
			int lastCellNum = curRow.getLastCellNum();	
			if (lastCellNum > 255)
				lastCellNum = 255;
			for (int cellpos = lastCellNum; cellpos >= beginCell.getColumnIndex(); cellpos--) {
				XSSFCell fromCell = WorkbookUtils.getCell(curRow, cellpos);
				XSSFCell toCell = WorkbookUtils.getCell(curRow, cellpos + shift);
				toCell.setCellType(fromCell.getCellType());
				toCell.setCellStyle(fromCell.getCellStyle());
				switch (fromCell.getCellType()) {
				case XSSFCell.CELL_TYPE_BOOLEAN:
					toCell.setCellValue(fromCell.getBooleanCellValue());
					break;
				case XSSFCell.CELL_TYPE_FORMULA:
					toCell.setCellFormula(fromCell.getCellFormula());
					break;
				case XSSFCell.CELL_TYPE_NUMERIC:
					toCell.setCellValue(fromCell.getNumericCellValue());
					break;
				case XSSFCell.CELL_TYPE_STRING:
					toCell.setCellValue(fromCell.getStringCellValue());
					break;
				case XSSFCell.CELL_TYPE_ERROR:
					toCell.setCellErrorValue(fromCell.getErrorCellValue());
					break;
				}
				fromCell.setCellValue("");
				fromCell.setCellType(XSSFCell.CELL_TYPE_BLANK);
//				XSSFWorkbook wb = new XSSFWorkbook();
//				XSSFCellStyle style = wb.createCellStyle();
//				fromCell.getCellStyle().cloneStyleFrom(style);
			}
			
			// process merged region
			for (int cellpos = lastCellNum; cellpos >= beginCell.getColumnIndex(); cellpos--) {
				XSSFCell fromCell = WorkbookUtils.getCell(curRow, cellpos);
				
				List<CellRangeAddress> shiftedRegions = new ArrayList<CellRangeAddress>();
				for (int i=0; i<sheet.getNumMergedRegions(); i++) {
					CellRangeAddress r = sheet.getMergedRegion(i);
					if (r.getFirstRow()==curRow.getRowNum() && r.getFirstColumn() == fromCell.getColumnIndex()) {
						r.setFirstColumn((short) (r.getFirstColumn() + shift));
						r.setLastColumn((short) (r.getLastColumn() + shift));
						// have to remove/add it back
						shiftedRegions.add(r);
						sheet.removeMergedRegion(i);
						// we have to back up now since we removed one
						i = i - 1;
					}
				}
				
				// readd so it doesn't get shifted again
				Iterator<CellRangeAddress> iterator = shiftedRegions.iterator();
				while (iterator.hasNext()) {
					CellRangeAddress region = iterator.next();
					sheet.addMergedRegion(region);
				}					
			}			
		}
	}
}
