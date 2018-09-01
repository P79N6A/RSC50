package com.synet.tool.rsc.excel;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.openxml4j.opc.PackageAccess;
import org.apache.poi.xssf.eventusermodel.ReadOnlySharedStringsTable;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.model.StylesTable;

import com.shrcn.found.common.log.SCTLogger;
import com.shrcn.found.file.excel.Xls2007Parser;
import com.synet.tool.rsc.excel.handler.TableHeadHandler;

public class TableHeadParser {
	
	@SuppressWarnings("unchecked")
	public Map<Integer, String> getTableHeadInfo(String xlspath, int headRow) {
		Map<Integer, String> result = null;
		try {
			OPCPackage xlsxPackage = OPCPackage.open(xlspath, PackageAccess.READ);
			ReadOnlySharedStringsTable strings = new ReadOnlySharedStringsTable(xlsxPackage);  
	        XSSFReader xssfReader = new XSSFReader(xlsxPackage);  
	        StylesTable styles = xssfReader.getStylesTable();  
	        XSSFReader.SheetIterator iter = (XSSFReader.SheetIterator) xssfReader.getSheetsData();  
	        while (iter.hasNext()) {  
	            InputStream stream = iter.next();
            	TableHeadHandler handler = new TableHeadHandler(headRow);
            	Xls2007Parser.processSheet(styles, strings, handler, stream);
            	List<Map<Integer, String>> list = (List<Map<Integer, String>>) handler.getResult();
            	if (list != null && list.size() > 0) {
            		result =  list.get(0);
            	}
	            stream.close();  
	            break;
	        }
	        xlsxPackage.close();
		} catch (Throwable e) {
			e.printStackTrace();
			SCTLogger.error(e.getMessage());
		}
		return result;
	}
}
