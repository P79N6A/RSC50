package com.synet.tool.rsc.dialog;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

import com.shrcn.found.ui.app.WrappedDialog;
import com.shrcn.found.ui.util.DialogHelper;
import com.shrcn.found.ui.util.SwtUtil;

public class ChooseTableColDialog extends WrappedDialog {
	
	private static final String DEF_COMBO_TEXT = "--请选择--";
	//列与字段的对应关系(key=-1，表示字段无导入信息)
	private Map<Integer, String> map = new HashMap<Integer, String>();
	//字段
	private String[] fieldList = null;
	//Excel列属性 key:第colNum列[colName]， value：colNum
	private Map<String, Integer> excelColMap = new HashMap<>();
	//第colNum列[colName]
	private String[] comboItem;
	List<Label> labelList;
	List<Combo> comboList;

	public ChooseTableColDialog(Shell parentShell) {
		super(parentShell);
	}
	
	@Override
	protected Control createDialogArea(Composite parent) {
		init();
		final ScrolledComposite scrolledComposite = new ScrolledComposite(parent, SWT.V_SCROLL | SWT.BORDER);
        scrolledComposite.setExpandHorizontal(true);
        scrolledComposite.setExpandVertical(true);
        scrolledComposite.setLayout(SwtUtil.getGridLayout(1));
        scrolledComposite.setLayoutData(new GridData(GridData.FILL_BOTH));
		Composite composite = SwtUtil.createComposite(scrolledComposite, new GridData(GridData.FILL_BOTH), 2);
		scrolledComposite.setContent(composite);
		scrolledComposite.setMinSize(scrolledComposite.computeSize(280, 280));
		SwtUtil.createLabel(composite, "导入字段", new GridData());
		SwtUtil.createLabel(composite, "Excel列属性", new GridData());
		GridData gridData = new GridData();
		gridData.widthHint = 150;
		if (fieldList != null && fieldList.length > 0) {
			for (String field : fieldList) {
				labelList.add(SwtUtil.createLabel(composite, field, new GridData()));
				Combo combo = SwtUtil.createCombo(composite,gridData, true);
				combo.setItems(comboItem);
				combo.select(0);
				comboList.add(combo);
			}
		}
		for (int i = 0; i < labelList.size(); i++) {
			String field = labelList.get(i).getText();
			Combo combo = comboList.get(i);
			for (int j = 0; j < comboItem.length; j++) {
				String item = comboItem[j];
				if (item.contains(field)) {
					combo.select(j);
				}
			}
		}
		return super.createDialogArea(parent);
	}
	
	private void init() {
		map = new HashMap<>();
		labelList = new ArrayList<>();
		comboList = new ArrayList<>();
		comboItem = new String[]{};
		if (excelColMap != null && excelColMap.size() > 0) {
			List<String> temp = new ArrayList<>();
			temp.add(DEF_COMBO_TEXT);
			for (String str : excelColMap.keySet()) {
				temp.add(str);
			}
			Collections.sort(temp);
			comboItem = temp.toArray(new String[0]);
		}
	}

	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("Excel导入->列属性匹配");
	}
	
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, "开始导入", true);
		createButton(parent, IDialogConstants.CANCEL_ID, "取消", true);
	}
	
	@Override
	protected Point getInitialSize() {
		return new Point(420, 580);
	}
	
	@Override
	protected void buttonPressed(int buttonId) {
		if (buttonId == IDialogConstants.OK_ID) {
			try {
				for (int i = 0; i < labelList.size(); i++) {
					String field = labelList.get(i).getText();
					String comboText = comboList.get(i).getText().trim();
					if (DEF_COMBO_TEXT.endsWith(comboText)){
						map.put(-1, field);
					} else {
						Integer col = excelColMap.get(comboText);
						map.put(col, field);
					}
				}
			} catch (Exception e) {
				DialogHelper.showAsynError("解析错误！");
			}
		}
		super.buttonPressed(buttonId);
	}
	
	public void setFields(String[] fieldList) {
		this.fieldList = fieldList;
	}

	public void setExcelColMap(Map<Integer, String> excelColName) {
		if (this.excelColMap == null)
			this.excelColMap = new HashMap<>();
		if (excelColName != null && excelColName.size() > 0) {
			for (Integer col : excelColName.keySet()) {
				String name = excelColName.get(col);
				excelColMap.put("第" + col + "列[" + name + "]", col);
			}
		}
	}

	public Map<Integer, String> getMap() {
		return map;
	}
}
