/**
 * Copyright (c) 2007-2009 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based on IEC61850 SCT.
 */
package com.shrcn.business.scl.table.print;


/**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2009-11-18
 */
/**
 * $Log: PTable.java,v $
 * Revision 1.1  2010/03/02 07:49:15  cchun
 * Add:添加重构代码
 *
 * Revision 1.2  2010/01/21 08:48:18  gj
 * Update:完成UI插件的国际化字符串资源提取
 *
 * Revision 1.1  2009/11/19 08:28:52  cchun
 * Update:完成信号关联打印功能
 *
 */
public class PTable {

	protected PrintKTableModel model;

	protected PTableBoxProvider boxProvider;

	protected PContainer parent;

	public PTable(PContainer parent) {
		this.parent = parent;
	}

	protected void fillDocument() {
		boolean abgeschnitten = false;
		// Zeilen
		for (int i = 0; i < model.getRowCount(); i++) {
			// System.out.println("Spalte "+i);
			int height = model.getRowHeight();
			if (i == 0)
				height = model.getFirstRowHeight();

			double width = parent.getPossibleWidth();

			// Spalten
			for (int j = 0; j < model.getColumnCount(); j++) {
				// System.out.println(" Zeile "+j);
				int style = PBox.POS_RIGHT | PBox.ROW_ALIGN;
				if (j == 0)
					style = PBox.POS_BELOW | PBox.ROW_ALIGN;

				PBox box = boxProvider.createBox(parent, style, j, i, model
						.getColumnWidth(j), height,
						(model.getFixedColumnCount() > j || model
								.getFixedRowCount() > i), model.getContentAt(j,
								i));
				double boxWidth = Math.max(box.minCm, parent.getPossibleWidth()
						* box.hWeight);
				width -= boxWidth;
				if (width < 0) {
					box.dispose();
					abgeschnitten = true;
					break;
				}
			}
		}
		if (abgeschnitten)
			MsgBox.show("Tabelle ist zu breit fur die Seite\n" //$NON-NLS-1$
					+ "und wird deshalb abgeschnitten."); //$NON-NLS-1$

	}

	/**
	 * @return PTableBoxProvider
	 */
	public PTableBoxProvider getBoxProvider() {
		return boxProvider;
	}

	/**
	 * @return PrintKTableModel
	 */
	public PrintKTableModel getModel() {
		return model;
	}

	/**
	 * Sets the boxProvider.
	 * 
	 * @param boxProvider
	 *            The boxProvider to set
	 */
	public void setBoxProvider(PTableBoxProvider boxProvider) {
		this.boxProvider = boxProvider;
		if (this.boxProvider != null && this.model != null) {
			fillDocument();
		}
	}

	/**
	 * Sets the model.
	 * 
	 * @param model
	 *            The model to set
	 */
	public void setModel(PrintKTableModel model) {
		this.model = model;
		if (this.boxProvider != null && this.model != null) {
			fillDocument();
		}
	}

}