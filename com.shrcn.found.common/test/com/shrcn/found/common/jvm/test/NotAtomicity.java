package com.shrcn.found.common.jvm.test;

public class NotAtomicity {
    //静态变量t
    public  static long t = 0;
    //静态变量t的get方法
    public  static synchronized  long getT() {
        return t;
    }
    //静态变量t的set方法
    public  static synchronized void setT(long t) {
        NotAtomicity.t = t;
    }
    //改变变量t的线程
    public static class ChangeT implements Runnable{
        private long to;
        public ChangeT(long to) {
            this.to = to;
        }
        public void run() {
            //不断的将long变量设值到 t中
            while (true) {
                NotAtomicity.setT(to);
                //将当前线程的执行时间片段让出去，以便由线程调度机制重新决定哪个线程可以执行
                Thread.yield();
            }
        }
    }
    //读取变量t的线程，若读取的值和设置的值不一致，说明变量t的数据被破坏了，即线程不安全
    public static class ReadT implements Runnable{

        public void run() {
            //不断的读取NotAtomicity的t的值
            while (true) {
                long tmp = NotAtomicity.getT();
                //比较是否是自己设值的其中一个
                if (tmp != 100L && tmp != 200L && tmp != -300L && tmp != -400L) {
                    //程序若执行到这里，说明long类型变量t，其数据已经被破坏了
                    System.out.println(tmp);
                }
                ////将当前线程的执行时间片段让出去，以便由线程调度机制重新决定哪个线程可以执行
                Thread.yield();
            }
        }
    }
    public static void main(String[] args) {
        new Thread(new ChangeT(100L)).start();
        new Thread(new ChangeT(200L)).start();
        new Thread(new ChangeT(-300L)).start();
        new Thread(new ChangeT(-400L)).start();
        new Thread(new ReadT()).start();
    }
}