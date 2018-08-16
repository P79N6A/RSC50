package com.synet.tool.rsc.service;

import java.util.List;

import com.shrcn.tool.found.das.impl.BeanDaoImpl;
import com.shrcn.tool.found.das.impl.HqlDaoImpl;
import com.synet.tool.rsc.model.Tb1046IedEntity;
import com.synet.tool.rsc.model.Tb1067CtvtsecondaryEntity;

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
	
	public void delete(Object entity) {
		beanDao.delete(entity);
	}
	
	public void update(Object entity) {
		beanDao.update(entity);
	}
}
