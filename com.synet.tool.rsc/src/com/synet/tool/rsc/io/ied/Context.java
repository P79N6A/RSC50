package com.synet.tool.rsc.io.ied;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Element;

import com.shrcn.found.common.cache.CacheFactory;
import com.shrcn.found.common.cache.CacheWrapper;
import com.shrcn.found.file.xml.DOM4JNodeHelper;
import com.shrcn.found.ui.view.Problem;
import com.synet.tool.rsc.model.Tb1061PoutEntity;
import com.synet.tool.rsc.model.Tb1062PinEntity;

public class Context {
	
	private Element commEl;
	private Element dtTypeNd;
	
	// 通信配置
	private Map<String, NetConfig> netCfgMap;
	// 数据模板
	private DatTypTplParser datTypTplParser;
	// 虚端子关联key=iedName value map:key=desc, ref  value=iedName, ref
	private Map<String, Map<String, String>> vtLinkMap;
	// 输出虚端子缓存
	private CacheWrapper poutCache;
	// 输入虚端子缓存
	private CacheWrapper pintCache;
	// 问题
	private List<Problem> problems;
	
	public Context() {
		this(null, null);
	}
	
	public Context(Element commEl, Element dtTypeNd) {
		this.commEl = commEl;
		this.dtTypeNd = dtTypeNd;
		init();
	}

	private void init() {
		this.vtLinkMap = new HashMap<>();
		problems = new ArrayList<Problem>();
		CacheFactory.createHashMapWrapper("pouts");
		CacheFactory.createHashMapWrapper("pints");
		poutCache = CacheFactory.getCacheWrapper("pouts");
		poutCache.clear();
		pintCache = CacheFactory.getCacheWrapper("pints");
		pintCache.clear();
		if (commEl != null) {
			initNetCfgs();
		}
		if (dtTypeNd != null) {
			initDatTpl();
		}
	}
	
	
	private void initNetCfgs() {
		this.netCfgMap = new HashMap<>();
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
	
	public void cachePout(String outAddr, Tb1061PoutEntity pout) {
		poutCache.put(outAddr, pout);
	}
	
	public void cachePin(String intAddr, Tb1062PinEntity pint) {
		pintCache.put(intAddr, pint);
	}
	
	public Tb1061PoutEntity getPout(String outAddr) {
		return (Tb1061PoutEntity) poutCache.get(outAddr);
	}
	
	public Tb1062PinEntity getPin(String intAddr) {
		return (Tb1062PinEntity) pintCache.get(intAddr);
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
