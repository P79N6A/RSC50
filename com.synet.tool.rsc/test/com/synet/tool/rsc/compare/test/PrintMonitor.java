package com.synet.tool.rsc.compare.test;

import org.eclipse.core.runtime.IProgressMonitor;

public class PrintMonitor implements IProgressMonitor {

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
		
		
	}
}