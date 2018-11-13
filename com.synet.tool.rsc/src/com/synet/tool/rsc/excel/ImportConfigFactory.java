package com.synet.tool.rsc.excel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.Element;

import com.shrcn.found.common.log.SCTLogger;
import com.shrcn.found.file.excel.SheetsHandler;
import com.shrcn.found.file.xml.XMLFileManager;
import com.synet.tool.rsc.ExcelConstants;
import com.synet.tool.rsc.excel.handler.IEDBoardHandler;
import com.synet.tool.rsc.excel.handler.ImportConfigHandler;
import com.synet.tool.rsc.model.IM102FibreListEntity;

public class ImportConfigFactory {
	
	private static Map<Class<?>, ImportConfig> cfgCache = new HashMap<>();
	
	static {
		loadDefault();
	}
	
	public static void loadDefault() {
		try {
			Document doc = XMLFileManager.loadXMLFile(ImportConfigFactory.class, "com/synet/tool/rsc/excel/importConfig.xml");
			@SuppressWarnings("unchecked")
			List<Element> elements = doc.getRootElement().elements();
			for (Element el : elements) {
				String clName = el.attributeValue("class");
				String prefix = el.attributeValue("prefix");
				Class<?> clazz = Class.forName(clName);
				Map<Integer, ImportField> entityPropties = new HashMap<>();
				List<Element> fs = el.elements();
				for (Element elF : fs) {
					String col = elF.attributeValue("col");
					String name = elF.attributeValue("name");
					String title = elF.attributeValue("title");
					String notnull = elF.attributeValue("notnull");
					int colInt = Integer.parseInt(col);
					ImportField field = new ImportField(colInt, title, name);
					if (colInt < 0) {
						colInt = Integer.MAX_VALUE;
						field.setColIndex(colInt);
						field.setPrefix(prefix);
					}
					if (notnull != null) {
						field.setNotNull(Boolean.parseBoolean(notnull));
					}
					entityPropties.put(colInt, field);
				}
				ImportConfig config = new ImportConfig(clazz, entityPropties);
				cfgCache.put(clazz, config);
			}
		} catch (ClassNotFoundException e) {
			SCTLogger.error("", e);
		}
	}
	
	public static SheetsHandler getImportHandler(int headRowNum, Map<Integer, String> excelColInfo, String fileType) {
		if (EnumFileType.IED_BOARD.getTitle().equals(fileType)) {
			return new IEDBoardHandler(headRowNum, excelColInfo);
		} else {
			ImportConfig config = ImportConfigFactory.getImportConfig(fileType);
			config.synFieldCols(excelColInfo);
			return new ImportConfigHandler(headRowNum, config);
		}
	}
	
	public static ImportConfig getImportConfig(String title) {
		EnumFileType type = EnumFileType.getByTitle(title);
		return cfgCache.get(type.getEntityClass());
	}

	public static ImportConfig getFiberImportConfig() {
		return cfgCache.get(IM102FibreListEntity.class);
		
	}
	
	public static String[] getExcelFields(String title) {
		return getImportConfig(title).getExcelFields();
	}
}
