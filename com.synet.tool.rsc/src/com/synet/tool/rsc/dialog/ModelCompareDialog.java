package com.synet.tool.rsc.dialog;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.shrcn.found.ui.app.WrappedDialog;
import com.shrcn.found.ui.util.SwtUtil;

public class ModelCompareDialog extends WrappedDialog{

	private Combo modelCombo;
	private Text textLeft;
	private Text textRight;

	public ModelCompareDialog(Shell parentShell) {
		super(parentShell);
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		GridData gridData = new GridData(GridData.FILL_BOTH);
		Composite composite = SwtUtil.createComposite(parent, gridData, 1);
		composite.setLayout(SwtUtil.getGridLayout(2));
		//左侧
		Composite comLeft = SwtUtil.createComposite(composite, gridData, 1);
		comLeft.setLayout(SwtUtil.getGridLayout(2));
		SwtUtil.createLabel(comLeft, "模版装置配置信息：", new GridData(110, SWT.DEFAULT));
		modelCombo = SwtUtil.createCombo(comLeft, new GridData(110, 25), true);
		GridData gd_2 = new GridData(GridData.FILL_BOTH);
		gd_2.horizontalSpan = 2;
		textLeft = SwtUtil.createText(comLeft, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL, gd_2);
		//右侧
		Composite comRight = SwtUtil.createComposite(composite, gridData, 1);
		comRight.setLayout(SwtUtil.getGridLayout(1));
		SwtUtil.createLabel(comRight, "当前装置配置信息:", new GridData(110, 22));
		textRight = SwtUtil.createText(comRight, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL, gridData);
		initData();
		return composite;
	}
	
	private void initData() {
		initCombo();
		initText();
	}


	private void initText() {
		// TODO Auto-generated method stub
		
	}


	private void initCombo() {
		// TODO Auto-generated method stub
		
	}


	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("模版对比");
	}
	
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, "OK", true);
		createButton(parent, IDialogConstants.CANCEL_ID, "取消", true);
	}
	
	@Override
	protected Point getInitialSize() {
		return new Point(1200, 650);
	}
	
//	@Override
//	protected void buttonPressed(int buttonId) {
//		if(buttonId == IDialogConstants.OK_ID){
//			
//		}
//		super.buttonPressed(buttonId);
//	}
	
}
