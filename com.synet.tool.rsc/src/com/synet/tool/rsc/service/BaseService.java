package com.synet.tool.rsc.service;

import java.util.List;

import com.shrcn.tool.found.das.impl.BeanDaoImpl;
import com.shrcn.tool.found.das.impl.HqlDaoImpl;
import com.synet.tool.rsc.model.Tb1046IedEntity;
import com.synet.tool.rsc.model.Tb1067CtvtsecondaryEntity;
import com.synet.tool.rsc.util.CheckEntityUtils;

public abstract class BaseService {
	
	protected BeanDaoImpl beanDao = BeanDaoImpl.getInstance();
	protected HqlDaoImpl hqlDao = HqlDaoImpl.getInstance();
	
	@SuppressWarnings("unchecked")
	public List<Tb1046IedEntity> getIedList(){
		return (List<Tb1046IedEntity>) beanDao.getAll(Tb1046IedEntity.class);
	}
	
	@SuppressWarnings("unchecked")
	public List<Tb1067CtvtsecondaryEntity> getCtvtList() {
		return (List<Tb1067CtvtsecondaryEntity>) beanDao.getAll(Tb1067CtvtsecondaryEntity.class);
	}
	
	public void save(Object entity) {
		beanDao.save(entity);
	}
	
	public void insert(Object obj) {
		beanDao.insert(obj);
	}
	
	public void delete(Object entity) {
		beanDao.delete(entity);
	}
	
	public void update(Object entity) {
		beanDao.update(entity);
	}
	
	public Object getById(Class<?> po, int id) {
		return beanDao.getById(po, id);
	}
	
	public Object getById(Class<?> po, String id) {
		return beanDao.getById(po, id);
	}
	
	public List<?> getAll(Class<?> clazz) {
		return beanDao.getAll(clazz);
	}
	
	/**
	 * 检查并保存Table数据
	 * @param obj
	 * @return 失败：-1，成功：0，不合法：1
	 */
	public int saveTableData(Object obj) {
		if (obj == null) return -1;
		try {
			if (CheckEntityUtils.check(obj)){
				save(obj);
			} else {
				return 0;
			}
		} catch (Exception e) {
			return -1;
		}
		return 1;
	}
}
