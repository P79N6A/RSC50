/**
 * Copyright (c) 2007-2010 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based on IEC61850 SCT.
 */
package com.shrcn.found.file.excel;

import java.text.DecimalFormat;
import java.text.ParseException;

import org.apache.poi.hssf.usermodel.HSSFCell;

/**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2012-9-14
 */
/**
 * $Log: ExcelUtil.java,v $
 * Revision 1.7  2013/12/06 01:52:10  cchun
 * Refactor:整理格式
 *
 * Revision 1.6  2013/12/06 01:31:51  cxc
 * update:修改
 *
 * Revision 1.5  2013/12/05 10:44:42  cxc
 * update:修改导入的整数出现浮点数
 *
 * Revision 1.4  2013/11/20 10:36:29  cxc
 * update:将科学计数法的格式转为小数形式
 *
 * Revision 1.3  2012/10/25 06:57:57  cchun
 * Fix Bug:取值后去掉空字符串
 *
 * Revision 1.2  2012/10/24 10:19:02  cchun
 * Update:修改getCellValue()逻辑
 *
 * Revision 1.1  2012/09/14 08:36:51  cchun
 * Update:统一Excel单元格取值方法
 *
 */
public class ExcelUtil {
	
	/**
	 * 获取单元格值字符串
	 * @param cell
	 * @return
	 */
	public static String getCellValue(HSSFCell cell) {
		String value = "";
		if (cell == null)
			return value;
		switch (cell.getCellType()) {
		case HSSFCell.CELL_TYPE_STRING:
			value = cell.getStringCellValue();
			break;
		case HSSFCell.CELL_TYPE_NUMERIC:
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
		}
		return value.trim();
	}
}
