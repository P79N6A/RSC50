/**
 * Copyright (c) 2007, 2008 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based Visual Device Develop System.
 */
package com.shrcn.business.scl;

import com.shrcn.business.scl.util.CommunicationUtil;
import com.shrcn.found.file.util.AProperties;
import com.shrcn.found.file.util.PropertyFileManager;

/**
 * 
 * @author 刘静(mailto:lj6061@shrcn.com)
 * @version 1.0, 2009-6-4
 */
/*
 * 修改历史 $Log: SCTProperties.java,v $
 * 修改历史 Revision 1.4  2014/01/08 03:52:40  cchun
 * 修改历史 Refactor:将getBoolValue()改成公共方法
 * 修改历史
 * 修改历史 Revision 1.3  2013/09/05 07:29:00  cxc
 * 修改历史 fix bug:修复导入FPK文件出错，判断是否为sg板时出错
 * 修改历史
 * 修改历史 Revision 1.2  2013/08/13 12:45:04  cchun
 * 修改历史 Udpate:增加一般属性访问方法
 * 修改历史
 * 修改历史 Revision 1.1  2013/03/29 09:36:56  cchun
 * 修改历史 Add:创建
 * 修改历史
 * 修改历史 Revision 1.25  2013/01/28 03:17:54  cchun
 * 修改历史 Update:添加goose最大、最小时间配置
 * 修改历史
 * 修改历史 Revision 1.24  2012/11/14 05:45:15  cchun
 * 修改历史 Update:提取property文件管理方法
 * 修改历史
 * 修改历史 Revision 1.23  2011/08/24 08:15:16  cchun
 * 修改历史 Update:按虚端子类型过滤
 * 修改历史
 * 修改历史 Revision 1.22  2011/08/11 10:12:01  cchun
 * 修改历史 Update:扩充可配置参数
 * 修改历史
 * 修改历史 Revision 1.21  2011/07/11 08:52:28  cchun
 * 修改历史 Refactor:修改常量定义类
 * 修改历史
 * 修改历史 Revision 1.20  2011/04/12 09:17:13  cchun
 * 修改历史 Update:sct.properties改为在第一次调用时加载
 * 修改历史
 * 修改历史 Revision 1.19  2011/01/07 09:40:43  cchun
 * 修改历史 Update:添加nextMmsIP()
 * 修改历史
 * 修改历史 Revision 1.18  2010/12/01 08:25:17  cchun
 * 修改历史 Update:修改异常处理
 * 修改历史
 * 修改历史 Revision 1.17  2010/11/30 01:06:17  cchun
 * 修改历史 Refactor:封装properties文件操作方法
 * 修改历史
 * 修改历史 Revision 1.16  2010/11/23 06:44:55  cchun
 * 修改历史 Update:添加异常日志
 * 修改历史
 * 修改历史 Revision 1.15  2010/11/08 02:29:57  cchun
 * 修改历史 Update:添加schema校验参数
 * 修改历史
 * 修改历史 Revision 1.14  2010/11/04 09:26:43  cchun
 * 修改历史 Update:添加依据描述更新描述
 * 修改历史
 * 修改历史 Revision 1.13  2010/11/03 03:02:33  cchun
 * 修改历史 Update:添加gse appID
 * 修改历史
 * 修改历史 Revision 1.12  2010/09/09 08:14:15  cchun
 * 修改历史 Update:添加CRC_CHECK
 * 修改历史
 * 修改历史 Revision 1.11  2010/06/28 02:45:25  cchun
 * 修改历史 Update:添加oid配置项
 * 修改历史
 * 修改历史 Revision 1.10  2010/06/18 08:51:00  cchun
 * 修改历史 Update:系统缺省配置改到文件中加载
 * 修改历史
 * 修改历史 Revision 1.9  2010/05/31 02:50:07  cchun
 * 修改历史 Update:增加needsVersions()
 * 修改历史
 * 修改历史 Revision 1.8  2010/05/27 05:40:23  cchun
 * 修改历史 Update:添加version配置项
 * 修改历史
 * 修改历史 Revision 1.7  2009/12/02 03:30:34  lj6061
 * 修改历史 整理选项配置代码
 * 修改历史
 * 修改历史 Revision 1.6  2009/12/01 03:44:41  hqh
 * 修改历史 添加网络模式常量
 * 修改历史
 * 修改历史 Revision 1.5  2009/11/30 04:45:41  hqh
 * 修改历史 添加ip配置常量
 * 修改历史
 * 修改历史 Revision 1.4  2009/11/09 07:04:53  hqh
 * 修改历史 修改判断条件
 * 修改历史
 * 修改历史 Revision 1.3  2009/11/02 10:32:16  hqh
 * 修改历史 添加SMV_APPID key
 * 修改历史
 * 修改历史 Revision 1.2  2009/07/15 02:58:34  cchun
 * 修改历史 添加信号关联调整功能
 * 修改历史
 * 修改历史 Revision 1.1  2009/07/06 07:35:13  lj6061
 * 修改历史 添加配置文件统一建模选项
 * 修改历史 改变配置文件工程的位置
 * 修改历史
 * 修改历史 Revision 1.12  2009/06/25 07:32:29  lj6061
 * 修改历史 整理AppID配置
 * 修改历史
 * 修改历史 Revision 1.11  2009/06/23 02:36:06  lj6061
 * 修改历史 提取配置常量
 * 修改历史
 * 修改历史 Revision 1.10  2009/06/22 10:56:29  lj6061
 * 修改历史 修改配置文件，为GooseSMV添加配置
 * 修改历史
 * 修改历史 Revision 1.9  2009/06/19 01:58:55  lj6061
 * 修改历史 修改变量类型
 * 修改历史
 * 修改历史 Revision 1.2.2.6  2009/06/19 01:52:24  lj6061
 * 修改历史 修改变量类型
 * 修改历史
 * 修改历史 Revision 1.2.2.5  2009/06/18 09:28:10  lj6061
 * 修改历史 修改大于255进位
 * 修改历史
 * 修改历史 Revision 1.8  2009/06/18 07:28:35  pht
 * 修改历史 添加静态方法。
 * 修改历史
 * 修改历史 Revision 1.7  2009/06/18 05:43:07  hqh
 * 修改历史 修改appid
 * 修改历史
 * 修改历史 Revision 1.6  2009/06/15 05:55:09  hqh
 * 修改历史 修改配置文件
 * 修改历史
 * 修改历史 Revision 1.5  2009/06/12 09:03:25  hqh
 * 修改历史 修改对话框
 * 修改历史 Revision 1.4 2009/06/12 02:23:25 lj6061
 * 添加SMVGSE配置
 * 
 * Revision 1.3 2009/06/12 01:10:28 hqh 修改配置文件
 * 
 * Revision 1.2 2009/06/04 08:43:02 lj6061 添加配置文件
 * 
 * Revision 1.1 2009/06/04 08:22:20 lj6061 添加配置文件
 * 
 */
public class SCTProperties extends AProperties {

	private String path = "com/shrcn/business/scl/sct.properties";

	private SCTProperties() {
		PropertyFileManager.initFile(data, properties, path, getClass());
	}

	private static volatile SCTProperties instance = new SCTProperties();

	/**
	 * 获取单例对象
	 */
	public static SCTProperties getInstance() {
		if (null == instance) {
			synchronized (SCTProperties.class) {
				if (null == instance) {
					instance = new SCTProperties();
				}
			}
		}
		return instance;
	}
	public String getSchemaPath() {
		return data.getProperty(IPropertTag.SCL_SCHEMA);
	}

	public void setSchemaPath(String schemaPath) {
		data.setProperty(IPropertTag.SCL_SCHEMA, schemaPath);
	}

	public String getGseMac() {
		return data.getProperty(IPropertTag.GSE_MAC_TITLE);
	}
	/**
	 * 得到下一个mac
	 * @return
	 */
	public String nextGseMac() {
		String mac = getGseMac();
		mac = CommunicationUtil.getNextMAC(mac);
		if (!"".equals(mac)) { //$NON-NLS-1$
			setGseMac(mac);
			saveData();
		}
		return mac;
	}
	public void setGseMac(String gseMac) {
		data.setProperty(IPropertTag.GSE_MAC_TITLE, gseMac);
	}
	
	public String getGseMinTime() {
		return data.getProperty(IPropertTag.GSE_MIN_TIME);
	}

	public void setGseMinTime(String minTime) {
		data.setProperty(IPropertTag.GSE_MIN_TIME, minTime);
	}
	
	public String getGseMaxTime() {
		return data.getProperty(IPropertTag.GSE_MAX_TIME);
	}

	public void setGseMaxTime(String maxTime) {
		data.setProperty(IPropertTag.GSE_MAX_TIME, maxTime);
	}

	public String getSmvMac() {
		return data.getProperty(IPropertTag.SMV_MAC_TITLE);
	}
	/**
	 * 得到下一个mac
	 * @return
	 */
	public String nextSmvMac() {
		String mac = getSmvMac();
		mac = CommunicationUtil.getNextMAC(mac);
		if (!"".equals(mac)) { //$NON-NLS-1$
			setSmvMac(mac);
			saveData();
		}
		return mac;
	}
	public void setSmvMac(String smvMac) {
		data.setProperty(IPropertTag.SMV_MAC_TITLE, smvMac);
	}
	
	public String getTempletModel() {
		return data.getProperty(IPropertTag.UNIFIED_MODEL);
	}

	public void setTempletModel(String templetModel) {
		data.setProperty(IPropertTag.UNIFIED_MODEL, templetModel);
	}
	
	public String getGseAPPID() {
		return data.getProperty(IPropertTag.GSE_APPID_TITLE);
	}

	public void setGseAPPID(String gseAPPID) {
		data.setProperty(IPropertTag.GSE_APPID_TITLE, gseAPPID);
	}
	
	public String getSmvAPPID() {
		return data.getProperty(IPropertTag.SMV_APPID_TITLE);
	}

	public void setSmvAPPID(String smvAPPID) {
		data.setProperty(IPropertTag.SMV_APPID_TITLE, smvAPPID);
	}
	
	public String getSubnetMask() {
		String mask = data.getProperty(IPropertTag.SUBNET_MASK);
		
		if (mask == null) {
			return "255.255.255.0";
		}
		return mask;
	}
	
	public void setSubnetMask(String subnetMask) {
		data.setProperty(IPropertTag.SUBNET_MASK, subnetMask);
	}

	/**
	 * 得到下一个ip
	 * @return
	 */
	public String nextMmsIP() {
		String ip = getMmsIP();
		ip = CommunicationUtil.getNextIP(ip);
		if (!"".equals(ip)) { //$NON-NLS-1$
			setMmsIP(ip);
			saveData();
		}
		return ip;
	}
	
	public String getMmsIP() {
		return data.getProperty(IPropertTag.MMS_IP_TITLE);
	}

	public void setMmsIP(String mmsIP) {
		data.setProperty(IPropertTag.MMS_IP_TITLE, mmsIP);
	}
	
	public String getMmsPSel() {
		return data.getProperty(IPropertTag.MMS_OSI_PSEL);
	}

	public void setMmsPSel(String psel) {
		data.setProperty(IPropertTag.MMS_OSI_PSEL, psel);
	}
	
	public String getMmsTSel() {
		return data.getProperty(IPropertTag.MMS_OSI_TSEL);
	}

	public void setMmsTSel(String tsel) {
		data.setProperty(IPropertTag.MMS_OSI_TSEL, tsel);
	}

	public boolean getPrefixAutoMode() {
	//	return getBoolValue(data.getProperty(IPropertTag.PREFIX_AUTO_MODE));
		return getBoolValue(IPropertTag.PREFIX_AUTO_MODE);
	}
	
	public void setPrefixAutoMode(boolean auto) {
		data.setProperty(IPropertTag.PREFIX_AUTO_MODE, "" + auto);
	}
	
	public boolean getRelationCheckMode() {
		return getBoolValue(IPropertTag.RELATION_CHECK_MODE);
	}

	public void setRelationCheckMode(boolean check) {
		data.setProperty(IPropertTag.RELATION_CHECK_MODE, "" + check);
	}
	
	public boolean getRelationNormMode() {
		return getBoolValue(IPropertTag.RELATION_NORM_MODE);
	}

	public void setRelationNormMode(boolean check) {
		data.setProperty(IPropertTag.RELATION_NORM_MODE, "" + check);
	}
	
	public boolean getRelationDaValue() {
		return getBoolValue(IPropertTag.RELATION_DA_VALUE);
	}

	public void setRelationDaValue(boolean check) {
		data.setProperty(IPropertTag.RELATION_DA_VALUE, "" + check);
	}
	
	public boolean isSameType(String type1, String type2) {
		boolean isSmailiar = getRelationCheckMode() ;
		if (!isSmailiar) {
			return type1!=null && type1.equals(type2);
		} else {
			return type1.equals(type2) || (isSv(type1) && isSv(type2))
					|| (isInt(type1) && isInt(type2)) || (isFloat(type1) && isFloat(type2));
		}
	}
	
	/**
	 * 判断是否为泛整型
	 * @param type
	 * @return
	 */
	private boolean isInt(String type) {
		return getRelationCheckMode() && ("Tcmd".equals(type) || "Dbpos".equals(type) || (type!=null && type.startsWith("INT")));
	}
	
	private boolean isSv(String type) {
		return "SAV".equals(type) || "MV".equals(type);
	}
	
	/**
	 * 判断是否为泛浮点型
	 * @param type
	 * @return
	 */
	private boolean isFloat(String type) {
		return getRelationCheckMode() && (type!=null && type.startsWith("FLOAT"));
	}

	public String getSclVersion() {
		return data.getProperty(IPropertTag.SCL_VERSION);
	}

	public void setSclVersion(String sclVersion) {
		data.setProperty(IPropertTag.SCL_VERSION, sclVersion);
	}
	
	public String getSchemaVersion() {
		return data.getProperty(IPropertTag.SCHEMA_VER);
	}

	public void setSchemaVersion(String sclVersion) {
		data.setProperty(IPropertTag.SCHEMA_VER, sclVersion);
	}

	public String getVTLnClass() {
		return data.getProperty(IPropertTag.VT_LNCLASS);
	}

	public void setVTLnClass(String lnClass) {
		data.setProperty(IPropertTag.VT_LNCLASS, lnClass);
	}
	
	public void setPrimaryOID(String oid){
		data.setProperty(IPropertTag.SCL_PRIMARY_OID, oid);
	}
	
	/**
	 * 设置是否需要CRC校验
	 * @param crcCheck
	 */
	public void setCRCCheck(boolean crcCheck) {
		data.setProperty(IPropertTag.CRC_CHECK, "" + crcCheck);
		saveData();
	}
	
	/**
	 * 设置是否需要历史记录
	 * @param crcCheck
	 */
	public void setHistory(boolean record) {
		data.setProperty(IPropertTag.HISTORY, "" + record);
		saveData();
	}
	
	/**
	 * 是否生成oid
	 * @return
	 */
	public boolean generateOID(){
		return getBoolValue(IPropertTag.SCL_PRIMARY_OID);
	}
	
	/**
	 * 是否需要version属性
	 * @return
	 */
	public boolean needsVersions() {
		return getBoolValue(IPropertTag.SCL_VERSION);
	}
	
	/**
	 * 是否需要CRC校验
	 * @return
	 */
	public boolean needsCRCCheck() {
		return getBoolValue(IPropertTag.CRC_CHECK);
	}

	/**
	 * 是否需要CRC校验
	 * @return
	 */
	public boolean needsHistory() {
		return getBoolValue(IPropertTag.HISTORY);
	}
	
	/**
	 * 判断是否为开入虚端子
	 * @param lnClass
	 * @return
	 */
	public boolean isVTLnClass(String lnClass) {
		String[] lnClasses = getVTLnClass().split(",");
		for (String ln : lnClasses) {
			if (ln.equals(lnClass)) {
				return true;
			}
		}
		return false;
	}
	
	public String getInterna() {
		return data.getProperty("LANGUAGE");
	}

	public void setInterna(String interna) {
		data.setProperty("LANGUAGE", interna);
	}
	
	public String getXmlParser() {
		return data.getProperty("XML_PARSER");
	}

	public void setXmlParser(String xmlparser) {
		data.setProperty("XML_PARSER", xmlparser);
	}

	public String getLayerModel() {
		return data.getProperty("NETWORK_MODEL");
	}

	public void setLayerModel(String layerModel) {
		data.setProperty("NETWORK_MODEL", layerModel);
	}
	
	public String getMmsIPDblIdx() {
		return data.getProperty("IP_DBL_IDX");
	}

	public void setMmsIPDblIdx(String mmsIPDblIdx) {
		data.setProperty("IP_DBL_IDX", mmsIPDblIdx);
	}

	public String getTempletPath() {
		return data.getProperty("PRITEMPLATE");
	}

	public void setTempletPath(String templetPath) {
		data.setProperty("PRITEMPLATE", templetPath);
	}
	
	public boolean impFCDAByOldDesc() {
		return getBoolValue("IMP_FCDA_BY_OLDDESC");
	}

	public void setImpFCDAByOldDesc(String value) {
		data.setProperty("IMP_FCDA_BY_OLDDESC", value);
	}

	/**
	 * 是否需要XML Schema校验
	 * @return
	 */
	public boolean needsXSDCheck() {
		return getBoolValue("XSD_CHECK");
	}
	
	/**
	 * 设置是否需要XML Schema校验
	 * @param xsdCheck
	 */
	public void setXSDCheck(boolean xsdCheck) {
		data.setProperty("XSD_CHECK", "" + xsdCheck);
		saveData();
	}
}
