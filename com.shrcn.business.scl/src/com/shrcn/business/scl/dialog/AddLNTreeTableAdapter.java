package com.shrcn.business.scl.dialog;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;

import com.shrcn.business.xml.schema.LnClass;
import com.shrcn.found.ui.treetable.DefaultTreeTableAdapter;

public class AddLNTreeTableAdapter extends DefaultTreeTableAdapter {
	public static AddLNTreeTableAdapter instance = new AddLNTreeTableAdapter();

	private AddLNTreeTableAdapter() {
	}

	public Object[] getChildren(Object parentElement) {
		if (parentElement instanceof LnClass
				&& ((LnClass) parentElement).getChildren() != null
				&& parentElement != null)
			return ((LnClass) parentElement).getChildren().toArray();
		else
			return new Object[0];

	}

	public Color getBackgroundColor(Object parentElement) {
		if ((((LnClass) parentElement).getChildren().size() > 0)
				&& (((LnClass) parentElement).getNum() > 0))
			return Display.getDefault().getSystemColor(SWT.COLOR_RED);
		else
			return null;
	}
}