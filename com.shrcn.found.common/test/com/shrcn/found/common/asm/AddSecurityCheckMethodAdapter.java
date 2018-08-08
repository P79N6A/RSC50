/**
 * Copyright (c) 2007-2015 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based on IEC61850 SCT.
 */
package com.shrcn.found.common.asm;

import org.objectweb.asm.MethodAdapter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

 /**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2017-11-1
 */
public class AddSecurityCheckMethodAdapter extends MethodAdapter { 
	 public AddSecurityCheckMethodAdapter(MethodVisitor mv) { 
		 super(mv); 
	 } 

	 public void visitCode() { 
		 visitMethodInsn(Opcodes.INVOKESTATIC,
			 "com/shrcn/found/common/asm/SecurityChecker", 
			"checkSecurity", "()V"); 
	 } 
 }

