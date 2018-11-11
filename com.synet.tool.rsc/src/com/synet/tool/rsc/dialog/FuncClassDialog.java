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
import com.shrcn.tool.found.das.impl.BeanDaoImpl;
import com.shrcn.tool.found.das.impl.HqlDaoImpl;
import com.synet.tool.rsc.DBConstants;
import com.synet.tool.rsc.RSCProperties;
import com.synet.tool.rsc.model.TB1084FuncClassEntity;
import com.synet.tool.rsc.ui.TableFactory;
import com.synet.tool.rsc.ui.table.DevKTable;
import com.synet.tool.rsc.util.FuncManager;

public class FuncClassDialog extends WrappedDialog {

	private Button btnAdd;
	private Button btnDel;
	private DevKTable table;

	public FuncClassDialog(Shell parentShell) {
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
		table = TableFactory.getFunClassTable(composite);
		table.getTable().setLayoutData(griddata_2);
		initData();
		addListeners();
		return parent;
	}
	
	private void initData() {
		List<TB1084FuncClassEntity> fcs = (List<TB1084FuncClassEntity>) HqlDaoImpl.getInstance().getListByHql("from " + TB1084FuncClassEntity.class.getName());
		table.setInput(fcs);
	}

	private void addListeners() {
		SelectionListener listener = new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Object obj =  e.getSource();
				if(obj == btnAdd) {
					TB1084FuncClassEntity defaultRow = (TB1084FuncClassEntity) table.getDefaultRow();
					defaultRow.setF1084CODE(RSCProperties.getInstance().nextTbCode(DBConstants.PR_FUNCLASS));
					table.addRow(defaultRow);
				} else if(obj == btnDel) {
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
		newShell.setText("保护功能定义");
	}
	
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, "保存", false);
	}
	
	@Override
	protected Point getInitialSize() {
		return new Point(800, 600);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	protected void buttonPressed(int buttonId) {
		if (buttonId == IDialogConstants.OK_ID) {
//			boolean confirm = DialogHelper.showConfirm("确定要保存改动？（若无改动请点击\"取消\"按钮）");
//			if(confirm) {//修改rule.xml文件
				List<TB1084FuncClassEntity> fcs = (List<TB1084FuncClassEntity>) table.getInput();
				FuncManager.getInstance().modify(fcs);
				BeanDaoImpl.getInstance().saveBatch(fcs);
//			}
		}
		super.buttonPressed(buttonId);
	}
}
