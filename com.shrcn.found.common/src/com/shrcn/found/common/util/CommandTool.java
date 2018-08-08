/**
 * Copyright (c) 2007, 2008 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based Visual Device Develop System.
 */
package com.shrcn.found.common.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import com.shrcn.found.common.log.SCTLogger;

/**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2009-7-16
 */

public class CommandTool {
	
	public static void executeFree(String command) {
		try {
			Runtime.getRuntime().exec(command);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 执行一个可执行程序
	 * @param command
	 */
	public static int execute(String command) {
		InputStream in = null;
		int re_code = -1;
		try {
			Process child = Runtime.getRuntime().exec(command);
			in = child.getInputStream();
			int c;
			while ((c = in.read()) != -1) {
				System.out.print((char)c);
				re_code++;
			}
			System.out.println("\n");
			child.waitFor();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			try {
				if(null != in)
					in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return re_code;
	}
	
	/**
	 * 指定指定命令及参数的程序
	 * @param cmdarray
	 * @return
	 */
	public static String execute(String...cmdarray) {
		String retMsg = null;
		InputStream in = null;
		try {
			Process child = Runtime.getRuntime().exec(cmdarray);
			in = child.getInputStream();
			List<Byte> outs = new ArrayList<Byte>();
			int c;
			while ((c = in.read()) != -1) {
				outs.add((byte) c);
			}
			child.destroy();
			int len = outs.size();
			byte[] data = new byte[len];
			for (int i=0; i<len; i++) {
				data[i] = outs.get(i);
			}
			retMsg = new String(data, System.getProperty("file.encoding"));
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if(null != in)
					in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return retMsg;
	}

	/**
	 * 打开文件夹
	 * @param dirPath
	 * @param isSelect 是否为选择模式
	 */
	public static void openDirectory(String dirPath, boolean isSelect) {
		File f = new File(dirPath);
		if (f.exists() && f.isDirectory()) {
			try {
				String command = isSelect ? "explorer /select, " : "explorer ";
				command += dirPath;
				Runtime.getRuntime().exec(command);
			} catch (IOException e) {
				SCTLogger.error("打开目录错误：", e);
			}
		}
	}
	

	/**
	 * 执行系统命令
	 * 
	 * @param cmd
	 * @return
	 * @throws Exception
	 */
	public static int execCommand(String cmd) throws Exception {
		Process proc;
		proc = Runtime.getRuntime().exec(cmd);
		StreamGobbler errorGobbler = new StreamGobbler(proc.getErrorStream(),
				"Error"); //$NON-NLS-1$
		StreamGobbler outputGobbler = new StreamGobbler(proc.getInputStream(),
				"Output"); //$NON-NLS-1$
		errorGobbler.start();
		outputGobbler.start();

		return proc.waitFor();
	}
}

/**
 * 压缩文件，解压文件线程
 * 
 * @author Administrator
 * 
 */
class StreamGobbler extends Thread {
	InputStream is;

	String type;

	StreamGobbler(InputStream is, String type) {
		this.is = is;
		this.type = type;
	}

	public void run() {
		try {
			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader br = new BufferedReader(isr);
			while ((br.readLine()) != null) {
			}
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}
}