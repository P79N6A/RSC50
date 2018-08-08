package com.shrcn.found.ui.treetable;

import org.eclipse.swt.graphics.Color;

public interface ITreeTableAdapter {
	public Object[] getChildren(Object o);
	public Object getParent(Object o);
    public Color getBackgroundColor(Object element);   
	
}
