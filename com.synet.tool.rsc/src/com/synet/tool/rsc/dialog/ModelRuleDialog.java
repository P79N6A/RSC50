package com.synet.tool.rsc.dialog;

import java.util.ArrayList;
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
	private RuleManager ruleManager;
	private org.eclipse.swt.widgets.List rules;
	private Button btnAddRule;
	private Button btnDelRule;
	private Button btnReNameRule;

	public ModelRuleDialog(Shell parentShell) {
		super(parentShell);
	}
	
	@Override
	protected Control createDialogArea(Composite parent) {
		GridData griddata = new GridData(GridData.FILL_BOTH);
		Composite composite = SwtUtil.createComposite(parent, griddata, 2);
		GridData gdLeft = new GridData(260, GridData.FILL_HORIZONTAL);
		Composite compLeft = SwtUtil.createComposite(composite, gdLeft, 3);
		btnAddRule = SwtUtil.createButton(compLeft, SwtUtil.bt_gd, SWT.BUTTON1, "添加");
		btnDelRule = SwtUtil.createButton(compLeft, SwtUtil.bt_gd, SWT.BUTTON1, "删除");
		btnReNameRule = SwtUtil.createButton(compLeft, SwtUtil.bt_gd, SWT.BUTTON1, "重命名");
		GridData gdLeft_3 = new GridData(260, GridData.FILL_HORIZONTAL);
		gdLeft_3.horizontalSpan = 3;
		rules = SwtUtil.createList(compLeft, gdLeft_3);
		Composite compRight = SwtUtil.createComposite(composite, griddata, 2);
		btnAdd = SwtUtil.createButton(compRight, SwtUtil.bt_gd, SWT.BUTTON1, "添加");
		btnDel = SwtUtil.createButton(compRight, SwtUtil.bt_gd, SWT.BUTTON1, "删除");
		GridData griddata_2 = new GridData(GridData.FILL_BOTH);
		griddata_2.horizontalSpan = 2;
		table = TableFactory.getModelRuleTable(compRight);
		table.getTable().setLayoutData(griddata_2);
		initData();
		addListeners();
		return composite;
	}
	
	private void initData() {
		ruleManager = RuleManager.getInstance();
		rules.setItems(ruleManager.getRuleList());
	}

	private void addListeners() {
		SelectionListener listener = new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Object obj =  e.getSource();
				if(obj == btnAdd) {
					table.insertRow(table.getDefaultRow());
				} else if(obj == btnDel) {
					table.removeSelected();
					DialogHelper.showAsynInformation("删除成功");
				} else if(obj == btnAddRule) {
					RulesNameDialog rulesNameDialog = new RulesNameDialog(getShell());
					if(rulesNameDialog.open() == IDialogConstants.OK_ID) {
						String ruleName = rulesNameDialog.getRuleName();
						boolean copyRuleFile = ruleManager.copyRuleFile(ruleName);
						if(copyRuleFile) {
							table.setInput(ruleManager.getRules());
							rules.setItems(ruleManager.getRuleList());
						} else {
							DialogHelper.showAsynInformation("添加失败，文件复制出错");
						}
					}
				} else if(obj == btnDelRule) {
					int selIdx = rules.getSelectionIndex();
					if(selIdx == -1) {
						return;
					}
					String selName = rules.getItem(selIdx);
					boolean deleteRule = ruleManager.deleteRule(selName);
					if(deleteRule) {
						table.setInput(new ArrayList<>());
						rules.setItems(ruleManager.getRuleList());
					}
					
				} else if(obj == btnReNameRule) {
					int selIdx = rules.getSelectionIndex();
					if(selIdx == -1) {
						return;
					}
					String selName = rules.getItem(selIdx);
					RulesNameDialog rulesNameDialog = new RulesNameDialog(getShell());
					if(rulesNameDialog.open() == IDialogConstants.OK_ID) {
						String ruleName = rulesNameDialog.getRuleName();
						boolean reNameRuleFile = ruleManager.reNameRuleFile(selName, ruleName);
						if(reNameRuleFile) {
							rules.setItems(ruleManager.getRuleList());
						} else {
							DialogHelper.showAsynInformation("重命名失败");
						}
					}
				} else if(obj == rules) {
					int selIdx = rules.getSelectionIndex();
					if(selIdx == -1) {
						return;
					}
					String selName = rules.getItem(rules.getSelectionIndex());
					List<Rule> rulesByFileName = ruleManager.getRulesByFileName(selName);
					table.setInput(rulesByFileName);
				}
			}
		};
		rules.addSelectionListener(listener);
		btnAddRule.addSelectionListener(listener);
		btnDelRule.addSelectionListener(listener);
		btnReNameRule.addSelectionListener(listener);
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
		return new Point(1250, 650);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	protected void buttonPressed(int buttonId) {
		if (buttonId == IDialogConstants.OK_ID) {
			boolean confirm = DialogHelper.showConfirm("确定要保存改动？（若无改动请点击\"取消\"按钮）");
			if(confirm) {//修改rule.xml文件
				String selName = rules.getItem(rules.getSelectionIndex());
				ruleManager.modify((List<Rule>) table.getInput(), selName);
			}
		}
		super.buttonPressed(buttonId);
	}
}
