package com.synet.tool.rsc.excel.handler;

import java.util.ArrayList;
import java.util.List;

import com.shrcn.found.file.excel.SheetsHandler;
import com.synet.tool.rsc.service.DefaultService;

public abstract class RscSheetHandler extends SheetsHandler {

	protected DefaultService service = new DefaultService();
	protected List<String> errorMsg;
	
	public RscSheetHandler() {
		super();
		this.result = new ArrayList<>();
		this.errorMsg = new ArrayList<>();
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
	
	
}
