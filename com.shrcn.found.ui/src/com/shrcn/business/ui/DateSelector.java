/**
 * Copyright (c) 2007-2013 上海思源弘瑞自动化有限公司. All rights reserved. 
 * This program is an eclipse Rich Client Application
 * designed for IED configuration and debuging.
 */
package com.shrcn.business.ui;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Widget;

import com.shrcn.found.common.Constants;
import com.shrcn.found.common.util.StringUtil;
import com.shrcn.found.ui.Activator;
import com.shrcn.found.ui.util.SwtUtil;

/**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2014-10-22
 */
/**
 * $Log$
 */
public class DateSelector extends Composite {
	private Text txtDate;
	private Button btSel;
	private String title;
	
	public DateSelector(Composite parent, String title) {
		super(parent, SWT.NONE);
		this.title = title;
		createContents();
	}
	
	private void createContents() {
		setLayout(new GridLayout(3, false));
		
		Label lbTitle = new Label(this, SWT.NONE);
		lbTitle.setText(title);
		txtDate = new Text(this, SWT.BORDER | SWT.READ_ONLY);
//		txtDate.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		txtDate.setLayoutData(new GridData(100, SWT.DEFAULT));
		btSel = SwtUtil.createPushButton(this, "", null);
		btSel.setImage(Activator.getImageDescriptor("icons/calendar.png").createImage());
		
		final CalendarHandler tooltip = new CalendarHandler(getShell(), txtDate);
		tooltip.activateClick(btSel);
	}
	
	public String getTimeStr() {
		return txtDate.getText();
	}
	
	public Timestamp getTimestamp() {
		String value = txtDate.getText();
		if (StringUtil.isEmpty(value))
			return null;
		Date date = null;
		try {
			date = new SimpleDateFormat(Constants.STD_DATE_FORMAT).parse(value);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return (date == null) ? null : new Timestamp(date.getTime());
	}

	protected static class CalendarHandler {
		private Shell  parentShell;
		private Shell  tipShell;
		private Widget tipWidget; // widget this tooltip is hovering over
		private Point  tipPosition; // the position being hovered over
		private DateTime date;
		private Text txtTarget;
		private boolean isShowing;
		
		public CalendarHandler(Shell parent, Text txtTarget) {
			this.parentShell = parent;
			this.txtTarget = txtTarget;
			createCalendar();
		}
		
		private void createCalendar() {
			final Display display = parentShell.getDisplay();
			tipShell = new Shell(parentShell, SWT.ON_TOP | SWT.TOOL);
			GridLayout gridLayout = new GridLayout();
			gridLayout.numColumns = 1;
			gridLayout.marginWidth = 2;
			gridLayout.marginHeight = 2;
			tipShell.setLayout(gridLayout);
	
			tipShell.setBackground(display.getSystemColor(SWT.COLOR_INFO_BACKGROUND));
			
			date = new DateTime(tipShell, SWT.CALENDAR);
			date.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					hide();
					Calendar cl = Calendar.getInstance();
					cl.set(Calendar.YEAR, date.getYear() );
					cl.set(Calendar.MONTH, date.getMonth() );
					cl.set(Calendar.DATE, date.getDay() );
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
			        String dateTime  = sdf.format(cl.getTime());
					txtTarget.setText(dateTime);
				}
			});
		}
		
		public void activateClick(final Control control) {
			control.addMouseListener(new MouseListener() {
				@Override
				public void mouseDoubleClick(MouseEvent e) {
				}
				@Override
				public void mouseDown(MouseEvent event) {
					if (isShowing) {
						hide();
						return;
					}
					Point pt = new Point (event.x, event.y);
					Widget widget = event.widget;
					if (widget == null) {
						tipShell.setVisible(false);
						tipWidget = null;
						return;
					}
					if (widget == tipWidget)
						return;
					tipWidget = widget;
					tipPosition = control.toDisplay(pt);
					tipShell.pack();
					setHoverLocation(tipShell, tipPosition);
					tipShell.setVisible(true);
					isShowing = true;
				}
				@Override
				public void mouseUp(MouseEvent e) {
				}
			});
		}
		
		private void hide() {
			if (tipShell.isVisible())
				tipShell.setVisible(false);
			tipWidget = null;
			isShowing = false;
		}
		
		/**
		 * Sets the location for a hovering shell
		 * @param shell the object that is to hover
		 * @param position the position of a widget to hover over
		 * @return the top-left location for a hovering box
		 */
		private void setHoverLocation(Shell shell, Point position) {
			Rectangle displayBounds = shell.getDisplay().getBounds();
			Rectangle shellBounds = shell.getBounds();
			shellBounds.x = Math.max(Math.min(position.x, displayBounds.width - shellBounds.width), 0);
			shellBounds.y = Math.max(Math.min(position.y + 16, displayBounds.height - shellBounds.height), 0);
			shell.setBounds(shellBounds);
		}
	}
}
