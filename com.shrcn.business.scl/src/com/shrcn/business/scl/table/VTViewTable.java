/**
 * Copyright (c) 2007, 2008 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based Visual Device Develop System.
 */
package com.shrcn.business.scl.table;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.operation.ModalContext;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

import com.shrcn.business.scl.table.print.FullScreenTable;
import com.shrcn.business.scl.table.print.FullScreenView;
import com.shrcn.business.scl.table.print.PDocument;
import com.shrcn.business.scl.table.print.PTableBoxProvider;
import com.shrcn.business.scl.table.print.PageSetup;
import com.shrcn.business.scl.table.print.PrintPreview;
import com.shrcn.business.scl.table.print.PrintTypeDialog;
import com.shrcn.business.scl.table.print.VTViewPTable;
import com.shrcn.business.scl.table.print.VTViewPTableModel;
import com.shrcn.business.scl.ui.Messages;
import com.shrcn.found.ui.table.DefaultKTable;
import com.shrcn.found.ui.table.DefaultKTableModel;
import com.shrcn.found.ui.util.ImageConstants;
import com.shrcn.found.ui.util.ImgDescManager;

/**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2009-10-30
 */
/**
 * $Log: VTViewTable.java,v $
 * Revision 1.5  2011/11/24 11:45:52  cchun
 * Refactor:使用统一的基础类
 *
 * Revision 1.4  2010/12/15 00:59:26  cchun
 * Update:消除警告
 *
 * Revision 1.3  2010/11/12 09:21:10  cchun
 * Refactor:修改IconManager类名为ImgDescManager，并移动至common项目
 *
 * Revision 1.2  2010/05/21 03:32:22  cchun
 * Update:完善“全屏”、“打印”列定制功能
 *
 * Revision 1.1  2010/03/02 07:49:40  cchun
 * Add:添加重构代码
 *
 * Revision 1.5  2010/01/21 08:47:54  gj
 * Update:完成UI插件的国际化字符串资源提取
 *
 * Revision 1.4  2009/12/07 03:04:58  cchun
 * Update:添加信号关联全屏查看功能
 *
 * Revision 1.3  2009/11/19 09:33:18  cchun
 * Update:增加纸型选择打印功能
 *
 * Revision 1.2  2009/11/19 08:28:54  cchun
 * Update:完成信号关联打印功能
 *
 * Revision 1.1  2009/11/04 02:19:35  cchun
 * Add:虚端子查看表格
 *
 */
public class VTViewTable extends DefaultKTable {
	
	public VTViewTable(Composite parent, DefaultKTableModel model) {
		super(parent, model);
		setStyle(SWT.FULL_SELECTION|SWT.V_SCROLL | SWT.H_SCROLL|SWT.MULTI);
	}
	
	/**
	 * 执行打印
	 */
	public void print() {
//		PageSetup.format = "A4";
//		PageSetup.rowCount = 29;
//		PageSetup.marginStyle = PageSetup.MARGIN_SMALL;
		PageSetup.format = "A3"; //$NON-NLS-1$
		PageSetup.rowCount = 62;
		PageSetup.marginStyle = PageSetup.MARGIN_SMALL;
		PrintTypeDialog dialog = new PrintTypeDialog(getShell());
		if(dialog.open() == PrintTypeDialog.CANCEL)
			return;
		
		PageSetup.paperHeight = PageSetup.getPaperWidthInCm(PageSetup.format);
		PageSetup.paperWidth = PageSetup.getPaperHeightInCm(PageSetup.format);
		PageSetup.isTable = true;
		VTViewTableModel vModel = (VTViewTableModel)model;
		PDocument doc = new PDocument(vModel.getIedName());
		VTViewPTable table = new VTViewPTable(doc);
		table.setModel(new VTViewPTableModel(vModel.getIedName(), vModel.getFields(), vModel.getInputs(),
				vModel.getOutputs()));
		table.setBoxProvider(new PTableBoxProvider());
		
		PrintPreview pr = new PrintPreview(null, Messages.getString("VTViewTable.preview"),  //$NON-NLS-1$
				ImgDescManager.getImageDesc(ImageConstants.PRINT).createImage(), doc);
		pr.open();
	}
	
	/**
	 * 全屏显示
	 */
	public void fullScreen() {
		PageSetup.format = "A3"; //$NON-NLS-1$
		PageSetup.rowCount = 62;
		PageSetup.marginStyle = PageSetup.MARGIN_SMALL;
		
		PageSetup.paperHeight = PageSetup.getPaperWidthInCm(PageSetup.format);
		PageSetup.paperWidth = PageSetup.getPaperHeightInCm(PageSetup.format);
		PageSetup.isTable = true;
		VTViewTableModel vModel = (VTViewTableModel)model;
		PDocument doc = new PDocument(vModel.getIedName());
		FullScreenTable table = new FullScreenTable(doc);
		table.setModel(new VTViewPTableModel(vModel.getIedName(), vModel.getFields(), vModel.getInputs(),
				vModel.getOutputs()));
		table.setBoxProvider(new PTableBoxProvider());
		
		FullScreenView pr = new FullScreenView(null, Messages.getString("VTViewTable.fullscreen"),  //$NON-NLS-1$
				ImgDescManager.getImageDesc(ImageConstants.PRINT).createImage(), doc);
		
		pr.open();
	}
	
	public void refreshTB() {
		try {
			ModalContext.run(new IRunnableWithProgress() {
				public void run(IProgressMonitor monitor)
						throws InvocationTargetException, InterruptedException {
					((VTViewTableModel)model).refresh();
					ModalContext.checkCanceled(monitor);
				}
			}, true, new NullProgressMonitor(), Display.getCurrent());
		} catch (InvocationTargetException e) {
		} catch (InterruptedException e) {
		} finally {
			clearSelection();
			redraw();
		}
	}
}
