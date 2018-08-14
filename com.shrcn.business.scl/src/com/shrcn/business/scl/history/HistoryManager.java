/**
 * Copyright (c) 2007-2009 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based on IEC61850 SCT.
 */
package com.shrcn.business.scl.history;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.dom4j.Element;

import com.shrcn.business.scl.SCTProperties;
import com.shrcn.business.scl.history.MarkInfo.DevType;
import com.shrcn.business.scl.history.MarkInfo.IedCfg;
import com.shrcn.business.scl.history.MarkInfo.OperType;
import com.shrcn.found.common.event.EventConstants;
import com.shrcn.found.common.event.EventManager;
import com.shrcn.found.file.xml.DOM4JNodeHelper;
import com.shrcn.found.xmldb.XMLDBHelper;

/**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2009-12-9
 */
/**
 * $Log: HistoryManager.java,v $
 * Revision 1.1  2013/03/29 09:38:25  cchun
 * Add:创建
 *
 * Revision 1.11  2012/01/31 01:37:30  cchun
 * Update:将XMLDBHelper.executeUpdate()改成XMLDBHelper.insert()
 *
 * Revision 1.10  2012/01/13 08:39:49  cchun
 * Refactor:为方便数据库切换改用新接口
 *
 * Revision 1.9  2010/07/29 09:56:22  cchun
 * Update:区分单双引号
 *
 * Revision 1.8  2010/05/26 08:08:53  cchun
 * Update:修改注释
 *
 * Revision 1.7  2010/04/23 03:22:25  cchun
 * Update:历史记录接口添加oid参数
 *
 * Revision 1.6  2010/01/19 09:02:44  lj6061
 * add:统一国际化工程
 *
 * Revision 1.5  2009/12/30 03:12:14  cchun
 * Update:修改<Hitem>插入顺序
 *
 * Revision 1.4  2009/12/11 07:08:40  cchun
 * Update:调整序号获取时机
 *
 * Revision 1.3  2009/12/09 08:21:49  cchun
 * Update:将int改成enum
 *
 * Revision 1.2  2009/12/09 07:24:39  cchun
 * Update:添加注释
 *
 * Revision 1.1  2009/12/09 07:16:12  cchun
 * Add:添加历史记录工具类
 *
 */
public class HistoryManager {
	
	public static final String TIME_FORMAT = "yyyy-MM-dd HH:mm:ss"; //$NON-NLS-1$
	public static final String EDIT = "EDIT"; //$NON-NLS-1$
	
	private boolean versioned = false;
	private boolean reversioned = false;
	
	private static volatile HistoryManager instance = null;
	
	private HistoryManager(){
	}
	
	public static HistoryManager getInstance() {
		if(null == instance) {
			synchronized (HistoryManager.class) {
				if(null == instance) {
					instance = new HistoryManager();
				}
			}
		}
		return instance;
	}
	
	public boolean isVersioned() {
		return versioned;
	}

	public boolean isReversioned() {
		return reversioned;
	}
	
	public void reset() {
		versioned = false;
		reversioned = false;
	}

	/**
	 * 刷新。
	 */
	public void refresh() {
		VersionSequence.getInstance().refresh();
	}
	
	/**
	 * 获取当前时间
	 * @return
	 */
	public String currentTime() {
		SimpleDateFormat format = new SimpleDateFormat(TIME_FORMAT);
		return format.format(new Date());
	}
	
	/**
	 * 添加修改记录
	 * @param mark
	 */
	public void mark(MarkInfo mark) {
		if(!isNeedMark())
			return;
		String reason = mark.getContent();
		//判断是否存在<History/>
		String hisPath = "/scl:SCL/scl:Header/scl:History"; //$NON-NLS-1$
		if(!XMLDBHelper.existsNode(hisPath)) {
			XMLDBHelper.insert("/scl:SCL/scl:Header", DOM4JNodeHelper.createSCLNode("History"));
		}
		if (mark.getType()==DevType.IED && (mark.getOpType() == OperType.ADD || mark.getOpType() == OperType.DELETE)) {
			versioned = true;
			reversioned = false;
		} else {
			versioned = false;
			reversioned = true;
		}
		// 保存到xmldb
		XMLDBHelper.insertAsLast("/scl:SCL/scl:Header/scl:History", getHitem(reason));
		EventManager.getDefault().notify(EventConstants.REFRESH_HISTORY, null);
	}

	/**
	 * 得到<Hitem/>信息
	 * @param reason
	 * @return
	 */
	private Element getHitem(String reason) {
		VersionSequence sequence = VersionSequence.getInstance();
		float[] next = sequence.next(versioned);
		String version = Float.toString(next[0]);
		String revision = Float.toString(next[1]);
		
		Element itNode = DOM4JNodeHelper.createSCLNode("Hitem");
		itNode.addAttribute("version", version);
		itNode.addAttribute("revision", revision);
		itNode.addAttribute("what", reason);
		itNode.addAttribute("who", "");
		itNode.addAttribute("when", currentTime());
		itNode.addAttribute("why", EDIT);
		return itNode;
	}
	
	/**
	 * 记录非通信模块的Add,Delete,Update操作
	 * @param devType 包括IED, BAY, STA, VOL, SUBNET
	 * @param opType 包括Add,Delete,Update
	 * @param name 设备名称
	 * @param oid
	 */
	public void markDevChanged(DevType devType, OperType opType, String name, String oid) {
		mark(new DeviceInfo(devType, opType, name, oid));
	}

	/**
	 * 记录非通信模块的改名历史，即Rename
	 * @param devType 目前只有STA,IED,BAY支持改名
	 * @param oldName 旧名称
	 * @param newName 新名称
	 */
	public void markRename(DevType devType, String oldName, String newName, String oid) {
		mark(new DeviceInfo(devType, OperType.RENAME, oldName, newName, oid));
	}
	
	/**
	 * 记录通信配置修改历史记录
	 * @param cfgType
	 * @param subNet
	 * @param iedName
	 * @param apName
	 * @param attrName
	 * @param oldValue
	 * @param newValue
	 */
	private void markComAttrChanged(IedCfg cfgType, String subNet, String iedName, String apName, 
			String attrName, String oldValue, String newValue) {
		mark(new ComInfo(cfgType, OperType.UPDATE, subNet, iedName, apName, 
				attrName, oldValue, newValue));
	}
	
	public void markMmsChanged(String subNet, String iedName, String apName, 
			String attrName, String oldValue, String newValue) {
		markComAttrChanged(IedCfg.MMS, subNet, iedName, apName, attrName, oldValue, newValue);
	}
	
	public void markGooseChanged(String subNet, String iedName, String apName, 
			String attrName, String oldValue, String newValue) {
		markComAttrChanged(IedCfg.GOOSE, subNet, iedName, apName, attrName, oldValue, newValue);
	}
	
	public void markSmvChanged(String subNet, String iedName, String apName, 
			String attrName, String oldValue, String newValue) {
		markComAttrChanged(IedCfg.SMV, subNet, iedName, apName, attrName, oldValue, newValue);
	}
	
	/**
	 * 记录IED控制块增加、删除
	 * @param cfgType
	 * @param opType
	 * @param cbRef
	 */
	public void markIedCfgChanged(IedCfg cfgType, OperType opType, String cbRef) {
		mark(new IedCfgInfo(cfgType, opType, cbRef));
	}
	
	/**
	 * 记录IED控制块属性修改
	 * @param cfgType
	 * @param opType
	 * @param cbRef
	 * @param attrName
	 * @param oldValue
	 * @param newValue
	 */
	public void markIedCfgUpdate(IedCfg cfgType, String cbRef, 
			String attrName, String oldValue, String newValue) {
		mark(new IedCfgInfo(cfgType, cbRef, attrName, oldValue, newValue));
	}
	
	/**
	 * 记录描述修改
	 * @param ref
	 * @param desc
	 * @param descNew
	 */
	public void markDescChanged(String ref, String desc, String descNew) {
		mark(new DescInfo(ref, desc, descNew));
	}

	public boolean isNeedMark() {
		return SCTProperties.getInstance().needsHistory();
	}

	public void setNeedMark(boolean needMark) {
		SCTProperties.getInstance().setHistory(needMark);
	}
	
	public boolean hasHistory() {
		if (!isVersioned() && !isReversioned()) {
			return false;
		}
		return true;
	}
}
