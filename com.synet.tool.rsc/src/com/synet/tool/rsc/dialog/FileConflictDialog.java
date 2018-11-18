package com.synet.tool.rsc.dialog;

import java.util.List;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

import com.shrcn.found.ui.UIConstants;
import com.shrcn.found.ui.app.WrappedDialog;
import com.shrcn.found.ui.util.DialogHelper;
import com.shrcn.found.ui.util.SwtUtil;
import com.shrcn.found.ui.view.ConsoleManager;
import com.synet.tool.rsc.excel.ImportConfig;
import com.synet.tool.rsc.excel.ImportConfigFactory;
import com.synet.tool.rsc.excel.ImportResult;
import com.synet.tool.rsc.model.IM100FileInfoEntity;
import com.synet.tool.rsc.service.ImprotInfoService;

public class FileConflictDialog extends WrappedDialog {
	
	private boolean next = false;
	private IM100FileInfoEntity oldFileInfo;
	private String fileType;
	private ImportResult importResult;

	public FileConflictDialog(Shell parentShell) {
		super(parentShell);
	}
	
	@Override
	protected Control createDialogArea(Composite parent) {
		String message = "文件已存在，继续导入会覆盖原有数据，请谨慎选择！";
		Label msg = SwtUtil.createLabel(parent, message, new GridData());
		msg.setFont(UIConstants.FONT12);
		msg.setForeground(Display.getDefault().getSystemColor(SWT.COLOR_DARK_RED));
		return super.createDialogArea(parent);
	}
	
	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("导入文件冲突提示信息");
	}
	
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OPEN_ID, "差异比较", true);
		createButton(parent, IDialogConstants.OK_ID, "下一步", true);
		createButton(parent, IDialogConstants.CANCEL_ID, "取消", true);
	}
	
	@Override
	protected Point getInitialSize() {
		return new Point(500, 100);
	}
	
	@Override
	protected void buttonPressed(int buttonId) {
		if (buttonId == IDialogConstants.OK_ID) {
			next = true;
		} else if (buttonId == IDialogConstants.OPEN_ID) {
			showCompare();
		}
		super.buttonPressed(buttonId);
	}
	
	private String getTextByFileInfo(IM100FileInfoEntity fileInfoEntity, Class<?> clazz) {
		ImprotInfoService improtInfoService = new ImprotInfoService();
		List<?> list = improtInfoService.getFileItems(clazz, fileInfoEntity);
		return getTextByList(list);
	}
	
	private String getTextByList(List<?> list) {
		if (list != null && list.size() > 0) {
			StringBuffer buffer = new StringBuffer();
			int i = 1;
			for (Object obj : list) {
				String str = obj.toString();
				buffer.append(i++ + " " + str + "\n");
			}
			return buffer.toString();
		}
		return "";
	}
	
	private void showCompare() {
		ImportConfig config = ImportConfigFactory.getImportConfig(fileType);
		if (config != null) {
			Class<?> clazz = config.getEntityClass();
			//获取方法
			//调用
			try {
				String title1 = "old File";
				String txt1 = getTextByFileInfo(oldFileInfo, clazz);
					
				String title2 = "new File";
				String txt2 = getTextByList(importResult.getResult());
					
				DialogHelper.showCompareDlg(title1, txt1, title2, txt2);
			} catch (Exception e) {
				e.printStackTrace();
				ConsoleManager.getInstance().append("打开差异比较异常！");
			}
		}
	}
	
	public void setData(IM100FileInfoEntity oldFileInfo, ImportResult importResult, String fileType) {
		this.oldFileInfo = oldFileInfo;
		this.importResult = importResult;
		this.fileType = fileType;
	}
	
	public boolean isNext() {
		return next;
	}

}
