package com.synet.tool.rsc.view;

import com.shrcn.found.ui.editor.IEditorInput;
import com.shrcn.found.ui.view.AContentView;

public class ContentView extends AContentView {
	
	public static final String ID = ContentView.class.getName();
	
	@Override
	protected boolean isOpened(IEditorInput currInput, IEditorInput input) {
		String currIed = currInput.getEditorName();
		String currEditor = currInput.getEditorId();
		String newIed = input.getEditorName();
		String newEditor = input.getEditorId();
		return (currIed.equals(newIed) && currEditor.equals(newEditor));
	}

}
