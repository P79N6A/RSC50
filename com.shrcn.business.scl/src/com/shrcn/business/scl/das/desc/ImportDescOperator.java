/**
 * Copyright (c) 2007-2013 上海思弘瑞电力控制技术有限公司. All rights reserved. 
 * This program is an eclipse Rich Client Application
 * designed for IED configuration and debuging.
 */
package com.shrcn.business.scl.das.desc;

import java.util.List;

import com.shrcn.business.scl.SCTProperties;
import com.shrcn.business.scl.model.ReceivedInput;

/**
 * 导入描述操作类
 * 
 * @author 周泉(mailto:zq@shrcn.com)
 * @version 1.0, 2013-11-13
 */
/**
 * $Log: ImportDescOperator.java,v $
 * Revision 1.2  2013/11/18 06:27:47  zq
 * Update: 修改信号描述不生成Excel文件
 *
 * Revision 1.1  2013/11/14 00:52:34  zq
 * Update: 替换ICD时, 增加是否保留描述的处理
 *
 */
public class ImportDescOperator {
	
	protected int curStatus = FoundStatus.FOUNDING;
	
	protected final int column1 = 1;
	protected final int column2 = 2;
	protected final int column3 = 3;
	protected final int column4 = 4;
	protected final int column5 = 5;
	protected final int column6 = 6;
	protected final int column8 = 8;
	protected final int column9 = 9;
	
	protected interface FoundStatus {
		int FOUNDING = 1;
		int FOUNDED = 2;
		int COMPLETED = 4;
	}
	
	private String ldInst = null;
	protected boolean blNewOldDesc = false;
	protected SCTProperties propert = SCTProperties.getInstance();
	
	public void readModel(String iedName, List<ReceivedInput> lstReceivedInput) {
		
	}
	
	public void readDesc(String filePath, String iedName,
			List<ReceivedInput> lstReceivedInput) {
		
	}
	
	public void update(List<ReceivedInput> lstReceivedInput) {
		
	}
	
	public String getLdInst() {
		return ldInst;
	}

	public void setLdInst(String ldInst) {
		this.ldInst = ldInst;
	}
}
