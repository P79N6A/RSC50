/**
 * Copyright (c) 2007-2009 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based on IEC61850 SCT.
 */
package com.shrcn.business.scl.table.print;

import java.util.List;
import java.util.Map.Entry;

import org.eclipse.swt.graphics.Point;

import com.shrcn.business.scl.model.SignalRelation;
import com.shrcn.business.scl.table.VTTableField;
import com.shrcn.business.scl.table.VTViewTableModel;

/**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2009-11-19
 */
/**
 * $Log: VTViewPTableModel.java,v $
 * Revision 1.5  2013/08/05 07:58:38  cchun
 * Update:输出虚端子做合并单元格处理
 *
 * Revision 1.4  2011/02/24 08:11:27  cchun
 * Update:尝试合并一对多开出虚端子
 *
 * Revision 1.3  2010/09/27 07:13:40  cchun
 * Update:统一开入、开出信号关联对象
 *
 * Revision 1.2  2010/05/21 03:32:22  cchun
 * Update:完善“全屏”、“打印”列定制功能
 *
 * Revision 1.1  2010/03/02 07:49:40  cchun
 * Add:添加重构代码
 *
 * Revision 1.4  2010/01/21 08:47:53  gj
 * Update:完成UI插件的国际化字符串资源提取
 *
 * Revision 1.3  2009/11/20 01:55:52  cchun
 * Update:调整A3列宽
 *
 * Revision 1.2  2009/11/19 09:33:18  cchun
 * Update:增加纸型选择打印功能
 *
 * Revision 1.1  2009/11/19 08:28:54  cchun
 * Update:完成信号关联打印功能
 *
 */
public class VTViewPTableModel extends VTViewTableModel implements PrintKTableModel {

	public VTViewPTableModel(String iedName, VTTableField[] fields, List<Entry<String, List<SignalRelation>>> inputs,
			List<Entry<String, List<SignalRelation>>> outputs) {
		this.iedName = iedName;
		this.fields = fields;
		this.inputs = inputs;
		this.outputs = outputs;
		init();
	}
	
	@Override
	protected void init() {
		inSize = getTotalSize(inputs);
		outSize = getTotalSize(outputs);
		inStartIndexs = getStartIndexs(inputs);
		outStartIndexs = getStartIndexs(outputs);
		itemSize = Math.max(inSize, outSize);
	}
	
	@Override
	public Point doBelongsToCell(int col, int row) {
		int iedNameNewIdx = getValueIndex(IDX_IEDNAME);
		if(col == iedNameNewIdx && iedNameNewIdx > -1) {
			if(row > 0 && row <= doGetRowCount())
				return new Point(iedNameNewIdx, 1);
		}
		if (isOutAttCol(col) && row > 0) {
			Object nextValue = getContentAt(col, row + 1);
			Object value = getContentAt(col, row);
			if (value.equals(nextValue))
				return new Point(col, row);
		} // 暂不合并一对多的输出端子信息
		return null;
	}
	
	@Override
	public int getInitialColumnWidth(int column) {
		column = fields[column].getValNum();
		int width = 80;
		if(PageSetup.format.equals("A4")) { //$NON-NLS-1$
			switch(column) {
				case IDX_SEQUENCE: 
					width = 22; 
					break;
				case IDX_OUT_IEDNAME:
					width = 60; 
					break;
				case IDX_OUT_DESC:
					width = 60; 
					break;
				case IDX_OUT_REF:
					width = 120; 
					break;
				case IDX_INLINE:
					width = 22; 
					break;
				case IDX_IN_INDEX:
					width = 30; 
					break;
				case IDX_TIN_REF_DESC:
					width = 60; 
					break;
				case IDX_TIN_REF:
					width = 120; 
					break;
				case IDX_IEDNAME:
					width = 60; 
					break;
				case IDX_TOUT_DESC:
					width = 60; 
					break;
				case IDX_TOUT_REF:
					width = 90; 
					break;
				case IDX_OUT_INDEX:
					width = 30; 
					break;
				case IDX_OUTLINE:
					width = 22; 
					break;
				case IDX_IN_IEDNAME:
					width = 60; 
					break;
				case IDX_IN_REF: 
					width = 100; 
					break;
			}
		} else {
			switch(column) {
			case IDX_SEQUENCE: 
				width = 25; 
				break;
			case IDX_OUT_IEDNAME:
				width = 75; 
				break;
			case IDX_OUT_DESC:
				width = 90; 
				break;
			case IDX_OUT_REF:
				width = 150; 
				break;
			case IDX_INLINE:
				width = 40; 
				break;
			case IDX_IN_INDEX:
				width = 75; 
				break;
			case IDX_TIN_REF_DESC:
				width = 90; 
				break;
			case IDX_TIN_REF:
				width = 130; 
				break;
			case IDX_IEDNAME:
				width = 90; 
				break;
			case IDX_TOUT_DESC:
				width = 90; 
				break;
			case IDX_TOUT_REF:
				width = 130; 
				break;
			case IDX_OUT_INDEX:
				width = 75; 
				break;
			case IDX_OUTLINE:
				width = 40; 
				break;
			case IDX_IN_IEDNAME:
				width = 75; 
				break;
			case IDX_IN_REF: 
				width = 130; 
				break;
			}
		}
		return width;
	}

	public int getRowHeight() {
		return 12;
	}

	public int getFirstRowHeight() {
		return 12;
	}
}
