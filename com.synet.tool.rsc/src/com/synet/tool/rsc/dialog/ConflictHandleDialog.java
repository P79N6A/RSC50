package com.synet.tool.rsc.dialog;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;

import com.shrcn.found.ui.UIConstants;
import com.shrcn.found.ui.app.WrappedDialog;
import com.shrcn.found.ui.util.DialogHelper;
import com.shrcn.found.ui.util.SwtUtil;
import com.synet.tool.rsc.compare.Difference;
import com.synet.tool.rsc.compare.OP;

public class ConflictHandleDialog extends WrappedDialog {

	private org.eclipse.swt.widgets.List nameList;
	
	private Difference diff;
	private Difference diffDest;
	private List<Difference> brothers;
	
	public ConflictHandleDialog(Difference diff, List<Difference> brothers, Shell parentShell) {
		super(parentShell);
		this.diff = diff;
		this.brothers = brothers;
	}

	public static org.eclipse.swt.widgets.List createList(Composite parent, GridData gridData) {
		org.eclipse.swt.widgets.List list = new org.eclipse.swt.widgets.List(parent, SWT.SINGLE|SWT.BORDER|SWT.V_SCROLL|SWT.H_SCROLL);
		list.setFont(UIConstants.FONT_CONTENT);
		list.setLayoutData(gridData);
		return list;
	}
	
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);
		container.setLayout(SwtUtil.getGridLayout(2));
		
		GridData gridData = new GridData(GridData.FILL_BOTH);
		gridData.horizontalSpan = 2;
		this.nameList = createList(container, gridData);

		initData();
		addListeners();
		return container;
	}
	
	private void initData() {
		List<String> items = new ArrayList<>();
		for (Difference brother : brothers) {
			items.add(brother.getName() + ":" + brother.getDesc());
		}
		nameList.setItems(items.toArray(new String[items.size()]));
	}
	
	private void addListeners() {
		nameList.addListener(SWT.MouseDoubleClick, new Listener() {
			@Override
			public void handleEvent(Event event) {
				buttonPressed(OK);
			}});
	}


	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("重命名设置");
	}
	
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, "确定", true);
		createButton(parent, IDialogConstants.CANCEL_ID, "取消", true);
	}
	
	@Override
	protected Point getInitialSize() {
		return new Point(350, 350);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	protected void buttonPressed(int buttonId) {
		if (OK == buttonId) {
			if (!handleConfilct()) {
				return;
			}
		}
		super.buttonPressed(buttonId);
	}

	private boolean handleConfilct() {
		String[] names = nameList.getSelection();
		if (names==null || names.length<1) {
			DialogHelper.showWarning("请指定新名称！");
			return false;
		}
		String select = names[0];
		String[] temp = select.split(":");
		diff.setNewName(temp[0]);
		diff.setNewDesc(temp[1]);
		diff.setOp(OP.RENAME);
		for (Difference d : brothers) {
			if (diff.getNewName().equals(d.getName())) {
				diffDest = d;
				break;
			}
		}
		diffDest.setOp(OP.NONE);
		return true;
	}

	public Difference getDiffDest() {
		return diffDest;
	}
}
