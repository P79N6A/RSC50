package com.shrcn.found.ui.treetable;

public class FixedTreeTableAdapterFactory implements ITreeTableAdapterFactory{
	
	private ITreeTableAdapter adapter;

	public FixedTreeTableAdapterFactory(ITreeTableAdapter adapter){
		this.adapter=adapter;
	}

	public ITreeTableAdapter getAdapter(Object treeitemdata) {
		return adapter;
	}

}
