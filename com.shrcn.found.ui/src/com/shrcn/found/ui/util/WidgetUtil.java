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

package com.shrcn.found.ui.util;

import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.widgets.FormText;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.forms.widgets.TableWrapData;
import org.eclipse.ui.forms.widgets.TableWrapLayout;

import com.shrcn.found.ui.UIConstants;
import com.shrcn.found.ui.widget.FormWidgetFactory;

public class WidgetUtil {

	public static int computeStringLength(Control control, String s) {
		GC gc = new GC(control);
		char[] chars = s.toCharArray();
		int length = 0;
		for (int i = 0; i < chars.length; i++) {
			length += gc.getAdvanceWidth(chars[i]);
		}
		gc.dispose();
		return length;
	}

	public static Label createLabel(Composite parent) {
		return createLabel(parent, "");
	}

	protected static Label createLabel(Composite parent, int span) {
		Label label = getToolkit().createLabel(parent, "");
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 4;
		label.setLayoutData(gd);
		return label;
	}

	public static FormText createFormText(Composite parent, String text,
			boolean parseTags, boolean expandURLs) {
		FormText rtext = getToolkit().createFormText(parent, false);
		TableWrapData td = new TableWrapData(TableWrapData.FILL);
		rtext.setLayoutData(td);
		rtext.setText(text, parseTags, expandURLs);
		return rtext;
	}

	public static FormText createFormText(Composite parent, String text) {
		return createFormText(parent, text, false, false);
	}

	public static FormText createFormText(Composite parent) {
		return createFormText(parent, "", false, false);
	}

	public static FormWidgetFactory getToolkit() {
		return FormWidgetFactory.getInstance();
	}

	public static Label createBoldLabel(Composite parent) {
		return createBoldLabel(parent, "");
	}

	public static Label createBoldLabel(Composite parent, String text) {
		Label label = getToolkit().createLabel(parent, text);
		label.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		label.setFont(JFaceResources.getFontRegistry().getBold(
				JFaceResources.DEFAULT_FONT));
		return label;
	}

	public static Label createLabel(Composite parent, String text) {
		Label label = getToolkit().createLabel(parent, text);
		return label;
	}

	public static Button createButton(Composite parent, String text) {
		Button button = getToolkit().createButton(parent, text, SWT.PUSH);
		return button;
	}
	

	/**
	 * 创建标题区域
	 * @param parent
	 * @param title
	 * @param msg
	 * @return
	 */
	public static Composite createTitleArea(Composite parent, String title, String msg) {
		Composite container = new Composite(parent, SWT.NONE);
		TableWrapLayout gridlayout = new TableWrapLayout();
		gridlayout.leftMargin = 5;
		gridlayout.rightMargin = 10;
		gridlayout.topMargin = 5;
		gridlayout.verticalSpacing = 20;
		gridlayout.numColumns = 1;
		container.setLayout(gridlayout);
		
		Section labName = WidgetUtil.getToolkit().createSection(container, Section.EXPANDED);
		labName.setText(title);
		labName.setBackground(UIConstants.PANEL_BACK);
		TableWrapData td = new TableWrapData(TableWrapData.FILL);
		td.grabHorizontal = true;
		labName.setLayoutData(td);
		WidgetUtil.getToolkit().createCompositeSeparator(labName);

		final Composite compTab = WidgetUtil.getToolkit().createComposite(labName);
		compTab.setBackground(UIConstants.PANEL_BACK);
		TableWrapData tabTd = new TableWrapData(TableWrapData.FILL);
		tabTd.grabHorizontal = true;
		tabTd.grabVertical = true;
		compTab.setLayoutData(tabTd);
		compTab.setLayout(new FillLayout());
		
		final Text txtMsg = new Text(compTab, SWT.BORDER | SWT.V_SCROLL | SWT.MULTI | SWT.READ_ONLY );
		txtMsg.setText(msg);
		
		labName.setClient(compTab);
		return container;
	}
}
