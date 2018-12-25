package com.synet.tool.rsc.editor.tree;

import com.shrcn.found.ui.treetable.DefaultTreeTableAdapter;
import com.synet.tool.rsc.compare.Difference;

public class DiffTreeTableAdapter extends DefaultTreeTableAdapter {
	public static DiffTreeTableAdapter instance=new DiffTreeTableAdapter();

	private DiffTreeTableAdapter() {
	}

	public Object[] getChildren(Object parentElement) {
		if (parentElement instanceof Difference
				&& ((Difference) parentElement).getChildren() != null)
			return ((Difference) parentElement).getChildren().toArray();
		else
			return new Object[0];
	}
} 
