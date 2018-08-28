package com.synet.tool.rsc.excel;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.openxml4j.opc.PackageAccess;
import org.apache.poi.xssf.eventusermodel.ReadOnlySharedStringsTable;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.model.StylesTable;

import com.shrcn.found.common.log.SCTLogger;
import com.shrcn.found.file.excel.Xls2007Parser;
import com.synet.tool.rsc.entity.FibreList;
import com.synet.tool.rsc.entity.IEDBoard;
import com.synet.tool.rsc.entity.StaInfo;
import com.synet.tool.rsc.excel.handler.FibreListHandler;
import com.synet.tool.rsc.excel.handler.IEDBoardHandler;
import com.synet.tool.rsc.excel.handler.RscSheetHandler;
import com.synet.tool.rsc.excel.handler.SecFibreListHandler;
import com.synet.tool.rsc.excel.handler.SecLockHandler;
import com.synet.tool.rsc.excel.handler.SecProBrkHandler;
import com.synet.tool.rsc.excel.handler.SecPwrBrkHandler;
import com.synet.tool.rsc.excel.handler.StaInfoHandler;
import com.synet.tool.rsc.model.Tb1090LineprotfiberEntity;
import com.synet.tool.rsc.model.Tb1091IotermEntity;
import com.synet.tool.rsc.model.Tb1092PowerkkEntity;
import com.synet.tool.rsc.model.Tb1093VoltagekkEntity;

public class ImportInfoParser {
	
	private RscSheetHandler handler;

	@SuppressWarnings("unchecked")
	public List<Tb1090LineprotfiberEntity> getLineprotfiberList(String xlspath) {
		 List<Tb1090LineprotfiberEntity> result = null;
		try {
			OPCPackage xlsxPackage = OPCPackage.open(xlspath, PackageAccess.READ);
			ReadOnlySharedStringsTable strings = new ReadOnlySharedStringsTable(xlsxPackage);  
	        XSSFReader xssfReader = new XSSFReader(xlsxPackage);  
	        StylesTable styles = xssfReader.getStylesTable();  
	        XSSFReader.SheetIterator iter = (XSSFReader.SheetIterator) xssfReader.getSheetsData();  
	        while (iter.hasNext()) {  
	            InputStream stream = iter.next();
            	SecFibreListHandler handler = new SecFibreListHandler();
            	setHandler(handler);
            	Xls2007Parser.processSheet(styles, strings, handler, stream);
				result = (List<Tb1090LineprotfiberEntity>) handler.getResult();
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
	
	@SuppressWarnings("unchecked")
	public List<Tb1091IotermEntity> getIotermList(String xlspath) {
		 List<Tb1091IotermEntity> result = null;
		try {
			OPCPackage xlsxPackage = OPCPackage.open(xlspath, PackageAccess.READ);
			ReadOnlySharedStringsTable strings = new ReadOnlySharedStringsTable(xlsxPackage);  
	        XSSFReader xssfReader = new XSSFReader(xlsxPackage);  
	        StylesTable styles = xssfReader.getStylesTable();  
	        XSSFReader.SheetIterator iter = (XSSFReader.SheetIterator) xssfReader.getSheetsData();  
	        while (iter.hasNext()) {  
	            InputStream stream = iter.next();
            	SecLockHandler handler = new SecLockHandler();
            	setHandler(handler);
            	Xls2007Parser.processSheet(styles, strings, handler, stream);
				result = (List<Tb1091IotermEntity>) handler.getResult();
	            stream.close();  
	            break;
	        }
	        xlsxPackage.close();
		} catch (Throwable e) {
			SCTLogger.error(e.getMessage());
		}
		return result;
	}
	
	@SuppressWarnings("unchecked")
	public List<Tb1092PowerkkEntity> getPowerkkList(String xlspath) {
		 List<Tb1092PowerkkEntity> result = null;
		try {
			OPCPackage xlsxPackage = OPCPackage.open(xlspath, PackageAccess.READ);
			ReadOnlySharedStringsTable strings = new ReadOnlySharedStringsTable(xlsxPackage);  
	        XSSFReader xssfReader = new XSSFReader(xlsxPackage);  
	        StylesTable styles = xssfReader.getStylesTable();  
	        XSSFReader.SheetIterator iter = (XSSFReader.SheetIterator) xssfReader.getSheetsData();  
	        while (iter.hasNext()) {  
	            InputStream stream = iter.next();
            	SecPwrBrkHandler handler = new SecPwrBrkHandler();
            	setHandler(handler);
            	Xls2007Parser.processSheet(styles, strings, handler, stream);
				result = (List<Tb1092PowerkkEntity>) handler.getResult();
	            stream.close(); 
	            break;
	        }
	        xlsxPackage.close();
		} catch (Throwable e) {
			SCTLogger.error(e.getMessage());
		}
		return result;
	}
	
	@SuppressWarnings("unchecked")
	public List<Tb1093VoltagekkEntity> getVoltagekkList(String xlspath) {
		 List<Tb1093VoltagekkEntity> result = null;
		try {
			OPCPackage xlsxPackage = OPCPackage.open(xlspath, PackageAccess.READ);
			ReadOnlySharedStringsTable strings = new ReadOnlySharedStringsTable(xlsxPackage);  
	        XSSFReader xssfReader = new XSSFReader(xlsxPackage);  
	        StylesTable styles = xssfReader.getStylesTable();  
	        XSSFReader.SheetIterator iter = (XSSFReader.SheetIterator) xssfReader.getSheetsData();  
	        while (iter.hasNext()) {  
	            InputStream stream = iter.next();
            	SecProBrkHandler handler = new SecProBrkHandler();
            	setHandler(handler);
            	Xls2007Parser.processSheet(styles, strings, handler, stream);
				result = (List<Tb1093VoltagekkEntity>) handler.getResult();
	            stream.close();  
	            break;
	        }
	        xlsxPackage.close();
		} catch (Throwable e) {
			SCTLogger.error(e.getMessage());
		}
		return result;
	}
	
	@SuppressWarnings("unchecked")
	public List<StaInfo> getStaInfoList(String xlspath) {
		 List<StaInfo> result = null;
		try {
			OPCPackage xlsxPackage = OPCPackage.open(xlspath, PackageAccess.READ);
			ReadOnlySharedStringsTable strings = new ReadOnlySharedStringsTable(xlsxPackage);  
	        XSSFReader xssfReader = new XSSFReader(xlsxPackage);  
	        StylesTable styles = xssfReader.getStylesTable();  
	        XSSFReader.SheetIterator iter = (XSSFReader.SheetIterator) xssfReader.getSheetsData();  
	        while (iter.hasNext()) {  
	            InputStream stream = iter.next();
            	StaInfoHandler handler = new StaInfoHandler();
            	setHandler(handler);
            	Xls2007Parser.processSheet(styles, strings, handler, stream);
				result = (List<StaInfo>) handler.getResult();
	            stream.close();  
	            break;
	        }
	        xlsxPackage.close();
		} catch (Throwable e) {
			SCTLogger.error(e.getMessage());
		}
		return result;
	}
	
	@SuppressWarnings("unchecked")
	public List<IEDBoard> getIEDBoardList(String xlspath) {
		 List<IEDBoard> result = null;
		try {
			OPCPackage xlsxPackage = OPCPackage.open(xlspath, PackageAccess.READ);
			ReadOnlySharedStringsTable strings = new ReadOnlySharedStringsTable(xlsxPackage); 
	        XSSFReader xssfReader = new XSSFReader(xlsxPackage);  
	        StylesTable styles = xssfReader.getStylesTable();  
	        XSSFReader.SheetIterator iter = (XSSFReader.SheetIterator) xssfReader.getSheetsData();  
	        while (iter.hasNext()) {  
	            InputStream stream = iter.next();
	            IEDBoardHandler handler = new IEDBoardHandler();
            	setHandler(handler);
            	Xls2007Parser.processSheet(styles, strings, handler, stream);
				result = (List<IEDBoard>) handler.getResult();
	            stream.close();  
	            break;
	        }
	        xlsxPackage.close();
		} catch (Throwable e) {
			SCTLogger.error(e.getMessage());
		}
		return result;
	}
	
	@SuppressWarnings("unchecked")
	public Map<String, List<FibreList>> getFibreList(String xlspath) {
		Map<String, List<FibreList>> result = new HashMap<String, List<FibreList>>();
		try {
			OPCPackage xlsxPackage = OPCPackage.open(xlspath, PackageAccess.READ);
			ReadOnlySharedStringsTable strings = new ReadOnlySharedStringsTable(xlsxPackage); 
	        XSSFReader xssfReader = new XSSFReader(xlsxPackage);  
	        StylesTable styles = xssfReader.getStylesTable();  
	        XSSFReader.SheetIterator iter = (XSSFReader.SheetIterator) xssfReader.getSheetsData();  
	        while (iter.hasNext()) {  
	            InputStream stream = iter.next();
	            String sheetName = iter.getSheetName();
	            FibreListHandler handler = new FibreListHandler();
            	setHandler(handler);
            	Xls2007Parser.processSheet(styles, strings, handler, stream);
				result.put(sheetName, (List<FibreList>) handler.getResult());
	            stream.close();  
	        }
	        xlsxPackage.close();
		} catch (Throwable e) {
			e.printStackTrace();
			SCTLogger.error(e.getMessage());
		}
		return result;
	}

	public RscSheetHandler getHandler() {
		return handler;
	}

	public void setHandler(RscSheetHandler handler) {
		this.handler = handler;
	}

}

