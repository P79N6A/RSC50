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

import com.shrcn.found.ui.app.WrappedTitleAreaDialog;
import com.shrcn.found.ui.util.SwtUtil;
import com.synet.tool.rsc.ui.TableFactory;
import com.synet.tool.rsc.ui.table.DevKTable;

public class ChanelConnectDialog extends WrappedTitleAreaDialog{

	private DevKTable tableState;
	private DevKTable tableChanel;
	private Button btnMove;
	private String curEntryName;
	private String[] comboDevItems;
	private String[] comboSvItems;

	public ChanelConnectDialog(Shell parentShell) {
		super(parentShell);
	}
	
	public ChanelConnectDialog(Shell parentShell, String curEntryName) {
		super(parentShell);
		this.curEntryName = curEntryName;
		comboDevItems = new String[]{"装置"};
		comboSvItems = new String[]{"SV控制块"};
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
		String switchLbName = curEntryName + "互感器次级：";
		SwtUtil.createLabel(comLeft, switchLbName, gdlb_2);
		
		tableChanel = TableFactory.getSwitchStatusTable(comLeft);
		tableChanel.getTable().setLayoutData(new GridData(GridData.FILL_BOTH));
		btnMove = SwtUtil.createButton(comLeft, new GridData(40, SWT.DEFAULT), SWT.BUTTON1, "<-");
		
		
		//右侧
		Composite comRight = SwtUtil.createComposite(composite, gridData, 1);
		comRight.setLayout(SwtUtil.getGridLayout(2));
		
		Combo comboDevice = SwtUtil.createCombo(comRight, SwtUtil.bt_hd);
		comboDevice.setItems(comboDevItems);
		comboDevice.select(0);
		
		Combo comboSvControl = SwtUtil.createCombo(comRight, SwtUtil.bt_hd);
		comboSvControl.setItems(comboSvItems);
		comboSvControl.select(0);
		
		SwtUtil.createLabel(comRight, "			", new GridData(SWT.DEFAULT,10));
		GridData gdSpan_2 = new GridData(GridData.FILL_BOTH);
		gdSpan_2.horizontalSpan = 2;
		tableState = TableFactory.getSwitchStatusTable(comRight);
		tableState.getTable().setLayoutData(gdSpan_2);
		addListeners();
		return parent;
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
