package com.synet.tool.rsc.das;

import com.shrcn.tool.found.das.BeanDaoService;
import com.shrcn.tool.found.das.HqlDaoService;
import com.shrcn.tool.found.das.impl.BeanDaoImpl;
import com.shrcn.tool.found.das.impl.HqlDaoImpl;

public class ProjectDataDao {
	private static ProjectDataDao inst;
	
	public ProjectDataDao() {
		super();
	}

	public static ProjectDataDao getInstance() {
		if(inst == null) {
			inst = new ProjectDataDao();
		}
		return inst;
	}
	 
	private static BeanDaoService beanDao = BeanDaoImpl.getInstance();
	private static HqlDaoService hqlDao = HqlDaoImpl.getInstance();
	
}
