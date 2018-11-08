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
 * @version 1.0, 2012-10-12
 */
/**
 * $Log: BeanDaoService.java,v $
 * Revision 1.10  2013/08/06 12:59:39  cxc
 * Add:添加数据库操作新接口
 *
 * Revision 1.9  2013/04/03 03:14:13  scy
 * Update：增加批量删除的方法
 *
 * Revision 1.8  2013/02/05 02:51:04  scy
 * Add：1、增加批量更新的方法；
 * 	  2、增加按照id进行排序的获取数据列表的方法；
 *
 * Revision 1.7  2012/12/06 04:48:32  cchun
 * Update:添加updateEvict()
 *
 * Revision 1.6  2012/12/06 02:41:55  cchun
 * Update:添加updateProperty()
 *
 * Revision 1.5  2012/11/12 00:55:16  cchun
 * Fix Bug:修改getListByCriteria()返回值
 *
 * Revision 1.4  2012/11/02 06:48:01  cchun
 * Update:增加getRefresh()
 *
 * Revision 1.3  2012/10/29 08:54:46  cchun
 * Update:增加getListByCriteria()和deleteAll()
 *
 * Revision 1.2  2012/10/22 07:25:56  cchun
 * Update:添加分页和批量处理方法
 *
 * Revision 1.1  2012/10/17 08:03:55  cchun
 * Add:数据库访问接口
 *
 */
public interface BeanDaoService {

	public boolean exitService();
	/**
	 * 保存持久层对象
	 * 
	 * @param obj
	 */
	public void save(Object obj);
	
	public void saveBatch(List<?> objs);
	
	public void saveOrUpdateBatch (List<?> objs);
	public void saveOrUpdate (Object obj);

	/**
	 * 添加持久层对象
	 * 
	 * @param obj
	 */
	public void insert(Object obj);

	public void insertBatch(List<?> objs);
	
	/**
	 * 更新持久层对象
	 * 
	 * @param obj
	 */
	public void update(Object obj);
	
	public void updateBatch(List<?> objs);
	
	public void updateEvict(Object obj);

	public void updateProperty(Object obj, String property, Object value);
	
	/**
	 * 删除持久层对象
	 * 
	 * @param obj
	 */
	public void delete(Object obj);


	public void deleteBatch(List<?> obj);

	/**
	 * 判断对象是否存在
	 * @param obj
	 */
	public boolean exists(Object obj);
	
	/**
	 * 得到主键字段值
	 * @param obj
	 * @return
	 */
	public String getIdentifierName(Class<?> clazz);
	
	/**
	 * 根据主键ID删除持久层对象
	 * 
	 * @param po
	 * @param id
	 */
	public void deleteById(Class<?> po, int id);
	
	/**
	 * 删除该持久层对象的所有数据
	 * 
	 * @param po
	 */
	public void deleteAll(Class<?> po);
	
	/**
	 * 按指定条件删除该持久层对象的所有数据。
	 * 
	 * @param po
	 * @param property
	 * @param value
	 */
	public void deleteAll(Class<?> po, String property, Object value);
	
	/**
	 * 根据主键ID获取持久层对象
	 * 
	 * @param po
	 * @param id
	 * @return Object
	 * @throws Exception
	 */
	public Object getById(Class<?> po, int id);
	
	/**
	 * 获取表中所有数据
	 * @param po
	 * @return
	 */
	public List<?> getAll(Class<?> po);
	
	/**
	 * 刷新对象。
	 * 
	 * @param obj
	 * @return
	 */
	public Object getRefresh(Object obj);

	/**
	 * 根据主键ID判断对象是否存在
	 * 
	 * @param po
	 * @param id
	 * @return
	 */
	public boolean exists(Class<?> po, int id);
	
	public boolean isSame(Object obj1, Object obj2);
	
	/**
	 * 按指定属性约束查询结果，并返回单条记录。如果结果不唯一，
	 * 则返回第一条记录；如果不存在，则返回null。
	 * 
	 * @param clazz
	 * @param params
	 * @return
	 */
	public Object getObject(Class<?> clazz, Map<String, Object> params);

	/**
	 * 按属性查询记录，并返回单条记录。如果结果不唯一，
	 * 则返回第一条记录；如果不存在，则返回null。
	 * 
	 * @param clazz
	 * @param property
	 * @param value
	 * @return
	 */
	public Object getObject(Class<?> clazz, String property, Object value);

	/**
	 * 按指定属性约束查询结果，并返回全部记录。
	 * 
	 * @param clazz
	 * @param params
	 * @return
	 */
	public List<?> getListByCriteria(Class<?> clazz, Map<String, Object> params);
	
	public List<?> getListByCriteria(Class<?> clazz, String property, Object value);
	
	/**
	 * 按指定属性约束查询结果，并返回全部记录。
	 * 
	 * @param clazz
	 * @param params
	 * @return
	 */
	public List<?> getListOrderById(Class<?> clazz, Map<String, Object> params);
	
	/**
	 * 按指定属性约束查询结果同时按照id进行排序后，返回全部记录。
	 * 
	 * @param clazz
	 * @param property
	 * @param value
	 * @return
	 */
	public List<?> getListOrderById(Class<?> clazz, String property, Object value);
	
	public List<?> getListByOrderCriteria(Class<?> clazz, String property, Map<String, Object> params);
	public List<?> getListByOrderCriteriaForGt(Class<?> clazz, String property, Map<String, Object> params);
	
	public List<?> getListLike(Class<?> clazz, String property, String param);
	/**
	 * 按指定属性约束查询结果，并返回指定页记录。
	 * 
	 * @param clazz
	 * @param params
	 * @param currentPage
	 * @param pageSize
	 * @return
	 */
	public List<?> getListByCriteriaAndPage(Class<?> clazz, Map<String, Object> params, int currentPage,
			int pageSize);
}
