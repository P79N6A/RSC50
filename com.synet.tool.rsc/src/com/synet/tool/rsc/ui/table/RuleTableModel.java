package com.synet.tool.rsc.ui.table;

import java.util.List;

import com.shrcn.found.ui.model.IField;
import com.shrcn.found.ui.model.TableConfig;
import com.shrcn.found.ui.table.RKTableModel;
import com.shrcn.found.ui.table.XOTable;
import com.shrcn.found.ui.util.DialogHelper;
import com.synet.tool.rsc.util.F1011_NO;
import com.synet.tool.rsc.util.Rule;

public class RuleTableModel extends RKTableModel{

	public RuleTableModel(XOTable table, TableConfig config) {
		super(table, config);
	}

	@Override
	public void doSetContentAt(int col, int row, Object value) {
		Rule obj = (Rule) getItem(row);
		IField f = fields[col];
		String title = f.getTitle();
		if("功能类型".equals(title)) {//更改时，禁止出现重复的功能类型
			@SuppressWarnings("unchecked")
			List<Rule> input = (List<Rule>) table.getInput();
			for (int i = 0; i < input.size(); i++) {
				if(i==row-1) {
					continue;
				}
				Rule rule = input.get(i);
				if(rule.getName().equals(value)) {
					DialogHelper.showAsynInformation("功能类型\" " + value + "\" 已存在");
					return;
				}
			}
		}
		super.doSetContentAt(col, row, value);
		obj.setName(F1011_NO.getNameById(obj.getId()));
	}
}
