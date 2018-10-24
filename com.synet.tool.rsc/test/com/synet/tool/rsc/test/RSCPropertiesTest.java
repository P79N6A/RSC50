/**
 * Copyright (c) 2018-2019 上海泗业技术咨询有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application.
 */
package com.synet.tool.rsc.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.dom4j.Document;
import org.dom4j.io.SAXReader;
import org.junit.Test;

import com.synet.tool.rsc.RSCProperties;

/**
 * 
 * @author 陈春(mailto:chench80@126.com)
 * @version 1.0, 2018-8-15
 */
public class RSCPropertiesTest {

	@Test
	public void testNextTbCode() {
		RSCProperties rscp = RSCProperties.getInstance();
		for (int i = 0; i < 25; i++) {
			String id = rscp.nextTbCode("IED");
			System.out.println(id);
		}
	}

	@Test
	public void testReadFile() {
		try {
			String path = "D:/eclipse_rcp/eclipse-rcp-indigo-SR2-win32/eclipse-rcp-indigo-SR2-win32/eclipse/template/SiFangJFZ6001.00.xml";
			StringBuffer buffer = new StringBuffer();
			BufferedReader bf = new BufferedReader(new FileReader(path));
			String s = null;
			while ((s = bf.readLine()) != null) {// 使用readLine方法，一次读一行
				buffer.append(s.trim());
			}

			String xml = buffer.toString();
			System.out.println(xml);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void test5() throws Exception {// xml文档或节点转换为字符串
		String path = "D:/eclipse_rcp/eclipse-rcp-indigo-SR2-win32/eclipse-rcp-indigo-SR2-win32/eclipse/template/SiFangJFZ6001.00.xml";

		// 创建SAXReader对象
		SAXReader reader = new SAXReader();
		// 读取文件 转换成Document
		Document document = reader.read(new File(path));
		// document转换为String字符串
		String documentStr = document.asXML();
		System.out.println("document 字符串：\n" + documentStr);

		// //获取根节点
		// Element root = document.getRootElement();
		// //根节点转换为String字符串
		// String rootStr = root.asXML();
		// System.out.println("root 字符串：" + rootStr);
		// //获取其中student1节点
		// Element student1Node = root.element("student1");
		// //student1节点转换为String字符串
		// String student1Str = student1Node.asXML();
		// System.out.println("student1 字符串：" + student1Str);
	}

	

}
