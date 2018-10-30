package com.synet.tool.rsc.util.test;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import com.shrcn.found.common.dict.DictManager;
import com.synet.tool.rsc.util.F1011_NO;
import com.synet.tool.rsc.util.Rule;
import com.synet.tool.rsc.util.RuleManager;
import com.synet.tool.rsc.util.RuleType;

public class RuleTypeTest {

	@Test
	public void testInitDicts() {
		RuleType.initDicts();
		DictManager dictmgr = DictManager.getInstance();
		for (RuleType t : RuleType.values()) {
			String[] dictNames = dictmgr.getDictNames(t.name());
			assertTrue(dictNames!=null && dictNames.length>0);
		}
		List<Rule> rules = RuleManager.getInstance().getRules();
		String dicttype = F1011_NO.class.getSimpleName();
		assertEquals(rules.size(), dictmgr.getDictNames(dicttype).length);
	}

}
