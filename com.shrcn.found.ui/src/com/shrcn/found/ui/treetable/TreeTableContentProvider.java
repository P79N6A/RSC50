package com.shrcn.found.ui.treetable;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;


public class TreeTableContentProvider extends ArrayContentProvider implements
		ITreeContentProvider {

	private ITreeTableAdapterFactory adapterfactory;

	public TreeTableContentProvider(ITreeTableAdapterFactory adapterfactory) {
		this.adapterfactory = adapterfactory;
	}

	public Object[] getChildren(Object parentElement) {
		return ((ITreeTableAdapter) adapterfactory.getAdapter(parentElement))
				.getChildren(parentElement);
	}

	public Object getParent(Object element) {
		return ((ITreeTableAdapter) adapterfactory.getAdapter(element))
				.getParent(element);
	}

	public boolean hasChildren(Object element) {
		return getChildren(element).length > 0;
	}
}
