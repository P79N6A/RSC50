package com.synet.tool.rsc.editor.tree;

import com.shrcn.found.ui.model.FieldBase;
import com.synet.tool.rsc.compare.Difference;

public class MsgField extends FieldBase {

	public MsgField() {
		super("修改内容", 380);
	}
	
	@Override
	public String getTextValue(Object element) {
		Difference diff = (Difference)element;
		return diff.getMsg();
	}
}
