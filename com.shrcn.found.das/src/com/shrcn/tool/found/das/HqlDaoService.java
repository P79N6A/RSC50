package com.shrcn.tool.found.das;
/**
 * Copyright (c) 2007-2010 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based on IEC61850 SCT.
 */


import java.util.List;
import java.util.Map;

/**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2012-10-11
 */
/**
 * $Log: HqlDaoService.java,v $
 * Revision 1.4  2012/12/05 05:15:00  cchun
 * Update:添加selectInObjects()
 *
 * Revision 1.3  2012/11/16 09:28:50  cchun
 * Update:添加updateBySql()
 *
 * Revision 1.2  2012/11/02 06:48:23  cchun
 * Update:增加updateByHql(),getCount()
 *
 * Revision 1.1  2012/10/17 08:03:55  cchun
 * Add:数据库访问接口
 *
 */
public interface HqlDaoService {
	
	/**
	 * 获取相应查询的数据（带参数）
	 * 
	 * @param hql
	 * @param params
	 * @return List
	 * @throws Exception
	 */
	public List<?> getListByHql(String hql, Map<String, Object> params);

	/**
	 * 获取相应查询的数据（不带参数）
	 * 
	 * @param hql
	 * @return
	 */
	public List<?> getListByHql(String hql);
	

	/**
	 * 根据hql更新数据。例如："update Customer set name = :newName where name = :oldName"
	 * 
	 * @param hql
	 * @param params
	 */
	public void updateByHql(String hql, Map<String, Object> params);
	
	/**
	 * 根据sql更新数据。
	 * @param sql
	 */
	public void updateBySql(String sql);

	/**
	 * 获取相应查询的记录总数
	 * 
	 * @param hql
	 * @param params
	 * @return int
	 * @throws Exception
	 */
	public int getCount(String hql, Map<String, Object> params);
	
	public Object getObject(String hql, Map<String, Object> params);
	
	/**
	 * 统计实体个数。
	 * 
	 * @param clazz
	 * @return
	 */
	public int getCount(Class<?> clazz);
	
	/**
	 * 根据属性统计实体个数。
	 * 
	 * @param clazz
	 * @param property
	 * @param value
	 * @return
	 */
	public int getCount(Class<?> clazz, String property, Object value);
	
	/**
	 * 获取整数查询结果
	 * 
	 * @param hql
	 * @return
	 */
	public int getInteger(String hql);

	/**
	 * 获取字符串查询结果
	 * 
	 * @param hql
	 * @return
	 */
	public String getString(String hql);
	
	/**
	 * 分页获取查询的数据
	 * 
	 * @param hql
	 * @param params
	 * @param currentPage
	 * @param pageSize
	 * @return List
	 * @throws Exception
	 */
	public List<?> getListByHqlAndPage(String hql, Map<String, Object> params, int currentPage,
			int pageSize);
	
	/**
	 * 按指定属性值集合查询
	 * @param clazz
	 * @param property
	 * @param objs
	 * @return
	 */
	public List<?> selectInObjects(Class<?> clazz, String property, List<?> objs);
	
	/**
	 * 按开始结束时间查询
	 * @param clazz
	 * @param property
	 * @param begin
	 * @param end
	 * @return
	 */
	public List<?> selectBetween(Class<?> clazz, String property, String begin, String end);

	/**
	 * 查询最近保存的数据
	 * @param clazz
	 * @return
	 */
	public Object selectTheLastest(Class<?> clazz);
	
	/**
	 * 直接调用本地SQL查询
	 * @param sql
	 * @param clazz
	 * @param colName
	 * @param value
	 * @return
	 */
	public List<?> queryBySql(String sql, Class<?> clazz, String colName, Object value);
	
	/**
	 * 直接调用本地SQL查询
	 * @param sql
	 * @param clazz
	 * @param colName
	 * @param value
	 * @return
	 */
	public List<?> queryBySql(String sql, Class<?> clazz, Map<String, Object> params);
	
	public List<?> queryBySql(String sql, Class<?> clazz, Map<String, Object> params,
			int currentPage, int pageSize);
	
	
	public int executeSql(String sql);
	

	/**
	 * 直接调用本地SQL查询，将查询出来的数据转换为Map集合，单一结果。
	 * @param sql
	 * @param params
	 * @return
	 */
	public Map<String, Object> getQueryResultToMap(String sql, Map<String, Object> params);

	/**
	 * 直接调用本地SQL查询，将查询出来的数据转换为Map集合，多个结果。
	 * @param sql
	 * @param params
	 * @return
	 */
	public List<Map<String, Object>> getQueryResultToListMap(String sql, Map<String, Object> params);
	
	public void insert(String tbName, Map<String, Object> map);
	
	public void deleteAll(Class<?> clazz);
	public void delete(String tbName, int id);
	public void deleteBatch(String tbName, List<Object> objs);
	
	public void update(String tbName, String property, String val, int id);

	public void dropTable(String tbName);
	public int getRecNum(String tbName);
	
	public void createTable(String tbName, Map<String,String> paramMap);

}
