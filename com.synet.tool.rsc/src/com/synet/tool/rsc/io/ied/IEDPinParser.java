package com.synet.tool.rsc.io.ied;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Element;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.ui.progress.IElementCollector;

import com.shrcn.business.scl.SCTProperties;
import com.shrcn.business.scl.check.SignalsChecker;
import com.shrcn.business.scl.model.SCL;
import com.shrcn.business.scl.model.intree.IInnerTreeEntry;
import com.shrcn.business.scl.model.intree.IInnerTreeEntry.DA_FC;
import com.shrcn.business.scl.model.intree.InTreeEntry;
import com.shrcn.found.common.log.SCTLogger;
import com.shrcn.found.file.xml.DOM4JNodeHelper;
import com.shrcn.found.ui.util.DialogHelper;
import com.shrcn.found.ui.util.ImageConstants;
import com.shrcn.found.xmldb.XMLDBHelper;
import com.shrcn.tool.found.das.BeanDaoService;
import com.shrcn.tool.found.das.impl.BeanDaoImpl;
import com.synet.tool.rsc.DBConstants;
import com.synet.tool.rsc.RSCProperties;
import com.synet.tool.rsc.io.parser.ParserUtil;
import com.synet.tool.rsc.model.Tb1006AnalogdataEntity;
import com.synet.tool.rsc.model.Tb1016StatedataEntity;
import com.synet.tool.rsc.model.Tb1046IedEntity;
import com.synet.tool.rsc.model.Tb1062PinEntity;
import com.synet.tool.rsc.util.F1011_NO;
import com.synet.tool.rsc.util.Rule;

public class IEDPinParser {
	
	//数据模板DO信息缓存
	private Map<String, Element> doNodeCache = new HashMap<String, Element>();
	//数据模板LNodeType信息缓存
	private Map<String, Element> lnTypeCache = new HashMap<String, Element>();
	private boolean onlyGGIO = true;
	private boolean loadAll = false;
	private String currIEDName = null;
	
	//记录ln，do，da的名字
	private String lnName = null;
	private String doName = null;
	private String doDesc = null;
	private String daName = null;
	
	//查询出来的intAddr信息缓存
	private List<String> innerAddrs = new ArrayList<String>();
	
	private Context context;
	private List<Tb1062PinEntity> pins = new ArrayList<>();
	private Tb1046IedEntity iedResv;
	private BeanDaoService beanDao = BeanDaoImpl.getInstance();
	private RSCProperties rscp = RSCProperties.getInstance();

	private static final MessageFormat LNODETYPE_PATH = new MessageFormat("/scl:SCL/scl:DataTypeTemplates/scl:LNodeType[@id=''{0}'']");
	private static final MessageFormat DOTYPE_PATH = new MessageFormat("/scl:SCL/scl:DataTypeTemplates/scl:DOType[@id=''{0}'']");
	private static final MessageFormat DATYPE_PATH = new MessageFormat("/scl:SCL/scl:DataTypeTemplates/scl:DAType[@id=''{0}'']");

	/**
	 * 单例模式私有构造函数
	 */
	public IEDPinParser(Tb1046IedEntity iedResv,Context context){
		this.iedResv = iedResv;
		this.currIEDName = iedResv.getF1046Name();
		this.context = context;
	}

	private String getAttribute(Element node, String attName) {
		return node.attributeValue(attName);
	}
	
	public IInnerTreeEntry createLDTreeEntry(Element ld) {
		String ldInst = ld.attributeValue("inst");
		String ldXPath = "/scl:SCL/scl:IED[@name='" + currIEDName + 
				"']/scl:AccessPoint/scl:Server/scl:LDevice[@inst='" + ldInst + "']";
		String xpath = "./*/scl:Inputs/scl:ExtRef";
		List<Element> extRefs = DOM4JNodeHelper.selectNodes(ld, xpath);
		if(innerAddrs.size()>0) {
			innerAddrs.clear();
		}
		if (extRefs.size()>0) {
			for (Element ele : extRefs) {
				innerAddrs.add(ele.attributeValue("intAddr"));
			}
		}
		String desc = getAttribute(ld, "desc");
		IInnerTreeEntry entryLD = new InTreeEntry(ldInst, ldXPath, 
				ImageConstants.LDEVICE, ldInst, IInnerTreeEntry.LD_ENTRY);
		entryLD.setDesc(desc);
		fillLDTreeData(ld, entryLD);
		return entryLD;
	}

	/**
	 * 为指定LD节点添加子节点(LN)信息
	 * @param elLD scd文件LDevice节点
	 * @param entryLD LD树节点
	 */
	private void fillLDTreeData(Element elLD, IInnerTreeEntry entryLD) {
		SCTProperties proper = SCTProperties.getInstance();
		String ldXPath = entryLD.getXPath();
		String ldName = entryLD.getName();
		List<?> lns = elLD.elements();
		int num = 1;
		for (Object obj : lns) {
			Element ln = (Element) obj;
			String tagName = ln.getName();
			if (!"LN0".equals(tagName) && !"LN".equals(tagName))
				continue;
			String prefix = getAttribute(ln, "prefix");
			String lnClass = getAttribute(ln, "lnClass");
			String inst = getAttribute(ln, "inst");
			String lnType = getAttribute(ln, "lnType");
			String desc = getAttribute(ln, "desc");

			if (onlyGGIO && !proper.isVTLnClass(lnClass))
				continue;
			
			String name = SCL.getLnName(ln);
			String xpath = ldXPath + SCL.getLNXPath(ln);
			
			IInnerTreeEntry entryLN = new InTreeEntry(name, xpath, ImageConstants.LNODE, name, IInnerTreeEntry.LN_ENTRY, num);
			num ++;
			entryLN.setDesc(desc);
			entryLN.setDataType(lnType);
			entryLN.setAttribute(IInnerTreeEntry.LN_PRIFIX, prefix);
			entryLN.setAttribute(IInnerTreeEntry.LN_CLASS, lnClass);
			entryLN.setAttribute(IInnerTreeEntry.LN_INST, inst);
			entryLD.addChild(entryLN);
			
			if ("GOIN".equals(prefix) || "SVIN".equals(prefix)) {
				fillLNTreeData(ldName, entryLN, null, null);
			}
		}
	}
	
	private Element getLNNodeByEntry(IInnerTreeEntry entryLN) {
		IInnerTreeEntry entryLD = (IInnerTreeEntry) entryLN.getParent();
		Element ndLD = XMLDBHelper.selectSingleNode(entryLD.getXPath());
		String prefix = entryLN.getAttribute(IInnerTreeEntry.LN_PRIFIX);
		String lnClass = entryLN.getAttribute(IInnerTreeEntry.LN_CLASS);
		String inst = entryLN.getAttribute(IInnerTreeEntry.LN_INST);
		return DOM4JNodeHelper.selectSingleNode(ndLD, "." + SCL.getLNXPath(prefix, lnClass, inst));
	}
	
	/**
	 * 为指定LN节点添加子节点信息
	 * @param entryLN LN节点对象
	 */
	public void fillLNTreeData(String ldName, IInnerTreeEntry entryLN, IElementCollector collector, IProgressMonitor monitor) {
		String xpath = entryLN.getXPath();
		String dataType = entryLN.getDataType();
		Element lnNode = getLNNodeByEntry(entryLN);
		lnName = entryLN.getName();
		String name = null;
		IInnerTreeEntry entryLN_FC = null; 
		if(null != monitor)
			monitor.beginTask("Loading", DA_FC.values().length);
		clearCaches();
		for (DA_FC fc : DA_FC.values()) {
			if (!isLoadAll() && (fc != DA_FC.ST && fc != DA_FC.MX))
				continue;
			name = fc.toString();
			entryLN_FC = new InTreeEntry(name, xpath, (name.equals("ST")||name.equals("MX"))?ImageConstants.DRAGABLE:ImageConstants.FC, 
					name, IInnerTreeEntry.LN_FC_ENTRY);
			entryLN_FC.setDataType(dataType);
			if (!fillLNByType(ldName, entryLN_FC, lnNode, fc))
				return;
			if (entryLN_FC.getChildren().size() > 0) {
				entryLN.addChild(entryLN_FC);
				if (null != monitor) {
					monitor.worked(1);
					if (null != collector)
						collector.add(entryLN_FC, monitor);
				}
			}
		}
		if (null != monitor)
			monitor.done();
	}
	
	/**
	 * 按照功能约束(fc)为LN树节点添加子节点(DOI)信息
	 * @param entryLNFC
	 * @param lnNode
	 * @param dafc
	 */
	private boolean fillLNByType(String ldName, IInnerTreeEntry entryLNFC, Element lnNode, DA_FC dafc) {
		String lnXPath = entryLNFC.getXPath();
		String lntString = entryLNFC.getDataType();
		List<Element> dois = DOM4JNodeHelper.selectNodes(lnNode, "./*[name()='DOI']");
		for(Element doi:dois) {
			String name = getAttribute(doi, "name");
			String desc = SCL.getDOIDesc(doi);
			if(!isLoadAll() && SignalsChecker.isExcludedDO(name))
				continue;
			IInnerTreeEntry entryDOI = new InTreeEntry(name, 
					lnXPath + "/scl:DOI[@name='" + name + "']",ImageConstants.DO,			
					name, IInnerTreeEntry.DO_ENTRY);
			doName = name;
			doDesc = desc;
			String ref = ldName + "/" + lnName + "." + doName;
			setSglRef(entryDOI,ref);
			
			Element lnTypeNode = getLnTypeNode(lntString);
			if(null == lnTypeNode) {
				DialogHelper.showAsynError("类型为" + lntString + "的LN缺少定义.");
				return false;
			}
			Element doNode = DOM4JNodeHelper.selectSingleNode(lnTypeNode, 
								"./*[name()='DO'][@name='" + name + "']");
			if(null == doNode) {
				DialogHelper.showAsynError("类型为" + lntString + "的LN下不存在名为" + name + "的DO.");
				return false;
			}
			String type = doNode.attributeValue("type");
			entryDOI.setDataType(type);
			entryDOI.setDesc(desc);
			fillDOTreeData(ldName, entryDOI, dafc);
			
			if(entryDOI.getChildren().size() > 0)
				entryLNFC.addChild(entryDOI);
		}
		// 为适应LN下无实例化后的DOI的情况，特增加从数据模板处获取内部信号值
		if(dois.size() == 0) {
			String lnType = getAttribute(lnNode, "lnType");
			Element lnTypeNode = getLnTypeNode(lnType);
			dois = DOM4JNodeHelper.selectNodes(lnTypeNode, "./*[name()='DO']");
			for (Element DO : dois) {
				String name = getAttribute(DO, "name");
				String desc = getAttribute(DO, "desc");
				IInnerTreeEntry entryDOI = new InTreeEntry(name, 
						lnXPath + "/scl:DOI[@name='" + name + "']",ImageConstants.DO,			
						name, IInnerTreeEntry.DO_ENTRY);
				doName = name;
				doDesc = desc;
				String ref = ldName + "/" + lnName + "." + doName;
				setSglRef(entryDOI,ref);
				
				String type = getAttribute(DO, "type");
				entryDOI.setDataType(type);
				entryDOI.setDesc(desc);
				fillDOTreeData(ldName, entryDOI, dafc);
				if(entryDOI.getChildren().size() > 0)
					entryLNFC.addChild(entryDOI);
			}
		}
		return true;
	}
	
	/**
	 * 根据lnType属性获取数据模板中LNodeType节点
	 * @param lnType
	 * @return LNodeType节点
	 */
	private Element getLnTypeNode(String lnType) {
		String lnTypeXPath = LNODETYPE_PATH.format(new Object[]{lnType});
		if(lnTypeCache.containsKey(lnTypeXPath))
			return lnTypeCache.get(lnTypeXPath);
		Element lnTypeNode = XMLDBHelper.selectSingleNode(lnTypeXPath);
		lnTypeCache.put(lnTypeXPath, lnTypeNode);
		return lnTypeNode;
	}
	
	private Element getDoTypeNode(String doType) {
		String doTypeXPath = DOTYPE_PATH.format(new Object[]{doType});
		Element dotype = null;
		if(doNodeCache.containsKey(doTypeXPath))
			dotype = doNodeCache.get(doTypeXPath).createCopy();
		else {
			dotype = XMLDBHelper.selectSingleNode(doTypeXPath);
			doNodeCache.put(doTypeXPath, dotype.createCopy());
		}
		return dotype;
	}
	
	private Element getStructTypeNode(String doType) {
		String structDAPath = DATYPE_PATH.format(new Object[]{doType});
		Element dotype = null;
		if(doNodeCache.containsKey(structDAPath))
			dotype = doNodeCache.get(structDAPath).createCopy();
		else {
			dotype = XMLDBHelper.selectSingleNode(structDAPath);
			doNodeCache.put(structDAPath, dotype.createCopy());
		}
		return dotype;
	}
	
	/**
	 * 清除缓存
	 */
	public void clearCaches() {
		lnTypeCache.clear();
		doNodeCache.clear();
	}

	
	/**
	 * 为指定DO节点添加子节点信息
	 * @param entryDO DO节点对象
	 */
	private void fillDOTreeData(String ldName, IInnerTreeEntry entryDO, DA_FC dafc) {
		String dataType = entryDO.getDataType();
		Element dotype = getDoTypeNode(dataType);
		// 判断DO是否存在
		if(null == dotype) {
			SCTLogger.error("类型为" + dataType + "的DO缺少定义！");
			return;
		}
		if(entryDO.getDesc() == null) {
			String desc = getAttribute(dotype, "desc");
			entryDO.setDesc(desc);
		}
		String doTypeXPath = DOTYPE_PATH.format(new Object[]{dataType});
		
		List<?> dais = dotype.elements();
		for (Object obj : dais) {
			Element dai = (Element) obj;
			String daType = dai.getName();
			String name = getAttribute(dai, "name");
			String bType = getAttribute(dai, "bType");
			IInnerTreeEntry entryDAI = null;
			if("DA".equals(daType)) {
				String fc = getAttribute(dai, "fc");
				if(!dafc.toString().equals(fc))
					continue;
				entryDAI = new InTreeEntry(name, doTypeXPath + "/scl:DA[@name='" + name + 
						"']", ImageConstants.INPUT_SIG, name, IInnerTreeEntry.DA_ENTRY);
				
				daName = name;
				String ref = ldName + "/" + lnName + "." + doName + "." + daName;
				setSglRef(entryDAI,ref);
				
				entryDO.addChild(entryDAI);
				if(isStruct(bType)) {  // 结构体
					entryDAI.setDataType(getAttribute(dai, "type"));
					fillStructTreeData(ldName, entryDAI, dafc);
					entryDO.removeChild(entryDAI);
				} else {			  // 简单类型
					entryDAI.setDataType(bType);
					addPin(ldName, dafc, entryDAI.isHavaSglRef() ? 1 : 0);
				}
			}
			if("SDO".equals(daType)) {
				entryDAI = new InTreeEntry(name, doTypeXPath + "/scl:SDO[@name='" + name + 
						"']", ImageConstants.INPUT_SIG, name, IInnerTreeEntry.SDO_ENTRY);
				entryDAI.setDataType(getAttribute(dai, "type"));
				fillDOTreeData(ldName, entryDAI, dafc);
				if(entryDAI.getChildren().size() > 0)
					entryDO.addChild(entryDAI);
			}
		}
	}
	
	/**
	 * 解析结构体数据
	 * @param entryDO
	 * @param dafc
	 */
	private void fillStructTreeData(String ldName, IInnerTreeEntry entryDO, DA_FC dafc) {
		String dataType = entryDO.getDataType();
		Element structType = getStructTypeNode(dataType);
		if(null == structType) {
			SCTLogger.error("类型为" + dataType + "的DA缺少定义！");
			return;
		}
		if(entryDO.getDesc() == null) {
			String desc = getAttribute(structType, "desc");
			entryDO.setDesc(desc);
		}
		String structDAPath = DATYPE_PATH.format(new Object[]{dataType});
		List<?> dais = structType.elements("BDA");
		for (Object obj : dais) {
			Element dai = (Element) obj;
			String name = getAttribute(dai, "name");
			String bType = getAttribute(dai, "bType");
			name = entryDO.getName().concat("." + name);
			InTreeEntry entryDAI = null;
			if(isStruct(bType)) {
				entryDAI = new InTreeEntry(name, structDAPath + "/scl:BDA[@name='" + name + 
						"']", ImageConstants.INPUT_SIG, name, IInnerTreeEntry.DA_ENTRY);
				daName = name;
				String ref = ldName + "/" + lnName + "." + doName + "." + daName;
				setSglRef(entryDAI,ref);
				
				entryDAI.setDataType(getAttribute(dai, "type"));
				entryDO.getParent().addChild(entryDAI);	//放到这里是为了避免空指针
				fillStructTreeData(ldName, entryDAI, dafc);
				entryDO.getParent().removeChild(entryDAI);
			} else {
				//为了和DO相区分，属性之间直接用"."分隔，而不是再加一级树节点
//				name = entryDO.getName().concat("." + name);
				entryDAI = new InTreeEntry(name, structDAPath + "/scl:BDA[@name='" + name + 
						"']", ImageConstants.INPUT_SIG, name, IInnerTreeEntry.DA_ENTRY);
				daName = name;
				String ref = ldName+"/"+lnName+"."+doName+"."+daName;
				setSglRef(entryDAI,ref);
				addPin(ldName, dafc, entryDAI.isHavaSglRef() ? 1 : 0);
				
				entryDAI.setDataType(bType);
				entryDO.getParent().addChild(entryDAI);
//				InTreeEntry entryDAI = (InTreeEntry) entryDO;
//				String newName = entryDO.getName().concat("." + name);
//				entryDAI.setName(newName);
//				entryDAI.setToolTip(newName);
//				entryDAI.setXpath(structDAPath + "/scl:BDA[@name='" + name + "']");
			}
//			entryDO.getParent().addChild(entryDAI);
//			structType.remove(dai);
		}
	}
	
	public boolean isStruct(String bType) {
		return "Struct".equals(bType);
	}

	public boolean isOnlyGGIO() {
		return onlyGGIO;
	}

	public void setOnlyGGIO(boolean onlyGGIO) {
		this.onlyGGIO = onlyGGIO;
	}

	public boolean isLoadAll() {
		return loadAll;
	}

	public void setLoadAll(boolean loadAll) {
		this.loadAll = loadAll;
	}
	
	private void setSglRef(IInnerTreeEntry entry,String ref){
		if(innerAddrs.contains(ref)){
			entry.setHaveSglRef(true);
		}else{
			entry.setHaveSglRef(false);
		}
	}

	private String getRef(String ldName, String fc) {
		if ("q".equals(daName) || "t".equals(daName)) {
			return null;
		}
		String doRef = ldName + "/" + lnName + "$" + fc + "$" + doName;
		return "MX".equals(fc) ? doRef : (doRef + "$" + daName);
	}

	private String getIntAddr(String ldName, String fc) {
		String doAddr = ldName + "/" + lnName + "." + doName;
		return "MX".equals(fc) ? doAddr : (doAddr + "." + daName);
	}
	
	private void addPin(String ldName, DA_FC dafc, int f1062IsUsed) {
		String fc = dafc.name();
		String pinRefAddr = getRef(ldName, fc);
		if (pinRefAddr == null) {
			return;
		}
		Rule type = F1011_NO.getType("", lnName, doName, doDesc, fc);
		Tb1062PinEntity pin = ParserUtil.createPin(iedResv, pinRefAddr, doDesc, type.getId(), f1062IsUsed);
//		System.out.println(pinRefAddr + ":" + doDesc);
		context.cachePin(currIEDName + getIntAddr(ldName, fc), pin);
		pins.add(pin);
	}
	
	public void savePins() {
		beanDao.insertBatch(pins);
	}
}
