/**
 * Copyright (c) 2007-2015 思源电气股份有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based on IEC61850 SCT.
 */
package com.shrcn.found.common.jvm.test;

/**
 * -Xss128K
 * -XX:+PrintGC
 * 每一个localvarGcN()函数都分配了一块6M的堆内存，并使用局部变量引用这块空间。

在localvarGc1()中，在申请空间后，立即进行垃圾回收，很明显由于byte数组被变量a引用，因此无法回收这块空间。

在localvarGc2()中，在垃圾回收前，先将变量a置为null，使得byte数组失去强引用，故垃圾回收可以顺利回收byte数组。

在localvarGc3()中，在进行垃圾回收前，先使局部变量a失效，虽然变量a已经离开了作用域，但是变量a依然存在于局部变量表中，
并且也指向这块byte数组，故byte数组依然无法被回收。

对于localvarGc4()，在垃圾回收之前，不仅使变量a失效，更是声明了变量c，使变量c复用了变量a的字，由于变量a此时被销毁，
故垃圾回收器可以顺利回收数组byte

对于localvarGc5()，它首先调用了localvarGc1()，很明显，在localvarGc1()中并没有释放byte数组，
但在localvarGc1()返回后，它的栈帧被销毁，自然也包含了栈帧中的所有局部变量，故byte数组失去了引用，
在localvarGc5()的垃圾回收中被回收。

可以使用-XX:+printGC执行上述几个函数，在输出日志里，可以看到垃圾回收前后堆的大小，进而推断出byte数组是否被回收。

下面的输出是函数localvarGc4()的运行结果：

[GC (System.gc()) 7618K->624K(94208K), 0.0015613 secs]
[Full GC (System.gc()) 624K->526K(94208K), 0.0070718 secs]

从日志中可以看出，堆空间从回收前的7618K变为回收后的624K，释放了>6M的空间，byte数组已经被回收释放。

 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2017-10-11
 */
public class LocalvarGC {
	public void localvarGc1() {
		byte[] a = new byte[6 * 1024 * 1024];// 6M
		System.gc();
	}

	public void localvarGc2() {
		byte[] a = new byte[6 * 1024 * 1024];
		a = null;
		System.gc();
	}

	public void localvarGc3() {
		{
			byte[] a = new byte[6 * 1024 * 1024];
		}
		System.gc();
	}

	public void localvarGc4() {
		{
			byte[] a = new byte[6 * 1024 * 1024];
		}
		int c = 10;
		System.gc();
	}

	public void localvarGc5() {
		localvarGc1();
		System.gc();
	}

	public static void main(String[] args) {
		LocalvarGC ins = new LocalvarGC();
//		ins.localvarGc1();
//		ins.localvarGc2();
//		ins.localvarGc3();
//		ins.localvarGc4();
		ins.localvarGc5();
	}
}
