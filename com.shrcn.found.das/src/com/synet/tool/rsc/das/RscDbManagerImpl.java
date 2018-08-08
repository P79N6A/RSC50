package com.synet.tool.rsc.das;

import com.shrcn.tool.found.das.impl.ToolDBManagerImpl;

public class RscDbManagerImpl extends ToolDBManagerImpl {
	private static RscDbManagerImpl inst;
	
	private RscDbManagerImpl() {
		super();
	}
	
	public static RscDbManagerImpl getInstance() {
		if (inst == null) {
			inst = new RscDbManagerImpl();
		}
		return inst;
	}

	@Override
	protected void setProDbName() {
		prjDbName = "RscData";
		user = "rscapp";
	}
}
