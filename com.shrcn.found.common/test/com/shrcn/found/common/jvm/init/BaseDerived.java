/**
 * Copyright (c) 2007-2015 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based on IEC61850 SCT.
 */
package com.shrcn.found.common.jvm.init;

 /**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2017-11-1
 */
public class BaseDerived {
    public static void main(String[] args)
    {
        Derived d = new Derived();
        System.out.println( d.whenAmISet );
    }
}

