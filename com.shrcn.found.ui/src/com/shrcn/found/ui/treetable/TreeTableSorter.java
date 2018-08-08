package com.shrcn.found.ui.treetable;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;

import com.shrcn.found.ui.model.IField;

public class TreeTableSorter extends ViewerSorter {

	public static final int ASCENDING = 1;

	public static final int DESCENDING = -1;

	private IField field;

	private int sortDirection = ASCENDING;

	public TreeTableSorter(IField field) {
		super();
		this.field = field;
	}

	public void setSortField(IField sortField) {
		if (this.field == sortField)
			sortDirection *= -1;
		else {
			sortDirection = ASCENDING;
			this.field = sortField;
		}
	}

	public IField getSortField() {
		return field;
	}

	public int getSortDirection() {
		return sortDirection;
	}

	public void setSortDirection(int sortDirection) {
		this.sortDirection = sortDirection;
	}

	public int category(Object element) {
		return super.category(element);
	}

	public int compare(Viewer viewer, Object e1, Object e2) {
		if (sortDirection == ASCENDING)
			return field.compare(e1, e2);
		return field.compare(e2, e1);
	}

	public boolean isSorterProperty(Object element, String property) {
		return super.isSorterProperty(element, property);
	}

	public void sort(Viewer viewer, Object[] elements) {
		super.sort(viewer, elements);
	}
}
