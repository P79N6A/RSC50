/**
 * Copyright (c) 2007, 2008 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based Visual Device Develop System.
 */
package com.shrcn.business.scl.ui;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.operation.ModalContext;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;

import com.shrcn.business.scl.table.VTViewTable;
import com.shrcn.business.scl.table.VTViewTableModel;
import com.shrcn.business.scl.util.SCLUtil;
import com.shrcn.found.ui.editor.BasicEditPart;


/**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2009-10-30
 */
/**
 * $Log: VTViewEditor.java,v $
 * Revision 1.7  2011/11/21 08:52:05  cchun
 * Update:清理引用
 *
 * Revision 1.6  2011/11/16 09:11:23  cchun
 * Update:为虚端子报表添加标题
 *
 * Revision 1.5  2011/08/28 06:28:20  cchun
 * Add:添加虚端子报表导出功能
 *
 * Revision 1.4  2010/08/10 06:52:26  cchun
 * Refactor:修改editor input类包路径
 *
 * Revision 1.3  2010/05/20 11:06:32  cchun
 * Update:修改表格菜单
 *
 * Revision 1.2  2010/03/09 08:46:07  cchun
 * Refactor:重构包路径
 *
 * Revision 1.1  2010/03/02 07:49:43  cchun
 * Add:添加重构代码
 *
 * Revision 1.6  2010/01/21 08:48:22  gj
 * Update:完成UI插件的国际化字符串资源提取
 *
 * Revision 1.5  2009/12/07 03:04:59  cchun
 * Update:添加信号关联全屏查看功能
 *
 * Revision 1.4  2009/11/19 08:28:54  cchun
 * Update:完成信号关联打印功能
 *
 * Revision 1.3  2009/11/17 06:00:00  cchun
 * Update:添加刷新功能
 *
 * Revision 1.2  2009/11/13 07:18:44  cchun
 * Update:完善关联表格功能
 *
 * Revision 1.1  2009/11/04 02:19:35  cchun
 * Add:虚端子查看表格
 *
 */
public class VTViewEditor extends BasicEditPart {

	public static final String ID = VTViewEditor.class.getName();
	private String iedName;
	private VTViewTableModel model = null;
	private VTViewTable table = null;
	
	@Override
	public void createPartControl(Composite parent) {
		final Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayout(new GridLayout(1, false));
		
		final Label lbTitle = new Label(composite, SWT.NONE);
		lbTitle.setText(iedName + "虚端子报表");
		lbTitle.setFont(new Font(null, "黑体", 20, SWT.BOLD));
		final GridData gridData = new GridData(SWT.CENTER, SWT.TOP, false, false);
		gridData.heightHint = 30;
		lbTitle.setLayoutData(gridData);
		
		model = new VTViewTableModel(iedName);
		table = new VTViewTable(composite, model);
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		SCLUtil.createVTViewMenu(table);
	}
	
	@Override
	public void init(IEditorSite site, IEditorInput input)
			throws PartInitException {
		super.init(site, input);
		SCTEditorInput in = (SCTEditorInput)input;
		this.iedName = in.getName();
	}
	
	/**
	 * 刷新表格
	 */
	public void refresh() {
		IProgressMonitor monitor = getEditorSite().getActionBars().getStatusLineManager().getProgressMonitor();
		try {
			monitor.beginTask("", 3); //$NON-NLS-1$
			monitor.subTask(Messages.getString("VTViewEditor.loading")); //$NON-NLS-1$
			monitor.worked(1);
			ModalContext.run(new IRunnableWithProgress() {
				public void run(IProgressMonitor monitor)
						throws InvocationTargetException, InterruptedException {
					model.refresh();
					ModalContext.checkCanceled(monitor);
				}
			}, true, new NullProgressMonitor(), Display.getCurrent());
		} catch (InvocationTargetException e) {
		} catch (InterruptedException e) {
		} finally {
			table.clearSelection();
			table.redraw();
			monitor.subTask(Messages.getString("VTViewEditor.finish")); //$NON-NLS-1$
			monitor.worked(1);
			monitor.done();
		}
	}
}