package com.shrcn.business.scl.ui;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.shrcn.business.scl.history.HistoryManager;
import com.shrcn.business.scl.history.VersionSequence;
import com.shrcn.business.scl.model.Hitem;
import com.shrcn.found.ui.app.WrappedDialog;
import com.shrcn.found.ui.util.SwtUtil;

public class HItemEditDialog extends WrappedDialog {
	private Text txtVersion;
	private Text txtRevision;
	private Text txtWhat;
	private Text txtWho;
	private Text txtWhen;
	private Text txtWhy;
	
	private Hitem hitem;

	public HItemEditDialog(Shell parentShell) {
		super(parentShell);
	}
	
	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("修改历史");
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);
		GridLayout gridLayout = SwtUtil.getGridLayout(2);
		gridLayout.marginTop = 5;
		gridLayout.marginLeft = 10;
		gridLayout.marginRight = 10;
		container.setLayout(gridLayout);
		
		txtVersion = createText(container, SWT.READ_ONLY, "版本号：");
		txtRevision = createText(container, SWT.READ_ONLY, "修订版本号：");
		txtWhen = createText(container, SWT.READ_ONLY, "生成时间：");
		txtWho = createText(container, SWT.NONE, "修改人：");
//		txtWhat = createText(container, SWT.NONE, "修改内容：");
		txtWhy = createText(container, SWT.NONE, "修改原因：");
		SwtUtil.createLabel(container, "修改内容：", null);
		txtWhat = SwtUtil.createMultiText(container, SWT.NONE, new GridData(GridData.FILL_BOTH));
		
		initData();
		
		return container;
	}
	
	private Text createText(Composite parent, int style, String title) {
		SwtUtil.createLabel(parent, title, null);
		return SwtUtil.createText(parent, style, new GridData(GridData.FILL_HORIZONTAL));
	}
	
	private void initData() {
		HistoryManager hisMgr = HistoryManager.getInstance();
		VersionSequence sequence = VersionSequence.getInstance();
		float[] next = sequence.next(hisMgr.isVersioned(), hisMgr.isReversioned());
		String version = Float.toString(next[0]);
		String revision = Float.toString(next[1]);
		String when = hisMgr.currentTime();
		txtVersion.setText(version);
		txtRevision.setText(revision);
		txtWhen.setText(when);
	}

	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, "确定", true);
		createButton(parent, IDialogConstants.CANCEL_ID, "取消", false);
	}
	
	@Override
	protected Point getInitialSize() {
		return new Point(350, 300);
	}
	@Override
	protected void buttonPressed(int buttonId) {
		if (OK == buttonId) {
			this.hitem = new Hitem(txtVersion.getText(), txtRevision.getText(),
					txtWhat.getText(), txtWho.getText(), txtWhen.getText(),
					txtWhy.getText());
		}
		super.buttonPressed(buttonId);
	}
	public Hitem getHitem() {
		return hitem;
	}
}
