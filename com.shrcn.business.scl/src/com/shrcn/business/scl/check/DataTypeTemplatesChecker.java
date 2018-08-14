/**
 * Copyright (c) 2007-2010 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based on IEC61850 SCT.
 */
package com.shrcn.business.scl.check;

import java.util.LinkedList;
import java.util.List;

import org.dom4j.Element;

import com.shrcn.business.scl.das.DataTypeTemplateDao;
import com.shrcn.found.xmldb.XMLDBHelper;

/**
 * 本类负责比较外部导入的icd和当前scd中的数据模板内容，并给出有冲突的数据类型的id集合。
 * 同时还对DO,DA,enum引用完整性进行检查，并生成报告。
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2011-1-10
 */
/**
 * $Log: DataTypeTemplatesChecker.java,v $
 * Revision 1.1  2013/03/29 09:36:37  cchun
 * Add:创建
 *
 * Revision 1.2  2012/04/06 12:45:27  cchun
 * Fix Bug:修复SCD原本没有数据模板时，模型检查无提示缺陷
 *
 * Revision 1.1  2011/01/10 09:19:13  cchun
 * Refactor:提取数据模板比较方法
 *
 */
public class DataTypeTemplatesChecker {
	
	// ICD与SCD冲突的所有类型id
	private List<String> ids = new LinkedList<String>();
	// ICD与SCD冲突的所有LNodeType id
	private List<String> lnodeIds = new LinkedList<String>();
	// ICD与SCD冲突的所有DOType id
	private List<String> doIds = new LinkedList<String>();
	// ICD与SCD冲突的所有DAType id
	private List<String> daIds = new LinkedList<String>();
	// ICD与SCD冲突的所有EnumType id
	private List<String> enumIds = new LinkedList<String>();
	
	private Element dataTypeTemplates;
	private String report = "";
	
	/**
	 * 构造方法
	 * @param dataTypeTemplates
	 */
	public DataTypeTemplatesChecker(Element dataTypeTemplates) {
		this.dataTypeTemplates = dataTypeTemplates;
	}
	
	/**
	 * 执行冲突处理
	 */
	public void execute() {
		List<String> refDos = null; 						// 所有被引用的DOType
		List<String> dos = new LinkedList<String>(); 		// ICD中存在的DOType
		List<String> refDas = new LinkedList<String>(); 	// 所有被引用的DAType
		List<String> das = new LinkedList<String>(); 		// ICD中存在的DAType
		List<String> refEnums = new LinkedList<String>(); 	// 所有被引用的EnumType
		List<String> enums = new LinkedList<String>(); 		// ICD中存在的EnumType
		
		refDos = checkLnodeType(dataTypeTemplates);			//检测LNodeType
		checkDOType(dataTypeTemplates, dos, refEnums, refDas);	//检测DOType
		checkDAType(dataTypeTemplates, refEnums, das);			//检测DAType
		checkEnumType(dataTypeTemplates, enums);				//检测EnumType
		
		for (String id : refDos) {
			if (!dos.contains(id)) {
				report += "\t\nDOType类型:" + id + "....未找到";
			}
		}
		for (String id : refDas) {
			if (!das.contains(id)) {
				report += "\t\nDAType类型:" + id + "....未找到";
			}
		}
		for (String id : refEnums) {
			if (!enums.contains(id)) {
				report += "\t\nEnumType类型:" + id + "....未找到";
			}
		}
	}
	
	/**
	 * 获取DO,DA,enum引用完整性检查报告。
	 * @return
	 */
	public String getCompletableReport() {
		return report;
	}

	/**
	 * 检查LNodeType
	 * @param dataTypeTemplates
	 * @return DO 类型集合
	 */
	private List<String> checkLnodeType(Element dataTypeTemplates) {
		List<String> doTypes = new LinkedList<String>();
		if (dataTypeTemplates == null)
			return doTypes;
		List<?> lNodeType = dataTypeTemplates.elements("LNodeType"); //$NON-NLS-1$
		for (Object obj : lNodeType) {
			Element icdLNodeType = (Element) obj;
			String id = icdLNodeType.attributeValue("id"); //$NON-NLS-1$
			Element scdLnodeType = XMLDBHelper
					.selectSingleNode("/scl:SCL/scl:DataTypeTemplates/scl:LNodeType[@id='" + id + "']");
			if (scdLnodeType != null) { // 有冲突
				if (DataTypeTemplateDao.isSameType(icdLNodeType, scdLnodeType)) {
					icdLNodeType.getParent().remove(icdLNodeType);
				} else {
					ids.add(id);
					lnodeIds.add(id);
				}
			}

			List<?> dosEle = icdLNodeType.elements("DO"); //$NON-NLS-1$
			for (Object obj1 : dosEle) {
				Element doEle = (Element) obj1;
				String type = doEle.attributeValue("type"); //$NON-NLS-1$
				if (type != null) {
					if (!doTypes.contains(type)) {
						doTypes.add(type);
					}
				}
			}
		}
		return doTypes;
	}
	
	/**
	 * 检查DOType
	 * @param dataTypeTemplates
	 * @param dos
	 * @param enums
	 * @param das
	 */
	private void checkDOType(Element dataTypeTemplates, List<String> dos, List<String> enums, List<String> das) {
		if(dataTypeTemplates == null)
			return ;
		List<?> doType = dataTypeTemplates.elements("DOType"); //$NON-NLS-1$
		for (Object obj : doType) {
			Element icdDOType = (Element) obj;
			String id = icdDOType.attributeValue("id"); //$NON-NLS-1$
			Element scdDOType = XMLDBHelper.selectSingleNode("/scl:SCL/scl:DataTypeTemplates/scl:DOType[@id='" + id //$NON-NLS-1$
					+ "']"); //$NON-NLS-1$
			if (scdDOType != null) {
				if (DataTypeTemplateDao.isSameType(icdDOType, scdDOType)) {
					icdDOType.getParent().remove(icdDOType);
				} else {
					ids.add(id);
					doIds.add(id);
				}
			}

			if (!dos.contains(id)) {
				dos.add(id);
			}

			checkSDO(dos, icdDOType);
			checkDoDA(enums, das, icdDOType);
		}
	}

	/**
	 * 检查DAType
	 * @param dataTypeTemplates
	 * @param enums
	 * @param ds
	 */
	private void checkDAType(Element dataTypeTemplates, List<String> enums,
			List<String> ds) {
		if(dataTypeTemplates == null)
			return ;
		List<?> daType = dataTypeTemplates.elements("DAType"); //$NON-NLS-1$
		for (Object obj : daType) {
			Element e = (Element) obj;
			String id = e.attributeValue("id"); //$NON-NLS-1$
			Element oldDAType = XMLDBHelper.selectSingleNode("/scl:SCL/scl:DataTypeTemplates/scl:DAType[@id='" + id //$NON-NLS-1$
					+ "']"); //$NON-NLS-1$
			if (oldDAType != null) {
				if (DataTypeTemplateDao.isSameType(e, oldDAType)) {
					e.getParent().remove(e);
				} else {
					ids.add(id);
					daIds.add(id);
				}
			}

			if (!ds.contains(id)) {
				ds.add(id);
			}
			List<?> bdas = e.elements("BDA"); //$NON-NLS-1$
			for (Object obj1 : bdas) {
				Element el = (Element) obj1;
				String btyp = el.attributeValue("bType"); //$NON-NLS-1$
				String ty = el.attributeValue("type"); //$NON-NLS-1$
				if (ty != null) {
					if (btyp.equals("Struct") && (!ds.contains(ty))) { //$NON-NLS-1$
						ds.add(ty);
					} else if (btyp.equals("enum") && (!enums.contains(ty))) { //$NON-NLS-1$
						enums.add(ty);
					}
				}
			}
		}
	}

	/**
	 * 检查SDO
	 * 
	 * @param dos
	 * @param e
	 */
	private void checkSDO(List<String> dos, Element e) {
		List<?> sdoEles = e.elements("SDO"); //$NON-NLS-1$
		for (Object obj : sdoEles) {
			Element sdoEle = (Element) obj;
			String type = sdoEle.attributeValue("type"); //$NON-NLS-1$
			if (type != null) {
				if (!dos.contains(type)) {
					dos.add(type);
				}
			}
		}
	}

	/**
	 * 检查DO中DA
	 * @param enums
	 * @param das
	 * @param elDOType
	 */
	private void checkDoDA(List<String> enums, List<String> das, Element elDOType) {
		List<?> dasEle = elDOType.elements("DA"); //$NON-NLS-1$
		for (Object obj : dasEle) {
			Element da = (Element) obj;
			String btype = da.attributeValue("bType"); //$NON-NLS-1$
			String type = da.attributeValue("type"); //$NON-NLS-1$

			if (type != null) {
				if (btype.equals("Struct")) { //$NON-NLS-1$
					if (!das.contains(type)) {
						das.add(type);
					}
				} else if (btype.equals("Enum")) { //$NON-NLS-1$
					if (!enums.contains(type)) {
						enums.add(type);
					}
				}
			}
		}
	}

	/**
	 * 检查EnumType
	 * 
	 * @param dataTypeTemplates
	 * @param ens
	 */
	private void checkEnumType(Element dataTypeTemplates, List<String> ens) {
		if(dataTypeTemplates == null)
			return ;
		List<?> enumType = dataTypeTemplates.elements("EnumType"); //$NON-NLS-1$
		for (Object obj : enumType) {
			Element e = (Element) obj;
			String id = e.attributeValue("id"); //$NON-NLS-1$
			Element oldEnumType = XMLDBHelper.selectSingleNode("/scl:SCL/scl:DataTypeTemplates/scl:EnumType[@id='" + id //$NON-NLS-1$
					+ "']"); //$NON-NLS-1$
			if (oldEnumType != null) {
				if (DataTypeTemplateDao.isSameType(e, oldEnumType)) {
					e.getParent().remove(e);
				} else {
					ids.add(id);
					enumIds.add(id);
				}
			}
			if (!ens.contains(id)) {
				ens.add(id);
			}
		}
	}
	
	/**
	 * 清理比较结果
	 */
	public void clear() {
		report = "";
		// ICD与SCD冲突的所有类型id
		ids.clear();
		// ICD与SCD冲突的所有LNodeType id
		lnodeIds.clear();
		// ICD与SCD冲突的所有DOType id
		doIds.clear();
		// ICD与SCD冲突的所有DAType id
		daIds.clear();
		// ICD与SCD冲突的所有EnumType id
		enumIds.clear();
	}

	public List<String> getIds() {
		return ids;
	}

	public List<String> getLnodeIds() {
		return lnodeIds;
	}

	public List<String> getDoIds() {
		return doIds;
	}

	public List<String> getDaIds() {
		return daIds;
	}

	public List<String> getEnumIds() {
		return enumIds;
	}
}
