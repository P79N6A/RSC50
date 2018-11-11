package com.synet.tool.rsc.util;

import java.io.File;
import java.io.InputStream;
import java.util.List;

import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.openxml4j.opc.PackageAccess;
import org.apache.poi.xssf.eventusermodel.ReadOnlySharedStringsTable;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.model.StylesTable;
import org.eclipse.swt.SWT;

import com.shrcn.found.common.log.SCTLogger;
import com.shrcn.found.common.util.StringUtil;
import com.shrcn.found.file.excel.SheetsHandler;
import com.shrcn.found.file.excel.Xls2007Parser;
import com.shrcn.found.ui.util.DialogHelper;
import com.shrcn.found.ui.util.SwtUtil;

public class ExcelReaderUtil {
	
	public static List<?> importByHandler(SheetsHandler handler) {
		String xlspath = DialogHelper.selectFile(SwtUtil.getDefaultShell(), SWT.OPEN, "*.xlsx");
		if (StringUtil.isEmpty(xlspath) || !new File(xlspath).exists()) {
			DialogHelper.showAsynWarning("文件路径为空或者不存在！");
			return null;
		}
		return parseByHandler(xlspath, handler);
	}

	public static List<?> parseByHandler(String xlspath, SheetsHandler handler) {
		try {
			OPCPackage xlsxPackage = OPCPackage.open(xlspath, PackageAccess.READ);
			ReadOnlySharedStringsTable strings = new ReadOnlySharedStringsTable(xlsxPackage);  
	        XSSFReader xssfReader = new XSSFReader(xlsxPackage);  
	        StylesTable styles = xssfReader.getStylesTable();  
	        XSSFReader.SheetIterator iter = (XSSFReader.SheetIterator) xssfReader.getSheetsData();  
	        while (iter.hasNext()) {  
	            InputStream stream = iter.next();
            	Xls2007Parser.processSheet(styles, strings, handler, stream);
	            stream.close();  
	            break;
	        }
	        xlsxPackage.close();
		} catch (Throwable e) {
			e.printStackTrace();
			SCTLogger.error(e.getMessage());
		}
		return handler.getResult();
	}
}
