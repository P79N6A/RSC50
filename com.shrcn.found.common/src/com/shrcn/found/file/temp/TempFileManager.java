package com.shrcn.found.file.temp;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

public class TempFileManager {
	
	public static void saveUTF8File(String content, String filepath) {
		saveUTF8File(content, new File(filepath));
	}
	
	public static void saveUTF8File(String content, File target) {
		XMLWriter writer = null;
		try {
			SAXReader saxReader = new SAXReader();
			saxReader.setEncoding("UTF-8");
			Document doc = saxReader.read(new ByteArrayInputStream(content.getBytes("UTF-8")));
			OutputFormat format = OutputFormat.createPrettyPrint();
			format.setEncoding("UTF-8");
			writer = new XMLWriter(new FileOutputStream(target), format);
			writer.write(doc);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (DocumentException e) {
		} finally {
			try {
				if(null != writer)
					writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void initDir(String strDir) {
		File fDir = new File(strDir);
		if (!fDir.exists()) {
			fDir.mkdirs();
		} else {
			if (!fDir.isDirectory()) {
				fDir.delete();
				fDir.mkdir();
			}
		}
	}
	
	public static String readTextFile(String filePath) {
		try {
			InputStream is = new FileInputStream(filePath);
			return readTextFile(is);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static String readTextFile(InputStream is) {
		String text = null;
		try {
			int size = is.available();
			byte[] data = new byte[size];
			is.read(data, 0, size);
			text = new String(data);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (is != null)
					is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return text;
	}
	
	public static String readTextFile(Class<?> baseClass, String fileName) {
		InputStream is = baseClass.getClassLoader().getResourceAsStream(fileName);
		return readTextFile(is);
	}
	
	public static void main(String[] args) {
		String eq = readTextFile(TempFileManager.class, "com/shrcn/tool/rtu/util/temp/system/coPoint.eq");
		System.out.println(eq);
	}
	
	/**
	 * 加载property文件
	 * @param properties
	 * @param path
	 * @param clazz
	 */
	public static void loadPropertiesRes(Properties properties, String path, Class<?> clazz) {
		InputStream is = null;
		try {
			ClassLoader loader = clazz.getClassLoader();
			is = loader.getResourceAsStream(path);
			properties.load(is);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
