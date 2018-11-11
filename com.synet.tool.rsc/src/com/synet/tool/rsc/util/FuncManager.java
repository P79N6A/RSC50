package com.synet.tool.rsc.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

import com.shrcn.found.common.log.SCTLogger;
import com.shrcn.found.file.xml.DOM4JNodeHelper;
import com.shrcn.found.file.xml.XMLFileManager;
import com.shrcn.tool.found.das.impl.BeanDaoImpl;
import com.shrcn.tool.found.das.impl.HqlDaoImpl;
import com.synet.tool.rsc.DBConstants;
import com.synet.tool.rsc.RSCConstants;
import com.synet.tool.rsc.RSCProperties;
import com.synet.tool.rsc.model.TB1084FuncClassEntity;

public class FuncManager {

	private static FuncManager rmgr = new FuncManager();
	private RSCProperties rscPro = RSCProperties.getInstance();

	public static FuncManager getInstance() {
		return rmgr;
	}
	
	private FuncManager() {
		synFunc();
	}
	
	private void synFunc() {
		HqlDaoImpl hqlDao = HqlDaoImpl.getInstance();
		int count = hqlDao.getCount(TB1084FuncClassEntity.class);
		if (count == 0) {
			List<TB1084FuncClassEntity> funs = paseFuncXml();
			if (funs != null && funs.size() > 0) {
				BeanDaoImpl.getInstance().insertBatch(funs);
			}
		}
	}
	
	public void modify(List<TB1084FuncClassEntity> fcList) {
		File f = new File(RSCConstants.FUNCFILE);
		if (!f.exists()) {
			try {
				f.createNewFile();
			} catch (IOException e) {
				SCTLogger.error(e.getMessage());
			}
		} 
		try {
			Element rootElement = DOM4JNodeHelper.createRTUNode("FunClass");
			int i=1;
			for (TB1084FuncClassEntity fc : fcList) {
				Element ruleElement = rootElement.addElement("Item");
				ruleElement.addAttribute("index", i + "");
				ruleElement.addAttribute("name", fc.getF1084DESC());
				i++;
			}
			OutputFormat format = OutputFormat.createPrettyPrint();
            format.setEncoding("UTF-8");
			XMLWriter writer = new XMLWriter(new FileOutputStream(f), format);
            writer.setEscapeText(false);
            writer.write(rootElement.getDocument());
            writer.close();
		} catch (FileNotFoundException e) {
			SCTLogger.error(e.getMessage());
		} catch (IOException e) {
			SCTLogger.error(e.getMessage());
		}
	}
	
	private List<TB1084FuncClassEntity> paseFuncXml() {
		List<TB1084FuncClassEntity> fcsData = new ArrayList<>();
		File f = new File(RSCConstants.FUNCFILE);
		if (!f.exists()) {
			return fcsData;
		}
		try {
			Document doc = XMLFileManager.loadXMLFile(f);
			@SuppressWarnings("unchecked")
			List<Element> elements = doc.getRootElement().elements();
			for (Element el : elements) {
				TB1084FuncClassEntity fc = new TB1084FuncClassEntity();
				fc.setF1084CODE(rscPro.nextTbCode(DBConstants.PR_FUNCLASS));
				fc.setF1084DESC(el.attributeValue("name"));
				fcsData.add(fc);
			}
		} catch (FileNotFoundException e) {
			SCTLogger.error(e.getMessage());
		} catch (IOException e) {
			SCTLogger.error(e.getMessage());
		} catch (DocumentException e) {
			SCTLogger.error(e.getMessage());
		}
		return fcsData;
	}
	
}
