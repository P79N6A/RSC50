package org.sf.feeling.swt.win32.extension.util;

import java.util.Arrays;

import org.sf.feeling.swt.win32.extension.system.SystemInfo;
import org.sf.feeling.swt.win32.internal.extension.Extension;

/**
 * 系统工具.
 * 
 * @author 孙春颖
 * @version 1.0, 2014-09-02
 */
public class SystemUtil {
	public static void main(String[] args) {
		System.err.println(getMacAddr());
		System.err.println(SystemInfo.getCPUID());
		System.err.println(SystemInfo.getMACID());
		System.err.println(SystemInfo.getCpuUsages());
	}

	/**
	 * 获得本机MAC地址.
	 * 
	 * @return
	 */
	public static String[] getMacAddr() {
		return SystemInfo.getMACAddresses();
	}
	
	/**
	 * 判断MAC地址是否为本机地址.
	 * 
	 * @param mac
	 * @return
	 */
	public static boolean isLocal(String mac){
		return Arrays.asList(SystemUtil.getMacAddr()).indexOf(mac.toUpperCase()) > -1;
	}

	/**
	 * 获得本机CPU usages.
	 * 
	 * @return
	 */
	public static int getCpuUsages() {
		return SystemInfo.getCpuUsages();
	}

	/**
	 * 获得本机CPUID.
	 * 
	 * @return
	 */
	public static String getCPUID() {
		return SystemInfo.getCPUID();
	}

	/**
	 * 获得本机MACID.
	 * 
	 * @return
	 */
	public static int[] getMACID() {
		return SystemInfo.getMACID();
	}
	
	/**
	 * 获得本机MACID.
	 * 
	 * @return
	 */
	public static int[] getMACAddr(int macId) {
		return Extension.GetMACAddress(macId);
	}
}
