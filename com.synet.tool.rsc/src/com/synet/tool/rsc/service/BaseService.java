package com.synet.tool.rsc.service;

import java.util.List;

import com.shrcn.tool.found.das.impl.BeanDaoImpl;
import com.shrcn.tool.found.das.impl.HqlDaoImpl;
import com.synet.tool.rsc.io.parser.ParserUtil;
import com.synet.tool.rsc.model.Tb1006AnalogdataEntity;
import com.synet.tool.rsc.model.Tb1016StatedataEntity;
import com.synet.tool.rsc.model.Tb1043EquipmentEntity;
import com.synet.tool.rsc.model.Tb1067CtvtsecondaryEntity;
import com.synet.tool.rsc.RSCProperties;
import com.synet.tool.rsc.model.Tb1046IedEntity;
import com.synet.tool.rsc.util.CheckEntityUtils;

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
	
	public void insert(Object entity) {
		beanDao.insert(entity);
	}
	
	public void insertBatch(List<?> objs) {
		beanDao.insertBatch(objs);
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
	
	public Object getListByCriteria(Class<?> clazz, String property, Object value){
		return beanDao.getListByCriteria(clazz, property, value);
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
		try {
			if (CheckEntityUtils.check(obj)){
				beanDao.save(obj);
			} else {
				return 0;
			}
		} catch (Exception e) {
			return -1;
		}
		return 1;
	}
	
	/**
	 * 添加状态量数据
	 * @param fcdaEl
	 * @param fcdaDesc
	 * @param f1011No
	 * @return
	 */
	public Tb1016StatedataEntity addStatedata(Tb1046IedEntity ied, String ref, String fcdaDesc, int f1011No) {
		Tb1016StatedataEntity statedata = ParserUtil.createStatedata(fcdaDesc, ref,
				ied.getF1046Code(), ied, f1011No);
		beanDao.insert(statedata);
		return statedata;
	}
	
	/**
	 * 添加模拟量数据
	 * @param fcdaEl
	 * @param fcdaDesc
	 * @param f1011No
	 * @return
	 */
	public Tb1006AnalogdataEntity addAlgdata(Tb1046IedEntity ied, String ref, String fcdaDesc, int f1011No) {
		Tb1006AnalogdataEntity algdata = ParserUtil.createAlgdata(ref, fcdaDesc, ied, f1011No);
		beanDao.insert(algdata);
		return algdata;
	}
}
