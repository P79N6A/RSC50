/**
 * Copyright (c) 2007-2013 上海思弘瑞电力控制技术有限公司. All rights reserved. 
 * This program is an eclipse Rich Client Application
 * designed for IED configuration and debuging.
 */
package com.shrcn.business.scl.das.desc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Element;
import org.eclipse.swt.widgets.Display;

import com.shrcn.business.scl.beans.FCDAModel;
import com.shrcn.business.scl.beans.IEDModel;
import com.shrcn.business.scl.beans.LDeviceModel;
import com.shrcn.business.scl.beans.LdDataSetModel;
import com.shrcn.business.scl.das.DescDao;
import com.shrcn.business.scl.das.LNDAO;
import com.shrcn.business.scl.model.DataSet;
import com.shrcn.business.scl.model.FCDA;
import com.shrcn.business.scl.model.LDevice;
import com.shrcn.business.scl.model.ReceivedInput;
import com.shrcn.business.scl.model.navgtree.LNInfo;
import com.shrcn.business.scl.util.ReceivedInputBuilder;
import com.shrcn.found.common.Constants;
import com.shrcn.found.common.util.StringUtil;
import com.shrcn.found.ui.view.ConsoleView;
import com.shrcn.found.ui.view.ViewManager;

/**
 * 
 * 导入数据集信号描述
 * 
 * @author 周泉(mailto:zq@shrcn.com)
 * @version 1.0, 2013-11-13
 */
/**
 * $Log: DataSetDescOperator.java,v $
 * Revision 1.2  2013/11/18 06:27:46  zq
 * Update: 修改信号描述不生成Excel文件
 *
 * Revision 1.1  2013/11/14 00:52:34  zq
 * Update: 替换ICD时, 增加是否保留描述的处理
 *
 */
public class DataSetDescOperator extends ImportDescOperator {
	
	/**
	 * IED描述
	 */
	private List<IEDModel> iedModels = new ArrayList<IEDModel>();
	
	/**
	 * 读取数据项描述模型
	 */
	public void readModel(String iedName, List<ReceivedInput> lstReceivedInput) {
		for (IEDModel ied : iedModels) {
			ReceivedInput receviedInput = new ReceivedInput();
			
			ReceivedInput.ied = ied.getName();
			ReceivedInput.iedName = ied.getName();
			ReceivedInput.iedType = ied.getType();
			lstReceivedInput.add(receviedInput);
			
			List<LDeviceModel> lds = ied.getLds();
			
			Map<String, LDevice> ldMap = new HashMap<String, LDevice>(); 
			for (LDeviceModel ld : lds) {
				// LD
				if (ld instanceof LdDataSetModel) {
					LDevice ldevice = null;
					
					if (ldMap.containsKey(ld.getLdInst())) {
						ldevice = ldMap.get(ld.getLdInst());
					} else {
						ldevice = new LDevice();
						ldevice.inst = ld.getLdInst();
						ldevice.apName = ld.getApName();
					}
					
					receviedInput.lstLDevice.add(ldevice);
					
					setLdInst(ld.getLdInst());
					
					LdDataSetModel dataSet = (LdDataSetModel) ld;
					List<FCDAModel> fcdas = dataSet.getFcdas();
					
					DataSet dataset = new DataSet();
					dataset.name = dataSet.getName();
					dataset.desc = dataSet.getDesc();
					
					ldevice.lstDataSet.add(dataset);
					
					// FCDA
					for (FCDAModel fcdaModel : fcdas) {
						FCDA fcda = new FCDA();
						
						fcda.path = fcdaModel.getDoName();
						fcda.daName = fcdaModel.getDaName();
						fcda.desc = fcdaModel.getDesc();
						fcda.newDesc = fcdaModel.getDesc();
						
						dataset.lstFCDA.add(fcda);
					}
				}
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see com.shrcn.sct.iec61850.action.ImportAction#update(java.util.List)
	 */
	@Override
	public void update(List<ReceivedInput> lstReceivedInput) {
		if (lstReceivedInput == null || lstReceivedInput.size() == 0)
			return;

		final ReceivedInput receivedInput = lstReceivedInput.get(0);
		List<LDevice> lstLDevice = receivedInput.lstLDevice;
		if (lstLDevice == null || lstLDevice.size() == 0)
			return;
		
		for (LDevice lDevice : lstLDevice) {
			if (lDevice == null)
				continue;
			List<DataSet> lstDataSet = lDevice.lstDataSet;
			if (lstDataSet == null || lstDataSet.size() == 0)
				continue;
			
			for (DataSet dataSet : lstDataSet) {
				if (dataSet == null)
					continue;
				List<FCDA> lstFCDA = dataSet.lstFCDA;
				if (lstFCDA == null || lstFCDA.isEmpty())
					continue;

				for (final FCDA fcda : lstFCDA) {
					// 为了与ReceivedInputBuilder中path一致
					// 修改LDeviceInst后的符号为/
					if (StringUtil.isEmpty(fcda.path) || "t".equals(fcda.daName) || "q".equals(fcda.daName))
						continue;
					String path = fcda.path.replaceFirst("\\$", "/");
					// 把do之前的的符号由$修改为点
					path = path.replaceFirst("\\$", "\\.");
					if (blNewOldDesc) { // 按描述修改
						ModifyHandle.modifyDoDesc(path, fcda.desc, fcda.newDesc, 
								ReceivedInput.iedName, lDevice.apName, dataSet.name);
					} else {			// 按参引修改
						DescDao.updateDoDesc(path, fcda.desc, ReceivedInput.iedName);
					}
				}
			}
			
		}
	}
	
	/**
	 *根据旧描述定位节点，然后修改该节点的描述
	 */
	static class ModifyHandle {
		public static void modifyDoDesc(final String path, final String oldDesc,
				final String newDesc, final String iedName, final String apName,
				final String datasetName) {
			if (StringUtil.isEmpty(newDesc))
				return;
			ReceivedInputBuilder builder = new ReceivedInputBuilder(path, newDesc);
			builder.parse();
			DescDao descDao = null;
			LNInfo lnInfo = builder.getLNInfo();

			List<Element> lstLN = LNDAO.getSingleLN(iedName, builder.getInst(),
					lnInfo.getInst(), lnInfo.getLnClass(), lnInfo
							.getPrefix());

			if (lstLN == null || lstLN.size() == 0)
				return;
			Element correctElement = null;
			for (Element ln : lstLN) {
				List<?> lstLNChild = ln.elements("DOI");
				correctElement = findCorrectDo(oldDesc, lstLNChild);
				if (correctElement != null) {
					descDao = new DescDao(iedName, apName, builder.getInst());
					descDao.updateDescByOldDesc(ln, correctElement, newDesc);// 更新do
																				// 描述
					descDao.updateDaiDu(ln, correctElement, newDesc);
					break;
				}
			}
			if (correctElement == null) {
				int dotPos = path.indexOf(".");
				int secondDotPos = path.indexOf(".", dotPos + 1);
				String preference = path;
				if (secondDotPos > 0) {
					preference = path.substring(0, secondDotPos);
					preference = preference.replace("/", Constants.DOLLAR);
					preference = preference.replace(Constants.DOT, Constants.DOLLAR);
				}
				final String pref = preference;
				Display.getDefault().asyncExec(new Runnable() {
					@Override
					public void run() {
						ConsoleView console = (ConsoleView) ViewManager.findView(ConsoleView.ID);
						if (console == null)
							return;
						console.appendCheckInfo("\t\n修改描述失败: 装置 " + iedName + " 中数据集 "
								+ datasetName + " 的数据项 \"" + oldDesc + " (" + pref + ")\" 不存在\n");
					}});
			}
		}

		/**
		 * 根据旧DOI描述找到对应的DOI节点
		 * 
		 * @param oldDesc
		 * @param lstChild
		 * @return
		 */
		private static Element findCorrectDo(String oldDesc, List<?> lstChild) {
			Element def = null;
			if (lstChild == null || lstChild.size() == 0)
				return def;
			for (Object obj : lstChild) {
				Element element = (Element) obj;
				String desc = element.attributeValue("desc", "");
				if (desc.equals(oldDesc)) {
					return element;
				} else {
					Element correctEl = findCorrectDo(oldDesc, element.elements());
					if (correctEl == null)
						continue;
					String tagName = correctEl.getName();
					if (!"DOI".equals(tagName) && !"SDI".equals(tagName)) {
						correctEl = element;
					}
					return correctEl;
				}
			}
			return def;
		}
	}

	public List<IEDModel> getIedModels() {
		return iedModels;
	}

	public void setIedModels(List<IEDModel> iedModels) {
		this.iedModels = iedModels;
	}
}
