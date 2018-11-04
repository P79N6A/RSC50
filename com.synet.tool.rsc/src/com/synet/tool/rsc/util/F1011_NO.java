package com.synet.tool.rsc.util;

import java.util.List;

import com.shrcn.found.common.util.StringUtil;
import com.synet.tool.rsc.RSCConstants;
import com.synet.tool.rsc.io.scd.SclUtil;


/**
 * 状态量功能类型
 * @author chunc
 *
 */
public class F1011_NO {
	
	public static final Rule ST_BRK = getRuleById(01); // 开关状态
	public static final Rule ST_DIS = getRuleById(21); // 刀闸状态
	public static final Rule VT_CRC = getRuleById(153); // 装置过程层配置CRC
	public static final Rule IED_COMM = getRuleById(202); // 装置通讯状态"
	public static final Rule IED_WRN_SV = getRuleById(104); // SV异常
	public static final Rule IED_WRN_GOOSE = getRuleById(105); // GOOSE异常
	
	public static final Rule IED_WRN_R19 = getRuleById(100); // 告警
	public static final Rule ST_R19 = getRuleById(200); // 遥信
	public static final Rule EVENT_R19 = getRuleById(400); // 动作
	public static final Rule PIN_R19 = getRuleById(500); // 开入虚端子
	public static final Rule STRAP_R19 = getRuleById(600); // 压板
	public static final Rule MX_R19 = getRuleById(700); // 保护测量量
	public static final Rule POUT_R19 = getRuleById(800); // 开出虚端子
	public static final Rule STATE_R19 = getRuleById(900); // 装置运行工况信号
	public static final Rule OTHERS = getRuleById(RSCConstants.OTHERS_ID); // 装置运行工况信号
	
	public static Rule getType(String dataset, String f1058RefAddr, String doDesc, Rule[] types) {
		String lnName = SclUtil.getLnName(f1058RefAddr);
		String doName = SclUtil.getDoName(f1058RefAddr);
		String fc = SclUtil.getFC(f1058RefAddr);
		return F1011_NO.getType(dataset, lnName, doName, doDesc, fc, types);
	}
	
	public static Rule getType(String datSet, String lnName, String doName, String doDesc, String fc) {
		return getType(datSet, lnName, doName, doDesc, fc, F1011_NO.values());
	}

	public static Rule getType(String datSet, String lnName, String doName, String doDesc, String fc, Rule[] types) {
		Rule type = null;
		Rule typeNullDesc = null;
		for (Rule rule : types) {
			if (rule.isEmpty()) {
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
				typeNullDesc = rule;
				continue;
			}
			type = rule;
			break;
		}
		if (type == null && typeNullDesc != null) {
			return typeNullDesc;
		}
		if ((type == null)) {
//			type = getDefault(datSet, fc);
			type = OTHERS;
		}
		return type;
	}
	
	public static String[][] getDictItems() {
		int size = F1011_NO.values().length;
		String[][] items = new String[size][2];
		for (int i=0; i<size; i++) {
			Rule typ = F1011_NO.values()[i];
			items[i] = new String[] {typ.getId()+"", typ.getName()};
		}
		return items;
	}
	
	public static String getNameById(int id) {
		for (Rule f1011 : F1011_NO.values()) {
			if(f1011.getId() == id) {
				return f1011.getName();
			}
		}
		return "";
	}
	
	public static Rule getRuleById(int id) {
		for (Rule f1011 : F1011_NO.values()) {
			if(f1011.getId() == id) {
				return f1011;
			}
		}
		return null;
	}
	
	private static Rule[] values() {
		List<Rule> rules = RuleManager.getInstance().getRules();
		return rules.toArray(new Rule[rules.size()]);
	}
	
	public static Rule getDefault(String datSet, String fc) {
		if (SclUtil.isAIN(datSet)) {
			return MX_R19;
		} else if (SclUtil.isDIN(datSet)) {
			return ST_R19;
		} else if (SclUtil.isStrap(datSet)) {
			return STRAP_R19;
		} else if (SclUtil.isWarning(datSet)) {
			return IED_WRN_R19;
		} else if (SclUtil.isState(datSet)) {
			return STATE_R19;
		} else {
			return "ST".equals(fc) ? ST_R19 : MX_R19;
		}
	}
}
