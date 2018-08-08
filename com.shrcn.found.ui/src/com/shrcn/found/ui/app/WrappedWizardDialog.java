/**
 * Copyright (c) 2007-2013 上海思源弘瑞自动化有限公司. All rights reserved. 
 * This program is an eclipse Rich Client Application
 * designed for IED configuration and debuging.
 */
package com.shrcn.found.ui.app;

import org.eclipse.jface.wizard.IWizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.sf.feeling.swt.win32.extension.widgets.ShellWrapper;

import com.shrcn.found.ui.UIConstants;
import com.shrcn.found.ui.util.SwtUtil;

/**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2015-2-3
 */
/**
 * $Log$
 */
public class WrappedWizardDialog extends WizardDialog {

	public WrappedWizardDialog(Shell parentShell, IWizard newWizard) {
		super(parentShell, newWizard);
	}

	@Override
	protected Control createContents(Composite parent) {
		if (SwtUtil.isLinux())
			return super.createContents(parent);
		Shell newShell = (Shell) parent;
		ShellWrapper wrapper = new ShellWrapper( newShell );
    	wrapper.installTheme(UIConstants.CurrentTheme);
    	
    	Composite composite = wrapper.getClientArea();
		if (composite != null) {
			super.createContents(composite);
			SwtUtil.setContainerBg(composite);
			composite.layout();
		}
		
		Point size = getInitialSize();
		Point location = getInitialLocation(size);
		newShell.setBounds(getConstrainedShellBounds(new Rectangle(location.x,
				location.y, size.x, size.y)));
		
		return composite;
	}
}
