package com.synet.tool.rsc.incr;

public interface IConflictHandler {

	void handle();
	
	void handleAdd();
	
	void handleDelete();
	
	void handleUpate();
	
	void handleRename();
}
