package com.rlf.module.threadDispatch;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.ReentrantLock;

//线程通信  线程调度
public class WaitAndNotify {

    public static void main(String[] args){
        CountDownLatch count = new CountDownLatch(1);
        char[] a = "123456".toCharArray();
        char[] b = "ABCDEF".toCharArray();
        final Object o = new Object();
        new Thread(()->{
            try {
                count.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            synchronized (o){
                try {
                    for (char c : a) {
                        System.out.print(c);
                        o.notify();
                        o.wait();
                    }
                    o.notify();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        new Thread(()->{
            synchronized (o){
                try {
                    for (char c : b) {
                        System.out.print(c);
                        count.countDown();
                        o.notify();
                        o.wait();
                    }
                    o.notify();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

}
