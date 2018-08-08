/*******************************************************************************
 * Copyright (c) 2007 cnfree.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  cnfree  - initial API and implementation
 *******************************************************************************/
package com.shrcn.found.ui.widget;

import org.eclipse.swt.widgets.Composite;

public abstract class TabPage {
	public abstract void buildUI(Composite parent);

	public abstract Composite getControl();

	public abstract String getDisplayName();

	public void refresh() {
		FormWidgetFactory.getInstance().paintFormStyle(getControl());
		FormWidgetFactory.getInstance().adapt(getControl());
	}

	public void deActivate() {
	}

	public void activate() {
	}
}
