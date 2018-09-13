package com.synet.tool.rsc.dialog;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.shrcn.found.ui.app.WrappedDialog;
import com.shrcn.found.ui.util.DialogHelper;
import com.shrcn.found.ui.util.SwtUtil;
import com.synet.tool.rsc.GlobalData;
import com.synet.tool.rsc.jdbc.ConnParam;

public class OracleConnDialog extends WrappedDialog {
	
	private Text txtIp;
	private Text txtPort;
	private Text txtDbName;
	private Text txtUser;
	private Text txtPassword;
	private ConnParam connParam;

	public OracleConnDialog(Shell parentShell) {
		super(parentShell);
	}
	
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite composite = SwtUtil.createComposite(parent, new GridData(GridData.FILL_BOTH), 2);
		SwtUtil.createLabel(composite, "主机：", new GridData());
		txtIp = SwtUtil.createText(composite, SwtUtil.bt_hd);
		SwtUtil.createLabel(composite, "端口：", new GridData());
		txtPort = SwtUtil.createText(composite, SwtUtil.bt_hd);
		SwtUtil.createLabel(composite, "服务名：", new GridData());
		txtDbName = SwtUtil.createText(composite, SwtUtil.bt_hd);
		SwtUtil.createLabel(composite, "用户名：", new GridData());
		txtUser = SwtUtil.createText(composite, SwtUtil.bt_hd);
		SwtUtil.createLabel(composite, "密码：", new GridData());
		txtPassword = SwtUtil.createText(composite, SWT.PASSWORD, SwtUtil.bt_hd);

		initData();
		return super.createDialogArea(parent);
	}
	
	
	private void initData() {
		GlobalData instance = GlobalData.getInstance();
		connParam = instance.getConnParam();
		if (connParam != null) {
			txtIp.setText(connParam.getIp());
			txtPort.setText(connParam.getPort());
			txtDbName.setText(connParam.getDbName());
			txtUser.setText(connParam.getUser());
			txtPassword.setText(connParam.getPassword());
		}
	}

	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("导出配置");
	}
	
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, "确定", true);
		createButton(parent, IDialogConstants.CANCEL_ID, "取消", true);
	}
	
	@Override
	protected Point getInitialSize() {
		return new Point(320, 270);
	}
	
	@Override
	protected void buttonPressed(int buttonId) {
		if (buttonId == IDialogConstants.OK_ID) {
			String ip = txtIp.getText().trim();
			String port = txtPort.getText().trim();
			String dbName = txtDbName.getText().trim();
			String user = txtUser.getText().trim();
			String password = txtPassword.getText().trim();
			if (ip == null || "".equals(ip)) {
				DialogHelper.showAsynError("连接主机不能为空！");
				return;
			}
			if (port == null || "".equals(port)) {
				DialogHelper.showAsynError("连接端口不能为空！");
				return;
			}
			if (dbName == null || "".equals(dbName)) {
				DialogHelper.showAsynError("连接服务名不能为空！");
				return;
			}
			if (user == null || "".equals(user)) {
				DialogHelper.showAsynError("连接用户名不能为空！");
				return;
			}
			if (password == null || "".equals(password)) {
				DialogHelper.showAsynError("连接用户名不能为空！");
				return;
			}
			connParam = new ConnParam();
			connParam.setIp(ip);
			connParam.setPort(port);
			connParam.setDbName(dbName);
			connParam.setUser(user);
			connParam.setPassword(password);
			
		}
		super.buttonPressed(buttonId);
	}
	
	public ConnParam getConnParam() {
		return connParam;
	}

}
