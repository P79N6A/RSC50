package com.synet.tool.rsc.editor.tree;

import com.shrcn.found.ui.model.FieldBase;
import com.synet.tool.rsc.compare.Difference;

public class NameField extends FieldBase {

	public NameField() {
		super("名称", 180);
	}
	
	@Override
	public String getTextValue(Object element) {
		Difference diff = (Difference)element;
		return diff.getName();
	}
}
