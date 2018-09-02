/**
 * Copyright (c) 2007-2017 泗业技术咨询有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application.
 */
package com.synet.tool.rsc.action.imp;

import java.util.List;
import java.util.Map;

import com.shrcn.found.ui.util.DialogHelper;
import com.synet.tool.rsc.ExcelConstants;
import com.synet.tool.rsc.dialog.ChooseTableColDialog;
import com.synet.tool.rsc.dialog.ChooseTableHeadDialog;
import com.synet.tool.rsc.excel.ImportInfoParser;
import com.synet.tool.rsc.excel.ImportResult;
import com.synet.tool.rsc.model.IM101IEDListEntity;
import com.synet.tool.rsc.model.IM102FibreListEntity;
import com.synet.tool.rsc.model.IM103IEDBoardEntity;
import com.synet.tool.rsc.model.IM104StatusInEntity;
import com.synet.tool.rsc.model.IM105BoardWarnEntity;
import com.synet.tool.rsc.model.IM106PortLightEntity;
import com.synet.tool.rsc.model.IM107TerStrapEntity;
import com.synet.tool.rsc.model.IM108BrkCfmEntity;
import com.synet.tool.rsc.model.IM109StaInfoEntity;
import com.synet.tool.rsc.processor.ImportBoardWarnProcessor;
import com.synet.tool.rsc.processor.ImportBrkCfmProcessor;
import com.synet.tool.rsc.processor.ImportFibreListProcessor;
import com.synet.tool.rsc.processor.ImportIEDBoardProcessor;
import com.synet.tool.rsc.processor.ImportIEDListProcessor;
import com.synet.tool.rsc.processor.ImportPortLightProcessor;
import com.synet.tool.rsc.processor.ImportStaInfoProcessor;
import com.synet.tool.rsc.processor.ImportStatusInProcessor;
import com.synet.tool.rsc.processor.ImportTerStrapProcessor;


 /**
 * 
 * @author 陈春(mailto:chench80@126.com)
 * @version 1.0, 2018-8-7
 */
public class ImportExcelAction extends BaseImportAction {
	
	public ImportExcelAction(String title) {
		super(title);
	}

	@Override
	public void run() {
		String filePath = DialogHelper.getSaveFilePath(getTitle() + "文件", "", new String[]{"*.xlsx"});
		if (filePath == null || "".equals(filePath)){
			return;
		}
		ChooseTableHeadDialog headDialog = new ChooseTableHeadDialog(getShell());
		headDialog.setExcelFilPath(filePath);
		if (headDialog.open() == 0) {
			ChooseTableColDialog colDialog = new ChooseTableColDialog(getShell());
			//表头信息
			Map<Integer, String> excelColName = headDialog.getExcelColName();
			int excelHeadRow = headDialog.getTableHeadRow();
			if (excelColName == null) {
				DialogHelper.showAsynError("文件异常");
				return;
			}
			colDialog.setExcelColMap(excelColName);
			String[] fields = getExcelFields();
			if (fields == null) {
				DialogHelper.showAsynError("系统异常");
				return;
			}
			colDialog.setFields(fields);
			if (colDialog.open() == 0) {
				Map<Integer, String> excelColInfo = colDialog.getMap();
				boolean b = importExcelData(filePath, excelHeadRow, excelColInfo);
				if (b) {
					DialogHelper.showAsynInformation("导入成功！");
				} else {
					DialogHelper.showAsynError("导入失败，请检查文件格式");
				}
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	private boolean importExcelData(String filePath, int excelHeadRow, Map<Integer, String> excelColInfo) {
		ImportInfoParser parser = new ImportInfoParser();
		ImportResult result = null;
		switch (getTitle()) {
		case ExcelConstants.IM101_IED_LIST:
			result = parser.getImportData(filePath, excelHeadRow, excelColInfo, ExcelConstants.IM101_IED_LIST);
			return new ImportIEDListProcessor().processor(result.getFileInfoEntity(),
					(List<IM101IEDListEntity>) result.getResult());
		case ExcelConstants.IM102_FIBRE_LIST:
			result = parser.getFibreList(filePath, excelHeadRow, excelColInfo);
			return new ImportFibreListProcessor().processor(result.getFileInfoEntity(), 
					(Map<String, List<IM102FibreListEntity>>) result.getResult());
		case ExcelConstants.IM103_IED_BOARD:
			result = parser.getImportData(filePath, excelHeadRow, excelColInfo, ExcelConstants.IM103_IED_BOARD);
			return new ImportIEDBoardProcessor().processor(result.getFileInfoEntity(), 
					(List<IM103IEDBoardEntity>) result.getResult());
		case ExcelConstants.IM104_STATUS_IN:
			result = parser.getImportData(filePath, excelHeadRow, excelColInfo, ExcelConstants.IM104_STATUS_IN);
			return new ImportStatusInProcessor().processor(result.getFileInfoEntity(), 
					(List<IM104StatusInEntity>) result.getResult());
		case ExcelConstants.IM105_BOARD_WARN:
			result = parser.getImportData(filePath, excelHeadRow, excelColInfo, ExcelConstants.IM105_BOARD_WARN);
			return new ImportBoardWarnProcessor().processor(result.getFileInfoEntity(),
					(List<IM105BoardWarnEntity>) result.getResult());
		case ExcelConstants.IM106_PORT_LIGHT:
			result = parser.getImportData(filePath, excelHeadRow, excelColInfo, ExcelConstants.IM106_PORT_LIGHT);
			return new ImportPortLightProcessor().processor(result.getFileInfoEntity(), 
					(List<IM106PortLightEntity>) result.getResult());
		case ExcelConstants.IM107_TER_STRAP:
			result = parser.getImportData(filePath, excelHeadRow, excelColInfo, ExcelConstants.IM107_TER_STRAP);
			return new ImportTerStrapProcessor().processor(result.getFileInfoEntity(),
					(List<IM107TerStrapEntity>) result.getResult());
		case ExcelConstants.IM108_BRK_CFM:
			result = parser.getImportData(filePath, excelHeadRow, excelColInfo, ExcelConstants.IM108_BRK_CFM);
			return new ImportBrkCfmProcessor().processor(result.getFileInfoEntity(),
					(List<IM108BrkCfmEntity>) result.getResult());
		case ExcelConstants.IM109_STA_INFO:
			result = parser.getImportData(filePath, excelHeadRow, excelColInfo, ExcelConstants.IM109_STA_INFO);
			return new ImportStaInfoProcessor().processor(result.getFileInfoEntity(),
(List<IM109StaInfoEntity>) result.getResult());
		default:
			return false;
		}
	}

	private String[] getExcelFields() {
		switch (getTitle()) {
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
		default:
			return null;
		}
	}

}

