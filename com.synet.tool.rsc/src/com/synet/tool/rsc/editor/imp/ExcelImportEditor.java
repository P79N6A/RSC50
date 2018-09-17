package com.synet.tool.rsc.editor.imp;

import java.util.Map;

import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

import com.shrcn.found.ui.editor.IEditorInput;
import com.synet.tool.rsc.editor.BaseConfigEditor;
import com.synet.tool.rsc.model.IM100FileInfoEntity;
import com.synet.tool.rsc.service.ImprotInfoService;

public class ExcelImportEditor extends BaseConfigEditor {

	protected ImprotInfoService improtInfoService;
	protected Map<String, IM100FileInfoEntity> map;
	protected org.eclipse.swt.widgets.List titleList;
	protected Button btImport;

	public ExcelImportEditor(Composite container, IEditorInput input) {
		super(container, input);
	}

	@Override
	public void refresh() {
		initData();
	}
}