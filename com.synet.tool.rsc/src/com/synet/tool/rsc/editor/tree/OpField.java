package com.synet.tool.rsc.editor.tree;

import org.eclipse.swt.graphics.Image;

import com.shrcn.found.ui.model.FieldBase;
import com.synet.tool.rsc.compare.Difference;
import com.synet.tool.rsc.compare.OP;

public class OpField extends FieldBase {

	public OpField() {
		super("操作", 120);
	}
	
	@Override
	public Image getImageValue(Object element) {
		Difference diff = (Difference)element;
		OP op = diff.getOp();
		return op.getImage();
	}
	
	@Override
	public String getTextValue(Object element) {
		Difference diff = (Difference)element;
		OP op = diff.getOp();
		return op.getDesc();
	}
}
