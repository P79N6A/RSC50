package com.synet.tool.rsc.editor.handler;

import com.shrcn.found.ui.view.Problem;

public interface IEditorImpHandler {

	void loadFileItems(String filename);
	
	void exportExcel();
	
	void checkConflict();
	
	void doImport();

	Object locate(Problem problem);
	
}
