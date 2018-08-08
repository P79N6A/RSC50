/**
 * Copyright (c) 2007-2010 chenchun.
 * All rights reserved. This program is an application based on tcp/ip.
 */
package com.shrcn.found.ui.app;

import org.eclipse.jface.action.ICoolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.internal.WorkbenchWindow;
import org.sf.feeling.swt.win32.extension.widgets.CToolBar;
import org.sf.feeling.swt.win32.extension.widgets.MenuBar;
import org.sf.feeling.swt.win32.extension.widgets.Separator;
import org.sf.feeling.swt.win32.extension.widgets.ThemeConstants;
import org.sf.feeling.swt.win32.extension.widgets.theme.ThemeRender;


/**
 * 
 * @author 陈春(mailto:chench80@126.com)
 * @version 1.0, 2014-4-28
 */
/**
 * $Log$
 */
public class TopBanner extends Composite {

	private static final ThemeRender theme = ThemeRender.getThemeRender(ThemeConstants.STYLE_OFFICE2007);
	private MenuBar menubar;
	private CToolBar toolbar;
	private IWorkbenchWindowConfigurer config;
	private WorkbenchWindow window;
	
	public static boolean isOpen = true;
	
	public TopBanner(Composite parent, IWorkbenchWindowConfigurer config) {
		super(parent, SWT.NONE);
		this.config = config;
		this.window = (WorkbenchWindow) config.getWindow();
		createContents();
		isOpen = false;
	}
	
	private void createContents() {
		GridLayout layout = new GridLayout( );
		layout.marginWidth = layout.marginHeight = layout.verticalSpacing = 0;
		setLayout( layout );
		
		 if (config.getShowMenuBar()) {
			menubar = new MenuBar( this, SWT.NONE, theme );
			menubar.getControl( )
					.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
	
			Separator menuSeparator = new Separator( this, SWT.SHADOW_IN | SWT.HORIZONTAL );
			menuSeparator.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
			setLayoutData( menuSeparator, true );
		 }
		
		toolbar = new CToolBar( this, CToolBar.SMALL_ICON, theme );
		toolbar.getControl( )
				.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );

		Separator toolSeparator = new Separator( this, SWT.SHADOW_IN | SWT.HORIZONTAL );
		toolSeparator.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
		setLayoutData( toolSeparator, true );
		
		refresh(true);
	}
	
	private void setLayoutData( Control control, boolean exclude )
	{
		GridData gd = (GridData) control.getLayoutData( );
		gd.exclude = exclude;
		control.setLayoutData( gd );
		control.setVisible( !exclude );
		control.getParent( ).layout( );
	}
	
	@Override
	public void setVisible(boolean visible) {
		super.setVisible(visible);
		if (config.getShowMenuBar())
			menubar.getControl().setVisible(visible);
		toolbar.getControl().setVisible(visible);
	}

	public void refresh(final boolean reload) {
		Display.getDefault().syncExec(new Runnable() {
			@Override
			public void run() {
				MenuToolFactory menuToolFactory = MenuToolFactory.getInstance();
				if (menubar != null) {
					if (reload)
						menuToolFactory.reloadMenuBar();
					menubar.getMenu().dispose();
					MenuManager menuBarManager = window.getMenuBarManager();
					menuBarManager.update();
					MenuToolHelper.createSystemMenu(menubar, menuBarManager);
					menubar.refresh();
				}
				if (toolbar != null) {
					if (reload)
						menuToolFactory.reloadToolBar();
					toolbar.removeAll();
					ICoolBarManager coolBarManager = window.getCoolBarManager2();
					coolBarManager.update(true);
					MenuToolHelper.createToolBar(toolbar, coolBarManager);
					toolbar.refresh();
				}
				layout();
			}
		});
	}
}
