/**
 * Copyright (c) 2007-2010 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based on IEC61850 SCT.
 */
package com.shrcn.tool.found.das.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.transform.Transformers;

import com.shrcn.found.common.Constants;
import com.shrcn.found.common.log.SCTLogger;
import com.shrcn.found.common.util.StringUtil;
import com.shrcn.tool.found.das.HqlDaoService;
import com.shrcn.tool.found.das.SessionService;


/**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2012-10-11
 */
/**
 * $Log: HqlDaoImpl.java,v $
 * Revision 1.9  2013/06/06 11:04:49  cchun
 * Fix Bug:修复批量导入IED时内存溢出的bug
 *
 * Revision 1.8  2012/12/05 05:14:59  cchun
 * Update:添加selectInObjects()
 *
 * Revision 1.7  2012/11/23 03:49:52  cchun
 * Fix Bug:修复getCount() null 异常
 *
 * Revision 1.6  2012/11/19 07:20:01  cchun
 * Update:iterate添加hasNext()判断
 *
 * Revision 1.5  2012/11/16 09:28:49  cchun
 * Update:添加updateBySql()
 *
 * Revision 1.4  2012/11/07 11:57:13  cchun
 * Update:将getInteger()中Long改成Integer
 *
 * Revision 1.3  2012/11/02 06:50:04  cchun
 * Update:增加flush(),getCount()
 *
 * Revision 1.2  2012/10/29 08:55:27  cchun
 * Fix Bug:修复getCount()缺陷
 *
 * Revision 1.1  2012/10/17 08:04:08  cchun
 * Add:数据库访问实现
 *
 */
public class HqlDaoImpl implements HqlDaoService {

	private SessionService service;

	public void setService(SessionService service) {
		this.service = service;
	}

	private static HqlDaoImpl inst;
	
	private HqlDaoImpl() {}
	
	public static HqlDaoImpl getInstance() {
		if (inst == null)
			inst = new HqlDaoImpl();
		return inst;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cn.org.osgi.module.hibernate.service.CommonDaoService#getCount(java.lang.String,
	 *      java.util.Map)
	 */
	public int getCount(String hql, Map<String, Object> params) {
		Session _session = service.get();
//		service.flush();
		String tmpHql = hql.toLowerCase();
		if (!tmpHql.startsWith("select")) {
			hql = "select count(*) " + hql;
		} else {
			tmpHql = hql.substring(tmpHql.indexOf("from"));
			hql = "select count(*) " + tmpHql;
		}
		int i = 0;
		try {
			Query query = _session.createQuery(hql);
			if (params != null) {
				Iterator<String> it = params.keySet().iterator();
				String name = null;
				for (; it.hasNext(); query.setParameter(name, params.get(name)))
					name = (String) it.next();
			}
			List<?> list = query.setCacheable(true).list();
			Long l =  (list != null && list.size() > 0) ? (Long)list.get(0) : -1;
			i = l.intValue();
		} 
		finally {
			service.flush();
		}
		return i;
	}
	
	public int getCount(Class<?> clazz, String property, Object value) {
		String fk = property;
		int p = fk.lastIndexOf('.');
		if (p > -1)
			fk = fk.substring(p + 1);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(fk, value);
		String hql = "from " + clazz.getName() + " where " + property + "=:" + fk;
		return getCount(hql, params);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cn.org.osgi.module.hibernate.service.CommonDaoService#getListByHql(java.lang.String,
	 *      java.util.Map)
	 */
	public List<?> getListByHql(String hql, Map<String, Object> params) {
		Session _session = service.get();
//		service.flush();
		try {
			Query query = _session.createQuery(hql);
			if (params != null) {
				Iterator<String> it = params.keySet().iterator();
				String name = null;
				for (; it.hasNext(); query.setParameter(name, params.get(name)))
					name = (String) it.next();
			}
			return query.setCacheable(true).list();
		} finally {
//			service.flush();
		}
	}
	
	public void updateByHql(String hql, Map<String, Object> params) {
		Session _session = service.get();
		Query query = _session.createQuery(hql);
		if (params != null) {
			Iterator<String> it = params.keySet().iterator();
			String name = null;
			for (; it.hasNext(); query.setParameter(name, params.get(name)))
				name = (String) it.next();
		}
		query.executeUpdate();
	}
	
	@SuppressWarnings("deprecation")
	public void updateBySql(String sql) {
		Session _session = service.get();
		Connection conn = _session.connection();
		Transaction tx = _session.beginTransaction();
		Statement state = null;
		try {
			state = conn.createStatement();
			state.executeUpdate(sql);
			tx.commit();
		} catch (SQLException e) {
			tx.rollback();
			throw new RuntimeException(e);
		} finally {
			try {
				if (state != null)
				state.close();
			} catch (SQLException e) {
				throw new RuntimeException(e);
			}
		}
	}
	
	public List<?> getListByHql(String hql) {
		return getListByHql(hql, null);
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see cn.org.osgi.module.hibernate.service.CommonDaoService#getListByHqlAndPage(java.lang.String,
	 *      java.util.Map, int, int)
	 */
	public List<?> getListByHqlAndPage(String hql, Map<String, Object> params, int currentPage,
			int pageSize) {
		Session _session = service.get();
//		service.flush();
		try {
			Query query = _session.createQuery(hql);
			if (params != null) {
				Iterator<String> it = params.keySet().iterator();
				String name = null;
				for (; it.hasNext(); query.setParameter(name, params.get(name)))
					name = (String) it.next();

			}
			if (currentPage < 1)
				currentPage = 1;
			query.setFirstResult((currentPage - 1) * pageSize);
			query.setMaxResults(pageSize);
			return query.setCacheable(true).list();
		} finally {
			service.flush();
		}
	}

	@Override
	public int getInteger(String hql) {
		List<?> list = getListByHql(hql);
		Integer l =  (list != null && list.size() > 0 && list.get(0)!=null) ? (Integer)list.get(0) : -1;
		return Integer.valueOf(l.intValue());
	}

	@Override
	public String getString(String hql) {
		List<?> list = getListByHql(hql);
		return (list != null && list.size() > 0) ? (String)list.get(0) : null;
	}
	
	@Override
	public int getCount(Class<?> clazz) {
		String hql = "from " + clazz.getName();
		return getCount(hql, null);
	}
	
	@Override
	public List<?> selectInObjects(Class<?> clazz, String property, List<?> objs) {
		if (objs == null || objs.size() < 1)
			return new ArrayList<Object>();
		StringBuilder sbObjs = new StringBuilder();
		Map<String, Object> params = new HashMap<String, Object>();
		int ldSize = objs.size();
		for (int i = 1; i <=ldSize; i++) {
			Object obj = objs.get(i-1);
			String key = "ld" + i;
			params.put(key, obj);
			sbObjs.append(":" + key);
			if (i < ldSize)
				sbObjs.append(", ");
		}
		String hql = "from " + clazz.getName() + " where " + property + " in (" + sbObjs + ")";
		return getListByHql(hql, params);
	}
	
	@Override
	public List<?> selectBetween(Class<?> clazz, String property, String begin, String end) {
		Map<String, Object> params = new HashMap<String, Object>();
		try {
			Date begint = new SimpleDateFormat(Constants.STD_TIME_FORMAT).parse(begin + " 00:00:00");
			Date endt = new SimpleDateFormat(Constants.STD_TIME_FORMAT).parse(end + " 00:00:00");
			params.put("begint", new Timestamp(begint.getTime()));
			params.put("endt", new Timestamp(endt.getTime()));
		} catch (ParseException e) {
			SCTLogger.error("日期格式错误：", e);
		}
		String hql = "from " + clazz.getName() + " where " + property + " between :begint and :endt";
		return getListByHql(hql, params);
	}
	
	@Override
	public Object selectTheLastest(Class<?> clazz) {
		String hql = "select max(t.id) from " + clazz.getName() + " as t";
		List<?> objs = getListByHql(hql);
		return (objs.size() > 0) ? objs.get(0) : null;
	}
	
	@Override
	public List<?> queryBySql(String sql, Class<?> clazz, String colName, Object value) {
		Session session = service.get();
		SQLQuery query = (SQLQuery) session.createSQLQuery(sql);
		if (!StringUtil.isEmpty(colName))
			query.setParameter(colName, value);
		if (clazz != null)
			query.addEntity(clazz);
		return query.list();
	}
	
	@Override
	public List<?> queryBySql(String sql, Class<?> clazz, Map<String, Object> params) {
		return queryBySql(sql, clazz, params, -1, -1);
	}
	
	@Override
	public List<?> queryBySql(String sql, Class<?> clazz, Map<String, Object> params,
			int currentPage, int pageSize) {
		Session session = service.get();
		SQLQuery query = (SQLQuery) session.createSQLQuery(sql);
		if (params != null) {
			for (String colName : params.keySet()) {
				query.setParameter(colName, params.get(colName));
			}
		}
		if (clazz != null)
			query.addEntity(clazz);
		if (pageSize > 0) {
			if (currentPage < 1)
				currentPage = 1;
			query.setFirstResult((currentPage - 1) * pageSize);
			query.setMaxResults(pageSize);
		}
		return query.list();
	}
	
	
	
	/**
	 * 
	* Discription : 将查询出来的数据转换为Map集合,但前提是只能为一条数据 ,它的key为其查询的字段.
	* @param sql
	* @return
	* Map<String,Object>
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Map<String, Object> getQueryResultToMap(String sql, Map<String, Object> params) {
		Session session = service.get();
		SQLQuery query = (SQLQuery) session.createSQLQuery(sql);
		if(params != null){
			for (String colName : params.keySet()) {
				query.setParameter(colName, params.get(colName));
			}
		}
		return (Map)query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).uniqueResult(); //返回值为map集合 且为唯一值（只能返回一条数据）
	}
	
	/**
	 * 
	 * Discription : 将sql中的全部数据查出来，返回值为一个List<Map<String, Object>>具体用法见
	 * 
	 * @see #getQueryResultToMap(String)
	 * @param sql
	 * @return List<Map<String,Object>>
	 */
	@Override
	public List<Map<String, Object>> getQueryResultToListMap(String sql, Map<String, Object> params) {
		List<Map<String, Object>> result = new ArrayList<>();
//		Session _session = service.get();
//		Connection conn = _session.connection();
		Connection conn = FutIdctDbManagerImpl.getInstance().getConn();
		Statement state = null;
		try {
			state = conn.createStatement();
			ResultSet rs = state.executeQuery(sql);
			ResultSetMetaData rsMeta = rs.getMetaData();
			while (rs.next()) {
				int colnum = rsMeta.getColumnCount();
				Map<String, Object> rowData = new HashMap<String, Object>();
				for (int i=1; i<colnum+1; i++) {
					String colName = rsMeta.getColumnName(i);
					String colLb = rsMeta.getColumnLabel(i);
					Object v = rs.getObject(colName);
					rowData.put(colLb, v);
				}
				result.add(rowData);
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			try {
				if (state != null)
				state.close();
			} catch (SQLException e) {
				throw new RuntimeException(e);
			}
		}
		return result;
	}

	@Override
	public int executeSql(String sql) {
		Session session = service.get();
		Transaction transaction = session.beginTransaction();
		SQLQuery query = (SQLQuery) session.createSQLQuery(sql);
		int rowNum = query.executeUpdate();
		transaction.commit();
		return rowNum;
	}

	@Override
	public void insert(String tbName, Map<String, Object> map) {
			String fieldsStr = " ";
			String valueStr =  " ";
			for (String fieldName : map.keySet()) {
				fieldsStr += "\"" + fieldName + "\",";
				if(map.get(fieldName) instanceof String || "char".equals(map.get(fieldName).getClass().getName())){
					valueStr += "'" + map.get(fieldName) + "',";
				}else {
					valueStr += map.get(fieldName) + ",";
					
				}
			}
			fieldsStr = fieldsStr.substring(0, fieldsStr.length()-1);
			valueStr = valueStr.substring(0, valueStr.length()-1);
			String insertHql = "insert into \"" + tbName + "\" ("
				               + fieldsStr + ")" + " values (" + valueStr + ")";
			executeSql(insertHql);
	}


	@Override
	public int getRecNum(String tbName) {
		String hql = "select * from \"" + tbName + "\"";
		return getQueryResultToListMap(hql, null).size();
	}

	public void deleteAll(Class<?> clazz) {
		String delHql = "delete from " + clazz.getName();
		Session session = service.get();
		Query query = session.createQuery(delHql);
		query.executeUpdate();
	}
	
	@Override
	public void delete(String tbName, int id) {
		String delHql = "delete from \"" + tbName + "\" where \"id\" = " + id;
		executeSql(delHql);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void deleteBatch(String tbName, List<Object> objs) {
		for (Object object : objs) {
			Map<String, String> rowData = (Map<String, String>) object;
			int id = Integer.parseInt(rowData.get("id"));
			delete(tbName, id);
		}
	}

	
	public Map<String, Object> selectById(String tbName, int id) {
		String hql = "select * from \""+ tbName+ "\" where \"id\" = "+ id;
		return getQueryResultToMap(hql, null);
	}

	@Override
	public void update(String tbName, String property, String val, int id) {
		String hql = "update \"" + tbName + "\" set \""+ property + "\" = " + val + " where \"id\" = " + id;
		executeSql(hql);
	}

	@Override
	public void dropTable(String tbName) {
		String hql = "drop table \"" + tbName + "\"";
		executeSql(hql);
	}

	@Override
	public void createTable(String tbName, Map<String,String> paramMap) {
		Set<String> properties = paramMap.keySet();
		String sql = "CREATE TABLE \""+ tbName 
				+ "\" (\"id\" INT generated by default as identity (START WITH 1, INCREMENT BY 1), \"seqNum\" int,";
		for (String property : properties) {
			sql += "\"" + property + "\" " + paramMap.get(property);
		}
	    sql += " primary key(\"id\"))";
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = FutIdctDbManagerImpl.getInstance().getConn();
			ps = conn.prepareStatement(sql);
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			dbClose(ps, rs);
		}
	}
	
	public void dbClose(PreparedStatement ps, ResultSet rs) {
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
