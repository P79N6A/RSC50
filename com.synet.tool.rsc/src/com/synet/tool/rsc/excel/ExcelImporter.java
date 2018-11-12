package com.synet.tool.rsc.excel;

import java.util.Map;

import org.eclipse.core.runtime.IProgressMonitor;

import com.shrcn.found.file.excel.SheetsHandler;
import com.synet.tool.rsc.DBConstants;
import com.synet.tool.rsc.RSCProperties;
import com.synet.tool.rsc.model.IM100FileInfoEntity;
import com.synet.tool.rsc.processor.DefaultImportProcessor;
import com.synet.tool.rsc.util.ExcelReaderUtil;

public class ExcelImporter {
	
	private static RSCProperties rscp = RSCProperties.getInstance();
	
	@SuppressWarnings("unchecked")
	public static boolean importExcelData(IProgressMonitor monitor, String title, String filePath, int excelHeadRow, Map<Integer, String> excelColInfo) {
		ImportResult result = getImportData(filePath, excelHeadRow, excelColInfo, title);
		monitor.worked(1);
		boolean b = new DefaultImportProcessor().processor(result.getFileInfoEntity(), result.getResult());
		monitor.worked(1);
		return b;
	}
	
	public static ImportResult getImportData(String xlspath, int headRowNum, Map<Integer, String> excelColInfo, String fileType) {
		ImportResult result = new ImportResult();
		SheetsHandler handler = ImportConfigFactory.getImportHandler(headRowNum, excelColInfo, fileType);
		result.setResult(ExcelReaderUtil.parseByHandler(xlspath, handler));
		result.setFileInfoEntity(getIM100FileInfoEntity(xlspath, fileType));
		return result;
	}

//	private static RscSheetHandler getHandler(int headRowNum, Map<Integer, String> excelColInfo, String fileType) {
//		switch (fileType) {
//		case ExcelConstants.IM101_IED_LIST:
//			return new IEDListHandler(headRowNum, excelColInfo);
//		case ExcelConstants.IM102_FIBRE_LIST:
//			return new NewFibreListHandler(headRowNum, excelColInfo);
//		case ExcelConstants.IM103_IED_BOARD:
//			return new IEDBoardHandler(headRowNum, excelColInfo);
//		case ExcelConstants.IM104_STATUS_IN:
//			return new StatusInHandler(headRowNum, excelColInfo);
//		case ExcelConstants.IM105_BOARD_WARN:
//			return new BoardWarnHandler(headRowNum, excelColInfo);
//		case ExcelConstants.IM106_PORT_LIGHT:
//			return new PortLightHandler(headRowNum, excelColInfo);
//		case ExcelConstants.IM107_TER_STRAP:
//			return new TerStrapHandler(headRowNum, excelColInfo);
//		case ExcelConstants.IM108_BRK_CFM:
//			return new BrkCfmHandler(headRowNum, excelColInfo);
//		case ExcelConstants.IM109_STA_INFO:
//			return new StaInfoHandler(headRowNum, excelColInfo);
//		case ExcelConstants.IM110_LINK_WARN:
//			return new LinkWarnHandler(headRowNum, excelColInfo);
//		default:
//			return null;
//		}
//	}
	
	private static IM100FileInfoEntity getIM100FileInfoEntity(String xlspath, String fileType) {
		IM100FileInfoEntity fileInfoEntity = new IM100FileInfoEntity();
		fileInfoEntity.setIm100Code(rscp.nextTbCode(DBConstants.PR_FILEINFO));
		fileInfoEntity.setFilePath(xlspath);
		fileInfoEntity.setFileName(xlspath.substring(xlspath.lastIndexOf("\\") + 1));
		fileInfoEntity.setFileType(EnumFileType.getByTitle(fileType).getId());
		return fileInfoEntity;
	}
}
