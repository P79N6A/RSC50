package com.shrcn.business.scl.das;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Attribute;
import org.dom4j.Element;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;

import com.shrcn.business.scl.model.SCL;
import com.shrcn.business.scl.util.SCLUtil;
import com.shrcn.found.common.util.StringUtil;
import com.shrcn.found.common.util.TimeCounter;
import com.shrcn.found.file.xml.DOM4JNodeHelper;
import com.shrcn.found.ui.util.TaskManager;
import com.shrcn.found.xmldb.XMLDBHelper;

public class UserCfgDAO {
	
	private List<Element> ldEls = null;
	private Map<Integer, List<String>> ldMap = new HashMap<Integer, List<String>>();// Key:ld的Index
	private Element dataTemplate;
	private Map<String, String> cdcMap = new HashMap<String, String>();

	private static final String PROT_SV1 = "dsSetting";//"dsRelayVal";
	private static final String PROT_SV2 = "dsParameter";//"dsRelayVal";
	private static final String PROT_SV3 = "dsRelayEna";//TODO：保护定值是否包含功能压板信息
	private static final String WARNING_1 = "dsWarning";
	private static final String WARNING_2 = "dsAlarm";
	private static final String PROT_SIGNAL1 = "dsTripInfo";
	private static final String PROT_SIGNAL2 = "dsRelayRec";//TODO：保护录波是否算为保护信号
	private static final String AIN_1 = "dsAin";
	private static final String AIN_2 = "dsRelayAin";
	private static final String DIN_1 = "dsDin";
	private static final String DIN_2 = "dsRelayDin";
	private static final String LOCK = "dsInterLock";
//	private Map<String, Map<String, String>> suitMap = new HashMap<String,  Map<String, String>>();
	private Map<String, String> unitMap = new HashMap<String, String>();//TODO：由于单位信息为规范直接规定信息，因此只解析一次（不考虑不同版本规范可能产生的不一致问题）。
	
	/**
	 * 构造函数
	 * 
	 * @param currIedName
	 */
	public UserCfgDAO(String currIedName) {
		ldEls = XMLDBHelper.selectNodes(SCL.XPATH_IED + "[@name='" + currIedName + "']/scl:AccessPoint/scl:Server/scl:LDevice"); 
		int i = 0;
		for (Element ldEl : ldEls) {
			List<Element> dsEls = DOM4JNodeHelper.selectNodesOnlyAtts(ldEl, "./*/scl:DataSet", "DataSet");
			List<String> dsNames = new ArrayList<String>();
			for (Element dsEl : dsEls) {
				dsNames.add(dsEl.attributeValue("name"));
			}
			ldMap.put(i++, dsNames);
		}
		dataTemplate = XMLDBHelper.selectSingleNode("/scl:SCL/scl:DataTypeTemplates");
		unitMap.clear();
		cdcMap.clear();
	}

	public Map<String, List<Element>> getAin() {
		return getMap(AIN_1, AIN_2, null);
	}

	public Map<String, List<Element>> getProtSignal() {
		return getMap(PROT_SIGNAL1, null, null);
	}

	public Map<String, List<Element>> getWarning() {
		return getMap(WARNING_1, WARNING_2, null);
	}

	public Map<String, List<Element>> getDin() {
		return getMap(DIN_1, DIN_2, null);
	}
	
	/**
	 * TODO:id可能被加上前缀
	 * 保护定值：KEY：LDInst，Value：保护定值数据
	 * 
	 * @return
	 */
	public Map<String, List<Element>> getProtSV() {
		putUnits();
		return getMap(PROT_SV1, PROT_SV2, PROT_SV3);
	}
	
	public List<Element> getAin(String ldInst) {
		return getList(ldInst, AIN_1, AIN_2, null);
	}
	
	public List<Element> getProtSignal(String ldInst) {
		return getList(ldInst, PROT_SIGNAL1, null, null);
	}
	
	public List<Element> getWarning(String ldInst) {
		return getList(ldInst, WARNING_1, WARNING_2, null);
	}
	
	public List<Element> getDin(String ldInst) {
		return getList(ldInst, DIN_1, DIN_2, null);
	}
	
	/**
	 * TODO:id可能被加上前缀
	 * 保护定值：KEY：LDInst，Value：保护定值数据
	 * 
	 * @return
	 */
	public List<Element> getProtSV(String ldInst) {
		putUnits();
		TimeCounter.end("单位", false);
		return getList(ldInst, PROT_SV1, PROT_SV2, PROT_SV3);
	}

	private void putUnits() {
		List<?> enumTypes = DOM4JNodeHelper.selectNodes(dataTemplate, "./scl:EnumType[@id='multiplier' or @id='SIUnit']");
		for (Object obj : enumTypes) {
			Element el = (Element) obj;
			String id = el.attributeValue("id");
			putUnit(el, id, "multiplier");
			putUnit(el, id, "SIUnit");
		}
	}
	
	/**
	 * 添加单位信息
	 * 
	 * @param el
	 * @param id
	 * @param type
	 */
	private void putUnit(Element el, String id, String type) {
		if (id.contains(type)) {
			List<?> elements = el.elements();
			for (Object obj1 : elements) {
				Element el1 = (Element) obj1;
				unitMap.put(type + el1.attributeValue("ord"), StringUtil.nullToEmpty(el1.getText()));
			}
		}
	}
	
	/**
	 * 获取指定类别的LDInst与对应信号列表的映射数据
	 * 
	 * @param dsName
	 * @param dsName1
	 * @param dsName2
	 * @return
	 */
	private Map<String, List<Element>> getMap(String dsName, String dsName1, String dsName2) {
		Map<String, List<Element>> map = new HashMap<String, List<Element>>();
		for (int i : ldMap.keySet()) {
			Element ldEl = ldEls.get(i);
			String inst = ldEl.attributeValue("inst");
			map.put(inst, getEls(ldEl, i, dsName, dsName1, dsName2));
		}
		return map;
	}
	
	/**
	 * 根据ldInst获取对应的信号列表
	 * 
	 * @param ldInst
	 *            LD实例信息
	 * @param dsName
	 *            数据集名称
	 * @param dsName1
	 *            数据集名称
	 * @param dsName2
	 *            数据集名称
	 * @return List<Element> 信号列表
	 */
	private List<Element> getList(String ldInst, String dsName, String dsName1, String dsName2) {
		for (int i : ldMap.keySet()) {
			Element ldEl = ldEls.get(i);
			String inst = ldEl.attributeValue("inst");
			if (!ldInst.equals(inst))
				continue;
			return getEls(ldEl, i, dsName, dsName1, dsName2);
		}
		return new ArrayList<Element>();
	}
	
	/**
	 * 获取信号列表
	 * 
	 * @param ldEl
	 *            LD节点元素
	 * @param i
	 *            ld内部索引序号
	 * @param dsName
	 *            数据集名称
	 * @param dsName1
	 *            数据集名称
	 * @param dsName2
	 *            数据集名称
	 * @return
	 */
	private List<Element> getEls(Element ldEl, int i, String dsName, String dsName1, String dsName2) {
		List<Element> els = new ArrayList<Element>();
		List<String> list = ldMap.get(i);
		for (String name : list) {
			if (name.startsWith(dsName) || (!StringUtil.isEmpty(dsName1) && name.startsWith(dsName1))
					|| (!StringUtil.isEmpty(dsName2) && name.startsWith(dsName2))) {
				els.addAll(getEls(name, ldEl));
			}
		}
		return els;
	}

	/**
	 * 
	 * 
	 * @param dsName
	 * @param ldEl
	 * @return
	 */
	private List<Element> getEls(String dsName, Element ldEl) {
		String inst = ldEl.attributeValue("inst");
		List<Element> values = new ArrayList<Element>();
		List<Element> list = DOM4JNodeHelper.selectNodes(ldEl, "./scl:LN0/scl:DataSet[@name='" + dsName + "']/scl:FCDA");
		for (Element el : list) {
			Element value = DOM4JNodeHelper.createSCLNode("Items");
			String doName = el.attributeValue("doName");
			Element lnEl = DOM4JNodeHelper.selectSingleNode(ldEl, "." + SCL.getLNXPath(el.attributeValue("prefix"),el.attributeValue("lnClass"),el.attributeValue("lnInst")));
			Element doEl = DOM4JNodeHelper.selectSingleNode(lnEl, "." + SCL.getDOXPath(doName));
			if (doEl == null)
				continue;
			value.addAttribute("name", doEl.attributeValue("desc"));
			value.addAttribute("ref", getRef(inst, el));
			
			if (dsName.startsWith(WARNING_1) || dsName.startsWith(WARNING_2)) {
				value.addAttribute("type", dsName.startsWith(WARNING_1) ? "告警" : "自检");
			} else {
				String lnType = lnEl.attributeValue("lnType");
				String cdc = "";
				String key = lnType + doName;
				if (cdcMap.containsKey(key)) {
					cdc = cdcMap.get(key);
				} else {
					cdc = DataTypeTemplateDao.getCDC(dataTemplate, lnType, doName);
					cdcMap.put(key, cdc);
				}
				value.addAttribute("type", cdc);//
				if (dsName.startsWith(PROT_SV1) || dsName.startsWith(PROT_SV2) || dsName.startsWith(PROT_SV3)) 
					getProtSVEls(dsName, doEl, value, cdc);
				else if (dsName.startsWith(AIN_1) || dsName.startsWith(AIN_2))
					getAinEls(dsName, doEl, value);
			}
			values.add(value);
		}
		return values;
	}

	/**
	 * 添加模拟量属性
	 * 
	 * @param dsName
	 *            数据集名称
	 * @param doEl
	 *            DO节点元素
	 * @param el
	 *            当前模拟量节点元素
	 */
	private void getAinEls(String dsName, Element doEl, Element el) {
		el.addAttribute("offset", getValue(doEl, "offset"));
		el.addAttribute("factor", getValue(doEl, "scaleFactor"));
		el.addAttribute("zeroDb", getValue(doEl, "zeroDb"));
		el.addAttribute("maxVal", getValue(doEl, "max"));
		el.addAttribute("minVal", getValue(doEl, "min"));
		el.addAttribute("hhLim", getValue(doEl, "hhLim"));
		el.addAttribute("hLim", getValue(doEl, "hLim"));
		el.addAttribute("lLim", getValue(doEl, "lLim"));
		el.addAttribute("llLim", getValue(doEl, "llLim"));
	}

	/**
	 * 添加保护定值属性
	 * 
	 * @param dsName
	 *            数据集名称
	 * @param doEl
	 *            DO节点元素
	 * @param el
	 *            当前保护定值节点元素
	 * @param cdc
	 *            CDC属性值
	 */
	private void getProtSVEls(String dsName, Element doEl, Element el, String cdc) {
		el.addAttribute("defaultVal", getValue(doEl, cdc.equals("ASG") ? "setMag" : "setVal"));//TODO: 若支持，可配置定值初始值
		el.addAttribute("maxVal", getValue(doEl, "maxVal"));
		el.addAttribute("minVal", getValue(doEl, "minVal"));
		el.addAttribute("step", getValue(doEl, "stepSize"));
		el.addAttribute("unit", getUnit(doEl));
	}

	/**
	 * 根据DO节点获取单位信息
	 * 
	 * @param doEl
	 * @return
	 */
	private String getUnit(Element doEl) {
		String multiplier = getValue(doEl, "multiplier");
		String siUnit = getValue(doEl, "SIUnit");
		String unit = "";	
		if (!StringUtil.isEmpty(multiplier))
			unit += unitMap.get("multiplier" + multiplier);
		if (!StringUtil.isEmpty(siUnit))
			unit += unitMap.get("SIUnit" + siUnit);
		return unit;
	}

	private String getValue(Element doEl, String name) {
		Element node = DOM4JNodeHelper.selectSingleNode(doEl, "./*[@name='" + name + "']");
		if (node == null)
			node = DOM4JNodeHelper.selectSingleNode(doEl, "./*/*[@name='" + name + "']");
		if (node == null)
			return "";
		Element valEl = DOM4JNodeHelper.selectSingleNode(node, "./scl:Val");
		if (valEl == null)
			valEl = DOM4JNodeHelper.selectSingleNode(node, "./*/scl:Val");
		return valEl == null ? "" : StringUtil.nullToEmpty(valEl.getText());
	}

	public static String getRef(String inst, Element el) {
		String ref = inst + "/" + SCL.getLnNameInFCDA(el) + "." + el.attributeValue("doName");
		String daName = el.attributeValue("daName");
		return ref + (StringUtil.isEmpty(daName) ? "" : ("." + daName));
	}
	
	public static void updateDesc(final String iedName, final String ref, final String desc) {
		TaskManager.addTask(new Job("") {
			protected IStatus run(IProgressMonitor monitor) {
				String[] intInfo = ref.split("/|\\.");
				String ldInst = intInfo[0];
				String doName = intInfo[2];
				String[] lnInfo = SCLUtil.getLNInfo(intInfo[1]);
				String prefix = lnInfo[0];
				String lnClass = lnInfo[1];
				String lnInst = lnInfo[2];
				String select = SCL.getLDXPath(iedName, ldInst) + SCL.getLNXPath(prefix,lnClass,lnInst);	
				String selPos = SCL.getDOXPath(doName);
				Element ele = XMLDBHelper.selectSingleNode(select + selPos);
				if (ele == null){
					Element eleDo = DOM4JNodeHelper.createSCLNode("DOI");
					eleDo.addAttribute("name", doName);
					eleDo.addAttribute("desc", desc);
					Element dAI = eleDo.addElement("DAI");
					dAI.addAttribute("name", "dU");
					Element val = dAI.addElement("Val");
					val.addText(desc);
					ControlDAO.insertNodeInLn(eleDo ,select);
				} else {
					Attribute attr = ele.attribute("desc");
					if (attr == null)
						ele.addAttribute("desc", desc);
					else 
						attr.setValue(desc);
					select +=selPos;
					ControlDAO.replaceControl(ele, select, desc, "dU");
				}
				return Status.OK_STATUS;
			}
		});
	}
}
