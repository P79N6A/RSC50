package com.shrcn.found.ui.dialog;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.shrcn.found.ui.UIConstants;
import com.shrcn.found.ui.app.WrappedTitleAreaDialog;

public class CompareResultDialog extends WrappedTitleAreaDialog {
	private String scrCode; //源文件路径
	private String desCode; //目标文件路径
	private String compareForm;
	private boolean match;
	private String msg;
	

	/**
	 * Create the dialog
	 * @param parentShell
	 */
	public CompareResultDialog(Shell parentShell, String scrCode,
			String desCode, String compareForm, boolean match) {
		super(parentShell);
		this.scrCode = scrCode;
		this.desCode = desCode;
		this.compareForm = compareForm;
		this.match = match;
		this.msg = match ? "匹配。" : "不匹配！";
	}

	/**
	 * Create contents of the dialog
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		//
		setTitle("文件比较结果");
		setMessage("源文件和目标文件" + compareForm + "比较结果");
		Composite area = (Composite) super.createDialogArea(parent);
		((GridLayout)area.getLayout()).marginWidth = 10;
		
		Composite cmpMsg = new Composite(area, SWT.NONE);
		cmpMsg.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		GridLayout gridLayout = new GridLayout(1, false);
		gridLayout.marginTop = 20;
		gridLayout.marginBottom = 10;
		cmpMsg.setLayout(gridLayout);
		
		Label lbResult = new Label(cmpMsg, SWT.NONE);
		GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		lbResult.setLayoutData(gridData);
		lbResult.setText("源文件和目标文件" + compareForm + msg);
		if (!match)
			lbResult.setForeground(UIConstants.RED);
		
		Composite cmpRes = new Composite(area, SWT.NONE);
		cmpRes.setLayoutData(new GridData(GridData.FILL_BOTH));
		GridLayout gridLayout1 = new GridLayout(2, false);
		gridLayout1.verticalSpacing = 15;
		cmpRes.setLayout(gridLayout1);
		
		Text txScrCode = createLabelText(cmpRes, "源文件" + compareForm + "：");
		txScrCode.setText(scrCode);
		
		Text txDesCode = createLabelText(cmpRes, "目标文件" + compareForm + "：");
		txDesCode.setText(desCode);
		return area;
	}
	
	private Text createLabelText(Composite parent, String lbTxt) {
    	final Label lbFCDA = new Label(parent, SWT.NONE);
		lbFCDA.setText(lbTxt);
		
    	Text txt = new Text(parent, SWT.BORDER | SWT.READ_ONLY);
		txt.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		return txt;
    }

	/**
	 * Create contents of the button bar
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, "关闭", true);
	}

	/**
	 * Return the initial size of the dialog
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(500, 300);
	}
	
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("文件比较结果");
		
	}
}
