package com.synet.tool.rsc.compare.test;

import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.junit.Test;

import com.shrcn.found.common.dict.DictManager;
import com.synet.tool.rsc.RSCConstants;
import com.synet.tool.rsc.compare.Difference;
import com.synet.tool.rsc.compare.ied.SCDComparator;

public class SCDComparatorTest {
	

	@Test
	public void testExecute() {
		String srcpath = "./test/CHB201810311445.scd";
		String destpath = "./test/CHB201810311445-new.scd";
		IProgressMonitor monitor = new PrintMonitor();
		//		String destpath = "F:/工程问题/rsc/CHB201810311445.scd";
		DictManager dictmgr = DictManager.getInstance();
		dictmgr.init(getClass(), RSCConstants.DICT_PATH, true);
		SCDComparator cmp = new SCDComparator(srcpath, destpath, monitor);
		long t = System.currentTimeMillis();
		List<Difference> results = cmp.execute();
		t = System.currentTimeMillis() - t;
		for (Difference result : results) {
//			System.out.println(result + "=====================================");
			result.print();
		}
		System.out.println("对比耗时：" + t);
	}

}
