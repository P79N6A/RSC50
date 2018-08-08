package com.shrcn.found.ui.treetable;

import org.eclipse.jface.viewers.CheckboxTreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;

import com.shrcn.found.ui.model.IField;

public class TreeTableViewer extends CheckboxTreeViewer {

	private IField[] fields;

	public TreeTableViewer(Tree tree, IField[] fields, ITreeTableAdapterFactory adapterFactory) {
		super(tree);
		this.fields =fields;
		super.setContentProvider(new TreeTableContentProvider(adapterFactory));
		super.setLabelProvider(new TreeTableLabelProvider(fields));
		createColumns();
		initSorters();
	}

	//must be an array
	public void setTableInput(Object[] input) {
		super.setInput(input);
	}

	protected void createColumns() {
		IField[] fields = getFields();
		for (int i = 0; i < fields.length; i++) {
			if (fields[i] != null) {
				TreeColumn tc = new TreeColumn(getTree(), SWT.NONE);
				tc.setText(fields[i].getTitle());
				tc.setWidth(fields[i].getWidth());
				tc.setData(fields[i]);
	        }
		}
	}

	public IField[] getFields() {
		return fields;
	}
	
	private TreeTableSorter tableSorter;

	protected void initSorters() {
		if (fields.length > 0) {
			tableSorter = new TreeTableSorter(fields[0]);
			setSorter(tableSorter);
		}
		TreeColumn[] columns = getTree().getColumns();
		columns[0].addSelectionListener(new RowSelectionListener());
	}

	private class RowSelectionListener extends SelectionAdapter {

		public void widgetSelected(SelectionEvent e) {
			TreeColumn column = (TreeColumn) e.widget;
			IField sortField = (IField) column.getData();

			Tree tree = column.getParent();
			tree.setSortColumn(column);

			tableSorter.setSortField(sortField);

			int direction = tableSorter.getSortDirection();
			if (direction == TreeTableSorter.ASCENDING)
				tree.setSortDirection(SWT.UP);
			else
				tree.setSortDirection(SWT.DOWN);

			refresh();
		}
	}
}
