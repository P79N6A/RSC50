package com.shrcn.found.ui.action;

import com.shrcn.found.common.event.EventConstants;
import com.shrcn.found.common.event.EventManager;
import com.shrcn.found.ui.editor.BaseEditorInput;
import com.shrcn.found.ui.editor.IEditorInput;

public abstract class OpenEditorAction extends ConfigAction {

	public OpenEditorAction(String text) {
		super(text);
	}
	
	@Override
	public void run() {
		EventManager.getDefault().notify(EventConstants.OPEN_CONFIG, getEditorInput());
	}
	
	protected IEditorInput getEditorInput() {
		String editorId = getEditorId();
		String editorName = getText();
		String icon = getIcon();
		return new BaseEditorInput(editorName, icon, editorId);
	}

	abstract protected String getEditorId();
}
