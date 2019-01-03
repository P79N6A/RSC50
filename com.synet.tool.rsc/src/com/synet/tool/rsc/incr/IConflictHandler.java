package com.synet.tool.rsc.incr;

import org.eclipse.core.runtime.IProgressMonitor;

public interface IConflictHandler {

	void handle();
	
	void handleAdd();
	
	void handleDelete();
	
	void handleUpate();
	
	void handleRename();
	
	void mergeDifference();
	
	void setMonitor(IProgressMonitor monitor);

	void setData();
}
