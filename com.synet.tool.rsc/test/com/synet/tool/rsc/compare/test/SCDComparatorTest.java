package com.synet.tool.rsc.compare.test;

import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.junit.Test;

import com.shrcn.found.common.util.TimeCounter;
import com.synet.tool.rsc.compare.CompareResult;
import com.synet.tool.rsc.compare.SCDComparator;

public class SCDComparatorTest {

	@Test
	public void testExecute() {
		String srcpath = "F:/工程问题/rsc/CHB201810311445.scd";
		String destpath = "F:/工程问题/rsc/CHB201810311445-new.scd";
		IProgressMonitor monitor = new IProgressMonitor() {

			@Override
			public void beginTask(String name, int totalWork) {
				System.out.println(name);
			}

			@Override
			public void done() {
				
				
			}

			@Override
			public void internalWorked(double work) {
				
				
			}

			@Override
			public boolean isCanceled() {
				
				return false;
			}

			@Override
			public void setCanceled(boolean value) {
				
				
			}

			@Override
			public void setTaskName(String name) {
				System.out.println(name);
			}

			@Override
			public void subTask(String name) {
				System.out.println(name);
			}

			@Override
			public void worked(int work) {
				
				
			}};
		//		String destpath = "F:/工程问题/rsc/CHB201810311445.scd";
		SCDComparator cmp = new SCDComparator(srcpath, destpath, monitor);
		long t = System.currentTimeMillis();
		List<CompareResult> results = cmp.execute();
		t = System.currentTimeMillis() - t;
		for (CompareResult result : results) {
			System.out.println(result + "=====================================");
			result.getDifference().print();
		}
		System.out.println("对比耗时：" + t);
	}

}
