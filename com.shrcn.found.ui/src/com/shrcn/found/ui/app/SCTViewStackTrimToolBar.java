/**
 * Copyright (c) 2007-2013 上海思源弘瑞自动化有限公司. All rights reserved. 
 * This program is an eclipse Rich Client Application
 * designed for IED configuration and debuging.
 */
package com.shrcn.found.ui.app;

import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.CoolBar;
import org.eclipse.swt.widgets.CoolItem;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.ui.internal.IntModel;
import org.eclipse.ui.internal.TrimFrame;
import org.eclipse.ui.internal.ViewStackTrimToolBar;
import org.eclipse.ui.internal.WorkbenchWindow;
import org.eclipse.ui.presentations.PresentationUtil;

import com.shrcn.found.common.util.ObjectUtil;
import com.shrcn.found.ui.UIConstants;

/**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2014-4-29
 */
/**
 * $Log$
 */
public class SCTViewStackTrimToolBar extends ViewStackTrimToolBar {

	private static final Color bgColor = UIConstants.Win_BG;
	private Composite clientArea;
	
	public SCTViewStackTrimToolBar(String id, int curSide, int paneOrientation,
			WorkbenchWindow wbw, Composite clientArea) {
		super(id, curSide, paneOrientation, wbw);
		this.clientArea = clientArea;
		dock(curSide);
	}

	@Override
	public void dock(int dropSide) {
		if (clientArea == null)
			return;
		createControl(dropSide);
	}

	private void injectParentElement(String name, Object value) {
		ObjectUtil.setFieldValue(this, name, value);
	}
	
	/**
	 * Set up the trim with its cursor, drag listener, context menu and menu listener.
	 * This method can also be used to 'recycle' a trim handle as long as the new handle
	 * is for trim under the same parent as it was originally used for.
	 */
	private void createControl(int curSide) {
		// out with the old
		dispose();
		
		IntModel radioVal = (IntModel) ObjectUtil.getFieldValue(this, "radioVal");
		radioVal.set(curSide);
//    	this.radioVal.set(curSide);
    	
    	// remember the orientation to use
    	orientation = (curSide == SWT.LEFT || curSide == SWT.RIGHT) ? SWT.VERTICAL  : SWT.HORIZONTAL;

    	TrimFrame frame = new TrimFrame(clientArea);
    	injectParentElement("frame", frame);
		
		// Create the necessary parts...
    	CoolBar cb = new CoolBar(frame.getComposite(), orientation | SWT.FLAT);
    	CoolItem ci = new CoolItem(cb, SWT.FLAT);
    	injectParentElement("cb", cb);
    	injectParentElement("ci", ci);
    	frame.getComposite().setBackground(bgColor);
    	cb.setBackground(bgColor);

		// Create (and 'fill') the toolbar
		tbMgr = new ToolBarManager(orientation | SWT.FLAT);
		
		// Have the subclass define any manager content
		initToolBarManager(tbMgr);
		
		// Create the new ToolBar
		ToolBar tb = tbMgr.createControl(cb);
		ci.setControl(tb);
		
		// Have the subclass hook any listeners
		hookControl(tbMgr);
		
		// set up the frame's layout
		update(true);
		
    	// Set the cursor affordance
    	Cursor dragCursor = getControl().getDisplay().getSystemCursor(SWT.CURSOR_SIZEALL);
    	cb.setCursor(dragCursor);
    	
    	// Now, we have to explicity set the arrow for the TB
    	Cursor tbCursor = getControl().getDisplay().getSystemCursor(SWT.CURSOR_ARROW);
    	tb.setCursor(tbCursor);
    	
    	//cb.setBackground(cb.getDisplay().getSystemColor(SWT.COLOR_RED));
    	
        // Set up the dragging behaviour
    	Listener dragListener = (Listener) ObjectUtil.getFieldValue(this, "dragListener");
        PresentationUtil.addDragListener(cb, dragListener);
    	
    	// Create the docking context menu
//    	dockMenuManager = new MenuManager();
//    	dockContributionItem = getDockingContribution();
//        dockMenuManager.add(dockContributionItem);

//        tb.addListener(SWT.MenuDetect, tbListener);
//        cb.addListener(SWT.MenuDetect, cbListener);
        
        //tbMgr.getControl().setBackground(cb.getDisplay().getSystemColor(SWT.COLOR_GREEN));
        //tbMgr.getControl().pack(true);
        cb.pack(true);
        cb.setVisible(true);
        
        tbMgr.getControl().setVisible(true);
        cb.setVisible(true);
        frame.getComposite().setVisible(true);
    }
}
