/**
 * Copyright (c) 2007-2010 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based on IEC61850 SCT.
 */
package com.shrcn.found.ui.dialog;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.wizard.IWizard;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.ProgressMonitorPart;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

import com.shrcn.found.ui.util.ContextPage;

/**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2011-7-21
 */
/**
 * $Log: FixedWizardDialog.java,v $
 * Revision 1.1  2013/03/29 09:37:38  cchun
 * Add:创建
 *
 * Revision 1.3  2011/12/08 03:03:36  cchun
 * Fix Bug:修改buttonPressed()避免重复校验
 *
 * Revision 1.2  2011/11/18 09:16:10  cchun
 * Fix Bug:向导对话框标题不在configureShell()方法中设置
 *
 * Revision 1.1  2011/09/15 08:39:01  cchun
 * Refactor:创建公用对话框包
 *
 * Revision 1.3  2011/08/01 08:19:06  cchun
 * Update:为向导对话框添加校验处理
 *
 * Revision 1.2  2011/07/27 07:37:18  cchun
 * Update:添加按钮事件
 *
 * Revision 1.1  2011/07/21 08:20:19  cchun
 * Update:使用自定义向导对话框
 *
 */
public class FixedWizardDialog extends WizardDialog {

	
	public FixedWizardDialog(Shell parentShell, IWizard newWizard) {
		super(parentShell, newWizard);
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite composite = (Composite) super.createDialogArea(parent);
		ProgressMonitorPart part = (ProgressMonitorPart)getProgressMonitor();
		((GridData)part.getLayoutData()).exclude = true;
		return composite;
	}
	
	@Override
    protected void buttonPressed(int buttonId) {
        if (buttonId == IDialogConstants.NEXT_ID ||
        		buttonId == IDialogConstants.FINISH_ID){
            if (!actionWhenNextButtonPressed()) {
                return;
            }
        }
        super.buttonPressed(buttonId);
    }
//    
//	/**
//	 * 校验
//	 * @return
//	 */
//	private boolean checkInput() {
//		for (int i = 0; i < getWizard().getPages().length; i++) {
//            if (!((ContextPage) getWizard().getPages()[i]).checkInput()) {
//				return false;
//			}
//        }
//		return true;
//	}
	
	/**
	 * “下一步”按钮事件
	 * @return
	 */
    protected boolean actionWhenNextButtonPressed() {
    	IWizardPage currentPage = getWizard().getContainer().getCurrentPage();
    	return ((ContextPage) currentPage).nextButtonClick();
    }
}
