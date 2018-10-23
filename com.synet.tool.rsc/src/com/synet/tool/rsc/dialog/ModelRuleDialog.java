package com.synet.tool.rsc.dialog;

import java.util.List;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

import com.shrcn.found.ui.app.WrappedDialog;
import com.shrcn.found.ui.util.DialogHelper;
import com.shrcn.found.ui.util.SwtUtil;
import com.synet.tool.rsc.ui.TableFactory;
import com.synet.tool.rsc.ui.table.RuleTable;
import com.synet.tool.rsc.util.Rule;
import com.synet.tool.rsc.util.RuleManager;

public class ModelRuleDialog extends WrappedDialog{

	private Button btnAdd;
	private Button btnDel;
	private RuleTable table;
	private List<Rule> ruleList;
	private RuleManager ruleManager;

	public ModelRuleDialog(Shell parentShell) {
		super(parentShell);
	}
	
	@Override
	protected Control createDialogArea(Composite parent) {
		GridData griddata = new GridData(GridData.FILL_BOTH);
		Composite composite = SwtUtil.createComposite(parent, griddata, 2);
		btnAdd = SwtUtil.createButton(composite, SwtUtil.bt_gd, SWT.BUTTON1, "添加");
		btnDel = SwtUtil.createButton(composite, SwtUtil.bt_gd, SWT.BUTTON1, "删除");
		
		GridData griddata_2 = new GridData(GridData.FILL_BOTH);
		griddata_2.horizontalSpan = 2;
		table = TableFactory.getModelRuleTable(composite);
		table.getTable().setLayoutData(griddata_2);
		initData();
		addListeners();
		return composite;
	}
	
	private void initData() {
		ruleManager = RuleManager.getInstance();
		ruleList = ruleManager.getRules();
		table.setInput(ruleList);
	}

	private void addListeners() {
		SelectionListener listener = new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Button btn = (Button) e.getSource();
				if(btn == btnAdd) {
					table.insertRow(table.getDefaultRow());
				} else if(btn == btnDel) {
					table.removeSelected();
					DialogHelper.showAsynInformation("删除成功");
				}
			}
		};
		btnAdd.addSelectionListener(listener);
		btnDel.addSelectionListener(listener);
	}
	
	
	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("模型辨识规则");
	}
	
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, "确定", false);
		createButton(parent, IDialogConstants.CANCEL_ID, "取消", false);
	}
	
	@Override
	protected Point getInitialSize() {
		return new Point(1000, 650);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	protected void buttonPressed(int buttonId) {
		if (buttonId == IDialogConstants.OK_ID) {
			boolean confirm = DialogHelper.showConfirm("确定要保存改动？（若无改动请点击\"取消\"按钮）");
			if(confirm) {//修改rule.xml文件
				ruleManager.modify((List<Rule>) table.getInput());
			}
		}
		super.buttonPressed(buttonId);
	}
}
