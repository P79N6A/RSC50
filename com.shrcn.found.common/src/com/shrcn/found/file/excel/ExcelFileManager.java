/**
 * Copyright (c) 2007, 2008 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based Visual Device Develop System.
 */
package com.shrcn.found.file.excel;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import net.sf.excelutils.ExcelException;
import net.sf.excelutils.ExcelUtils;
import net.sf.excelutils.WorkbookUtils;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.shrcn.found.common.Constants;
import com.shrcn.found.common.log.SCTLogger;
import com.shrcn.found.common.util.StringUtil;

/**
 * 
 * @author 孙春颖(mailto:scy@shrcn.com)
 * @version 1.0, 2014-5-20
 */
public class ExcelFileManager {

	/**
	 * 保存Excel文件
	 * 
	 * @param workbook
	 * @param file
	 */
	public static void saveExcelFile(HSSFWorkbook workbook, String fileName) {
		FileOutputStream fileOut = null;
		try {
			fileOut = new FileOutputStream(fileName);
			workbook.write(fileOut);
		} catch (FileNotFoundException e) {
			SCTLogger.error("不存在文件" + fileName + "。", e);
		} catch (IOException e) {
			SCTLogger.error("文件读写错误。", e);
		} finally {
			try {
				if (fileOut != null) {
					fileOut.flush();
					fileOut.close();
				}
			} catch (IOException e) {
				SCTLogger.error("关闭输入流错误。", e);
			}
		}
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
	 * @param tplFileName
	 * @param fileName
	 */
	public static void saveExcelFile(String tplFileName, String fileName) {
		InputStream is = null;
		FileOutputStream out = null;
		try {
			is = new FileInputStream(tplFileName);
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
}
