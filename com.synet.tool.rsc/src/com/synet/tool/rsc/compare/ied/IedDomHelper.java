package com.synet.tool.rsc.compare.ied;

import java.io.File;
import java.util.List;

import org.dom4j.Element;

import com.shrcn.found.common.Constants;
import com.shrcn.found.common.dict.DictManager;
import com.shrcn.found.file.util.FileManager;
import com.shrcn.found.file.util.FileManipulate;
import com.shrcn.found.file.xml.DOM4JNodeHelper;
import com.synet.tool.rsc.DBConstants;
import com.synet.tool.rsc.io.ied.Context;
import com.synet.tool.rsc.io.ied.NetConfig;
import com.synet.tool.rsc.io.parser.ParserUtil;
import com.synet.tool.rsc.io.scd.IedInfoDao;
import com.synet.tool.rsc.io.scd.SclUtil;
import com.synet.tool.rsc.model.Tb1046IedEntity;

public class IedDomHelper {

	private String iedName;
	private String iedSaveName;
	private Context context;
	
	private Element root;
	private Element ndPins;
	private Element ndDsSettings;
	private Element ndDsParams;
	private Element ndRcbs;
	private Element ndGooses;
	private Element ndSmvs;
	private Element ndInputs;
	
	public IedDomHelper(String iedName, Context context) {
		this.iedSaveName = iedName;
		if (iedName.endsWith("_old") || iedName.endsWith("_new")) {
			this.iedName = iedName.substring(0, iedName.length() - 4);
		} else {
			this.iedName = iedName;
		}
		this.context = context;
		init();
	}

	private void init() {
		this.root = DOM4JNodeHelper.createRTUNode("IED");
		this.ndPins = root.addElement("Pins");
		this.ndDsSettings = root.addElement("DsSettings");
		this.ndDsParams = root.addElement("DsParams");
		this.ndRcbs = root.addElement("Rcbs");
		this.ndGooses = root.addElement("Gooses");
		this.ndSmvs = root.addElement("Smvs");
		this.ndInputs = root.addElement("Inputs");
	}
	
	/**
	 * 保存装置属性
	 * @param ied
	 */
	public void saveIED(Tb1046IedEntity ied) {
		root.addAttribute("type", ied.getF1046Model());
		root.addAttribute("manufacturer", ied.getF1046Manufacturor());
		root.addAttribute("configVersion", ied.getF1046ConfigVersion());
		root.addAttribute("crc", ied.getF1046Crc());
		root.addAttribute("ipA", ied.getF1046aNetIp());
		root.addAttribute("ipB", ied.getF1046bNetIp());
		if (ndRcbs.elements().size() < 1) {
			if (ndSmvs.elements().size() > 0) {			// 合并单元
				ied.setF1046Type(DBConstants.IED_MU);
			} else {										// 智能终端
				ied.setF1046Type(DBConstants.IED_TERM);
			}
		}
		Integer f1046Type = ied.getF1046Type();
		root.addAttribute("iedtype", DictManager.getInstance().getNameById("IED_TYPE", f1046Type));
	}
	
	/**
	 * 保存输入虚端子
	 * @param ref
	 * @param desc
	 */
	public void savePins(String ref, String desc) {
		ndPins.addAttribute("ref", ref);
		ndPins.addAttribute("desc", desc);
	}
	
	/**
	 * 保存定值控制块
	 * @param sgcb
	 */
	public void saveDsSetting(Element elDat, Element elLd) {
		Element ndSGCB = ndDsSettings.addElement("SGCB");
		String cbName = "SGCB";
		String ldInst = elLd.attributeValue("inst");
		String cbRef = ldInst + "/LLN0$SP$SGCB";
		ndSGCB.addAttribute("cbName", cbName);
		ndSGCB.addAttribute("cbRef", cbRef);
		ndSGCB.addAttribute("dsName", elDat.attributeValue("name"));
		ndSGCB.addAttribute("dsDesc", elDat.attributeValue("desc"));
		saveFCDAs(ndSGCB, elDat, elLd);
		appendMd5(ndSGCB);
	}
	
	/**
	 * 保存参数数据集
	 * @param elDat
	 * @param elLd
	 */
	public void saveDsParams(Element elDat, Element elLd) {
		Element ndDs = ndDsParams.addElement("DataSet");
		ndDs.addAttribute("name", elDat.attributeValue("name"));
		ndDs.addAttribute("desc", elDat.attributeValue("desc"));
		saveFCDAs(ndDs, elDat, elLd);
		appendMd5(ndDs);
	}
	
	/**
	 * 保存报告控制块
	 * @param elDat
	 * @param elRcb
	 * @param elLd
	 */
	public void saveRcbs(Element elDat, Element elRcb, Element elLd) {
		Element ndRCB = ndRcbs.addElement("Rcb");
		String datSet = elDat.attributeValue("name");
		ndRCB.addAttribute("dsName", datSet);
		ndRCB.addAttribute("dsDesc", elDat.attributeValue("desc"));
		boolean isBrcb = IedInfoDao.isBrcb(elRcb);
		int brcb = isBrcb ? 1 : 0;
		ndRCB.addAttribute("brcb", brcb + "");
		String ldInst = elLd.attributeValue("inst");
		String cbName = elRcb.attributeValue("name");
		String cbId = ParserUtil.getCbId(ldInst, cbName, isBrcb ? "BR" : "RP");
		ndRCB.addAttribute("cbRef", cbId);
		ndRCB.addAttribute("cbType", IedInfoDao.getRcbType(datSet) + "");
		saveFCDAs(ndRCB, elDat, elLd);
		appendMd5(ndRCB);
	}
	
	/**
	 * 保存Goose控制块
	 * @param elDat
	 * @param cbNd
	 * @param elLd
	 */
	public void saveGoose(Element elDat, Element cbNd, Element elLd) {
		Element ndGoose = ndGooses.addElement("Goose");
		String datSet = elDat.attributeValue("name");
		String cbName = cbNd.attributeValue("name");
		String ldInst = elLd.attributeValue("inst");
		String cbRef = ParserUtil.getCbRef(iedName, ldInst, cbName, "GO");
		String cbId = ParserUtil.getCbId(ldInst, cbName, "GO");
		ndGoose.addAttribute("dsName", datSet);
		ndGoose.addAttribute("dsDesc", elDat.attributeValue("desc"));
		ndGoose.addAttribute("cbName", cbName);
		ndGoose.addAttribute("cbRef", cbId);
		NetConfig netCfg = context.getNetConfig(cbRef);
		if (netCfg != null) {
			ndGoose.addAttribute("macAddr", netCfg.getMACAddr());
			ndGoose.addAttribute("vlanid", netCfg.getVLANID());
			ndGoose.addAttribute("vlanPriority", netCfg.getVLANPriority());
			ndGoose.addAttribute("appid", netCfg.getAPPID());
		} else {
			context.addError(iedName, "GOOSE", cbRef, "通信参数未定义。");
		}
		saveFCDAs(ndGoose, elDat, elLd);
		appendMd5(ndGoose);
	}
	
	/**
	 * 保存Smv控制块
	 * @param elDat
	 * @param cbNd
	 * @param elLd
	 */
	public void saveSmv(Element elDat, Element cbNd, Element elLd) {
		Element ndSmv = ndSmvs.addElement("Smv");
		String datSet = elDat.attributeValue("name");
		String cbName = cbNd.attributeValue("name");
		String ldInst = elLd.attributeValue("inst");
		String cbRef = ParserUtil.getCbRef(iedName, ldInst, cbName, "MS");
		String cbId = ParserUtil.getCbId(ldInst, cbName, "MS");
		ndSmv.addAttribute("dsName", datSet);
		ndSmv.addAttribute("dsDesc", elDat.attributeValue("desc"));
		ndSmv.addAttribute("cbName", cbName);
		ndSmv.addAttribute("cbRef", cbId);
		NetConfig netCfg = context.getNetConfig(cbRef);
		if (netCfg != null) {
			ndSmv.addAttribute("macAddr", netCfg.getMACAddr());
			ndSmv.addAttribute("vlanid", netCfg.getVLANID());
			ndSmv.addAttribute("vlanPriority", netCfg.getVLANPriority());
			ndSmv.addAttribute("appid", netCfg.getAPPID());
		} else {
			context.addError(iedName, "SMV", cbRef, "通信参数未定义。");
		}
		saveFCDAs(ndSmv, elDat, elLd);
		appendMd5(ndSmv);
	}
	
	/**
	 * 保存信号关联
	 * @param indesc
	 * @param intAddr
	 * @param extIedName
	 * @param fcdaRef
	 */
	public void saveExtRef(String indesc, String intAddr, String extIedName, String fcdaRef) {
		Element ndExtRef = ndInputs.addElement("ExtRef");
		ndExtRef.addAttribute("intDesc", indesc);
		ndExtRef.addAttribute("intAddr", intAddr);
		ndExtRef.addAttribute("iedName", extIedName);
		ndExtRef.addAttribute("fcdaRef", fcdaRef);
	}
	
//	/**
//	 * 保存数据集
//	 * @param ndParent
//	 * @param elDat
//	 * @param elLd
//	 */
//	private void addDataSet(Element ndParent, Element elDat, Element elLd) {
//		Element ndDs = ndParent.addElement("DataSet");
//		ndDs.addAttribute("name", elDat.attributeValue("name"));
//		ndDs.addAttribute("desc", elDat.attributeValue("desc"));
//		saveFCDAs(ndDs, elDat, elLd);
//		appendMd5(ndDs);
//	}

	/**
	 * 保存数据项
	 * @param ndDs
	 * @param elDat
	 * @param elLd
	 */
	private void saveFCDAs(Element ndDs, Element elDat, Element elLd) {
		List<Element> elFcdas = elDat.elements("FCDA");
		int i = 0;
		for (Element fcdaEl : elFcdas) {
			String fcdaDesc = SclUtil.getFCDADesc(elLd, fcdaEl);
			Element ndFCDA = ndDs.addElement("FCDA");
			ndFCDA.addAttribute("index", i + "");
			ndFCDA.addAttribute("desc", fcdaDesc);
			ndFCDA.addAttribute("ref", SclUtil.getFcdaRef(fcdaEl));
			ndFCDA.addAttribute("datType", context.getBType(elLd, fcdaEl) + "");
			ndFCDA.addAttribute("fc", fcdaEl.attributeValue("fc"));
			i++;
		}
	}
	
	/**
	 * 保存配置
	 */
	public String saveDom() {
		String md5 = calcMd5(ndPins,
					ndDsSettings,
					ndDsParams,
					ndRcbs,
					ndGooses,
					ndSmvs,
					ndInputs);
		root.addAttribute("md5", md5);
		FileManipulate.initDir(Constants.tempDir);
		String path = Constants.tempDir + File.separator + iedSaveName + ".rsc";
		FileManager.saveTextFile(path , root.asXML(), "UTF-8");
		return path;
	}
	
	private String calcMd5(Element...nds) {
		String md5 = "";
		for (Element nd : nds) {
			md5 += appendMd5(nd);
		}
		return FileManipulate.getMD5CodeForStr(md5);
	}
	
	private String appendMd5(Element nd) {
		String md5 = FileManipulate.getMD5CodeForStr(nd.asXML());
		nd.addAttribute("md5", md5);
		return md5;
	}
	
	public Element getDomRoot() {
		return root;
	}
}
