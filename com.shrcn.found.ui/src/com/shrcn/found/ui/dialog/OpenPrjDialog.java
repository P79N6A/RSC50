/**
 * Copyright (c) 2007-2010 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based on IEC61850 SCT.
 */
package com.shrcn.found.ui.dialog;

import java.io.File;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.shrcn.found.common.Constants;
import com.shrcn.found.common.util.StringUtil;
import com.shrcn.found.ui.app.WrappedDialog;
import com.shrcn.found.ui.util.DialogHelper;
import com.shrcn.found.ui.util.SwtUtil;

/**
 * 
 * @author 孙春颖(scy@shrcn.com)
 * @version 1.0, 2013 4 11
 */
public class OpenPrjDialog extends WrappedDialog {
	
	private CTabFolder folder;
	protected List nameList;
	protected Label descLbl;
	private Text pathTxt;
	private Button selBtn;
	
	private Button okBtn;
	
	private String name;
	private String path;
	protected String oldName;
	private String oldPath;
	
	public OpenPrjDialog(Shell parentShell) {
		super(parentShell);
		oldName = Constants.CURRENT_PRJ_NAME;
		oldPath = Constants.CURRENT_PRJ_PATH;
	}

	/**
	 * 初始化数据.
	 */
	protected void initValue() {
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);
		container.setLayout(new GridLayout(1, false));
		
		folder = SwtUtil.createTabFolder(container, SWT.NONE);
		folder.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		
		// 默认路径分页
		final Composite defaultCmp = new Composite(folder, SWT.NONE);
		defaultCmp.setLayout(new GridLayout(2, false));
		SwtUtil.addTabItem(folder, "默认路径", defaultCmp);
		
		Composite selCmp = new Composite(defaultCmp, SWT.NONE);
		selCmp.setLayout(new GridLayout(1, false));
		GridData gridData = new GridData(GridData.FILL_BOTH);
		gridData.widthHint = 150;
		selCmp.setLayoutData(gridData);
		
		SwtUtil.createLabel(selCmp, "名称：", new GridData(GridData.FILL, GridData.BEGINNING, false, false));
		nameList = SwtUtil.createList(selCmp, new GridData(GridData.FILL_BOTH));
		
		Group grp = SwtUtil.createGroup(defaultCmp, "描述", new GridData(GridData.FILL_BOTH));
		grp.setLayout(new GridLayout(1, false));
		descLbl = SwtUtil.createLabel(grp, "", new GridData(GridData.FILL_BOTH));

		// 指定路径分页
		final Composite appointCmp = new Composite(folder, SWT.NONE);
		GridLayout cmpLayout = new GridLayout(3, false);
		cmpLayout.marginLeft = 5;
		cmpLayout.marginRight = 5;
		cmpLayout.marginTop = 70;
		appointCmp.setLayout(cmpLayout);
		SwtUtil.addTabItem(folder, "指定路径", appointCmp);

		GridData lblGd = new GridData(GridData.END, GridData.CENTER, false, false);
		GridData txtGd = new GridData(GridData.FILL, GridData.CENTER, true, false);
		SwtUtil.createLabel(appointCmp, "路径：", lblGd);
		pathTxt = SwtUtil.createText(appointCmp, SWT.BORDER | SWT.READ_ONLY, "", txtGd);
		selBtn = SwtUtil.createButton(appointCmp, new GridData(GridData.END, GridData.CENTER, false, false), SWT.PUSH, "...");
    	
		addListener();
		initValue();
		return container;
	}
	
	/**
	 * 添加监听器
	 */
	private void addListener() {
		folder.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				updateOkBtnEnable();
			}
		});
		nameList.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				selName();
				updateOkBtnEnable();
			}
		});
		nameList.addMouseListener(new MouseAdapter() {
			public void mouseDown(MouseEvent e) {
				if (nameList.getSelectionCount() > 0 && e.button == 3){
					createMenu();
				}
			}
			@Override
			public void mouseDoubleClick(MouseEvent e) {
				okPressed();
			}
		});
		selBtn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				final String fileName = DialogHelper.selectDirectory(getShell(), "");
				if (!StringUtil.isEmpty(fileName))
					pathTxt.setText(fileName);
				updateOkBtnEnable();
			}
		});
	}

	protected void selName() {
	}
	
	/**
	 * 创建右键菜单.
	 * 
	 * @param table
	 *            当前选择表格.
	 */
	protected void createMenu(){
		final Menu menu = new Menu(nameList);
		nameList.setMenu(menu);
		if (nameList.getSelectionCount()> 0) {
			// 删除菜单
			final MenuItem selItem = new MenuItem(menu, SWT.NONE);
			selItem.setText("删除");
			selItem.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent e) {
					del();
				}
			});
		}
	}

	protected void del() {
	}
	
	private void updateOkBtnEnable(){
		int idx = folder.getSelectionIndex();
		okBtn.setEnabled(idx == 0 && nameList.getSelectionCount() > 0 || idx == 1 && pathTxt.getText().length() > 0);
	}

	/**
	 * Create contents of the button bar
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		okBtn = createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
		createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);
	}

	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("打开工程");
	}
	
	@Override
	protected Point getInitialSize() {
		return new Point(500, 350);
	}
	
	private String checkInput() {
		int idx = folder.getSelectionIndex();
		if(idx == 0){
			if (nameList.getSelectionIndex() < 0)
				return "请选择要打开的历史工程";
		} else {
			String path = pathTxt.getText();
			if (StringUtil.isEmpty(path))
				return "工程路径不能为空！";
			File prjDir = new File(path);
			if (prjDir.exists()) {
				if (!isValid(prjDir))
					return "无效的工程文件夹！";
			}
		}
		return null;
	}
	
	protected boolean isValid(File prjDir) {
		return true;
	}

	@Override
	public boolean close() {
		Constants.CURRENT_PRJ_NAME = oldName;
		Constants.CURRENT_PRJ_PATH = oldPath;
		return super.close();
	}

	@Override
	protected void okPressed() {
		String msg = checkInput();
		if (msg != null) {
			DialogHelper.showWarning(msg);
			return;
		}
		int idx = folder.getSelectionIndex();
		if(idx == 0){
			name = nameList.getItem(nameList.getSelectionIndex());
		} else {
			path = pathTxt.getText();
		}
		super.okPressed();
	}

	public String getName() {
		return name;
	}

	public String getPath() {
		return path;
	}
}
