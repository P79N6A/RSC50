package com.synet.tool.rsc.dialog;

import java.io.File;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.shrcn.found.common.Constants;
import com.shrcn.found.ui.app.WrappedDialog;
import com.shrcn.found.ui.util.DialogHelper;
import com.shrcn.found.ui.util.SwtUtil;
import com.synet.tool.rsc.io.TemplateExport;
import com.synet.tool.rsc.model.Tb1046IedEntity;

public class ModelCompareDialog extends WrappedDialog{

	private Combo modelCombo;
	private Text textLeft;
	private Text textRight;
	private Tb1046IedEntity iedEntity;

	public ModelCompareDialog(Shell parentShell) {
		super(parentShell);
	}
	
	public ModelCompareDialog(Shell parentShell, Tb1046IedEntity iedEntity) {
		super(parentShell);
		this.iedEntity = iedEntity;
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		GridData gridData = new GridData(GridData.FILL_BOTH);
		Composite composite = SwtUtil.createComposite(parent, gridData, 1);
		composite.setLayout(SwtUtil.getGridLayout(2));
		//左侧
		Composite comLeft = SwtUtil.createComposite(composite, gridData, 1);
		comLeft.setLayout(SwtUtil.getGridLayout(2));
		SwtUtil.createLabel(comLeft, "模版装置配置信息：", new GridData(110, SWT.DEFAULT));
		modelCombo = SwtUtil.createCombo(comLeft, new GridData(110, 25), true);
		GridData gd_2 = new GridData(GridData.FILL_BOTH);
		gd_2.horizontalSpan = 2;
		textLeft = SwtUtil.createText(comLeft, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL, gd_2);
		//右侧
		Composite comRight = SwtUtil.createComposite(composite, gridData, 1);
		comRight.setLayout(SwtUtil.getGridLayout(1));
		SwtUtil.createLabel(comRight, "当前装置配置信息:", new GridData(110, 22));
		textRight = SwtUtil.createText(comRight, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL, gridData);
		initData();
		addListener();
		return composite;
	}
	
	private void addListener() {
		SelectionListener listener = new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(e.getSource() == modelCombo) {
					String fileName = modelCombo.getText();
					String documentStr = getStrByXml(fileName);
					textLeft.setText(documentStr);
				}
			}
		};
		modelCombo.addSelectionListener(listener);
	}

	private void initData() {
		
		initCombo();
		initText();
	}


	private void initText() {
		initModelCfgText();
		initDevCfgText();
		
	}

	private void initDevCfgText() {
		TemplateExport export = new TemplateExport(iedEntity);
		Document devDocument = export.createDocument();
		String tempCfgPath = Constants.tplDir + "tempCfg.xml";
		export.createXml(devDocument, tempCfgPath);
		String documentStr = null;
		try {
			SAXReader reader = new SAXReader();
			Document document = reader.read(new File(tempCfgPath));
			documentStr = document.asXML();
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		textRight.setText(documentStr);
	}

	private void initModelCfgText() {
		//初始化左边txt 模版配置
		String f1046Manufacturor = iedEntity.getF1046Manufacturor();
		String f1046Model = iedEntity.getF1046Model();
		String f1046ConfigVersion = iedEntity.getF1046ConfigVersion();
		
		if(f1046Manufacturor.isEmpty() || f1046Model.isEmpty() || f1046ConfigVersion.isEmpty()) {
			DialogHelper.showAsynInformation("当前装置数据配置不完善，无法找到匹配模版");
			return;
		}
		String fileName = f1046Manufacturor + f1046Model + f1046ConfigVersion;
		
		String documentStr = getStrByXml(fileName);
		textLeft.setText(documentStr);
	}

	private String getStrByXml(String fileName) {
		String pathTemplate = Constants.tplDir.substring(0, Constants.tplDir.length()-1);
		File fileTemplate = new File(pathTemplate);
		if(!fileTemplate.exists()) {
			DialogHelper.showAsynInformation("模版文件夹找不到！");
			return "";
		}
		String path = Constants.tplDir + fileName + ".xml";
		File cfgFile = new File(path);
		if(!cfgFile.exists()) { //判断是否存在模版
			DialogHelper.showAsynInformation("当前设备类型匹配的模版不存在！");
			return "";
		}
		modelCombo.setText(fileName);
		String documentStr = null;
		try {
			SAXReader reader = new SAXReader();
			Document document = reader.read(new File(path));
			documentStr = document.asXML();
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		return documentStr;
	}


	private void initCombo() {
		String pathTemplate = Constants.tplDir.substring(0, Constants.tplDir.length()-1);
		File fileTemplate = new File(pathTemplate);
		if(!fileTemplate.exists()) {
			DialogHelper.showAsynInformation("模版文件夹找不到！");
			return;
		}
		
		File[] listFiles = fileTemplate.listFiles();
		String[] fileNames = new String[listFiles.length];
		for (int i = 0; i < listFiles.length; i++) {
			String temp = listFiles[i].getName();
			int pointIdx = temp.lastIndexOf(".");
			fileNames[i] = temp.substring(0, pointIdx);
		}
		modelCombo.setItems(fileNames);
		modelCombo.select(0);
	}


	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("模版对比");
	}
	
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, "OK", true);
		createButton(parent, IDialogConstants.CANCEL_ID, "取消", true);
	}
	
	@Override
	protected Point getInitialSize() {
		return new Point(1200, 650);
	}
	
//	@Override
//	protected void buttonPressed(int buttonId) {
//		if(buttonId == IDialogConstants.OK_ID){
//			
//		}
//		super.buttonPressed(buttonId);
//	}
	
}
