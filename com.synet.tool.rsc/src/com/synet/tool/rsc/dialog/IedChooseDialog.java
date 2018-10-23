package com.synet.tool.rsc.dialog;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.shrcn.found.common.util.StringUtil;
import com.shrcn.found.ui.table.KTableEditorDialog;
import com.shrcn.found.ui.util.SwtUtil;
import com.synet.tool.rsc.model.Tb1046IedEntity;
import com.synet.tool.rsc.model.Tb1090LineprotfiberEntity;
import com.synet.tool.rsc.model.Tb1091IotermEntity;
import com.synet.tool.rsc.model.Tb1092PowerkkEntity;
import com.synet.tool.rsc.service.DefaultService;
import com.synet.tool.rsc.ui.TableFactory;
import com.synet.tool.rsc.ui.table.DevKTable;

public class IedChooseDialog extends KTableEditorDialog {
	
	private DevKTable table;
	private DefaultService defaultService;
	private List<Tb1046IedEntity> list;

	public IedChooseDialog(Shell parentShell, Object item) {
		super(parentShell, item);
	}
	
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = SwtUtil.createComposite(parent, new GridData(GridData.FILL_BOTH), 2);
		final Text txIED = SwtUtil.createText(container, new GridData(300, SWT.DEFAULT));
		txIED.setMessage("装置名称或描述");
		final Button btSearch = SwtUtil.createPushButton(container, "查询", new GridData(100, SWT.DEFAULT));
		btSearch.setFocus();
		
		table = TableFactory.getIEDChooseTable(container);
		GridData tbData = new GridData(GridData.FILL_BOTH);
		tbData.horizontalSpan = 2;
		table.getTable().setLayoutData(tbData);
		initData();
		btSearch.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String msg = txIED.getText().trim();
				if (StringUtil.isEmpty(msg)) {
					table.setInput(list);
					txIED.setText("");
				} else {
					List<Tb1046IedEntity> listInput = new ArrayList<>();
					for (Tb1046IedEntity ied : list) {
						if (ied.getF1046Name().toLowerCase().contains(msg.toLowerCase()) || 
								ied.getF1046Desc().toLowerCase().contains(msg.toLowerCase())) {
							listInput.add(ied);
						}
					}
					table.setInput(listInput);
				}
			}
		});
		return container;
	}
	
	private void initData() {
		defaultService = new DefaultService();
		list = defaultService.getIedList();
		if (list != null) {
			table.setInput(list);
		}
	}
	
	@Override
	protected void buttonPressed(int buttonId) {
		if (buttonId == OK) {
			if (table.getSelection() != null) {
				Tb1046IedEntity entity = (Tb1046IedEntity) table.getSelection();
				if (item instanceof Tb1090LineprotfiberEntity) {
					((Tb1090LineprotfiberEntity)item).setTb1046IedByF1046Code(entity);
				} else if (item instanceof Tb1091IotermEntity) {
					((Tb1091IotermEntity)item).setTb1046IedByF1046Code(entity);
				} else if (item instanceof Tb1092PowerkkEntity) {
					((Tb1092PowerkkEntity)item).setTb1046IedByF1046Code(entity);
				}
				defaultService.saveTableData(item);
			}
		}
		super.buttonPressed(buttonId);
	}
	
	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("IED设备列表"); //$NON-NLS-1$
	}
	
	@Override
	protected Point getInitialSize() {
		return new Point(680, 510);
	}

}
