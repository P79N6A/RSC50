/**
 * Copyright (c) 2007, 2008 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based Visual Device Develop System.
 */
package com.shrcn.business.scl.model;

import java.util.Arrays;
import java.util.List;

import org.dom4j.Element;

import com.shrcn.business.scl.common.DefaultInfo;
import com.shrcn.business.scl.enums.EnumCtrlBlock;
import com.shrcn.business.scl.model.navgtree.BayEntry;
import com.shrcn.business.scl.model.navgtree.INaviTreeEntry;
import com.shrcn.business.scl.model.navgtree.LNInfo;
import com.shrcn.business.scl.util.SCLUtil;
import com.shrcn.found.common.Constants;
import com.shrcn.found.common.log.SCTLogger;
import com.shrcn.found.common.util.StringUtil;
import com.shrcn.found.file.xml.DOM4JNodeHelper;
import com.shrcn.found.xmldb.XMLDBHelper;

/**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2009-5-15
 */
/*
 * 修改历史
 * $Log: SCL.java,v $
 * Revision 1.5  2013/08/28 09:18:03  cchun
 * Update:修改getIntAddrInfo()返回值
 *
 * Revision 1.4  2013/08/22 03:43:58  cchun
 * Fix Bug:修复getIntAddrInfo()
 *
 * Revision 1.3  2013/08/20 08:20:36  scy
 * Update：getIntAddrInfo方法返回值增加daName信息
 *
 * Revision 1.2  2013/07/18 13:34:09  cchun
 * Update:增加getIntAddrInfo
 *
 * Revision 1.1  2013/03/29 09:36:35  cchun
 * Add:创建
 *
 * Revision 1.47  2013/01/30 02:50:39  cchun
 * Update:添加getFcdaLNXPath()
 *
 * Revision 1.46  2012/06/04 03:39:23  cchun
 * Update:修复lnInst属性错误，添加getAccessPointName()
 *
 * Revision 1.45  2012/03/22 03:05:19  cchun
 * Fix Bug:修复getBayName()逻辑错误；Update:添加getBayXPath()
 *
 * Revision 1.44  2012/03/09 07:35:55  cchun
 * Update:规范prefix和daName属性用法
 *
 * Revision 1.43  2012/02/21 01:19:19  cchun
 * Update:增加方法getLNAtts（）
 *
 * Revision 1.42  2012/02/16 09:43:54  cchun
 * Fix Bug:修复由于XML数据库替换之后程序不能兼容以前的单线图文件的问题
 *
 * Revision 1.41  2012/02/14 03:47:53  cchun
 * Fix Bug:修复xpath变化导致getEqpPath(),getOwnerBayXPath()错误的问题
 *
 * Revision 1.40  2012/02/01 03:24:44  cchun
 * Update:统一使用带有name()函数的xpath
 *
 * Revision 1.39  2012/01/17 08:50:27  cchun
 * Update:使用更加安全的xpath形式
 *
 * Revision 1.38  2012/01/13 08:37:25  cchun
 * Fix Bug:修复getDOIDesc() xpath不兼容bug
 *
 * Revision 1.37  2012/01/13 00:53:17  cchun
 * Fix Bug:修复getLNXPath() prefix属性为空处理不当问题
 *
 * Revision 1.36  2011/11/30 11:11:43  cchun
 * Update:添加getNodeRef()
 *
 * Revision 1.35  2011/10/18 02:29:21  cchun
 * Refactor:将getEqpPath()和findLN()移动到SCL
 *
 * Revision 1.34  2011/09/08 09:09:26  cchun
 * Update:添加接地名称
 *
 * Revision 1.33  2011/08/31 06:13:10  cchun
 * Update:增加常量
 *
 * Revision 1.32  2011/08/11 06:56:17  cchun
 * Update:添加getLnName()
 *
 * Revision 1.31  2011/08/11 05:47:55  cchun
 * Update:增加FCDA查询辅助方法
 *
 * Revision 1.30  2011/08/10 06:30:40  cchun
 * Update:添加getDOXPath()
 *
 * Revision 1.29  2011/08/01 08:18:06  cchun
 * Update:修改 getLNXPath（）
 *
 * Revision 1.28  2011/02/22 06:14:00  cchun
 * Update:修改getEqpPath()，去掉最前面的'/'符号
 *
 * Revision 1.27  2011/01/21 03:38:27  cchun
 * Update:添加信号关联相关算法
 *
 * Revision 1.26  2011/01/14 02:57:28  cchun
 * Fix Bug:修复getParentXPath()逻辑错误
 *
 * Revision 1.25  2011/01/13 03:23:14  cchun
 * Update:添加getFunListXPath()
 *
 * Revision 1.24  2010/12/20 02:37:52  cchun
 * Update:添加getLnNameInFCDA()
 *
 * Revision 1.23  2010/12/06 05:07:17  cchun
 * Update:添加getEqpPath(),getOwnerBayXPath()
 *
 * Revision 1.22  2010/11/04 02:48:52  cchun
 * Update:整理代码
 *
 * Revision 1.21  2010/09/29 09:30:58  cchun
 * Update:添加getLnName()
 *
 * Revision 1.20  2010/09/08 07:36:04  cchun
 * Update:修改LN0 xpath
 *
 * Revision 1.19  2010/06/28 02:51:48  cchun
 * Update:添加isBusbay()判断是否为母线节点
 *
 * Revision 1.18  2010/06/17 03:08:17  cchun
 * Update:添加getBayName()
 *
 * Revision 1.17  2010/03/29 02:44:47  cchun
 * Update:提交
 *
 * Revision 1.16  2010/03/16 12:17:19  cchun
 * Update: 更新
 *
 * Revision 1.15  2009/12/23 08:13:55  lj6061
 * 删除重复常量类，统一常量引用
 *
 * Revision 1.14  2009/12/11 05:26:48  cchun
 * Update:添加修改历史标记
 *
 * Revision 1.13  2009/11/13 07:18:13  cchun
 * Update:完善关联表格功能
 *
 * Revision 1.12  2009/10/22 06:58:17  wyh
 * 增加scl:SubFunction节点常量
 *
 * Revision 1.11  2009/10/21 03:11:07  cchun
 * Update:将"功能图元"代码中的字符串常量改用静态变量代替
 *
 * Revision 1.10  2009/09/23 07:20:19  cchun
 * Update:允许变电站节点下添加变压器图元
 *
 * Revision 1.9  2009/09/17 09:03:18  cchun
 * Update:封装isBusbarEntry()方法
 *
 * Revision 1.8  2009/09/14 05:46:27  cchun
 * Update:添加获取父节点方法
 *
 * Revision 1.7  2009/09/03 03:05:17  cchun
 * Update:增加节点xpath类型判断方法
 *
 * Revision 1.6  2009/08/31 08:50:03  cchun
 * Update:添加节点类型处理
 *
 * Revision 1.5  2009/08/05 05:47:51  cchun
 * Update:合并代码
 *
 * Revision 1.4  2009/08/03 11:56:56  cchun
 * Update:合并信号关联增加上移、下移功能
 *
 * Revision 1.3.4.1  2009/08/03 11:33:57  cchun
 * Update:为内部信号增加ST,MX过滤功能
 *
 * Revision 1.3  2009/06/04 05:30:41  hqh
 * 添加数据模板变量
 *
 * Revision 1.2  2009/05/18 05:56:57  cchun
 * Update:添加FCDA属性常量
 *
 * Revision 1.1  2009/05/15 08:02:21  cchun
 * Update:修改xpath错误
 *
 */
public class SCL {

	public static final String INST = "inst";
	public static final String DESC = "desc";
	//LN属性
	public static final String LN_PREFIX = "prefix"; 
	public static final String LN_CLASS  = "lnClass";
	//ExtRef属性
	public static final String EXTREF_IEDNAME  = "iedName";
	public static final String EXTREF_LDINST   = "ldInst";
	public static final String EXTREF_LNPREFIX = "prefix";
	public static final String EXTREF_LNCALSS  = "lnClass";
	public static final String EXTREF_LNINST   = "lnInst";
	public static final String EXTREF_DONAME   = "doName";
	public static final String EXTREF_DANAME   = "daName";
	public static final String EXTREF_INTADDR  = "intAddr";
	//FCDA属性
	public static final String FCDA_LDINST  = "ldInst";
	public static final String FCDA_PREFIX  = "prefix";
	public static final String FCDA_LNCLASS = "lnClass";
	public static final String FCDA_LNINST  = "lnInst";
	public static final String FCDA_DONAME  = "doName";
	public static final String FCDA_DANAME  = "daName";
	public static final String FCDA_FC      = "fc";
	
	public static final String XPATH_ROOT = "/scl:SCL";
	public static final String XPATH_HISTORY = "/scl:SCL/scl:Header/scl:History/scl:Hitem";
	public static final String XPATH_SUBNETWORK = "/scl:SCL/scl:Communication/scl:SubNetwork";
	public static final String XPATH_COMMUNICATION = "/scl:SCL/scl:Communication";
	public static final String XPATH_IED = "/scl:SCL/scl:IED";
	public static final String XPATH_ACCESSPOINT = "/scl:SCL/scl:IED/scl:AccessPoint"; 
	public static final String XPATH_DATATYPETEMPLATES = "/scl:SCL/scl:DataTypeTemplates";
	public final static String XPATH_SUBSTATION = "/SCL/Substation";
	public final static String XPATH_HEAD = "/scl:SCL/scl:Header";
	
	public static final String NODE_ST = "Substation";
	public static final String NODE_VL = "VoltageLevel";
	public static final String NODE_BAY = "Bay";
	public static final String NODE_FUNLIST = "FunList";
	public static final String NODE_FUNCTION = "Function";
	public static final String NODE_SUBFUNCTION = "SubFunction";	

	public static final String GROUNDED = "grounded";	

	public static final String[] INPUT_PREFIX_SHR = new String[] {"AISVINGGIO", "BIGOINGGIO", "NIGOINGGIO"};	//公司标准
	public static final String[] INPUT_PREFIX_GW = new String[] {"GOIN", "MXGOIN"};		//国网标准
//	public static enum IO_STANDARD {SHR, GW};
	
	public static final String[] VERSIONS = new String[] {"1.4", "2.0", "3.0", "3.1", "3.5"};
	public static final String[] VOLTAGES = new String[] {"1000", "800", "750", "660", "500", "400", 
		"330", "220", "120", "110", "66", "35", "20", "15", "13", "10", "6", ""};
	public static final int V110_IDX = 9;
	public static final String[] EXCLUDED_DOS = new String[]{"Beh", "Health", "Mod", "NamPlt"};
	public static final String ATT_T = "t";
	public static final String ATT_Q = "q";

//	// 功能限制分类
//	public static final String[] FCS = new String[]{"MX", "ST", "CO", "SP", "SG", "SE"};
	
	/**
	 * 根据ln prefix属性判断当前ln是否为输入端子
	 * @param prefix
	 * @return
	 */
	public static boolean isInputLn(String prefix) {
		return Arrays.binarySearch(INPUT_PREFIX_SHR, prefix) > -1 ||
			Arrays.binarySearch(INPUT_PREFIX_GW, prefix) > -1;
	}
	
	/**
	 * 获取ied xpath
	 * @param iedName
	 * @return
	 */
	public static String getIEDXPath(String iedName) {
		return XPATH_IED + "[@name='" + iedName + "']";
	}
	
	/**
	 * 获取ld xpath
	 * @param iedName
	 * @param ldInst
	 * @return
	 */
	public static String getLDXPath(String iedName, String ldInst) {
		return XPATH_IED + "[@name='" + iedName + "']" + 
			"/scl:AccessPoint/scl:Server/scl:LDevice[@inst='" + ldInst + "']";
	}
	
	/**
	 * 构造LN XPath
	 * @param ln
	 * @return
	 */
	public static String getLNXPath(Element ln) {
		String prefix = ln.attributeValue("prefix");
		String lnClass = ln.attributeValue("lnClass");
		String inst = ln.attributeValue("inst");
		return getLNXPath(prefix, lnClass, inst);
	}
	
	/**
	 * 构造LN XPath
	 * @param fcda
	 * @return
	 */
	public static String getFcdaLNXPath(Element fcda) {
		String prefix = fcda.attributeValue("prefix");
		String lnClass = fcda.attributeValue("lnClass");
		String inst = fcda.attributeValue("lnInst");
		return getLNXPath(prefix, lnClass, inst);
	}
	
	/**
	 * 构造LN XPath
	 * @param prefix
	 * @param lnClass
	 * @param lnInst
	 * @return
	 */
	public static String getLNXPath(String prefix, String lnClass, String lnInst) {
		StringBuilder xpath = new StringBuilder(); 
		if("LLN0".equals(lnClass)) {
			xpath.append("/LN0");
		} else {
			xpath.append("/scl:LN");
			if(StringUtil.isEmpty(prefix))
				xpath.append("[not(@prefix) or @prefix='']");
			else
				xpath.append("[@prefix='" + prefix + "']");
			xpath.append("[@lnClass='" + lnClass + "']");
			if(StringUtil.isEmpty(lnInst))
				xpath.append("[not(@inst) or @inst='']");
			else
				xpath.append("[@inst='" + lnInst + "']");
		}
		return xpath.toString();
	}
	
	/**
	 * 获取LN限制属性。
	 * @param prefix
	 * @param lnClass
	 * @param lnInst
	 * @return
	 */
	public static String getLNAtts(String prefix, String lnClass, String lnInst) {
		StringBuilder xpath = new StringBuilder();
		if(StringUtil.isEmpty(prefix))
			xpath.append("[not(@prefix) or @prefix='']");
		else
			xpath.append("[@prefix='" + prefix + "']");
		xpath.append("[@lnClass='" + lnClass + "']");
		if(StringUtil.isEmpty(lnInst))
			xpath.append("[not(@lnInst) or @lnInst='']");
		else
			xpath.append("[@lnInst='" + lnInst + "']");
		return xpath.toString();
	}
	
	/**
	 * 获取DA限制属性。
	 * @param daName
	 * @return
	 */
	public static String getDaAtts(String daName) {
		return StringUtil.isEmpty(daName) ? 
				"[not(@daName) or @daName='']" : 
				"[@daName='" + daName + "']";
	}
	
	/**
	 * 获取DO xpath
	 * @param doName
	 * @return
	 */
	public static String getDOXPath(String doName) {
		String[] dos = doName.split("\\.");
		StringBuilder sb = new StringBuilder();
		for (String name : dos) {
			sb.append("/*[@name='" + name + "']");
		}
		return sb.toString();
	}

	/**
	 * 遍历取最后的DOI
	 * @param ln
	 * @param doName
	 * @return
	 */
	public static Element getDOI(Element ln, String doName) {
		return DOM4JNodeHelper.selectSingleNode(ln, "." + SCL.getDOXPath(doName));
	}
	
	/**
	 * 获取DOI描述
	 * @param doNode
	 * @param doName
	 * @param daName
	 * @return
	 */
	public static String getDOIDesc(Element doNode, String daName) {
		String doDesc = DOM4JNodeHelper.getAttribute(doNode, "desc");
		if (StringUtil.isEmpty(doDesc)) {
			final Element dUNode = DOM4JNodeHelper.selectSingleNode(doNode, "./*[name()='DAI'][@name='dU']/*[name()='Val']");
			if(null != dUNode)
				doDesc = dUNode.getStringValue();
		}
		if (daName != null) {
			String daXPath = SCL.getDOXPath(daName);
			String daDesc = DOM4JNodeHelper.getAttributeByXPath(doNode, "." + daXPath + "/@desc");
			if (!StringUtil.isEmpty(daDesc)) { // 兼容desc写在DAI上的情况
				doDesc = daDesc;
			} else {
				int pos = daName.lastIndexOf('.');
				if (StringUtil.isEmpty(doDesc) && pos > 0) {
					daXPath = SCL.getDOXPath(daName.substring(0, pos));
					doDesc = DOM4JNodeHelper.getAttributeByXPath(doNode, "." + daXPath + "/@desc");
				}
			}
		}
		return doDesc;
	}
	
	/**
	 * 获取DO下dU值
	 * @param doi
	 * @return
	 */
	public static String getDU(Element doi) {
		Element eleDai = DOM4JNodeHelper.selectSingleNode(doi, "./*[name()='DAI'][@name='dU']/*[name()='Val']");
		return eleDai == null ? "" : eleDai.getStringValue();
	}

	/**
	 * 获取DO指定da的sAddr
	 * @param doi
	 * @param daName
	 * @return
	 */
	public static String getSAddr(Element doi, String daName) {
		if (StringUtil.isEmpty(daName))
			return "";
		return DOM4JNodeHelper.getAttributeByXPath(doi, "." + SCL.getDOXPath(daName) + "/@sAddr");
	}

	/**
	 * 从Element对象获取DO描述
	 * @param DOI
	 * @return
	 */
	public static String getDOIDesc(Element DOI) {
		String desc = DOI.attributeValue("desc");
		if(null == desc) {
			Element dU = DOM4JNodeHelper.selectSingleNode(DOI, "./*[name()='DAI'][@name='dU']/*[name()='Val']");
			if(null != dU)
				desc = dU.getStringValue().trim();
		}
		return desc;
	}
	
	/**
	 * 获取指定LN的指定DO的描述
	 * @param ldevice
	 * @param doName
	 * @param lnPrefix
	 * @param lnClass
	 * @param lnInst
	 * @return
	 */
	public static String getDoDesc(Element ldevice, String doName,
			String lnPrefix, String lnClass, String lnInst) {
		String desc = null;
		String lnXpath = "." + SCL.getLNXPath(lnPrefix, lnClass, lnInst); 
		String doXpath = null;
		if (doName.contains(".")) {
			String[] doNames = doName.split("\\.");
			doXpath = lnXpath;
			for (String doNa : doNames) {
				doXpath = doXpath + "/*[@name='" + doNa + "']";
			}
		} else {
			doXpath = lnXpath + "/*[@name='" + doName + "']";
		}
		
		Element DOI = DOM4JNodeHelper.selectSingleNode(ldevice, doXpath);
		if (DOI == null) {// 当LN下不存在DOI时,它的描述文件就在当前的LN下的desc属性中
			Element ln = DOM4JNodeHelper.selectSingleNode(ldevice, lnXpath);
			if (ln != null)
				desc = ln.attributeValue("desc"); //$NON-NLS-1$
		} else {
			desc = getDOIDesc(DOI);
		}
		return desc;
	}
	
	/**
	 * 根据DO XPath获取描述
	 * @param doXPath
	 * @return
	 */
	public static String getDOIDesc(String doXPath) {
		return getDOIDesc(XMLDBHelper.selectSingleNode(doXPath));
	}

	/**
	 * 根据FCDA或者ExtRef获取参引值
	 * @param data
	 * @return
	 */
	public static String getNodeRef(Element data) {
		String ldInst = DOM4JNodeHelper.getAttribute(data, SCL.EXTREF_LDINST);
		String prefix = DOM4JNodeHelper.getAttribute(data, SCL.EXTREF_LNPREFIX);
		String lnClass = DOM4JNodeHelper.getAttribute(data, SCL.EXTREF_LNCALSS);
		String lnInst = DOM4JNodeHelper.getAttribute(data, SCL.EXTREF_LNINST);
		String doName = DOM4JNodeHelper.getAttribute(data, SCL.EXTREF_DONAME);
		String daName = DOM4JNodeHelper.getAttribute(data, SCL.EXTREF_DANAME);
		if("".equals(ldInst)) {
			return "";
		} else {
			if(StringUtil.isEmpty(daName))
				return ldInst + "/" + 
					prefix + lnClass + lnInst + "." + doName;
			else
				return ldInst + "/" + 
					prefix + lnClass + lnInst + "." +
					doName + "." + daName;
		}
	}
	
	/**
	 * 根据ExtRef获取参引值
	 * @param data
	 * @return
	 */
	public static String getNodeFullRef(Element data) {
		String iedName = DOM4JNodeHelper.getAttribute(data, SCL.EXTREF_IEDNAME);
		String ldInst = DOM4JNodeHelper.getAttribute(data, SCL.EXTREF_LDINST);
		String prefix = DOM4JNodeHelper.getAttribute(data, SCL.EXTREF_LNPREFIX);
		String lnClass = DOM4JNodeHelper.getAttribute(data, SCL.EXTREF_LNCALSS);
		String lnInst = DOM4JNodeHelper.getAttribute(data, SCL.EXTREF_LNINST);
		String doName = DOM4JNodeHelper.getAttribute(data, SCL.EXTREF_DONAME);
		String daName = DOM4JNodeHelper.getAttribute(data, SCL.EXTREF_DANAME);
		if("".equals(ldInst)) {
			return "";
		} else {
			if(StringUtil.isEmpty(daName))
				return iedName + ldInst + "/" + 
					prefix + lnClass + lnInst + "." + doName;
			else
				return iedName + ldInst + "/" + 
					prefix + lnClass + lnInst + "." +
					doName + "." + daName;
		}
	}
	
	public static String getNodeDoDa(Element data) {
		String doName = DOM4JNodeHelper.getAttribute(data, SCL.EXTREF_DONAME);
		String daName = DOM4JNodeHelper.getAttribute(data, SCL.EXTREF_DANAME);
		return StringUtil.isEmpty(daName) ? doName : doName + "." + daName;
	}
	
	/**
	 * 获取设备所在间隔xpath
	 * @param eqpXpath
	 * @return
	 */
	public static String getOwnerBayXPath(String eqpXpath) {
		String[] parts = eqpXpath.split("/");
		if (parts.length < 4)
			return null;
		String bayXpath = "";
		boolean findBay = false;
		for (String part : parts) {
			if (part.length() < 1)
				continue;
			bayXpath += "/" + part;
			if (matchName(part, "Bay")) {
				findBay = true;
				break;
			}
		}
		return findBay ? bayXpath : null;
	}
	
	public static boolean matchName(String part, String name) {
		return !StringUtil.isEmpty(part) && 
				(part.startsWith("*[name()='" + name + "']") || part.startsWith(name) || part.startsWith("scl:" + name));
	}
	
	/**
	 * 将xpath转成61850设备path
	 * @param eqpXpath
	 * @return
	 */
	public static String getEqpPath(String eqpXpath) {
		StringBuilder temp = new StringBuilder(); //$NON-NLS-1$
		String[] nodes = eqpXpath.split("/");
		int len = nodes.length;
		if (len < 3) {
			SCTLogger.error("错误的xpath格式：" + eqpXpath + "！");
			return null;
		}
		for (int i=2; i<nodes.length; i++) {
			String node = nodes[i];
			int begin = node.indexOf("@name=");
			if ("".equals(node) || begin < 0)
				continue;
			int end = node.lastIndexOf("'");
			temp.append(node.substring(begin + 7, end));
			if (i < nodes.length - 1)
				temp.append("/");
		}
		return temp.toString();
	}
	
	/**
	 * 获取设备路径信息
	 * @param lnode
	 * @return
	 */
	public static String getEqpPath(Element lnode) {
		Element parent = lnode.getParent();
		String eqpPath = "";
		while(parent != null) {
			String name = parent.attributeValue("name", "");
			eqpPath = "/" + name + eqpPath;
			parent = parent.getParent();
		}
		return eqpPath;
	}

	/**
	 * 从替换文件中获取在ied下满足特定条件的LN以及LN0
	 * @param iedNode
	 * @param ldInst
	 * @param ln
	 * @return
	 */
	public static Element findLN(Element iedNode, String ldInst, LNInfo ln) {
		StringBuffer sbXPath = new StringBuffer("./*[name()='AccessPoint']/*[name()='Server']");
		sbXPath.append("/*[name()='LDevice'][@inst='" + ldInst + "']");
		sbXPath.append(SCL.getLNXPath(ln.getPrefix(), ln.getLnClass(), ln.getInst()));
		return DOM4JNodeHelper.selectSingleNode(iedNode, sbXPath.toString());
	}
	
	/**
	 * 根据间隔下节点xpath获取间隔名称
	 * @param xpath
	 * @return
	 */
	public static String getBayName(String xpath) {
		String[] parts = xpath.split("/");
		for (String part : parts) {
			if (matchName(part, "Bay")) {
				int p = part.indexOf("@name=");
				p = p+"@name=".length()+1;
				return part.substring(p, part.indexOf('\'', p));
			}
		}
		return null;
	}
	
	/**
	 * 从ied xpath获取ied name
	 * @param xpath
	 * @return
	 */
	public static String getIEDName(String xpath) {
		String att = xpath.substring(xpath.lastIndexOf('['));
		return att.split("\'")[1];
	}
	
	/**
	 * 判断该xpath对应的节点是否为变电站
	 * @param xpath
	 * @return 是为true，反之为false
	 */
	public static boolean isSubstationNode(String xpath) {
		return getBelongTo(xpath, NODE_ST);
	}
	
	/**
	 * 判断该xpath对应的节点是否为电压等级
	 * @param xpath
	 * @return 是为true，反之为false
	 */
	public static boolean isVoltageLevelNode(String xpath) {
		return getBelongTo(xpath, NODE_VL);
	}
	
	/**
	 * 判断该xpath对应的节点是否为间隔
	 * @param xpath
	 * @return 是为true，反之为false
	 */
	public static boolean isBayNode(String xpath) {
		return getBelongTo(xpath, NODE_BAY);
	}
	
	/**
	 * 判断该xpath对应的节点是否为某种节点
	 * @param xpath
	 * @param ndType
	 * @return
	 */
	private static boolean getBelongTo(String xpath, String ndType) {
		String[] parts = xpath.split("/");
		return (parts.length>0) && matchName(parts[parts.length-1], ndType);
	}
	
	/**
	 * 得到当前节点的父节点xpath
	 * @param xpath
	 * @return
	 */
	public static String getParentXPath(String xpath) {
		int idx = xpath.lastIndexOf('/');
		if(idx < 0)
			return null;
		else
			return xpath.substring(0, idx);
	}
	
	/**
	 * 根据节点名称判断是否为母线间隔
	 * @param entry
	 * @return
	 */
	public static boolean isBusbarEntry(INaviTreeEntry entry) {
		if(!(entry instanceof BayEntry))
			return false;
		return entry.getName().startsWith(DefaultInfo.BUSBAR_NAME) 
					|| ((BayEntry)entry).isBusbar();
	}
	
	/**
	 * 判断当前节点是否为母线
	 * @param any
	 * @return
	 */
	public static boolean isBusbay(Element any){
		String name = any.getName();
		if (!"Bay".equalsIgnoreCase(name))
			return false;
		List<?> lstChild = any.elements("Private");
		for (Object obj : lstChild) {
			Element ele = (Element) obj;
			if (ele == null)
				continue;
			String attr = ele.attributeValue("type");
			if ("Busbar".equalsIgnoreCase(attr)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 根据LN节点获取ln名称
	 * @param ln
	 * @return
	 */
	public static String getLnName(Element ln) {
		String prefix = ln.attributeValue("prefix");
		String lnClass = ln.attributeValue("lnClass");
		String inst = ln.attributeValue("inst");
		return getLnName(prefix, lnClass, inst);
	}
	
	/**
	 * 根据FCDA节点获取ln名称
	 * @param fcda
	 * @return
	 */
	public static String getLnNameInFCDA(Element fcda) {
		String prefix = fcda.attributeValue("prefix");
		String lnClass = fcda.attributeValue("lnClass");
		String inst = fcda.attributeValue("lnInst");
		return getLnName(prefix, lnClass, inst);
	}
	
	public static String getLnName(String prefix, String lnClass, String lnInst) {
		return StringUtil.nullToEmpty(prefix) + lnClass + StringUtil.nullToEmpty(lnInst);
	}
	
	/**
	 * 获取CB参引
	 * @param iedName
	 * @param ldInst
	 * @param cbName
	 * @param block
	 * @return
	 */
	public static String getCbRef(String iedName, String ldInst, String cbName, EnumCtrlBlock block) {
		if (StringUtil.isEmpty(cbName)) {
			return iedName + ldInst + "/LLN0";
		}
		return iedName + ldInst + "/LLN0$" + block.getFc() + "$" + cbName;
	}
	
	public static String getCbRef(String ldInst, String cbName, EnumCtrlBlock block) {
		return getCbRef("", ldInst, cbName, block);
	}
	
	/**
	 * 查找集合中功能列表xpath
	 * @param xpathes
	 * @return
	 */
	public static String getFunListXPath(List<String> xpathes) {
		String funListXPath = null;
		for (String xpath : xpathes) {
			if (xpath.contains(SCL.NODE_FUNLIST)) {
				funListXPath = xpath;
				break;
			}
		}
		return funListXPath;
	}

	/**
	 * 是否为连接用FC
	 * @param fc
	 * @return
	 */
	public static boolean isConnectFC(String fc) {
		return "ST".equals(fc) || "MX".equals(fc);
	}
	
	/**
	 * 判断是否为排除掉的IO类型DO
	 * @param doName
	 * @return
	 */
	public static boolean isExcludeDo(String doName) {
		return "Mod".equals(doName) || "Beh".equals(doName)
			|| "Health".equals(doName) || "NamPlt".equals(doName);
	}
	
	/**
	 * 判断是否为排除掉的IO类型DA
	 * @param daName
	 * @return
	 */
	public static boolean isExcludeDa(String daName) {
		return "q".equals(daName) || "t".equals(daName);
	}
	
	/**
	 * 获取xpath新格式
	 * @param xpath
	 * @return
	 */
	public static String getNewPattern(String xpath) {
		String[] arr = xpath.split("/");
		StringBuilder sb = new StringBuilder();
		for (String s : arr) {
			if ("".equals(s))
				continue;
			if (s.indexOf("scl:") == 0) {
				int pos = s.indexOf("[");
				int end = pos < 0 ? s.length() : pos;
				String ndName = s.substring(4, end);
				String att = "";
				if (end < s.length()) {
					att = s.substring(end);
				}
				s = "*[name()='" + ndName + "']" + att;
			}
			sb.append("/").append(s);
		}
		return sb.toString();
	}
	
	public static String getAccessPointName(String iedName, String ldInst) {
		if (Constants.XQUERY) {
			return XMLDBHelper.queryAttribute("let $root:="
					+ XMLDBHelper.getDocXPath()
					+ " for $accpoint in $root/scl:SCL/scl:IED[@name='"
					+ iedName + "']/scl:AccessPoint"
					+ " where exists($accpoint/scl:Server/scl:LDevice[@inst='"
					+ ldInst + "'])" + " return $accpoint/@name/string()");
		} else {
			String select = SCL.getIEDXPath(iedName) + "/scl:AccessPoint[count(./scl:Server/scl:LDevice[@inst='"
					+ ldInst + "'])>0]/@name";
			return XMLDBHelper.getAttributeValue(select);
		}
	}
	
	/**
	 * 获取intAddr信息
	 * @param addr
	 * @return
	 */
	public static String[] getIntAddrInfo(String addr) {
		if(StringUtil.isEmpty(addr)) {
			return new String[0];
		}
		if (addr.indexOf(":") > -1) {
			if (addr.endsWith(":")) {
				return new String[0];// 只有物理端口
			} else {
				addr = addr.split(":")[1];
			}
		}
		String[] daAddr = addr.split("/|\\.");
		String ldInst = daAddr[0];
		String[] lnInfo = SCLUtil.getLNInfo(daAddr[1]);
		String prefix = lnInfo[0];
		String lnClass = lnInfo[1];
		String lnInst = lnInfo[2];
		
		String doDa = "";
		int len = daAddr.length;
		for (int i=2; i<len; i++) {
			if (!"".equals(doDa))
				doDa += ".";
			doDa += daAddr[i];
		}
		return new String[] {ldInst, prefix, lnClass, lnInst, doDa};
	}
	/**
	 * 获取intAddr信息
	 * @param addr
	 * @return
	 */
	public static String[] getIntAddrInfo(String addr, String iedname) {
		if(StringUtil.isEmpty(addr)) {
			return new String[0];
		}
		if(addr.contains(":"))
			addr = addr.substring(addr.indexOf(":") + 1);
		String[] daAddr = addr.split("/|\\.");
		String ldInst = daAddr[0].replace(iedname, "");
		String[] lnInfo = SCLUtil.getLNInfo(daAddr[1]);
		String prefix = lnInfo[0];
		String lnClass = lnInfo[1];
		String lnInst = lnInfo[2];
		
		String doDa = "";
		int len = daAddr.length;
		for (int i=2; i<len; i++) {
			if (!"".equals(doDa))
				doDa += ".";
			doDa += daAddr[i];
		}
		return new String[] {ldInst, prefix, lnClass, lnInst, doDa};
	}
}
