/**
 * Copyright (c) 2007-2013 上海思弘瑞电力控制技术有限公司. All rights reserved. 
 * This program is an eclipse Rich Client Application
 * designed for IED configuration and debuging.
 */
package com.shrcn.business.scl.das.desc;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import com.shrcn.business.scl.beans.IEDModel;
import com.shrcn.business.scl.beans.LDeviceModel;
import com.shrcn.business.scl.beans.LdLnModel;
import com.shrcn.business.scl.das.LNDAO;
import com.shrcn.business.scl.model.LDevice;
import com.shrcn.business.scl.model.LNode;
import com.shrcn.business.scl.model.ReceivedInput;
import com.shrcn.business.scl.model.SCL;
import com.shrcn.business.scl.util.SCLUtil;
import com.shrcn.found.file.excel.ExcelUtil;
import com.shrcn.found.xmldb.XMLDBHelper;

/**
 * 
 * 导入LN描述操作
 * 
 * @author 周泉(mailto:zq@shrcn.com)
 * @version 1.0, 2013-11-13
 */
/**
 * $Log: LNDescOperator.java,v $
 * Revision 1.2  2013/11/18 06:27:47  zq
 * Update: 修改信号描述不生成Excel文件
 *
 * Revision 1.1  2013/11/14 00:52:34  zq
 * Update: 替换ICD时, 增加是否保留描述的处理
 *
 */
public class LNDescOperator extends ImportDescOperator {
	
	/**
	 * IED模型
	 */
	private List<IEDModel> iedModels = new ArrayList<IEDModel>();
	
	/**
	 * 根据LN模型, 导入LN描述
	 */
	public void readModel(String iedName, List<ReceivedInput> lstReceivedInput) {
		for (IEDModel ied : iedModels) {
			ReceivedInput receviedInput = new ReceivedInput();
			ReceivedInput.ied = ied.getName();
			ReceivedInput.iedName = ied.getName();
			ReceivedInput.iedType = ied.getType();
			
			lstReceivedInput.add(receviedInput);
			
			List<LDeviceModel> lds = ied.getLds();
			
			for (LDeviceModel ld : lds) {
				LDevice ldevice = new LDevice();
				
				ldevice.inst = ld.getLdInst();
				ldevice.apName = ld.getApName();
				
				receviedInput.lstLDevice.add(ldevice);
				
				List<LdLnModel> lns = ld.getLns();
				
				for (LdLnModel ln : lns) {
					LNode lnode = new LNode();
					lnode.name = ln.getName();
					lnode.desc = ln.getDesc();
					
					ldevice.lstLNode.add(lnode);
				}
			}
		}
	}
	
	public void readDesc(String filePath, String iedName,
			List<ReceivedInput> lstReceivedInput) {
		curStatus = FoundStatus.FOUNDING;
		
		try {
			POIFSFileSystem fs = new POIFSFileSystem(new FileInputStream(
					filePath));
			HSSFWorkbook workBook = new HSSFWorkbook(fs);
			for (int i = 0; i < workBook.getNumberOfSheets(); i++) {
				HSSFSheet sheet = workBook.getSheetAt(i);
				int rows = sheet.getPhysicalNumberOfRows();
				HSSFRow row1 = sheet.getRow(1);
				HSSFCell cell = row1.getCell((short) column1);
				if (cell != null) {
					String cellValue = ExcelUtil.getCellValue(cell);
					if (!cellValue.equals("装置逻辑节点报告")) {
						return;
					} else if (rows > 5) {
						for (int j = 5; j < rows; j++) {
							HSSFRow row = sheet.getRow(j);
							if (row != null) {
								HSSFCell cell1 = row.getCell((short) column1);
								if (cell1 != null) {
									String cell1Value = ExcelUtil.getCellValue(cell1);
									if (!cell1Value.trim().equals("")) {
										// 去掉空白行
										// 以表格每行的第二列作为判断依据,分为四种情况<1>"装置名称"、<2>逻辑设备、
										// <3>逻辑节点、<4>prefix+lnclass
										switch (curStatus) {
										case FoundStatus.FOUNDING:
											if (cell1Value.equals("装置名称：")) {
												HSSFCell cell2 = row.getCell((short) column2);
												String cell2Value = ExcelUtil.getCellValue(cell2);
												if (cell2Value.equals(iedName)) {
													HSSFCell cell6 = row.getCell((short) column6);
													String cell6Value = ExcelUtil.getCellValue(cell6);
													String regIedName = cell2Value;
													curStatus = curStatus << 1;
													ReceivedInput receviedInput = new ReceivedInput();
													ReceivedInput.ied = regIedName;
													ReceivedInput.iedName = regIedName;
													ReceivedInput.iedType = cell6Value;
													lstReceivedInput.add(receviedInput);
												}
											}
											break;
										case FoundStatus.FOUNDED:
											if (cell1Value.equals("装置名称：")) {
												curStatus = curStatus << 1;
												break;
											}
											if (cell1Value.equals("逻辑节点"))
												break;
											ReceivedInput receivedInput = lstReceivedInput.get(0);
											if (cell1Value.equals("逻辑设备：")) {
												HSSFCell cell2 = row.getCell((short) column2);
												String cell2Value = ExcelUtil.getCellValue(cell2);
												HSSFCell cell4 = row.getCell((short) column4);
												String cell4Value = ExcelUtil.getCellValue(cell4);
												LDevice ldevice = new LDevice();
												ldevice.inst = cell2Value;
												ldevice.apName = cell4Value;
												receivedInput.lstLDevice.add(ldevice);
											} else {
												LNode lnode = new LNode();
												HSSFCell cell2 = row.getCell((short) column2);
												String cell2Value = ExcelUtil.getCellValue(cell2);
												lnode.name = cell1Value;
												lnode.desc = cell2Value;
												List<LDevice> lstLDevice = receivedInput.lstLDevice;
												if (lstLDevice != null && lstLDevice.size() != 0) {
													lstLDevice.get(lstLDevice.size() - 1).lstLNode.add(lnode);
												}
											}
											break;
										case FoundStatus.COMPLETED:
											return;
										}
									}
								}
							}
						}
					}
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void update(List<ReceivedInput> lstReceivedInput) {
		if (lstReceivedInput == null || lstReceivedInput.size() == 0)
			return;
		ReceivedInput receivedInput = lstReceivedInput.get(0);
		List<LDevice> lstLDevice = receivedInput.lstLDevice;
		if (lstLDevice == null || lstLDevice.size() == 0)
			return;
		for (LDevice lDevice : lstLDevice) {
			if (lDevice == null)
				continue;
			LNDAO lnDao = new LNDAO(ReceivedInput.iedName, lDevice.inst);
			String ldxpath=lnDao.getLdXpath();
			List<LNode> lstLNode = lDevice.lstLNode;
			if (lstLNode == null || lstLNode.size() == 0)
				continue;
			for (int n = 0, size = lstLNode.size(); n < size; n++) {
				LNode lNode = lstLNode.get(n);
				if (lNode == null || lNode.desc == null || lNode.name == null)
					continue;
				String[] lnInfo = SCLUtil.getLNInfo(lNode.name);
				String lNpath=ldxpath.concat(SCL.getLNXPath(lnInfo[0], lnInfo[1], lnInfo[2]));
				XMLDBHelper.saveAttribute(lNpath, "desc", lNode.desc);
			}
		}
	}

	public List<IEDModel> getIedModels() {
		return iedModels;
	}

	public void setIedModels(List<IEDModel> iedModels) {
		this.iedModels = iedModels;
	}
}
