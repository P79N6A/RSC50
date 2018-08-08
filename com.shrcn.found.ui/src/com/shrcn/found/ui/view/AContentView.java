package com.shrcn.found.ui.view;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;

import com.shrcn.found.common.event.Context;
import com.shrcn.found.common.event.EventConstants;
import com.shrcn.found.common.event.EventManager;
import com.shrcn.found.common.event.IEventHandler;
import com.shrcn.found.common.util.ObjectUtil;
import com.shrcn.found.ui.UIConstants;
import com.shrcn.found.ui.editor.IConfigEditor;
import com.shrcn.found.ui.editor.IEditorInput;

public abstract class AContentView extends ViewPart implements IEventHandler {

	public static final String ID = UIConstants.View_Content_ID;
	
	protected Composite container;
	protected IConfigEditor cfgEditor;
	
	public void createPartControl(Composite parent) {
		container = new Composite(parent, SWT.NONE);
		EventManager.getDefault().registEventHandler(this);
	}
	
	@Override
	public void dispose() {
		EventManager.getDefault().removeEventHandler(this);
		super.dispose();
	}

	public void setFocus() {
	}

	protected void openEditor(final IEditorInput input) {
		if (cfgEditor != null) {
			IEditorInput currInput = cfgEditor.getInput();
			if (input.getEditorId().equals(currInput.getEditorId())) {
				if (isOpened(currInput, input))
					return;
			}
			if (!saveEditContent())
				return;
			cfgEditor.dispose();
		}
//		IRunnableWithProgress openProgress = new IRunnableWithProgress() {
//			@Override
//			public void run(IProgressMonitor monitor)
//					throws InvocationTargetException, InterruptedException {
//				monitor.setTaskName("正在打开" + input.getEditorName() + "......");
//				Display.getDefault().syncExec(new Runnable() {
//					@Override
//					public void run() {
						cfgEditor = (IConfigEditor) ObjectUtil.newInstance(getClass(), input.getEditorId(), 
								new Class<?>[] {Composite.class, IEditorInput.class}, 
								container, input);
						container.layout();
//					}
//				});
//			}};
//		ProgressManager.execute(openProgress, false);
	}
	
	protected abstract boolean isOpened(IEditorInput currInput, IEditorInput input);
	
	protected void closeEditor(boolean isSave) {
		if (isSave && !saveEditContent())
			return;
		cfgEditor.dispose();
		cfgEditor = null;
	}
	
	protected boolean saveEditContent() {
		if (cfgEditor != null)
			return cfgEditor.doSave();
		return true;
	}

	protected void editContent() {
		cfgEditor.setDirty(true);
	}

	@Override
	public void execute(Context context) {
		String event = context.getEventName();
		if (EventConstants.OPEN_CONFIG.equals(event)) {
			IEditorInput input = (IEditorInput) context.getData();
			openEditor(input);
			return;
		}
		if (cfgEditor == null || cfgEditor.isDisposed())
			return;
		if (EventConstants.CLEAR_CONFIG.equals(event)) {
			closeEditor(true);
		} else if (EventConstants.REFRESH_EIDTOR.equals(event)) {
			cfgEditor.refresh();
		} else if (EventConstants.DEVICE_SAVE.equals(event)) {
			saveEditContent();
		}
	}
}
