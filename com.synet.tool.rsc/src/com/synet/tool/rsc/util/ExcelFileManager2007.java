package com.synet.tool.rsc.util;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import net.sf.excelutils2007.ExcelException;
import net.sf.excelutils2007.ExcelUtils;

import com.shrcn.found.common.Constants;
import com.shrcn.found.common.util.StringUtil;

public class ExcelFileManager2007 {

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
}
