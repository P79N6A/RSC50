package com.synet.tool.rsc.dialog;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

import com.shrcn.found.ui.app.WrappedDialog;
import com.shrcn.found.ui.util.SwtUtil;
import com.synet.tool.rsc.ui.TableFactory;
import com.synet.tool.rsc.ui.table.RuleTable;
import com.synet.tool.rsc.util.Rule;
import com.synet.tool.rsc.util.RuleManager;

public class SelectRuleDialog extends WrappedDialog {

	private RuleTable table;
	private List<Rule> rulesSelect;
	private List<Rule> rules;
	
	public SelectRuleDialog(Shell parentShell) {
		super(parentShell);
	}
	
	@Override
	protected Control createDialogArea(Composite parent) {
		GridData gridData = new GridData(GridData.FILL_BOTH);
		Composite composite = SwtUtil.createComposite(parent, gridData, 1);
		composite.setLayout(SwtUtil.getGridLayout(1));
		table = TableFactory.getRuleSelectTable(composite);
		table.getTable().setLayoutData(gridData);
		initData();
		return composite;
	}

	private void initData() {
		rules = RuleManager.getInstance().getRules();
		table.setInput(rules);
	}

	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("挑选规则");
	}
	
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, "确定", true);
		createButton(parent, IDialogConstants.CANCEL_ID, "取消", true);
	}
	
	@Override
	protected Point getInitialSize() {
		return new Point(1000, 500);
	}
	
	@Override
	protected void buttonPressed(int buttonId) {
		if(buttonId == IDialogConstants.OK_ID) {
			rulesSelect = new ArrayList<>();
			for (Rule rule : rules) {
				if(rule.isSelected()) {
					rulesSelect.add(rule);
				}
			}
			RuleManager.getInstance().modify(rules);
		}
		super.buttonPressed(buttonId);
	}
	
	public List<Rule> getRulesSelect() {
		return rulesSelect;
	}
}
