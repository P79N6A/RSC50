/**
 * Copyright (c) 2007-2010 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based on IEC61850 SCT.
 */
package com.shrcn.tool.found.das.impl;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.mapping.PersistentClass;
import org.hibernate.mapping.Property;

import com.shrcn.found.common.log.SCTLogger;
import com.shrcn.found.common.util.ObjectUtil;
import com.shrcn.tool.found.das.BeanDaoService;
import com.shrcn.tool.found.das.DBManager;
import com.shrcn.tool.found.das.SessionService;


/**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2012-10-11
 */
/**
 * $Log: BeanDaoImpl.java,v $
 * Revision 1.15  2013/06/06 11:04:50  cchun
 * Fix Bug:修复批量导入IED时内存溢出的bug
 *
 * Revision 1.14  2013/04/03 03:14:12  scy
 * Update：增加批量删除的方法
 *
 * Revision 1.13  2013/02/05 02:50:54  scy
 * Add：1、增加批量更新的方法；
 * 	  2、增加按照id进行排序的获取数据列表的方法；
 * 	  3、增加指定参数进行排序的获取数据列表的方法。
 *
 * Revision 1.12  2013/01/25 09:08:52  cchun
 * Fix Bug:修复updateProperty()逻辑错误
 *
 * Revision 1.11  2012/12/06 07:55:18  cchun
 * Update:修改save()
 *
 * Revision 1.10  2012/12/06 04:48:32  cchun
 * Update:添加updateEvict()
 *
 * Revision 1.9  2012/12/06 02:41:55  cchun
 * Update:添加updateProperty()
 *
 * Revision 1.8  2012/12/05 05:15:36  cchun
 * Update:修改update()，delete()中flush()时机
 *
 * Revision 1.7  2012/12/04 07:50:32  cchun
 * Update:去掉getById()中flush()
 *
 * Revision 1.6  2012/11/12 00:55:16  cchun
 * Fix Bug:修改getListByCriteria()返回值
 *
 * Revision 1.5  2012/11/05 07:51:23  cchun
 * Fix Bug:delete,update之前调用flush，避免更新时出现对象不唯一异常
 *
 * Revision 1.4  2012/11/02 06:49:32  cchun
 * Update:增加flush()
 *
 * Revision 1.3  2012/10/29 08:54:55  cchun
 * Update:增加getListByCriteria()和deleteAll()
 *
 * Revision 1.2  2012/10/22 07:26:25  cchun
 * Update:添加分页和批量处理方法
 *
 * Revision 1.1  2012/10/17 08:04:08  cchun
 * Add:数据库访问实现
 *
 */
public class BeanDaoImpl implements BeanDaoService {

	private SessionService service;
	public void setService(SessionService service) {
		this.service = service;
	}
	
	@Override
	public boolean exitService() {
		if(service==null){
			return false;
		}else{
			return true;
		}
	}

	
	private static BeanDaoImpl inst;
	
	private BeanDaoImpl() {}
	
	public static BeanDaoImpl getInstance() {
		if (inst == null)
			inst = new BeanDaoImpl();
		return inst;
	}

	@Override
	public void save(Object obj) {
		if (exists(obj))
			updateEvict(obj);
		else
			insert(obj);
	}
	
	@Override
	public void saveBatch(List<?> objs) {
		for (Object obj : objs) {
			save(obj);
		}
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see cn.org.osgi.module.hibernate.service.CommonDaoService#save(java.lang.Object)
	 */
	public void insert(Object obj) {
		Session _session = service.get();
		Transaction tx = _session.beginTransaction();
		try {
			_session.save(obj);
			tx.commit();
		} catch (HibernateException e) {
			tx.rollback();
			throw new RuntimeException(e);
		} finally {
			service.flush();
		}
	}

	@Override
	public void insertBatch(List<?> objs) {
		Session _session = service.get();
		Transaction tx = _session.beginTransaction();
		try {
			int size = objs.size();
			for (int i=0; i<size; i++) {
				Object obj = objs.get(i);
				_session.save(obj);
				if ((i>0) && (i % DBManager.HB_CACHESIZE == 0)) {
					_session.flush();
					_session.clear();
				}
			}
			tx.commit();
		} catch (HibernateException e) {
			tx.rollback();
			throw new RuntimeException(e);
		} finally {
			service.flush();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cn.org.osgi.module.hibernate.service.CommonDaoService#update(java.lang.Object)
	 */
	public void update(Object obj) {
		Session _session = service.get();
		Transaction tx = _session.beginTransaction();
		try {
			_session.update(obj);
			tx.commit();
		} catch (HibernateException e) {
			tx.rollback();
			throw new RuntimeException(e);
		} finally {
			service.flush();
		}
	}
	
	public void updateBatch(List<?> objs) {
		Session _session = service.get();
		Transaction tx = _session.beginTransaction();
		try {
			int size = objs.size();
			for (int i=0; i<size; i++) {
				Object obj = objs.get(i);
				_session.update(obj);
				if ((i>0) && (i % DBManager.HB_CACHESIZE == 0)) {
					_session.flush();
					_session.clear();
				}
			}
			tx.commit();
		} catch (HibernateException e) {
			tx.rollback();
			throw new RuntimeException(e);
		} finally {
			service.flush();
		}
	}
	
	public void updateEvict(Object obj) {
//		Object refObj = getRefresh(obj);
		Session _session = service.get();
		Transaction tx = _session.beginTransaction();
		try {
//			_session.evict(refObj);
			_session.update(obj);
			tx.commit();
		} catch (HibernateException e) {
			tx.rollback();
			throw new RuntimeException(e);
		} finally {
			service.flush();
		}
	}
	
	public void updateProperty(Object obj, String property, Object value) {
		String hql = "update " + obj.getClass().getName() +
			" set " + property + "=:" + property +
			" where id=:id";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(property, value);
		params.put("id", ObjectUtil.getProperty(obj, "id"));
		HqlDaoImpl.getInstance().updateByHql(hql, params);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cn.org.osgi.module.hibernate.service.CommonDaoService#delete(java.lang.Object)
	 */
	public void delete(Object obj) {
		Session _session = service.get();
		Transaction tx = _session.beginTransaction();
		try {
			_session.delete(obj);
			tx.commit();
		} catch (HibernateException e) {
			tx.rollback();
			throw new RuntimeException(e);
		} finally {
			service.flush();
		}
	}

	@Override
	public void deleteBatch(List<?> objs) {
		Session _session = service.get();
		Transaction tx = _session.beginTransaction();
		try {
			int size = objs.size();
			for (int i=0; i<size; i++) {
				Object obj = objs.get(i);
				_session.delete(obj);
				if ((i>0) && (i % DBManager.HB_CACHESIZE == 0)) {
					_session.flush();
					_session.clear();
				}
			}
			tx.commit();
		} catch (HibernateException e) {
			tx.rollback();
			throw new RuntimeException(e);
		} finally {
			service.flush();
		}
	}

	@Override
	public boolean exists(Object obj) {
		PersistentClass pc = service.getConfiguration().getClassMapping(
				obj.getClass().getName());
		if(pc == null){
			return false;
		}
		Property p = pc.getIdentifierProperty();
		try {
			Object property = ObjectUtil.getProperty(obj, p.getName());
			
			if (property instanceof Integer) {
				int id = (Integer) property;
				
				return exists(obj.getClass(), id);
			} else if (property instanceof String) {
				String id = (String)property;
				
				return exists(obj.getClass(), id);
			}
		} catch (NumberFormatException e) {
			SCTLogger.error("主键值不为整数：", e);
		} 
		return false;
	}

	@Override
	public boolean isSame(Object obj1, Object obj2) {
		if (obj1 == null || obj2 == null)
			return false;
		Integer id1 = (Integer) ObjectUtil.getProperty(obj1, "id");
		Integer id2 = (Integer) ObjectUtil.getProperty(obj2, "id");
		return (obj1.getClass()==obj2.getClass() && 
				id1.intValue() == id2.intValue());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cn.org.osgi.module.hibernate.service.CommonDaoService#deleteById(java.lang.Class,
	 *      int)
	 */
	public void deleteById(Class<?> po, int id) {
		Object obj = getById(po, id);
		if (obj != null) {
			delete(obj);
		} else {
			SCTLogger.warn("删除失败：" + po.getSimpleName() + 
					"中不存在id= " + id + " 的值！");
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cn.org.osgi.module.hibernate.service.CommonDaoService#deleteAll(java.lang.Class)
	 */
	public void deleteAll(Class<?> po) {
		Session _session = service.get();
		Transaction tx = _session.beginTransaction();
		try {
			_session.createQuery("delete from " + po.getName()).executeUpdate();
			tx.commit();
		} catch (HibernateException e) {
			tx.rollback();
			throw new RuntimeException(e);
		} finally {
			service.flush();
		}
	}
	
	/**
	 * 批量删除
	 * @param po
	 * @param property
	 * @param value
	 */
	@Override
	public void deleteAll(Class<?> po, String property, Object value) {
		Session _session = service.get();
		Transaction tx = _session.beginTransaction();
		try {
			Query query = _session.createQuery("delete from " + po.getName() + 
					" where " + property + "=:" + property);
			query.setParameter(property, value);
			query.executeUpdate();
			tx.commit();
		} catch (HibernateException e) {
			tx.rollback();
			throw new RuntimeException(e);
		} finally {
			service.flush();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cn.org.osgi.module.hibernate.service.CommonDaoService#getById(java.lang.Class,
	 *      int)
	 */
	public Object getById(Class<?> po, int id) {
		Session _session = service.get();
		Object obj = null;
		try {
			obj = _session.get(po, new Integer(id));
		} finally {
			service.flush();
		}
		return obj;
	}
	
	public Object getById(Class<?> po, String id) {
		Session _session = service.get();
		Object obj = null;
		try {
			obj = _session.get(po, id);
		} finally {
			service.flush();
		}
		return obj;
	}
	
	/**
	 * 刷新对象
	 * @param obj
	 */
	public Object getRefresh(Object obj) {
		return getById(obj.getClass(), (Integer) ObjectUtil.getProperty(obj, "id"));
	}

	@Override
	public boolean exists(Class<?> po, int id) {
		return getById(po, id) != null;
	}
	
	public boolean exists(Class<?> po, String id) {
		return getById(po, id) != null;
	}
	
	public Object getObject(Class<?> clazz, Map<String, Object> params) {
		List<?> list = getListByCriteria(clazz, params);
		if (list != null && list.size() > 0) {
			return list.get(0);
		} else {
			return null;
		}
	}
	
	public Object getObject(Class<?> clazz, String property, Object value) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(property, value);
		return getObject(clazz, params);
	}
	
	@Override
	public List<?> getListByCriteria(Class<?> clazz, Map<String, Object> params) {
		return getListByOrderCriteria(clazz, null, params);
	}
	
	@Override
	public List<?> getListOrderById(Class<?> clazz, Map<String, Object> params) {
		return getListByOrderCriteria(clazz, "id", params);
	}

	public List<?> getListLike(Class<?> clazz, String property, String param) {
		Session _session = service.get();
		Transaction tx = _session.beginTransaction();
		try {
			Query query = _session.createQuery("from " + clazz.getName() + 
					" where " + property + " like '%" + param+ "%'");
		//	query.setParameter(property, value);
			List<?> result=query.list();
			tx.commit();
			return result;
		} catch (HibernateException e) {
			tx.rollback();
			throw new RuntimeException(e);
		} finally {
			service.flush();
		}
	}
	
	/**
	 * 获取排序的表单数据,返回全部数据.
	 * 
	 * @param clazz 表单对应的class
	 * @param property 遵循排序的属性.
	 * @param params 
	 * @return
	 */
	public List<?> getListByOrderCriteria(Class<?> clazz, String property, Map<String, Object> params) {
		Session _session = service.get();
//		service.flush();
		try {
			Criteria criteria = _session.createCriteria(clazz);
			if (property != null) {
				criteria = criteria.addOrder(Order.asc(property));
			}
			if (params != null) {
				Iterator<String> it = params.keySet().iterator();
				String name = null;
				for (; it.hasNext(); criteria.add(Restrictions.eq(name, params.get(name))))
					name = (String) it.next();
			}
			return criteria.list();
		} finally {
			service.flush();
		}
	}
	/**
	 * 获取排序的表单数据,返回全部数据(大于参数值).
	 * 
	 * @param clazz 表单对应的class
	 * @param property 遵循排序的属性.
	 * @param params 
	 * @return
	 */
	public List<?> getListByOrderCriteriaForGt(Class<?> clazz, String property, Map<String, Object> params) {
		Session _session = service.get();
//		service.flush();
		try {
			Criteria criteria = _session.createCriteria(clazz);
			if (property != null) {
				criteria = criteria.addOrder(Order.asc(property));
			}
			if (params != null) {
				Iterator<String> it = params.keySet().iterator();
				String name = null;
				for (; it.hasNext(); criteria.add(Restrictions.gt(name, params.get(name))))
					name = (String) it.next();
			}
			return criteria.list();
		} finally {
			service.flush();
		}
	}
	
	
	public int getMaxInteger(Class<?> clazz, String property, int defaultVal) {
		Session _session = service.get();
//		service.flush();
		
		try {
			Criteria criteria = _session.createCriteria(clazz);
			if (property != null) {
				criteria.setProjection(Projections.projectionList().add(Projections.max(property)));
			}
			Object result = criteria.uniqueResult();
			if (result instanceof Integer) {
				return (Integer) result;
			}
			return defaultVal;
		} finally {
			service.flush();
		}
	}

	@Override
	public List<?> getListOrderById(Class<?> clazz, String property, Object value) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(property, value);
		return getListByOrderCriteria(clazz, "id", params);
	}
	
	@Override
	public List<?> getListByCriteria(Class<?> clazz, String property, Object value) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(property, value);
		return getListByCriteria(clazz, params);
	}
	
	public List<?> getListByCriteriaAndPage(Class<?> clazz, Map<String, Object> params, int currentPage,
			int pageSize) {
		Session _session = service.get();
//		service.flush();
		try {
			Criteria criteria = _session.createCriteria(clazz);
			if (params != null) {
				Iterator<String> it = params.keySet().iterator();
				String name = null;
				for (; it.hasNext(); criteria.add(Restrictions.eq(name, params.get(name))))
					name = (String) it.next();
			}
			if (currentPage < 1)
				currentPage = 1;
			criteria.setFirstResult((currentPage - 1) * pageSize);
			criteria.setMaxResults(pageSize);
			criteria.setCacheable(true);
			return criteria.list();
		} finally {
			service.flush();
		}
	}

	@Override
	public void saveOrUpdateBatch(List<?> list) {
		for (Object object : list) {
			if(exists(object)){
				update(object);
			}else {
				save(object);
			}
		}
	}

	@Override
	public void saveOrUpdate(Object obj) {
		if(exists(obj)){
			update(obj);
		}else {
			save(obj);
		}
		
	}

	@Override
	public List<?> getAll(Class<?> clazz) {
		Session _session = service.get();
		Transaction tx = _session.beginTransaction();
		try {
			
			Query query = _session.createQuery("from " + clazz.getName());
			List<?> result=query.list();
			tx.commit();
			return result;
		} catch (HibernateException e) {
			tx.rollback();
			throw new RuntimeException(e);
		} finally {
			service.flush();
		}
	}

}
