package com.synet.tool.rsc.excel;

import java.util.List;

import com.synet.tool.rsc.RSCProperties;
import com.synet.tool.rsc.excel.handler.RegionHandler;
import com.synet.tool.rsc.excel.handler.RscSheetHandler;
import com.synet.tool.rsc.excel.handler.SecFibreListHandler;
import com.synet.tool.rsc.excel.handler.SecLockHandler;
import com.synet.tool.rsc.excel.handler.SecProBrkHandler;
import com.synet.tool.rsc.excel.handler.SecPwrBrkHandler;
import com.synet.tool.rsc.model.Tb1049RegionEntity;
import com.synet.tool.rsc.model.Tb1090LineprotfiberEntity;
import com.synet.tool.rsc.model.Tb1091IotermEntity;
import com.synet.tool.rsc.model.Tb1092PowerkkEntity;
import com.synet.tool.rsc.model.Tb1093VoltagekkEntity;
import com.synet.tool.rsc.util.ExcelReaderUtil;

public class ImportInfoParser {
	
	private RscSheetHandler handler;
	protected RSCProperties rscp = RSCProperties.getInstance();
	
	@SuppressWarnings("unchecked")
	public List<Tb1090LineprotfiberEntity> getLineprotfiberList(String xlspath) {
		this.handler = new SecFibreListHandler(0);
		return (List<Tb1090LineprotfiberEntity>) ExcelReaderUtil.parseByHandler(xlspath, handler);
	}
	
	@SuppressWarnings("unchecked")
	public List<Tb1091IotermEntity> getIotermList(String xlspath) {
		this.handler = new SecLockHandler();
		return (List<Tb1091IotermEntity>) ExcelReaderUtil.parseByHandler(xlspath, handler);
	}
	
	@SuppressWarnings("unchecked")
	public List<Tb1092PowerkkEntity> getPowerkkList(String xlspath) {
		this.handler = new SecPwrBrkHandler();
		return (List<Tb1092PowerkkEntity>) ExcelReaderUtil.parseByHandler(xlspath, handler);
	}
	
	@SuppressWarnings("unchecked")
	public List<Tb1093VoltagekkEntity> getVoltagekkList(String xlspath) {
		this.handler = new SecProBrkHandler();
		return (List<Tb1093VoltagekkEntity>) ExcelReaderUtil.parseByHandler(xlspath, handler);
	}
	
	@SuppressWarnings("unchecked")
	public List<Tb1049RegionEntity> getRegionList(String xlspath) {
		this.handler = new RegionHandler();
		return (List<Tb1049RegionEntity>) ExcelReaderUtil.parseByHandler(xlspath, handler);
	}
	
}

