/**
 * Copyright (c) 2007-2010 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based on IEC61850 SCT.
 */
package com.shrcn.business.scl.table;

/**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2010-5-20
 */
/**
 * $Log: VTTableField.java,v $
 * Revision 1.2  2010/09/03 03:50:42  cchun
 * Update:增加列定制持久化
 *
 * Revision 1.1  2010/05/20 11:08:09  cchun
 * Add:列对象
 *
 */
public class VTTableField {
	private String head;
	private int valNum;
	private boolean visible;
	
	public VTTableField(String head, int valNum) {
		this.head = head;
		this.valNum = valNum;
	}
	
	public String getHead() {
		return head;
	}
	public void setHead(String head) {
		this.head = head;
	}
	public int getValNum() {
		return valNum;
	}
	public void setValNum(int valNum) {
		this.valNum = valNum;
	}

	/**
	 * @return the visible
	 */
	public boolean isVisible() {
		return visible;
	}

	/**
	 * @param visible the visible to set
	 */
	public void setVisible(boolean visible) {
		this.visible = visible;
	} 
}
