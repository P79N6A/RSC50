package com.synet.tool.rsc.dialog;

import java.util.List;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

import com.shrcn.found.ui.app.WrappedDialog;
import com.shrcn.found.ui.util.DialogHelper;
import com.shrcn.found.ui.util.SwtUtil;
import com.synet.tool.rsc.model.Tb1041SubstationEntity;
import com.synet.tool.rsc.service.SubstationService;
import com.synet.tool.rsc.ui.TableFactory;
import com.synet.tool.rsc.ui.table.DevKTable;

public class SelsectSubstationDialog extends WrappedDialog {

	private DevKTable table;
	private Tb1041SubstationEntity entity;
	
	protected SelsectSubstationDialog(Shell parentShell) {
		super(parentShell);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite composite = SwtUtil.createComposite(parent, new GridData(GridData.FILL_BOTH), 1);
		table = TableFactory.getSubstationTable(composite);
		table.getTable().setLayoutData(new GridData(GridData.FILL_BOTH));
		initData();
		return super.createDialogArea(parent);
	}
	
	private void initData() {
		SubstationService service = new SubstationService();
		List<Tb1041SubstationEntity> list = service.getAllSubstation();
		if (list != null) {
			table.setInput(list);
		}
	}
	
	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("选择所属变电站");
	}
	
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, "确定", true);
		createButton(parent, IDialogConstants.CANCEL_ID, "取消", true);
	}
	
	@Override
	protected Point getInitialSize() {
		return new Point(400, 450);
	}
	
	@Override
	protected void buttonPressed(int buttonId) {
		if (buttonId == IDialogConstants.OK_ID) {
			if (table.getSelection() != null) {
				entity = (Tb1041SubstationEntity) table.getSelection();
			} else {
				DialogHelper.showAsynError("没有选择的数据！");
				return;
			}
		}
		super.buttonPressed(buttonId);
	}

	public Tb1041SubstationEntity getEntity() {
		return entity;
	}

}
