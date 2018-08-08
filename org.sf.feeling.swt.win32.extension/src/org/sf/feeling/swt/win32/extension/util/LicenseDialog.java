/**
 * Copyright (c) 2007-2010 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based on IEC61850 SCT.
 */
package org.sf.feeling.swt.win32.extension.util;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

/**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2013-3-6
 */
/**
 * $Log: LicenseDialog.java,v $
 * Revision 1.4  2013/12/05 10:14:13  cchun
 * Fix Bug:修复校验bug
 *
 * Revision 1.3  2013/12/05 09:49:45  zq
 * Update: 如果校验不通过, 那么就按钮设置成不可用。
 *
 * Revision 1.2  2013/12/02 08:53:37  zq
 * Update: 如果校验不通过, 那么就按钮设置成不可用。
 *
 * Revision 1.1  2013/03/07 06:39:48  cchun
 * Add:认证对话框
 *
 */
public class LicenseDialog extends TitleAreaDialog {

	private Text txtSerialNo;
	private Text txtLicense;
	private Label lbInfo;
	
	private String lic_file;
	
	/**
	 * Create the dialog
	 * @param parentShell
	 */
	public LicenseDialog(Shell parentShell, String lic_file) {
		super(parentShell);
		this.lic_file = lic_file;
	}

	/**
	 * Create contents of the dialog
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite area = (Composite) super.createDialogArea(parent);
		Composite container = new Composite(area, SWT.NONE);
		container.setLayoutData(new GridData(GridData.FILL_BOTH));
		//
		setTitle("授权说明");
		setMessage("    产品激活之前不允许使用，如要激活请向思源弘瑞自动化有限公司技术管理部申请。", IMessageProvider.WARNING);
		createLicenseUI(container);
		return area;
	}
	
	private void createLicenseUI(Composite container) {
		int cols = 4;
		container.setLayoutData(new GridData(GridData.FILL_BOTH));
		container.setLayout(new GridLayout(cols, false));
		// 一行
		Label lbSer = new Label(container, SWT.NONE);
		lbSer.setText("序列号：");
		this.txtSerialNo = new Text(container, SWT.BORDER | SWT.READ_ONLY);
		GridData txtData = new GridData(SWT.FILL, SWT.DEFAULT, true, false, cols - 1, 1);
		txtSerialNo.setLayoutData(txtData);
		txtSerialNo.setText(CryptManager.getSerialNo());
		// 二行
		Label lbLic = new Label(container, SWT.NONE);
		lbLic.setText("激活码：");
		this.txtLicense = new Text(container, SWT.BORDER | SWT.MULTI | SWT.WRAP);
		txtData = new GridData(SWT.FILL, SWT.FILL, true, true, cols - 1, 1);
		txtLicense.setLayoutData(txtData);
		// 三行
		lbInfo = new Label(container, SWT.NONE);
		lbInfo.setForeground(new Color(null,   0,   0, 255));
		lbInfo.setText(" ");
		txtData = new GridData(SWT.FILL, SWT.DEFAULT, true, false, cols, 1);
		lbInfo.setLayoutData(txtData);
	}
	
	private void initControls() {
		final Button btOk = getButton(IDialogConstants.OK_ID);
		btOk.setEnabled(false);
		
		txtLicense.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				String activeCode = txtLicense.getText().trim();
				if (!CryptManager.isValid(activeCode)) {
					lbInfo.setText("非法的激活码！");
					btOk.setEnabled(false);
				} else {
					if (CryptManager.checkLicense(activeCode)) {
						lbInfo.setText("恭喜您，产品已激活，祝使用愉快！");
						btOk.setEnabled(true);
					} else {
						lbInfo.setText("激活码无效，请重新输入！");
						btOk.setEnabled(false);
					}
				}
			}});
	}
	
	@Override
	protected void okPressed() {
		// 保存
		String activeCode = txtLicense.getText().trim();
		FileManager.saveTextFile(activeCode, lic_file);
		super.okPressed();
	}

	/**
	 * Create contents of the button bar
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, "确定",	true);
		createButton(parent, IDialogConstants.CANCEL_ID, "取消", false);
		initControls();
	}
	
	/**
	 * Return the initial size of the dialog
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(500, 375);
	}
	
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("警告");
	}

}
