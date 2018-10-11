package com.synet.tool.rsc.util;

import com.shrcn.found.common.util.StringUtil;


/**
 * 状态量功能类型
 * @author chunc
 *
 */
public enum F1011_NO {
	
	ST_BRK(01, "开关状态"),
	ST_BRK_A(02, "开关分相A相状态"),
	ST_BRK_B(03, "开关分相B相状态"),
	ST_BRK_C(04, "开关分相C相状态"),
	ST_DIS(05, "刀闸状态"),
	LINK_BRK(06, "链路中断信息"),
	LINK_WRN(07, "链路告警信号"),
	PROT_ACT(8, "保护动作信号"),
	PROT_DOUT(9, "保护跳闸出口信号"),
	BRK_COF(10, "跳合闸反校信号"),
	BRK_OUT(11, "保护合闸出口信号"),
	START_ERR(12, "启动失灵信号"),
	P_LINK_BRK(13, "保护联跳信号"),
	P_LOCK(14, "保护闭锁信号"),
	VT_CRC(15, "装置过程层配置CRC"),
	IED_COMM(16, "装置通讯状态"),
	ST_FAULT(17, "缺陷状态"),
	ST_PHYCONN(18, "物理回路状态"),
	ST_R19(19, "预留"),
	ST_R20(20, "预留"),
	ST_R21(21, "预留"),
	ST_R22(22, "预留"),
	ST_R23(23, "预留"),
	ST_R24(24, "预留"),
	ST_R25(25, "预留"),
	ST_R26(26, "预留"),
	ST_R27(27, "预留"),
	ST_R28(28, "预留"),
	ST_R29(29, "预留"),
	ST_R30(30, "预留"),
	CHECK_STRAP(31, "装置检修硬压板"),
	REMOTE_STRAP(32, "远方操作硬压板"),
	BRK_STRAP(33, "跳合闸出口硬压板"),
	GOOUT_STRAP(34, "GOOSE输出软压板"),
	GOIN_STRAP(35, "GOOSE输入软压板"),
	SVOUT_STRAP(36, "SV输出软压板"),
	SVIN_STRAP(37, "SV接收软压板"),
	BRK_OPEN_STRAP(38, "边（中）断路器强制分位软压板"),
	DIS_OPEN_STRAP(39, "隔离刀闸强制分位软压板"),
	DIS_CLOSE_STRAP(40, "隔离刀闸强制合位软压板"),
	FUN_STRAP(41, "功能软压板"),
	PROT_D_STRAP(42, "差动或纵联保护投入软压板"),
	PROT_L_STRAP(43, "距离保护投入软压板"),
	LOCK_CLOSE_STRAP(44, "闭锁重合闸软压板"),
	UNLOCK_CLOSE_STRAP(45, "投入重合闸软压板（预留）"),
	EAN_BUSLIN_STRAP(46, "投母线互联软压板"),
	EAN_BUSSEP_STRAP(47, "投母联分列软压板"),
	EAN_FIN_STRAP(48, "投分段分列软压板"),
	EAN_PROT_PRT_BAK_STRAP(49, "主变后备保护投入软压板"),
	R_STRAP(50, "压板类型备用（预留）"),
	IED_RUN_LIGHT(51, "装置运行状态灯"),
	IED_WRN_HW(52, "装置告警-硬件异常"),
	IED_WRN_SW(53, "装置告警-软件异常"),
	IED_WRN_CPT(54, "装置告警-CT/PT断线"),
	IED_WRN_BY(55, "装置告警-差流越限"),
	IED_WRN_DIN(56, "装置告警-开入异常"),
	IED_WRN_SYN(57, "对时异常"),
	IED_WRN_SV(58, "SV异常"),
	IED_WRN_GOOSE(59, "GOOSE异常"),
//	ST(60~99, "装置告警预留"),
	IED_WRN_R60(60, "装置告警预留"),
	IED_WRN_R61(61, "装置告警预留"),
	IED_WRN_R62(62, "装置告警预留"),
	IED_WRN_R63(63, "装置告警预留"),
	IED_WRN_R64(64, "装置告警预留"),
	IED_WRN_R65(65, "装置告警预留"),
	IED_WRN_COMMON(100, "装置一般告警"),
	PROT_MEAR(101, "保护测量值（通用）"),
	PROT_MEAR_IA(102, "A相电流保护测量值"),
	PROT_MEAR_IB(103, "B相电流保护测量值"),
	PROT_MEAR_IC(104, "C相电流保护测量值"),
	PROT_MEAR_IN(105, "零序或中性点电流保护测量值"),
	PROT_MEAR_UA(106, "A相电压保护测量值"),
	PROT_MEAR_UB(107, "B相电压保护测量值"),
	PROT_MEAR_UC(108, "C相电压保护测量值"),
	PROT_MEAR_UN(109, "零序或中性点电压保护测量值"),
	PROT_MEAR_IDA(110, "A相差动电流测量值"),
	PROT_MEAR_IDB(111, "B相差动电流测量值"),
	PROT_MEAR_IDC(112, "C相差动电流测量值"),
	PROT_MEAR_IDN(113, "零序差动电流测量值"),
	PROT_MEAR_R114(114, "保护测量值备用"),
	PROT_MEAR_R115(115, "保护测量值备用"),
	PROT_MEAR_R116(116, "保护测量值备用"),
	PROT_MEAR_R117(117, "保护测量值备用"),
	PROT_MEAR_R118(118, "保护测量值备用"),
	PROT_MEAR_R119(119, "保护测量值备用"),
	PROT_MEAR_SV(120, "SV采样值（通用）"),
	PROT_MEAR_IA_AD1(121, "A相电流AD1采样值"),
	PROT_MEAR_IA_AD2(122, "A相电流AD2采样值"),
	PROT_MEAR_IB_AD1(123, "B相电流AD1采样值"),
	PROT_MEAR_IB_AD2(124, "B相电流AD2采样值"),
	PROT_MEAR_IC_AD1(125, "C相电流AD1采样值"),
	PROT_MEAR_IC_AD2(126, "C相电流AD2采样值"),
	PROT_MEAR_IN_AD1(127, "单相或零序电流AD1采样值"),
	PROT_MEAR_IN_AD2(128, "单相或零序电流AD2采样值"),
	PROT_MEAR_UA_AD1(129, "A相电压AD1采样值"),
	PROT_MEAR_UA_AD2(130, "A相电压AD2采样值"),
	PROT_MEAR_UB_AD1(131, "B相电压AD1采样值"),
	PROT_MEAR_UB_AD2(132, "B相电压AD2采样值"),
	PROT_MEAR_UC_AD1(133, "C相电压AD1采样值"),
	PROT_MEAR_UC_AD2(134, "C相电压AD2采样值"),
	PROT_MEAR_UN_AD1(135, "单相或零序电压AD1采样值"),
	PROT_MEAR_UN_AD2(136, "单相或零序电压AD2采样值"),
	PROT_MEAR_SV_R137(137, "SV采样值备用"),
	PROT_MEAR_SV_R138(138, "SV采样值备用"),
	PROT_MEAR_SV_R139(139, "SV采样值备用"),
	PROT_MEAR_SV_R140(140, "SV采样值备用"),
	IED_TEMP(141, "装置温度"),
	IED_VOL1_5(142, "装置工作电压1.5V"),
	IED_VOL5(143, "装置工作电压5V"),
	IED_VOL12(144, "装置工作电压12V"),
	IED_VOL24(145, "装置工作电压24V"),
	IED_VOL48(146, "装置工作电压48V"),
	IED_VOL110(147, "装置工作电压110V"),
	IED_VOL220(148, "装置工作电压220V"),
	FIB_POWER(149, "光口光强");
	
	private int id;
	private String name;
	private Rule rule;
	
	private F1011_NO(int id, String name) {
		this.id = id;
		this.name = name;
		this.rule = RuleManager.getInstance().getRule(id);
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public Rule getRule() {
		return rule;
	}
	
	public void setRule(Rule rule) {
		this.rule = rule;
	}

	public static F1011_NO getType(String datSet, String lnName, String doName, String doDesc) {
		F1011_NO[] types = F1011_NO.values();
		F1011_NO type = null;
		F1011_NO typeNullDesc = null;
		for (F1011_NO temp : types) {
			Rule rule = temp.getRule();
			if (rule == null) {
				continue;
			}
			if (!StringUtil.isEmpty(rule.getDatSet())) {
				String[] dats = rule.getDatSet().split("/");
				boolean match = false;
				for (String dat : dats) {
					if (datSet.contains(dat)) {
						match = true;
						break;
					}
				}
				if (!match) {
					continue;
				}
			}
			if (!StringUtil.isEmpty(rule.getLnName()) && !rule.getLnName().contains(lnName)) {
				continue;
			}
			if (!StringUtil.isEmpty(rule.getDoName()) && !doName.contains(rule.getDoName())) {
				continue;
			}
			if (!StringUtil.isEmpty(rule.getDoDesc())) {
				String[] dcs = rule.getDoDesc().split("/");
				boolean match = false;
				for (String dc : dcs) {
					int p = dc.indexOf('@');
					if (p < 0) {
						if (doDesc.contains(dc)) {
							match = true;
							break;
						}
					} else {
						boolean is = true;
						String[] ands = dc.split("@");
						for (String and : ands) {
							if (!doDesc.contains(and)) {
								is = false;
								break;
							}
						}
						if (is) {
							match = true;
							break;
						}
					}
				}
				if (!match) {
					continue;
				}
			} else {
				typeNullDesc = temp;
				continue;
			}
			type = temp;
			break;
		}
		if (type == null && typeNullDesc != null) {
			return typeNullDesc;
		}
		return (type == null) ? F1011_NO.ST_R19 : type;
	}
	
	public static String[][] getDictItems() {
		int size = F1011_NO.values().length;
		String[][] items = new String[size][2];
		for (int i=0; i<size; i++) {
			F1011_NO typ = F1011_NO.values()[i];
			items[i] = new String[] {typ.getId()+"", typ.getName()};
		}
		return items;
	}
	
	public static String getNameById(int id) {
		for (F1011_NO f1011 : F1011_NO.values()) {
			if(f1011.getId() == id) {
				return f1011.getName();
			}
		}
		return "";
	}
	
	/**
	 * 根据id找到实例，设置Rule
	 * @param id
	 * @param rule
	 */
	public static void setRule(int id, Rule rule) {
		for (F1011_NO f1011 : F1011_NO.values()) {
			if(f1011.getId() == id) {
				f1011.setRule(rule);
			}
		}
	}
	
	
}
