/**
 * Copyright (c) 2007, 2008 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based Visual Device Develop System.
 */
package com.shrcn.business.scl.das;

import java.util.List;

import org.dom4j.Attribute;
import org.dom4j.Element;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;

import com.shrcn.business.scl.model.SCL;
import com.shrcn.found.common.util.StringUtil;
import com.shrcn.found.file.xml.DOM4JNodeHelper;
import com.shrcn.found.ui.util.TaskManager;
import com.shrcn.found.xmldb.XMLDBHelper;

/**
 * 
 * @author 刘静(mailto:lj6061@shrcn.com)
 * @version 1.0, 2009-4-21
 */
/*
 * 修改历史 $Log: ControlDAO.java,v $
 * 修改历史 Revision 1.1  2013/03/29 09:36:20  cchun
 * 修改历史 Add:创建
 * 修改历史
 * 修改历史 Revision 1.31  2012/03/09 07:35:51  cchun
 * 修改历史 Update:规范prefix和daName属性用法
 * 修改历史
 * 修改历史 Revision 1.30  2012/01/17 08:50:29  cchun
 * 修改历史 Update:使用更加安全的xpath形式
 * 修改历史
 * 修改历史 Revision 1.29  2011/11/21 06:40:46  cchun
 * 修改历史 Fix Bug:修复“控制”超时时间自动转成控制方式文字描述的bug
 * 修改历史
 * 修改历史 Revision 1.28  2011/05/12 07:55:18  cchun
 * 修改历史 Fix Bug:修复xpath不能查询LN0的bug
 * 修改历史
 * 修改历史 Revision 1.27  2010/12/07 09:37:47  cchun
 * 修改历史 Update:去掉访问点过滤
 * 修改历史
 * 修改历史 Revision 1.26  2010/11/08 07:13:31  cchun
 * 修改历史 Update:清理引用
 * 修改历史
 * 修改历史 Revision 1.25  2010/09/07 09:59:32  cchun
 * 修改历史 Update:更换过时接口
 * 修改历史
 * 修改历史 Revision 1.24  2010/05/26 08:10:32  cchun
 * 修改历史 Update:修改构造函数参数
 * 修改历史
 * 修改历史 Revision 1.23  2009/12/11 05:26:47  cchun
 * 修改历史 Update:添加修改历史标记
 * 修改历史
 * 修改历史 Revision 1.22  2009/09/23 08:37:55  lj6061
 * 修改历史 修改空指针异常
 * 修改历史
 * 修改历史 Revision 1.21  2009/09/21 05:59:51  lj6061
 * 修改历史 删除: Xpath带有空格
 * 修改历史
 * 修改历史 Revision 1.20  2009/08/25 05:58:52  lj6061
 * 修改历史 控制部分Bug
 * 修改历史
 * 修改历史 Revision 1.19  2009/08/18 09:36:09  cchun
 * 修改历史 Update:合并代码
 * 修改历史
 * 修改历史 Revision 1.18.2.3  2009/08/06 02:07:29  lj6061
 * 修改历史 替换通过Doc方法插入节点
 * 修改历史
 * 修改历史 Revision 1.18.2.2  2009/07/31 09:37:12  cchun
 * 修改历史 Update:修改命名控件处理方式
 * 修改历史
 * 修改历史 Revision 1.18.2.1  2009/07/24 07:10:40  cchun
 * 修改历史 Update:切换数据库Sedna
 * 修改历史
 * 修改历史 Revision 1.18  2009/07/21 00:46:12  lj6061
 * 修改历史 对控制信息中短地址修改
 * 修改历史
 * 修改历史 Revision 1.17  2009/07/20 06:34:46  lj6061
 * 修改历史 1.修改了多个CtlVal取SDI= ‘oper’为短地址
 * 修改历史 2.修改方法提高效率
 * 修改历史
 * 修改历史 Revision 1.16  2009/07/17 09:38:54  lj6061
 * 修改历史 添加编译时间
 * 修改历史
 * 修改历史 Revision 1.15  2009/07/17 08:36:03  pht
 * 修改历史 遥控加超时时间。
 * 修改历史
 * 修改历史 Revision 1.14  2009/07/01 06:42:23  lj6061
 * 修改历史 由于DOI描述改为由dU获取，修改后一并添加
 * 修改历史
 * 修改历史 Revision 1.13  2009/06/25 05:41:33  lj6061
 * 修改历史 DAI VAl文件不统一处理
 * 修改历史
 * 修改历史 Revision 1.12  2009/06/17 03:35:51  cchun
 * 修改历史 Thread改成Job
 * 修改历史
 * 修改历史 Revision 1.11.2.1  2009/06/17 03:31:55  cchun
 * 修改历史 Thread改成Job
 * 修改历史
 * 修改历史 Revision 1.11  2009/06/11 03:51:42  lj6061
 * 修改历史 添加线程操作修改
 * 修改历史
 * 修改历史 Revision 1.10  2009/06/10 04:53:57  lj6061
 * 修改历史 加入修改线程处理
 * 修改历史
 * 修改历史 Revision 1.9  2009/04/29 02:23:34  cchun
 * 修改历史 统一数据操作对象接口
 * 修改历史
 * 修改历史 Revision 1.8  2009/04/28 07:50:52  lj6061
 * 修改历史 添加GSE功能
 * 修改历史
 * 修改历史 Revision 1.7  2009/04/27 03:18:00  lj6061
 * 修改历史 update:创建Name=pos的节点
 * 修改历史
 * 修改历史 Revision 1.6  2009/04/24 06:24:09  lj6061
 * 修改历史 修改时判断Pos节点存在否
 * 修改历史
 * 修改历史 Revision 1.5  2009/04/24 05:23:21  lj6061
 * 修改历史 修改desc Bug
 * 修改历史
 * 修改历史 Revision 1.4  2009/04/23 11:50:38  lj6061
 * 修改历史 添加LN的删除和刷新
 * 修改历史
 * 修改历史 Revision 1.3  2009/04/22 10:19:21  lj6061
 * 修改历史 update:控制中的Val
 * 修改历史
 * 修改历史 Revision 1.2  2009/04/22 08:36:54  lj6061
 * 修改历史 修改表记录分离
 * 修改历史
 * 修改历史 Revision 1.1  2009/04/22 01:27:21  lj6061
 * 修改历史 重构并添加ControlDAO
 * 修改历史
 */
public class ControlDAO extends IEDEditDAO {
	
	/** DA VAL选项. */
	public static final String[] COMB_ITEM = new String[] { "status-only",
			"direct-with-normal-security", "sbo-with-normal-security",
			"direct-with-enhanced-security", "sbo-with-enhanced-security" };
	
	private List<Element> data = null;
	
	public ControlDAO(String iedName, String ldInst) {
		super(iedName, ldInst);
		// 初始化界面时候取数据
		reLoad();
	}
	
	
	public void reLoad() {
		this.data = queryCSWILN();
	}
	
	/**
	 * 获取当前AP,LD对应的LN
	 * 
	 * @param iedXpath
	 * @return LN Element List
	 */
	public List<Element> getCSWILN() {
		return this.data;
	}
	
	/**
	 * 获取当前IED下所有的lnClass为CSWI的LN
	 * 
	 * @param iedXPath
	 * @return AP name值数组
	 */
	private List<Element> queryCSWILN(){
		String lnpath = getLdXpath() + "/*[@lnClass='CSWI']";
		List<Element> items = XMLDBHelper.selectNodes(lnpath);
		return items;
	}
	
	/**
	 * 获取控制界面的ref列
	 */
	public String getRef(Element ele) {
		String inst = StringUtil.nullToEmpty(ele.attributeValue("inst"));
		String perfix = StringUtil.nullToEmpty(ele.attributeValue("prefix"));
		return perfix + "CSWI" + inst + "$Pos";
	}

	/**
	 * 获取控制界面的描述列
	 */
	public String getDesc(Element ele) {
		Element eDesc = DOM4JNodeHelper.selectSingleNode(ele, "./*[name()='DOI'][@name='Pos']");
		String desc = "";
		if (eDesc != null){
			desc = StringUtil.nullToEmpty(eDesc.attributeValue("desc"));
			if ("".equals(desc)) {
				Element dU = DOM4JNodeHelper.selectSingleNode(ele, "./*[name()='DOI'][@name='Pos']/*[name()='DAI'][@name='dU']");
				if(dU !=null)
					desc = StringUtil.nullToEmpty(dU.elementText("Val"));
			}
		}
		return desc;	
	}
	
	/**
	 * 获取控制界面的DAI下的Val列
	 */
	public String getVal(Element ele) {
		Element eVal = DOM4JNodeHelper.selectSingleNode(ele,"./*[name()='DOI'][@name='Pos']/*[name()='DAI'][@name='ctlModel']");
		String val = "";
		if (eVal != null)
			val = StringUtil.nullToEmpty(eVal.elementText("Val"));
		return transformNumToVal(val);
	}
	
	/**
	 * 由于文件格式不统一，因此在获取Val时候有可能数字，可能字符串 
	 * 对数字的转换为对应字符串
	 */
	private String transformNumToVal(String val) {
		String dAIVal = val;
		if (isNum(val)) {
			int index = Integer.parseInt(val);
			dAIVal = COMB_ITEM[index];
		}
		return dAIVal;
	}
	
	/**
	 * 由于文件格式不统一，因此在获取Val时候有可能数字，可能字符串 
	 * 将字符串转换对应数字
	 */
	private String transformValToNum(String val) {
		String index = val;
		if (!isNum(val)) {
			for (int i = 0; i < COMB_ITEM.length; i++) {
				if(COMB_ITEM[i].equals(val)){
					index = Integer.toString(i);
					break;
				}
			}
		}
		return index;
	}
	
	/**
	 * 判断是否是数字 0-4之间
	 */
	private boolean isNum(String val){
		if (("01234".indexOf(val) >= 0) && val.length() == 1) {
			return true;
		}else
			return false;
	}
	
	/**
	 * 获取控制界面的DAI中的Address列
	 * 
	 */
	public String getAddr(Element ele) {
		String addrpath="./*[name()='DOI'][@name='Pos']/descendant::*[name()='SDI'][@name='Oper']/*[name()='DAI'][@name='ctlVal']";
		Element eAddr = DOM4JNodeHelper.selectSingleNode(ele,addrpath);
		String addr = "";
		if (eAddr != null)
			addr = StringUtil.nullToEmpty(eAddr.attributeValue("sAddr"));
		return addr;
	}
	/**
	 * 
	 * @author puhongtao 功能： 获取遥控超时时间
	 * @param ele
	 * @return
	 */
	public String getSboTimeOut(Element ele){
		String sboPath = "./*[name()='DOI'][@name='Pos']/*[name()='DAI'][@name='sboTimeout']";
		Element eVal = DOM4JNodeHelper.selectSingleNode(ele, sboPath);
		String val = "";
		if (eVal != null)
			val = StringUtil.nullToEmpty(eVal.elementText("Val"));
		return val;
	}
	
	/**
	 * 获得LD的路径
	 */
	private String getLdXpath(){
		String ldXPath = iedXPath + "/scl:AccessPoint/scl:Server/scl:LDevice[@inst='" + ldInst + "']";
		return ldXPath;
	}
	/**
	 * 获得唯一确定的LN
	 * @param modify 修改内容
	 * @param inst 必须存在的字段！作为条件取的固定某条记录
	 * @param prefix 必须存在的字段！作为条件取的固定某条记录
	 */
	private String getSelect(String prefix, String lnClass, String inst){
		return getLdXpath() + SCL.getLNXPath(prefix, lnClass, inst);
	}
	
	public static boolean insertNodeInLn(Element ele, String select) {
		boolean flag = false;
		try {
			XMLDBHelper.insertAsFirst(select, ele);
			flag = true;
		} catch (Exception e) {
		}
		return flag;
	}
	
	/**
	 * 修改DOI的描述信息
	 * 
	 * @param modify 修改内容
	 * @param inst 必须存在的字段！作为条件取的固定某条记录
	 * @param prefix 必须存在的字段！作为条件取的固定某条记录
	 */
	public void updateDesc(final String modify, final String prefix, final String inst) {
		TaskManager.addTask(new Job("") {
			protected IStatus run(IProgressMonitor monitor) {
				String select = getSelect(prefix, "CSWI", inst);	
				String selPos = "/scl:DOI[@name='Pos']";
				Element ele = XMLDBHelper.selectSingleNode(select + selPos);
				if (ele == null){
					Element eleDo = DOM4JNodeHelper.createSCLNode("DOI");
					eleDo.addAttribute("name", "Pos");
					eleDo.addAttribute("desc", modify);
					Element dAI = eleDo.addElement("DAI");
					dAI.addAttribute("name", "dU");
					Element val = dAI.addElement("Val");
					val.addText(modify);
					insertNodeInLn(eleDo ,select);
				}else{
					Attribute attr = ele.attribute("desc");
					if (attr == null)
						ele.addAttribute("desc", modify);
					else {
						attr.setValue(modify);
					}
					select +=selPos;
					replaceControl(ele, select, modify, "dU");
				}
				return Status.OK_STATUS;
			}
		});
	}

	/**
	 * 修改DAI 的ctlModel 对应的Val值
	 * 
	 * @param modify 修改内容
	 * @param inst 必须存在的字段！作为条件取的固定某条记录
	 * @param prefix 必须存在的字段！作为条件取的固定某条记录
	 */
	public void updateCtlVal(String modify, final String prefix, final String inst) {
		if(modify.equals(""))return;
		final String daiVal = transformValToNum(modify);
		TaskManager.addTask(new Job("") {
			@Override
			protected IStatus run(IProgressMonitor monitor) {
				String select = getSelect(prefix, "CSWI", inst);	
				String selPos = "/scl:DOI[@name='Pos']";
				Element ele = XMLDBHelper.selectSingleNode(select + selPos);
				if (ele == null){
					Element eleDo = DOM4JNodeHelper.createSCLNode("DOI");
					eleDo.addAttribute("name", "Pos");
					Element dAI = eleDo.addElement("DAI");
					dAI.addAttribute("name", "ctlModel");
					Element val = dAI.addElement("Val");
					val.addText(daiVal);
					insertNodeInLn(eleDo ,select);
				}else{
					select +=selPos;
					replaceControl(ele, select, daiVal, "ctlModel");
				}
				return Status.OK_STATUS;
			}
		});
	}
	
	

	/**
	 * 修改遥控超时时间
	 * @param modify
	 * @param prefix
	 * @param inst
	 */
	public void updateSboTimeOut(final String modify, final String prefix, final String inst) {
		if(modify.equals(""))return;
		TaskManager.addTask(new Job("") {
			@Override
			protected IStatus run(IProgressMonitor monitor) {
				String select = getSelect(prefix, "CSWI", inst);	
				String selPos = "/scl:DOI[@name='Pos']";
				Element ele = XMLDBHelper.selectSingleNode(select + selPos);
				if (ele == null){
					Element eleDo = DOM4JNodeHelper.createSCLNode("DOI");
					eleDo.addAttribute("name", "Pos");
					Element dAI = eleDo.addElement("DAI");
					dAI.addAttribute("name", "sboTimeout");
					Element val = dAI.addElement("Val");
					val.addText(modify);
					insertNodeInLn(eleDo ,select);
				}else{
					select +=selPos;
					replaceControl(ele, select, modify, "sboTimeout");
				}
				return Status.OK_STATUS;
			}
		});
	}
	
	/***
	 * 
	 * 公共方法，替换节点信息
	 * @param ele
	 * @param select
	 * @param modify
	 * @param eleType
	 */
	public static void replaceControl(Element ele,String select,String modify,String eleType){
		String xpath = "./*[name()='DAI'][@name = '" + eleType + "']";
		Element du = DOM4JNodeHelper.selectSingleNode(ele, xpath);
		if(du == null){
			Element duEle = ele.addElement("DAI");
			duEle.addAttribute("name", eleType);
			Element valEle = duEle.addElement("Val");
			valEle.addText(modify);
		}else{
			Element val = du.element("Val");
			if(val == null){
				du.addElement("Val").addText(modify);
			}else{
				val.setText(modify);
			}
		}
		XMLDBHelper.replaceNode(select, ele);
	}
	
	public void setLdInst(String ldInst) {
		this.ldInst = ldInst;
	}

}
