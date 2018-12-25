package com.synet.tool.rsc.editor.tree;

import com.shrcn.found.ui.model.FieldBase;
import com.synet.tool.rsc.compare.Difference;

public class NewNameField extends FieldBase {

	public NewNameField() {
		super("新名称", 180);
	}
	
	@Override
	public String getTextValue(Object element) {
		Difference diff = (Difference)element;
		return diff.getNewName();
	}
}
