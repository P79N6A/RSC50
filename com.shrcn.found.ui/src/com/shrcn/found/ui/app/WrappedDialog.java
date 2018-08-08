/**
 * Copyright (c) 2007-2014 上海思源弘瑞自动化有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based on substation automation.
 */
package com.shrcn.found.ui.app;

import org.eclipse.jface.dialogs.Dialog;
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
 * @version 1.0, 2014-5-4
 */
/**
 * $Log$
 */
public abstract class WrappedDialog extends Dialog {

	protected WrappedDialog(Shell parentShell) {
		super(parentShell);
	}
	
	protected Control createContents(Composite parent) {
		if (SwtUtil.isLinux())
			return super.createContents(parent);
		Shell newShell = (Shell) parent;
		ShellWrapper wrapper = new ShellWrapper( newShell );
    	wrapper.installTheme(UIConstants.CurrentTheme);
    	Composite composite = wrapper.getClientArea();
		if (composite!=null){
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
