package com.synet.tool.rsc.service;

import java.util.ArrayList;
import java.util.List;

import com.synet.tool.rsc.model.Tb1006AnalogdataEntity;
import com.synet.tool.rsc.model.Tb1046IedEntity;
import com.synet.tool.rsc.util.F1011_NO;

public class AnalogdataService extends BaseService {
	
//	/**
//	 * 根据codes查找
//	 * @param codes
//	 * @return
//	 */
//	@SuppressWarnings("unchecked")
//	public List<Tb1006AnalogdataEntity> getAnologByCodes(List<String> codes) {
//		if(!DataUtils.listNotNull(codes)) {
//			return new ArrayList<>();
//		}
//		StringBuilder sbObjs = new StringBuilder();
//		Map<String, Object> params = new HashMap<String, Object>();
//		int ldSize = codes.size();
//		for (int i = 1; i <=ldSize; i++) {
//			Object obj = codes.get(i-1);
//			String key = "ld" + i;
//			params.put(key, obj);
//			sbObjs.append(":" + key);
//			if (i < ldSize)
//				sbObjs.append(", ");
//		}
//		String hql = "from " + Tb1006AnalogdataEntity.class.getName() + " where f1006Code in (" + sbObjs + ") and f1011No=" + F1011_NO.PROT_MEAR.getId();
//		return (List<Tb1006AnalogdataEntity>) hqlDao.getListByHql(hql, params);
//	}
	
	/**
	 * 根据codes查找
	 * @param codes
	 * @return
	 */
	public Tb1006AnalogdataEntity getAnologByCodes(String code) {
		return (Tb1006AnalogdataEntity) beanDao.getObject(Tb1006AnalogdataEntity.class, "f1006Code", code);
	}
	
	public List<Tb1006AnalogdataEntity> getMeasDataByIed(Tb1046IedEntity iedEntity) {
		List<Tb1006AnalogdataEntity> result = new ArrayList<>();
		List<Tb1006AnalogdataEntity> temp = getAnologByIed(iedEntity);
		for (Tb1006AnalogdataEntity tb1006AnalogdataEntity : temp) {
			int f1011No = tb1006AnalogdataEntity.getF1011No();
			if (f1011No > F1011_NO.IED_WRN_COMMON.getId() && f1011No < F1011_NO.IED_TEMP.getId()) {
				result.add(tb1006AnalogdataEntity);
			}
		}
		return result;
	}
	
	public List<Tb1006AnalogdataEntity> getAnologByIed(Tb1046IedEntity iedEntity) {
		return (List<Tb1006AnalogdataEntity>) beanDao.getListByCriteria(Tb1006AnalogdataEntity.class, "tb1046IedByF1046Code", iedEntity);
	}
	
	/**
	 * 根据IED获取
	 * @param iedEntity
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<String> getAnologNameByIed(Tb1046IedEntity iedEntity) {
		List<String> result = new ArrayList<>();
		List<Tb1006AnalogdataEntity> temp = getAnologByIed(iedEntity);
		for (Tb1006AnalogdataEntity tb1006AnalogdataEntity : temp) {
			result.add(tb1006AnalogdataEntity.getF1006Byname());
		}
		return result;
	}
	

}
