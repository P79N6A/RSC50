/**
 * Copyright (c) 2007-2015 上海思源弘瑞自动化有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based on IEC61850 SCT.
 */
package com.shrcn.business.scl.das;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.dom4j.Element;

import com.shrcn.business.scl.SCTProperties;
import com.shrcn.business.scl.beans.IEDInputsModel;
import com.shrcn.business.scl.beans.IEDModel;
import com.shrcn.business.scl.check.SchemaChecker;
import com.shrcn.business.scl.das.desc.DataSetDescOperator;
import com.shrcn.business.scl.das.desc.GOOSERelationOperator;
import com.shrcn.business.scl.das.desc.ImportDescOperator;
import com.shrcn.business.scl.das.desc.LNDescOperator;
import com.shrcn.business.scl.model.ReceivedInput;
import com.shrcn.business.scl.model.SCL;
import com.shrcn.found.common.Constants;
import com.shrcn.found.common.util.StringUtil;
import com.shrcn.found.file.util.FileManipulate;
import com.shrcn.found.file.xml.XMLFileManager;
import com.shrcn.found.ui.util.DialogHelper;
import com.shrcn.found.xmldb.XMLDBHelper;

 /**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2015-8-21
 */
public class ReplaceIEDDao {
	
	public static final int KEEP_INPUTS = 1 << 0;
	public static final int KEEP_COMM = 1 << 1;
	public static final int KEEP_CBS = 1 << 2;
	public static final int KEEP_DESCS = 1 << 3;
	public static final int KEEP_ALL = KEEP_INPUTS | KEEP_COMM | KEEP_CBS | KEEP_DESCS;
	
	private final List<String> cbNames = Arrays.asList(new String[] {
			"ReportControl", "LogControl",
			"GSEControl", "SampledValueControl", "SettingControl"});
	private final List<String> lnElNames = Arrays.asList(new String[] {
			"Text", "Private", "DataSet", "ReportControl", "LogControl", "DOI",
			"Inputs", "Log", "GSEControl", "SampledValueControl", "SettingControl"});
	
	private Map<String, List<IEDInputsModel>> inputsMap = new HashMap<String, List<IEDInputsModel>>(); 
	private Map<String, List<IEDModel>> dataSetMap = new HashMap<String, List<IEDModel>>(); 
	private Map<String, List<IEDModel>> lnMap = new HashMap<String, List<IEDModel>>(); 
	
	private int options;
	// 被替换装置名称
	private String iedName;
	// icd
	private Element icdRoot;
	
	public ReplaceIEDDao(String iedName, Element icdRoot, int options) {
		this.options = options;
		this.iedName = iedName;
		this.icdRoot = icdRoot;
	}
	
	public void execute() {
		if ((KEEP_DESCS & options) > 0)
			doKeepDesc();
		
		replaceIED();
		
		if ((KEEP_DESCS & options) > 0)
			doImportDesc();
		
		replaceComm();
	}
	
	/**
	 * 使用ICD文件替换SCD中指定的IED
	 * @param iedName
	 * @param icdIED
	 */
	public void replaceIED() {
		Element icdIED = icdRoot.element("IED");
		String oldIedName = icdIED.attributeValue("name", "");
		Map<String, Element> keepMap = new HashMap<String, Element>();
		Element scdIED = IEDDAO.getIEDNode(iedName);
		
		String xpath = "/scl:SCL/scl:IED[@name='" + iedName
				+ "']/scl:AccessPoint[@name='" + "S2" + "']";
		boolean existS2Node = XMLDBHelper.existsNode(xpath);
		String s1Xpath = "/scl:SCL/scl:IED[@name='" + iedName
				+ "']/scl:AccessPoint[@name='" + "S1" + "']";
        boolean existS1Node = XMLDBHelper.existsNode(s1Xpath);
        
		icdIED.addAttribute("name", scdIED.attributeValue("name", ""));
		icdIED.addAttribute("desc", scdIED.attributeValue("desc", ""));
		
		if ((KEEP_CBS & options) > 0 || (KEEP_INPUTS & options) > 0) {
			getKeepsInfoBasedSCD(keepMap, scdIED);
			replaceKeepsInfo(keepMap, icdIED);
		}
		
		XMLDBHelper.replaceNode("/scl:SCL/scl:IED[@name='" + iedName + "']", icdIED.createCopy());
		
		//添加S2访问点
		if (existS1Node && existS2Node) 
			CommunicationDAO.insertServerAt(s1Xpath);
		// 修改cbId
		IEDManager.synControlsID(oldIedName, iedName);
	}
	
	/**
	 * 将scd中指定ied的Inputs和cb信息存入map中。
	 * 
	 * @param keepMap
	 * @param scdIed
	 */
	@SuppressWarnings("unchecked")
	private void getKeepsInfoBasedSCD(Map<String, Element> keepMap, Element scdIed){
		List<?> apEls = scdIed.elements("AccessPoint");
		for(Object obj : apEls) {
			Element ap = (Element) obj;
			String apName = ap.attributeValue("name"); //$NON-NLS-1$
			Element server = ap.element("Server"); //$NON-NLS-1$
			if(server != null){
				List<?> ldEls = server.elements("LDevice");
				for (Object obj1 : ldEls) {
					Element ldEl = (Element) obj1;
					String ldInst = ldEl.attributeValue("inst"); //$NON-NLS-1$
					Element ln0 = ldEl.element("LN0");
					String lnName = SCL.getLnName(ln0);
					List<Element> subEls = ln0.elements();
					for (Element subEl : subEls) {
						String cbName = subEl.attributeValue("name");
						boolean needKeep = false;
						if (cbNames.contains(subEl.getName())
								&& (KEEP_CBS & options) > 0) {
							needKeep = true;
						}
						if ("Inputs".equals(subEl.getName()) && (subEl.elements().size()>0)   // 排除空的<Inputs>
								&& (KEEP_INPUTS & options) > 0) {
							needKeep = true;
						}
						if (needKeep) {
							String keyINln = subEl.getName() + "$" + apName + "$" + ldInst + "$" + lnName;
							if (!StringUtil.isEmpty(cbName)) {
								keyINln += "$" + cbName;
							}
							keepMap.put(keyINln, subEl);
						}
					}
				}
			}
		}
	}
	
	/**
	 * 删除并替换外部文件根结点下所有的<Inputs>元素
	 * 
	 * @param keepMap 
	 * @param icdIED
	 */
	@SuppressWarnings("unchecked")
	private void replaceKeepsInfo(Map<String, Element> keepMap, Element icdIED){
		List<?> aps = icdIED.elements("AccessPoint");
		for(Object obj : aps) {
			Element ap = (Element) obj;
			String apName = ap.attributeValue("name");
			Element server = ap.element("Server");
			if(server != null){
				List<Element> ldEls = server.elements("LDevice");
				for(Element ldEl : ldEls) {
					String ldInst = ldEl.attributeValue("inst");
					Element ln0 = ldEl.element("LN0");
					String lnName = SCL.getLnName(ln0);
					// 处理<Inputs>
					if ((options & KEEP_INPUTS) > 0) {
						String keyINln = "Inputs$" + apName + "$" + ldInst + "$" + lnName;
						Element keepEl = keepMap.get(keyINln);
						if (keepEl != null) {
							replaceSubEl(ln0, (Element) keepEl.createCopy());
						}
					}
					// 处理CBs
					if ((options & KEEP_CBS) > 0) {
						List<Element> subEls = ln0.elements();
						for (Element subEl : subEls) {
							if (cbNames.contains(subEl.getName())) {
								String cbName = subEl.attributeValue("name");
								String keyINln = subEl.getName() + "$" + apName + "$" + ldInst + "$" + lnName + "$" + cbName;
								Element keepEl = keepMap.get(keyINln);
								if (keepEl != null) {
									replaceSubEl(ln0, (Element) keepEl.createCopy());
								}
							}
						}
					}
				}
			}
		}
	}
	
	/**
	 * 按照schema顺序将<Inputs>和各种控制块添加到LN/LN0下。
	 * @param ln
	 * @param subEl
	 */
	@SuppressWarnings("unchecked")
	private void replaceSubEl(Element ln, Element subEl) {
		List<Element> children = ln.elements();
		
		String subType = subEl.getName();
		String cbName = subEl.attributeValue("name");
		int elIdx = lnElNames.indexOf(subType);
		int index = -1;
		boolean doReplace = false;
		for(int i=0; i<children.size(); i++) {
			String ndType = children.get(i).getName();
			boolean isPreEl = false;
			for (int j=0; j<elIdx; j++) {
				if (lnElNames.get(j).equals(ndType))
					isPreEl = true;
			}
			if (isPreEl) { // 处理前序节点
				index = i; // 找最后一个前序节点
			} else {
				if (ndType.equals(subType)) { // 处理同名节点
					if (StringUtil.isEmpty(cbName)) { // <Inputs>
						doReplace = true;
						index = i;
						break;
					} else {						  // <CBs>
						if (cbName.equals(children.get(i).attributeValue("name"))) {
							doReplace = true;
							index = i;
							break;
						} else {
							index = i; // 找最后一个同名节点
						}
					}
				} else {					// 非前序节点或同名节点时停止查找
					break;
				}
			}
		}
		if (doReplace) {
			children.remove(index);
			children.add(index, subEl);
		} else {
			children.add(index + 1, subEl);
		}
	}
	
	/**
	 * 保留IED描述信息
	 * 
	 * @param iedName IED名称
	 */
	private void doKeepDesc() {
		inputsMap.clear();
		dataSetMap.clear();
		lnMap.clear();
		// 导出信号关联描述
		List<String> selection = new ArrayList<String>();
		selection.add(iedName);
		
		List<IEDInputsModel> ieds = ExcelReportDao.getIEDInputs(selection);
		inputsMap.put(iedName, ieds);
		
		// 导出数据项描述
		List<String[]> lstSelectedItems = new ArrayList<String[]>();
		
		String[] aps = IEDDAO.getAPNames(iedName);
		String[] param = new String[aps.length + 1];
		
		param[0] = iedName;
		for (int i = 0; i < aps.length; i++) {
			param[i + 1] = aps[i];
		}
		
		lstSelectedItems.add(param);
		List<IEDModel> iedModels = ExcelReportDao.getDataSetsInfo(lstSelectedItems);
		dataSetMap.put(iedName, iedModels);
		
		// 导出LN描述
		iedModels = ExcelReportDao.getLNsInfo(lstSelectedItems);
		lnMap.put(iedName, iedModels);
	}
	
	/**
	 * 导入IED描述
	 * 
	 * @param iedName IED名称
	 */
	private void doImportDesc() {
		// 导入信号关联描述
		List<ReceivedInput> lstReceivedInput = new ArrayList<ReceivedInput>();
		
		ImportDescOperator oper = new GOOSERelationOperator();
		((GOOSERelationOperator)oper).setIeds(inputsMap.get(iedName));
		
		oper.readModel(iedName, lstReceivedInput);
		oper.update(lstReceivedInput);
		
		// 导入数据项描述
		oper = new DataSetDescOperator();
		lstReceivedInput.clear();
		
		((DataSetDescOperator)oper).setIedModels(dataSetMap.get(iedName));
		oper.readModel(iedName, lstReceivedInput);
				
		oper.update(lstReceivedInput);
		
		// 导入LN描述
		oper = new LNDescOperator();
		lstReceivedInput.clear();
		
		((LNDescOperator)oper).setIedModels(lnMap.get(iedName));
		oper.readModel(iedName, lstReceivedInput);
		
		oper.update(lstReceivedInput);
	}
	
	/**
	 * 根据IED type判断IED是否可以替换
	 * 
	 * @param iedINSCD
	 * @param root
	 */
	public static boolean canReplace(String typeINSCD, String icdPath, Element root) {
		Element iedINICD = root.element("IED"); //$NON-NLS-1$
		String typeINICD = iedINICD.attributeValue("type"); //$NON-NLS-1$
		boolean match = (StringUtil.isEmpty(typeINICD) && StringUtil.isEmpty(typeINSCD)) ||
				typeINSCD.equals(typeINICD);
		if (!match && 
				!DialogHelper.showConfirm("IED装置类型不一致（SCD中为 " + typeINSCD +
				"，ICD中为 " + typeINICD + "），确定要替换吗？")) {
			return false;
		}
		FileManipulate.initDirs(Constants.tempDir);
		String tempIcdPath = Constants.tempDir + new File(icdPath).getName();
		// 添加versoin,revision
		if(SCTProperties.getInstance().needsVersions()) {
			root.addAttribute("revision", "A");
			root.addAttribute("version", "2007");
		}
		// 使用统一namespace后的文件进行schema校验
		XMLFileManager.saveUTF8File(root.asXML(), new File(tempIcdPath));
		boolean checkSchema = SchemaChecker.checkSchema(tempIcdPath, false);
		if (!checkSchema
				&& !DialogHelper.showConfirm("Schema校验不通过，确定要继续打开吗？\n\n" +
				"如要关闭Schema检查，可通过“文件->Schema校验开关”完成。")) {
			return false;
		}
		return true;
	}
	
	/**
	 * 替换通信配置部分。
	 */
	private void replaceComm() {
		boolean isKeep = ((KEEP_COMM & options) > 0);
		Map<String, List<String>> map = CommunicationDAO.getSubNet(iedName); 
		if (isKeep) {
			// 根据新的信息获取通信
			Set<String> keySet = map.keySet();
			for (String key : keySet) {// 根据原有子网配置添加通信信息
				String[] comms = key.split(Constants.COLON);
				CommunicationDAO.updateCommunication(comms[0], iedName, comms[1], icdRoot, map);
			}
		} else {
			IEDDAO.deleteComm(iedName, true);// 移除原有通信信息
			Set<String> keySet = map.keySet();
			for (String key : keySet) {// 根据原有子网配置添加通信信息
				String[] comms = key.split(Constants.COLON);
				Element el = CommunicationDAO.insertCommunication(comms[0], iedName, comms[1], icdRoot);
				XMLDBHelper.insertAsLast("/scl:SCL/scl:Communication/scl:SubNetwork[@name='" + comms[0] + "']", el);
			}
		}
	}
}