/**
 * Copyright (c) 2007-2014 上海思源弘瑞自动化有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based on substation automation.
 */
package com.shrcn.found.ui.app;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IPartListener;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.internal.WorkbenchPage;
import org.eclipse.ui.part.EditorPart;
import org.sf.feeling.swt.win32.extension.widgets.ShellWrapper;

import com.shrcn.found.common.util.ObjectUtil;

/**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2014-3-27
 */
/**
 * $Log$
 */
public class WorkbenchWindowHelper {
	/**
	 * 修改窗口样式。
	 * @param shell
	 * @param theme ThemeConstants.STYLE_VISTA
	 * 				ThemeConstants.STYLE_OFFICE2007
	 * 				ThemeConstants.STYLE_GLOSSY
	 */
	public static void setTheme(Shell shell, String theme) {
		ShellWrapper wrapper = new ShellWrapper(shell);
		wrapper.installTheme(theme);
	}
	
	/**
	 * 设置工具栏背景。
	 * @param window
	 * @param timage
	 */
	public static void setToorbarBG(IWorkbenchWindow window, Image timage) {
		Object[] childrens = window.getShell().getChildren();
		for (int i = 0; i < childrens.length; i++) {
			String clazz = childrens[i].getClass().getName();
			if (clazz.endsWith("CBanner")) {
				((Composite) childrens[i]).setBackgroundImage(timage);
				((Composite) childrens[i]).setBackgroundMode(SWT.INHERIT_FORCE);
			}
		}
	}
	
	/**
	 * 设置状态栏背景。
	 * @param window
	 * @param image
	 */
	public static void setStausLineBG(IWorkbenchWindow window, Image image) {
		Object[] childrens = window.getShell().getChildren();
		for (int i = 0; i < childrens.length; i++) {
			String clazz = childrens[i].getClass().getName();

			if (clazz.endsWith("StatusLine")) {
				((Composite) childrens[i]).setBackgroundImage(image);
				((Composite) childrens[i]).setBackgroundMode(SWT.INHERIT_FORCE);
			}
		}
	}
	
	/**
	 * 设置菜单背景。
	 * @param menu
	 * @param mimage
	 */
	public static void setMenuBG(Menu menu, Image mimage) {
		ObjectUtil.invoke(menu, "setBackgroundImage", new Class[] { Image.class }, mimage);
	}
	
	/**
	 * 设置窗口背景。
	 * @param window
	 * @param menu
	 */
	public static void setWindowBG(IWorkbenchWindow window, Menu menu) {
		LookAndFeel lookfeel = LookAndFeel.getDefault();
		setMenuBG(menu, lookfeel.getMenuImage());
		setToorbarBG(window, lookfeel.getToolBarImage());
		setStausLineBG(window, lookfeel.getToolBarImage());
		window.getShell().setBackground(lookfeel.getShellColor());
		window.getShell().redraw();
	}
	

	/**
	 * 设置TabFolder背景
	 * @param windowConfigurer
	 * @param color
	 */
	public static void setWorkbenchPageColor(IWorkbenchWindowConfigurer windowConfigurer, Color color) {
		if (windowConfigurer.getWindow() == null) {
			return;
		}
		for (IWorkbenchPage obj : windowConfigurer.getWindow().getPages()) {
			WorkbenchPage page = (WorkbenchPage) obj;
			Composite client = page.getClientComposite();
			if (client == null)
				continue;
			client.setBackground(color);
			Control[] children = client.getChildren();
			for (final Control control : children) {
				control.setBackground(color);
			}
		}
	}

	public static void setEditorAreaBG(IWorkbenchWindowConfigurer windowConfigurer, Image image) {
		if (windowConfigurer.getWindow() == null) {
			return;
		}
		if (windowConfigurer.getWindow().getActivePage() == null) {
			return;
		}
		WorkbenchPage page = (WorkbenchPage) windowConfigurer.getWindow()
				.getActivePage();
		Composite client = page.getClientComposite();
		Control[] children = client.getChildren();
		Composite child = (Composite) children[0];
		Control[] controls = child.getChildren();

		for (final Control control : controls) {
			if (control instanceof CTabFolder) {
				CTabFolder tabfolder = (CTabFolder) control;
//				Listener[] listeners = tabfolder.getListeners(SWT.MenuDetect);
//				if (listeners != null) {
//					for (int i = 0; i < listeners.length; i++) {
//						// 屏蔽系统右键菜单
//						tabfolder.removeListener(SWT.MenuDetect, listeners[i]);
//					}
//				}
//				Listener[] listeners2 = tabfolder.getListeners(SWT.DragDetect);
//				if (listeners2 != null) {
//					for (int i = 0; i < listeners2.length; i++) {
//						// 屏蔽编辑器默认可拖动的属性
//						tabfolder.removeListener(SWT.DragDetect, listeners2[i]);
//					}
//				}
				tabfolder.setBackgroundImage(image);
				tabfolder.setBackgroundMode(SWT.INHERIT_FORCE);
			}
		}
	}
	
	public static void addPartListener(final IWorkbenchWindowConfigurer windowConfigurer, final Color color) {
		windowConfigurer.getWindow().getActivePage().addPartListener(
			new IPartListener() {

				public void partActivated(IWorkbenchPart part) {
					if (part instanceof EditorPart) {
						setWorkbenchPageColor(windowConfigurer, color);
					}
				}

				public void partBroughtToTop(IWorkbenchPart part) {
					if (part instanceof EditorPart) {
						setWorkbenchPageColor(windowConfigurer, color);
					}
				}

				public void partClosed(IWorkbenchPart part) {
					if (part instanceof EditorPart) {
						setWorkbenchPageColor(windowConfigurer, color);
					}
				}

				public void partDeactivated(IWorkbenchPart part) {
					if (part instanceof EditorPart) {
						setWorkbenchPageColor(windowConfigurer, color);
					}
				}

				public void partOpened(IWorkbenchPart part) {
					if (part instanceof EditorPart) {
						setWorkbenchPageColor(windowConfigurer, color);
					}
				}
			});
	}
}
