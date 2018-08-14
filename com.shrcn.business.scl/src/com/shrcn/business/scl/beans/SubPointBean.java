/**
 * Copyright (c) 2007-2010 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based on IEC61850 SCT.
 */
package com.shrcn.business.scl.beans;

import java.io.Serializable;

/**
 * 信息点模型类。
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2011-11-25
 */
/**
 * $Log: SubPointBean.java,v $
 * Revision 1.1  2013/03/29 09:37:13  cchun
 * Add:创建
 *
 * Revision 1.2  2011/12/02 03:37:23  cchun
 * Update:添加序列化处理
 *
 * Revision 1.1  2011/11/30 11:00:01  cchun
 * Add:工程点表数据类
 *
 */
public class SubPointBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private String type;
	private String desc;
	private String ref;
	private String resvRef;
	private String resvDesc;
	private String flow;
	private String sendDesc;
	private String sendRef;
	private String sendIED;
	
	/**
	 * 获取信号关联信息
	 * @param point
	 */
	public void getSourceInfo(SubPointBean point) {
		setResvRef(point.getResvRef());
		setResvDesc(point.getResvDesc());
		setSendRef(point.getSendRef());
		setSendDesc(point.getSendDesc());
		setFlow(point.getFlow());
		setSendIED(point.getSendIED());
	}
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public String getRef() {
		return ref;
	}
	public void setRef(String ref) {
		this.ref = ref;
	}
	public String getResvRef() {
		return resvRef;
	}
	public void setResvRef(String resvRef) {
		this.resvRef = resvRef;
	}
	public String getResvDesc() {
		return resvDesc;
	}
	public void setResvDesc(String resvDesc) {
		this.resvDesc = resvDesc;
	}
	public String getSendDesc() {
		return sendDesc;
	}
	public void setSendDesc(String sendDesc) {
		this.sendDesc = sendDesc;
	}
	public String getSendRef() {
		return sendRef;
	}
	public void setSendRef(String sendRef) {
		this.sendRef = sendRef;
	}
	public String getSendIED() {
		return sendIED;
	}
	public void setSendIED(String sendIED) {
		this.sendIED = sendIED;
	}
	public String getFlow() {
		return flow;
	}
	public void setFlow(String flow) {
		this.flow = flow;
	}

}
