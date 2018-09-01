/**
 * Copyright (c) 2007-2017 泗业技术咨询有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application.
 */
package com.synet.tool.rsc.action.imp;

import java.util.HashMap;
import java.util.Map;

import com.synet.tool.rsc.ExcelConstants;
import com.synet.tool.rsc.dialog.ChooseTableColDialog;
import com.synet.tool.rsc.dialog.ChooseTableHeadDialog;


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
		ChooseTableHeadDialog headDialog = new ChooseTableHeadDialog(getShell());
		ChooseTableColDialog colDialog = new ChooseTableColDialog(getShell());
		if (headDialog.open() == 0) {
			Map<Integer, String> map = new HashMap<Integer, String>();
			map.put(1, "A");
			map.put(2, "B");
			map.put(3, "C");
			map.put(4, "D");
			map.put(5, "D");
			switch (getTitle()) {
			case ExcelConstants.IED_LIST:
				colDialog.setFieldList(ExcelConstants.IED_LIST_FIELDS);
				break;
			case ExcelConstants.FIBRE_LIST:
				colDialog.setFieldList(ExcelConstants.FIBRE_LIST_FIELDS);
				break;
			case ExcelConstants.IED_BOARD:
				colDialog.setFieldList(ExcelConstants.IED_BOARD_FIELDS);
				break;
			case ExcelConstants.STATUS_IN:
				colDialog.setFieldList(ExcelConstants.STATUS_IN_FIELDS);
				break;
			case ExcelConstants.BOARD_WARN:
				colDialog.setFieldList(ExcelConstants.BOARD_WARN_FIELDS);
				break;
			case ExcelConstants.PORT_LIGHT:
				colDialog.setFieldList(ExcelConstants.PORT_LIGHT_FIELDS);
				break;
			case ExcelConstants.TER_STRAP:
				colDialog.setFieldList(ExcelConstants.TER_STRAP_FIELDS);
				break;
			case ExcelConstants.BRK_CFM:
				colDialog.setFieldList(ExcelConstants.BRK_CFM_FIELDS);
				break;
			case ExcelConstants.STA_INFO:
				colDialog.setFieldList(ExcelConstants.STA_INFO_FIELDS);
				break;

			default:
				break;
			}
			colDialog.setExcelColMap(map);
			if (colDialog.open() == 0) {
				System.out.println(colDialog.getMap());
			}
		}
	}

}

