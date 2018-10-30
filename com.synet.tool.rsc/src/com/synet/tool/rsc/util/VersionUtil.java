package com.synet.tool.rsc.util;

import java.util.List;
import java.util.Map;

import com.shrcn.tool.found.das.HqlDaoService;
import com.shrcn.tool.found.das.impl.HqlDaoImpl;

public class VersionUtil {

	public static void updateDB() {
		// pout 增加 f1061type
		HqlDaoService hqlDao = HqlDaoImpl.getInstance();
		String sql = "SELECT t.TABLENAME, c.COLUMNNAME FROM SYS.SYSTABLES t, SYS.SYSCOLUMNS c WHERE c.REFERENCEID = t.TABLEID AND t.TABLENAME = 'TB1061_POUT'";
		List<Map<String, Object>> columns = (List<Map<String, Object>>) hqlDao.getQueryResultToListMap(sql, null);
		boolean exists = false;
		for (Map<String, Object> column : columns) {
			if ("F1061_TYPE".equalsIgnoreCase(column.get("COLUMNNAME").toString())) {
				exists = true;
				break;
			}
		}
		if (!exists) {
			sql = "alter table TB1061_POUT add F1061_TYPE INT not null default 0";
			hqlDao.updateBySql(sql);
			sql = "update TB1061_POUT a set a.F1061_TYPE=(select b.F1011_NO from TB1016_StateData b where b.F1016_CODE=a.DATA_CODE) where a.DATA_CODE like 'State%'";
			hqlDao.updateBySql(sql);
			sql = "update TB1061_POUT a set a.F1061_TYPE=(select b.F1011_NO from TB1006_AnalogData b where b.F1006_CODE=a.DATA_CODE) where a.DATA_CODE like 'Analog%'";
			hqlDao.updateBySql(sql);
		}
	}
	
}
