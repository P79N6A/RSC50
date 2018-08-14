package com.shrcn.business.scl.ui;

import java.util.List;

import org.dom4j.Element;
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
import com.shrcn.business.scl.util.SCLFileManipulate;
import com.shrcn.found.common.Constants;
import com.shrcn.found.ui.app.WrappedDialog;
import com.shrcn.found.ui.util.SwtUtil;
import org.eclipse.swt.widgets.Group;

public class ReleaseVersionDialog extends WrappedDialog {
	
	private Text txtVersion;
	private Text txtRevision;
	private Text txtWhat;
	private Text txtWho;
	private Text txtWhen;
	private Text txtWhy;
	
	private Hitem hitem;
	private Text txtVersion_pre;
	private Text txtRevision_pre;
	private Text txtWhen_pre;
	private Text txtWho_pre;
	private Text txtWhy_pre;
	private Text txtWhat_pre;

	public ReleaseVersionDialog(Shell parentShell) {
		super(parentShell);
	}
	
	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("版本发布");
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);
		GridLayout gridLayout_1 = (GridLayout) container.getLayout();
		gridLayout_1.numColumns = 2;
		GridLayout gridLayout = SwtUtil.getGridLayout(2);
		gridLayout.marginTop = 5;
		gridLayout.marginLeft = 10;
		gridLayout.marginRight = 10;
		container.setLayout(gridLayout);
		
		Composite comp_pre = new Composite(container, SWT.NONE);
		comp_pre.setLayout(new GridLayout(1, false));
		comp_pre.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		Group group_pre = new Group(comp_pre, SWT.NONE);
		group_pre.setLayout(new GridLayout(2, false));
		group_pre.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		group_pre.setText("当前版本");
		
		txtVersion_pre = createText(group_pre, SWT.READ_ONLY, "版本号：");
		txtRevision_pre = createText(group_pre, SWT.READ_ONLY, "修订版本号：");
		txtWhen_pre = createText(group_pre, SWT.READ_ONLY, "生成时间：");
		txtWho_pre = createText(group_pre, SWT.NONE, "修改人：");
		txtWhy_pre = createText(group_pre, SWT.NONE, "修改原因：");
		SwtUtil.createLabel(group_pre, "修改内容：", null);
		txtWhat_pre = SwtUtil.createMultiText(group_pre, SWT.NONE, new GridData(GridData.FILL_BOTH));
		txtVersion_pre.setEditable(false);
		txtRevision_pre.setEditable(false);
		txtWhen_pre.setEditable(false);
		txtWho_pre.setEditable(false);
		txtWhy_pre.setEditable(false);
		txtWhat_pre.setEditable(false);
		Composite comp = new Composite(container, SWT.NONE);
		comp.setLayout(new GridLayout(1, false));
		comp.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		Group group = new Group(comp, SWT.NONE);
		group.setLayout(new GridLayout(2, false));
		group.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		group.setText("发布版本");
		
		txtVersion = createText(group, SWT.READ_ONLY, "版本号：");
		txtRevision = createText(group, SWT.READ_ONLY, "修订版本号：");
		txtWhen = createText(group, SWT.READ_ONLY, "生成时间：");
		txtWho = createText(group, SWT.NONE, "修改人：");
		txtWhy = createText(group, SWT.NONE, "修改原因：");
		SwtUtil.createLabel(group, "修改内容：", null);
		txtWhat = SwtUtil.createMultiText(group, SWT.NONE, new GridData(GridData.FILL_BOTH));
		
		initData();
		
		return container;
	}
	
	private Text createText(Composite parent, int style, String title) {
		SwtUtil.createLabel(parent, title, null);
		return SwtUtil.createText(parent, style, new GridData(GridData.FILL_HORIZONTAL));
	}
	
	private void initData() {
		String fileName = Constants.oldFile;
		if (Constants.HisVer){
			fileName = Constants.mainFile;
		}
		String temp = Constants.getVerFile(fileName);
		String version = "";
		String revision = "";
		VersionSequence sequence = VersionSequence.getInstance();
		List<Element> items = SCLFileManipulate.readVerItems(temp);
		if (items.isEmpty()){
			items.add(SCLFileManipulate.getInitVersion());
		}
		Element lastEle = items.get(items.size()-1);
		txtWho_pre.setText(lastEle.attributeValue("who"));
		txtVersion_pre.setText(lastEle.attributeValue("version"));
		txtRevision_pre.setText(lastEle.attributeValue("revision"));
		txtWhen_pre.setText(lastEle.attributeValue("when"));
		txtWhy_pre.setText(lastEle.attributeValue("why"));
		txtWhat_pre.setText(lastEle.attributeValue("what"));
		String ver = lastEle.attributeValue("version");
		String reVer = lastEle.attributeValue("revision");
		float fver = Float.parseFloat(ver);
		float freVer = Float.parseFloat(reVer);
		float[] next = sequence.nextVer(fver,freVer);
		version = Float.toString(next[0]);
		revision = Float.toString(next[1]);
		
		
		HistoryManager hisMgr = HistoryManager.getInstance();
		String when = hisMgr.currentTime();
		txtWho.setText("Admin");
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
		return new Point(567, 380);
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
