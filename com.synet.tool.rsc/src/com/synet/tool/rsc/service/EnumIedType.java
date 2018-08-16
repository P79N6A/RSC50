package com.synet.tool.rsc.service;

public enum EnumIedType {
	
	//保护设备
	PROTECT_DEVICE(new int[]{0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19}),
	//智能终端
	TERMINAL_DEVICE(new int[]{20,21,22,23,24,25,26,27,28,29}),
	//合并单元，
	UNIT_DEVICE(new int[]{30,31,32,33,34,35,36,37,38,39}),
	//合并智能终端
	TER_UNI_DEVICE(new int[]{40,41,42,43,44,45,46,47,48,49}),
	//测控装置
	RTU_DEVICE(new int[]{50});
	
	int[] types;
	private  EnumIedType(int[] types) {
		this.types = types;
	}

	public int[] getTypes() {
		return types;
	}
	
}
