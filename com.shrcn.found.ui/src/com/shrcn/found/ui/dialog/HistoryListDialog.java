/**
 * Copyright (c) 2007-2009 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based on IEC61850 SCT.
 */
package com.shrcn.found.ui.dialog;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Shell;

import com.shrcn.found.ui.app.WrappedDialog;
import com.shrcn.found.ui.util.SwtUtil;
import com.shrcn.found.ui.util.TaskManager;

/**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2010-3-28
 */
/**
 * $Log: HistoryListDialog.java,v $
 * Revision 1.2  2012/11/21 08:16:55  cchun
 * Update:调整布局
 *
 * Revision 1.1  2011/09/15 08:39:01  cchun
 * Refactor:创建公用对话框包
 *
 * Revision 1.3  2011/09/08 09:22:06  cchun
 * Fix Bug:修复取消按钮逻辑错误
 *
 * Revision 1.2  2010/09/02 03:40:20  cchun
 * Update:不存在历史项目时，删除按钮置灰
 *
 * Revision 1.1  2010/03/29 02:44:42  cchun
 * Update:提交
 *
 */
public abstract class HistoryListDialog extends WrappedDialog {
	
	protected List hisList;
	protected Shell shell = null;
	protected Button okbtn;
	protected Button nobtn;
	protected Button cancelbtn;
	protected String[] items;
	protected String value;
	protected String msg;
	
	/**
	 * Create the dialog
	 * @param parentShell
	 */
	public HistoryListDialog(Shell shell, String msg) {
		super(shell);
		this.shell = shell;
		this.msg = msg;
		this.items = getHistoryItems();
	}
	
	/**
	 * Create contents of the dialog
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);
		container.setLayout(SwtUtil.getGridLayout(1));

		final Group group = SwtUtil.createGroup(container, msg, new GridData(GridData.FILL_BOTH));
		group.setLayout(new FillLayout());

		hisList = new List(group, SWT.V_SCROLL | SWT.BORDER | SWT.FULL_SELECTION | SWT.MULTI);
		hisList.addMouseListener(new MouseAdapter() {
			public void mouseDoubleClick(final MouseEvent e) {
				buttonPressed(IDialogConstants.OK_ID);
			}
		});
		hisList.setItems(items);
		if (items.length > 0)
			hisList.setSelection(0);
		return container;
	}
	
	/**
	 * Create contents of the button bar
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		okbtn = createButton(parent, IDialogConstants.OK_ID, "确定", true);
		nobtn = createButton(parent, IDialogConstants.NO_ID, "删除", false);
		cancelbtn = createButton(parent, IDialogConstants.CANCEL_ID, "取消", false);
		if (hisList.getItemCount() < 1) {
			okbtn.setEnabled(false);
			nobtn.setEnabled(false);
		}
	}
	/**
	 * Return the initial size of the dialog
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(480, 410);
	}
	
	public String getValue() {
		return value; 
	}
	
	protected void buttonPressed(int buttonId) {
		if (buttonId == IDialogConstants.NO_ID) {
			handleDelete();
		}
		else if (buttonId == IDialogConstants.CANCEL_ID) {
			super.buttonPressed(buttonId);
		}
		else if (buttonId == IDialogConstants.OK_ID) {
			String[] db = hisList.getSelection();
			if (db.length == 0) {
				MessageDialog.openError(shell, Messages.getString("HistoryListDialog.warn"), Messages.getString("HistoryListDialog.please.open")); //$NON-NLS-1$ //$NON-NLS-2$
			}else{
				this.value = db[0];
				super.buttonPressed(buttonId);
			}
		}
	}

	/**
	 * 获取历史信息
	 * @return
	 */
	abstract protected String[] getHistoryItems();
	
	/**
	 * 执行数据库删除
	 * @param item
	 */
	abstract protected void deleteFromDB(String item);
	
	/**
	 * 删除按钮操作
	 */
	protected void handleDelete() {
		String[] dbs = hisList.getSelection();
		if (dbs.length == 0) {
			nobtn.setEnabled(false);
			return;
		}
		boolean confDel = MessageDialog.openConfirm(getShell(), Messages.getString("HistoryListDialog.confirm.del"), Messages.getString("HistoryListDialog.del.warn"));
		if (confDel){
			deleteSelectItems(dbs);
		}
	}
	
	/**
	 * @param dbs
	 */
	protected void deleteSelectItems(final String[] dbs) {
		getDialogArea().setCursor(new Cursor(shell.getDisplay(), SWT.CURSOR_WAIT));
		setEnable(false);
		TaskManager.addTask(new Job(Messages.getString("HistoryListDialog.deleting")) { //$NON-NLS-1$
			@Override
			protected IStatus run(IProgressMonitor monitor) {
				for (final String dbName : dbs) {
					deleteFromDB(dbName);
					Display.getDefault().asyncExec(new Runnable() {
						public void run() {
							int index = hisList.indexOf(dbName);
							hisList.remove(dbName);
							hisList.redraw();
							hisList.setSelection(index);
							getDialogArea().setCursor(new Cursor(shell.getDisplay(), SWT.CURSOR_ARROW));
							setEnable(true);
						}
					});
				}
				return Status.OK_STATUS;
			}
		});
	}

//	/**
//	 * 删除选择项
//	 * @param dbName
//	 */
//	private void deleteSelectItem(final String dbName){
//		getDialogArea().setCursor(new Cursor(shell.getDisplay(), SWT.CURSOR_WAIT));
//		setEnable(false);
//		TaskManager.addTask(new Job(Messages.getString("HistoryListDialog.deleting")) { //$NON-NLS-1$
//			@Override
//			protected IStatus run(IProgressMonitor monitor) {
//				deleteFromDB(dbName);
//				Display.getDefault().asyncExec(new Runnable() {
//					public void run() {
//						int index = hisList.indexOf(dbName);
//						hisList.remove(dbName);
//						hisList.redraw();
//						hisList.setSelection(index);
//						getDialogArea().setCursor(new Cursor(shell.getDisplay(), SWT.CURSOR_ARROW));
//						setEnable(true);
//					}
//				});
//				return Status.OK_STATUS;
//			}
//		});
//	}
	
	/**
	 * 设置按钮状态
	 * @param enable
	 */
	private void setEnable(boolean enable){
		okbtn.setEnabled(enable);
		nobtn.setEnabled(enable);
		cancelbtn.setEnabled(enable);
	}
}
