package com.synet.tool.rsc.incr;

import com.synet.tool.rsc.incr.scd.GooseConflictHandler;
import com.synet.tool.rsc.incr.scd.IEDConflictHandler;
import com.synet.tool.rsc.incr.scd.InputConflictHandler;
import com.synet.tool.rsc.incr.scd.ParamConflictHandler;
import com.synet.tool.rsc.incr.scd.PinConflictHandler;
import com.synet.tool.rsc.incr.scd.RcbConflictHandler;
import com.synet.tool.rsc.incr.scd.SetConflictHandler;
import com.synet.tool.rsc.incr.scd.SmvConflictHandler;
import com.synet.tool.rsc.incr.ssd.BayConflictHandler;
import com.synet.tool.rsc.incr.ssd.EqpConflictHandler;
import com.synet.tool.rsc.incr.ssd.StaConflictHandler;
import com.synet.tool.rsc.incr.ssd.VolConflictHandler;

public enum EnumConflict {

	// 二次
	IED("IED", IEDConflictHandler.class),
	Pins("输入虚端子", PinConflictHandler.class),
	Sets("定值", SetConflictHandler.class),
	Params("参数", ParamConflictHandler.class),
	Rcbs("报告控制块", RcbConflictHandler.class),
	Gooses("Goose控制块", GooseConflictHandler.class),
	Smvs("Smv控制块", SmvConflictHandler.class),
	Inputs("虚回路", InputConflictHandler.class),
	// 一次
	Sta("Substation", StaConflictHandler.class),
	Vol("VoltageLevel", VolConflictHandler.class),
	Bay("Bay", BayConflictHandler.class),
	EQP("ConductingEquipment", EqpConflictHandler.class),
	PTR("PowerTransformer", EqpConflictHandler.class);
	
	private String type;
	private Class<?> clazz;
	
	EnumConflict(String type, Class<?> clazz) {
		this.type = type;
		this.clazz = clazz;
	}

	public String getType() {
		return type;
	}

	public Class<?> getClazz() {
		return clazz;
	}
	
}
