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
import com.synet.tool.rsc.DBConstants;
import com.synet.tool.rsc.ExcelConstants;
import com.synet.tool.rsc.RSCProperties;
import com.synet.tool.rsc.excel.handler.BoardWarnHandler;
import com.synet.tool.rsc.excel.handler.BrkCfmHandler;
import com.synet.tool.rsc.excel.handler.FibreListHandler;
import com.synet.tool.rsc.excel.handler.IEDBoardHandler;
import com.synet.tool.rsc.excel.handler.IEDListHandler;
import com.synet.tool.rsc.excel.handler.PortLightHandler;
import com.synet.tool.rsc.excel.handler.RscSheetHandler;
import com.synet.tool.rsc.excel.handler.SecFibreListHandler;
import com.synet.tool.rsc.excel.handler.SecLockHandler;
import com.synet.tool.rsc.excel.handler.SecProBrkHandler;
import com.synet.tool.rsc.excel.handler.SecPwrBrkHandler;
import com.synet.tool.rsc.excel.handler.StaInfoHandler;
import com.synet.tool.rsc.excel.handler.StatusInHandler;
import com.synet.tool.rsc.excel.handler.TerStrapHandler;
import com.synet.tool.rsc.model.IM100FileInfoEntity;
import com.synet.tool.rsc.model.IM102FibreListEntity;
import com.synet.tool.rsc.model.Tb1090LineprotfiberEntity;
import com.synet.tool.rsc.model.Tb1091IotermEntity;
import com.synet.tool.rsc.model.Tb1092PowerkkEntity;
import com.synet.tool.rsc.model.Tb1093VoltagekkEntity;

public class ImportInfoParser {
	
	private RscSheetHandler handler;
	protected RSCProperties rscp = RSCProperties.getInstance();

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
            	SecFibreListHandler handler = new SecFibreListHandler(0);
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
	
//	
//	public ImportResult getIEDBoardList(String xlspath, int headRowNum, Map<Integer, String> excelColInfo) {
//		ImportResult result = new ImportResult();
//		try {
//			OPCPackage xlsxPackage = OPCPackage.open(xlspath, PackageAccess.READ);
//			ReadOnlySharedStringsTable strings = new ReadOnlySharedStringsTable(xlsxPackage); 
//	        XSSFReader xssfReader = new XSSFReader(xlsxPackage);  
//	        StylesTable styles = xssfReader.getStylesTable();  
//	        XSSFReader.SheetIterator iter = (XSSFReader.SheetIterator) xssfReader.getSheetsData();  
//	        while (iter.hasNext()) {  
//	            InputStream stream = iter.next();
//	            IEDBoardHandler handler = new IEDBoardHandler(headRowNum, excelColInfo);
//            	setHandler(handler);
//            	Xls2007Parser.processSheet(styles, strings, handler, stream);
//				result.setResult(handler.getResult());
//	            stream.close();  
//		        result.setFileInfoEntity(getIM100FileInfoEntity(xlspath, DBConstants.FILE_TYPE103));
//	            break;
//	        }
//	        xlsxPackage.close();
//		} catch (Throwable e) {
//			e.printStackTrace();
//			SCTLogger.error(e.getMessage());
//		}
//		return result;
//	}
	
	@SuppressWarnings("unchecked")
	public ImportResult getFibreList(String xlspath, int headRowNum, Map<Integer, String> excelColInfo) {
		ImportResult result = new ImportResult();
		Map<String, List<IM102FibreListEntity>> map = new HashMap<String, List<IM102FibreListEntity>>();
		try {
			OPCPackage xlsxPackage = OPCPackage.open(xlspath, PackageAccess.READ);
			ReadOnlySharedStringsTable strings = new ReadOnlySharedStringsTable(xlsxPackage); 
	        XSSFReader xssfReader = new XSSFReader(xlsxPackage);  
	        StylesTable styles = xssfReader.getStylesTable();  
	        XSSFReader.SheetIterator iter = (XSSFReader.SheetIterator) xssfReader.getSheetsData();  
	        while (iter.hasNext()) {  
	            InputStream stream = iter.next();
	            String sheetName = iter.getSheetName();
	            FibreListHandler handler = new FibreListHandler(headRowNum, excelColInfo);
            	setHandler(handler);
            	Xls2007Parser.processSheet(styles, strings, handler, stream);
            	map.put(sheetName, (List<IM102FibreListEntity>) handler.getResult());
	            stream.close();  
	        }
	        if (map != null && map.size() > 0) {
	        	result.setFileInfoEntity(getIM100FileInfoEntity(xlspath, ExcelConstants.IM102_FIBRE_LIST));
	        	result.setResult(map);
	        }
	        xlsxPackage.close();
		} catch (Throwable e) {
			e.printStackTrace();
			SCTLogger.error(e.getMessage());
		}
		return result;
	}
	
	public ImportResult getImportData(String xlspath, int headRowNum, Map<Integer, String> excelColInfo, String fileType) {
		ImportResult result = new ImportResult();
		RscSheetHandler handler = getHandler(headRowNum, excelColInfo, fileType);
		if (handler == null) {
			return result;
		}
    	setHandler(handler);
		try {
			OPCPackage xlsxPackage = OPCPackage.open(xlspath, PackageAccess.READ);
			ReadOnlySharedStringsTable strings = new ReadOnlySharedStringsTable(xlsxPackage); 
	        XSSFReader xssfReader = new XSSFReader(xlsxPackage);  
	        StylesTable styles = xssfReader.getStylesTable();  
	        XSSFReader.SheetIterator iter = (XSSFReader.SheetIterator) xssfReader.getSheetsData();  
	        while (iter.hasNext()) {  
	            InputStream stream = iter.next();
            	Xls2007Parser.processSheet(styles, strings, handler, stream);
				result.setResult(handler.getResult());
	            stream.close();  
		        result.setFileInfoEntity(getIM100FileInfoEntity(xlspath, fileType));
	            break;
	        }
	        xlsxPackage.close();
		} catch (Throwable e) {
			e.printStackTrace();
			SCTLogger.error(e.getMessage());
		}
		return result;
	}

	private RscSheetHandler getHandler(int headRowNum, Map<Integer, String> excelColInfo, String fileType) {
		switch (fileType) {
		case ExcelConstants.IM101_IED_LIST:
			return new IEDListHandler(headRowNum, excelColInfo);
		case ExcelConstants.IM102_FIBRE_LIST:
			return new FibreListHandler(headRowNum, excelColInfo);
		case ExcelConstants.IM103_IED_BOARD:
			return new IEDBoardHandler(headRowNum, excelColInfo);
		case ExcelConstants.IM104_STATUS_IN:
			return new StatusInHandler(headRowNum, excelColInfo);
		case ExcelConstants.IM105_BOARD_WARN:
			return new BoardWarnHandler(headRowNum, excelColInfo);
		case ExcelConstants.IM106_PORT_LIGHT:
			return new PortLightHandler(headRowNum, excelColInfo);
		case ExcelConstants.IM107_TER_STRAP:
			return new TerStrapHandler(headRowNum, excelColInfo);
		case ExcelConstants.IM108_BRK_CFM:
			return new BrkCfmHandler(headRowNum, excelColInfo);
		case ExcelConstants.IM109_STA_INFO:
			return new StaInfoHandler(headRowNum, excelColInfo);
		default:
			return null;
		}
	}
	
	private IM100FileInfoEntity getIM100FileInfoEntity(String xlspath, String fileType) {
		IM100FileInfoEntity fileInfoEntity = new IM100FileInfoEntity();
		fileInfoEntity.setIm100Code(rscp.nextTbCode(DBConstants.PR_FILEINFO));
		fileInfoEntity.setFilePath(xlspath);
		fileInfoEntity.setFileName(xlspath.substring(xlspath.lastIndexOf("\\") + 2));
		switch (fileType) {
		case ExcelConstants.IM101_IED_LIST:
			fileInfoEntity.setFileType(DBConstants.FILE_TYPE101);
			break;
		case ExcelConstants.IM102_FIBRE_LIST:
			fileInfoEntity.setFileType(DBConstants.FILE_TYPE102);
			break;
		case ExcelConstants.IM103_IED_BOARD:
			fileInfoEntity.setFileType(DBConstants.FILE_TYPE103);
			break;
		case ExcelConstants.IM104_STATUS_IN:
			fileInfoEntity.setFileType(DBConstants.FILE_TYPE104);
			break;
		case ExcelConstants.IM105_BOARD_WARN:
			fileInfoEntity.setFileType(DBConstants.FILE_TYPE105);
			break;
		case ExcelConstants.IM106_PORT_LIGHT:
			fileInfoEntity.setFileType(DBConstants.FILE_TYPE106);
			break;
		case ExcelConstants.IM107_TER_STRAP:
			fileInfoEntity.setFileType(DBConstants.FILE_TYPE107);
			break;
		case ExcelConstants.IM108_BRK_CFM:
			fileInfoEntity.setFileType(DBConstants.FILE_TYPE108);
			break;
		case ExcelConstants.IM109_STA_INFO:
			fileInfoEntity.setFileType(DBConstants.FILE_TYPE109);
			break;
		default:
			break;
		}
		return fileInfoEntity;
	}

	public RscSheetHandler getHandler() {
		return handler;
	}

	public void setHandler(RscSheetHandler handler) {
		this.handler = handler;
	}

}

