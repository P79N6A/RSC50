package com.shrcn.tool.found.das.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.shrcn.found.common.Constants;
import com.shrcn.found.common.util.StringUtil;
import com.shrcn.tool.found.das.SqlDaoService;
import com.shrcn.tool.rtu.model.BaseCalcExpr;
import com.shrcn.tool.rtu.model.BaseCalcPoint;
import com.shrcn.tool.rtu.model.BaseDcaPoint;
import com.shrcn.tool.rtu.model.BaseDcaied;
import com.shrcn.tool.rtu.model.BaseDpaPoint;
import com.shrcn.tool.rtu.model.TApplication;
import com.shrcn.tool.rtu.model.TDcaChannel;
import com.shrcn.tool.rtu.model.TDcaObject;
import com.shrcn.tool.rtu.model.TInterLock;

public class SqlDaoImpl implements SqlDaoService {

	private static SqlDaoImpl inst;
	
	private SqlDaoImpl() {}
	
	public static SqlDaoImpl getInstance() {
		if (inst == null)
			inst = new SqlDaoImpl();
		return inst;
	}
	
	/**
	 * 根据新旧上一级ID号，在当前表格进行拷贝
	 * 
	 * @param clazz
	 * @param parentStr
	 * @param parentId
	 * @param copyId
	 * @throws SQLException
	 */
	public void copyCurrTable(Class<?> clazz, String parentStr, int parentId, int copyId) {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			// 数据库连接
			conn = DBManagerImpl.getInstance().getConnection(Constants.CURRENT_RTU_NAME);
			
			// 发送sql语句
			String sql = getSql(clazz, getSeqStr(clazz), parentStr, parentId);
			ps = conn.prepareStatement(sql);
			ps.setInt(1, copyId);
			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			dbClose(ps, rs);
		}
	}
	
	/**
	 * 根据上一级ID获取，当前表格ID列表.
	 * 
	 * @param clazz
	 * @param parentStr
	 * @param parentId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Integer> getIds(Class<?> clazz, String parentStr, int parentId){
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(parentStr, parentId);
		String hql = "select id from " + clazz.getName() + " where " + parentStr + "=:" + parentStr;
		return (List<Integer>) HqlDaoImpl.getInstance().getListByHql(hql, params);
	}
	
	private static String getSql(Class<?> clazz, String seqStr, String parentStr, int parentId){
		String tbName = HEntityUtil.getTableName(clazz);
		boolean isSeq = !StringUtil.isEmpty(seqStr);
		List<String> strs = new ArrayList<String>();
		strs.add(parentStr.toUpperCase());
		strs.add(HEntityUtil.getPrimaryKey(clazz).toUpperCase());
		
		// insert into select句式。
		// 插入语句
		String sql = "insert into " + tbName
				+ " (" + (isSeq ? HEntityUtil.getColumnNamesExcludeStr(clazz, parentStr) : HEntityUtil.getColumnNamesExcludeStrs(clazz, strs)) + ","+parentStr+" ) ";
		// 查询语句
		sql += "(select " + (isSeq ? "NEXT VALUE FOR " + seqStr + ", " : "") + HEntityUtil.getColumnNamesExcludeStrs(clazz, strs) + ", ? from " + tbName;
		// 查询条件
		sql += " where "+parentStr+"=" + parentId + ")";
		
		return sql;
	}
	
	/**
	 * 根据数据对象获取Sequence名称。
	 * 
	 * @param clazz
	 * @return
	 */
	public static String getSeqStr(Class<?> clazz) {
		try {
			Object obj = clazz.newInstance();
			if (obj instanceof BaseCalcExpr || obj instanceof BaseDcaPoint) {
				return "DcaPoint_Seq";
			} else if (obj instanceof TDcaChannel || obj instanceof BaseDcaied
					 || obj instanceof TDcaObject
					|| obj instanceof TApplication) {
				return "Common_Seq";
			} else if (obj instanceof BaseCalcPoint
					|| obj instanceof BaseDpaPoint || obj instanceof TInterLock) {
				return "DpaPoint_Seq";
			}
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	/**
	 * @param conn
	 * @param ps
	 * @param rs
	 */
	public static void dbClose(PreparedStatement ps, ResultSet rs) {
		try {
			if (rs != null) {
				rs.close();
			}
			if (ps != null) {
				ps.close();
			}
		} catch (Exception e) {
		}
	}
}
