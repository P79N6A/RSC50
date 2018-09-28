package com.synet.tool.rsc.ui.table;

import org.eclipse.swt.graphics.Point;

import com.shrcn.found.ui.UIConstants;
import com.shrcn.found.ui.model.TableConfig;

import de.kupzog.ktable.KTableCellRenderer;
import de.kupzog.ktable.renderers.DefaultCellRenderer;

public class IEDBoardTableModel extends DevKTableModel {
	
	private static final int COL_MODEL = 1;
	private static final int COL_MAN = 2;
	private static final int COL_VER = 3;

	public IEDBoardTableModel(DevKTable table, TableConfig config) {
		super(table, config);
	}
	
	@Override
	public KTableCellRenderer doGetCellRenderer(int col, int row) {
		DefaultCellRenderer cellRenderer = (DefaultCellRenderer) super.doGetCellRenderer(col, row);
		if (COL_MODEL==col || COL_MAN==col || COL_VER==col) {
			cellRenderer.setBackground(UIConstants.ROW_ODD_BACK);
		}
		return cellRenderer;
	}
	
	@Override
	public Point doBelongsToCell(int col, int row) {
		if (row > 1) {
			if (COL_MODEL==col || COL_MAN==col || COL_VER==col) {
				String value1 = (String) doGetContentAt(COL_MODEL, row-1);
				String value2 = (String) doGetContentAt(COL_MODEL, row);
				if (value1.equals(value2)) {
					return new Point(col, row-1);
				}
			}
		}
		return super.doBelongsToCell(col, row);
	}

}
