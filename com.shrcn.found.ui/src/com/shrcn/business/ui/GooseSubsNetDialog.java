package com.shrcn.business.ui;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

import com.shrcn.found.common.util.StringUtil;
import com.shrcn.found.ui.app.WrappedDialog;
import com.shrcn.found.ui.model.FieldBase;
import com.shrcn.found.ui.model.IField;
import com.shrcn.found.ui.model.TableConfig;
import com.shrcn.found.ui.table.RKTable;
import com.shrcn.found.ui.util.DialogHelper;

import de.kupzog.ktable.KTableCellSelectionAdapter;

public class GooseSubsNetDialog extends WrappedDialog {

	private RKTable gooseSubsNetTable;
	private String netPorts;
	private String existPorts;
	private int totalNum;
	private boolean hasPrefix;
	private int count;
	private boolean hasPorts;

	public GooseSubsNetDialog(Shell parentShell, String netPorts, int pNums) {
		super(parentShell);
		this.netPorts = netPorts;
		this.totalNum = pNums;
		this.count = pNums;
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);
		container.setLayout(new GridLayout(1, true));

		final Composite tablecmp = new Composite(container, SWT.NONE);
		tablecmp.setLayout(new FillLayout());
		tablecmp.setLayoutData(new GridData(GridData.FILL_BOTH));

		gooseSubsNetTable = getGooseSubsNetTable(tablecmp);
		gooseSubsNetTable.getTable().addCellSelectionListener(
				new KTableCellSelectionAdapter() {
					@Override
					public void fixedCellSelected(int col, int row,
							int statemask) {
						// 全选功能
						if (row == 0 && col == 1 && count > 1) {// count为1的不能进行全选操作
							List<?> input = gooseSubsNetTable.getInput();
							boolean b = true;
							// 计算选中数据个数
							int size = 0;
							for (Object obj : input) {
								GooseSubsNet net = (GooseSubsNet) obj;
								if (net.getSelect()) {
									size++;
								} else {
									break;
								}
							}
							b = (input.size() > size);
							// 全选或全部取消
							for (Object obj : input) {
								GooseSubsNet net = (GooseSubsNet) obj;
								net.setSelect(b);
							}
							gooseSubsNetTable.refresh();
						}
					}
				});

		setValues(netPorts);
		return container;
	}

	private RKTable getGooseSubsNetTable(Composite parent) {
		IField field = new FieldBase("max", "最大值", count);
		field.setVisible(false);
		IField selField = new FieldBase("select", "选择", 80);
		selField.setEditor("checkbox");
		IField[] fields = new IField[] {
				new FieldBase("index", "网口", "", "0"),
				selField, field
		};
		TableConfig tc = new TableConfig("NetPorts", "网口设置", GooseSubsNet.class.getName(), GooseSubsTable.class.getName());
		tc.setFields(fields);
		return new GooseSubsTable(parent, tc);
	}

	/**
	 * 初始化网口表格
	 * 
	 * @param netPorts
	 */
	public void setValues(String netPorts) {
		List<GooseSubsNet> list = new ArrayList<GooseSubsNet>();
		for (int i = 0; i < totalNum; i++) {
			GooseSubsNet e = new GooseSubsNet("" + (i + 1), false);
			list.add(e);
		}
		gooseSubsNetTable.setInput(list);

		// 设置初始值
		if (!StringUtil.isEmpty(netPorts)) {
			String[] nets = netPorts.split(",");
			if (nets.length > 0 && nets[0].startsWith("F"))
				hasPrefix = true;
			for (String net : nets) {
				if (hasPrefix)
					net = net.substring(1);
				int index = Integer.parseInt(net);
				GooseSubsNet e = list.get(index - 1);
				e.setSelect(true);
			}
		}
	}

	public void getData() {

	}

	public String getNetPorts() {
		return netPorts;
	}

	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("选择网口");
	}

	@Override
	protected Point getInitialSize() {
		return new Point(325, 320);
	}

	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, "确定", true);
		createButton(parent, IDialogConstants.CANCEL_ID, "取消", false);
	}

	/**
	 * 获取配置信息
	 * 
	 * @return
	 */
	private String getNetPortsValue() {
		List<?> list = gooseSubsNetTable.getInput();
		String nums = "";
		for (int i = 0; i < totalNum; i++) {
			GooseSubsNet subnet = (GooseSubsNet) list.get(i);
			if (subnet.getSelect()) {
				if ("".equals(nums))
					nums += (hasPrefix ? "F" : "") + (i + 1);
				else
					nums += (hasPrefix ? ",F" : ",") + (i + 1);
			}
		}
		return nums;
	}

	@Override
	protected void buttonPressed(int buttonId) {
		if (buttonId == IDialogConstants.OK_ID) {
			netPorts = getNetPortsValue();
			if (netPorts.split(",").length > count) {
				DialogHelper.showWarning("最多只能选择" + count + "个网口！");
				return;
			}
			if (!hasPorts){
				DialogHelper.showWarning("选择双网只能是SGA网口和SGB网口！");
				return;
			}
			if (!StringUtil.isEmpty(existPorts) && isSameNet()) {
				DialogHelper.showWarning("同一控制块下一个网口只能作为A网口或者B网口！");
				return;
			}
		}
		super.buttonPressed(buttonId);
	}

	private boolean isSameNet() {
		String[] split = existPorts.split(",");
		String[] nets = netPorts.split(",");
		for (String port : split) {
			port = port.replace("F", "");
			for (String net : nets) {
				net = net.replace("F", "");
				if (net.equals(port)) {
					return true;
				}
			}
		}
		return false;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public void setExistPorts(String existPorts) {
		this.existPorts = existPorts;
	}
	public void setHasPorts(boolean hasPorts) {
		this.hasPorts = hasPorts;
	}
}
