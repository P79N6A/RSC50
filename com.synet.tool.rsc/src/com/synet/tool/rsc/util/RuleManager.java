package com.synet.tool.rsc.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.derby.iapi.services.io.FileUtil;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

import com.shrcn.found.common.Constants;
import com.shrcn.found.common.dict.DictManager;
import com.shrcn.found.common.log.SCTLogger;
import com.shrcn.found.file.util.FileManager;
import com.shrcn.found.file.xml.DOM4JNodeHelper;
import com.shrcn.found.file.xml.XMLFileManager;
import com.synet.tool.rsc.RSCConstants;

public class RuleManager {

	private static RuleManager rmgr = new RuleManager();
	private List<Rule> rules = new ArrayList<>();

	public static RuleManager getInstance() {
		return rmgr;
	}
	
	private RuleManager() {
		load();
	}
	
	public boolean deleteRule(String fileName) {
		File f = new File(Constants.cfgDir);
		if (f.exists()) {
			File[] listFiles = f.listFiles();
			for (File file : listFiles) {
				if(file.getName().equals(fileName)) {
					if(file.isFile()) {
						return file.delete();
					}
				}
			}
		}
		return false;
	}
	
	public boolean copyRuleFile(String fileName) {
		String path = Constants.cfgDir + "/rules.xml";
		File f = new File(path);
		if (!f.exists()) {
			return false;
		}
		String newPath = Constants.cfgDir + File.separator + fileName;
		File newf = new File(newPath);
		if (!newf.exists()) {
			boolean createNewFile;
			try {
				createNewFile = newf.createNewFile();
				if(createNewFile) {
					return FileUtil.copyFile(f, newf);
				} else {
					return createNewFile;
				}
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}
		}
		return false;
		
	}
	
	public String[] getRuleList() {
		String path = Constants.cfgDir;
		File f = new File(path);
		List<String> fileNames = new ArrayList<>();
		if(f.exists()) {
			File[] listFiles = f.listFiles();
			for (int i = 0; i < listFiles.length; i++) {
				String name = listFiles[i].getName();
				if(name.contains("rule")) {
					fileNames.add(name);
				}
			}
			
		}
		return fileNames.toArray(new String[fileNames.size()]);
	}

	public void modify(List<Rule> ruleList, String selName) {
		String path = Constants.cfgDir + File.separator + selName;
		File f = new File(path);
		if (!f.exists()) {
			try {
				f.createNewFile();
			} catch (IOException e) {
				SCTLogger.error(e.getMessage());
			}
		} 
		
		rules.clear();
		rules.addAll(ruleList);
		
		DictManager dictmgr = DictManager.getInstance();
		String dicttype = F1011_NO.class.getSimpleName();
		dictmgr.removeDict(dicttype);
		dictmgr.addDict(dicttype, dicttype, F1011_NO.getDictItems());
		
		try {
			Document doc = XMLFileManager.loadXMLFile(f);
			doc.remove(doc.getRootElement());
			Element rootElement = doc.addElement("RuleCfg");
			rootElement.addAttribute("bundleID", "com.shrcn.tool.ecfg");
			for (Rule rule : rules) {
					Element ruleElement = rootElement.addElement("Rule");
					ruleElement.addAttribute("id", rule.getId()+"");
					ruleElement.addAttribute("name", rule.getName());
					ruleElement.addAttribute("datSet", rule.getDatSet());
					ruleElement.addAttribute("lnName", rule.getLnName());
					ruleElement.addAttribute("doName", rule.getDoName());
					ruleElement.addAttribute("doDesc", rule.getDoDesc());
			}
			OutputFormat format = OutputFormat.createPrettyPrint();
            format.setEncoding("UTF-8");
			XMLWriter writer = new XMLWriter(new FileOutputStream(f), format);
            writer.setEscapeText(false);
            writer.write(doc);
            writer.close();
		} catch (FileNotFoundException e) {
			SCTLogger.error(e.getMessage());
		} catch (IOException e) {
			SCTLogger.error(e.getMessage());
		} catch (DocumentException e) {
			SCTLogger.error(e.getMessage());
		}
		
	}
	
	private void load() {
		String path = Constants.cfgDir + "/rules.xml";
		rules = paseRuleXml(path);
	}
	
	public boolean reNameRuleFile(String oldName, String newName) {
		String oldpath = Constants.cfgDir + File.separator + oldName;
		File oldf = new File(oldpath);
		if (!oldf.exists()) {
			return false;
		}
		String newpath = Constants.cfgDir + File.separator + newName;
		File newf = new File(newpath);
		return oldf.renameTo(newf);
	}

	private List<Rule> paseRuleXml(String path) {
		List<Rule> rulesData = new ArrayList<>();
		File f = new File(path);
		if (!f.exists()) {
			InputStream inputStream = getClass().getClassLoader().getResourceAsStream(RSCConstants.RULEURL);
			FileManager.saveInputStream(inputStream, f);
			try {
				inputStream.close();
			} catch (IOException e) {
				SCTLogger.error(e.getMessage());
			}
		}
		try {
			Document doc = XMLFileManager.loadXMLFile(f);
			@SuppressWarnings("unchecked")
			List<Element> elements = doc.getRootElement().elements();
			for (Element el : elements) {
				Rule rule = new Rule();
				DOM4JNodeHelper.copyAttributes(el, rule);
				rulesData.add(rule);
			}
		} catch (FileNotFoundException e) {
			SCTLogger.error(e.getMessage());
		} catch (IOException e) {
			SCTLogger.error(e.getMessage());
		} catch (DocumentException e) {
			SCTLogger.error(e.getMessage());
		}
		return rulesData;
	}
	
	public List<Rule> getRulesByFileName(String fileName) {
		String path = Constants.cfgDir + File.separator + fileName;
		return paseRuleXml(path);
	}
	
	public Rule getRule(int id) {
		return rules.get(id);
	}
	
	public List<Rule> getRules() {
		List<Rule> result = new ArrayList<>();
		for (Rule rule : rules) {
			result.add(rule);
		}
		return result;
	}
}
