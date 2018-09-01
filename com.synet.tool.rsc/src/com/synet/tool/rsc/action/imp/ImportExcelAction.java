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
import com.synet.tool.rsc.model.IM103IEDBoardEntity;
import com.synet.tool.rsc.processor.ImportIEDBoardProcessor;


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
					
				}
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	private boolean importExcelData(String filePath, int excelHeadRow, Map<Integer, String> excelColInfo) {
		ImportInfoParser parser = new ImportInfoParser();
		switch (getTitle()) {
		case ExcelConstants.IM101_IED_LIST:
			return false;
		case ExcelConstants.IM102_FIBRE_LIST:
			return false;
		case ExcelConstants.IM103_IED_BOARD:
			ImportResult result = parser.getIEDBoardList(filePath, excelHeadRow, excelColInfo);
			return new ImportIEDBoardProcessor().processor(result.getFileInfoEntity(), 
					(List<IM103IEDBoardEntity>) result.getResult());
		case ExcelConstants.IM104_STATUS_IN:
			return false;
		case ExcelConstants.IM105_BOARD_WARN:
			return false;
		case ExcelConstants.IM106_PORT_LIGHT:
			return false;
		case ExcelConstants.IM107_TER_STRAP:
			return false;
		case ExcelConstants.IM108_BRK_CFM:
			return false;
		case ExcelConstants.IM109_STA_INFO:
			return false;
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

