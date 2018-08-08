/**
* Copyright (c) 2007, 2008 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application 
 * based Visual Device Develop System.
 */

package com.shrcn.found.ui.editor;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorPart;

/**
 * 编辑器基类
 * 
 * Add one sentence class summary here.
 * Add class description here.
 *
 * @author 黄钦辉
 * @version 1.0, 2009-4-21
 */
public class BasicEditPart extends EditorPart {
	
	private boolean dirty = false;

	@Override
	public void doSave(IProgressMonitor monitor) {

	}

	@Override
	public void doSaveAs() {

	}

	@Override
	public void init(IEditorSite site, IEditorInput input)
			throws PartInitException {
		setInput(input);
		setSite(site);
		setPartName(input.getName());
	}

	public void setDirty(boolean dirty) {
        if (this.dirty != dirty) {
        	this.dirty = dirty;
            firePropertyChange(IEditorPart.PROP_DIRTY);
        }
    }
	
	@Override
	public boolean isDirty() {
		return this.dirty;
	}

	@Override
	public boolean isSaveAsAllowed() {
		return false;
	}

	@Override
	public void createPartControl(Composite parent) {

	}

	@Override
	public void setFocus() {

	}

	@Override
	public void setPartName(String partName) {
		super.setPartName(partName);
	}
}


