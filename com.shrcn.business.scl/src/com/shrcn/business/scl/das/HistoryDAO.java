/**
 * Copyright (c) 2007, 2008 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based Visual Device Develop System.
 */
package com.shrcn.business.scl.das;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.dom4j.Element;

import com.shrcn.business.scl.history.VersionSequence;
import com.shrcn.business.scl.model.Hitem;
import com.shrcn.found.common.Constants;
import com.shrcn.found.dbxapi.io.ScdSaxParser;
import com.shrcn.found.dbxapi.io.ScdSection;
import com.shrcn.found.file.xml.DOM4JNodeHelper;
import com.shrcn.found.xmldb.XMLDBHelper;

/**
 * 
 * @author 吴云华(mailto:wyh@shrcn.com)
 * @version 1.0, 2009-4-22
 */
/*
 * 修改历史
 * $Log: HistoryDAO.java,v $
 * Revision 1.1  2013/03/29 09:36:20  cchun
 * Add:创建
 *
 * Revision 1.4  2010/11/08 07:13:32  cchun
 * Update:清理引用
 *
 * Revision 1.3  2010/09/07 09:59:33  cchun
 * Update:更换过时接口
 *
 * Revision 1.2  2009/04/29 02:23:35  cchun
 * 统一数据操作对象接口
 *
 * Revision 1.1  2009/04/22 09:09:34  wyh
 * 添加历史DAO
 *
 */
public class HistoryDAO implements SCLDAO {

	@SuppressWarnings("unchecked")
	public List<Element> queryHItems() {
		if (Constants.FINISH_FLAG) {
			return XMLDBHelper.selectNodes("/scl:SCL/scl:Header/scl:History/scl:Hitem");
		} else {
			return ScdSaxParser.getInstance().getPart(ScdSection.History).elements();
		}
	}
	
	/**
	 * 添加历史版本信息
	 * @param hitem xml字符串
	 */
	public static void addHitem(String hitem) {
		XMLDBHelper.insertAsLast("/scl:SCL/scl:Header/scl:History", DOM4JNodeHelper.parseText2Node(hitem));
		XMLDBHelper.forceCommit();
	}
	
	/**
	 * 添加历史版本信息
	 * @param hitem Hitem对象
	 */
	public static void addHitem(Hitem hitem) {
		initParents();
		addHitem(hitem.asXML());
	}
	
	
	/**
	 * 添加历史版本信息（顺序添加）
	 * @return
	 */
	public static Element addHitem(boolean seqFlag) {
		initParents();
		
		Date date = new Date();
		SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");		 //$NON-NLS-1$
		VersionSequence sequence = VersionSequence.getInstance();
		float[] next = sequence.next(seqFlag);
		String version = Float.toString(next[0]);
		String revision = Float.toString(next[1]);
		Element his = DOM4JNodeHelper.createSCLNode("Hitem"); //$NON-NLS-1$
		his.addAttribute("version", version); //$NON-NLS-1$ //$NON-NLS-2$
		his.addAttribute("revision", revision); //$NON-NLS-1$
		his.addAttribute("when", dateformat.format(date)); //$NON-NLS-1$
		his.addAttribute("who", "Admin"); //$NON-NLS-1$ //$NON-NLS-2$
		his.addAttribute("what", ""); //$NON-NLS-1$ //$NON-NLS-2$
		his.addAttribute("why", ""); //$NON-NLS-1$ //$NON-NLS-2$
		XMLDBHelper.insertAsLast("/scl:SCL/scl:Header/scl:History", his);
		return his;
	}
	public static Element addHitemFirst() {
		initParents();
		
		Date date = new Date();
		SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");		 //$NON-NLS-1$
		VersionSequence sequence = VersionSequence.getInstance();
		sequence.setCurrReVersion(-1);
		sequence.setCurrVersion(-1);
		float[] next = sequence.next(true);
		String version = Float.toString(next[0]);
		String revision = Float.toString(next[1]);
		Element his = DOM4JNodeHelper.createSCLNode("Hitem"); //$NON-NLS-1$
		his.addAttribute("version", version); //$NON-NLS-1$ //$NON-NLS-2$
		his.addAttribute("revision", revision); //$NON-NLS-1$
		his.addAttribute("when", dateformat.format(date)); //$NON-NLS-1$
		his.addAttribute("who", "Admin"); //$NON-NLS-1$ //$NON-NLS-2$
		his.addAttribute("what", "无"); //$NON-NLS-1$ //$NON-NLS-2$
		his.addAttribute("why", "初始版本"); //$NON-NLS-1$ //$NON-NLS-2$
		XMLDBHelper.insertAsLast("/scl:SCL/scl:Header/scl:History", his);
		return his;
	}
	private static void initParents() {
		// 判断工程文件中是否存在Header节点
		boolean existHeader = XMLDBHelper.existsNode("/scl:SCL/scl:Header"); //$NON-NLS-1$
		if (!existHeader) {
			Element header = DOM4JNodeHelper.createSCLNode("Header"); //$NON-NLS-1$
			header.addAttribute("id", "SHR"); //$NON-NLS-1$ //$NON-NLS-2$
			header.addAttribute("toolID", "EASY50"); //$NON-NLS-1$ //$NON-NLS-2$
			header.addAttribute("nameStructure", "IEDName"); //$NON-NLS-1$ //$NON-NLS-2$
			header.addElement("History");
			XMLDBHelper.insertAsFirst("/scl:SCL", header); //$NON-NLS-1$
			XMLDBHelper.forceCommit();
		} else {
			// 判断工程中History节点是否存在
			boolean existHistory = XMLDBHelper.existsNode("/scl:SCL/scl:Header/scl:History"); //$NON-NLS-1$
			if (!existHistory) {
				Element history = DOM4JNodeHelper.createSCLNode("History"); //$NON-NLS-1$
				XMLDBHelper.insertAsFirst("/scl:SCL/scl:Header", history); //$NON-NLS-1$
				XMLDBHelper.forceCommit();
			}
		}
	}
	public void clear(){
		String path = "/scl:SCL/scl:Header/scl:History/scl:Hitem"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		XMLDBHelper.removeNodes(path);
		XMLDBHelper.forceCommit();
	}
	
	/**
	 * 删除指定历史版本信息
	 * @param rowSelection
	 */
	public static void removeHitem(int[] rowSelection) {
		if (rowSelection == null || rowSelection.length < 1)
			return;
		// 删除数据
		String index = "";
		for(int i=rowSelection.length; i>0; i--) { // 先删除序号大的，避免删除异常
			int row = rowSelection[i-1];
			if (index.length()>0)
				index += " or ";
			index += "position()=" + row;
		}
		//删除数据库中指定的节点
		String path = "/scl:SCL/scl:Header/scl:History/scl:Hitem" + "[" + index + "]"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		XMLDBHelper.removeNodes(path);
		XMLDBHelper.forceCommit();
	}
}
