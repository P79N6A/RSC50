package com.synet.tool.rsc.incr;

import com.synet.tool.rsc.incr.scd.BaseCbConflictHandler;
import com.synet.tool.rsc.incr.scd.ExtRefConflictHandler;
import com.synet.tool.rsc.incr.scd.FCDAConflictHandler;
import com.synet.tool.rsc.incr.scd.IEDConflictHandler;
import com.synet.tool.rsc.incr.scd.PinConflictHandler;
import com.synet.tool.rsc.incr.scd.RcbConflictHandler;
import com.synet.tool.rsc.incr.scd.SetConflictHandler;
import com.synet.tool.rsc.incr.ssd.BayConflictHandler;
import com.synet.tool.rsc.incr.ssd.CNodeConflictHandler;
import com.synet.tool.rsc.incr.ssd.EqpConflictHandler;
import com.synet.tool.rsc.incr.ssd.LNodeConflictHandler;
import com.synet.tool.rsc.incr.ssd.StaConflictHandler;
import com.synet.tool.rsc.incr.ssd.TerminalConflictHandler;

public enum EnumConflict {

	// 二次
	IED("IED", IEDConflictHandler.class),
	Pins("输入虚端子", NoConflictHandler.class),
	Sets("定值", NoConflictHandler.class),
	Params("参数", NoConflictHandler.class),
	Rcbs("报告控制块", NoConflictHandler.class),
	Gooses("Goose控制块", NoConflictHandler.class),
	Smvs("Smv控制块", NoConflictHandler.class),
	Inputs("虚回路", NoConflictHandler.class),
	// 子项
	Pin("Pin", PinConflictHandler.class),
	FCDA("FCDA", FCDAConflictHandler.class),
	ExtRef("ExtRef", ExtRefConflictHandler.class),
	Set("SGCB", SetConflictHandler.class),
	Param("DataSet", NoConflictHandler.class),
	Rcb("Rcb", RcbConflictHandler.class),
	Goose("Goose", BaseCbConflictHandler.class),
	Smv("Smv", BaseCbConflictHandler.class),
	// 一次
	Sta("Substation", StaConflictHandler.class),
	Vol("VoltageLevel", NoConflictHandler.class),
	Bay("Bay", BayConflictHandler.class),
	CNODE("ConnectivityNode", CNodeConflictHandler.class),
	EQP("ConductingEquipment", EqpConflictHandler.class),
	PTR("PowerTransformer", EqpConflictHandler.class),
	TERM("Terminal", TerminalConflictHandler.class),
	LNODE("LNode", LNodeConflictHandler.class);
	
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
	
	public static EnumConflict getByType(String type) {
		EnumConflict conflict = null;
		for (EnumConflict cft : EnumConflict.values()) {
			if (cft.getType().equals(type)) {
				conflict = cft;
				break;
			}
		}
		return conflict;
	}
}
