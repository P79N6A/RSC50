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

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import com.shrcn.found.common.event.EventConstants;
import com.shrcn.found.common.event.EventManager;
import com.shrcn.found.ui.UIConstants;
import com.shrcn.found.ui.util.IconsManager;
import com.shrcn.found.ui.util.ImageConstants;
import com.shrcn.found.ui.util.SwtUtil;

/**
 * The title in the tabbed property sheet page.
 * 
 * @author Anthony Hunter
 */
public class TabbedPropertyTitle extends Composite {

	private CLabel label;

	private Image image = null;

	private String text = null;

	private static final String BLANK = ""; //$NON-NLS-1$

	/**
	 * Width of the margin that will be added around the control.
	 */
	public int marginWidth = 0;

	/**
	 * Height of the margin that will be added around the control.
	 */
	public int marginHeight = 0;

	/**
	 * Constructor for TabbedPropertyTitle.
	 * 
	 * @param parent
	 *            the parent composite.
	 * @param factory
	 *            the widget factory for the tabbed property sheet
	 */
	public TabbedPropertyTitle(Composite parent, FormWidgetFactory factory) {
		super(parent, SWT.NO_FOCUS);

		this.addPaintListener(new PaintListener() {

			public void paintControl(PaintEvent e) {
				if (image == null && (text == null || text.equals(BLANK))) {
					label.setVisible(false);
				} else {
					label.setVisible(true);
				}
			}
		});

		GridLayout gridLayout = new GridLayout(2, false);
		gridLayout.marginWidth = 0;
		gridLayout.marginHeight = 0;
		gridLayout.horizontalSpacing = 0;
		setLayout(gridLayout);

		label = factory.createCLabel(this, BLANK);
		label.setBackground(new Color[] { UIConstants.WHITE,
				UIConstants.TITLE_BACK }, new int[] { 100 }, true);
		label.setFont(UIConstants.IED_TITLE);
		label.setForeground(UIConstants.DARK_BLUE);
		label.setLayoutData(SwtUtil.hf_gd);

		CLabel lbClose = factory.createCLabel(this, BLANK);
		lbClose.setBackground(new Color[] { UIConstants.WHITE,
				UIConstants.TITLE_BACK }, new int[] { 100 }, true);
		lbClose.setImage(IconsManager.getInstance().getImage(
				ImageConstants.EDIT_DELETE));
		lbClose.setCursor(new Cursor(Display.getDefault(), SWT.CURSOR_HAND));
		lbClose.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				EventManager.getDefault().notify(EventConstants.CLEAR_CONFIG, null);
			}
		});
	}

	/**
	 * Set the text label.
	 * 
	 * @param text
	 *            the text label.
	 */
	public void setTitle(String text, Image image) {
		this.text = text;
		this.image = image;
		if (text != null) {
			label.setText(text);
		} else {
			label.setText(BLANK);
		}
		label.setImage(image);
		redraw();
	}

	/**
	 * @return the height of the title.
	 */
	public int getHeight() {
		Shell shell = new Shell();
		GC gc = new GC(shell);
		gc.setFont(getFont());
		Point point = gc.textExtent(BLANK);
		point.x++;
		int textOrImageHeight = Math.max(point.x, 16);
		gc.dispose();
		shell.dispose();
		return textOrImageHeight + 8;
	}
}
