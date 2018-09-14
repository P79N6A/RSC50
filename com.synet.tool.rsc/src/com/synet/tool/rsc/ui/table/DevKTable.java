package com.synet.tool.rsc.ui.table;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import net.sf.excelutils2007.ExcelUtils;

import org.eclipse.swt.widgets.Composite;
import com.shrcn.found.common.util.ObjectUtil;
import com.shrcn.found.common.util.StringUtil;
import com.shrcn.found.ui.UICommonConstants;
import com.shrcn.found.ui.model.IField;
import com.shrcn.found.ui.model.TableConfig;
import com.shrcn.found.ui.table.DefaultKTable;
import com.shrcn.found.ui.table.RKTable;
import com.shrcn.found.ui.util.DialogHelper;
import com.shrcn.tool.found.das.impl.BeanDaoImpl;
import com.synet.tool.rsc.model.Tb1046IedEntity;
import com.synet.tool.rsc.model.Tb1061PoutEntity;
import com.synet.tool.rsc.model.Tb1063CircuitEntity;
import com.synet.tool.rsc.util.ExcelFileManager2007;


public class DevKTable extends RKTable {

	public DevKTable(Composite parent, TableConfig config) {
		super(parent, config);
	}


	protected void initUI() {
		tablemodel = new DevKTableModel(this, config);
		table = new DefaultKTable(parent, tablemodel);	
	}
	
	/**
	 * 重写父类导出Excel方法，改为导出.xlsx格式
	 */
	@Override
	public void exportExcel(String title) {
		String fileName = DialogHelper.getSaveFilePath("保存", "", new String[]{"*.xlsx"});
		if (fileName == null)
			return;
		Map<String, String> mapTitle = new LinkedHashMap<String, String>();
		mapTitle.put("key0", title);
		Map<String, String> mapSep = new LinkedHashMap<String, String>();
		mapSep.put("key0", "");
		Map<String, String> mapTime = new LinkedHashMap<String, String>();
		mapTime.put("printDate", StringUtil.getCurrentTime("yyyy年MM月dd日HH点mm分ss秒"));
		
		IField[] vfields = getExportFields();
		int fLen = vfields.length;
		String[] fields = new String[fLen];
		for (int i = 0; i < fields.length; i++) {
			IField f = vfields[i];
			fields[i] = f.getTitle();
		}
		
		List<String[]> exportData = new ArrayList<String[]>();
		if (getInput() != null) {
			int index = 1;
			for (Object o : getInput()) {
				String[] row = new String[fLen];
				for (int i=0; i<fLen; i++) {
					IField f = vfields[i];
					String fieldName = f.getName();
					if ("index".equals(fieldName))
						row[i] = "" + index;
					else
						row[i] = "" + ObjectUtil.getProperty(o, fieldName);
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
	}

	@Override
	public void saveCellValue(Object data, String property, Object value) {
		if(data instanceof Tb1063CircuitEntity) {
			//根据下拉框选择的POUT描述，将value改为pout对象
			if(property.contains("tb1061PoutByF1061CodeConvChk")) {
				String poutDesc = (String) value;
				Tb1061PoutEntity pout = (Tb1061PoutEntity) 
						BeanDaoImpl.getInstance().getListByCriteria(
								Tb1061PoutEntity.class, "f1061Desc", poutDesc);
				value = pout;
			}
		}
		super.saveCellValue(data, property, value);
	}
}
