/**
 * Copyright (c) 2007-2013 上海思弘瑞电力控制技术有限公司. All rights reserved. 
 * This program is an eclipse Rich Client Application
 * designed for IED configuration and debuging.
 */
package com.shrcn.found.ui.dialog;

import java.awt.Font;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

import com.shrcn.found.ui.UIConstants;
import com.shrcn.found.ui.app.WrappedDialog;
import com.shrcn.found.ui.util.IconsManager;
import com.shrcn.found.ui.util.ImageConstants;
import com.shrcn.found.ui.util.UIProperties;

/**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2013-5-16
 */
/**
 * $Log: AboutDialog.java,v $
 * Revision 1.9  2013/08/14 11:59:55  cchun
 * Udpate:调整about对话框
 *
 * Revision 1.8  2013/07/30 03:28:01  scy
 * Update：修改公司名称
 *
 * Revision 1.7  2013/07/05 08:05:11  cchun
 * Refactor:提取产品信息构造参数
 *
 * Revision 1.6  2013/06/27 02:09:56  cchun
 * Update:统一图片管理
 *
 * Revision 1.5  2013/06/26 02:06:34  cchun
 * Fix Bug:修复网址错误
 *
 * Revision 1.2  2013/05/21 07:32:52  cchun
 * Update:调整图标位置
 *
 * Revision 1.1  2013/05/16 13:54:38  cchun
 * Update:添加关于对话框
 *
 */
public class AboutDialog extends WrappedDialog {

	private String productInfo;
	
	/**
	 * 构造一个对话框.
	 * @param parentShell
	 */
	public AboutDialog(Shell parentShell, String productInfo) {
		super(parentShell);
		this.productInfo = productInfo;
	}

	/**
	 * 配置对话框.
	 */
	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("关于");
	}

	/**
	 * 创建对话框区域.
	 * 
	 * @param parent
	 *            父类的面板.
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);
		container.setLayout(new GridLayout(2, false));
		// 左边放logo， 右边写版权
		Label lbLogo = new Label(container, SWT.NONE);
		lbLogo.setAlignment(SWT.BOTTOM);
		lbLogo.setImage(IconsManager.getInstance().getImage(ImageConstants.logo));
		GridData logoData = new GridData(GridData.FILL_BOTH);
		logoData.horizontalAlignment = SWT.CENTER;
		logoData.verticalAlignment = SWT.TOP;
		logoData.heightHint = 160;
		lbLogo.setLayoutData(logoData);
		
		String buildId = "Build ID：" +
		UIProperties.getInstance().getBuildID();
		String copyRight = productInfo + buildId + "\n\nCopyright©2018 思源电气股份有限公司\n版权所有。\n" +
				"网址：www.sieyuan.com\nEmail:cchun@shrcn.com";
		Label lbCR= new Label(container, SWT.WRAP); 
		lbCR.setText(copyRight);
		lbCR.setFont(UIConstants.FONT_CONTENT);
		GridData layoutData = new GridData(GridData.FILL_BOTH);
		layoutData.horizontalAlignment = SWT.LEFT;
		layoutData.verticalAlignment = SWT.CENTER;
		lbCR.setLayoutData(layoutData);
		
		return container;
	}

	/**
	 * 对话框的尺寸.
	 * 
	 * @return 对话框的初始尺寸.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(531, 331);
	}

	/**
	 * 创建按钮.
	 * @return 此方法返回<code>null</code>可去掉对话框上的按钮.
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, "关闭", true);
	}

	@Override
	protected void setShellStyle(int newShellStyle) {	
		super.setShellStyle(SWT.TITLE);
	}

}
