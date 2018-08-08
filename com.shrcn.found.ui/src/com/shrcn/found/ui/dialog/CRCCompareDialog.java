/**
 * Copyright (c) 2007-2010 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based on IEC61850 SCT.
 */
package com.shrcn.found.ui.dialog;

import java.io.File;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.shrcn.found.common.Constants;
import com.shrcn.found.ui.app.WrappedTitleAreaDialog;
import com.shrcn.found.ui.util.DialogHelper;
import com.shrcn.found.ui.util.SwtUtil;

/**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2010-9-8
 */
/**
 * $Log: CRCCompareDialog.java,v $
 * Revision 1.7  2013/03/08 10:31:15  cchun
 * Update:增加比较类型常量
 *
 * Revision 1.6  2012/09/03 07:45:09  cchun
 * Update:增加ICD差异比较
 *
 * Revision 1.5  2011/09/20 08:43:34  cchun
 * Update:为文件比较增加字符集选项
 *
 * Revision 1.4  2011/09/19 09:24:50  cchun
 * Fix Bug:修改对话框校验提示bug
 *
 * Revision 1.3  2011/09/19 08:30:30  cchun
 * Update:添加MD5和文件内容比较
 *
 * Revision 1.2  2010/09/09 08:18:29  cchun
 * Update:增加打开比较功能
 *
 * Revision 1.1  2010/09/09 02:18:04  cchun
 * Update:添加CRC码比较功能
 *
 */
public class CRCCompareDialog extends WrappedTitleAreaDialog {

	public static final int cmp_crc = 0;
	public static final int cmp_md5 = 1;
	public static final int cmp_content = 2;
	public static final int cmp_txt = 0;
	public static final int cmp_scl = 1;
	private static String prompt = "文件比较";
	private Text txtDest;
	private Text txtSrc;
	private String[] files = null;
	private String srcPath;
	private String charset;
	private int compareType; // 文件比较方法
	private Button btCRC; // CRC选择按钮
	private Button btMD5; // MD5选择按钮
	private Button btContent; // 内容选择按钮
	private Combo cmbChar;
	//内容比较的比较方式，纯文本比较，或控制块等内容的比较
	private Combo cmbType;
	private int type;
	
	/**
	 * Create the dialog
	 * 
	 * @param parentShell
	 */
	public CRCCompareDialog(Shell parentShell, String srcPath) {
		super(parentShell);
		this.srcPath = srcPath;
	}

	/**
	 * Create contents of the dialog
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		setTitle(prompt);
		setMessage("比较前，请选择正确的源文件和目标文件。");
		Composite area = (Composite) super.createDialogArea(parent);
		Composite cmpType = createPane(area);
		GridLayout gridLayout = new GridLayout(3, false);
		gridLayout.marginTop = 20;
		gridLayout.marginBottom = 10;
		gridLayout.marginWidth = 80;
		cmpType.setLayout(gridLayout);
	    btCRC = SwtUtil.createRadioButton(cmpType, "CRC码比较", new GridData(SWT.LEFT, SWT.CENTER, true, false));
		btCRC.setSelection(true);
	    btMD5 = SwtUtil.createRadioButton(cmpType, "MD5码比较", new GridData(SWT.LEFT, SWT.CENTER, true, false));
	    btContent = SwtUtil.createRadioButton(cmpType, "内容比较", new GridData(SWT.LEFT, SWT.CENTER, true, false));
		
		Composite cmpFiles = createPane(area);
		txtSrc = SwtUtil.createFileSelector(cmpFiles, "源文件：", Constants.SCL_FILES);
		txtSrc.setData("源文件");
		txtDest = SwtUtil.createFileSelector(cmpFiles, "目标文件：", Constants.SCL_FILES);
		txtDest.setData("目标文件");
		((GridLayout)cmpFiles.getLayout()).verticalSpacing = 10;

		final Composite cmp = createPane(area);
		final GridLayout layout = new GridLayout(2, false);
		layout.horizontalSpacing = 30;
		layout.marginLeft = -5;
		cmp.setLayout(layout);
		
		final Composite compType = new Composite(cmp, SWT.NONE);
		final GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		final GridLayout layoutChar = new GridLayout(2, false);
		compType.setLayoutData(gridData);
		compType.setLayout(layoutChar);
		cmbType = SwtUtil.createLabelCombo(compType, "比较方式：", new String[] {"文本", "ICD版本差异"}, 60, 110);
		cmbType.select(cmp_scl);
		
		final Composite cmpChar = new Composite(cmp, SWT.NONE);
		cmpChar.setLayoutData(gridData);
		layoutChar.horizontalSpacing = 17;
		cmpChar.setLayout(layoutChar);
		cmbChar = SwtUtil.createLabelCombo(cmpChar, "字符集：", new String[] {"UTF-8", "GBK", "US-ASCII"}, 50, 110);
		cmbChar.select(0);
		
		SwtUtil.setVisible(cmp, false);
		
		btContent.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				SwtUtil.setVisible(cmp, btContent.getSelection());
			}
		});
		cmbType.addSelectionListener(new SelectionAdapter(){
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(cmbType.getSelectionIndex() == 1){
					cmbChar.setEnabled(false);
					cmbChar.setText("");
				} else if(cmbType.getSelectionIndex() == 0){
					cmbChar.setEnabled(true);
					cmbChar.select(0);
				}
			}
		});
		
		if (this.srcPath != null)
			txtSrc.setText(srcPath);
		return area;
	}
	
	private Composite createPane(Composite parent) {
		Composite cmp = new Composite(parent, SWT.NONE);
		cmp.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		return cmp;
	}

	/**
	 * Create contents of the button bar
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, "比较", true);
		createButton(parent, IDialogConstants.CANCEL_ID, "取消", false);
	}

	/**
	 * Return the initial size of the dialog
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(500, 350);
	}
	
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText(prompt);
	}
	
	@Override
	protected void buttonPressed(int buttonId) {
		if (buttonId == OK) {
			if (!checkInputs())
				return;
			if (cmbType.isVisible() && cmbType.getSelectionIndex() == 1 && ! checkIsICD()) {
				DialogHelper.showError("请选择要比较的ICD文件!");
				return;
			}
			this.files = new String[] {txtSrc.getText().trim(), txtDest.getText().trim()};
			if (btCRC.getSelection()) {
				setCompareType(cmp_crc);
			} else if (btMD5.getSelection()) {
				setCompareType(cmp_md5);
			} else if (btContent.getSelection()) {
				setCompareType(cmp_content);
				this.charset = cmbChar.getText();
				this.type = cmbType.getSelectionIndex();
			}
		}
		super.buttonPressed(buttonId);
	}
	
	/**
	 * 检查源文件和目标文件是否都为ICD文件
	 * @return
	 */
	private boolean checkIsICD(){
		boolean is = true;
		if(!txtSrc.getText().toLowerCase().endsWith("icd")){
			txtSrc.setText("");
			is = false;
		}
		if(!txtDest.getText().toLowerCase().endsWith("icd")){
			txtDest.setText("");
			is = false;
		}
		return is;
	}
	
	/**
	 * 得到待比较文件
	 * @return
	 */
	public String[] getCompareFile() {
		return files;
	}
	
	/**
	 * 检查用户输入
	 * @return
	 */
	private boolean checkInputs() {
		Text[] texts = new Text[]{txtSrc, txtDest};
		for (Text txt : texts) {
			String msg = checkText(txt);
			if (msg != null) {
				DialogHelper.showWarning(msg);
				return false;
			}
		}
		return true;
	}
	
	/**
	 * 校验路径信息
	 * @param txt
	 * @return
	 */
	private String checkText(Text txt) {
		String path = txt.getText();
		Object title = txt.getData();
		if (path.trim().equals("")) {
			return title + "路径不能为空！";
		}
		File f = new File(path);
		if (!f.exists()) {
			return title + "不存在！";
		}
		return null;
	}

	public void setSrcPath(String srcPath) {
		this.srcPath = srcPath;
	}

	public int getCompareType() {
		return compareType;
	}

	public void setCompareType(int compareForm) {
		this.compareType = compareForm;
	}

	public String getCharset() {
		return charset;
	}

	public int getType() {
		return type;
	}
	
 }
