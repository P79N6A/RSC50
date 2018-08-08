package com.shrcn.found.ui.editor.xml;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.action.GroupMarker;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.editors.text.TextEditor;
import org.eclipse.ui.texteditor.ITextEditorActionConstants;

public class XMLEditor extends TextEditor {

	public static String ID = XMLEditor.class.getName();
	private ColorManager colorManager;

	@Override
	public void doSetInput(IEditorInput input) throws CoreException {
		super.doSetInput(input);
	}

	public XMLEditor() {
		super();
		colorManager = new ColorManager();
		setSourceViewerConfiguration(new XMLConfiguration(colorManager));
		setDocumentProvider(new XMLDocumentProvider());
	}
	
	@Override
	protected void editorContextMenuAboutToShow(IMenuManager menu) {
		menu.add(new GroupMarker(ITextEditorActionConstants.GROUP_EDIT));
		addAction(menu, ITextEditorActionConstants.GROUP_EDIT, ITextEditorActionConstants.FIND);
		addAction(menu, ITextEditorActionConstants.GROUP_EDIT, ITextEditorActionConstants.GOTO_LINE);
		addAction(menu, ITextEditorActionConstants.GROUP_EDIT, ITextEditorActionConstants.SELECT_ALL);
		addAction(menu, ITextEditorActionConstants.GROUP_EDIT, ITextEditorActionConstants.COPY);
	}

	public void dispose() {
		colorManager.dispose();
		super.dispose();
	}

}
