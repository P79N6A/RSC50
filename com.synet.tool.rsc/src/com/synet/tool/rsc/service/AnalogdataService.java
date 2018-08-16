package com.synet.tool.rsc.service;

import java.util.ArrayList;
import java.util.List;

import com.synet.tool.rsc.model.Tb1006AnalogdataEntity;
import com.synet.tool.rsc.util.DataUtils;

public class AnalogdataService extends BaseService {
	
	/**
	 * 根据code查找
	 * @param codes
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Tb1006AnalogdataEntity> getAnologByCodes(List<String> codes) {
		if(!DataUtils.notNull(codes)) {
			return new ArrayList<>();
		}
		return (List<Tb1006AnalogdataEntity>) hqlDao.selectInObjects(Tb1006AnalogdataEntity.class, "f1006Code", codes);
	}

}
