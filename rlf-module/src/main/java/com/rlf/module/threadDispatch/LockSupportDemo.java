package com.rlf.module.threadDispatch;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.LockSupport;

public class LockSupportDemo {
    static Thread t1,t2;
    public static void main(String[] args) {

        char[] a = "123456".toCharArray();
        char[] b = "ABCDEF".toCharArray();

        t1 = new Thread(()->{
            for (char c : a) {
                System.out.print(c);
                LockSupport.unpark(t2);
                LockSupport.park();
            }
        });
        t1.start();

        t2 = new Thread(()->{
            for (char c : b) {
                LockSupport.park();
                System.out.print(c);
                LockSupport.unpark(t1);
            }
        });
        t2.start();



    }
}
