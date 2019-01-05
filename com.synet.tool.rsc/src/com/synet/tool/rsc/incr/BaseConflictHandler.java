package com.synet.tool.rsc.incr;

import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.IProgressMonitor;

import com.shrcn.tool.found.das.BeanDaoService;
import com.shrcn.tool.found.das.HqlDaoService;
import com.shrcn.tool.found.das.impl.BeanDaoImpl;
import com.shrcn.tool.found.das.impl.HqlDaoImpl;
import com.synet.tool.rsc.RSCProperties;
import com.synet.tool.rsc.compare.CompareUtil;
import com.synet.tool.rsc.compare.Difference;
import com.synet.tool.rsc.compare.OP;
import com.synet.tool.rsc.incr.scd.IEDConflictHandler;
import com.synet.tool.rsc.incr.ssd.BayConflictHandler;

public abstract class BaseConflictHandler implements IConflictHandler {

	protected IProgressMonitor monitor;
	protected Difference diff;
	protected BeanDaoService beanDao;
	protected HqlDaoService hqlDao;
	protected RSCProperties rscp;
	
	public BaseConflictHandler(Difference diff) {
		this.diff = diff;
		this.beanDao = BeanDaoImpl.getInstance();
		this.hqlDao = HqlDaoImpl.getInstance();
		this.rscp = RSCProperties.getInstance();
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
			case NONE:
				return;
		}
		setData();
		List<Difference> subDiffs = diff.getChildren();
		if (subDiffs != null && subDiffs.size() > 0) {
			for (Difference subDiff : subDiffs) {
				op = subDiff.getOp();
				IConflictHandler handler = ConflictHandlerFactory.getHandlerByType(subDiff);
				if (handler == null) {
					return;
				}
				if (needMonitor()) {
					monitor.setTaskName("正在" + op.getDesc() + subDiff.getType() + " " + subDiff.getName());
				}
				handler.setMonitor(monitor);
				handler.handle();
				if (needMonitor()) {
					monitor.worked(1);
				}
			}
		}
	}
	
	private boolean needMonitor() {
		return (monitor != null) && (monitor instanceof IEDConflictHandler
				|| monitor instanceof BayConflictHandler);
	}
	
	@Override
	public void setData() {
	}
	
	@Override
	public void mergeDifference() {
	}
	
	@Override
	public void handleRename() {
		mergeDifference();
		if (this.diff != null) {
			handleUpate();
		}
	}
	
	public Object getByName(Class<?> entityClass, String fname) {
		return beanDao.getObject(entityClass, fname, diff.getName());
	}
	
	public Map<String, String> getUpdateInfo() {
		String msg = diff.getMsg();
		Map<String, String> updateInfo = CompareUtil.getUpdateInfo(msg);
		updateInfo.put("name", diff.getName());
		updateInfo.put("newName", diff.getNewName());
		updateInfo.put("newDesc", diff.getNewDesc());
		return updateInfo;
	}
	
	public Map<String, String> getDisInfo() {
		String msg = diff.getMsg();
		Map<String, String> disInfo = CompareUtil.getDisInfo(msg);
		disInfo.put("name", diff.getName());
		return disInfo;
	}

	public void setMonitor(IProgressMonitor monitor) {
		this.monitor = monitor;
	}
	
}
