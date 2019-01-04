package com.synet.tool.rsc.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.synet.tool.rsc.io.parser.ParserUtil;
import com.synet.tool.rsc.model.Tb1046IedEntity;
import com.synet.tool.rsc.model.Tb1062PinEntity;
import com.synet.tool.rsc.model.Tb1064StrapEntity;
import com.synet.tool.rsc.util.F1011_NO;
import com.synet.tool.rsc.util.Rule;

public class PinEntityService extends BaseService {
	
	@SuppressWarnings("unchecked")
	public List<Tb1062PinEntity> getByIed(Tb1046IedEntity ied) {
		return (List<Tb1062PinEntity>) beanDao.getListByCriteria(Tb1062PinEntity.class, "tb1046IedByF1046Code", ied);
	}

	public Tb1062PinEntity getPinEntity(String devName, String f1062RefAddr) {
		Tb1046IedEntity ied = (Tb1046IedEntity) beanDao.getObject(Tb1046IedEntity.class, "f1046Name", devName);
		if (ied != null) {
			return getPin(ied, f1062RefAddr);
		}
		return null;
	}
	
	/**
	 * 根根据关联的保护压板查询
	 * @param straps
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Tb1062PinEntity> getByStraps(List<Tb1064StrapEntity> straps) {
		return (List<Tb1062PinEntity>) hqlDao.selectInObjects(Tb1062PinEntity.class, "tb1064StrapByF1064Code", straps);
	}
	
	/**
	 * 查询Pin
	 * @param ied
	 * @param ref
	 * @return
	 */
	public Tb1062PinEntity getPin(Tb1046IedEntity ied, String ref) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("tb1046IedByF1046Code", ied);
		params.put("f1062RefAddr", ref);
		return (Tb1062PinEntity) beanDao.getObject(Tb1062PinEntity.class, params);
	}
	
	/**
	 * 添加Pin
	 * @param ied
	 * @param ref
	 * @param desc
	 */
	public void addPin(Tb1046IedEntity ied, String ref, String desc) {
		Rule type = F1011_NO.OTHERS;
		Tb1062PinEntity pin = getPin(ied, ref);
		if (pin == null) {
			ParserUtil.createPin(ied, ref , desc, type.getId(), 0);
			beanDao.insert(pin);
		} else {
			beanDao.markRecovered(pin);
		}
	}
	
	/**
	 * 软删除Pin
	 * @param ied
	 * @param ref
	 */
	public void deletePin(Tb1046IedEntity ied, String ref) {
		Tb1062PinEntity pin = getPin(ied, ref);
		if (pin != null) {
			beanDao.markDeleted(pin);
		}
	}
	
	/**
	 * 更新Pin
	 * @param ied
	 * @param ref
	 * @param desc
	 */
	public void updatePin(Tb1046IedEntity ied, String ref, String desc) {
		Tb1062PinEntity pin = getPin(ied, ref);
		if (pin != null) {
			pin.setF1062Desc(desc);
			beanDao.update(pin);
		}
	}
}
