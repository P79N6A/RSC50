package com.synet.tool.rsc.incr;

import com.synet.tool.rsc.incr.scd.ExtRefConflictHandler;
import com.synet.tool.rsc.incr.scd.FCDAConflictHandler;
import com.synet.tool.rsc.incr.scd.GooseConflictHandler;
import com.synet.tool.rsc.incr.scd.GoosesConflictHandler;
import com.synet.tool.rsc.incr.scd.IEDConflictHandler;
import com.synet.tool.rsc.incr.scd.InputsConflictHandler;
import com.synet.tool.rsc.incr.scd.ParamConflictHandler;
import com.synet.tool.rsc.incr.scd.ParamsConflictHandler;
import com.synet.tool.rsc.incr.scd.PinConflictHandler;
import com.synet.tool.rsc.incr.scd.PinsConflictHandler;
import com.synet.tool.rsc.incr.scd.RcbConflictHandler;
import com.synet.tool.rsc.incr.scd.RcbsConflictHandler;
import com.synet.tool.rsc.incr.scd.SetConflictHandler;
import com.synet.tool.rsc.incr.scd.SetsConflictHandler;
import com.synet.tool.rsc.incr.scd.SmvConflictHandler;
import com.synet.tool.rsc.incr.scd.SmvsConflictHandler;
import com.synet.tool.rsc.incr.ssd.BayConflictHandler;
import com.synet.tool.rsc.incr.ssd.EqpConflictHandler;
import com.synet.tool.rsc.incr.ssd.StaConflictHandler;
import com.synet.tool.rsc.incr.ssd.VolConflictHandler;

public enum EnumConflict {

	// 二次
	IED("IED", IEDConflictHandler.class),
	Pins("输入虚端子", PinsConflictHandler.class),
	Sets("定值", SetsConflictHandler.class),
	Params("参数", ParamsConflictHandler.class),
	Rcbs("报告控制块", RcbsConflictHandler.class),
	Gooses("Goose控制块", GoosesConflictHandler.class),
	Smvs("Smv控制块", SmvsConflictHandler.class),
	Inputs("虚回路", InputsConflictHandler.class),
	// 子项
	Pin("Pin", PinConflictHandler.class),
	FCDA("FCDA", FCDAConflictHandler.class),
	ExtRef("ExtRef", ExtRefConflictHandler.class),
	Set("SGCB", SetConflictHandler.class),
	Param("DataSet", ParamConflictHandler.class),
	Rcb("Rcb", RcbConflictHandler.class),
	Goose("Goose", GooseConflictHandler.class),
	Smv("Smv", SmvConflictHandler.class),
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
