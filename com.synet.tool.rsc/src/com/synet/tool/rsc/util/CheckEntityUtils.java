package com.synet.tool.rsc.util;

import com.synet.tool.rsc.model.Tb1049RegionEntity;
import com.synet.tool.rsc.model.Tb1050CubicleEntity;
import com.synet.tool.rsc.model.Tb1051CableEntity;
import com.synet.tool.rsc.model.Tb1053PhysconnEntity;
import com.synet.tool.rsc.model.Tb1090LineprotfiberEntity;
import com.synet.tool.rsc.model.Tb1091IotermEntity;
import com.synet.tool.rsc.model.Tb1092PowerkkEntity;
import com.synet.tool.rsc.model.Tb1093VoltagekkEntity;

public class CheckEntityUtils {
	
	/**
	 * 检查数据是否符合数据库not null的要求
	 * @param obj
	 * @return
	 */
	public static boolean check(Object obj) {
		if (obj instanceof Tb1049RegionEntity) {
			return checkRegion((Tb1049RegionEntity) obj);
		}
		if (obj instanceof Tb1050CubicleEntity) {
			return checkCubicle((Tb1050CubicleEntity) obj);
		}
		if (obj instanceof Tb1051CableEntity) {
			return checkCable((Tb1051CableEntity) obj);
		}
		if (obj instanceof Tb1053PhysconnEntity) {
			return checkPhysconn((Tb1053PhysconnEntity) obj);
		}
		if (obj instanceof Tb1090LineprotfiberEntity) {
			return CheckEntityUtils.checkLineprotfiber((Tb1090LineprotfiberEntity) obj);
		}
		if (obj instanceof Tb1091IotermEntity){
			return CheckEntityUtils.checkIoterm((Tb1091IotermEntity) obj);
		}
		if (obj instanceof Tb1092PowerkkEntity){
			return CheckEntityUtils.checkPowerkk((Tb1092PowerkkEntity) obj);
		}
		if (obj instanceof Tb1093VoltagekkEntity){
			return CheckEntityUtils.checkVoltagekk((Tb1093VoltagekkEntity) obj);
		}
		return true;
	}
	
	public static boolean checkRegion(Tb1049RegionEntity entity) {
		if (entity.getF1049Code() != null && entity.getF1049Name() != null) {
			if (entity.getTb1041SubstationByF1041Code() != null && entity.getTb1041SubstationByF1041Code().getF1041Code() != null) {
				return true;
			}
		}
		return false;
	}
	
	public static boolean checkCubicle(Tb1050CubicleEntity entity) {
		if (entity.getF1050Code() != null && entity.getF1050Name() != null) {
			if (entity.getTb1049RegionByF1049Code() != null && entity.getTb1049RegionByF1049Code().getF1049Code() != null) {
				return true;
			}
		}
		return false;
	}
	
	public static boolean checkCable(Tb1051CableEntity entity) {
		if (entity.getF1051Code() != null && entity.getF1051Name() != null) {
			if (entity.getTb1041SubstationByF1041Code() != null && entity.getTb1041SubstationByF1041Code().getF1041Code() != null) {
				if (entity.getTb1050CubicleByF1050CodeA() != null && entity.getTb1050CubicleByF1050CodeA().getF1050Code() != null) {
					if (entity.getTb1050CubicleByF1050CodeB() != null && entity.getTb1050CubicleByF1050CodeB().getF1050Code() != null) {
						return true;
					}
				}
			}
		}
		return false;
	}
	
	public static boolean checkPhysconn(Tb1053PhysconnEntity entity) {
		if (entity.getF1053Code() != null) {
			if (entity.getTb1048PortByF1048CodeA() != null && entity.getTb1048PortByF1048CodeA().getF1048Code() != null) {
				if (entity.getTb1048PortByF1048CodeB() != null && entity.getTb1048PortByF1048CodeB().getF1048Code() != null) {
					return true;
				}
			}
		}
		return false;
	}

	public static boolean checkLineprotfiber(Tb1090LineprotfiberEntity entity) {
		if (entity.getF1090Code() != null && entity.getF1090Desc() != null) {
			if (entity.getTb1046IedByF1046Code() != null && entity.getTb1046IedByF1046Code().getF1046Code() != null) {
				return true;
			}
		}
		return false;
	}
	
	public static boolean checkIoterm(Tb1091IotermEntity entity) {
		if (entity.getF1091Code() != null && entity.getF1091Desc() != null) {
			if (entity.getTb1046IedByF1046Code() != null 
					&& entity.getTb1046IedByF1046Code().getF1046Code() != null) {
				return true;
			}
		}
		return false;
	}
	
	public static boolean checkPowerkk(Tb1092PowerkkEntity entity) {
		if (entity.getF1092Code() != null && entity.getF1092Desc() != null) {
			if (entity.getTb1046IedByF1046Code() != null 
					&& entity.getTb1046IedByF1046Code().getF1046Code() != null) {
				return true;
			}
		}
		return false;
	}
	
	public static boolean checkVoltagekk(Tb1093VoltagekkEntity entity) {
		if (entity.getF1093Code() != null && entity.getF1093Desc() != null) {
			if (entity.getTb1067CtvtsecondaryByF1067Code() != null 
					&& entity.getTb1067CtvtsecondaryByF1067Code().getF1067Code() != null) {
				return true;
			}
		}
		return false;
	}
	
}
