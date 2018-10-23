package com.synet.tool.rsc.util;

import java.util.List;

import com.shrcn.found.common.util.StringUtil;


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
	public static final Rule ST_R19 = getRuleById(201); // 预留
	public static final Rule MX_R19 = getRuleById(701); // 预留
	public static final Rule STRAP_R19 = getRuleById(621); // 预留
	public static final Rule IED_WRN_SV = getRuleById(104); // SV异常
	public static final Rule IED_WRN_GOOSE = getRuleById(105); // GOOSE异常
	public static final Rule IED_WRN_COMMON = getRuleById(151); // 装置一般告警
	public static final Rule IED_WRN_ALL = getRuleById(200); // 告警总
	public static final Rule IED_TEMP = getRuleById(901); // 装置温度
	public static final Rule FIB_POWER = getRuleById(950); // 光口光强
	
	public static Rule getType(String datSet, String lnName, String doName, String doDesc, String fc) {
		Rule[] types = F1011_NO.values();
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
			if (fc != null) {
				type = "ST".equalsIgnoreCase(fc) ? ST_R19 : MX_R19;
			} else {
				type = STRAP_R19;
			}
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
	
}
