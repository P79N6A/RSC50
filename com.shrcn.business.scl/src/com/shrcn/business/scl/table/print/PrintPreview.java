/**
 * Copyright (c) 2007-2009 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based on IEC61850 SCT.
 */
package com.shrcn.business.scl.table.print;

import java.io.File;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.printing.PrintDialog;
import org.eclipse.swt.printing.Printer;
import org.eclipse.swt.printing.PrinterData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.ToolItem;

/**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2009-11-18
 */
/**
 * $Log: PrintPreview.java,v $
 * Revision 1.2  2012/04/10 06:30:03  cchun
 * Update:修改文件路径分隔符，以兼容linux平台
 *
 * Revision 1.1  2010/03/02 07:49:12  cchun
 * Add:添加重构代码
 *
 * Revision 1.4  2010/01/22 01:39:38  cchun
 * Update:重构关联检查国际化
 *
 * Revision 1.3  2010/01/21 08:48:15  gj
 * Update:完成UI插件的国际化字符串资源提取
 *
 * Revision 1.2  2009/12/07 03:04:57  cchun
 * Update:添加信号关联全屏查看功能
 *
 * Revision 1.1  2009/11/19 08:28:51  cchun
 * Update:完成信号关联打印功能
 *
 */
public class PrintPreview extends KDialog {

	public static final String ONEPAGE = Messages.getString("ONEPAGE");
	
	private static final int DEFAULT_ZO0M = 4;
	
	protected PDocument document;

	protected Label guiImageLabel;

	protected CLabel guiPageLabel;

	protected Combo guiZoom;

	protected ScrolledComposite guiScrollArea;

	protected boolean layoutNeccessary;

	protected int percent;

	protected int page;

	/**
	 * @param parent
	 * @param title
	 * @param icon
	 */
	public PrintPreview(Shell parent, String title, Image icon, PDocument doc) {
		super(parent, title, icon);
		createContents();
		document = doc;
		page = 1;
		percent = 100;
		layoutNeccessary = true;

		addToolItem("print", Messages.getString("PRINT"), IconSource.PRINT); //$NON-NLS-1$
		addToolItem("first", Messages.getString("FIRST_PAGE"), IconSource.FIRST); //$NON-NLS-1$
		addToolItem("prev", Messages.getString("PREVIOUS_PAGE"), IconSource.PREVIOUS); //$NON-NLS-1$
		addToolItem("next", Messages.getString("NEXT_PAGE"), IconSource.NEXT); //$NON-NLS-1$
		addToolItem("last", Messages.getString("LAST_PAGE"), IconSource.LAST); //$NON-NLS-1$
		
		Button close = addButtonRight(Messages.getString("CLOSE"), ""); //$NON-NLS-1$
		close.setFocus();
		close.addSelectionListener(new SelectionListener(){
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}
			@Override
			public void widgetSelected(SelectionEvent e) {
				onClose();
			}});
		
		guiShell.addShellListener(new ShellAdapter() {
			public void shellClosed(ShellEvent arg0) {
				onClose();
			}
		});

		Composite comp = new Composite(guiToolBarArea, SWT.BORDER);
		comp.setLayout(new FillLayout());
		guiPageLabel = new CLabel(comp, SWT.NONE);
		guiPageLabel.setText(guiPageLabel.getText() + "        "); //$NON-NLS-1$
		guiPageLabel.setBackground(Display.getCurrent().getSystemColor(
				SWT.COLOR_WHITE));
		adjustToToolBar(comp);

		guiZoom = new Combo(guiToolBarArea, SWT.BORDER | SWT.READ_ONLY);
		guiZoom.add("500%"); //$NON-NLS-1$
		guiZoom.add("300%"); //$NON-NLS-1$
		guiZoom.add("200%"); //$NON-NLS-1$
		guiZoom.add("175%"); //$NON-NLS-1$
		guiZoom.add("150%"); //$NON-NLS-1$
		guiZoom.add("125%"); //$NON-NLS-1$
		guiZoom.add("100%"); //$NON-NLS-1$
		guiZoom.add("80%"); //$NON-NLS-1$
		guiZoom.add("50%"); //$NON-NLS-1$
		guiZoom.add("20%"); //$NON-NLS-1$
		guiZoom.add(ONEPAGE);
//		guiZoom.add("一页");
		adjustToToolBar(guiZoom);
//		guiZoom.setToolTipText("VorschaugroBe");
		guiZoom.setToolTipText(Messages.getString("ZOOM"));
		guiZoom.select(DEFAULT_ZO0M);
		guiZoom.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent arg0) {
				onCombo(((Combo) arg0.widget).getText());
			}
		});
		
//		Button pageSetup = new Button(guiToolBarArea, SWT.NONE);
//		pageSetup.setText("打印设置");
//		adjustToToolBar(pageSetup);
//		pageSetup.addSelectionListener(new SelectionAdapter() {
//			public void widgetSelected(SelectionEvent arg0) {
//				onPageSetup();
//			}
//		});
		
		guiMainArea.setLayout(new FillLayout());
		guiScrollArea = new ScrolledComposite(guiMainArea, SWT.H_SCROLL
				| SWT.V_SCROLL);
		guiImageLabel = new Label(guiScrollArea, SWT.NONE);
		guiScrollArea.setContent(guiImageLabel);
		if (guiImageLabel.getImage() != null)
			guiImageLabel.getImage().dispose();
		guiImageLabel.setImage(getPageImage(page));
		guiPageLabel.setText(" " + Messages.getString("PAGE") + " " + page + " / " //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				+ document.getNumOfPages() + "       "); //$NON-NLS-1$
		guiImageLabel.setSize(guiImageLabel.computeSize(SWT.DEFAULT,
				SWT.DEFAULT));
		
		//初始化缩放
		onCombo(guiZoom.getText());
		//初始化快捷键
		registShortCut();
		//初始化焦点
		guiShell.forceFocus();
	}
	
	/**
	 * 注册快捷键
	 */
	private void registShortCut() {
		guiShell.addKeyListener(new KeyListener(){

			@Override
			public void keyPressed(KeyEvent e) {
			}

			@Override
			public void keyReleased(KeyEvent e) {
				switch(e.keyCode) {
					case SWT.PAGE_DOWN:
					case SWT.ARROW_DOWN:
					case SWT.ARROW_RIGHT:
						turnToNext();
						break;
					case SWT.PAGE_UP:
					case SWT.ARROW_UP:
					case SWT.ARROW_LEFT:
						turnToPrevious();
						break;
				}
			}});
	}
	
	/**
	 * 翻到下一页
	 */
	private void turnToNext() {
		if (page < document.getNumOfPages()) {
			page++;
			if (guiImageLabel.getImage() != null)
				guiImageLabel.getImage().dispose();
			guiImageLabel.setImage(getPageImage(page));
			guiPageLabel.setText(" " + Messages.getString("PAGE") + " " + page + " / " //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
					+ document.getNumOfPages());
		}
	}
	
	/**
	 * 翻到上一页
	 */
	private void turnToPrevious() {
		if (page > 1) {
			page--;
			if (guiImageLabel.getImage() != null)
				guiImageLabel.getImage().dispose();
			guiImageLabel.setImage(getPageImage(page));
			guiPageLabel.setText(" " + Messages.getString("PAGE") + " " + page + " / " //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
					+ document.getNumOfPages());
		}
	}

	public int getShellStyle() {
		return super.getShellStyle() | SWT.MAX | SWT.MIN;
	}

	protected void doLayout() {
		int x = Display.getCurrent().getBounds().width - 100;
		int y = Display.getCurrent().getBounds().height - 10;
		guiShell.setSize(x, y);
		guiShell.setMaximized(true);
	}

	public Image getPageImage(int page) {
		Point dpi = Display.getCurrent().getDPI();

		try {
			int h = (int) Math.round(document.getPageHeight() / 2.54 * dpi.y
					* percent / 100 + 5);
			int w = (int) Math.round(document.getPageWidth() / 2.54 * dpi.x
					* percent / 100 + 5);
			Image newImage = new Image(Display.getCurrent(), w, h);
			GC gc = new GC(newImage);

			PBox.setParameters(gc, Display.getCurrent(), dpi, percent);
			if (layoutNeccessary)
				document.layout();
			layoutNeccessary = false;
			document.draw(page);

			// 页边阴影效果
			gc.setBackground(Display.getCurrent().getSystemColor(
					SWT.COLOR_DARK_GRAY));
			gc.fillRectangle(0, newImage.getBounds().height - 5, newImage
					.getBounds().width, newImage.getBounds().height);
			gc.fillRectangle(newImage.getBounds().width - 5, 0, newImage
					.getBounds().width - 5, newImage.getBounds().height);

			gc.setBackground(Display.getCurrent().getSystemColor(
					SWT.COLOR_WIDGET_BACKGROUND));
			gc.fillRectangle(0, newImage.getBounds().height - 5, 5, newImage
					.getBounds().height);
			gc.fillRectangle(newImage.getBounds().width - 5, 0, newImage
					.getBounds().width, 5);

			gc.dispose();
			return newImage;
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return null;
		}

	}

	protected void onCombo(String text) {
		if (text.startsWith(ONEPAGE)) {
			long ypixel = Math.round(document.getPageHeight() / 2.54
					* Display.getCurrent().getDPI().y);
			long xpixel = Math.round(document.getPageWidth() / 2.54
					* Display.getCurrent().getDPI().x);

			int yscale = (int) (100 * (guiScrollArea.getBounds().height) / ypixel);
			int xscale = (int) (100 * (guiScrollArea.getBounds().width) / xpixel);

			percent = Math.min(yscale, xscale);
		} else {
			text = text.substring(0, text.length() - 1);
			percent = 100;
			try {
				percent = Integer.parseInt(text);
			} catch (Exception e1) {
				MsgBox.show("'" + text + "' ist keine gultige Zahl."); //$NON-NLS-1$ //$NON-NLS-2$
				guiZoom.select(DEFAULT_ZO0M);
			}
		}
		layoutNeccessary = true;
		if (guiImageLabel.getImage() != null)
			guiImageLabel.getImage().dispose();
		guiImageLabel.setImage(getPageImage(page));
		guiImageLabel.setSize(guiImageLabel.computeSize(SWT.DEFAULT,
				SWT.DEFAULT));

	}

	protected void onToolItem(ToolItem toolitem, String name) {
		if (name.equals("next")) { //$NON-NLS-1$
			turnToNext();
		} else if (name.equals("prev")) { //$NON-NLS-1$
			turnToPrevious();
		} else if (name.equals("first")) { //$NON-NLS-1$
			page = 1;
			if (guiImageLabel.getImage() != null)
				guiImageLabel.getImage().dispose();
			guiImageLabel.setImage(getPageImage(page));
			guiPageLabel.setText(" " + Messages.getString("PAGE") + " " + page + " / " //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
					+ document.getNumOfPages());
		} else if (name.equals("last")) { //$NON-NLS-1$
			page = document.getNumOfPages();
			if (guiImageLabel.getImage() != null)
				guiImageLabel.getImage().dispose();
			guiImageLabel.setImage(getPageImage(page));
			guiPageLabel.setText(" " + Messages.getString("PAGE") + " " + page + " / " //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
					+ document.getNumOfPages());
		} else if (name.equals("print")) { //$NON-NLS-1$
			onPrint();
		}
	}

//	protected void onButton(Button button, String buttonText) {
//		if (buttonText.startsWith("&Schlie"))
//			onClose();
//		else if (buttonText.startsWith("Seite"))
//			onPageSetup();
//	}

	protected void onClose() {
		if (guiImageLabel.getImage() != null)
			guiImageLabel.getImage().dispose();
		PTextStyle.disposeFonts();
		close();
	}

	protected void onPageSetup() {
		/*
		 * funktioniert nicht: - Abgeschnittene Tabellen bleiben abgeschnitten -
		 * Skalierung geht nicht
		 */
		PageSetup ps = new PageSetup(guiShell);
		ps.open();
		document.readMeasuresFromPageSetup();
		layoutNeccessary = true;
		if (guiImageLabel.getImage() != null)
			guiImageLabel.getImage().dispose();
		guiImageLabel.setImage(getPageImage(page));
		guiImageLabel.setSize(guiImageLabel.computeSize(SWT.DEFAULT,
				SWT.DEFAULT));

	}

	protected void onPrint() {
		PrintDialog dialog = new PrintDialog(guiShell, SWT.BORDER);
		PrinterData data = dialog.open();
		if (data == null)
			return;
		if (data.printToFile) {
			FileDialog d = new FileDialog(guiShell, SWT.SAVE);
			d.setFilterNames(new String[] { "All Files (*.*)" }); //$NON-NLS-1$
			d.setFilterExtensions(new String[] { "*.*" }); // Windows wild //$NON-NLS-1$
			// cards
			d.setFileName(""); //$NON-NLS-1$
			d.open();
			data.fileName = d.getFilterPath() + File.separator + d.getFileName(); //$NON-NLS-1$

		}
		
		Printer printer = new Printer(data);
		document.layout();
		
		if (printer.startJob(document.getTitle())) {
			GC gc = new GC(printer);
			PBox.setParameters(gc, printer, printer.getDPI(), (int) (document
					.getScale() * 100));
			if (data.scope == PrinterData.ALL_PAGES) {
				data.startPage = 1;
				data.endPage = document.getNumOfPages();
			} else if (data.scope == PrinterData.SELECTION) {
				data.startPage = page;
				data.endPage = page;
			} else if (data.scope == PrinterData.PAGE_RANGE) {
				if (data.startPage > document.getNumOfPages())
					data.startPage = document.getNumOfPages();
				if (data.endPage > document.getNumOfPages())
					data.endPage = document.getNumOfPages();
			}
			for (int page = data.startPage; page <= data.endPage; page++) {
				printer.startPage();
				document.draw(page);
				printer.endPage();
			}
			printer.endJob();
			gc.dispose();
		}
		printer.dispose();
		layoutNeccessary = true;

	}

}