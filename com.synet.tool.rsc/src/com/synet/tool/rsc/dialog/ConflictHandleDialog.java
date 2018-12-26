package com.synet.tool.rsc.dialog;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

import com.shrcn.found.ui.UIConstants;
import com.shrcn.found.ui.app.WrappedDialog;
import com.shrcn.found.ui.util.DialogHelper;
import com.shrcn.found.ui.util.SwtUtil;
import com.shrcn.found.ui.view.ConsoleManager;
import com.synet.tool.rsc.compare.Difference;
import com.synet.tool.rsc.compare.OP;
import com.synet.tool.rsc.incr.ConflictHandler;

public class ConflictHandleDialog extends WrappedDialog {

	private static final String unrename = "无法重命名";
	
	private Button rdRename;
	private Button rdIgnore;
	private org.eclipse.swt.widgets.List nameList;
	
	private Difference diff;
	private List<Difference> brothers;
	
	public ConflictHandleDialog(Difference diff, Shell parentShell) {
		super(parentShell);
		this.diff = diff;
		this.brothers = diff.getParent().getChildren();
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
		
		this.rdIgnore = SwtUtil.createRadioButton(container, "忽略", null);
		this.rdRename = SwtUtil.createRadioButton(container, "重命名", null);
		
		GridData gridData = new GridData(GridData.FILL_BOTH);
		gridData.horizontalSpan = 2;
		this.nameList = createList(container, gridData);

		initData();
		addListeners();
		return container;
	}
	
	private void initData() {
		rdIgnore.setSelection(true);
		enableRename(false);
		OP op = diff.getOp();
		if (OP.DELETE == op) {
			List<String> items = new ArrayList<>();
			for (Difference brother : brothers) {
				items.add(brother.getName() + ":" + brother.getDesc());
			}
			int size = items.size();
			if (size < 1) {
				items.add(unrename);
			}
			nameList.setItems(items.toArray(new String[size]));
		}
	}
	
	private void addListeners() {
		EnableListener listenerEnable = new EnableListener();
		rdIgnore.addSelectionListener(listenerEnable);
		rdRename.addSelectionListener(listenerEnable);
	}

	private void enableRename(boolean able) {
		nameList.setEnabled(able);
	}
	
	class EnableListener extends SelectionAdapter {
		@Override
		public void widgetSelected(SelectionEvent e) {
			enableRename(rdRename.getSelection());
		}
	}

	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("冲突处理");
	}
	
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, "确定", true);
		createButton(parent, IDialogConstants.CANCEL_ID, "取消", true);
	}
	
	@Override
	protected Point getInitialSize() {
		return new Point(350, 500);
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
		if (rdIgnore.getSelection()) {
			diff.setOp(OP.NONE);
		} else {
			String[] names = nameList.getSelection();
			if (names==null || names.length<1) {
				DialogHelper.showWarning("请指定新名称！");
				return false;
			}
			String select = names[0];
			if (!unrename.equals(select)) {
				String[] temp = select.split(":");
				diff.setNewName(temp[0]);
				diff.setNewDesc(temp[1]);
				Difference diffDest = null;
				for (Difference d : brothers) {
					if (diff.getNewName().equals(d.getName())) {
						diffDest = d;
						break;
					}
				}
				new ConflictHandler(diff, diffDest).handle();
				ConsoleManager.getInstance().append("重命名操作已完成！");
			} else {
				DialogHelper.showWarning("无法执行重命名！");
				return false;
			}
		}
		return true;
	}
}
