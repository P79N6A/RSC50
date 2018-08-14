/**
 * Copyright (c) 2007-2010 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based on IEC61850 SCT.
 */
package com.synet.tool.rsc.util;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.shrcn.found.common.util.StringUtil;
import com.shrcn.found.file.xml.XMLFileManager;
import com.synet.tool.rsc.das.ProjectManager;

/**
 * 
 * @author 孙春颖
 * @version 1.0, 2013 8 2
 */
/**
 *
 * Revision 1.4  2013/08/16 08:03:40  cxc
 * update:修改删除掉工程名为“”时出现list不对的情况
 *
 * Revision 1.3  2013/08/06 07:25:54  scy
 * Update：修改工程或SCD文件时更新配置文件
 *
 * Revision 1.2  2013/08/05 08:46:32  scy
 * Update：增加重命名Scd方法
 *
 * Revision 1.1  2013/08/05 08:43:39  scy
 * Add：增加工程名称文件管理操作类
 *
 */
public class ProjectFileManager {

	private static ProjectFileManager manager;
	
	private static Map<String, String> map;
	
	private boolean closed = true;
		
	private ProjectFileManager() {
		map = new LinkedHashMap<String, String>();
		if (new File(getPath()).exists()) {
			readFile();
		} else {
			File dirRoot = new File(ProjectManager.getDataDir());
			if (dirRoot.exists()) {
				final File[] listFiles = dirRoot.listFiles();
				for (File file : listFiles) {
					if (file.isDirectory()) {
						final String name = file.getName();
						map.put(name, name);
					}
				}
				saveFile();
			}
		}
	}
	
	public static ProjectFileManager getInstance() {
		if (manager == null) {
			manager = new ProjectFileManager();
		}
		return manager;
	}

	/**
	 * 保存工程配置数据.
	 * 
	 * @param projectData
	 */
	private void saveFile() {
		Document doc = DocumentHelper.createDocument();
		Element projectEl = doc.addElement("Project");
		
		for (String prjName : map.keySet()) {
			Element itemEl = projectEl.addElement("Item");
			itemEl.addAttribute("prjName", prjName);
			itemEl.addAttribute("scdName", map.get(prjName));
		}
		String path = getPath();
		File rootDir = new File(path).getParentFile();
		if (!rootDir.exists()) {
			rootDir.mkdirs();
		}
		XMLFileManager.writeDoc(doc, path);
	}

	@SuppressWarnings("unchecked")
	public void readFile() {
		map.clear();
		Document doc = XMLFileManager.loadXMLFile(getPath());
		if (doc == null)
			return;
		Element root = doc.getRootElement();
		
		List<Element> itemEls = root.elements("Item");
		if (itemEls != null) {
			for (Element el : itemEls) {
				map.put(el.attributeValue("prjName"), el.attributeValue("scdName"));
			}
		}
	}
	
	private String getPath() {
		return ProjectManager.getProjectFilePath();
	}
	
	public void addProject(String prjName, String scdDbName) {
		map.put(prjName, scdDbName);
		saveFile();
	}
	
	public String getScdName(String prjName) {
		return map.get(prjName);
	}
	public String getprjName(String scdName){
		String prjName = null;
		for (String name : map.keySet()){
			if (scdName.equals(map.get(name))){
				prjName = name;
			}
		}
		return prjName;
	}
	public List<String> getHistoryItems() {
		List<String> list = new ArrayList<String>();
		list.addAll(map.keySet());
		return list;
	}
	
	public List<String> getHistorySCDItems() {
		List<String> list = new ArrayList<String>();
		for (String name : map.values()) {
			if (!StringUtil.isEmpty(name) && !list.contains(name))
				list.add(name);
		}
		return list;
	}

	public void removeProject(String item) {
		map.remove(item);
		saveFile();
	}

	public void renameScd(String prjName, String name) {
		map.put(prjName, name);
		saveFile();
	}

	public boolean isClosed() {
		return closed;
	}

	public void setClosed(boolean closed) {
		this.closed = closed;
	}

}
