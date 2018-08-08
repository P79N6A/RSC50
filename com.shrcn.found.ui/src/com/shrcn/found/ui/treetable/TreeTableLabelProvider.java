package com.shrcn.found.ui.treetable;

import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

import com.shrcn.found.ui.model.IField;

/**
 * The label provider for asset content table
 * 
 * @author zhao yong
 */
public class TreeTableLabelProvider extends LabelProvider implements
		ITableLabelProvider {
 
	private IField[] fields;
 
	public TreeTableLabelProvider(IField[] fields) {
		this.fields = fields;
	}

	public Image getColumnImage(Object element, int columnIndex) {
		if (element == null || fields == null || columnIndex < 0
				|| columnIndex >= fields.length || fields[columnIndex] == null) {
			return null;
		}
		return fields[columnIndex].getImageValue(element);
	}

 
	public String getColumnText(Object element, int columnIndex) {
		// problem dto
		if (element == null || fields[columnIndex] == null || fields == null
				|| columnIndex < 0 || columnIndex > fields.length) {
			return "";
		}
		return fields[columnIndex].getTextValue(element);
	}
}
