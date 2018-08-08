package com.shrcn.found.ui.treetable;

import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ViewForm;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

import com.shrcn.found.ui.model.IField;

public  class TreeTable {
	protected Composite parent;
	protected Tree tree;
	protected TreeTableViewer treeViewer;	
	protected ViewForm form;
	private ITreeTableAdapterFactory adapterFactory;
 
	public TreeTable(Composite parent, int style, IField[] fields,
			ITreeTableAdapterFactory adapterFactory) {
		this.parent = parent;
		initTable(parent, style);
		treeViewer = new TreeTableViewer(tree, fields, adapterFactory);
		this.adapterFactory = adapterFactory;
		form.setContent(tree);
	}
	 
	private void initTable( Composite parent  , int style) {
		form = new ViewForm(parent, style);
		form.setLayout(new FillLayout());
		form.setBorderVisible(false);
		form.marginHeight = 0;
		form.marginWidth = 0;

		GridData gd = new GridData(SWT.FILL, SWT.FILL, true, true);
		form.setLayoutData(gd);
		initTree(form, style);
	}
 
	private void initTree(Composite parent, int style) {
		tree = new Tree(parent, style);
		tree.setHeaderVisible(true);
		tree.setLinesVisible(true);
		tree.setEnabled(true);
		tree.setLayout(new TableLayout());
	}
	
	public void setInput(Object[] inputs){
		if (treeViewer == null)
			return;
		treeViewer.setTableInput(inputs);
		initColumnBGColorProperty();
	}
	
	private void initColumnBGColorProperty() {
		TreeItem[] items = getTree().getItems();
		for (int i = 0; i < items.length; i++) {
			TreeItem item = items[i];
			Color rowItemBGColr = adapterFactory.getAdapter(item.getData())
					.getBackgroundColor(item.getData());
			if (rowItemBGColr != null) {
				item.setBackground(rowItemBGColr);
			}
		}
	}
	 
	public Tree getTree() {
		return tree;
	}
	
	public TreeTableViewer getTreeViewer() {
		return treeViewer;
	}
	
	/**
	 * 获取当前选择项
	 * @return
	 */
	public Object getSelection() {
		StructuredSelection selection = (StructuredSelection)treeViewer.getSelection();
		return selection.getFirstElement();
	}
}
