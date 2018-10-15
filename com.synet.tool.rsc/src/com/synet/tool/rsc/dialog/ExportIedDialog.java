package com.synet.tool.rsc.dialog;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.shrcn.found.ui.app.WrappedDialog;
import com.shrcn.found.ui.util.DialogHelper;
import com.shrcn.found.ui.util.SwtUtil;
import com.shrcn.found.ui.util.UIPreferences;
import com.synet.tool.rsc.model.Tb1046IedEntity;
import com.synet.tool.rsc.service.IedEntityService;
import com.synet.tool.rsc.ui.TableFactory;
import com.synet.tool.rsc.ui.table.DevKTable;

public class ExportIedDialog extends WrappedDialog {

	private static final String pathKey = ExportIedDialog.class.getName() + ".txtFilePath";
	private Text txtFilePath;
	private DevKTable table;
	private IedEntityService iedEntityService;
	private String filePath;
	private List<Tb1046IedEntity> ieds;

	public ExportIedDialog(Shell parentShell) {
		super(parentShell);
	}
	
	@Override
	protected Control createDialogArea(Composite parent) {
		parent.setLayout(SwtUtil.getGridLayout(1));
		Composite topComp = SwtUtil.createComposite(parent, new GridData(GridData.FILL_HORIZONTAL), 3);
		txtFilePath = SwtUtil.createDirectorySelector(topComp, "保存路径：", "请选择文件导出路径");
		
		GridData tableGridData = new GridData(GridData.FILL_BOTH);
		tableGridData.heightHint = 400;
		table = TableFactory.getIEDTable(parent);
		table.getTable().setLayoutData(tableGridData);
		initData();
		txtFilePath.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				UIPreferences.newInstance().setInfo(pathKey, txtFilePath.getText());
			}
		});
		return super.createDialogArea(parent);
	}
	
	
	private void initData() {
		txtFilePath.setText(UIPreferences.newInstance().getInfo(pathKey));
		iedEntityService = new IedEntityService();
		List<Tb1046IedEntity> list = iedEntityService.getIedList();
		if (list != null) {
			table.setInput(list);
		}
	}

	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("装置列表");
	}
	
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, "确定", true);
		createButton(parent, IDialogConstants.CANCEL_ID, "取消", true);
	}
	
	@Override
	protected Point getInitialSize() {
		return new Point(800, 470);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	protected void buttonPressed(int buttonId) {
		if (buttonId == IDialogConstants.OK_ID) {
			filePath = txtFilePath.getText().trim();
			if ("".equals(filePath)) {
				DialogHelper.showAsynError("请选择导出文件保存路径");
				return;
			}
			List<Tb1046IedEntity> inputs = (List<Tb1046IedEntity>) table.getInput();
			if (inputs != null && inputs.size() > 0) {
				ieds = new ArrayList<>();
				for (Tb1046IedEntity ied : inputs) {
					if (ied.isOverwrite()) {//选择
						ieds.add(ied);
					}
				}
			} else {
				DialogHelper.showAsynError("无装置数据");
				return;
			}
		}
		super.buttonPressed(buttonId);
	}
	
	public String getFilePath() {
		return filePath;
	}
	
	public List<Tb1046IedEntity> getIeds() {
		return ieds;
	}
}
