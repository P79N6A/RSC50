/**
 * Copyright (c) 2007-2010 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based on IEC61850 SCT.
 */
package com.shrcn.business.scl.model;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.Set;

import javax.imageio.ImageIO;
import javax.swing.AbstractButton;
import javax.swing.Icon;
import javax.swing.ImageIcon;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;

import com.shrcn.business.scl.enums.EnumEqpCategory;
import com.shrcn.found.common.Constants;
import com.shrcn.found.common.util.StringUtil;
import com.shrcn.found.file.util.FileManipulate;
import com.shrcn.found.file.xml.DOM4JNodeHelper;
import com.shrcn.found.file.xml.XMLFileManager;

/**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2011-7-10
 */
/**
 * $Log: EquipmentConfig.java,v $
 * Revision 1.5  2011/09/06 08:28:12  cchun
 * Fix Bug:修复工具栏图标重复出现的bug
 *
 * Revision 1.4  2011/09/02 07:06:17  cchun
 * Update:添加设备管理相关方法
 *
 * Revision 1.3  2011/07/14 08:03:45  cchun
 * Fix Bug:修复属性使用错误，增加getGraphEquipments(),getStdEquipments()
 *
 * Revision 1.2  2011/07/13 08:44:34  cchun
 * Update:增加addEquipment()，getCategory()
 *
 * Revision 1.1  2011/07/11 08:52:56  cchun
 * Add:一次设备定义及其配置文件
 *
 */
public class EquipmentConfig {

	private static final ResourceBundle eqpBundle = ResourceBundle.getBundle("com.shrcn.business.scl.model.equipment");
	private static final String VAR_SYS = "SYS_EQUIPMENT";
	
	// 设备图符类型
	public static final String P_TYPE = "type";
	// 设备模型类型
	public static final String P_MTYPE = "mtype";
	public static final String P_DESC = "desc";
	public static final String P_LNODE = "lnode";
	public static final String P_TERMINAL = "terminal";
	public static final String P_GRAPH = "graph";
	private static final String DOT = ".";
	private Map<String, Set<String>> cateMap = new HashMap<String, Set<String>>();
	private Map<String, String> cfgMap = new HashMap<String, String>();
	private Document doc;
	
	private static final String pkg 		= "/com/shrcn/business/scl/model/";
	private static final String fileName 	= "PrimaryEquipments.xml";
	private static final String cfgFilePath = Constants.cfgDir + File.separator + fileName;
	
	private static EquipmentConfig config = new EquipmentConfig();
	
	private EquipmentConfig() {
		load();
	}
	
	public static EquipmentConfig getInstance() {
		if (config == null) {
			config = new EquipmentConfig();
		}
		return config;
	}
	
	/**
	 * 加载一次设备配置信息
	 */
	private void load() {
		File cfgFile = new File(cfgFilePath);
		if (!cfgFile.exists()) {
			FileManipulate.copyResource(getClass(), pkg, Constants.cfgDir, fileName);
		}
		doc = XMLFileManager.loadXMLFile(cfgFilePath);
		List<?> cateEles = doc.getRootElement().elements();
		for (Object obj : cateEles) {
			Element cate = (Element) obj;
			String cateType = cate.attributeValue(P_TYPE);
			addProperties(cateType, cate);
			List<?> eqpEles = cate.elements();
			Set<String> eqps = new HashSet<String>();
			for (Object obj1 : eqpEles) {
				Element eqp = (Element) obj1;
				String eqpType = eqp.attributeValue(P_TYPE);
				eqps.add(eqpType);
				addProperties(eqpType, eqp);
			}
			cateMap.put(cateType, eqps);
		}
	}
	
	/**
	 * 重新加载
	 */
	private void reLoad() {
		cateMap.clear();
		cfgMap.clear();
		load();
	}
	
	private void addProperties(String type, Element cfg) {
		List<?> atts = cfg.attributes();
		for (Object obj : atts) {
			Attribute att = (Attribute) obj;
			String name = att.getName();
			if (!P_TYPE.equals(name))
				cfgMap.put(type + DOT + name, att.getValue());
		}
	}
	
	/**
	 * 添加设备图符定义
	 * @param category
	 * @param type
	 * @param mtype
	 * @param desc
	 * @param lnode
	 * @param termNums
	 */
	private void addEquipment(String category, String type, String mtype, String desc, String lnode, String termNums) {
		Element cateEle = (Element) doc.getRootElement().selectSingleNode("./Category[@type='" + category + "']");
		Element eqpEle = (Element) cateEle.selectSingleNode("./Equipment[@type='" + type + "']");
		if (eqpEle == null)
			eqpEle = cateEle.addElement("Equipment");
		eqpEle.addAttribute(P_TYPE, type);
		eqpEle.addAttribute(P_MTYPE, mtype);
		eqpEle.addAttribute(P_DESC, desc);
		eqpEle.addAttribute(P_LNODE, lnode);
		eqpEle.addAttribute(P_TERMINAL, termNums);
		eqpEle.addAttribute(P_GRAPH, "true");
		addProperties(type, eqpEle);
		cateMap.get(category).add(type);
		saveConfig();
	}
	
	private void saveConfig() {
		XMLFileManager.saveUTF8File(doc.asXML(), new File(cfgFilePath));
	}

	/**
	 * 删除设备定义
	 * @param category
	 * @param type
	 */
	public void deleteEquipment(Element eqpCfg) {
		String category = eqpCfg.getParent().attributeValue("type", "");
		String type = eqpCfg.attributeValue("type", "");
		if (StringUtil.isEmpty(category) || StringUtil.isEmpty(type))
			return;
		Element eqpEle = (Element) doc.getRootElement().
				selectSingleNode("./Category[@type='" + category + "']/Equipment[@type='" + type + "']");
		eqpEle.getParent().remove(eqpEle);
		saveConfig();
		reLoad();
	}
	
	/**
	 * 添加设备图符定义
	 * @param eqpInfo
	 */
	public synchronized void addEquipment(EquipmentInfo eqpInfo) {
//		System.out.println("ADD: " + eqpInfo.getType() +
//				"!");
		addEquipment(eqpInfo.getCategory(), eqpInfo.getType(), eqpInfo.getMtype(), 
				eqpInfo.getDesc(), eqpInfo.getLnode(), eqpInfo.getTerminal());
	}
	
	/**
	 * 获取图形设备分组信息
	 * @return
	 */
	public Element[] getGraphCategory() {
		List<Element> graphCates = new ArrayList<Element>();
		List<?> cfgCates = doc.getRootElement().elements();
		for (Object obj : cfgCates) {
			Element cfg = (Element) obj;
			String cateType = cfg.attributeValue("type", "");
			String cateDesc = cfg.attributeValue("desc", "");
			if (EnumEqpCategory.contains(cateType)) {
				Element gCate = DOM4JNodeHelper.createRTUNode("Category");//cfg.createCopy();
				gCate.addAttribute("type", cateType);
				gCate.addAttribute("desc", cateDesc);
				List<?> subCfgs = cfg.elements();
				for (Object obj1 : subCfgs) {
					Element subCfg = (Element) obj1;
					String cfgType = subCfg.attributeValue("type", "");
					if (hasGraph(cfgType) && !isSysEqp(cfgType)) {
						gCate.add(subCfg.createCopy());
					}
				}
				if (gCate.elements().size() > 0)
					graphCates.add(gCate);
			}
		}
		return (Element[]) graphCates.toArray(new Element[graphCates.size()]);
	}
	
	/**
	 * 更新设备定义
	 * @param graphCates
	 */
	public void updateGraphCategory(Element[] graphCates) {
		Element docRoot = doc.getRootElement();
		for (Element gCate : graphCates) {
			String category = gCate.attributeValue("type", "");
			Element cateEle = (Element) docRoot.selectSingleNode("./Category[@type='" + category + "']");
			List<?> subCfgs = gCate.elements();
			for (Object obj : subCfgs) {
				Element subCfg = (Element) obj;
				String cfgType = subCfg.attributeValue("type", "");
				Element eqpEle = (Element) cateEle.selectSingleNode("./Equipment[@type='" + cfgType + "']");
				if (eqpEle != null) {
					cateEle.remove(eqpEle);
					cateEle.add(subCfg.createCopy());
				}
			}
		}
		saveConfig();
	}
	
	/**
     * 配置（工具栏）按钮。
     * @param button
     * @param type
     */
    public void configureToolBarButton(AbstractButton button, String type, Class<?> baseClass) {
    	Icon icon = getImageIcon(type, baseClass);
    	String name = getDesc(type);
        if (icon != null) {
	        button.setIcon(icon);
        } else {
            button.setText(name);
        }
        button.setToolTipText(name);
    }
    
    /**
     * 获取图标
     * @param labelKey
     * @param baseClass
     * @return
     */
    public ImageIcon getImageIcon(String type, Class<?> baseClass) {
    	URL url = null;
		try {
			File iconFile = new File(Constants.getToolIconPath(type));
			url = iconFile.toURI().toURL();
		} catch (MissingResourceException e) {
	        return null;
	    } catch (MalformedURLException e) {
	    	return null;
		}
		// 刷新缓存
		BufferedImage bufImg = null;
		try {
			bufImg = ImageIO.read(url);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return (null == url) ? null : new ImageIcon(bufImg);
	}
	
    /**
     * 得到指定类型所有设备
     * @param category
     * @return
     */
	public Set<String> getEquipments(String category) {
		return cateMap.get(category);
	}
	
	/**
	 * 得到指定类型所有定义了图符的设备
	 * @param category
	 * @return
	 */
	public List<String> getGraphEquipments(String category) {
		Set<String> eqps = getEquipments(category);
		List<String> eqpsHasGraph = new ArrayList<String>();
		for (String type : eqps) {
			if (hasGraph(type))
				eqpsHasGraph.add(type);
		}
		return eqpsHasGraph;
	}
	
	/**
	 * 获取设备分类
	 * @param type
	 * @return
	 */
	public String getCategory(String type) {
		Iterator<String> cates = cateMap.keySet().iterator();
		while(cates.hasNext()) {
			String category = cates.next();
			Set<String> types = cateMap.get(category);
			if (types.contains(type))
				return category;
		}
		return null;
	}
	
	/**
	 * 判断是否为系统预定义设备
	 * @param type
	 * @return
	 */
	public boolean isSysEqp(String type) {
		String[] sysEqps = getStdEquipments(VAR_SYS);
		List<String> sysEqpsList = java.util.Arrays.asList(sysEqps);
		return sysEqpsList.contains(type);
	}
	
	/**
	 * 得到指定类型下所有的标准设备
	 * @param category
	 * @return
	 */
	public String[] getStdEquipments(String category) {
		return eqpBundle.getString(category).split(",");
	}

	public String getDesc(String type) {
		return cfgMap.get(type + DOT + P_DESC);
	}
	
	public String getMType(String type) {
		return cfgMap.get(type + DOT + P_MTYPE);
	}
	
	public String[] getLNodes(String type) {
		return cfgMap.get(type + DOT + P_LNODE).split(",");
	}
	
	public int getTerminal(String type) {
		String t = cfgMap.get(type + DOT + P_TERMINAL);
		if (t == null)
			return 0;
		return Integer.parseInt(t);
	}
	
	public boolean hasGraph(String type) {
		String graph = cfgMap.get(type + DOT + P_GRAPH);
		return graph==null ? false : Boolean.valueOf(graph);
	}
}
