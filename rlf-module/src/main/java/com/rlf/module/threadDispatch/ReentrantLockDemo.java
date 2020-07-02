package com.rlf.module.threadDispatch;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.LockSupport;
import java.util.concurrent.locks.ReentrantLock;

public class ReentrantLockDemo {

    public static void main(String[] args) {
        char[] a = "123456".toCharArray();
        char[] b = "ABCDEF".toCharArray();

        ReentrantLock lock = new ReentrantLock();
        Condition condition1 = lock.newCondition();
        Condition condition2 = lock.newCondition();

        new Thread(()->{
            try{
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

    }
}
