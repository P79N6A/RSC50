/**
 * Copyright (c) 2007-2013 上海思源弘瑞自动化有限公司. All rights reserved. 
 * This program is an eclipse Rich Client Application
 * designed for IED configuration and debuging.
 */
package com.shrcn.found.ui.action;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;

import com.shrcn.found.common.util.StringUtil;
import com.shrcn.found.file.util.FileManager;
import com.shrcn.found.ui.util.DialogHelper;
import com.shrcn.found.ui.util.ImageConstants;
import com.shrcn.found.ui.util.ImgDescManager;
import com.shrcn.found.ui.util.SwtUtil;

/**
 * @author 曹兴春(mailto:cxc.14375@shrcn.com)
 * @version 1.0, 2013-6-24
 */
/**
 * $log$
 */
public class ItExportAction extends Action {

	private StyledText text;

	public ItExportAction(StyledText text) {
		ImageDescriptor imgDesc = ImgDescManager
				.getImageDesc(ImageConstants.EDIT_EXPORT);
		setImageDescriptor(imgDesc);
		setText("导出文本");
		this.text = text;
	}

	@Override
	public void run() {
		if (text.getText().equals("")) { //$NON-NLS-1$
			DialogHelper.showWarning("导出内容为空！");
			return;
		}
		final String fileName = DialogHelper.selectFile(
				SwtUtil.getDefaultShell(), SWT.SAVE, "*.txt");
		if (StringUtil.isEmpty(fileName))
			return;
		FileManager.writeFile(text.getText(), fileName);
	}
}
