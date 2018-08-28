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
import com.shrcn.found.ui.util.DialogHelper;
import com.shrcn.found.ui.util.SwtUtil;
import com.synet.tool.rsc.model.Tb1041SubstationEntity;
import com.synet.tool.rsc.model.Tb1049RegionEntity;
import com.synet.tool.rsc.service.DefaultService;
import com.synet.tool.rsc.util.RscObjectUtils;

public class AddRegionDialog extends WrappedDialog {
	
	private Text txtF1049Code;
	private Text txtF1049Name;
	private Text txtF1049Desc;
	private Combo cmbF1049Area;
	private Text txtF1041Name;
	private Button btnBrowse;
	private Tb1041SubstationEntity substationEntity;
	private Tb1049RegionEntity regionEntity;

	public AddRegionDialog(Shell parentShell) {
		super(parentShell);
	}
	
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite composite = SwtUtil.createComposite(parent, new GridData(GridData.FILL_BOTH), 2);
		txtF1049Code = SwtUtil.createText(composite, SwtUtil.bt_hd);
		txtF1049Code.setMessage("");
		txtF1049Name = SwtUtil.createText(composite, SwtUtil.bt_hd);
		txtF1049Name.setMessage("区域名称");
		txtF1049Desc = SwtUtil.createText(composite, SwtUtil.bt_hd);
		txtF1049Desc.setMessage("区域描述");
		cmbF1049Area = SwtUtil.createCombo(composite, SwtUtil.bt_hd, true);
		cmbF1049Area.setItems(new String[]{"户内", "户外"});
		cmbF1049Area.select(0);
		txtF1041Name = SwtUtil.createText(composite, SwtUtil.bt_hd);
		txtF1041Name.setMessage("变电站名称");
		btnBrowse = SwtUtil.createButton(composite, SwtUtil.bt_gd, SWT.BUTTON1, "浏览");
		initData();
		addListeners();
		return super.createDialogArea(parent);
	}
	
	private void initData() {
		regionEntity = RscObjectUtils.createRegion();
		txtF1049Code.setText(regionEntity.getF1049Code());
	}
	
	private void addListeners() {
		btnBrowse.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				selsectSubstation();
			}
		});
	}
	
	private void selsectSubstation() {
		SelsectSubstationDialog dialog = new SelsectSubstationDialog(getShell());
		if (dialog.open() == 0) {
			substationEntity = dialog.getEntity();
			txtF1041Name.setText(substationEntity.getF1041Name());
		}
	}
	
	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("添加区域信息");
	}
	
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, "添加", true);
		createButton(parent, IDialogConstants.CANCEL_ID, "取消", true);
	}
	
	@Override
	protected Point getInitialSize() {
		return new Point(340, 200);
	}
	
	@Override
	protected void buttonPressed(int buttonId) {
		if (buttonId == IDialogConstants.OK_ID) {
			String f1049Name = txtF1049Name.getText().trim();
			String f1049Desc = txtF1049Desc.getText().trim();
			String f1049Area = cmbF1049Area.getText().trim();
			if ("".equals(f1049Name)) {
				DialogHelper.showAsynInformation("请输入区域名称！");
				return;
			}
			if (substationEntity == null) {
				DialogHelper.showAsynError("请选择所属变电站！");
				return;
			}
			regionEntity.setF1049Name(f1049Name);
			regionEntity.setF1049Desc(f1049Desc);
			regionEntity.setF1049Area("户内".equals(f1049Area)? 0:1);
			regionEntity.setTb1041SubstationByF1041Code(substationEntity);
			try {
				new DefaultService().insert(regionEntity);
				DialogHelper.showAsynError("添加区域信息成功！");
			} catch (Exception e) {
				DialogHelper.showAsynError("添加区域信息失败！");
				return;
			}
			
		}
		super.buttonPressed(buttonId);
	}

}
