/**
 * Copyright (c) 2007-2013 上海思弘瑞电力控制技术有限公司. All rights reserved. 
 * This program is an eclipse Rich Client Application
 * designed for IED configuration and debuging.
 */
package com.shrcn.found.ui.view;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;

import com.shrcn.found.ui.UIConstants;
import com.shrcn.found.ui.action.ItClearAction;
import com.shrcn.found.ui.action.ItExportAction;
import com.shrcn.found.ui.util.SwtUtil;

/**
 * 通讯报文监视视图s
 * 
 * @author 王敏(mailto:wm.202039@sieyuan.com)
 * @version 1.0, 2017-1-17
 */
/**
 * $Log$
 */
public class ComMsgView extends ViewPart {
	
	public static final String ID = UIConstants.View_Commsg_ID;
	
	private static Color colorsend;
	private static Color colorrecv;
	
	/**
	 * 文本域
	 */
	private StyledText text;
	
	@Override
	public void createPartControl(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayout(new FillLayout());

		text = new StyledText(composite, SWT.V_SCROLL | SWT.READ_ONLY | SWT.H_SCROLL | SWT.BORDER);
		
		Action actions[]={new ItClearAction(text),new ItExportAction(text)};
		SwtUtil.createContextMenu(text, actions);
		colorsend =new Color(parent.getDisplay(), 154, 5, 50);
		colorrecv =new Color(parent.getDisplay(), 16, 143, 22);
		initializeToolBar();
	}

	@Override
	public void setFocus() {

	}

	/**
	 * 设置校验结果信息
	 * @param info
	 */
	public void setCheckInfo(final String info) {
		if (text.isDisposed()) {
			return;
		}
		text.setText(info);
	}
	
	/**
	 * 清空控制台
	 */
	public void clearInfo() {
		if (text.isDisposed()) {
			return;
		}
		text.setText(""); //$NON-NLS-1$
	}
	
	/**
	 * 设置校验结果信息
	 * @param inf
	 */
	public void appendCheckInfo(String info) {
		if (text.isDisposed()) {
			return;
		}
		int len = text.getText().length();
		text.append(info);
		StyleRange range = new StyleRange();
		range.start      = len;
		range.length     = info.length();
		if(info.contains("send")){
			range.foreground = colorsend;
		} else {
			range.foreground = colorrecv;
		}
		text.setStyleRange(range);//设置字体颜色
		text.setTopPixel(text.getLineCount() * text.getLineHeight());//设置自动滚到最底部
	}

	/**
	 * 初始化工具条
	 */
	private void initializeToolBar() {
		IToolBarManager toolBarManager = getViewSite().getActionBars().getToolBarManager();
		toolBarManager.add(new ItClearAction(text));
		toolBarManager.add(new ItExportAction(text));	
	}

}
