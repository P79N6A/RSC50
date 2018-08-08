/**
 * Copyright (c) 2007-2010 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based on IEC61850 SCT.
 */
package com.shrcn.found.ui.app;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.ICoolBarManager;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.internal.Workbench;

import com.shrcn.found.common.Constants;
import com.shrcn.found.common.event.EventConstants;
import com.shrcn.found.common.event.EventManager;
import com.shrcn.found.common.log.SCTLogger;
import com.shrcn.found.ui.action.MenuAction;
import com.shrcn.found.ui.action.ShowViewAction;
import com.shrcn.found.ui.util.ExtensionUtil;
import com.shrcn.found.ui.util.IconsManager;
import com.shrcn.found.ui.view.ViewManager;

/**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2010-11-23
 */
/**
 * $Log: ActionBarAdvisorHelper.java,v $
 * Revision 1.5  2013/05/17 08:32:27  cchun
 * Update:添加disableActions()
 *
 * Revision 1.4  2013/05/17 05:03:44  cchun
 * Fix Bug: 修复enableActions()
 *
 * Revision 1.3  2013/04/24 05:56:18  zsy
 * Update: 执行setIcon()
 *
 * Revision 1.2  2013/04/23 12:03:32  zsy
 * Update:修改取图标的路径
 *
 * Revision 1.1  2013/03/29 09:36:52  cchun
 * Add:创建
 *
 * Revision 1.7  2012/11/27 06:22:31  cchun
 * Fix Bug:修复菜单、工具栏不刷新的bug
 *
 * Revision 1.6  2011/09/15 08:41:31  cchun
 * Update:修改常量为可变化形式
 *
 * Revision 1.5  2011/05/24 06:40:04  cchun
 * Fix Bug:修复ActionFactory可能找不到action的bug
 *
 * Revision 1.4  2011/02/25 07:38:08  cchun
 * Update:添加图标
 *
 * Revision 1.3  2010/12/23 06:15:42  cchun
 * Fix Bug:修复禁用逻辑错误
 *
 * Revision 1.2  2010/12/01 08:26:04  cchun
 * Refactor:重构
 *
 * Revision 1.1  2010/11/23 06:13:24  cchun
 * Refactor:使用统一的菜单action管理工具
 *
 */
public class MenuToolFactory {
	
	private static final String extensionId = "com.shrcn.found.ui.AppMenuExtension";
	private static final int MENU_ACTION = 1;
	private static final int TOOL_ACTION = 2;
	private static final int BOTH_ACTION = 3;

	private static final String ACTION_FACTORY = ActionFactory.class.getName();
	private static final String SHOWVIEW = ShowViewAction.class.getName();
	private static final Separator SEPARATOR = new Separator();
	
	private static MenuToolFactory instance;
	private IExtension[] extensions;
	private Map<String, IAction> registry = new HashMap<String, IAction>();
	private List<IAction> disabledActions = new ArrayList<IAction>();
	private IMenuManager menuBar;
	private ICoolBarManager coolBar;
	
	private MenuToolFactory() {
		this.extensions = ExtensionUtil.getExtensions(extensionId);
	}
	
	public static MenuToolFactory getInstance() {
		if (instance == null) {
			instance = new MenuToolFactory();
		}
		return instance;
	}
	
	private void registAction(IAction action) {
		registry.put(action.getId(), action);
	}
	
	private IAction getAction(String id) {
		return registry.get(id);
	}
	
	private boolean isMenu(IAction action) {
		if (action instanceof MenuAction) {
			int style = ((MenuAction)action).getUistyle();
			return style == MENU_ACTION || style == BOTH_ACTION;
		}
		return true;
	}
	
	private boolean isTool(IAction action) {
		if (action instanceof MenuAction) {
			int style = ((MenuAction)action).getUistyle();
			return style == TOOL_ACTION || style == BOTH_ACTION;
		}
		return false;
	}
	
	/**
	 * 创建菜单项action
	 * @param window
	 */
	public void initActions(IWorkbenchWindow window) {
		//实例化action，并注册
		for (int i = 0; i < extensions.length; i++) {
			IConfigurationElement[] configurations = extensions[i].getConfigurationElements();
			for (int j = 0; j < configurations.length; j++) {
				IConfigurationElement configuration = configurations[j];
				initConfigAction(configuration, window);
			}
		}
	}
	
	/**
	 * 初始化action
	 * @param configuration
	 */
	private void initConfigAction(IConfigurationElement configuration, IWorkbenchWindow window) {
		String type = configuration.getName();
		if("action".equals(type)) {
			String className = configuration.getAttribute("class");
			if (!Constants.XQUERY && className.indexOf("com.shrcn.sct.iec61850.action.HistoryProjectAction")>-1) {
				return;
			}
			String icon = configuration.getAttribute("icon");
			String style = configuration.getAttribute("style");
			IAction action = null;
			if(ACTION_FACTORY.equals(className)) {
				String cmd = configuration.getAttribute("command");
				action = MenuActionFactory.createById(cmd, window);
				registAction(action);
			} else if(className.indexOf(SHOWVIEW)>-1) {
				String text = configuration.getAttribute("text");
				String viewId = configuration.getAttribute("viewId");
				action = new ShowViewAction(viewId, text);
				registAction(action);
			} else if(!Separator.class.getName().equals(className)) {
				String text = configuration.getAttribute("text");
				action = MenuActionFactory.createAction(className, text);
				registAction(action);
			}
			if (icon != null) {
				ImageDescriptor imgDesc = IconsManager.getInstance().getImageDescriptor(icon);
				action.setImageDescriptor(imgDesc);
			}
			if (action instanceof MenuAction) {
				style = style==null ? "1" : style;
				((MenuAction)action).setIcon(icon);
				((MenuAction)action).setUistyle(Integer.valueOf(style));
			}
		} else if("menu".equals(type)) {
			IConfigurationElement[] children = configuration.getChildren();
			for(IConfigurationElement child : children) {
				initConfigAction(child, window);
			}
		}
	}
	
	/**
	 * 填充菜单
	 * @param menuBar
	 */
	public void loadMenuBar(IMenuManager menuBar) {
		this.menuBar = menuBar;
		reloadMenuBar();
	}

	/**
	 * 重新加载菜单栏
	 */
	public void reloadMenuBar() {
		this.menuBar.removeAll();
    	String perspective = getPerspective();
		for (int i = 0; i < extensions.length; i++) {
			IConfigurationElement[] configurations = extensions[i].getConfigurationElements();
			for (int j = 0; j < configurations.length; j++) {
				IConfigurationElement configuration = configurations[j];
				String id = configuration.getAttribute("id");
				String text = configuration.getAttribute("text");
				String menuPerspective = configuration.getAttribute("perspective");
				if (menuPerspective != null && !perspective.equals(menuPerspective))
					continue;
				MenuManager rootMenu = new MenuManager(text, id);
				int menuNum = createMenuItems(configuration, rootMenu);
				if (menuNum > 0)
					this.menuBar.add(rootMenu);
			}
		}
	}

	public String getPerspective() {
		if (PlatformUI.getWorkbench().getWorkbenchWindowCount()==0)
			return ((Workbench)PlatformUI.getWorkbench()).getDefaultPerspectiveId();
		else
			return ViewManager.getWorkBenchPage().getPerspective().getId();
	}
	
	/**
	 * 初始化系统菜单
	 * @param configuration
	 * @param menu
	 */
	private int createMenuItems(IConfigurationElement configuration, MenuManager menu) {
		int menuNum = 0;
		IConfigurationElement[] cfgChildren = configuration.getChildren();
		for (IConfigurationElement cfgChild : cfgChildren) {
			String type = cfgChild.getName();
			String text = cfgChild.getAttribute("text");
			if ("menu".equals(type)) {
				String id = cfgChild.getAttribute("id");
				MenuManager subMenu = new MenuManager(text, id);
				createMenuItems(cfgChild, subMenu);
				menu.add(subMenu);
			} else if ("action".equals(type)) {
				String className = cfgChild.getAttribute("class");
				int idx = className.indexOf('/');
				if (idx != -1)
					className = className.substring(idx + 1);
				IAction action = null;
				if(ACTION_FACTORY.equals(className)) {
					String cmd = cfgChild.getAttribute("command");
					try {
						Field field = ActionFactory.class.getField(cmd);
						ActionFactory factory = (ActionFactory)field.get(null);
						action = getAction(factory.getId());
					} catch (Exception e) {
						SCTLogger.error("创建菜单项" + cmd + "出错。", e);
					}
				} else if(SHOWVIEW.equals(className)) {
					String viewId = cfgChild.getAttribute("viewId");
					action = getAction(viewId);
				} else {
					action = getAction(className + "." + text);
				}
				if (null != action && isMenu(action)) {
					menu.add(action);
					menuNum++;
					String disabled = cfgChild.getAttribute("disabled");
					if (disabled != null && !disabledActions.contains(action)) {
						action.setEnabled(!Boolean.valueOf(disabled));
						disabledActions.add(action);
					}
				} else {
//					menu.add(SEPARATOR);
				}
			}
		}
		return menuNum;
	}
	
	/**
	 * 填充菜单
	 * @param coolBar
	 */
	public void loadToolBar(ICoolBarManager coolBar) {
		this.coolBar = coolBar;
		reloadToolBar();
	}

	/**
	 * 重新加载工具栏
	 * @param coolBar
	 */
	public void reloadToolBar() {
		this.coolBar.removeAll();
		String perspective = getPerspective();
		IToolBarManager toolBar = new ToolBarManager(SWT.FLAT | SWT.RIGHT);
		this.coolBar.add(toolBar);
		for (int i = 0; i < extensions.length; i++) {
			IConfigurationElement[] configurations = extensions[i].getConfigurationElements();
			for (int j = 0; j < configurations.length; j++) {
				IConfigurationElement configuration = configurations[j];
				String menuPerspective = configuration.getAttribute("perspective");
				if (menuPerspective != null && !perspective.equals(menuPerspective))
					continue;
				List<IAction> actions = new ArrayList<IAction>();
				findToolActions(configuration, actions);
				if (toolBar.getItems().length > 0 && actions.size()>0)
					toolBar.add(SEPARATOR);
				for (IAction action : actions) {
					toolBar.add(action);
				}
			}
		}
	}
	
	private void findToolActions(IConfigurationElement configuration,
			List<IAction> actions) {
		IConfigurationElement[] cfgChildren = configuration.getChildren();
		for (IConfigurationElement cfgChild : cfgChildren) {
			String type = cfgChild.getName();
			if ("menu".equals(type)) {
				findToolActions(cfgChild, actions);
			} else if ("action".equals(type)) {
				String className = cfgChild.getAttribute("class");
				int idx = className.indexOf('/');
				if (idx != -1)
					className = className.substring(idx + 1);
				IAction action = null;
				if(SHOWVIEW.equals(className)) {
					String viewId = cfgChild.getAttribute("viewId");
					action = getAction(viewId);
				} else {
					action = getAction(className);
				}
				if (null != action && isTool(action)) {
					actions.add(action);
					String disabled = cfgChild.getAttribute("disabled");
					if (disabled != null && !disabledActions.contains(action)) {
						action.setEnabled(!Boolean.valueOf(disabled));
						disabledActions.add(action);
					}
				}
			}
		}
	}

	/**
	 * 使能禁用菜单
	 */
	public void enableActions() {
		setActionStatus(true);
	}
	
	/**
	 * 禁用菜单
	 */
	public void disableActions() {
		setActionStatus(false);
	}
	
	/**
	 * 切换禁用状态
	 */
	private void setActionStatus(boolean enabled) {
		for (IAction action : disabledActions) {
			if (action == null)
				continue;
			action.setEnabled(enabled);
		}
		refreshMenuTools();
	}

	/**
	 * 刷新工具栏
	 */
	public void refreshMenuTools() {
		EventManager.getDefault().notify(EventConstants.SYS_REFRESH_TOP_BAN, null);
	}
	
	/**
	 * 重新加载工具栏
	 */
	public void reloadMenuTools() {
		EventManager.getDefault().notify(EventConstants.SYS_RELOAD_TOP_BAN, null);
	}
	
	public List<IAction> getActions() {
		List<IAction> actions = new ArrayList<>();
		actions.addAll(registry.values());
		return actions;
	}

	public Map<String, IAction> getRegistry() {
		return registry;
	}
}
