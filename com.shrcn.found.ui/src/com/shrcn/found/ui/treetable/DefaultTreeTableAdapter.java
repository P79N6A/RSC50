package com.shrcn.found.ui.treetable;

import org.eclipse.swt.graphics.Color;

public class DefaultTreeTableAdapter implements ITreeTableAdapter {

	public Color getBackgroundColor(Object element) {
		return null;
	}

	public Object[] getChildren(Object o) {
		return new Object[0];
	}

	public Object getParent(Object o) {
		return null;
	}

}
