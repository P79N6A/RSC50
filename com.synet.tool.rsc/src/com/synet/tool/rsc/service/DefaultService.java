package com.synet.tool.rsc.service;

import com.synet.tool.rsc.model.Tb1090LineprotfiberEntity;
import com.synet.tool.rsc.model.Tb1091IotermEntity;
import com.synet.tool.rsc.model.Tb1092PowerkkEntity;
import com.synet.tool.rsc.model.Tb1093VoltagekkEntity;

public class DefaultService extends BaseService {
	
	/**
	 * 检查并保存对象
	 * @param obj
	 * @return 失败：-1，成功：0，不合法：1
	 */
	public int saveTableData(Object obj) {
		if (obj == null) return -1;
		boolean b = false;
		try {
			if (obj instanceof Tb1090LineprotfiberEntity) {
				Tb1090LineprotfiberEntity entity = (Tb1090LineprotfiberEntity) obj;
				if (entity.getTb1046IedByF1046Code() != null 
						&& entity.getTb1046IedByF1046Code().getF1046Code() != null) {
					b = true;
				}
			}
			if (obj instanceof Tb1091IotermEntity){
				Tb1091IotermEntity entity = (Tb1091IotermEntity) obj;
				if (entity.getTb1046IedByF1046Code() != null 
						&& entity.getTb1046IedByF1046Code().getF1046Code() != null) {
					b = true;
				}
			}
			if (obj instanceof Tb1092PowerkkEntity){
				Tb1092PowerkkEntity entity = (Tb1092PowerkkEntity) obj;
				if (entity.getTb1046IedByF1046Code() != null 
						&& entity.getTb1046IedByF1046Code().getF1046Code() != null) {
					b = true;
				}
			}
			if (obj instanceof Tb1093VoltagekkEntity){
				Tb1093VoltagekkEntity entity = (Tb1093VoltagekkEntity) obj;
				if (entity.getTb1067CtvtsecondaryByF1067Code() != null 
						&& entity.getTb1067CtvtsecondaryByF1067Code().getF1067Code() != null) {
					b = true;
				}
			}
			if (b){
				super.save(obj);
			} else {
				return 0;
			}
		} catch (Exception e) {
			return -1;
		}
		return 1;
	}

}
