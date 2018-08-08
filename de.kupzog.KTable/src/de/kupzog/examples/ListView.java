/*
 * Copyright (C) 2004 by Friederich Kupzog Elektronik & Software
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
    
    Author: Friederich Kupzog  
    fkmk@kupzog.de
    www.kupzog.de/fkmk
*/

package de.kupzog.examples;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Text;

import de.kupzog.ktable.KTable;
import de.kupzog.ktable.KTableCellResizeListener;
import de.kupzog.ktable.KTableCellSelectionListener;
import de.kupzog.ktable.KTableSortComparator;
import de.kupzog.ktable.KTableSortOnClick;
import de.kupzog.ktable.KTableSortedModel;
import de.kupzog.ktable.SWTX;

/**
 * KTable example GUI.<p>
 * Demonstrates some usages of KTable.
 */

public class ListView {
	public static void main(String[] args) {
		// create a shell...
		Display display = new Display();
		Shell shell = new Shell(display);
		shell.setLayout(new FillLayout());
		shell.setText("KTable examples1");
		
		// put a tab folder in it...
		TabFolder tabFolder = new TabFolder(shell, SWT.NONE);
		
		createSortableTable(tabFolder);
	
		// display the shell...
		shell.setSize(600,600);
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}
	
	private void initCellEditor(final KTable table) {
		Text text = new Text(table, SWT.BORDER);
//		verifyListener = new VerifyListener() {
//			public void verifyText(VerifyEvent event) {
//				Text t = (Text) getCellEditor().getControl();
//				String oldText = t.getText();
//				String leftText = oldText.substring(0, event.start);
//				String rightText = oldText.substring(event.end, oldText
//						.length());
//				GC gc = new GC(t);
////				Point size = gc.textExtent(leftText + event.text + rightText);
//				Point size = ConstraintCaculator.getTextSize(leftText + event.text + rightText);
//				gc.dispose();
//				if (size.x != 0)
//					size = t.computeSize(size.x, SWT.DEFAULT);
//				getCellEditor().getControl().setSize(size); //size.x, size.y);
//				Rectangle rect=_figure.getBounds().getCopy();
//				rect.width = size.x + 8;
//				rect.height = size.y + 8;
//				_figure.setBounds(rect);
//			}
//		};
//		text.addVerifyListener(verifyListener);
//
//		String initialLabelText = ((Label)_figure).getText();
//		getCellEditor().setValue(initialLabelText);
//		IFigure figure = getEditPart().getFigure();
//		scaledFont = figure.getFont();
//		FontData data = scaledFont.getFontData()[0];
//		Dimension fontSize = new Dimension(0, data.getHeight());
//		_figure.translateToAbsolute(fontSize);
//		data.setHeight(fontSize.height);
//		scaledFont = new Font(null, data);
//
//		Point size = ConstraintCaculator.getTextSize(initialLabelText);
//		text.setSize(size);
//		text.setFont(scaledFont);
//		text.selectAll();
	}
	
	private static void buildContextMenu(final KTable table, final int col, final int row) {
		final Menu menu = new Menu(table);
		table.setMenu(menu);
		if (row == 0) {
			final MenuItem menuItem2 = new MenuItem(menu, SWT.NONE);
			menuItem2.setText("(&R)");
			menuItem2.addSelectionListener(new SelectionListener() {

				@Override
				public void widgetDefaultSelected(SelectionEvent e) {
//					System.out.println("widgetDefaultSelected(SelectionEvent e)");
				}

				@Override
				public void widgetSelected(SelectionEvent e) {
//					System.out.println("widgetSelected(SelectionEvent e)");
					Rectangle rect = table.getCellRect(col, row);
//					Object target = e.getSource();
//					System.out.println(target.getClass().getName());
//					Text text = new Text(table, )
				}

			});
			final MenuItem menuItem = new MenuItem(menu, SWT.NONE);
			menuItem.setText("(&I)");
			menuItem.addSelectionListener(new SelectionListener() {

				@Override
				public void widgetDefaultSelected(SelectionEvent e) {
					System.out.println("widgetDefaultSelected(SelectionEvent e)");
				}

				@Override
				public void widgetSelected(SelectionEvent e) {
					System.out.println("widgetSelected(SelectionEvent e)");
				}

			});
			final MenuItem menuItem1 = new MenuItem(menu, SWT.NONE);
			menuItem1.setText("(&A)");
		} else {
			final MenuItem menuItem = new MenuItem(menu, SWT.NONE);
			menuItem.setText("(&I)");
			final MenuItem menuItem1 = new MenuItem(menu, SWT.NONE);
			menuItem1.setText("(&A)");
		}
	}

    /**
     * Constructs a simple text table that demonstrates the creation of a sorted table.
     * <p>
     * Note that the only thing that is necessary to make the table itself sortable is
     * subclassing the <code>KTableSortedModel</code>. Then the sort() method is available
     * to custom handlers.<p>
     * This shows how to register some common listeners that make the table behave in an 
     * often seen way.
     */
    private static void createSortableTable(TabFolder tabFolder) {
    	
		TabItem item1 = new TabItem(tabFolder, SWT.NONE);
		item1.setText("Sortable Table");
		
		final Composite composite = new Composite(tabFolder, SWT.NONE);
		item1.setControl(composite);
		composite.setLayout(new FormLayout());
		final KTableSortedModel model = new SortableModelExample();
		

		final Composite comp_tb = new Composite(composite, SWT.NONE);
		final FormData fd_comp_tb = new FormData();
		fd_comp_tb.bottom = new FormAttachment(0, 225);
		fd_comp_tb.left = new FormAttachment(0, 5);
		comp_tb.setLayoutData(fd_comp_tb);
		comp_tb.setLayout(new FormLayout());
		
		final KTable table = new KTable(comp_tb, SWT.MULTI | SWT.V_SCROLL | SWT.H_SCROLL);
		final FormData fd_table = new FormData();
		fd_table.right = new FormAttachment(100, -5);
		fd_table.bottom = new FormAttachment(100, -5);
		fd_table.top = new FormAttachment(0, 5);
		fd_table.left = new FormAttachment(0, 0);
		table.setLayoutData(fd_table);
		table.setRedraw(true);
//		buildContextMenu(table);
		table.setModel(model);
		table.addCellSelectionListener(
			new KTableCellSelectionListener()
			{
			    public void cellSelected(int col, int row, int statemask) {
			    	// the idea is to map the row index back to the model index since the given row index
			    	// changes when sorting is done. 
			        int modelRow = model.mapRowIndexToModel(row);
					System.out.println("Cell ["+col+";"+row+"] selected. - Model row: "+modelRow);
					buildContextMenu(table, col, row);
				}
				
				public void fixedCellSelected(int col, int row, int statemask) {
					System.out.println("Header ["+col+";"+row+"] selected.");
					buildContextMenu(table, col, row);
				}
			}
		);
		// implement resorting when the user clicks on the table header:
		table.addCellSelectionListener(new KTableSortOnClick(table, 
				new SortComparatorExample(model, -1, KTableSortComparator.SORT_NONE)));
	
		table.addCellResizeListener(
			new KTableCellResizeListener()
			{
				public void columnResized(int col, int newWidth) {
					System.out.println("Column "+col+" resized to "+newWidth);
				}
				public void rowResized(int row, int newHeight) {
					System.out.println("Row "+row+" resized to "+newHeight);
				}
			}
		);

		Composite comp_bts;
		comp_bts = new Composite(composite, SWT.NONE);
		fd_comp_tb.right = new FormAttachment(comp_bts, 0, SWT.RIGHT);
		fd_comp_tb.top = new FormAttachment(comp_bts, 0, SWT.BOTTOM);
		final FormData fd_comp_bts = new FormData();
		fd_comp_bts.bottom = new FormAttachment(0, 30);
		fd_comp_bts.right = new FormAttachment(100, -5);
		fd_comp_bts.top = new FormAttachment(0, 5);
		fd_comp_bts.left = new FormAttachment(0, 5);
		comp_bts.setLayoutData(fd_comp_bts);
		comp_bts.setLayout(new FormLayout());

		final Button bt_left = new Button(comp_bts, SWT.NONE);
		final FormData fd_bt_left = new FormData();
		fd_bt_left.right = new FormAttachment(0, 50);
		fd_bt_left.left = new FormAttachment(0, 5);
		bt_left.setLayoutData(fd_bt_left);
		bt_left.setText("");

		Button bt_right;
		bt_right = new Button(comp_bts, SWT.NONE);
		fd_bt_left.top = new FormAttachment(bt_right, 0, SWT.TOP);
		final FormData fd_bt_right = new FormData();
		fd_bt_right.top = new FormAttachment(bt_left, 0, SWT.TOP);
		fd_bt_right.right = new FormAttachment(0, 120);
		fd_bt_right.left = new FormAttachment(0, 75);
		bt_right.setLayoutData(fd_bt_right);
		bt_right.setText("");

		final Button bt_up = new Button(comp_bts, SWT.NONE);
		final FormData fd_bt_up = new FormData();
		fd_bt_up.bottom = new FormAttachment(bt_right, 22, SWT.TOP);
		fd_bt_up.top = new FormAttachment(bt_right, 0, SWT.TOP);
		fd_bt_up.right = new FormAttachment(0, 190);
		fd_bt_up.left = new FormAttachment(0, 145);
		bt_up.setLayoutData(fd_bt_up);
		bt_up.setText("");

		Button bt_down;
		bt_down = new Button(comp_bts, SWT.NONE);
		final FormData fd_bt_down = new FormData();
		fd_bt_down.right = new FormAttachment(0, 260);
		fd_bt_down.top = new FormAttachment(bt_up, 0, SWT.TOP);
		fd_bt_down.left = new FormAttachment(0, 215);
		bt_down.setLayoutData(fd_bt_down);
		bt_down.setText("");
		
		/**
		 *  Set Excel-like table cursors
		 */
		
        if ( SWT.getPlatform().equals("win32") ) {
        	// Cross

    		Image crossCursor = SWTX.loadImageResource(table.getDisplay(), "/icons/cross_win32.gif");
        			
        			// Row Resize
        			
    		Image row_resizeCursor = SWTX.loadImageResource(table.getDisplay(), "/icons/row_resize_win32.gif");
        			
        			// Column Resize
        			
    		Image column_resizeCursor  = SWTX.loadImageResource(table.getDisplay(), "/icons/column_resize_win32.gif");
        	
			// we set the hotspot to the center, so calculate the number of pixels from hotspot to lower border:
			Rectangle crossBound        = crossCursor.getBounds();
			Rectangle rowresizeBound    = row_resizeCursor.getBounds();
			Rectangle columnresizeBound = column_resizeCursor.getBounds();
			
			Point crossSize        = new Point(crossBound.width/2, crossBound.height/2);
			Point rowresizeSize    = new Point(rowresizeBound.width/2, rowresizeBound.height/2);
			Point columnresizeSize = new Point(columnresizeBound.width/2, columnresizeBound.height/2);
			
    		table.setDefaultCursor(new Cursor(table.getDisplay(), crossCursor.getImageData(), crossSize.x, crossSize.y), crossSize);
    		table.setDefaultRowResizeCursor(new Cursor(table.getDisplay(), row_resizeCursor.getImageData(), rowresizeSize.x, rowresizeSize.y));
    		table.setDefaultColumnResizeCursor(new Cursor(table.getDisplay(), column_resizeCursor.getImageData(), columnresizeSize.x, columnresizeSize.y));
		} else {
		
			// Cross
		
			Image crossCursor      = SWTX.loadImageResource(table.getDisplay(), "/icons/cross.gif");
			Image crossCursor_mask = SWTX.loadImageResource(table.getDisplay(), "/icons/cross_mask.gif");
			
			// Row Resize
		
			Image row_resizeCursor      = SWTX.loadImageResource(table.getDisplay(), "/icons/row_resize.gif");
			Image row_resizeCursor_mask = SWTX.loadImageResource(table.getDisplay(), "/icons/row_resize_mask.gif");
		
			// Column Resize
		
			Image column_resizeCursor      = SWTX.loadImageResource(table.getDisplay(), "/icons/column_resize.gif");
			Image column_resizeCursor_mask = SWTX.loadImageResource(table.getDisplay(), "/icons/column_resize_mask.gif");
	
			// we set the hotspot to the center, so calculate the number of pixels from hotspot to lower border:
		
			Rectangle crossBound        = crossCursor.getBounds();
			Rectangle rowresizeBound    = row_resizeCursor.getBounds();
			Rectangle columnresizeBound = column_resizeCursor.getBounds();
		
			Point crossSize        = new Point(crossBound.width/2, crossBound.height/2);
			Point rowresizeSize    = new Point(rowresizeBound.width/2, rowresizeBound.height/2);
			Point columnresizeSize = new Point(columnresizeBound.width/2, columnresizeBound.height/2);
		
			table.setDefaultCursor(new Cursor(table.getDisplay(), crossCursor_mask.getImageData(), crossCursor.getImageData(), crossSize.x, crossSize.y), crossSize);
			table.setDefaultRowResizeCursor(new Cursor(table.getDisplay(), row_resizeCursor_mask.getImageData(), row_resizeCursor.getImageData(), rowresizeSize.x, rowresizeSize.y));
			table.setDefaultColumnResizeCursor(new Cursor(table.getDisplay(), column_resizeCursor_mask.getImageData(), column_resizeCursor.getImageData(), columnresizeSize.x, columnresizeSize.y));
		
		}
			
    }

}
