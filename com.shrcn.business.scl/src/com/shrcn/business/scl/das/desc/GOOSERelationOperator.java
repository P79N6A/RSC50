/**
 * Copyright (c) 2007-2013 上海思弘瑞电力控制技术有限公司. All rights reserved. 
 * This program is an eclipse Rich Client Application
 * designed for IED configuration and debuging.
 */
package com.shrcn.business.scl.das.desc;

import java.util.ArrayList;
import java.util.List;

import com.shrcn.business.scl.beans.ExtRefModel;
import com.shrcn.business.scl.beans.IEDInputsModel;
import com.shrcn.business.scl.das.DescDao;
import com.shrcn.business.scl.model.ReceivedInput;
import com.shrcn.found.common.util.StringUtil;

/**
 * 
 * 导入GOOSE信号关联操作
 * 
 * @author 周泉(mailto:zq@shrcn.com)
 * @version 1.0, 2013-11-13
 */
/**
 * $Log: GOOSERelationOperator.java,v $
 * Revision 1.2  2013/11/18 06:27:46  zq
 * Update: 修改信号描述不生成Excel文件
 *
 * Revision 1.1  2013/11/14 00:52:34  zq
 * Update: 替换ICD时, 增加是否保留描述的处理
 *
 */
public class GOOSERelationOperator extends ImportDescOperator {
	
	/**
	 * IED信号关联的信息
	 */
	private List<IEDInputsModel> ieds = new ArrayList<IEDInputsModel>();
	
	/**
	 * 读取信号关联描述模型
	 */
	public void readModel(String iedName, List<ReceivedInput> lstReceivedInput) {
		for (IEDInputsModel ied : ieds) {
			// IED 
			ReceivedInput receviedInput = new ReceivedInput();
			
			ReceivedInput.ied = ied.getName();
			ReceivedInput.iedName = ied.getName();
			ReceivedInput.iedType = ied.getType();
			
			lstReceivedInput.add(receviedInput);
			
			// Inputs
			List<ExtRefModel> inputs = ied.getInputs();
			
			for (ExtRefModel input : inputs) {
				receviedInput = new ReceivedInput();
				
				receviedInput.path = input.getIntAddr();
				receviedInput.desc = input.getIntDesc();
				
				lstReceivedInput.add(receviedInput);
			}
		}
	}
	
	/**
	 * 更新描述
	 */
	public void update(List<ReceivedInput> lstReceivedInput) {
		if (lstReceivedInput == null || lstReceivedInput.size() == 0)
			return;

		for (ReceivedInput receivedInput : lstReceivedInput) {
			if (receivedInput.path == null)
				continue;
			String intAddr = receivedInput.path;
			String path = StringUtil.getDoXpath(intAddr);
			DescDao.updateDoDesc(path, receivedInput.desc, ReceivedInput.iedName);
		}
	}

	public List<IEDInputsModel> getIeds() {
		return ieds;
	}

	public void setIeds(List<IEDInputsModel> ieds) {
		this.ieds = ieds;
	}
}
