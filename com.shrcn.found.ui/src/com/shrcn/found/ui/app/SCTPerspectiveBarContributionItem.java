/**
 * Copyright (c) 2007-2010 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based on IEC61850 SCT.
 */
package com.shrcn.found.ui.app;

import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.ui.IPerspectiveDescriptor;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.internal.PerspectiveBarContributionItem;

import com.shrcn.found.common.util.ObjectUtil;

/**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2014-7-30
 */
/**
 * $Log$
 */
public class SCTPerspectiveBarContributionItem extends PerspectiveBarContributionItem {

    /**
     * Create a new perspective contribution item
     * 
     * @param perspective the descriptor for the perspective
     * @param workbenchPage the page that this perspective is in
     */
    public SCTPerspectiveBarContributionItem(IPerspectiveDescriptor perspective,
            IWorkbenchPage workbenchPage) {
    	super(perspective, workbenchPage);
    }

    /**
     * Select this perspective
     */
    public void select() {
    	final IWorkbenchPage workbenchPage = (IWorkbenchPage) ObjectUtil.getFieldValue(this, "workbenchPage");
    	final IPerspectiveDescriptor perspective = (IPerspectiveDescriptor) ObjectUtil.getFieldValue(this, "perspective");
    	final ToolItem toolItem = (ToolItem) ObjectUtil.getFieldValue(this, "toolItem");
        if (workbenchPage.getPerspective() != perspective) {
        	BusyIndicator.showWhile(null, new Runnable() {
                public void run() {
                	ObjectUtil.invoke(workbenchPage, "busySetPerspective",
                			new Class<?>[]{IPerspectiveDescriptor.class}, perspective);
                	MenuToolFactory.getInstance().reloadMenuTools();
                }
            });
        } else {
			toolItem.setSelection(true);
		}
    }

}
