/**
 * Copyright (c) 2007-2010 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based on IEC61850 SCT.
 */
package com.shrcn.business.scl.beans;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2011-7-19
 */
/**
 * $Log: IEDInputsModel.java,v $
 * Revision 1.1  2013/03/29 09:37:10  cchun
 * Add:创建
 *
 * Revision 1.1  2011/07/20 05:51:09  cchun
 * Add:增加excel导出java bean
 *
 */
public class IEDInputsModel extends IEDModel {

	private List<ExtRefModel> inputs = new ArrayList<ExtRefModel>();
	
	public IEDInputsModel(String name, String desc, String type) {
		super(name, desc, type);
	}

	public List<ExtRefModel> getInputs() {
		return inputs;
	}

	public void setInputs(List<ExtRefModel> inputs) {
		this.inputs = inputs;
	}

}
