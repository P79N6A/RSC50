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

public class RKPrintTable extends PTable {

	public RKPrintTable(PContainer parent) {
		super(parent);
		PDocument doc = (PDocument)parent;
		PContainer footer1 = doc.getFirstFooter();
		PContainer footer = doc.getFooter();
		footer1.addChild(new PPageNumber(footer1, PBox.POS_BELOW | PBox.ROW_ALIGN));
		footer.addChild(new PPageNumber(footer, PBox.POS_BELOW | PBox.ROW_ALIGN));
	}
	
	@Override
	protected void fillDocument() {
		boolean abgeschnitten = false;
	    // 行
	    for (int i = 0; i < model.getRowCount(); i++) {
//	      System.out.println("行 "+i);
	      int height = model.getRowHeight();
	      if (i == 0)
	        height = model.getFirstRowHeight();

	      double width = parent.getPossibleWidth();

	      // 列
	      for (int j = 0; j < model.getColumnCount(); j++) {
//	         System.out.println("列 "+j);
	        int style = PBox.POS_RIGHT | PBox.ROW_ALIGN;
	        if (j == 0)
	          style = PBox.POS_BELOW | PBox.ROW_ALIGN;

	        PBox box = boxProvider.createBox(parent, style, j, i, 
	        		model.getColumnWidth(j), height,
	        		(model.getFixedColumnCount() > j || model.getFixedRowCount() > i), 
	        		model.getContentAt(j, i));
	        parent.addChild(box);
	        
	        //设置页头
//	        if(i==0) {
//	        	PDocument doc = (PDocument)parent;
//				doc.getHeader().addChild(box);
//				//这里重新创建一个对象是因为同一个box不能重复打印
//				PBox headerBox = boxProvider.createBox(parent, style, j, i, model
//	    	            .getColumnWidth(j), height,
//	    	            (model.getFixedColumnCount() > j || model.getFixedRowCount() > i), 
//	    	            model.getContentAt(j, i));
//	        	doc.getFirstHeader().addChild(headerBox);
//	        	((PLittleTextBox)box).getTextStyle().textAlign = PTextStyle.ALIGN_CENTER;
//	        	((PLittleTextBox)headerBox).getTextStyle().textAlign = PTextStyle.ALIGN_CENTER;
//	        }
	        
	        //纠正左右间隔线过粗
	        PStyle boxStyle = box.getBoxStyle();
	        if(i != 0)
	        	boxStyle.lines[0] = -1;
	        PLittleTextBox textbox = (PLittleTextBox)box;
	        PTextStyle textStyle = textbox.getTextStyle();
	        
	        //处理合并单元格
//	        Point belongsTo = model.belongsToCell(j, i);
//	        if (belongsTo!=null) {
//	        	if(i == 0)
//	        		boxStyle.lines = new double[] { 0.02, 0.01, -1, 0.0 };
//	        	else if(i == (model.getRowCount()-1))
//	        		boxStyle.lines = new double[] { -1, 0.01, 0.005, 0.0 };
//	        	else
//	        		boxStyle.lines = new double[] { -1, 0.01, -1, 0.0 };
//	        	textbox.setText(""); //$NON-NLS-1$
//	        	
//	        	int pageRows = PageSetup.rowCount;
//				int mod = model.getRowCount() % pageRows;
//	        	int startIdx = pageRows * (i / pageRows);
//	        	int endIdx = 0;
//	        	if(model.getRowCount() - startIdx < pageRows)
//	        		endIdx = startIdx + mod;
//	        	else
//	        		endIdx = startIdx + pageRows;
//	        	
//	        	if(i == (startIdx + endIdx) / 2) {
//	        		textStyle.textAlign = PTextStyle.ALIGN_CENTER;
//	        		textStyle.fontStyle = SWT.BOLD;
//	        		textStyle.fontSize = 12;
//	        		textbox.setText(model.getContentAt(belongsTo.x, belongsTo.y).toString());
//	        	} else {
//	        		textbox.setText(""); //$NON-NLS-1$
//	        	}
//	        }
	        
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

}