/**
 * Copyright (c) 2007-2015 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based on IEC61850 SCT.
 */
package com.shrcn.found.common.asm;

import java.io.File;
import java.io.FileOutputStream;

import org.objectweb.asm.ClassAdapter;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;

 /**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2017-11-1
 */
public class Generator {
	public static void main(String[] args) throws Exception { 
		 ClassReader cr = new ClassReader(Account.class.getName()); 
//		 ClassReader cr = new ClassReader("Account"); 
		 ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS); 
		 ClassAdapter classAdapter = new AddSecurityCheckClassAdapter(cw); 
		 cr.accept(classAdapter, ClassReader.SKIP_DEBUG); 
		 byte[] data = cw.toByteArray();
		 File file = new File("./bin/com/shrcn/found/common/asm/Account.class"); 
		 FileOutputStream fout = new FileOutputStream(file); 
		 fout.write(data); 
		 fout.close(); 
	 } 
}

