package com.synet.tool.rsc.service;

public enum EnumIedType {
	
	//保护设备
	PROTECT_DEVICE(new int[]{0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19}, "通用保护装置"),
	//智能终端
	TERMINAL_DEVICE(new int[]{20,21,22,23,24,25,26,27,28,29}, "通用智能终端"),
	//合并单元，
	UNIT_DEVICE(new int[]{30,31,32,33,34,35,36,37,38,39}, "通用合并单元"),
	//合并智能终端
	TER_UNI_DEVICE(new int[]{40,41,42,43,44,45,46,47,48,49}, "通用合并智能终端"),
	//测控装置
	RTU_DEVICE(new int[]{50}, "测控装置"),
	//交换机
	SWC_DEVICE(new int[]{60}, "交换机"),
	//光纤配线架
	GAT_DEVICE(new int[]{61}, "光配单元"),
	//采集器
	ODF_DEVICE(new int[]{62}, "采集器");
	
	int[] types;
	String desc;
	
	private  EnumIedType(int[] types, String desc) {
		this.types = types;
		this.desc = desc;
	}

	public int[] getTypes() {
		return types;
	}
	
	public boolean include(int typ) {
		for (int temp : types) {
			if (temp == typ) {
				return true;
			}
		}
		return false;
	}
	
	public static boolean isBayIED(int typ) {
		return PROTECT_DEVICE.include(typ) || RTU_DEVICE.include(typ);
	}
	
	public static boolean isSubIED(int typ) {
		return TERMINAL_DEVICE.include(typ) || UNIT_DEVICE.include(typ)
				 || TER_UNI_DEVICE.include(typ);
	}
	
	public static EnumIedType getTypeByDesc(String desc) {
		for (EnumIedType type : EnumIedType.values()) {
			if (type.desc.equals(desc)) {
				return type;
			}
		}
		return null;
	}
}
