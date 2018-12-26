package com.synet.tool.rsc.action;

import org.eclipse.swt.widgets.Display;

import com.shrcn.found.common.event.EventConstants;
import com.shrcn.found.common.event.EventManager;
import com.shrcn.found.ui.action.MenuAction;
import com.shrcn.found.ui.util.ImageConstants;
import com.shrcn.found.ui.util.ImgDescManager;
import com.synet.tool.rsc.RSCProperties;
import com.synet.tool.rsc.UICommonConstants;

public class EditModeAction extends MenuAction {

	public EditModeAction(String text){
		super(text);
		resetImage();
	}
	
	/**
	 * 增加操作
	 */
	public void run(){
		shell = Display.getDefault().getActiveShell();
		RSCProperties.getInstance().switchReplaceMode();
		resetImage();
		EventManager.getDefault().notify(EventConstants.SYS_REFRESH_TOP_BAN, null);
	}

	private void resetImage() {
		setImageDescriptor(RSCProperties.getInstance().isReplaceMode() ? 
				ImgDescManager.getImageDesc(ImageConstants.SCHEMA_CHECK) : null);
	}
}
