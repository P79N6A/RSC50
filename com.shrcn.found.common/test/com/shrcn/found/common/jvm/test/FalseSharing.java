/**
 * Copyright (c) 2007-2015 思源电气股份有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based on IEC61850 SCT.
 */
package com.shrcn.found.common.jvm.test;

 /**
 * 补齐缓存
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2017-10-24
 */
public class FalseSharing implements Runnable {
//	public static int NUM_THREADS = 4;   
//    public final static long ITERATIONS = 500L * 1000L * 1000L; 
	public static int NUM_THREADS = 16;   
    public final static long ITERATIONS = 5L * 1L * 100L; 
    private final int arrayIndex;  
    private static VolatileLong[] longs;  
  
    public FalseSharing(final int arrayIndex) {  
        this.arrayIndex = arrayIndex;  
    }  
  
    public static void main(final String[] args) throws Exception {  
        Thread.sleep(1000);  
        System.out.println("starting....");  
        if (args.length == 1) {  
            NUM_THREADS = Integer.parseInt(args[0]);  
        }  
  
        longs = new VolatileLong[NUM_THREADS];  
        for (int i = 0; i < longs.length; i++) {  
            longs[i] = new VolatileLong();  
        }  
        final long start = System.nanoTime();  
        runTest();  
        System.out.println("duration = " + (System.nanoTime() - start));  
    }  
  
    private static void runTest() throws InterruptedException {  
        Thread[] threads = new Thread[NUM_THREADS];  
        for (int i = 0; i < threads.length; i++) {  
            threads[i] = new Thread(new FalseSharing(i));  
        }  
        for (Thread t : threads) {  
            t.start();  
        }  
        for (Thread t : threads) {  
            t.join();  
        }  
    }  
  
    public void run() {  
        long i = ITERATIONS + 1;  
        while (0 != --i) {  
            longs[arrayIndex].value = i;  
        }  
    }  
  
    public final static class VolatileLong {  
        public volatile long value = 0L;  
        public long p1, p2, p3, p4, p5, p6; 
    }  
}  

//37037254304
//51376922376

//    2				4			8			16
//233759308		174238814	283267201	541311464
//328929448		513972929	455633616	776348341
