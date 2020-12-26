package com.rlf.module.threadDispatch;

import com.google.common.collect.Lists;

import java.util.*;
import java.util.concurrent.*;

/**
 * @author RU
 * @date 2020/7/30
 * @Desc
 */
public class CallableThreadDemo {

    //模拟情况,一组列表,需要多线程并行查询
    //只要一个结果返回false,则直接停止其他的线程查询,继续执行下面逻辑
    public static void main(String[] args) throws Exception{
        CallableThreadDemo testCallable = new CallableThreadDemo();
        Boolean aBoolean = testCallable.myTest();
        System.out.println(aBoolean);
        System.out.println("执行完成开始下面逻辑");
    }

    public Boolean myTest() throws Exception{
        List<Future<Boolean>> futureTasks = new ArrayList<>();
        List<Integer> list = Lists.newArrayList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        ExecutorService pool = Executors.newFixedThreadPool(10);
        list.parallelStream().forEach(ele ->{
            Future f = pool.submit(()->{
                if(ele == 2){
                    Thread.sleep(2000);
                    return false;
                }else{
                    Thread.sleep(6000);
                    return true;
                }
            });
            futureTasks.add(f);
        });
//        for (Integer ele : list) {
//            Future f = pool.submit(()->{
//                if(ele == 2){
//                    Thread.sleep(2000);
//                    return false;
//                }else{
//                    Thread.sleep(6000);
//                    return true;
//                }
//            });
//            futureTasks.add(f);
//        }
        // 关闭线程池
        pool.shutdown();

        // 获取所有并发任务的运行结果
        int count = 0;
        CyclicBarrier cb = new CyclicBarrier(2);
        Set<Future> futureSet = new LinkedHashSet<>();//防止重复，记录完成的对象
        Boolean result = true;
        while (true){
            //用于跳出循环的条件, 只要有一个线程返回false,则修改该值为true;
            boolean flag = false;
            for (Future f : futureTasks) {
                if(f.isDone()){
                    futureSet.add(f);
                    // 从Future对象上获取任务的返回值，并输出到控制台
                    System.out.println(">>>" + f.get().toString());
                    Boolean resBoo = Boolean.valueOf(f.get().toString());

                    if(!resBoo){
                        //同时将其他的线程全部取消了
                        for(Future f1 : futureTasks){
                            f1.cancel(true);
                        }
                        flag = true;
                        result = false;
                        break;
                    }

                }

            }
            count = futureSet.size();
            if(flag || count==futureTasks.size()){
                new Thread(()->{
                    try {
                        cb.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (BrokenBarrierException e) {
                        e.printStackTrace();
                    }
                }).start();
                break;
            }
        }
        cb.await();
        return result;
    }
}
