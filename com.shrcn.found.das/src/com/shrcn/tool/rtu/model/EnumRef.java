/**
 * Copyright (c) 2007-2010 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based on IEC61850 SCT.
 */
package com.shrcn.tool.rtu.model;


/**
 * 
 * @author 孙春颖
 * @version 1.0, 2013 4 1
 */
/**
 * $Log: EnumRef.java,v $
 * Revision 1.1  2013/04/03 03:13:04  scy
 * ADD：增加com.shrcn.tool.found.enums包，创建EnumRef枚举类
 *
 */
public enum EnumRef {
	
	CALC_AI(TaiPointMap.class, 1), 
	CALC_CI(TciPointMap.class, 2), 
	CALC_DI(TdiPointMap.class, 3), 
	CALC_AO(TaoPointMap.class, 4), 
	CALC_DO(TdoPointMap.class, 5),
	ADIO_AI(TAipoint.class, 6), 
	ADIO_CI(TCipoint.class, 7), 
	ADIO_DI(TDipoint.class, 8), 
	ADIO_AO(TAopoint.class, 9), 
	ADIO_DO(TDopoint.class, 10),
	ADIO_SG(TSgpoint.class, 11),
	ADIO_DMD(TDmdpoint.class, 12),
	ADIO_PQ(TPqpoint.class, 13),
	ADIO_FJ(TFjpoint.class, 14),
	INTER_LOCK(TInterLock.class, 15),
	ADIO_FJSP(TFjsppoint.class, 16);
	
	private Class<?> modelClass;
	private int type;
	
	EnumRef(Class<?> modelClass) {
		this.modelClass = modelClass;
	}
	
	EnumRef(Class<?> modelClass, int type) {
		this.modelClass = modelClass;
		this.type = type;
	}

	public Class<?> getModelClass() {
		return modelClass;
	}
	
	public int getType() {
		return type;
	}
	
	public static int getTypeByClass(Class<?> modelClass) {
		if (null != modelClass)
			for (EnumRef var : EnumRef.values()) {
				if (modelClass.equals(var.getModelClass()))
					return var.getType();
			}
		return -1;
	}

	public static Class<?> getClassByType(int type) {
		for (EnumRef var : EnumRef.values()) {
			if (type == var.getType())
				return var.getModelClass();
		}
		return null;
	}
	
	public static String getCalcTypes() {
		return "(" + CALC_AI.getType() +
				"," + CALC_DI.getType() +
				"," + CALC_CI.getType() +
				"," + CALC_AO.getType() +
				"," + CALC_DO.getType() +
				")";
	}
	
	public static String getCategory(int reftype) {
		if (reftype < 6)
			return "计算器变量";
		else if (reftype > 5 && reftype < 15 || reftype > 15)
			return "转发表";
		else if (reftype == 15)
			return "五防";
		return "未知";
	}
}
