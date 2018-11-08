package com.synet.tool.rsc.das;

import com.shrcn.tool.found.das.DBManager;
import com.shrcn.tool.found.das.impl.BeanDaoImpl;
import com.shrcn.tool.found.das.impl.HqlDaoImpl;

public class SessionRsc extends SessionComponent{
	
	private static SessionRsc session;
	private static DBManager dbMgr = RscDbManagerImpl.getInstance();

	public SessionRsc() {
		super(dbMgr);
	}
	
	public static SessionRsc getInstance() {
		if(session == null) {
			session = new SessionRsc();
		}
		BeanDaoImpl.getInstance().setService(session);
		HqlDaoImpl.getInstance().setService(session);
		return session;
	}

	public String getTableName(Class<?> clazz) {
		return getConfiguration().getClassMapping(clazz.getName()).getTable().getName();
	}
}
