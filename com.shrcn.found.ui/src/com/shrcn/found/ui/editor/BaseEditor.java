/**
 * Copyright (c) 2007-2013 上海杏仁软件技术有限公司. All rights reserved. 
 * This program is an eclipse Rich Client Application
 * designed for IED configuration and debuging.
 */
package com.shrcn.found.ui.editor;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

import com.shrcn.found.common.util.StringUtil;
import com.shrcn.found.ui.util.IconsManager;
import com.shrcn.found.ui.util.SwtUtil;
import com.shrcn.found.ui.view.ConsoleManager;
import com.shrcn.found.ui.widget.FormWidgetFactory;
import com.shrcn.found.ui.widget.TabbedPropertyTitle;

/**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2013-4-3
 */
public abstract class BaseEditor implements IConfigEditor {

	protected Composite parent;
	protected Composite editArea;
	private TabbedPropertyTitle title;
	protected IEditorInput input;
	private boolean dirty;
	private boolean disposed;
	protected ConsoleManager console = ConsoleManager.getInstance();

	public BaseEditor(Composite parent, IEditorInput input) {
		this.parent = parent;
		this.input = input;
		this.dirty = false;
		this.disposed = false;
		init();
		if (!isDisposed()) {
			initData();
			addListeners();
		}
	}

	@Override
	public void init() {
		int w = 0, cols = 1;
		GridLayout layout = new GridLayout(cols, false);
		layout.marginBottom = -5;
		layout.marginLeft = w;
		layout.marginRight = w;
		layout.marginTop = -5;
		layout.marginWidth = w;
		layout.verticalSpacing = w;
		parent.setLayout(layout);
		
		title = new TabbedPropertyTitle(parent, FormWidgetFactory.getInstance());
		title.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		editArea = SwtUtil.createComposite(parent, new GridData(GridData.FILL_BOTH), 1);
		buildUI(editArea);
		layout();
	}
	
	public void setEditorTitle(String editorName, Image icon) {
		title.setTitle(editorName, icon);
	}
	
	protected void addListeners() {
	}
	
	public void layout() {
		String titleText = StringUtil.nullToEmpty(input.getEditorName());
		setEditorTitle(titleText, IconsManager.getInstance().getImage(input.getIcon()));
		GridLayout gridlayout = (GridLayout) editArea.getLayout();
		gridlayout.marginLeft = 3;
		gridlayout.marginRight = 3;
		gridlayout.marginTop = 0;
		editArea.layout();
		parent.layout();
	}
	
	public Shell getShell() {
		return parent.getShell();
	}
	
	@Override
	public void dispose() {
		if (parent != null) {
			for (Control control : parent.getChildren()) {
				control.dispose();
			}
			this.disposed = true;
		}
	}
	
	@Override
	public void refresh() {
	}
	
	@Override
	public void refresh(IEditorInput input) {
	}
	
	@Override
	public IEditorInput getInput() {
		return input;
	}

	@Override
	public void setInput(IEditorInput input) {
		this.input = input;
	}
	
	public String getEditorName() {
		return input==null ? "" : input.getEditorName();
	}

	@Override
	public boolean doSave() {
		return true;
	}

	@Override
	public boolean isDirty() {
		return dirty;
	}

	@Override
	public void setDirty(boolean isDirty) {
		this.dirty = isDirty;
	}

	@Override
	public boolean isDisposed() {
		return disposed;
	}

	@Override
	public void setDisposed(boolean disposed) {
		this.disposed = disposed;
	}
}
