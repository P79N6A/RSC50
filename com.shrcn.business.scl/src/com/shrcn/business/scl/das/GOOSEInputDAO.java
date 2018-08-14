/**
 * Copyright (c) 2007, 2008 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based Visual Device Develop System.
 */
package com.shrcn.business.scl.das;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.velocity.VelocityContext;
import org.dom4j.Element;

import com.shrcn.business.scl.check.SignalsChecker;
import com.shrcn.business.scl.enums.EnumFCTypes;
import com.shrcn.business.scl.history.HistoryManager;
import com.shrcn.business.scl.history.MarkInfo.IedCfg;
import com.shrcn.business.scl.history.MarkInfo.OperType;
import com.shrcn.business.scl.model.FCDAEntry;
import com.shrcn.business.scl.model.GOOSEInput;
import com.shrcn.business.scl.model.SCL;
import com.shrcn.business.scl.model.SCLModel;
import com.shrcn.business.scl.util.RelationsConfig;
import com.shrcn.found.common.log.SCTLogger;
import com.shrcn.found.common.util.StringUtil;
import com.shrcn.found.file.temp.TemplateUtil;
import com.shrcn.found.file.xml.DOM4JNodeHelper;
import com.shrcn.found.ui.view.ConsoleManager;
import com.shrcn.found.xmldb.XMLDBHelper;

/**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2009-4-24
 */
/*
 * 修改历史
 * $Log: GOOSEInputDAO.java,v $
 * Revision 1.1  2013/03/29 09:36:21  cchun
 * Add:创建
 *
 * Revision 1.46  2013/01/29 11:08:05  cchun
 * Refactor:提取getSubType()
 *
 * Revision 1.45  2012/09/03 07:02:15  cchun
 * Update:增加delInner()
 *
 * Revision 1.44  2012/03/09 07:35:50  cchun
 * Update:规范prefix和daName属性用法
 *
 * Revision 1.43  2012/01/17 08:50:29  cchun
 * Update:使用更加安全的xpath形式
 *
 * Revision 1.42  2011/12/12 09:27:42  cchun
 * Update:修改getSubType()增加对类型不存在的提示
 *
 * Revision 1.41  2011/11/30 11:00:38  cchun
 * Update:整理代码
 *
 * Revision 1.40  2011/11/21 06:49:57  cchun
 * Update:添加排序方法sort()
 *
 * Revision 1.39  2011/07/20 05:55:36  cchun
 * Refactor:增加getInnerDesc()
 *
 * Revision 1.38  2011/05/09 11:30:33  cchun
 * Update:添加信号数据类型查询方法
 *
 * Revision 1.37  2010/12/07 09:37:43  cchun
 * Update:去掉访问点过滤
 *
 * Revision 1.36  2010/11/08 07:13:31  cchun
 * Update:清理引用
 *
 * Revision 1.35  2010/09/27 07:08:13  cchun
 * Update:将描述查询对象放到循环外面，提高性能
 *
 * Revision 1.34  2010/08/18 08:30:27  cchun
 * Refactor:使用insertAfterType()
 *
 * Revision 1.33  2010/07/28 06:54:55  cchun
 * Update:添加null判断
 *
 * Revision 1.31  2010/05/26 08:10:31  cchun
 * Update:修改构造函数参数
 *
 * Revision 1.30  2010/04/12 02:02:36  cchun
 * Update:修改fillInnerDesc()，return->continue
 *
 * Revision 1.29  2009/12/11 05:26:47  cchun
 * Update:添加修改历史标记
 *
 * Revision 1.28  2009/11/13 07:18:11  cchun
 * Update:完善关联表格功能
 *
 * Revision 1.27  2009/10/29 09:08:03  cchun
 * Refactor:统一fcda描述查询
 *
 * Revision 1.26  2009/09/03 06:16:18  cchun
 * Update:为外部信号添加LN描述
 *
 * Revision 1.25  2009/09/02 07:10:23  cchun
 * Update:支持选定信号添加q、t
 *
 * Revision 1.24  2009/09/01 09:36:03  cchun
 * Fix Bug:修改删除多个关联信号bug
 *
 * Revision 1.23  2009/08/21 09:54:21  cchun
 * Update:增加对外部信号LD不存在的判断
 *
 * Revision 1.22  2009/08/18 09:36:10  cchun
 * Update:合并代码
 *
 * Revision 1.21  2009/08/03 08:44:52  cchun
 * Update:信号关联增加上移、下移功能
 *
 * Revision 1.20  2009/07/15 02:58:34  cchun
 * 添加信号关联调整功能
 *
 * Revision 1.19  2009/07/03 09:34:43  cchun
 * 修改内部信号描述为null的bug
 *
 * Revision 1.18  2009/06/12 02:43:10  cchun
 * Fix Bug:修改信号关联主键错误
 *
 * Revision 1.17.2.1  2009/06/12 02:41:58  cchun
 * Fix Bug:修改信号关联主键错误
 *
 * Revision 1.17  2009/06/11 04:07:04  cchun
 * Update:修改LD可能为null的bug
 *
 * Revision 1.16  2009/06/10 08:17:38  cchun
 * Fix Bug:修正Inputs节点位置顺序及其删除操作
 *
 * Revision 1.15  2009/06/09 07:28:58  cchun
 * Update:Inputs需放在GSEControl节点之前
 *
 * Revision 1.14  2009/06/08 06:07:53  cchun
 * 修改DOI不存在的异常
 *
 * Revision 1.13  2009/05/20 03:28:29  cchun
 * Update:添加delExtRef()
 *
 * Revision 1.12  2009/05/20 02:39:23  cchun
 * Fix Bug:修改事务错误
 *
 * Revision 1.11  2009/05/18 09:44:02  cchun
 * Update:增加对inputs节点不存在的处理
 *
 * Revision 1.10  2009/05/18 09:29:48  pht
 * 当内部信号为空时，导出时空显示。
 *
 * Revision 1.9  2009/05/18 09:28:25  pht
 * 当内部信号为空时，导出时空显示。
 *
 * Revision 1.8  2009/05/18 05:52:37  cchun
 * Update:完善关联数据保存操作
 *
 * Revision 1.7  2009/05/15 08:02:21  cchun
 * Update:修改xpath错误
 *
 * Revision 1.6  2009/05/14 00:40:48  hqh
 * 统一全局变量
 *
 * Revision 1.5  2009/05/08 12:07:04  cchun
 * Update:完善外部、内部信号视图
 *
 * Revision 1.4  2009/04/29 02:23:33  cchun
 * 统一数据操作对象接口
 *
 * Revision 1.3  2009/04/28 07:03:45  cchun
 * Update:将xpath改成绝对路径，提高性能
 *
 * Revision 1.2  2009/04/28 06:46:51  cchun
 * Update:性能优化
 *
 * Revision 1.1  2009/04/27 03:45:35  cchun
 * Update:添加goose数据方位类
 *
 */
public class GOOSEInputDAO extends IEDEditDAO {
	
	private String  ldXPath;
	private String  lnXPath;
	private Element ldData;
	private Element lnData;
	private Element dataTemplate;
	private List<GOOSEInput> data;
	private Map<String, Element> outLDCache = new HashMap<String, Element>();
	private Map<String, Map<String, FCDAEntry>> gooseCache;
	private ConsoleManager console = ConsoleManager.getInstance();
	private RelationsConfig relaCfg = RelationsConfig.getInstance();
	
	public GOOSEInputDAO(String iedName, String ldInst, Element lnData) {
		super(iedName, ldInst);
		this.lnData = lnData;
		this.ldXPath = iedXPath + "/scl:AccessPoint/scl:Server/scl:LDevice[@inst='" + ldInst + "']";
		this.lnXPath = ldXPath + SCLModel.getLnNodePath(lnData);
		ldData = XMLDBHelper.selectSingleNode(ldXPath);
	}
	
	public GOOSEInputDAO(String iedName, String ldInst, Element lnData, Map<String, Map<String, FCDAEntry>> gooseCache) {
		this(iedName, ldInst, lnData);
		this.gooseCache = gooseCache;
	}
	
	/**
	 * 添加关联。
	 * @param goin
	 */
	public synchronized void addExtRef(GOOSEInput goin) {
		try {
			String inputPath = lnXPath + "/scl:Inputs";
			if(!XMLDBHelper.existsNode(inputPath)) {
				String[] nodeTypes = new String[]{"DataSet", "ReportControl", "LogControl", "DOI"};
				Element input = DOM4JNodeHelper.createSCLNode("Inputs");
				XMLDBHelper.insertAfterType(lnXPath, nodeTypes, input);
			}
			XMLDBHelper.insertAsLast(inputPath, goin.getData());
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}
	
	public void delInputs() {
		XMLDBHelper.removeNodes(lnXPath + "/scl:Inputs");
		markChanged(-1);
	}
	
	
	public synchronized void addExtRefbyElements(List<Element> importElements) {
		try {
			String inputPath = lnXPath + "/scl:Inputs";
			if(!XMLDBHelper.existsNode(inputPath)) {
				String[] nodeTypes = new String[]{"DataSet", "ReportControl", "LogControl", "DOI"};
				Element input = DOM4JNodeHelper.createSCLNode("Inputs");
				XMLDBHelper.insertAfterType(lnXPath, nodeTypes, input);
			}
			for (Element ele : importElements) {
				XMLDBHelper.insertAsLast(inputPath, ele);
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}
//	/**
//	 * 删除指定关联
//	 * @param goin
//	 */
//	public void delExtRef(GOOSEInput goin) {
////		String extRefPath = lnXPath + "/scl:Inputs/scl:ExtRef[@intAddr='" + 
////					goin.getIntAddr() + "']";
//		String extRefPath = lnXPath + goin.getInputsXPath();
//		XMLDBHelper.removeNodes(extRefPath);
//	}
	
	/**
	 * 按序号删除指定关联
	 * @param idx
	 */
	public void delExtRef(int idx) {
		String extRefPath = lnXPath + "/scl:Inputs/scl:ExtRef[" + idx + "]";
		XMLDBHelper.removeNodes(extRefPath);
		markChanged(idx);
	}
	
	/**
	 * 删除选中行的内部信号
	 * @param idx 数据库中对应的行号
	 */
	public void delInner(int idx) {
		String extRefPath = lnXPath + "/scl:Inputs/scl:ExtRef[" + idx + "]";
		XMLDBHelper.saveAttribute(extRefPath, "intAddr", "");
	}
	
	/**
	 * 上移
	 * @param selIdx
	 */
	public void moveUp(int selIdx) {
		XMLDBHelper.moveUp(lnXPath + "/scl:Inputs/scl:ExtRef", selIdx);
		
		String ref = getRef(selIdx);
		HistoryManager.getInstance().markIedCfgChanged(IedCfg.Inputs, OperType.MOVE_UP, ref);
	}
	
	/**
	 * 下移
	 * @param selIdx
	 */
	public void moveDown(int selIdx) {
		XMLDBHelper.moveDown(lnXPath + "/scl:Inputs/scl:ExtRef", selIdx);

		String ref = getRef(selIdx);
		HistoryManager.getInstance().markIedCfgChanged(IedCfg.Inputs, OperType.MOVE_DOWN, ref);
	}
	
	private String getAttString(Element extRef, String attName) {
		String value = extRef.attributeValue(attName);
		return StringUtil.isEmpty(value)?"":"[@" + attName + "='" + value + "']";
	}
	
	/**
	 * 修改关联。
	 * @param goin
	 * @param oldIntAddr 
	 */
	public void updateExtRef(GOOSEInput goin, GOOSEInput oldIntAddr) {
		Element extRef = oldIntAddr.getData();
		String prefix = extRef.attributeValue("prefix");
		String lnClass = extRef.attributeValue("lnClass");
		String lnInst = extRef.attributeValue("lnInst");
		String intAddr = extRef.attributeValue("intAddr");
		if (StringUtil.isEmpty(intAddr)) {
			intAddr = "[not(@intAddr) or @intAddr='']";
		} else {
			intAddr = "[@intAddr='" + intAddr + "']";
		}
		String select = lnXPath + "/scl:Inputs/scl:ExtRef".concat(getAttString(extRef, "iedName")).concat(getAttString(extRef, "ldInst"))
				.concat(SCL.getLNAtts(prefix, lnClass, lnInst)).concat(getAttString(extRef, "doName"))
				.concat(getAttString(extRef, "daName")).concat(intAddr);
		if (XMLDBHelper.existsNode(select))
			XMLDBHelper.replaceNode(select, goin.getData());
		markChanged(-1);
	}
	
	/**
	 * 为关联信号增加q、t后保存
	 * @param goins
	 */
	public void saveFixedInputs(List<GOOSEInput> goins) {
		if(goins==null || goins.size()==0)
			return;
		Element inputs = DOM4JNodeHelper.createSCLNode("Inputs");
		for(GOOSEInput goin:goins)
			inputs.add(goin.getData().createCopy());
		String select = lnXPath + "/scl:Inputs";
		XMLDBHelper.replaceNode(select, inputs);
		markChanged(-1);
	}
	
	/**
	 * 为选中的关联信号增加q、t并保存
	 * @param selIdx
	 * @param fixGoin
	 */
	public void saveSelectedFix(int selIdx, GOOSEInput fixGoin) {
		String select = lnXPath + "/scl:Inputs/scl:ExtRef[" + selIdx + "]";
		XMLDBHelper.insertAfter(select, fixGoin.getData());
		markChanged(selIdx);
	}
	
	private void markChanged(int selIdx) {
		String ref = getRef(selIdx);
		HistoryManager.getInstance().markIedCfgUpdate(IedCfg.Inputs, ref, "", "", "");
	}

	private String getRef(int selIdx) {
		String ref = iedName + ldInst + "/" + SCL.getLnName(lnData) + "$Inputs";
		if (selIdx > -1)
			ref += "$ExtRef[" + selIdx + "]";
		return ref;
	}

	public void reLoad() {
		this.data = new ArrayList<GOOSEInput>();
		List<Element> extRefs = DOM4JNodeHelper.selectNodes(lnData,
				"./*[name()='Inputs']/*[name()='ExtRef']");
		for(Element extRef:extRefs) {
			GOOSEInput gin = new GOOSEInput("", "", extRef);
			data.add(gin);
		}
		if(extRefs.size() > 0) {
			outLDCache.clear();
			fillDesc();
		}
	}
	
	private void fillDesc() {
		fillOutDesc();
		fillInnerDesc();
	}
	
	private void fillOutDesc() {
		FcdaDAO fcdaDao = FcdaDAO.getInstance();
		for(GOOSEInput gin:data) {
			Element extRef = gin.getData();
			String iedName = extRef.attributeValue("iedName");
			if(null==iedName || "".equals(iedName))
				continue;
			String ldInst = extRef.attributeValue("ldInst");
			String prefix = extRef.attributeValue("prefix");
			String lnClass = extRef.attributeValue("lnClass");
			String lnInst = extRef.attributeValue("lnInst");
			String doName = extRef.attributeValue("doName");
			String daName = extRef.attributeValue("daName");
			String desc = fcdaDao.getFCDADesc(iedName, ldInst, 
					prefix, lnClass, lnInst, doName);
			if(null == desc)
				gin.setOutSignalDesc("");
			else if(!SignalsChecker.T.equals(daName) && !SignalsChecker.Q.equals(daName))
				gin.setOutSignalDesc(desc);
		}
	}
		
	private void fillInnerDesc() {
		for(GOOSEInput gin:data) {
			Element extRef = gin.getData();
			gin.setInSignalDesc(getInnerDesc(extRef, ldData));
		}
	}
	
	public static String getInnerDesc(Element extRef, Element ldData) {
		String addr = extRef.attributeValue("intAddr");
		String[] addrInfo = SCL.getIntAddrInfo(addr);
		if (addrInfo.length < 5)
			return "";
		String prefix = addrInfo[1];
		String lnClass = addrInfo[2];
		String lnInst = addrInfo[3];
		String doName = addrInfo[4];
		int p = doName.indexOf(".");
		if (p > 0) {
			doName = doName.substring(0, p);
		}
		if(addr.indexOf(".q")>0 || addr.indexOf(".t")>0) {
			return "";
		}
		String ln = SCL.getLNXPath(prefix, lnClass, lnInst);
		String doXpath = "." + ln + "/*[name()='DOI'][@name='" + doName + "']";
		String desc = DOM4JNodeHelper.getNodeValueByXPath(ldData, doXpath + "/*[name()='DAI'][@name='dU']/*[name()='Val']");
		if(StringUtil.isEmpty(desc))
			desc = DOM4JNodeHelper.getAttributeByXPath(ldData, doXpath + "/@desc");
		return desc;
	}
	
	/**
	 * 获取DA数据类型
	 * @param daAtts
	 * @return
	 */
	public String getDAType(EnumFCTypes fc, String...daAtts) {
		if (daAtts.length < 7)
			return null;
		if (dataTemplate == null)
			dataTemplate = XMLDBHelper.selectSingleNode("/scl:SCL/scl:DataTypeTemplates");
		String iedName = daAtts[0];
		String ldInst = daAtts[1];
		String prefix = daAtts[2];
		String lnClass = daAtts[3];
		String lnInst = daAtts[4];
		String doName = daAtts[5];
		String daName = daAtts[6];
		
		if (StringUtil.isEmpty(daName) && EnumFCTypes.MX != fc)
			return null;
		
		String lnXpath = "/scl:SCL/scl:IED[@name='" + iedName +
				"']/scl:AccessPoint/scl:Server/scl:LDevice[@inst='" + ldInst +
				"']" + SCL.getLNXPath(prefix, lnClass, lnInst) +
				"/@lnType";
		String lnType = XMLDBHelper.getAttributeValue(lnXpath);
		if (EnumFCTypes.MX == fc) {
			return DataTypeTemplateDao.getCDC(dataTemplate, lnType, doName);
		}
//		String doType = DataTypeTemplateDao.getSubType(lnType, doName, dataTemplate);
//		return DataTypeTemplateDao.getSubType(doType, daName, dataTemplate);
		return DataTypeTemplateDao.getBType(dataTemplate, lnType, doName, daName);
	}
	
	public List<GOOSEInput> getData() {
		if (data == null) {
			reLoad();
		}
		return this.data;
	}

	public String getIedXPath() {
		return iedXPath;
	}

	public void setIedXPath(String iedXPath) {
		this.iedXPath = iedXPath;
	}

	public String getLdInst() {
		return ldInst;
	}

	public void setLdInst(String ldInst) {
		this.ldInst = ldInst;
	}

	public Element getLnData() {
		return lnData;
	}

	public void setLnData(Element lnData) {
		this.lnData = lnData;
	}

	public String getLnXPath() {
		return lnXPath;
	}
    
	/**
	 * 排序IED的二次关联关系
	 * @param sortFlag 升序降序标志位
	 */
	@SuppressWarnings("unchecked")
	public void sort(boolean asc) {
		Element inputs = XMLDBHelper.selectSingleNode(lnXPath + "/scl:Inputs");
		if(inputs == null) 
			return;
		List<Element> extRefs = inputs.elements("ExtRef");
		sort(extRefs, asc);
		XMLDBHelper.replaceNode(lnXPath + "/scl:Inputs", inputs);
	}
	
	/**
	 * 按IED名称降序排列
	 * @param extRefs
	 * @param asc
	 */
	private void sort(List<Element> extRefs, boolean asc) {
		int size = extRefs.size();
		for (int i = 0; i < size; i++) {
			for (int j = i + 1; j <= size - 1; j++) {
				Element eleI = extRefs.get(i);
				String iedNamei = eleI.attributeValue("iedName");
				Element eleJ = extRefs.get(j);
				String iedNamej = eleJ.attributeValue("iedName");
				int compare = iedNamei.compareTo(iedNamej);
				if ((asc && compare < 0)
						|| (!asc && compare > 0)) {
					extRefs.set(i, eleJ.createCopy());
					extRefs.remove(eleI);
					extRefs.set(j, eleI.createCopy());
					extRefs.remove(eleJ);
				}
			}
		}
	}
	
	/**
	 * 得到所有外部装置名称
	 * @return
	 */
	public String[] getOutIEDNames() {
		Set<String> iedNames = new HashSet<>();
		List<String> nameList = XMLDBHelper.getAttributeValues(lnXPath + "/scl:Inputs/scl:ExtRef/@iedName");
		iedNames.addAll(nameList);
		return iedNames.toArray(new String[iedNames.size()]);
	}
	
	private Map<String, FCDAEntry> getMapEntry(String outIed) {
		Map<String, FCDAEntry> mapFCDAEntry = gooseCache.get(outIed);
		if (mapFCDAEntry == null) {
			GetFCDA getFCDA = new GetFCDA(outIed, "PIGO", "/scl:LN0", "dsGOOSE");
			mapFCDAEntry = getFCDA.getMapEntry();
			gooseCache.put(outIed, mapFCDAEntry);
		}
		return mapFCDAEntry;
	}
	
	private String replaceBayNum(String desc, int num, String line) {
		if ("A".equals(line)) {
			line = "1";
		} else if ("B".equals(line)) {
			line = "2";
		} else {
			line = (desc.indexOf("${n}") > 0) ? "1" : "";
		}
		VelocityContext context = new VelocityContext();
		context.put("d", num);
		context.put("n", line);
		return TemplateUtil.evaluateContext(context, desc);
	}
	
	/**
	 * 根据外部装置名称自动创建关联关系
	 * @param outIeds
	 */
	public String[] autoRelate(String[] outIeds) {
		Element iedCfg = relaCfg.getIEDCfg(iedName);
		int innerNum = relaCfg.getIedNumber(iedName);
		String lineIn = iedName.charAt(iedName.length()-1)+"";
		if (innerNum < 0) {
			String message = "警告：当前装置 " + iedName + " 序号有误！";
			SCTLogger.warn(message);
			console.append(message);
		}
		if (iedName.startsWith("PT_")) {
			if (innerNum < 10) {
				innerNum = innerNum - 3;
			} else {
				innerNum = innerNum - 13;
			}
		}
		XMLDBHelper.removeNodes(ldXPath + "/scl:LN0/scl:Inputs");
		if (outIeds != null && outIeds.length>0) {
			List<String> relatedIeds = new ArrayList<>();
			Element inputNode = DOM4JNodeHelper.createSCLNode("Inputs");
			for (String outIed : outIeds) {
				int outerNum = relaCfg.getIedNumber(outIed);
				String lineOut = outIed.charAt(outIed.length()-1)+"";
				if (outerNum < 0) {
					String message = "警告：关联装置 " + outIed + " 序号有误！";
					SCTLogger.warn(message);
					console.append(message);
				}
				if (outIed.startsWith("PT_")) {
					if (outerNum < 10) {
						outerNum = outerNum - 3;
					} else {
						outerNum = outerNum - 13;
					}
				}
				Map<String, FCDAEntry> mapFCDAEntry = getMapEntry(outIed);
				Element inputsCfg = relaCfg.getInputsCfg(iedCfg, outIed);
				List<Element> extRefs = inputsCfg.elements("ExtRef");
				int maxNum = relaCfg.getMaxNumber(inputsCfg);
				for (Element extRef : extRefs) {
					String intAddrDesc = extRef.attributeValue("intAddr");
					String outAddrDesc = extRef.attributeValue("outAddr");
					if ((maxNum > 0 && intAddrDesc.indexOf("${d}") > 0) || 
							intAddrDesc.indexOf("${n}") > 0) {
						intAddrDesc = replaceBayNum(intAddrDesc, outerNum, lineOut);
					}
					if (outAddrDesc.indexOf("${d}") > 0 ||
							outAddrDesc.indexOf("${n}") > 0) {
						outAddrDesc = replaceBayNum(outAddrDesc, innerNum, lineIn);
					}
					FCDAEntry outFcda = mapFCDAEntry.get(outAddrDesc);
					if (outFcda == null) {
						String relation = outAddrDesc + "[" + outIed + "] -> [" + iedName + "]" + intAddrDesc;
						String message = "错误：关联装置 " + outIed + " 中未找到虚端子“" + outAddrDesc + "”，" + relation + "，关联失败！";
						SCTLogger.warn(message);
						console.append(message);
						continue;
					}
					String intAddr = getIntAddr(intAddrDesc);
					if (intAddr == null) {
						String relation = outAddrDesc + "[" + outIed + "] -> [" + iedName + "]" + intAddrDesc;
						String message = "错误：当前装置 " + iedName + " 中未找到虚端子“" + intAddrDesc + "”，" + relation + "，关联失败！";
						SCTLogger.warn(message);
						console.append(message);
						continue;
					}
					SCLModel.addExtRef(inputNode, outFcda, intAddr);
					if (!relatedIeds.contains(outIed)) {
						relatedIeds.add(outIed);
					}
				}
			}
			XMLDBHelper.insertAfterType(ldXPath + "/scl:LN0", new String[]{"DOI"}, inputNode);
			return relatedIeds.toArray(new String[relatedIeds.size()]);
		} else {
			return null;
		}
	}
	
	/**
	 * 根据描述查找内部接收虚端子
	 * @param intAddrDesc
	 * @return
	 */
	private String getIntAddr(String intAddrDesc) {
		String intAddr = null;
		String xpath = ".//*[name()='DOI'][@desc='" + intAddrDesc + "']/*[name()='DAI'][not(not(@sAddr))]";
		Element daNode = DOM4JNodeHelper.selectSingleNode(ldData, xpath);
		if (daNode == null) {
			xpath = ".//*[name()='DOI']/*[name()='DAI'][@name='dU']/*[name()='Val'][text()='" + intAddrDesc + "']";
			Element duNode = DOM4JNodeHelper.selectSingleNode(ldData, xpath);
			if (duNode != null) {
				Element doNode = duNode.getParent().getParent();
				daNode = DOM4JNodeHelper.selectSingleNode(doNode, "./[name()='DAI'][not(not(@sAddr))]");
			}
		}
		if (daNode != null) {
			String daName = daNode.attributeValue("name");
			Element doNode = daNode.getParent();
			String doName = doNode.attributeValue("name");
			String lnName = SCL.getLnName(doNode.getParent());
			intAddr = ldInst + "/" + lnName + "." + doName + "." + daName;
		}
		return intAddr;
	}
	
}
