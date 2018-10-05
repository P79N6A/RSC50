package com.synet.tool.rsc.io.ied;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Element;

import com.shrcn.business.scl.model.SCL;
import com.shrcn.found.file.xml.DOM4JNodeHelper;
import com.shrcn.found.ui.view.Problem;
import com.shrcn.found.xmldb.XMLDBHelper;

public class Context {
	// 通信配置
	private Map<String, NetConfig> netCfgMap;
	// 数据模板
	private DatTypTplParser datTypTplParser;
	// 虚端子关联key=iedName value map:key=desc, ref  value=iedName, ref
	private Map<String, Map<String, String>> vtLinkMap;
	// 问题
	private List<Problem> problems;
	
	public Context() {
		init();
	}

	private void init() {
		this.vtLinkMap = new HashMap<>();
		initNetCfgs();
		initDatTpl();
	}
	
	
	private void initNetCfgs() {
		this.netCfgMap = new HashMap<>();
		Element commEl = XMLDBHelper.selectSingleNode(SCL.XPATH_COMMUNICATION);
		List<Element> subNetList = commEl.elements("SubNetwork");
		for (Element subNet : subNetList) {
			List<Element> connApList = subNet.elements("ConnectedAP");
			for (Element connApEl : connApList) {
				String iedName = connApEl.attributeValue("iedName");
				List<Element> subElList = connApEl.elements();
				for (Element subEl : subElList) {
					String ndName = subEl.getName();
					String cBID = "";
					NetConfig netCfg = null;
					switch (ndName) {
					case "Address":// mms
						cBID = iedName + ".MMS";
						String ip = DOM4JNodeHelper.getNodeValue(subEl, "./P[@type='IP']");
						netCfg = netCfgMap.get(cBID);
						if (netCfg == null) {
							netCfg = new NetConfig(cBID, ip);
							netCfgMap.put(cBID, netCfg);
						} else {
							netCfg.setIpB(ip);
						}
						break;
					case "GSE":// goose
						addCbNetCfg(iedName, subEl, "GO");
						break;
					case "SMV":// smv
						addCbNetCfg(iedName, subEl, "MS");
						break;
					default:
						break;
					}
				} // end SubEl
			}// end ConnectedAP
		}// end SubNetwork
	}
	
	private void addCbNetCfg(String iedName, Element subEl, String fc) {
		String ldInst = subEl.attributeValue("ldInst");
		String cbName = subEl.attributeValue("cbName");
		String cbRef = iedName + ldInst + "/LLN0$" + fc + "$" + cbName;//PL6602PIGO/LLN0$GO$gocb0，IL6602MUSV/LLN0$MS$smvcb1
		String mACAddr = DOM4JNodeHelper.getNodeValue(subEl, "./Address/P[@type='MAC-Address']");
		String vLANID = DOM4JNodeHelper.getNodeValue(subEl, "./Address/P[@type='VLAN-ID']"); 
		String vLANPriority = DOM4JNodeHelper.getNodeValue(subEl, "./Address/P[@type='VLAN-PRIORITY']"); 
		String aPPID = DOM4JNodeHelper.getNodeValue(subEl, "./Address/P[@type='APPID']");
		NetConfig netCfg = new NetConfig(cbRef, cbName, mACAddr, vLANID, vLANPriority, aPPID);
		netCfgMap.put(cbRef, netCfg);
	}

	private void initDatTpl() {
		Element dtTypeNd = XMLDBHelper.selectSingleNode(SCL.XPATH_DATATYPETEMPLATES);
		problems = new ArrayList<Problem>();
		datTypTplParser = new DatTypTplParser(dtTypeNd, problems);
	}
	
	public Map<String, Map<String, Object[]>> getLnTypeMap() {
		return datTypTplParser.getLnTypeMap();
	}
	
	public NetConfig getNetConfig(String cbId) {
		return netCfgMap.get(cbId);
	}

	public int getBType(Element ldEl, Element fcdaEl) {
		return datTypTplParser.getBType(ldEl, fcdaEl);
	}
	
	public void addVTLink(String iedName, String intAddr, String outAddr) {
		Map<String, String> links = vtLinkMap.get(iedName);
		if (links == null) {
			links = new HashMap<>();
			vtLinkMap.put(iedName, links);
		}
		links.put(intAddr, outAddr);
	}
	
	public Map<String, Map<String, String>> getVtLinkMap() {
		return vtLinkMap;
	}

	public List<Problem> getProblems() {
		return this.problems;
	}
	
	public void addProblem(Problem problem) {
		problems.add(problem);
	}

	public void addError(String iedName, String subType, String ref, String desc) {
		addProblem(Problem.createError(iedName, subType, ref, desc));
	}
	
	public void addWarning(String iedName, String subType, String ref, String desc) {
		addProblem(Problem.createWarning(iedName, subType, ref, desc));
	}
}
