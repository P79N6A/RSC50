package com.synet.tool.rsc.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.shrcn.found.common.dict.DictManager;

public enum RuleType {

	EQP_STATE("一次设备状态", 1, 99),
	IED_WARN("装置告警", 100, 199),
	IED_YX("装置遥信", 200, 299),
	IED_BUG("装置缺陷", 300, 399),
	PROT_EVENT("保护事件", 400, 499),
	IO_SIGNAL("开入开出信号", 500, 599),
	STRAP("压板", 600, 699),
	PROT_MS("保护测量量", 700, 799),
	PROT_SV("保护采样值", 800, 899),
	IED_STATE("装置运行工况", 900, 999);
	
	private String type;
	private int min;
	private int max;
	
	private RuleType(String type, int min, int max) {
		this.type = type;
		this.min = min;
		this.max = max;
	}
	
	public String getType() {
		return type;
	}
	public int getMin() {
		return min;
	}
	public int getMax() {
		return max;
	}
	
	public static void initDicts() {
		DictManager dictmgr = DictManager.getInstance();
		List<String[]> dictF1011 = new ArrayList<>();
		List<String[]> dictTemp = new ArrayList<>();
		List<Rule> rules = RuleManager.getInstance().getRules();
		RuleType[] types = RuleType.values();
		int curType = 0;
		for (Rule typ : rules) {
			String[] dictItem = new String[] {typ.getId()+"", typ.getName()};
			dictF1011.add(dictItem);
			dictTemp.add(dictItem);
			for (int i=0; i<types.length; i++) {
				RuleType rtyp = types[i];
				if (typ.getId() > rtyp.max && i==curType) {
					dictTemp.remove(dictItem);
					dictmgr.addDict(rtyp.name(), rtyp.name(), dictTemp.toArray(new String[dictTemp.size()][]));
					dictTemp.clear();
					dictTemp.add(dictItem);
					curType++;
					break;
				}
			}
		}
		dictmgr.addDict(IED_STATE.name(), IED_STATE.name(), dictTemp.toArray(new String[dictTemp.size()][]));
		String dicttype = F1011_NO.class.getSimpleName();
		dictmgr.addDict(dicttype, dicttype, dictF1011.toArray(new String[dictF1011.size()][]));
	}
}
