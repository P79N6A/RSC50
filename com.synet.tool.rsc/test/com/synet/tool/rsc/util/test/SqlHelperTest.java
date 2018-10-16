package com.synet.tool.rsc.util.test;

import static org.junit.Assert.*;

import org.junit.Test;

import com.synet.tool.rsc.RSCProperties;
import com.synet.tool.rsc.jdbc.ConnParam;
import com.synet.tool.rsc.util.SqlHelper;

public class SqlHelperTest {

	@Test
	public void testInitOracle() {
		ConnParam connParam = RSCProperties.getInstance().getConnParam();
		SqlHelper.initOracle(connParam);
	}

}
