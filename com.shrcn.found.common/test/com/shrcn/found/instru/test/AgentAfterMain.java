/**
 * Copyright (c) 2007-2015 思源电气股份有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based on IEC61850 SCT.
 */
package com.shrcn.found.instru.test;

import java.lang.instrument.Instrumentation;

 /**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2017-10-25
 */
public class AgentAfterMain {
	
	private static Instrumentation inst;
	
	public static void agentmain(String args, Instrumentation inst)  
    {  
        System.out.println("loadagent after main run.args=" + args);  
  
        Class<?>[] classes = inst.getAllLoadedClasses();  
  
        for (Class<?> cls : classes)  
        {  
            System.out.println(cls.getName());  
        }  
        
        System.out.println("=========================");
        
        ClassLoader cloader = classes[0].getClassLoader();
        classes = inst.getInitiatedClasses(cloader);
        for (Class<?> cls : classes)  
        {  
            System.out.println(cls.getName());  
        } 
  
        System.out.println("agent run completely.");  
    }
	
	public static long size(Object o) {
		return inst.getObjectSize(o);
	}
}

