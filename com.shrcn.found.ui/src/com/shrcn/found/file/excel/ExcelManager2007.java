/**
 * Copyright (c) 2007-2013 上海思弘瑞电力控制技术有限公司. All rights reserved. 
 * This program is an eclipse Rich Client Application
 * designed for IED configuration and debuging.
 */
package com.shrcn.found.file.excel;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.Date;

import net.sf.excelutils2007.ExcelException;
import net.sf.excelutils2007.ExcelUtils;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.Region;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.shrcn.found.common.Constants;
import com.shrcn.found.common.util.StringUtil;

/**
 * 
 * @author 吴小兵(mailto:wxb.14417@shrcn.com)
 * @version 1.0, 2014-8-25
 */
/**
 * excel2007读写
 */
public class ExcelManager2007 {

	public static XSSFWorkbook getWorkbook(String fileName) {
		XSSFWorkbook book = null;
		FileInputStream in = null;
		try {
			in = new FileInputStream(fileName);
			book = new XSSFWorkbook(in);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return book;
	}

	public static XSSFSheet getSheet(XSSFWorkbook book, int index) {
		XSSFSheet sheet = book.getSheetAt(index);
		return sheet;
	}

	public static XSSFRow getRow(XSSFSheet sheet, int row) {
		XSSFRow row1 = sheet.getRow(row);
		return row1;
	}
	
	public static Date getDateValue(XSSFSheet sheet, int col, int row) {
		return sheet.getRow(row).getCell(col).getDateCellValue();
	}

	public static String getStringValue(XSSFSheet sheet, int col, int row) {
		XSSFRow row1 = sheet.getRow(row);
		String value = "";
		if (row1 == null)
			return value;
		XSSFCell cell = row1.getCell(col);
		if (cell == null)
			return value;
		switch (cell.getCellType()) {
		case XSSFCell.CELL_TYPE_STRING:
			value = cell.getStringCellValue();
			break;
		case XSSFCell.CELL_TYPE_NUMERIC:
			value = String.valueOf(cell.getNumericCellValue());
			DecimalFormat a = new DecimalFormat("#,##0.00000000");
			try {
				Number num = a.parse(value);
				if (num instanceof Long) {
					value = num.toString();
				} else {
					value = a.format(cell.getNumericCellValue());
				}
			} catch (ParseException e) {
				e.printStackTrace();
			}
			break;
		default:
			value = cell.getStringCellValue();
			break;
		}
		String result = value.trim();
		result = result.replace("\r", " ");
		result = result.replace("\n", " ");
		result = result.replace("'", "‘");
		result = result.replace(";", "；");
		result = result.replace(",", "，");
		return result;
	}
	
	/**
	 * 根据指定模板导出Excel文件。
	 * @param baseClass
	 * @param tplPath
	 * @param fileName
	 */
	public static void saveExcelFile(Class<?> baseClass, String tplPath,
			String fileName) {
		InputStream is = null;
		FileOutputStream out = null;
		try {
			is = baseClass.getClassLoader().getResourceAsStream(tplPath);
			out = new FileOutputStream(fileName);
			exportExcel(is, out);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (is != null)
					is.close();
				if (out != null)
					out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 根据指定模板导出Excel文件。
	 * @param is
	 * @param out
	 * @throws ExcelException
	 */
	private static void exportExcel(InputStream is, FileOutputStream out)
			throws ExcelException {
		ExcelUtils.addValue("printDate",
				StringUtil.getCurrentTime(Constants.STD_TIME_FORMAT));
		ExcelUtils.export(is, ExcelUtils.getContext(), out);
	}

	public static void main(String[] args) {
		String filename = "d:\\工作周报模板.xls";
		XSSFWorkbook book = ExcelManager2007.getWorkbook(filename);
		XSSFSheet sheet = ExcelManager2007.getSheet(book, 0);
		int maxcol = 8;
		int maxrow = sheet.getPhysicalNumberOfRows();
		for (int i = 0; i < maxrow; i++) {
			for (int j = 0; j < maxcol; j++) {
				String value = ExcelManager2007.getStringValue(sheet, j, i);
				System.out.println("row" + i + "col" + j + ":" + value);
			}

		}

	}

}
