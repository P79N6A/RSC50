/**
 * Copyright (c) 2007-2015 思源电气股份有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based on IEC61850 SCT.
 */
package com.shrcn.found.instru.test;

import com.sun.tools.attach.VirtualMachine;

 /**
  * 
 * Agent-Class: com.shrcn.found.instru.test.AgentAfterMain
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2017-10-25
 */
public class RunAttach {
	public static void main(String[] args) throws Exception  
    {  
        // args[0]传入的是某个jvm进程的pid  
//        String targetPid = args[0];  
        String targetPid = "4060";  
  
        VirtualMachine vm = VirtualMachine.attach(targetPid);  
  
        vm.loadAgent("./test/agentmain.jar", "toagent");
        
//        System.out.println("o size=" + AgentAfterMain.size(new A()));
    }
}

