/**
 * Copyright (c) 2007-2009 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based on IEC61850 SCT.
 */
package com.shrcn.business.scl.history;

/**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2009-12-8
 */
/**
 * $Log: MarkInfo.java,v $
 * Revision 1.1  2013/03/29 09:38:27  cchun
 * Add:创建
 *
 * Revision 1.2  2009/12/09 08:21:48  cchun
 * Update:将int改成enum
 *
 * Revision 1.1  2009/12/09 07:16:11  cchun
 * Add:添加历史记录工具类
 *
 */
public abstract class MarkInfo {
	
	//信息分隔符
	public static final String SEP = " ";
	//操作对象类型{装置，间隔，变电站，电压等级，通信}
	public enum DevType {IED, BAY, STA, VOL, SUBNET};//, SUBNET, MMS, GOOSE, SMV
	public enum OperType {ADD, DELETE, UPDATE, RENAME, REPLACE, UP_DESC, MOVE_UP, MOVE_DOWN};
	public enum IedCfg {MMS, GOOSE, SMV, Control, DataSet, ReportControl, LogControl, GSEControl, SampledValueControl, Inputs};
	
	protected DevType type;
	protected OperType opType;
	
	public MarkInfo(DevType type, OperType opType) {
		this.type = type;
		this.opType = opType;
	}
	
	/**
	 * 得到类型描述
	 * @param opType
	 * @return
	 */
	protected String getOpType(OperType opType) {
		switch(opType) {
			case ADD : return "Add";
			case DELETE : return "Delete";
			case UPDATE : return "Update";
			case RENAME : return "Rename";
			case REPLACE : return "Replace";
			case UP_DESC : return "Update Desc";
			case MOVE_UP : return "Move Up";
			case MOVE_DOWN : return "Move Down";
		}
		return "";
	}
	
	/**
	 * 得到修改信息内容
	 * @return
	 */
	public abstract String getContent();

	/**
	 * 得到操作类型
	 * @return
	 */
	public OperType getOpType() {
		return opType;
	}

	public DevType getType() {
		return type;
	}
}
