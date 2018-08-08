/**
 * Copyright (c) 2007-2009 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based on IEC61850 SCT.
 */
package com.shrcn.found.ui.util;

import java.util.Map;
import java.util.Map.Entry;

import net.sf.excelutils.ExcelUtils;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import com.shrcn.found.file.excel.ExcelFileManager;

/**
 * 
 * @author 孙春颖(mailto:scy@shrcn.com)
 * @version 1.0, 2014-5-27
 */
public class FileDialogHelper {

	/**
	 * 选择导出excel文件路径
	 * @param shell
	 * @return
	 */
	public static String selectExcelFile(Shell shell, int type) {
		String fileName = DialogHelper.selectFile(shell, type, new String[] { "*.xls"});
		if (fileName != null && !fileName.endsWith(".xls")) 
			fileName += ".xls"; 
		return fileName;
	}
	public static String selectExcelFile2007(Shell shell, int type) {
		String fileName = DialogHelper.selectFile(shell, type, new String[] { "*.xlsx;*.xls"});
		if (fileName != null && !fileName.endsWith(".xls")
				&& !fileName.endsWith(".xlsx")) 
			fileName += ".xlsx"; 
		return fileName;
	}
	/**
	 * 选择导出excel文件路径
	 * @param shell
	 * @return
	 */
	public static String selectExcelFile(Shell shell) {
		return selectExcelFile(shell, SWT.SAVE);
	}
	
	/**
	 * 导出Excel
	 * @param clazz
	 * @param config
	 * @param params
	 * @return
	 */
	public static boolean exportExcel(Class<?> clazz, String config, Map<String, Object> params) {
		Shell shell = Display.getDefault().getActiveShell();
		String fileName = selectExcelFile(shell);
		if (fileName == null)
			return false;
		for (Entry<String, Object> entry : params.entrySet()) {
			ExcelUtils.addValue(entry.getKey(), entry.getValue());
		}
		ExcelFileManager.saveExcelFile(clazz, config, fileName);
		return true;
	}
}
