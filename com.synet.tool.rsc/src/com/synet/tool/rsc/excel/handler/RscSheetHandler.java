package com.synet.tool.rsc.excel.handler;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.shrcn.found.file.excel.SheetsHandler;
import com.shrcn.found.ui.view.ConsoleManager;
import com.synet.tool.rsc.RSCProperties;
import com.synet.tool.rsc.service.DefaultService;

public abstract class RscSheetHandler extends SheetsHandler {

	protected DefaultService service = new DefaultService();
	protected List<String> errorMsg;
	protected int headRowNum;
	//excel表格列-字段映射
	protected Map<Integer, String> excelColInfo;
	protected RSCProperties rscp = RSCProperties.getInstance();
	protected ConsoleManager console = ConsoleManager.getInstance();
	
	public RscSheetHandler(int headRowNum, Map<Integer, String> excelColInfo) {
		super();
		this.headRowNum = headRowNum;
		this.excelColInfo = excelColInfo;
		this.result = new ArrayList<>();
		this.errorMsg = new ArrayList<>();
	}

	public RscSheetHandler() {
		super();
	}

	public DefaultService getService() {
		return service;
	}

	public void setService(DefaultService service) {
		this.service = service;
	}

	public List<String> getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(List<String> errorMsg) {
		this.errorMsg = errorMsg;
	}

	public Map<Integer, String> getExcelColInfo() {
		return excelColInfo;
	}

	public void setExcelColInfo(Map<Integer, String> excelColInfo) {
		this.excelColInfo = excelColInfo;
	}
	
}
