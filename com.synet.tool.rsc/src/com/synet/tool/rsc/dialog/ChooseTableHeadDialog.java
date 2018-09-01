package com.synet.tool.rsc.dialog;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.shrcn.found.ui.app.WrappedDialog;
import com.shrcn.found.ui.util.SwtUtil;

public class ChooseTableHeadDialog extends WrappedDialog {
	
	private int tableHeadRow;
	private Text txtTableHead;
	private Label labMsg;

	public ChooseTableHeadDialog(Shell parentShell) {
		super(parentShell);
	}
	
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite composite = SwtUtil.createComposite(parent, new GridData(GridData.FILL_BOTH), 2);
		
		SwtUtil.createLabel(composite, "  ", new GridData());
		SwtUtil.createLabel(composite, "  ", new GridData());
		SwtUtil.createLabel(composite, "表头行数：", new GridData());
		txtTableHead = SwtUtil.createText(composite, SwtUtil.bt_gd);
		
		GridData msgGridData = new GridData();
		msgGridData.horizontalSpan = 2;
		labMsg = SwtUtil.createLabel(composite, "", msgGridData);
		
		addListeners();
		return super.createDialogArea(parent);
	}
	
	private void addListeners() {
		txtTableHead.addModifyListener(new ModifyListener() {
			
			@Override
			public void modifyText(ModifyEvent e) {
				labMsg.setText("");
			}
		});
	}

	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("Excel导入->表头匹配");
	}
	
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, "下一步", true);
		createButton(parent, IDialogConstants.CANCEL_ID, "取消", true);
	}
	
	@Override
	protected Point getInitialSize() {
		return new Point(260, 160);
	}
	
	@Override
	protected void buttonPressed(int buttonId) {
		if (buttonId == IDialogConstants.OK_ID) {
			String text = txtTableHead.getText().trim();
			if (text == null || "".equals(text)) {
				labMsg.setText("请输入表头所在的行数！");
				return;
			}
			try {
				tableHeadRow = Integer.valueOf(text);
			} catch (Exception e) {
				labMsg.setText("请输入正确的行数！");
				return;
			}
		}
		super.buttonPressed(buttonId);
	}

	public int getTableHeadRow() {
		return tableHeadRow;
	}
}
