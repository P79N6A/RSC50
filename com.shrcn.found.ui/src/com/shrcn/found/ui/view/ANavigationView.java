/**
 * Copyright (c) 2007-2009 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based on IEC61850 SCT.
 */
package com.shrcn.found.ui.view;

import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;

import com.shrcn.found.common.event.Context;
import com.shrcn.found.common.event.EventConstants;
import com.shrcn.found.common.event.EventManager;
import com.shrcn.found.common.event.IEventHandler;
import com.shrcn.found.common.util.StringUtil;
import com.shrcn.found.ui.app.MenuToolFactory;
import com.shrcn.found.ui.editor.BaseEditorInput;
import com.shrcn.found.ui.model.ConfigTreeEntry;
import com.shrcn.found.ui.model.IEDEntry;
import com.shrcn.found.ui.model.ITreeEntry;
import com.shrcn.found.ui.tree.DeviceTreeViewer;


/**
 * 
 * @author 孙春颖(mailto:scy@shrcn.com)
 * @version 1.0, 2014-05-27
 */
public abstract class ANavigationView extends ViewPart implements IEventHandler {

	protected DeviceTreeViewer cfgViewer;
	
	@Override
	public void createPartControl(Composite parent) {
		createTV(parent);
		addListeners();
		loadProject();
		EventManager.getDefault().registEventHandler(this);
	}
	
	protected abstract void createTV(Composite parent);

	protected void addListeners() {
		cfgViewer.addSelectionChangedListener(new ISelectionChangedListener() {
			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				ITreeEntry selEntry = cfgViewer.getSelTreeEntry();
				if (selEntry == null)
					return;
				if (selEntry instanceof ConfigTreeEntry) {
					openConfig(selEntry);
				}/* else {
					EventManager.getDefault().notify(EventConstants.CLEAR_CONFIG, null);
				}*/
			}
		});
	}
	
	protected void openConfig(ITreeEntry selEntry) {
		ConfigTreeEntry configEntry = (ConfigTreeEntry) selEntry;
		IEDEntry iedEntry = (IEDEntry) getIEDEntry(configEntry);
		String editorId = configEntry.getEditorId();
		if (StringUtil.isEmpty(editorId))
			return;
		BaseEditorInput input = getInput(configEntry, iedEntry, editorId);
		setInputValue(input, iedEntry);
		EventManager.getDefault().notify(EventConstants.OPEN_CONFIG, input);
	}
	
	protected BaseEditorInput getInput(ConfigTreeEntry configEntry, IEDEntry iedEntry, String editorId) {
		return new BaseEditorInput(configEntry.getName(),
				configEntry.getIcon(), editorId, (iedEntry==null) ? "" : iedEntry.getName());
	}
	
	/**
	 * 设置编辑器特殊属性
	 * 
	 * @param input
	 * @param iedEntry
	 */
	protected void setInputValue(BaseEditorInput input, IEDEntry iedEntry){
	}
	
	public IEDEntry getIEDEntry(ConfigTreeEntry configEntry) {
		ITreeEntry entry = configEntry.getParent();
		while (entry != null) {
			if (entry instanceof IEDEntry) {
				return (IEDEntry) entry;
			} else {
				entry = entry.getParent();
			}
		}
		return null;
	}

	public abstract void loadProject();
	
	/**
	 * 刷新指定节点
	 * @param entry
	 */
	protected void refresh(ITreeEntry entry) {
		cfgViewer.refresh(entry);
		cfgViewer.expandToLevel(entry, TreeViewer.ALL_LEVELS);
	}
	
	/**
	 * Passing the focus request to the viewer's control.
	 */
	public void setFocus() {
		cfgViewer.getControl().setFocus();
	}
	

	@Override
	public void execute(Context context) {
		String event = context.getEventName();
		MenuToolFactory menuToolFactory = MenuToolFactory.getInstance();
		if (EventConstants.PROJECT_CREATE.equals(event)) {
			createProject();
		} else if (EventConstants.PROJECT_OPEN.equals(event)) {
			openProject();
		} else if (EventConstants.PROJECT_CLOSE.equals(event)) {
			closeProject();
		} else if (EventConstants.PROJECT_OPEN_EXP.equals(event)) {
			exportProject();
		} else if (EventConstants.PROJECT_OPEN_IMP.equals(event)) {
			importProject();
		}
		menuToolFactory.disableActions();
	}
	
	/**
	 * 创建工程。
	 */
	protected abstract void createProject();

	
	/**
	 * 打开工程。
	 */
	protected abstract void openProject();

	/**
	 * 导出工程。
	 */
	protected abstract void exportProject();
	
	/**
	 * 导入工程。
	 */
	protected abstract void importProject();
	
	/**
	 * 关闭工程操作.
	 */
	protected abstract void closeProject();
}
