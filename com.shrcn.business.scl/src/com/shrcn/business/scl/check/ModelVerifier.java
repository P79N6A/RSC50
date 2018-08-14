/**
 * Copyright (c) 2007, 2008 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based Visual Device Develop System.
 */
package com.shrcn.business.scl.check;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;

import com.shrcn.business.scl.das.DataTemplateDao;
import com.shrcn.business.scl.das.IEDDAO;
import com.shrcn.business.scl.enums.EnumCtrlBlock;
import com.shrcn.business.scl.model.LNTypeInSubstationCheck;
import com.shrcn.business.scl.model.Messages;
import com.shrcn.business.scl.model.SCL;
import com.shrcn.business.scl.ui.Outputer;
import com.shrcn.business.scl.ui.ProblemManager;
import com.shrcn.found.common.Constants;
import com.shrcn.found.common.util.ListUtil;
import com.shrcn.found.common.util.StringUtil;
import com.shrcn.found.file.xml.DOM4JNodeHelper;
import com.shrcn.found.ui.view.ConsoleManager;
import com.shrcn.found.xmldb.XMLDBHelper;

/**
 * 
 * @author 吴云华(mailto:wyh@shrcn.com)
 * @version 1.0, 2009-7-30
 */
public class ModelVerifier {
	
	public static final int schemaCheck = 1<<0; 			// GSE、SMV引用检查（控制块不存在、数据集不存在）-全局
	public static final int iedNameCheck = 1<<1; 			// 装置名称重复性检查-全局
	public static final int commCheck = 1<<2; 				// 通信配置重复性、属性缺失检查-全局
	public static final int relationCheck = 1<<3; 			// 一二次关联检查-全局
		
	public static final int datatypeTemplatesCheck = 1<<4; 	// 数据模板关联检查-全局
	
	public static final int lnodeInstCheck = 1<<5; 			// 模型实例引用检查-可选
	public static final int innerdsCheck = 1<<6; 			// 控制块数据集不存在，数据集内数据引用检查-可选
	public static final int inputsCheck = 1<<7; 			// Inputs关联信号引用检查-可选

	Map<String, List<String>> lnTypeCache = new HashMap<String, List<String>>(); // lnType : subRefs
	// 待检查的所有IED名称
	private List<String> allName = null;
	private Element dataTypeTemplates;
	private Outputer mvDialog;
	private List<String> types = new ArrayList<String>(); // 用于数据模板检查，避免重复检查
	
	private boolean checkIED = false;
	private static Map<String, Element> iedCache = null;
	
	
	public ModelVerifier(Outputer mvDialog, Element dataTypeTemplates){
		this.mvDialog = mvDialog;
		this.dataTypeTemplates = dataTypeTemplates;
	}
	
	public ModelVerifier(Element dataTypeTemplates){
		this.dataTypeTemplates = dataTypeTemplates;
	}
	
	/**
	 * 把gse、smv、数据集、逻辑结点、输入项等一起进行检查
	 * 
	 * @param gsesmvCheck
	 * @param innerdsCheck
	 * @param lnodeInstCheck
	 * @param inputsCheck
	 * @param datatypeTemplatesCheck
	 */
	public void start(int selected) {
		if (mvDialog != null)
			mvDialog.setRun(true);
		
//		if((gsesmvCheck & selected)>0) {
//			if (Constants.DEBUG) TimeCounter.begin();
//			setProgressVisual(true);
//			textlog("\t\n" + Messages.getString("ModelVerifyModel.gse.ref.check.begin")); //$NON-NLS-1$ //$NON-NLS-2$
//			gseCheckForScd(allName);
//			textlog("\t\n" + Messages.getString("ModelVerifyModel.gse.ref.check.end")+"\n"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
//			textlog("\t\n" + Messages.getString("ModelVerifyModel.smv.ref.check.begin")); //$NON-NLS-1$ //$NON-NLS-2$
//			smvCheckForScd(allName);
//			textlog("\t\n" + Messages.getString("ModelVerifyModel.smv.ref.check.end")+"\n"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
//			setProgressVisual(false);
//			if (Constants.DEBUG) textlog("\t\n" + TimeCounter.end(Messages.getString("ModelVerifyModel.smv.ref.check.end"), true));
//		}
		
		if((schemaCheck & selected)>0) {
			//if (Constants.DEBUG) TimeCounter.begin();
			SchemaChecker.check();
			//if (Constants.DEBUG) textlog("\t\n" + TimeCounter.end("SCL Schema检查", true));
		}
		if((iedNameCheck & selected)>0) {
			//if (Constants.DEBUG) TimeCounter.begin();
			textlog("\t\n------装置名称重复性检查开始------");
			checkIedNames();
			textlog("\t\n------装置名称重复性检查结束------");
			//if (Constants.DEBUG) textlog("\t\n" + TimeCounter.end("参数重复性检查", true));
		}
		if((commCheck & selected)>0) {
			//if (Constants.DEBUG) TimeCounter.begin();
			textlog("\t\n------通信参数设置及重复性检查开始------");
			textlog(checkComm(null, LEVEL.ERROR) + checkCbIds(null, LEVEL.ERROR));
			textlog("\t\n------通信参数设置及重复性检查结束------");
			//if (Constants.DEBUG) textlog("\t\n" + TimeCounter.end("参数重复性检查", true));
		}
		// 一二次关联检查
		if((relationCheck & selected)>0){
			textlog("\n------设备一二次关联检查开始------\n");
			List<String> msgs = ModelChecker.checkLNodesRelated();
			for (String msg : msgs) {
				textlog(msg);
			}
			textlog("------设备一二次关联检查结束------\n");
		}
		// 数据模板关联检查
		if((datatypeTemplatesCheck & selected)>0){
			//if (Constants.DEBUG) TimeCounter.begin();
			setProgressVisual(true);
			textlog("\t\n" + Messages.getString("ModelVerifyModel.datatemplate.ref.check.begin")); //$NON-NLS-1$ //$NON-NLS-2$
			datatypeTemplateCheckeForScd();
			textlog("\t\n" + Messages.getString("ModelVerifyModel.datatemplate.ref.check.end")+"\n"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			setProgressVisual(false);
			//if (Constants.DEBUG) textlog("\t\n" + TimeCounter.end(Messages.getString("ModelVerifyModel.datatemplate.ref.check.end"), true));
		}
		
		if (allName != null && allName.size() > 0) {
			List<String> iedsPass = new ArrayList<String>();
			List<String> iedsFail = new ArrayList<String>();
			for (String iedName : allName) {
				CCDModelChecker checker = new CCDModelChecker(iedName, CCDModelChecker.CHECK_CID);
				if (!checker.doCheck()) {
					iedsFail.add(iedName);
				} else {
					iedsPass.add(iedName);
				}
				List<Problem> problems = checker.getProblems();
				ProblemManager.getInstance().append(problems);
				checker.clear();
			}
			exportReport(iedsPass, iedsFail);
			
//			// 逻辑节点实例引用检查
//			if((lnodeInstCheck & selected)>0){
////				TimeCounter.begin();
//				setProgressVisual(true);
//				textlog("\t\n" + Messages.getString("ModelVerifyModel.ln.ref.check.begin")); //$NON-NLS-1$ //$NON-NLS-2$
//				lnodeInstCheckForScd(allName);
//				textlog("\t\n" + Messages.getString("ModelVerifyModel.ln.ref.check.end")+"\n"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
//				setProgressVisual(false);
////				textlog("\t\n" + TimeCounter.end("逻辑节点实例引用检查", true));
//			}
//			// 控制块及数据集内数据引用检查
//			if((innerdsCheck & selected)>0){
//				//if (Constants.DEBUG) TimeCounter.begin();
//				setProgressVisual(true);
//				textlog("\t\n" + Messages.getString("ModelVerifyModel.data.ref.check.begin")); //$NON-NLS-1$ //$NON-NLS-2$
//				innerCbDsCheckForScd(allName);
//				textlog("\t\n" + Messages.getString("ModelVerifyModel.data.ref.check.end")+"\n"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
//				setProgressVisual(false);
//				//if (Constants.DEBUG) textlog("\t\n" + TimeCounter.end(Messages.getString("ModelVerifyModel.data.ref.check.end"), true));
//			}
//			// Inputs关联信号引用检查
//			if((inputsCheck & selected)>0){
//				//if (Constants.DEBUG) TimeCounter.begin();
//				setProgressVisual(true);
//				textlog("\t\n" + Messages.getString("ModelVerifyModel.input.ref.check.begin")); //$NON-NLS-1$ //$NON-NLS-2$
//				inputsCheckForScd(allName);
//				textlog("\t\n" + Messages.getString("ModelVerifyModel.input.ref.check.end")+"\n"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
//				setProgressVisual(false);
//				//if (Constants.DEBUG) textlog("\t\n" + TimeCounter.end(Messages.getString("ModelVerifyModel.input.ref.check.end"), true));
//			}
		}
		if (mvDialog != null)
			mvDialog.setRun(false);
	}
	
	protected void exportReport(List<String> iedsPass, List<String> iedsFail) {
		String msg = "-------------装置模型检查结果-------------\n";
		if (iedsPass.size() > 0)
			msg += "检查通过：" + getIedNames(iedsPass);
		if (iedsFail.size() > 0)
			msg += "检查失败：" + getIedNames(iedsFail) + "错误原因请参考问题视图。";
		ConsoleManager.getInstance().append(msg, false);
	}
	
	protected String getIedNames(List<String> ieds) {
		StringBuilder sb = new StringBuilder();
		int num = 0;
		for (String iedName : ieds) {
			num++;
			if (sb.length()!=0) {
				sb.append("，");
				if (num % 10 == 0)
					sb.append("\n");
			}
			sb.append(iedName);
		}
		sb.append("。\n");
		return sb.toString();
	}
	
	private Element getIedNode(String iedName) {
		if (iedCache==null)
			iedCache = new HashMap<>();
		Element iedNd = iedCache.get(iedName);
		if (iedNd == null) {
			iedNd = IEDDAO.getIEDNode(iedName);
			iedCache.put(iedName, iedNd);
		}
		return iedNd;
	}

	/**
	 * 检查装置名称
	 */
	private void checkIedNames() {
		// 测试SCD文件IED名称不唯一时TUT校验功能
		List<Element> allied = IEDDAO.getALLIED();
		List<String> iedNames = new ArrayList<String>();
		for (Element e : allied) {
			String iedName = e.attributeValue("name");
			if (iedNames.contains(iedName))
				textlog("\n错误：存在重复的装置名称 " + iedName + " 。");
			else
				iedNames.add(iedName);
		}
	}
	
	/**
	 * 检查cbId是否重复。
	 * @param names
	 * @return
	 */
	private String checkCbIds(List<String> names, LEVEL level) {
		String msg = "";
		Map<String, String[]> appIdMap = new HashMap<>();
		Map<String, String[]> smvIdMap = new HashMap<>();
		Map<String, String[]> rptIdMap = new HashMap<>();
		if (Constants.XQUERY) {
			String xquery = "for $ied in " + XMLDBHelper.getDocXPath() + "/scl:SCL/scl:IED" +
					" return <IED name='{$ied/@name}'>{" +
					" for $ld in $ied/scl:AccessPoint/scl:Server/scl:LDevice " +
					" return <LDevice inst='{$ld/@inst}'>{" +
					" for $cb in $ld/*/*[name()='GSEControl' or name()='SampledValueControl' or name()='ReportControl'] return $cb" +
					"}</LDevice>" +
					"}</IED>";
			List<Element> iedCbs = XMLDBHelper.queryNodes(xquery);
			for (Element ied : iedCbs) {
				String iedName = ied.attributeValue("name");
				msg = checkCbIds(names, level, msg, appIdMap, smvIdMap, rptIdMap, iedName, ied.elements());
			}
		} else {
			List<String> allieds = IEDDAO.getIEDNames();
			for (String iedName : allieds) {
				List<Element> lds = getLDs(iedName);
				msg = checkCbIds(names, level, msg, appIdMap, smvIdMap, rptIdMap, iedName, lds);
			}
		}
		return msg;
	}

	public List<Element> getLDs(String iedName) {
		String ldXpath = SCL.XPATH_IED + "[@name='" + iedName + "']/scl:AccessPoint/scl:Server/scl:LDevice";
		List<Element> ldEls = XMLDBHelper.selectNodes(ldXpath);
		List<Element> lds = new ArrayList<Element>();
		for (Element ldEl : ldEls) {
			Element ldCpEl = DOM4JNodeHelper.createRTUNode("LDevice");
			String inst = ldEl.attributeValue("inst");
			ldCpEl.addAttribute("inst", inst);
			List<Element> cbEls = DOM4JNodeHelper.selectNodes(ldEl, "./*/*[name()='GSEControl' or name()='SampledValueControl' or name()='ReportControl']");
			ldCpEl.setContent(cbEls);
			lds.add(ldCpEl);
		}
		return lds;
	}

	private String checkCbIds(List<String> names, LEVEL level, String msg,
			Map<String, String[]> appIdMap, Map<String, String[]> smvIdMap,
			Map<String, String[]> rptIdMap, String iedName, List<Element> lds) {
		for (Element ld : lds) {
			String ldInst = ld.attributeValue("inst");
			List<Element> cbs = ld.elements();
			for (Element cb : cbs) {
				EnumCtrlBlock block = EnumCtrlBlock.valueOf(cb.getName());
				Map<String, String[]> map = null;
				String att = null;
//					LEVEL level = LEVEL.ERROR;
				if (EnumCtrlBlock.GSEControl == block) {
					map = appIdMap;
					att = "appID";
				} else if (EnumCtrlBlock.SampledValueControl == block) {
					map = smvIdMap;
					att = "smvID";
				} else {
					map = rptIdMap;
					att = "rptID";
				}
				String value = cb.attributeValue(att);
				String cbRef = iedName + ldInst + "/LLN0$" + block.getFc() +
						"$" + cb.attributeValue("name");
				if (!StringUtil.isEmpty(value)) {
					if (map.containsKey(value)) {
						String iedName1 = map.get(value)[0];
						String cbRef1 = map.get(value)[2];
						boolean contains = (names != null && (names.contains(iedName)||names.contains(iedName1)));
						if (contains) {
							msg += level.getPrefix() + block.getDesc() + " " + cbRef + " 和 " + cbRef1 +
									" 的 " + att + " 属性均为 " + value + " 。";
						}
					} else {
						map.put(value, new String[]{iedName, ldInst, cbRef});
					}
				}
			}
		}
		return msg;
	}
	
	/**
	 * 检查控制块通信参数配置重复（IP、MAC、CB）及相关参数合法性检查。
	 * 
	 * @param items
	 * @param attrMap
	 */
	@SuppressWarnings("unchecked")
	private String checkComm(List<String> names, LEVEL level) {
		String prefix = level.getPrefix();
		StringBuilder msg = new StringBuilder();
		StringBuilder duplicates = new StringBuilder();
		Map<String, String> map = new HashMap<String, String>();
		List<String> subNames = XMLDBHelper.getAttributeValues(SCL.XPATH_SUBNETWORK + "/@name");
		for(String netName : subNames) {
			List<String> values = new ArrayList<>();
			List<Element> apEls = XMLDBHelper.selectNodes(SCL.XPATH_SUBNETWORK + "[@name='"+ netName +"']/scl:ConnectedAP");
			for (Element el : apEls) {
				String iedName = el.attributeValue("iedName");
				if (names != null && !names.contains(iedName))
					continue;
				String apName = el.attributeValue("apName");
				List<Element> elements = el.elements();
				for (Element elApCb : elements) {
					String ndName = elApCb.getName();
					if ("Address".equals(ndName)) {
						msg.append(ModelChecker.checkMMS(iedName, elApCb, level));
						
						String currIedAp = iedName + "." + apName;
						String ip = DOM4JNodeHelper.getNodeValueByXPath(elApCb, "./scl:P[@type='IP']");
						if (!StringUtil.isEmpty(ip)) {
							if (values.contains(currIedAp)) {
								duplicates.append(prefix + "装置 " + iedName + " 的访问点 " + apName + " 在子网 " + netName +
										" 中存在多个IP地址。 ");
							} else {
								values.add(currIedAp);
							}
							if (map.containsKey(ip)) {
								String[] iedap = map.get(ip).split("\\.");
								duplicates.append(prefix + "装置 " + iedName + " 的访问点 " + apName + " 与装置 " 
											+ iedap[0] + " 的访问点 " + iedap[1] + " 的IP地址均为 " + ip + " 。");
							} else {
								map.put(ip, currIedAp);
							}
						}
					}
					EnumCtrlBlock block = null;
					if (EnumCtrlBlock.GSEControl.getCbName().equals(ndName)) {
						block = EnumCtrlBlock.GSEControl;
					}
					if (EnumCtrlBlock.SampledValueControl.getCbName().equals(ndName)) {
						block = EnumCtrlBlock.SampledValueControl;
					}
					if (block != null) {
						String cbName = elApCb.attributeValue("cbName");
						String ldInst = elApCb.attributeValue("ldInst");
						String cbRef = iedName + ldInst + "/LLN0$" + block.getFc() + "$" + cbName;
						msg.append(ModelChecker.checkCbProps(iedName, elApCb, level));
						
						if (map.containsKey(cbRef)) {
							String net = map.get(cbRef);
							if (net.equals(netName)) {
								duplicates.append(prefix + "" + block.getDesc() + " " + cbRef + " 在子网 " + net + " 中存在重复配置。");
							} else {
								if (!net.equals(netName)) {
									msg.append(prefix + "" + block.getDesc() + " " + cbRef + " 同时出现在 " + net +
										" 和 " + netName + " 两个子网中。");
								} else {
									msg.append(prefix + "" + block.getDesc() + " " + cbRef + " 在子网 " + net +
											" 中存在重复配置。");
								}
							}
						} else {
							map.put(cbRef, netName);
						}
						String[] atts = new String[] {"MAC-Address", "APPID"};
						for (String att : atts) {
							String attValue = DOM4JNodeHelper.getNodeValueByXPath(elApCb, "./scl:Address/scl:P[@type='" + att + "']");
							if (!StringUtil.isEmpty(attValue)) {
								if (map.containsKey(attValue)) {
									duplicates.append(prefix + "" + block.getDesc() + " " + cbRef +
											" 和 " + map.get(attValue) + " 的 " + att + " 属性值均为 " + attValue + " 。");
								} else {
									map.put(attValue, cbRef);
								}
							}
						}
					}
				}
			}
		}
		msg.append(duplicates);
		return msg.toString();
	}
	
	/**
	 * 数据集内数据引用检查
	 * @param iedNames
	 */
	private void innerCbDsCheckForScd(List<String> iedNames){
		int i = 1;
		for (String iedName : iedNames) {
			labellog(Messages.getString("ModelVerifyModel.check.ied")+iedName); //$NON-NLS-1$
			progressMove(i);
			textlog(innerDsCheckForIed(iedName));
		}
	}

	public List<String> innerDsCheckForIed(String iedName) {
		Element iedNode = getIedNode(iedName);
		List<String> msgs = new ArrayList<>();
		msgs.add(ModelChecker.checkInnerCtrl(iedNode, LEVEL.ERROR));
		msgs.add(ModelChecker.checkCbInIed(iedName, LEVEL.ERROR));
		msgs.addAll(checkDataSet(iedNode));
		return msgs;
	}
	
	/**
	 * 逻辑节点实例引用检查
	 * @param iedNames
	 */
	private void lnodeInstCheckForScd(List<String> iedNames){
		int i = 1;
		for (String iedName : iedNames) {
			labellog(Messages.getString("ModelVerifyModel.check.ied")+iedName); //$NON-NLS-1$
			progressMove(i);
			textlog(lnodeInstCheckForIed(getIedNode(iedName)));
		}
	}
	
	/**
	 * Inputs关联信号引用检查
	 * @param iedNames
	 */
	private void inputsCheckForScd(List<String> iedNames){
		int i = 1;
		for (String iedName : iedNames) {
			labellog(Messages.getString("ModelVerifyModel.check.ied")+iedName); //$NON-NLS-1$
			progressMove(i);
			textlog(inputsCheckForIed(iedName));
		}
	}
	
	/**
	 * 数据模板关联检查
	 */
	private void datatypeTemplateCheckeForScd() {
		List<Element> lnodeTypes = new ArrayList<>();
		Map<String, Element> tplCache = new HashMap<>();
		for (Object el : dataTypeTemplates.elements()) {
			Element elTpl = (Element) el;
			if ("LNodeType".equals(elTpl.getName())) {
				lnodeTypes.add(elTpl);
			} else {
				String id = elTpl.attributeValue("id");
				tplCache.put(id, elTpl);
			}
		}
		int maxCount = lnodeTypes.size();
		setProgressMaxValue(maxCount);
		labellog(Messages.getString("ModelVerifyModel.datatemplate.checking")); //$NON-NLS-1$
		int i = 1;
		for (Object obj : lnodeTypes) {
			Element lnodeType = (Element) obj;
			progressMove(i);
			loopCheck(lnodeType, tplCache);
		}
	}
	
	/**
	 * 递归检查数据类型定义
	 * @param node
	 */
	private void loopCheck(Element node, Map<String, Element> tplCache) {
		String ndName = node.getName();
		String ndType = node.attributeValue("id"); //$NON-NLS-1$
		List<?> elements = node.elements();
		for (Object obj : elements) {
			Element element = (Element) obj;
			String eleNdName = element.getName();
			String eleName = element.attributeValue("name"); //$NON-NLS-1$
			String typeValue = element.attributeValue("type"); //$NON-NLS-1$
			if (typeValue == null)
				continue;
			Element typeNode = tplCache.get(typeValue);
			if (typeNode == null) {
				textlog("错误：数据模板中id为 " + ndType + " 的 " + ndName +
						" 下名为 " + eleName + " 的子元素所对应的类型 " + typeValue +
						" 不存在(" + ndName + "=" + ndType + " " + 
						eleNdName + "=" + eleName +	")。"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			}
			if (!types.contains(typeValue)) {
				types.add(typeValue);
				if(typeNode != null && !"EnumType".equals(typeNode.getName())) //$NON-NLS-1$
					loopCheck(typeNode, tplCache);
			}
		}
	}
	
	/**
	 * 数据集引用进行检查。
	 * @param root
	 */
	public List<String> innerDsCheckForIed(Element root){
		Element iedNode = IEDDAO.getIEDNode(root);
		return checkDataSet(iedNode);
	}

	private List<String> checkDataSet(Element iedNode) {
		List<String> lstStatus = new ArrayList<String>();
		String iedName = iedNode.attributeValue("name");
		// 数据项检查
		Map<String, List<String>> fcdaRefMap = IEDDAO.getFCDARefs(iedNode);
		lstStatus.addAll(lnodeInstCheck(iedName, fcdaRefMap));
		// 数据集最大条目数检查
		lstStatus.addAll(checkDSMax(iedNode));
		return lstStatus;
	}
	
	/**
	 * 控制块对数据集的引用检查。（已知<IED>，无需查询。）
	 * @param root
	 */
	public List<String> innerCbDsCheckForIed(Element root){
		List<String> lstStatus = new ArrayList<String>();
		List<Element> ldList = DOM4JNodeHelper.selectNodes(root, "/scl:SCL/scl:IED/scl:AccessPoint/scl:Server/scl:LDevice");
		for (Element ldEl : ldList) {
			List<String> dsList = DOM4JNodeHelper.getAttributeValues(ldEl, "./*/scl:DataSet/@name");
			for (EnumCtrlBlock cb : EnumCtrlBlock.values()) {
				List<Element> cbList = DOM4JNodeHelper.selectNodes(ldEl, "./*/scl:" + cb.name());
				for (Element cbEl : cbList) {
					String datSet = cbEl.attributeValue("datSet", "");
					if (!StringUtil.isEmpty(datSet) && !dsList.contains(datSet)) {
						String ldInst = ldEl.attributeValue("inst");
						String cbRef = ldInst + "/" + SCL.getLnName(cbEl.getParent()) 
								+ "$" + cb.getFc() + "$" + cbEl.attributeValue("name");
						lstStatus.add("\t\n错误：" + cb.getDesc() + cbRef + "引用的数据集" + datSet + 
								"在当前逻辑设备" + ldInst + "下不存在！");
					}
				}
			}
		}
		
		return lstStatus;
	}
	
	/**
	 * 逻辑节点实例引用检查。（已知<IED>，无需查询。）
	 * @param root
	 */
	public List<String> lnodeInstCheckForIed(Element root) {
		List<String> lstStatus = new ArrayList<String>();
		if (root == null)
			return lstStatus;
		Element ied = root;
		String iedName = ied.attributeValue("name");
		List<Element> ldevices = DOM4JNodeHelper.selectNodes(ied, "./scl:AccessPoint/scl:Server/scl:LDevice");
		Map<String, List<String>> iedMap = IEDDAO.getLnSubRef(ldevices);
		lstStatus.addAll(lnodeInstCheck(iedName, iedMap));
		return lstStatus;
	}
	
	/**
	 * 对指定IED的逻辑节点及其下属节点进行检查
	 * @param iedName
	 * @param iedMap 	//subRefMap.put(ldInst + "/" + lnName + ":" + lnType, subRefs);
	 * 					// String key = lnName + ":" + lnType + "@" + datName;
	 */
	private List<String> lnodeInstCheck(String iedName, Map<String, List<String>> iedMap) {
		List<String> lstStatus = new ArrayList<String>();
		List<String> errorLNs = new ArrayList<String>(); // 避免重复提示
		for (String key : iedMap.keySet()) {
			String lnName = key.substring(0, key.indexOf(':'));
			String lnType = key.substring(key.indexOf(':') + 1);
			String datName = null;
			int atPos = lnType.indexOf('@');
			if (atPos > 0) {
				datName = lnType.substring(atPos + 1);
				lnType = lnType.substring(0, atPos);
			}
			
			// 检查模型引用
			List<String> lnTypeRefs = lnTypeCache.get(lnType);
			if (lnTypeRefs == null) {
				lnTypeRefs = DataTemplateDao.getLnTypeSubRef(lnType, dataTypeTemplates);
				if (lnTypeRefs == null) {
					if (datName == null) {
						if ("null".equals(lnType) && !errorLNs.contains(lnName)) {
							// 日志：ln不存在
							lstStatus.add("\n错误：装置 " + iedName + " 引用的逻辑节点  " + lnName + " 不存在。");
							errorLNs.add(lnName);
						} else {
							// 日志：lnType不存在
							lstStatus.add("\n错误：逻辑节点 " + iedName + lnName + " 的类型 " + lnType + " 在数据模板中未定义。");
						}
					} else {
						lstStatus.add("\n错误：数据集 " + iedName + lnName + "$DS$" + datName + " 中存在无效的逻辑节点 " + lnName + " 。");
					}
					continue;
				}
				if (lnTypeRefs != null)
					lnTypeCache.put(lnType, lnTypeRefs);
			}
			List<String> values = iedMap.get(key);
			for (String ref : values) {
				boolean contains = false;
				if (lnTypeRefs != null) {
					contains = ListUtil.search(lnTypeRefs, ref) > -1;
				}
				if (!contains) {
					// 日志：DO,DA不存在
					if (datName == null) {
						lstStatus.add("\n错误：逻辑节点 " + iedName + lnName + " 的数据类型LNodeType[" + lnType + "]下不存在 " + ref + " 。");
					} else {
						lstStatus.add("\n错误：数据集 " + iedName + lnName + "$DS$" + datName + " 中存在无效的信号 " + lnName + "." + ref + " 。");
					}
				}
			}
		}
		return lstStatus;
	}

	/**
	 * 检查数据集成员最大值
	 * 
	 * @param list
	 * @param iedName
	 */
	private List<String> checkDSMax(Element iedNode) {
		String iedName = iedNode.attributeValue("name");
		List<String> msgs = new ArrayList<>();
		String maxAttr = DOM4JNodeHelper.getAttributeValue(iedNode, "./scl:Services/scl:ConfDataSet/@maxAttributes");
		if (StringUtil.isEmpty(maxAttr)){
//			msgs.add("\n错误：装置 " + iedName + " 的数据集成员最大值未声明。");
			return msgs;
		} else {
			int max = Integer.parseInt(maxAttr);
			List<Element> dsEls = DOM4JNodeHelper.selectNodes(iedNode, "./scl:AccessPoint/scl:Server/scl:LDevice/*/scl:DataSet");
			for (Element dsEl : dsEls) {
				String name = dsEl.attributeValue("name");
				List<?> elements = dsEl.elements("FCDA");
				int count = (elements == null ? 0 : elements.size());
				if (count > max) {
					Element lnEl = dsEl.getParent();
					String lnName = lnEl.getParent().attributeValue("inst") + "/" + SCL.getLnName(lnEl);
					msgs.add("\n错误：数据集 " + iedName + lnName + "$DS$" + name + " 成员数量 " + count +
							" 超过了声明最大值 " + max + " 。");
				}
			}
		}
		return msgs;
	}

	/**
	 * Inputs关联信号引用检查。（需查询<IED>，再检查。）
	 * @param iedName
	 */
	public List<String> inputsCheckForIed(String iedName) {
		return inputsCheck(iedName, IEDDAO.getExtRefs(getIedNode(iedName)));
	}
	
	/**
	 * 信号关联检查
	 * @param iedName
	 * @param extRefs
	 */
	private List<String> inputsCheck(String iedName, List<Element> extRefs) {
		List<String> lstInfo = new ArrayList<String>();
		List<String> intAddrList = new ArrayList<String>();
		List<String> extIedList = new ArrayList<String>();
		for (int i = 0; i < extRefs.size() ; i++) {
			Element extRef = extRefs.get(i);
			addInfo(lstInfo, ModelChecker.checkInnerSignal(iedName, extRef, dataTypeTemplates, lnTypeCache));
			String intAddr = DOM4JNodeHelper.getAttribute(extRef, "intAddr");
			if (intAddrList.contains(intAddr)) {
				addInfo(lstInfo, "\n错误：装置 " + iedName + " 的内部虚端子 " + intAddr + " 存在重复关联。");
			} else if (!StringUtil.isEmpty(intAddr)) {
				if (!intAddr.contains(":")) {
					addInfo(lstInfo, "\n警告：虚端子 " + intAddr + " 物理端口号未配置！");
				}
				intAddrList.add(intAddr);
			}
			String extIed = extRef.attributeValue("iedName");
			if (checkIED && !extIedList.contains(extIed)) {
				extIedList.add(extIed);
			}
		}
		if (checkIED) {
//			LEVEL level = LEVEL.WARNING; //??
			LEVEL level = LEVEL.ERROR;
			for (String extIed : extIedList) {
				Element iedNode = getIedNode(extIed);
				if (iedNode == null) {
					addInfo(lstInfo, level.getPrefix() + "SCD中不存在与装置 " + iedName + " 有关联关系的装置 " + extIed + " 。");
					continue;
				}
				addInfo(lstInfo, ModelChecker.checkInnerCtrl(iedNode, level));
				addInfo(lstInfo, ModelChecker.checkCbInIed(extIed, level));
			}
			List<String> names = new ArrayList<String>();
			names.add(iedName);
			addInfo(lstInfo, checkCbIds(names, LEVEL.WARNING));
		}
		return lstInfo;
	}

	private void addInfo(List<String> lstInfo, String info) {
		if (!StringUtil.isEmpty(info)) {
			lstInfo.add(info);
			ConsoleManager.getInstance().append(info.substring(1), false);
		}
	}
	
	/**
	 * 检查控制块内容是否完整
	 * @param root
	 */
	public void checkControlBlock(Element root) {
		List<?> lstNewLDevice = root.selectNodes("./*[name()='IED']/*[name()='AccessPoint']/*[name()='Server']/*[name()='LDevice']");
		List<Element> lstOldLDevice = new ArrayList<Element>();
		if (allName != null) {
			for (String iedName : allName) {
				List<Element> ldevices = IEDDAO.queryLDevices(iedName);
				for(Element ldevice : ldevices){
					ldevice.addAttribute("iedName", iedName);
				}
				lstOldLDevice.addAll(ldevices);
			}
		}
		List<String> lstRe = ControlBlockCheck.checkControl(lstNewLDevice,
				lstOldLDevice);
		if (lstRe.size() > 0) {
			for (String re : lstRe) {
				textlog(re);
			}
		} else {
			textlog("\t\n控制块检查....OK");
		}
	}
	
	/**
	 * 检查与一次设备关联的LNode lnType与LN lnType是否一致
	 * @param root
	 */
	public List<String> checkPrimaryDeviceLNType(Element root) {
		LNTypeInSubstationCheck lnTypeCheck = LNTypeInSubstationCheck
				.newInstance();
		List<String> lstRe = lnTypeCheck.check(root, allName);
		if (lstRe.size() > 0) {
			for (String re : lstRe) {
				textlog(re);
			}
		}
		return lstRe;
	}
	
	private void textlog(final List<String> msgs) {
		StringBuilder allSb = new StringBuilder();
		for (String msg : msgs) {
			allSb.append(msg);
		}
		textlog(allSb.toString());
	}

	private void textlog(final String str) {
		if (mvDialog != null && !StringUtils.isEmpty(str))
			mvDialog.textAppend(str);
	}

	private void labellog(final String str) {
		if (mvDialog != null)
			mvDialog.labelPrint(str);
	}
	
	private void progressMove(final int i) {
		if (mvDialog != null)
			mvDialog.moveProgress(i);
	}

	private void setProgressMaxValue(final int i) {
		if (mvDialog != null)
			mvDialog.setProgressMaxCount(i);
	}
	
	private void setProgressVisual(final boolean state) {
		if (mvDialog != null)
			mvDialog.setProgressVisual(state);
	}

	public void setAllName(List<String> allName) {
		this.allName = allName;
	}

	/**
	 * @return the checkIED
	 */
	public boolean isCheckIED() {
		return checkIED;
	}

	/**
	 * @param checkIED the checkIED to set
	 */
	public void setCheckIED(boolean checkIED) {
		this.checkIED = checkIED;
	}
}
