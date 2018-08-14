package com.synet.tool.rsc.dialog;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.shrcn.found.ui.app.WrappedDialog;
import com.shrcn.found.ui.util.SwtUtil;
import com.synet.tool.rsc.ui.TableFactory;
import com.synet.tool.rsc.ui.table.DevKTable;

public class SampleConnectDialog extends WrappedDialog {

	private String curEntryName;
	private DevKTable tableProtctSample;
	private Button btnMove;
	private Button btnSearch;
	private DevKTable tableSample;
	private String[] comboItems;
	
	public SampleConnectDialog(Shell parentShell) {
		super(parentShell);
	}

	public SampleConnectDialog(Shell defaultShell, String curEntryName) {
		super(defaultShell);
		this.curEntryName = curEntryName;
		comboItems = new String[]{"智能终端1"};
	}
	
	@Override
	protected Control createDialogArea(Composite parent) {
		GridData gridData = new GridData(GridData.FILL_BOTH);
		Composite composite = SwtUtil.createComposite(parent, gridData, 1);
		composite.setLayout(SwtUtil.getGridLayout(2));
		//左侧
		Composite comLeft = SwtUtil.createComposite(composite, gridData, 1);
		comLeft.setLayout(SwtUtil.getGridLayout(2));
		GridData gdlb_2 = new GridData(200,25);
		gdlb_2.horizontalSpan = 2;
		String switchLbName = curEntryName + "互感器保护采样值：";
		SwtUtil.createLabel(comLeft, switchLbName, gdlb_2);
		
		tableProtctSample = TableFactory.getProtAnalogTable(comLeft);
		tableProtctSample.getTable().setLayoutData(new GridData(GridData.FILL_BOTH));
		btnMove = SwtUtil.createButton(comLeft, new GridData(40, SWT.DEFAULT), SWT.BUTTON1, "<-");
		//右侧
		Composite comRight = SwtUtil.createComposite(composite, gridData, 1);
		comRight.setLayout(SwtUtil.getGridLayout(3));
		
		Combo combo = SwtUtil.createCombo(comRight, SwtUtil.bt_hd);
		combo.setItems(comboItems);
		combo.select(0);
		
		Text text = SwtUtil.createText(comRight, SwtUtil.bt_hd);
		text.setMessage("描述");
		
		btnSearch = SwtUtil.createButton(comRight, new GridData(50, 25), SWT.BUTTON1, "查询");
		GridData gdSpan_3 = new GridData(GridData.FILL_BOTH);
		gdSpan_3.horizontalSpan = 3;
		tableSample = TableFactory.getAnalogTable(comRight);
		tableSample.getTable().setLayoutData(gdSpan_3);
		addListeners();
		return composite;
	}
	
	
	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("互感器通道关联");
	}
	
	private void addListeners() {
		btnMove.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				super.widgetSelected(e);
			}
		});
		btnSearch.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				super.widgetSelected(e);
			}
		});
		
	}

	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, "OK", true);
		createButton(parent, IDialogConstants.CANCEL_ID, "取消", true);
	}
	
	@Override
	protected Point getInitialSize() {
		return new Point(800, 550);
	}

}
