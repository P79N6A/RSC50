package com.synet.tool.rsc.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;

import com.shrcn.found.common.Constants;
import com.shrcn.found.common.log.SCTLogger;
import com.shrcn.found.file.util.FileManager;
import com.shrcn.found.file.xml.DOM4JNodeHelper;
import com.shrcn.found.file.xml.XMLFileManager;
import com.synet.tool.rsc.RSCConstants;

public class RuleManager {

	private static RuleManager rmgr = new RuleManager();
	private Map<Integer, Rule> rules = new HashMap<>();

	public static RuleManager getInstance() {
		return rmgr;
	}
	
	private RuleManager() {
		load();
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
			List<Element> elements = doc.getRootElement().elements();
			for (Element el : elements) {
				Rule rule = new Rule();
				DOM4JNodeHelper.copyAttributes(el, rule);
				rules.put(rule.getId(), rule);
			}
		} catch (FileNotFoundException e) {
			SCTLogger.error(e.getMessage());
		} catch (IOException e) {
			SCTLogger.error(e.getMessage());
		} catch (DocumentException e) {
			SCTLogger.error(e.getMessage());
		}
	}
	
	public Rule getRule(int id) {
		return rules.get(id);
	}
}
