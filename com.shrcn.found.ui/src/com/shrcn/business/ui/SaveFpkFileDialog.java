/**
 * Copyright (c) 2007-2013 上海思弘瑞电力控制技术有限公司. All rights reserved. 
 * This program is an eclipse Rich Client Application
 * designed for IED configuration and debuging.
 */
package com.shrcn.business.ui;

import java.util.Date;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.shrcn.found.ui.UIConstants;
import com.shrcn.found.ui.app.WrappedDialog;
import com.shrcn.found.ui.util.DialogHelper;

/**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2013-7-3
 */
/**
 * $Log: SaveFpkFileDialog.java,v $
 * Revision 1.1  2013/07/03 12:05:15  cchun
 * Add:增加fpk保存方法
 *
 */
public class SaveFpkFileDialog extends WrappedDialog {

	protected Shell shell;
	private Text text;
	private String ver;
	private String result;
	private Text text_inside;
	private String insideVer;
	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 * @param ver 
	 */
	public SaveFpkFileDialog(Shell parent) {
		super(parent);
		this.ver = "1.00.000";
		this.shell = parent;
	}

	/**
	 * 配置对话框.
	 */
	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("\u8BF7\u8F93\u5165\u7248\u672C\u53F7");
	}

	/**
	 * 对话框的尺寸.
	 * 
	 * @return 对话框的初始尺寸.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(294, 175);
	}

	@Override
	protected void setShellStyle(int newShellStyle) {
		super.setShellStyle(SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
	}
	
	@Override
	protected Control createDialogArea(Composite parent) {
		final Composite container = (Composite) super.createDialogArea(parent);
		container.setLayout(new GridLayout(2, false));
		
		Label lblNewLabel = new Label(container, SWT.NONE);
		lblNewLabel.setText("版本号：");
		
		text = new Text(container, SWT.BORDER);
		text.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		text.setText(this.ver);
		text.setSelection(0, 8);
		text.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				String ver = text.getText();
				byte[] verByte = ver.getBytes();
				if (verByte.length>8){
					DialogHelper.showError("版本号不能超过8个字符（一个汉字占2个字符）");
					return;
				}
			}
		});
		Label lblNewLabel_1 = new Label(container, SWT.NONE);
		lblNewLabel_1.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblNewLabel_1.setText("内部版本号：");
		
		text_inside = new Text(container, SWT.BORDER);
		text_inside.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblNewLabel_2 = new Label(container, SWT.NONE);
		lblNewLabel_2.setForeground(UIConstants.FPK_LBL);
		GridData gd_lblNewLabel_2 = new GridData(SWT.FILL, SWT.FILL, false, false, 2, 1);
		gd_lblNewLabel_2.widthHint = 269;
		gd_lblNewLabel_2.heightHint = 29;
		lblNewLabel_2.setLayoutData(gd_lblNewLabel_2);
		lblNewLabel_2.setText("内部版本号为空则按照老的文件格式打包\n否则按照新的版本格式打包。");
		text_inside.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				String ver = text_inside.getText();
				byte[] verByte = ver.getBytes();
				if (verByte.length>4){
					DialogHelper.showError("内部版本号不能超过4个字符（一个汉字占2个字符）");
					return;
				}
			}
		});
		
		return container;
	}

	@Override
	protected void buttonPressed(int buttonId) {
		if (text.getText().equals("")){
			DialogHelper.showInformation("版本号不能为空");
			return;
		}
		
		if (buttonId == OK) {
			String ver = text.getText();
			byte[] verByte = ver.getBytes();
			if (verByte.length==8){
				result = text.getText();
			} else {
				byte[] temp = new byte[8];
				for (int i = 0; i < temp.length; i++) {
					if (i>=verByte.length){
						temp[i]=' ';//不够补空格
					} else {
						temp[i] = verByte[i];
					}
				}
				result = new String(temp);
			}
			ver = text_inside.getText();
			if (!ver.equals("")){
				ver = ver.replace(".", "");
				verByte = ver.getBytes();
				Date date = new Date();
				long ldate = date.getTime() ;
				long randonNum = (long) (Math.random() * ldate/1000);
				String randStr = String.valueOf(randonNum) +"00000";
				randStr = randStr.substring(0, 5);
				if (verByte.length==3){
					insideVer = randStr+ver;
				} else{
					ver = ver+"000";
					insideVer = randStr+ver.substring(0, 3);
				}
			} else {
				insideVer = "";
			}
		}
		super.buttonPressed(buttonId);
	}

	public String getResult() {
		return result;
	}

	public String getInsideVer() {
		return insideVer;
	}

	public void setInsideVer(String insideVer) {
		this.insideVer = insideVer;
	}
}
