package com.synet.tool.rsc.view;

import com.shrcn.found.common.event.Context;
import com.shrcn.found.common.event.EventConstants;
import com.shrcn.found.ui.editor.ConfigEditorInput;
import com.shrcn.found.ui.editor.IEditorInput;
import com.shrcn.found.ui.view.AContentView;

public class ContentView extends AContentView {
	
	public static final String ID = ContentView.class.getName();
	
	@Override
	protected boolean isOpened(IEditorInput currInput, IEditorInput input) {
		String currIed = ((ConfigEditorInput) currInput).getIedName();
		String currEditor = currInput.getEditorName();
		String newIed = ((ConfigEditorInput) input).getIedName();
		String newEditor = input.getEditorName();
		return (currIed.equals(newIed) && currEditor.equals(newEditor));
	}

	@Override
	public void execute(Context context) {
		super.execute(context);
//		String event = context.getEventName();
//		if (EventConstants.OPEN_CONFIG.equals(event)) {
//			ConfigEditorInput input = (ConfigEditorInput) context.getData();
//			openEditor(input);
//		} else if (EventConstants.CLEAR_CONFIG.equals(event)) {
//			if(cfgEditor != null) {
//				closeEditor(true);
//			}
//		} 
	}
}
