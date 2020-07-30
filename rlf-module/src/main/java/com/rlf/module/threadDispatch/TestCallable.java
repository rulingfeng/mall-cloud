package com.rlf.module.threadDispatch;

import java.util.*;
import java.util.concurrent.*;

/**
 * @author RU
 * @date 2020/7/30
 * @Desc
 */
public class TestCallable {

    public static void main(String[] args) throws Exception{
        TestCallable testCallable = new TestCallable();
        testCallable.myTest();
    }

    public void myTest() throws Exception{
        long l = System.currentTimeMillis();
        int taskSize = 10;
        List<Future<Integer>> futureTasks = new ArrayList<>();
        ExecutorService pool = Executors.newFixedThreadPool(taskSize);
        for(int i=0;i<taskSize;i++){
            int type = i;
            Future f = pool.submit(()->{
                Random random = new Random();
                int a = random.nextInt(10)+1;
                if(type == 2){
                    Thread.sleep(10000);
                }else{
                    Thread.sleep(1000);
                }

                System.out.println(Thread.currentThread().getName()+":随机值a:"+a+",类型i："+ type);
                return a;
            });
            futureTasks.add(f);
        }
        // 关闭线程池
        pool.shutdown();
        System.out.println("线程关闭了");
        int sum = 0;
        // 获取所有并发任务的运行结果
        int count = 0;
        CyclicBarrier cb = new CyclicBarrier(2);
        Set<Future> futureSet = new LinkedHashSet<>();//防止重复，记录完成的对象
        while (true){
            boolean flag = false;
            for (Future f : futureTasks) {
                if(f.isDone()){
                    futureSet.add(f);
                    // 从Future对象上获取任务的返回值，并输出到控制台
                    System.out.println(">>>" + f.get().toString());
                    int aa = Integer.valueOf(f.get().toString());
                    if(aa == 4){
                        sum = aa;
                        //同时将其他的线程全部取消了
                        for(Future f1 : futureTasks){
                            f1.cancel(true);
                        }
                        flag = true;
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
        System.out.println("wode"+sum);

        long end = System.currentTimeMillis();
        System.out.println(end - l);
    }
}
