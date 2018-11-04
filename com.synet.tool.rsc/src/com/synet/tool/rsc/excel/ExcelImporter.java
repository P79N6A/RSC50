package com.synet.tool.rsc.excel;

import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.IProgressMonitor;

import com.synet.tool.rsc.ExcelConstants;
import com.synet.tool.rsc.model.IM101IEDListEntity;
import com.synet.tool.rsc.model.IM102FibreListEntity;
import com.synet.tool.rsc.model.IM103IEDBoardEntity;
import com.synet.tool.rsc.model.IM104StatusInEntity;
import com.synet.tool.rsc.model.IM105BoardWarnEntity;
import com.synet.tool.rsc.model.IM106PortLightEntity;
import com.synet.tool.rsc.model.IM107TerStrapEntity;
import com.synet.tool.rsc.model.IM108BrkCfmEntity;
import com.synet.tool.rsc.model.IM109StaInfoEntity;
import com.synet.tool.rsc.model.IM110LinkWarnEntity;
import com.synet.tool.rsc.processor.ImportBoardWarnProcessor;
import com.synet.tool.rsc.processor.ImportBrkCfmProcessor;
import com.synet.tool.rsc.processor.ImportFibreListProcessor3;
import com.synet.tool.rsc.processor.ImportIEDBoardProcessor;
import com.synet.tool.rsc.processor.ImportIEDListProcessor;
import com.synet.tool.rsc.processor.ImportLinkWarnProcessor;
import com.synet.tool.rsc.processor.ImportPortLightProcessor;
import com.synet.tool.rsc.processor.ImportStaInfoProcessor;
import com.synet.tool.rsc.processor.ImportStatusInProcessor;
import com.synet.tool.rsc.processor.ImportTerStrapProcessor;

public class ExcelImporter {
	
	@SuppressWarnings("unchecked")
	public static boolean importExcelData(IProgressMonitor monitor, String title, String filePath, int excelHeadRow, Map<Integer, String> excelColInfo) {
		ImportInfoParser parser = new ImportInfoParser();
		ImportResult result = null;
		boolean b = false;
		switch (title) {
		case ExcelConstants.IM101_IED_LIST:
			result = parser.getImportData(filePath, excelHeadRow, excelColInfo, ExcelConstants.IM101_IED_LIST);
			monitor.worked(1);
			b = new ImportIEDListProcessor().processor(result.getFileInfoEntity(),
					(List<IM101IEDListEntity>) result.getResult());
			monitor.worked(1);
			break;
		case ExcelConstants.IM102_FIBRE_LIST:
//			result = parser.getFibreList(filePath, excelHeadRow, excelColInfo);
//			return new ImportFibreListProcessor().processor(result.getFileInfoEntity(), 
//					(Map<String, List<IM102FibreListEntity>>) result.getResult());
			result = parser.getImportData(filePath, excelHeadRow, excelColInfo, ExcelConstants.IM102_FIBRE_LIST);
			monitor.worked(1);
			b = new ImportFibreListProcessor3().processor(result.getFileInfoEntity(), 
					(List<IM102FibreListEntity>) result.getResult());
			monitor.worked(1);
			break;
		case ExcelConstants.IM103_IED_BOARD:
			result = parser.getImportData(filePath, excelHeadRow, excelColInfo, ExcelConstants.IM103_IED_BOARD);
			monitor.worked(1);
			b = new ImportIEDBoardProcessor().processor(result.getFileInfoEntity(), 
					(List<IM103IEDBoardEntity>) result.getResult());
			monitor.worked(1);
			break;
		case ExcelConstants.IM104_STATUS_IN:
			result = parser.getImportData(filePath, excelHeadRow, excelColInfo, ExcelConstants.IM104_STATUS_IN);
			monitor.worked(1);
			b = new ImportStatusInProcessor().processor(result.getFileInfoEntity(), 
					(List<IM104StatusInEntity>) result.getResult());
			monitor.worked(1);
			break;
		case ExcelConstants.IM105_BOARD_WARN:
			result = parser.getImportData(filePath, excelHeadRow, excelColInfo, ExcelConstants.IM105_BOARD_WARN);
			monitor.worked(1);
			b = new ImportBoardWarnProcessor().processor(result.getFileInfoEntity(),
					(List<IM105BoardWarnEntity>) result.getResult());
			monitor.worked(1);
			break;
		case ExcelConstants.IM106_PORT_LIGHT:
			result = parser.getImportData(filePath, excelHeadRow, excelColInfo, ExcelConstants.IM106_PORT_LIGHT);
			monitor.worked(1);
			b = new ImportPortLightProcessor().processor(result.getFileInfoEntity(), 
					(List<IM106PortLightEntity>) result.getResult());
			monitor.worked(1);
			break;
		case ExcelConstants.IM107_TER_STRAP:
			result = parser.getImportData(filePath, excelHeadRow, excelColInfo, ExcelConstants.IM107_TER_STRAP);
			monitor.worked(1);
			b = new ImportTerStrapProcessor().processor(result.getFileInfoEntity(),
					(List<IM107TerStrapEntity>) result.getResult());
			monitor.worked(1);
			break;
		case ExcelConstants.IM108_BRK_CFM:
			result = parser.getImportData(filePath, excelHeadRow, excelColInfo, ExcelConstants.IM108_BRK_CFM);
			monitor.worked(1);
			b = new ImportBrkCfmProcessor().processor(result.getFileInfoEntity(),
					(List<IM108BrkCfmEntity>) result.getResult());
			monitor.worked(1);
			break;
		case ExcelConstants.IM109_STA_INFO:
			result = parser.getImportData(filePath, excelHeadRow, excelColInfo, ExcelConstants.IM109_STA_INFO);
			monitor.worked(1);
			b = new ImportStaInfoProcessor().processor(result.getFileInfoEntity(),
					(List<IM109StaInfoEntity>) result.getResult());
			monitor.worked(1);
			break;
		case ExcelConstants.IM110_LINK_WARN:
			result = parser.getImportData(filePath, excelHeadRow, excelColInfo, ExcelConstants.IM110_LINK_WARN);
			monitor.worked(1);
			b = new ImportLinkWarnProcessor().processor(result.getFileInfoEntity(),
					(List<IM110LinkWarnEntity>) result.getResult());
			monitor.worked(1);
			break;
		default:
			return false;
		}
		return b;
	}

	public static String[] getExcelFields(String title) {
		switch (title) {
		case ExcelConstants.IM101_IED_LIST:
			return ExcelConstants.IM101_IED_LIST_FIELDS;
		case ExcelConstants.IM102_FIBRE_LIST:
			return ExcelConstants.IM102_FIBRE_LIST_FIELDS;
		case ExcelConstants.IM103_IED_BOARD:
			return ExcelConstants.IM103_IED_BOARD_FIELDS;
		case ExcelConstants.IM104_STATUS_IN:
			return ExcelConstants.IM104_STATUS_IN_FIELDS;
		case ExcelConstants.IM105_BOARD_WARN:
			return ExcelConstants.IM105_BOARD_WARN_FIELDS;
		case ExcelConstants.IM106_PORT_LIGHT:
			return ExcelConstants.IM106_PORT_LIGHT_FIELDS;
		case ExcelConstants.IM107_TER_STRAP:
			return ExcelConstants.IM107_TER_STRAP_FIELDS;
		case ExcelConstants.IM108_BRK_CFM:
			return ExcelConstants.IM108_BRK_CFM_FIELDS;
		case ExcelConstants.IM109_STA_INFO:
			return ExcelConstants.IM109_STA_INFO_FIELDS;
		case ExcelConstants.IM110_LINK_WARN:
			return ExcelConstants.IM110_LINK_WARN_FIELDS;
		default:
			return null;
		}
	}
}
