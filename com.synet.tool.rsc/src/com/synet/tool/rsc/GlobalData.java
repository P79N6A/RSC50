package com.synet.tool.rsc;

import com.synet.tool.rsc.jdbc.ConnParam;

public class GlobalData {
	
	private static GlobalData instance = new GlobalData();
	//缓存上次使用的数据库连接参数
	private ConnParam connParam;
	
	public static GlobalData getInstance() {
		if (instance == null)
			instance = new GlobalData();
		return instance;
	}
	
	private GlobalData() {
		 connParam = new ConnParam();
	     connParam.setIp("localhost");
		 connParam.setPort("1521");
		 connParam.setDbName("ORCL");
		 connParam.setUser("RSC");
		 connParam.setPassword("rsc123456");
	}

	public ConnParam getConnParam() {
		return connParam;
	}

	public void setConnParam(ConnParam connParam) {
		this.connParam = connParam;
	}

}
