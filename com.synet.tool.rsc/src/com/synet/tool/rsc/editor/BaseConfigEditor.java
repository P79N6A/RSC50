/**
 * Copyright (c) 2007-2013 上海思弘瑞电力控制技术有限公司. All rights reserved. 
 * This program is an eclipse Rich Client Application
 * designed for IED configuration and debuging.
 */
package com.synet.tool.rsc.editor;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import net.sf.excelutils2007.ExcelUtils;

import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

import com.shrcn.found.common.util.ObjectUtil;
import com.shrcn.found.common.util.StringUtil;
import com.shrcn.found.ui.editor.BaseEditor;
import com.shrcn.found.ui.editor.ConfigEditorInput;
import com.shrcn.found.ui.editor.IEditorInput;
import com.shrcn.found.ui.model.IField;
import com.shrcn.found.ui.util.SwtUtil;
import com.shrcn.found.ui.view.ConsoleManager;
import com.shrcn.tool.found.das.impl.BeanDaoImpl;
import com.shrcn.tool.found.das.impl.HqlDaoImpl;
import com.synet.tool.rsc.RSCProperties;
import com.synet.tool.rsc.UICommonConstants;
import com.synet.tool.rsc.ui.table.DevKTable;
import com.synet.tool.rsc.util.ExcelFileManager2007;

/**
 * 
 * @author 孙春颖(mailto:scy@shrcn.com)
 * @version 1.0, 2013 5 21
 */
public class BaseConfigEditor extends BaseEditor {
	
	protected static final String DEV_TYPE_TITLE = "装置类型";
	protected static final String DEV_NAME_TITLE = "装置名称";
	
	protected DevKTable table;
	protected BeanDaoImpl beandao;
	protected HqlDaoImpl hqldao;
	protected RSCProperties rscp;
	
	public BaseConfigEditor(Composite container, IEditorInput input) {
		super(container, input);
	}

	@Override
	public void buildUI(Composite container) {
		GridLayout layout = SwtUtil.getGridLayout(1, 0);
		container.setLayout(layout);
	}
	
	@Override
	public void init() {
		super.init();
		beandao = BeanDaoImpl.getInstance();
		hqldao = HqlDaoImpl.getInstance();
		rscp = RSCProperties.getInstance();
	}

	@Override
	public void initData() {
	}

	@Override
	public boolean doSave() {
		return true;
	}
	
	protected void successInfo() {
		setDirty(false);
		ConsoleManager.getInstance().append("装置“" + getName() + "”的" + getEditorName() +
				"保存成功！");
	}

	public String getName() {
		return ((ConfigEditorInput)getInput()).getIedName();
	}
	
	public void exportTemplateExcel(String fileName, String title, IField[] vfields, List<Object> list) {
		long start = System.currentTimeMillis();
		Map<String, String> mapTitle = new LinkedHashMap<String, String>();
		mapTitle.put("key0", title);
		Map<String, String> mapSep = new LinkedHashMap<String, String>();
		mapSep.put("key0", "");
		Map<String, String> mapTime = new LinkedHashMap<String, String>();
		mapTime.put("printDate", StringUtil.getCurrentTime("yyyy年MM月dd日HH点mm分ss秒"));
		
		int fLen = vfields.length;
		String[] fields = new String[fLen];
		for (int i = 0; i < fields.length; i++) {
			IField f = vfields[i];
			fields[i] = f.getTitle();
		}
		
		List<String[]> exportData = new ArrayList<String[]>();
		if (list != null) {
			int index = 1;
			for (Object o : list) {
				String[] row = new String[fLen];
				for (int i=0; i<fLen; i++) {
					IField f = vfields[i];
					String fieldName = f.getName();
					if ("index".equals(fieldName)) {
						row[i] = "" + index;
					} else {
						Object obj = ObjectUtil.getProperty(o, fieldName);
						if (obj == null) {
							obj = "";
						}
						row[i] = "" + obj;
					}
				}
				exportData.add(row);
				index++;
			}
		}
		
		ExcelUtils.addValue("title", mapTitle);
		ExcelUtils.addValue("sep", mapSep);
		ExcelUtils.addValue("time", mapTime);
		ExcelUtils.addValue("width", fLen - 1);
		ExcelUtils.addValue("fields", fields);
		ExcelUtils.addValue("data", exportData);
		ExcelFileManager2007.saveExcelFile(getClass(), UICommonConstants.EXCEL_COMM_EXPORT_2007, fileName);
		long time = (System.currentTimeMillis() - start) / 1000;
		console.append(fileName + "导出完成");
		System.out.println("导出耗时：" + time + "秒");
	}

}
