package com.synet.tool.rsc.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

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

	public void modify(List<Rule> ruleList) {
		String path = Constants.cfgDir + "/rules.xml";
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
		sortRules();
		
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
				rules.add(rule);
			}
			sortRules();
		} catch (FileNotFoundException e) {
			SCTLogger.error(e.getMessage());
		} catch (IOException e) {
			SCTLogger.error(e.getMessage());
		} catch (DocumentException e) {
			SCTLogger.error(e.getMessage());
		}
	}
	
	private void sortRules() {
		Collections.sort(rules, new Comparator<Rule>() {
			@Override
			public int compare(Rule r1, Rule r2) {
				return r1.getId() - r2.getId();
			}});
	}
	
	public List<Rule> getRules() {
		return rules;
	}
}
