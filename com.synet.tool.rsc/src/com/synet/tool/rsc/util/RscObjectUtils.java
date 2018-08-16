package com.synet.tool.rsc.util;

import com.synet.tool.rsc.model.Tb1046IedEntity;
import com.synet.tool.rsc.model.Tb1067CtvtsecondaryEntity;
import com.synet.tool.rsc.model.Tb1090LineprotfiberEntity;
import com.synet.tool.rsc.model.Tb1091IotermEntity;
import com.synet.tool.rsc.model.Tb1092PowerkkEntity;
import com.synet.tool.rsc.model.Tb1093VoltagekkEntity;

public class RscObjectUtils {
	
	public static Tb1090LineprotfiberEntity createTb1090() {
		Tb1046IedEntity ied = new Tb1046IedEntity();
		Tb1090LineprotfiberEntity entity = new Tb1090LineprotfiberEntity();
		entity.setTb1046IedByF1046Code(ied);
		return entity;
	}
	
	public static Tb1091IotermEntity createTb1091() {
		Tb1046IedEntity ied = new Tb1046IedEntity();
		Tb1091IotermEntity entity = new Tb1091IotermEntity();
		entity.setTb1046IedByF1046Code(ied);
		return entity;
	}
	
	public static Tb1092PowerkkEntity createTb1092() {
		Tb1046IedEntity ied = new Tb1046IedEntity();
		Tb1092PowerkkEntity entity = new Tb1092PowerkkEntity();
		entity.setTb1046IedByF1046Code(ied);
		return entity;
	}
	
	public static Tb1093VoltagekkEntity createTb1093() {
		Tb1067CtvtsecondaryEntity tb1067 = new Tb1067CtvtsecondaryEntity();
		Tb1093VoltagekkEntity entity = new Tb1093VoltagekkEntity();
		entity.setTb1067CtvtsecondaryByF1067Code(tb1067);
		return entity;
	}

}
