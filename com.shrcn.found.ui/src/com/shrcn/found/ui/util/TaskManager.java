/**
 * Copyright (c) 2007, 2008 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based Visual Device Develop System.
 */
package com.shrcn.found.ui.util;

import java.util.concurrent.ConcurrentLinkedQueue;

import org.eclipse.core.runtime.jobs.Job;

/**
 * 本类用于控制用户并发操作。目前使用的是线程队列来实现，可以保证执行任务先后顺序，因此不可
 * 使用eclipse自身的Job的并发控制机制。例如：
 * private ISchedulingRule Schedule_RULE = new ISchedulingRule() {
 * 		public boolean contains(ISchedulingRule rule) {
 * 			return this.equals(rule);
 * 		}
 * 		public boolean isConflicting(ISchedulingRule rule) {
 * 			return this.equals(rule);
 * 		}
 * 	};
 * 	myjob1.setRule(Schedule_RULE);
 * 	myjob2.setRule(Schedule_RULE);
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2009-6-10
 */
/*
 * 修改历史
 * $Log: TaskManager.java,v $
 * Revision 1.1  2013/03/29 09:36:30  cchun
 * Add:创建
 *
 * Revision 1.4  2010/11/08 07:14:32  cchun
 * Update:清理引用
 *
 * Revision 1.3  2010/09/25 02:13:40  cchun
 * Update:添加注释
 *
 * Revision 1.2  2009/06/17 03:37:08  cchun
 * Thread改成Job
 *
 * Revision 1.1.2.1  2009/06/17 03:36:19  cchun
 * Thread改成Job
 *
 * Revision 1.1  2009/06/10 07:02:42  cchun
 * Add:添加多任务顺序执行管理工具
 *
 */
public class TaskManager extends Thread {
	
	private static ConcurrentLinkedQueue<Job> taskQueue = new ConcurrentLinkedQueue<Job>();
	
	public static void addTask(Job task) {
		taskQueue.add(task);
	}
	
	@Override
	public void run() {
		while(true) {
			try {
				if(!taskQueue.isEmpty()) {
					Job task = taskQueue.poll();
					task.schedule();
					task.join();
				}
				if(taskQueue.isEmpty())
					Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	/*private static Job genJob(final int id) {
		return new Job(""){ protected IStatus run(IProgressMonitor monitor) {
			System.out.println("Thread " + id);
			return Status.OK_STATUS;
		} };
	}
	public static void main(String[] args) {
		TaskManager mgr = new TaskManager();
		mgr.start();
		for(int i=1; i<99; i++) {
			TaskManager.addTask(genJob(i));
		}
	}*/
}
