package com.rlf.module.threadDispatch;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.*;

/**
 * @author RU
 * @date 2020/7/30
 * @Desc
 */
public class ThreadCallableDemo {
    ///多个线程同时执行, 只要有一个线程返回false,则立马关闭其他线程
    public static void main(String[] args) throws Exception{
        ExecutorService pool = Executors.newFixedThreadPool(3);
        List<Future<Boolean>> futures = new ArrayList<>();
        Future<Boolean> a = pool.submit(() -> {
            Thread.sleep(2000);
            System.out.println(2);
            return true;
        });
        futures.add(a);
        Future<Boolean> b = pool.submit(() -> {
            Thread.sleep(5000);
            System.out.println(5);
            return true;
        });
        futures.add(b);
        boolean isWrong = false;

        CyclicBarrier cb = new CyclicBarrier(2);
        Set<Future> futureSet = new LinkedHashSet<>();//防止重复，记录完成的对象
        while (true){
            boolean flag = false;
            for (Future<Boolean> future : futures) {
               if(future.isDone()){
                   futureSet.add(future);
                   if(!future.get()){
                       for (Future<Boolean> f : futures) {
                           f.cancel(true);
                       }
                       flag = true;
                       break;
                   }
               }

            }
            if(flag || futures.size() == futureSet.size()){

                new Thread(()->{
                    try {
                        cb.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (BrokenBarrierException e) {
                        e.printStackTrace();
                    }
                }).start();
                if(flag){
                    isWrong = true;
                }
                break;
            }
        }
        cb.await();
        System.out.println("wode");
        if(isWrong){
            System.out.println("其中一个线程返回了false");
        }

        pool.shutdown();
        System.out.println("线程关闭了");
    }

}
