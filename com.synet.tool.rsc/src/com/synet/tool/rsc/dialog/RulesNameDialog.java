package com.synet.tool.rsc.dialog;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.shrcn.found.ui.app.WrappedDialog;
import com.shrcn.found.ui.util.DialogHelper;
import com.shrcn.found.ui.util.SwtUtil;

public class RulesNameDialog extends WrappedDialog{

	private String ruleName;
	private Text ruleNameText;

	public RulesNameDialog(Shell parentShell) {
		super(parentShell);
	}
	
	@Override
	protected Control createDialogArea(Composite parent) {
		GridData griddata = new GridData(GridData.FILL_BOTH);
		Composite composite = SwtUtil.createComposite(parent, griddata, 3);
		ruleNameText = SwtUtil.createLabelText(composite, "名称：", SwtUtil.bt_hd);
		SwtUtil.createLabel(composite, "(需要包含\"rule\",以\".xml\"结尾)", new GridData(200, SWT.DEFAULT));
		return composite;
	}
	

	
	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("输入规则文件名称");
	}
	
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, "确定", false);
		createButton(parent, IDialogConstants.CANCEL_ID, "取消", false);
	}
	
	@Override
	protected Point getInitialSize() {
		return new Point(380, 120);
	}
	
	@Override
	protected void buttonPressed(int buttonId) {
		if (buttonId == IDialogConstants.OK_ID) {
			ruleName = ruleNameText.getText().trim();
			if(!ruleName.contains("rule") || !ruleName.endsWith(".xml")) {
				DialogHelper.showAsynInformation("文件名不规范，请重新输入");
				return;
			}
		}
		super.buttonPressed(buttonId);
	}

	public String getRuleName() {
		return ruleName;
	}

}
