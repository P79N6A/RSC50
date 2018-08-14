/**
 * Copyright (c) 2007, 2008 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based Visual Device Develop System.
 */
package com.shrcn.business.scl.util;

import com.shrcn.business.scl.SCTProperties;
import com.shrcn.business.scl.enums.EnumCommType;
import com.shrcn.found.common.util.ByteUtil;
import com.shrcn.found.common.util.CommonUtil;

/**
 * 
 * @author 刘静(mailto:lj6061@shrcn.com)
 * @version 1.0, 2009-6-22
 */
/*
 * 修改历史
 * $Log: CommunicationUtil.java,v $
 * Revision 1.1  2013/03/29 09:36:26  cchun
 * Add:创建
 *
 * Revision 1.4  2011/11/21 02:09:18  cchun
 * Refactor:统一MAC地址大写转换逻辑
 *
 * Revision 1.3  2011/11/18 09:16:27  cchun
 * Update:将初始MAC改成大写
 *
 * Revision 1.2  2011/11/16 09:07:18  cchun
 * Update:增加方法getNextMACCfg()
 *
 * Revision 1.1  2011/01/07 09:44:17  cchun
 * Refactor:移动到common项目
 *
 * Revision 1.5  2010/11/08 07:16:52  cchun
 * Update:清理引用
 *
 * Revision 1.4  2010/11/03 08:36:31  cchun
 * Fix Bug:修复保存错误
 *
 * Revision 1.3  2010/10/18 02:44:05  cchun
 * Update:清理引用
 *
 * Revision 1.2  2010/06/18 08:52:55  cchun
 * Refactor:重构系统配置保存方式
 *
 * Revision 1.1  2010/03/02 07:49:52  cchun
 * Add:添加重构代码
 *
 * Revision 1.6  2010/01/21 08:48:19  gj
 * Update:完成UI插件的国际化字符串资源提取
 *
 * Revision 1.5  2009/11/30 04:50:15  hqh
 * 添加获取ip
 *
 * Revision 1.4  2009/07/15 02:59:23  cchun
 * Refactor:修改包名
 *
 * Revision 1.3  2009/07/06 08:36:49  cchun
 * Refactor:将选项对话框相关代码统一放到同一个包下
 *
 * Revision 1.2  2009/07/06 07:35:13  lj6061
 * 添加配置文件统一建模选项
 * 改变配置文件工程的位置
 *
 * Revision 1.1  2009/06/26 09:05:48  lj6061
 * 重新整理类所在的包
 *
 * Revision 1.2  2009/06/25 07:31:35  lj6061
 * 整理AppID配置
 *
 * Revision 1.1  2009/06/22 10:56:28  lj6061
 * 修改配置文件，为GooseSMV添加配置
 *
 */
public class CommunicationUtil {
	
	private CommunicationUtil() {}
	
	/**
	 * 得到十六进制数合并后的值
	 * @param highBit
	 * @param lowBit
	 * @return
	 */
	public static int getValue(String highBit, String lowBit) {
		int high = Integer.parseInt(highBit, 16);
		int low = Integer.parseInt(lowBit, 16);
		return (high << 8) | low;
	}
	
	/**
	 * 根据旧appId得到新appId
	 * @param appId
	 * @return
	 */
	public static String getNewAppId(String appId) {
		int value = Integer.parseInt(appId, 16);
		value++;
		byte[] ids = ByteUtil.shortToBytesBE((short) value);
		return ByteUtil.bytes2HexString(ids);
	}
	
	/**
	 * 获得ip
	 * @param ip
	 * @return
	 */
	public static String getNextIP(String ip) {
		String[] split = ip.split("\\."); //$NON-NLS-1$
		int high = Integer.parseInt(split[2]);
		int low = Integer.parseInt(split[3]);
		int value = (high << 8) | low;
		value++;
		int lowBit = value & 255;
		int highBit = (value & 32767) >>> 8;
		String newIp = split[0] + "." + split[1] + "." //$NON-NLS-1$ //$NON-NLS-2$
				+ String.valueOf(highBit) + "." + String.valueOf(lowBit); //$NON-NLS-1$
		return newIp;
	}
	
	/**
	 * 计算mac地址后两位信息
	 */
	public static String[] getAddres(int value) {
		int lowBit = value & 0x00FF;
		int highBit = (value & 0xFF00) >>> 8;
		String low = CommonUtil.formatFillZero(lowBit, 2, "x"); //$NON-NLS-1$
		String high = CommonUtil.formatFillZero(highBit, 2, "x"); //$NON-NLS-1$
		return new String[] {high.toUpperCase(), low.toUpperCase()};
	}
	
	/**
	 * 得到下一GOOSE或者SMV的MAC和appId
	 * （用于复制和批量复制功能）
	 * @return
	 */
	public static String[] getNextMACCfg(EnumCommType commType) {
		SCTProperties propert = SCTProperties.getInstance();
		boolean isGSE = EnumCommType.GSE == commType;
		String mac = isGSE ? propert.getGseMac() : propert.getSmvMac();
		String appId = isGSE ? propert.getGseAPPID() : propert.getSmvAPPID();
		
		String[] cfg = getNextMACCfg(mac, appId);
		
		if (isGSE) {
			propert.setGseMac(cfg[0]);
			propert.setGseAPPID(cfg[1]);
		} else {
			propert.setSmvMac(cfg[0]);
			propert.setSmvAPPID(cfg[1]);
		}
		propert.saveData();
		return new String[] {cfg[0], cfg[1]};
	}
	
	/**
	 * 根据
	 * 
	 * @param mac
	 * @param appId
	 * @param isGSE
	 * @return
	 */
	public static String[] getNextMACCfg(String mac, String appId) {
		String[] b = mac.split("-");
		boolean useDefAppId = appId.equals(b[4] + b[5]);

		// 获取下一个MAC地址
		mac = getNextMAC(mac);
		
		if (useDefAppId) {
			appId = getAppId(mac);
		} else {
			appId = getNewAppId(appId);
		}
		
		return new String[] {mac, appId};
	}
	
	public static String getNextMAC(String mac) {
		String[] b = mac.split("-");
		String macPrefix = b[0] + "-" + b[1] + "-" + b[2] + "-" + b[3] + "-";
		int value = getValue(b[4], b[5]);
		value++;
		
		String[] addres = CommunicationUtil.getAddres(value);
		String lastAddr = (value < 65536) ? addres[0] + "-" + addres[1] : "FF-FF";
		return macPrefix + lastAddr;
	}
	
	public static String getAppId(String mac) {
		String[] b = mac.split("-");
		if (b.length!=6){
			return "";
		}
		int value = getValue(b[4], b[5]);
		String[] addres = CommunicationUtil.getAddres(value);
		String appId = b[3].substring(b[3].length()-1)+addres[0].substring(addres[0].length()-1) + addres[1];
		return appId;
	}
	
	public static void main(String[] args) {
		System.out.println(getAppId("01-0C-CD-4-0-1"));
	}
}
