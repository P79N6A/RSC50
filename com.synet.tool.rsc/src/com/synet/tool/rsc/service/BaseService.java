package com.synet.tool.rsc.service;

import java.util.List;

import com.shrcn.tool.found.das.impl.BeanDaoImpl;
import com.shrcn.tool.found.das.impl.HqlDaoImpl;
import com.synet.tool.rsc.model.Tb1046IedEntity;

public abstract class BaseService {
	
	protected BeanDaoImpl beanDao = BeanDaoImpl.getInstance();
	protected HqlDaoImpl hqlDao = HqlDaoImpl.getInstance();
	
	@SuppressWarnings("unchecked")
	public List<Tb1046IedEntity> getIedList(){
		return (List<Tb1046IedEntity>) beanDao.getAll(Tb1046IedEntity.class);
	}
	
	public void add(Object entity) {
		beanDao.save(entity);
	}
	
	public void delete(Object entity) {
		beanDao.delete(entity);
	}
	
	public void update(Object entity) {
		beanDao.update(entity);
	}
}
