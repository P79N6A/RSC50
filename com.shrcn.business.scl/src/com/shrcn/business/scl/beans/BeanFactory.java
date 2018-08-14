package com.shrcn.business.scl.beans;

import org.dom4j.Element;

import com.shrcn.business.scl.das.CommunicationDAO;
import com.shrcn.found.common.util.StringUtil;

public class BeanFactory {
	
	private BeanFactory() {}
	
	/**
	 * 创建MMS设置Bean
	 * @param cpEle
	 * @return
	 */
	public static MMSConfigModel createMMS(Element cpEle) {
		Element addrEle = cpEle.element("Address");
		String iedName = cpEle.attributeValue("iedName");
		String apName = cpEle.attributeValue("apName");
		String ip = CommunicationDAO.getTypeValue(addrEle, "IP");
		String sub = CommunicationDAO.getTypeValue(addrEle, "IP-SUBNET");
		String getWay = CommunicationDAO.getTypeValue(addrEle, "IP-GATEWAY");
		String aeInvoke = CommunicationDAO.getTypeValue(addrEle, "OSI-AE-Invoke");
		String aeQualifier = CommunicationDAO.getTypeValue(addrEle, "OSI-AE-Qualifier");
		String apInvoke = CommunicationDAO.getTypeValue(addrEle, "OSI-AP-Invoke");
		String apTitle = CommunicationDAO.getTypeValue(addrEle, "OSI-AP-Title");
		String nasp = CommunicationDAO.getTypeValue(addrEle, "OSI-NASP");
		String tsel = CommunicationDAO.getTypeValue(addrEle, "OSI-TSEL");
		String psel = CommunicationDAO.getTypeValue(addrEle, "OSI-PSEL");
		String ssel = CommunicationDAO.getTypeValue(addrEle, "OSI-SSEL");
		MMSConfigModel datasetMMS = new MMSConfigModel(iedName,apName,ip,sub,getWay,aeInvoke,aeQualifier,apInvoke,apTitle,nasp,tsel,psel,ssel);
		return datasetMMS;
	}

	/**
	 * 创建GSE设置Bean
	 * @param gseEle
	 * @param iedNamecs
	 * @param apNamecs
	 * @return
	 */
	public static GSEConfigModel createGSE(Element gseEle, String iedNamecs,
			String apNamecs) {
		String iedName = iedNamecs;
		String apName = apNamecs;
		String controlBk = gseEle.attributeValue("cbName");
		String ldInst = gseEle.attributeValue("ldInst");
		Element addrEle = gseEle.element("Address");
		String macAddress = CommunicationDAO.getTypeValue(addrEle, "MAC-Address");
		String vlanId = CommunicationDAO.getTypeValue(addrEle, "VLAN-ID");
		String vlanP = CommunicationDAO.getTypeValue(addrEle, "VLAN-PRIORITY");
		String appId = CommunicationDAO.getTypeValue(addrEle, "APPID");
		Element minT = gseEle.element("MinTime");
		Element maxT = gseEle.element("MaxTime");
		String minTime = StringUtil.nullToEmpty(minT==null ? "" : minT.getStringValue());
		String maxTime = StringUtil.nullToEmpty(maxT==null ? "" : maxT.getStringValue());
		GSEConfigModel datasetGSE = new GSEConfigModel(iedName, apName,
				controlBk, ldInst, macAddress, vlanId, vlanP, appId, minTime,
				maxTime);
		return datasetGSE;
	}

	/**
	 * 创建SMV设置Bean
	 * @param smvEle
	 * @param iedNamecs
	 * @param apNamecs
	 * @return
	 */
	public static SMVConfigModel createSMV(Element smvEle, String iedNamecs,
			String apNamecs) {
		String iedName = iedNamecs;
		String apName = apNamecs;
		String controlBk = smvEle.attributeValue("cbName");
		String ldInst = smvEle.attributeValue("ldInst");
		Element addrEle = smvEle.element("Address");
		String macAddress = CommunicationDAO.getTypeValue(addrEle, "MAC-Address");
		String vlanId = CommunicationDAO.getTypeValue(addrEle, "VLAN-ID");
		String vlanP = CommunicationDAO.getTypeValue(addrEle, "VLAN-PRIORITY");
		String appId = CommunicationDAO.getTypeValue(addrEle, "APPID");
		SMVConfigModel datasetSMV = new SMVConfigModel(iedName, apName,
				controlBk, ldInst, macAddress, vlanId, vlanP, appId);
		return datasetSMV;
	}
}
