package com.synet.tool.rsc.util;

import java.util.List;
import java.util.Map;

import com.shrcn.tool.found.das.HqlDaoService;
import com.shrcn.tool.found.das.impl.HqlDaoImpl;

public class VersionUtil {

	private static HqlDaoService hqlDao = HqlDaoImpl.getInstance();
	
	public static void updateDB() {
		updatePout();
		updateMms();
	}
	
	private static void updatePout() {
		// pout 增加 f1061type
		if (!existsColumn("TB1061_POUT", "F1061_TYPE")) {
			addTableColumn("TB1061_POUT", "F1061_TYPE", "INT");
			updateDataType("TB1061_POUT", "F1061_TYPE");
		}
		// pout 增加 parentCode
		if (!existsColumn("TB1061_POUT", "Parent_CODE")) {
			addTableColumn("TB1061_POUT", "Parent_CODE", "varchar(48)");
			updateParentCode("TB1061_POUT", "Parent_CODE");
		}
		// core 增加 F1053_CODE
		if (!existsColumn("TB1052_Core", "F1053_CODE")) {
			addTableColumn("TB1052_Core", "F1053_CODE", "varchar(48)");
		}
	}

	private static void updateMms() {
		// mms 增加 f1058type
		if (!existsColumn("TB1058_MMSFCDA", "F1058_Type")) {
			addTableColumn("TB1058_MMSFCDA", "F1058_Type", "INT");
			updateDataType("TB1058_MMSFCDA", "F1058_Type");
		}
	}
	
	private static boolean existsColumn(String tbName, String colName) {
		String sql = "SELECT t.TABLENAME, c.COLUMNNAME FROM SYS.SYSTABLES t, SYS.SYSCOLUMNS c WHERE c.REFERENCEID = t.TABLEID AND t.TABLENAME = '" + 
						tbName + "'";
		List<Map<String, Object>> columns = (List<Map<String, Object>>) hqlDao.getQueryResultToListMap(sql, null);
		boolean exists = false;
		for (Map<String, Object> column : columns) {
			if (colName.equalsIgnoreCase(column.get("COLUMNNAME").toString())) {
				exists = true;
				break;
			}
		}
		return exists;
	}

	private static void updateParentCode(String tbName, String colName) {
		String sql = "update " + tbName + " a set a." + colName + "=(select b.Parent_CODE from TB1016_StateData b where b.F1016_CODE=a.DATA_CODE) where a.DATA_CODE like 'State%'";
		hqlDao.updateBySql(sql);
		sql = "update " + tbName + " a set a." + colName + "=(select b.Parent_CODE from TB1006_AnalogData b where b.F1006_CODE=a.DATA_CODE) where a.DATA_CODE like 'Analog%'";
		hqlDao.updateBySql(sql);
	}
	
	private static void updateDataType(String tbName, String colName) {
		String sql = "update " + tbName + " a set a." + colName + "=(select b.F1011_NO from TB1016_StateData b where b.F1016_CODE=a.DATA_CODE) where a.DATA_CODE like 'State%'";
		hqlDao.updateBySql(sql);
		sql = "update " + tbName + " a set a." + colName + "=(select b.F1011_NO from TB1006_AnalogData b where b.F1006_CODE=a.DATA_CODE) where a.DATA_CODE like 'Analog%'";
		hqlDao.updateBySql(sql);
	}

	private static void addTableColumn(String tbName, String colName, String dType) {
		String sql = "alter table " + tbName + " add " + colName + " " + dType;
		hqlDao.updateBySql(sql);
	}
}
