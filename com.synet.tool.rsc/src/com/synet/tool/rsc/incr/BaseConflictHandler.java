package com.synet.tool.rsc.incr;

import com.shrcn.tool.found.das.BeanDaoService;
import com.shrcn.tool.found.das.HqlDaoService;
import com.shrcn.tool.found.das.impl.BeanDaoImpl;
import com.shrcn.tool.found.das.impl.HqlDaoImpl;
import com.synet.tool.rsc.compare.Difference;
import com.synet.tool.rsc.compare.OP;

public abstract class BaseConflictHandler implements IConflictHandler {

	protected Difference diff;
	protected BeanDaoService beanDao;
	protected HqlDaoService hqlDao;
	
	public BaseConflictHandler(Difference diff) {
		this.diff = diff;
		this.beanDao = BeanDaoImpl.getInstance();
		this.hqlDao = HqlDaoImpl.getInstance();
	}

	@Override
	public void handle() {
		OP op = diff.getOp();
		switch (op) {
			case ADD:
				handleAdd();
				break;
			case DELETE:
				handleDelete();
				break;
			case UPDATE:
				handleUpate();
				break;
			case RENAME:
				handleRename();
				break;
		}
	}
}
