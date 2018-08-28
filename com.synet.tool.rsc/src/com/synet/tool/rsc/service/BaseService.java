package com.synet.tool.rsc.service;

import java.util.List;

import com.shrcn.tool.found.das.impl.BeanDaoImpl;
import com.shrcn.tool.found.das.impl.HqlDaoImpl;
import com.synet.tool.rsc.model.Tb1043EquipmentEntity;
import com.synet.tool.rsc.model.Tb1067CtvtsecondaryEntity;
import com.synet.tool.rsc.RSCProperties;
import com.synet.tool.rsc.model.Tb1046IedEntity;

public abstract class BaseService {
	
	protected BeanDaoImpl beanDao = BeanDaoImpl.getInstance();
	protected HqlDaoImpl hqlDao = HqlDaoImpl.getInstance();
	protected RSCProperties rscp = RSCProperties.getInstance();
	
	@SuppressWarnings("unchecked")
	public List<Tb1046IedEntity> getIedList(){
		return (List<Tb1046IedEntity>) beanDao.getAll(Tb1046IedEntity.class);
	}
	
	@SuppressWarnings("unchecked")
	public List<Tb1043EquipmentEntity> getEquipmentList() {
		return (List<Tb1043EquipmentEntity>) beanDao.getAll(Tb1043EquipmentEntity.class);
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
	
	public Object getById(Class<?> po, int id) {
		return beanDao.getById(po, id);
	}
	
	public Object getById(Class<?> po, String id) {
		return beanDao.getById(po, id);
	}
}
