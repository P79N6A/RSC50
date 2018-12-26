package com.synet.tool.rsc.incr;

import java.util.List;

import com.shrcn.tool.found.das.BeanDaoService;
import com.shrcn.tool.found.das.HqlDaoService;
import com.shrcn.tool.found.das.impl.BeanDaoImpl;
import com.shrcn.tool.found.das.impl.HqlDaoImpl;
import com.synet.tool.rsc.compare.Difference;

public abstract class BaseIncreImportor implements IncreImportor {

	protected List<Difference> diffs;
	protected BeanDaoService beanDao;
	protected HqlDaoService hqlDao;
	
	public BaseIncreImportor(List<Difference> diffs) {
		this.diffs = diffs;
		this.beanDao = BeanDaoImpl.getInstance();
		this.hqlDao = HqlDaoImpl.getInstance();
	}
	
}