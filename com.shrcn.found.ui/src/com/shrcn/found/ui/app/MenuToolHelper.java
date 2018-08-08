/**
 * Copyright (c) 2007-2010 chenchun.
 * All rights reserved. This program is an application based on tcp/ip.
 */
package com.shrcn.found.ui.app;

import java.util.List;

import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.ICoolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.action.ToolBarContributionItem;
import org.eclipse.jface.bindings.keys.KeySequence;
import org.eclipse.jface.bindings.keys.SWTKeySupport;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.sf.feeling.swt.win32.extension.widgets.CMenu;
import org.sf.feeling.swt.win32.extension.widgets.CMenuItem;
import org.sf.feeling.swt.win32.extension.widgets.CToolBar;
import org.sf.feeling.swt.win32.extension.widgets.CToolItem;
import org.sf.feeling.swt.win32.extension.widgets.MenuBar;
import org.sf.feeling.swt.win32.extension.widgets.MenuHolderManager;
import org.sf.feeling.swt.win32.extension.widgets.Shortcut;

import com.shrcn.found.common.util.StringUtil;
import com.shrcn.found.ui.action.GroupAction;
import com.shrcn.found.ui.action.MenuAction;

/**
 * 
 * @author 陈春(mailto:chench80@126.com)
 * @version 1.0, 2014-4-28
 */
/**
 * $Log$
 */
public class MenuToolHelper {
	
	/**
	 * 根据MenuManager创建菜单
	 * @param menubar
	 * @param menuManager
	 */
	public static void createSystemMenu(MenuBar menubar, MenuManager menuManager) {
		IContributionItem[] roots = menuManager.getItems();
		int index = 0;
		for (IContributionItem root : roots) {
			if (root instanceof MenuManager) {
				MenuManager menuRoot = (MenuManager) root;
				if (menuRoot.getItems().length < 1)
					continue;
				String rootText = menuRoot.getMenuText();
				CMenuItem item = new CMenuItem(rootText, SWT.NONE );
				menubar.getMenu( ).addItem( index, item );
				CMenu menu = new CMenu( );
				item.setMenu( menu );
				
				fillMenus(menu, menuRoot);
				index++;
			}
		}
		MenuHolderManager.registryShortcut(menubar);
	}
	
	private static void fillMenus(CMenu menu, MenuManager root) {
		int index = 0;
		for (IContributionItem item : root.getItems()) {
			if (item instanceof ActionContributionItem) {
				ActionContributionItem actionItem = (ActionContributionItem) item;
				final CMenuItem menuItem = createMenuItem(actionItem.getAction());
				menu.addItem(index, menuItem );
				index++;
			} else if (item instanceof Separator) {
				menu.addItem(index, new CMenuItem( null, SWT.SEPARATOR ) );
				index++;
			} else if (item instanceof MenuManager) {
				String rootText = ((MenuManager)item).getMenuText();
				CMenuItem subRootItem = new CMenuItem(rootText, SWT.NONE );
				menu.addItem(index, subRootItem );
				index++;
				CMenu subMenu = new CMenu( );
				subRootItem.setMenu( subMenu );
				
				fillMenus(subMenu, (MenuManager) item);
			}
		}
	}

	private static CMenuItem createMenuItem(final IAction action) {
		setShell(action);
		final CMenuItem cItem = new CMenuItem(SWT.NONE);
		String text = getMenuText(action);
		String[] splits = text.split("\t");
		cItem.setText(splits[0]);
		ImageDescriptor imageDescriptor = action.getImageDescriptor();
		if (imageDescriptor != null)
			cItem.setImage( imageDescriptor.createImage() );
		cItem.setEnabled(action.isEnabled());
		if (splits.length > 1) {
			Shortcut shortcut = new Shortcut(splits[1]);
			cItem.setShortcut(shortcut);
		}
		cItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (action.isEnabled()) {
					action.run();
				}
			}});
		if (action instanceof GroupAction) {
			CMenu itemMenu = new CMenu( );
			cItem.setMenu( itemMenu );
			List<MenuAction> subactions = ((GroupAction)action).getSubactions();
			if (subactions != null) {
				for (MenuAction subaction : subactions) {
					itemMenu.addItem(createMenuItem(subaction));
				}
			}
		}
		return cItem;
	}
	
	public static String getMenuText(final IAction action) {
		String menuText = action.getText();
		int accelerator = action.getAccelerator();
		if (accelerator > 0) {
			final KeySequence keySequence = KeySequence
				.getInstance(SWTKeySupport.convertAcceleratorToKeyStroke(accelerator));
			String shortCut = keySequence.format();
			return menuText + "\t" + shortCut; //$NON-NLS-1$
		}
		return menuText;
	}
	
	/**
	 * 根据ICoolBarManager创建工具栏
	 * @param toolbar
	 * @param coolBarManager
	 */
	public static void createToolBar(CToolBar toolbar, ICoolBarManager coolBarManager) {
		IContributionItem[] items = coolBarManager.getItems();
		if (items.length > 0) {
			int index = 0;
			for (IContributionItem item : items) {
				if (item instanceof ToolBarContributionItem) {
					ToolBarContributionItem currItem = (ToolBarContributionItem) item;
					if (!StringUtil.isEmpty(currItem.getId())){
						IWorkbench workbench = PlatformUI.getWorkbench();
						IWorkbenchPage activePage = workbench.getWorkbenchWindows()[0].getActivePage();
						if (activePage == null)
							continue;
						IEditorPart activeEditor = activePage.getActiveEditor();
						if (activeEditor == null || activeEditor != null && !(activeEditor.getClass().getName().equals(currItem.getId())))
							continue;
					}
					if (toolbar.getItemCount() > 0) {// 分割两组菜单项
						toolbar.addItem(index, new CToolItem(SWT.SEPARATOR));
						index++;
					}
					for (IContributionItem toolItem : currItem.getToolBarManager().getItems()) {
						if (toolItem instanceof ActionContributionItem) {
							ActionContributionItem actionItem = (ActionContributionItem) toolItem;
							final IAction action = actionItem.getAction();
							setShell(action);
							CToolItem cToolItem = new CToolItem("", SWT.NONE);
							ImageDescriptor imageDescriptor = action.getImageDescriptor();
							if (imageDescriptor != null)
								cToolItem.setImage(imageDescriptor.createImage());
							cToolItem.setEnabled(action.isEnabled());
							cToolItem.setText(action.getText());
							cToolItem.setToolTip(action.getToolTipText());
							cToolItem.addSelectionListener(new SelectionAdapter() {
								public void widgetSelected(SelectionEvent event) {
									if (action.isEnabled()) {
										action.run();
									}
								}
							});
							if (action instanceof GroupAction) {
								CMenu itemMenu = new CMenu( );
								cToolItem.setMenu( itemMenu );
								List<MenuAction> subactions = ((GroupAction)action).getSubactions();
								if (subactions != null) {
									for (MenuAction subaction : subactions) {
										itemMenu.addItem(createMenuItem(subaction));
									}
								}
							}
							toolbar.addItem(index, cToolItem);
							index++;
						} else if (toolItem instanceof Separator) {
							toolbar.addItem(index, new CToolItem(SWT.SEPARATOR));
							index++;
						}
					}
				}
			}
		}
	}
	
	private static void setShell(IAction action) {
		if (action instanceof MenuAction) {
			MenuAction menuAction = (MenuAction)action;
			if (menuAction.getShell()==null)
				menuAction.setShell(Display.getCurrent().getActiveShell());
		}
	}
}
