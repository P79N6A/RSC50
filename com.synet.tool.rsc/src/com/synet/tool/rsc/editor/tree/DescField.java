package com.synet.tool.rsc.editor.tree;

import com.shrcn.found.ui.model.FieldBase;
import com.synet.tool.rsc.compare.Difference;

public class DescField extends FieldBase {

	public DescField() {
		super("描述", 360);
	}
	
	@Override
	public String getTextValue(Object element) {
		Difference diff = (Difference)element;
		return diff.getDesc();
	}
}
