/**
 * Copyright   2007, 2008 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application 
 * based Device Customization Platform System.
 */
package com.shrcn.found.ui.view;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;

import com.shrcn.found.common.util.StringUtil;
import com.shrcn.found.ui.UIConstants;
import com.shrcn.found.ui.action.ItClearAction;
import com.shrcn.found.ui.action.ItExportAction;
import com.shrcn.found.ui.util.ImageConstants;
import com.shrcn.found.ui.util.ImgDescManager;
import com.shrcn.found.ui.util.SwtUtil;

/**
 * 底部信息，输出视图
 * 
 * @author 刘静
 * @date 2009-4-1
 */
public class ConsoleView extends ViewPart {

	public static final int MAX_LINE = 1000000;
	public static final String ID = UIConstants.View_Console_ID;
	private StyledText text;
	Composite composite ;
	
	@Override
	public void createPartControl(Composite parent) {
		composite = new Composite(parent, SWT.NONE);
		composite.setLayout(new FillLayout());

		text = new StyledText(composite, SWT.V_SCROLL | SWT.READ_ONLY | SWT.H_SCROLL | SWT.BORDER);
		
		Action actions[]={new CopyAction(), new ItExportAction(text), new ItClearAction(text)};
		SwtUtil.createContextMenu(text, actions);
		initializeToolBar();
	}
	
	class CopyAction extends Action {
		
		public CopyAction() {
			ImageDescriptor imgDesc = ImgDescManager
					.getImageDesc(ImageConstants.COPY);
			setImageDescriptor(imgDesc);
			setText("复制");
		}
		
		@Override
		public void run() {
			String selText = text.getSelectionText();
			if (StringUtil.isEmpty(selText)) {
				return;
			}
			SwtUtil.copyToClipBoard(selText);
		}
	}

	@Override
	public void setFocus() {

	}

	/**
	 * 设置校验结果信息
	 * @param info
	 */
	public void setCheckInfo(final String info) {
		text.setText(info);
	}
	
	/**
	 * 清空控制台
	 */
	public void clearInfo() {
		text.setText(""); //$NON-NLS-1$
	}
	
	/**
	 * 设置校验结果信息
	 * @param inf
	 */
	public String appendCheckInfo(String info) {
		if (text.getLineCount() > MAX_LINE) {
			String text2 = text.getText();
			char[] cs = text2.toCharArray();
			boolean isc = false;
			int i=0;
			for (; i<text2.length(); i++) {
				char c = cs[i];
				if (c=='\n' || c==' ' || c=='\t') {
					if (isc) {
						break;
					}
				} else {
					isc = true;
				}
			}
			text.replaceTextRange(0, i, "");
		}
		text.append(info);
		text.setTopPixel(text.getLineCount() * text.getLineHeight());//设置自动滚到最底部\\
		return text.getText();
	}
	
	private void initializeToolBar() {
		IToolBarManager toolBarManager = getViewSite().getActionBars().getToolBarManager();
		toolBarManager.add(new ItClearAction(text));
		toolBarManager.add(new ItExportAction(text));	
	}
}
