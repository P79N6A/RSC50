package com.synet.tool.rsc.app;

import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;

import com.shrcn.found.ui.app.AbstractWorkbenchWindowAdvisor;
import com.shrcn.found.ui.view.ViewManager;
import com.synet.tool.rsc.view.InfoView;

public class ApplicationWorkbenchWindowAdvisor extends AbstractWorkbenchWindowAdvisor {

    public ApplicationWorkbenchWindowAdvisor(IWorkbenchWindowConfigurer configurer) {
        super(configurer);
    }

    public ActionBarAdvisor createActionBarAdvisor(IActionBarConfigurer configurer) {
        return new ApplicationActionBarAdvisor(configurer);
    }
    
    @Override
	public void postWindowOpen() {
		super.postWindowOpen();
		IWorkbenchWindow window = getWindowConfigurer().getWindow();
		//窗口最大化
		window.getShell().setMaximized(true);
		ViewManager.hideView(InfoView.ID);
	}
	
	@Override
	public boolean preWindowShellClose() {
//		if (Constants.CURRENT_PRJ_NAME != null) {
//			EventManager.getDefault().notify(EventConstants.DEVICE_SAVE, null);
//		}
		return super.preWindowShellClose();
	}
    
}
