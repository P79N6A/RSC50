/**
 * Copyright (c) 2007-2017 思源电气股份有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application.
 */
package com.shrcn.found.file.excel;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.openxml4j.opc.PackageAccess;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.util.SAXHelper;
import org.apache.poi.xssf.eventusermodel.ReadOnlySharedStringsTable;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.eventusermodel.XSSFSheetXMLHandler;
import org.apache.poi.xssf.eventusermodel.XSSFSheetXMLHandler.SheetContentsHandler;
import org.apache.poi.xssf.model.StylesTable;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import com.shrcn.found.common.log.SCTLogger;

 /**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2018-7-29
 */
public class Xls2007Parser {

	public static List<?> parse(String xlspath, SheetsHandler sheetHandler) {
		try {
			OPCPackage xlsxPackage = OPCPackage.open(xlspath, PackageAccess.READ);
			ReadOnlySharedStringsTable strings = new ReadOnlySharedStringsTable(xlsxPackage);  
	        XSSFReader xssfReader = new XSSFReader(xlsxPackage);  
	        StylesTable styles = xssfReader.getStylesTable();  
	        XSSFReader.SheetIterator iter = (XSSFReader.SheetIterator) xssfReader.getSheetsData();  
	        while (iter.hasNext()) {
	            InputStream stream = iter.next();
	            String sheetName = iter.getSheetName();
				processSheet(styles, strings, sheetHandler, stream);
	            stream.close();
	        }
	        xlsxPackage.close();
		} catch (Throwable e) {
			SCTLogger.error(e.getMessage());
		}
		return sheetHandler.getResult();
	}

	public static void processSheet(  
            StylesTable styles,  
            ReadOnlySharedStringsTable strings,  
            SheetContentsHandler sheetHandler,  
            InputStream sheetInputStream)  
            throws IOException, ParserConfigurationException, SAXException {  
        DataFormatter formatter = new DataFormatter();  
        InputSource sheetSource = new InputSource(sheetInputStream);  
        try {  
            XMLReader sheetParser = SAXHelper.newXMLReader();  
            ContentHandler handler = new XSSFSheetXMLHandler(  
                    styles, null, strings, sheetHandler, formatter, false);  
            sheetParser.setContentHandler(handler);  
            sheetParser.parse(sheetSource);  
        } catch (ParserConfigurationException e) {  
            throw new RuntimeException("SAX parser appears to be broken - " + e.getMessage());  
        }  
    } 
}

