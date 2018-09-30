package com.synet.tool.rsc.app;

import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;

import com.shrcn.found.ui.UIConstants;
import com.shrcn.found.ui.app.ToolsPerspective;

public class Perspective extends ToolsPerspective {

	public static final String ID = "com.synet.tool.rsc.perspective";
	
	@Override
	public void createInitialLayout(IPageLayout layout) {
		String editorArea = layout.getEditorArea();
		layout.setEditorAreaVisible(false);
		layout.addStandaloneView(UIConstants.View_Navg_ID, false, IPageLayout.LEFT, 0.2f, editorArea);
		layout.addStandaloneView(UIConstants.View_Content_ID, false, IPageLayout.TOP, 0.8f, editorArea);
		
		IFolderLayout bottomLeft = layout.createFolder("bottom", IPageLayout.BOTTOM, 0.15f, editorArea);
 		bottomLeft.addView(UIConstants.View_Console_ID);
 		bottomLeft.addView(com.shrcn.found.ui.view.ProblemView.ID);
	}

}
