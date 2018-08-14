/**
 * 
 */
package com.shrcn.business.scl.das;

import java.util.ArrayList;
import java.util.List;

import org.dom4j.Element;

import com.shrcn.business.scl.enums.EnumCtrlBlock;
import com.shrcn.business.scl.history.HistoryManager;
import com.shrcn.business.scl.history.MarkInfo.IedCfg;
import com.shrcn.business.scl.model.SCL;
import com.shrcn.business.scl.model.navgtree.DataSetInfo;
import com.shrcn.business.scl.model.navgtree.LNInfo;
import com.shrcn.found.common.Constants;
import com.shrcn.found.common.util.StringUtil;
import com.shrcn.found.file.xml.DOM4JNodeHelper;
import com.shrcn.found.xmldb.XMLDBHelper;

/**
 * @author zhouhuiming @ 2010-5-13 上午08:59:07
 */
public class DataSetDao extends IEDEditDAO {
	
	public DataSetDao(String iedName, String ldInst) {
		super(iedName, ldInst);
	}
	
	public DataSetDao(String iedName, String ldInst,
			String lnName) {
		super(iedName, ldInst, lnName);
	}
	
	/**
	 * 更新数据集属性
	 * @param name
	 * @param attName
	 * @param value
	 */
	public void updateDataSetAtt(String name, String attName, String value) {
		XMLDBHelper.saveAttribute(getDataSetXpath(name), attName, value); //$NON-NLS-1$
	}
	
	/**
	 * 删除数据集
	 * @param dataSetName
	 * @param ele
	 */
	public void deleteDataSet(String dataSetName){
		String dataSetXpath = getDataSetXpath(dataSetName);
		Element dataSet = XMLDBHelper.selectSingleNode(dataSetXpath);
		List<?> fcdas = dataSet.elements("FCDA");
		for (Object obj : fcdas) {
			Element fcda = (Element) obj;
			String ifExp = FCDAMgrDao.getIfString(
					fcda.attributeValue("ldInst"), 
					fcda.attributeValue("prefix"), 
					fcda.attributeValue("lnClass"), 
					fcda.attributeValue("lnInst"), 
					fcda.attributeValue("doName"));
			String refXpath = "/scl:SCL/scl:IED/scl:AccessPoint/scl:Server/scl:LDevice/*/scl:Inputs/scl:ExtRef" +
				"[@iedName='" + iedName + "']" + ifExp;
			XMLDBHelper.removeNodes(refXpath);
		}
		XMLDBHelper.removeNodes(dataSetXpath);
	}
	
	/**
	 * 增加数据集
	 * @param dataSetInfo
	 */
	public void insertDataSet(DataSetInfo dataSetInfo) {
		String datXPath = dataSetInfo.parentXPath + "/scl:DataSet[@name='" + dataSetInfo.name + "']"; //$NON-NLS-1$ //$NON-NLS-2$
		if(!XMLDBHelper.existsNode(datXPath)) {
			Element datSet = DOM4JNodeHelper.createSCLNode("DataSet"); //$NON-NLS-1$
			datSet.addAttribute("name", dataSetInfo.name); //$NON-NLS-1$
			if(null != dataSetInfo.description)
				datSet.addAttribute("desc", dataSetInfo.description); //$NON-NLS-1$
			int datSetNum = XMLDBHelper.countNodes(dataSetInfo.parentXPath + "/scl:DataSet"); //$NON-NLS-1$
			if(datSetNum > 0) {
				XMLDBHelper.insertAsLast(dataSetInfo.parentXPath, datSet);
			} else {
				XMLDBHelper.insertAsFirst(dataSetInfo.parentXPath, datSet);
			}
		}
	}

	/**
	 * 查询指定ln下数据集信息
	 * @param lnInfo
	 * @return
	 */
	public List<Element> queryDataSetByLNInfo(LNInfo lnInfo) {
		StringBuffer querySelect=new StringBuffer();
		querySelect.append(getLdXpath())
			.append(SCL.getLNXPath(lnInfo.getPrefix(), lnInfo.getLnClass(), lnInfo.getInst()))
			.append("/scl:DataSet");
		return XMLDBHelper.selectNodes(querySelect.toString());
	}
	
	/**
	 * 获取ld xpath
	 * @param querySelect
	 */
	private String getLdXpath() {
		return iedXPath + "/scl:AccessPoint/scl:Server/scl:LDevice[@inst=\"" + ldInst + "\"]/";
	}
	
	/**
	 * 获取数据集xpath
	 * @param name
	 * @return
	 */
	private String getDataSetXpath(String name) {
		return getLdXpath() + "/*/scl:DataSet[@name=\"" + name + "\"]";
	}
	
	/**
	 * 查询控制块引用信息
	 * @param name
	 * @return
	 */
	public String getRefRCBsInfo(String name) {
		List<Element> cbInfos = getRefCBs(name);
		String msg = "";
		int i = 1;
		for (Element cbInfo : cbInfos) {
			String type = cbInfo.attributeValue("type");
			String cbname = cbInfo.attributeValue("name");
			String desc = cbInfo.attributeValue("desc");
			msg += EnumCtrlBlock.valueOf(type).getDesc() + " " + cbname + " ";
			if (!StringUtil.isEmpty(desc))
				msg += "（" + desc + "）";
			if (i < cbInfos.size())
				msg += "\n";
			i++;
		}
		if (!StringUtil.isEmpty(msg))
			msg = "属于：\n" + msg;
		return msg;
	}

	public List<Element> getRefCBs(String name) {
		String condition = "[name()='" + EnumCtrlBlock.ReportControl.name() +
				"' or name()='" + EnumCtrlBlock.GSEControl.name() +
				"' or name()='" + EnumCtrlBlock.SampledValueControl.name() +
				"'][@datSet='" + name + "']";
		if (Constants.XQUERY) {
			String xquery = "for $cb in " + XMLDBHelper.getDocXPath() +
					getLdXpath() + "*/*" + condition +
					" return <CB type='{$cb/name()}' name='{$cb/@name}' desc='{$cb/@desc}'/>";
			return XMLDBHelper.queryNodes(xquery);
		} else {
			List<Element> cbEls = XMLDBHelper.selectNodes(getLdXpath() + "*/*" + condition);
			List<Element> cbs = new ArrayList<>();
			for (Element cbEl : cbEls) {
				String type = cbEl.getName();
				String cbname = cbEl.attributeValue("name");
				String cbdesc = cbEl.attributeValue("desc");
				Element cb = DOM4JNodeHelper.createSCLNode("CB");
				cb.addAttribute("type", type);
				cb.addAttribute("name", cbname);
				cb.addAttribute("desc", cbdesc);
				cbs.add(cb);
			}
			return cbs;
		}
	}
	
	public void markDataSetRefsChanged(String name) {
		List<Element> cbInfos = getRefCBs(name);
		for (Element cbInfo : cbInfos) {
			String type = cbInfo.attributeValue("type");
			String cbname = cbInfo.attributeValue("name");
			String ref = iedName + ldInst + "/LLN0$" + EnumCtrlBlock.valueOf(type).getFc() + "$" + cbname;
			HistoryManager.getInstance().markIedCfgUpdate(IedCfg.valueOf(type), ref, "datSet", "", "");
			String xpath = getLdXpath() + "/*/*[@name='" + cbname + "']";
			BlockDao.updateConfRev(xpath);
		}
	}
}
