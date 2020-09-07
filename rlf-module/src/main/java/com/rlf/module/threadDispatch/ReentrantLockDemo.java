package com.rlf.module.threadDispatch;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.LockSupport;
import java.util.concurrent.locks.ReentrantLock;

public class ReentrantLockDemo {

    public static void main(String[] args) {
        char[] a = "123456".toCharArray();
        char[] b = "ABCDEF".toCharArray();
        char[] d = "!@#$%^".toCharArray();

        ReentrantLock lock = new ReentrantLock();
        Condition condition1 = lock.newCondition();
        Condition condition2 = lock.newCondition();
        Condition condition3 = lock.newCondition();
        CountDownLatch downLatch = new CountDownLatch(1);
        new Thread(()->{
            try{
                //downLatch.await();
                lock.lock();
                for (char c : a) {
                    System.out.print(c);
                    condition2.signal();
                    condition1.await();
                }
                condition2.signal();
            }catch (InterruptedException e){
            }finally {
                lock.unlock();
            }
        }).start();

        new Thread(()->{
            try{
                lock.lock();
               // downLatch.countDown();
                for (char c : b) {
                    System.out.print(c);
                    condition1.signal();
                    condition2.await();
                }
                condition1.signal();

            }catch (InterruptedException e){
            }finally {
                lock.unlock();
            }
        }).start();

        //3个线程通信 依次输出

//        new Thread(()->{
//            try{
//                lock.lock();
//                for (char c : a) {
//                    System.out.print(c);
//                    condition2.signal();
//                    condition1.await();
//                }
//                condition2.signal();
//            }catch (InterruptedException e){
//            }finally {
//                lock.unlock();
//            }
//        }).start();
//
//        new Thread(()->{
//            try{
//                lock.lock();
//                for (char c : b) {
//                    System.out.print(c);
//                    condition3.signal();
//                    condition2.await();
//                }
//                condition3.signal();
//
//            }catch (InterruptedException e){
//            }finally {
//                lock.unlock();
//            }
//        }).start();
//
//        new Thread(()->{
//            try{
//                lock.lock();
//                for (char c : d) {
//                    System.out.print(c);
//                    condition1.signal();
//                    condition3.await();
//                }
//                condition1.signal();
//
//            }catch (InterruptedException e){
//            }finally {
//                lock.unlock();
//            }
//        }).start();
    }
}
