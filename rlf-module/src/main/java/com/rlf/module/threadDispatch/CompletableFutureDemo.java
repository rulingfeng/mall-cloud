package com.rlf.module.threadDispatch;

import com.alibaba.fastjson.JSONObject;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import io.swagger.models.auth.In;
import lombok.Data;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;


/**
 * @author 茹凌丰
 * @Title:
 * @Package
 * @Description:
 * @date 2020/7/1113:20
 */
public class CompletableFutureDemo {

    private static int coreThreads = Runtime.getRuntime().availableProcessors();

    private static ThreadFactory namedThread = new ThreadFactoryBuilder().setNameFormat("worker-thread-%d").build();

    private static final ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(coreThreads, coreThreads << 1 + 1,
            300, TimeUnit.SECONDS, new LinkedBlockingQueue<>(1000), namedThread);

    public static void main(String[] args)throws Exception {
        //3个CompletableFuture  只要有一个返回false 其他2个线程立马停止 不在执行.
        //rightOffReturnFalseIfHasFalse();
        //同时执行两个不相关任务，然后合并
        //performMerge();
        //在两个阶段都执行完后运行一个 Runnable
        //runAfterBoth();
        //组合 CompletableFuture     两结果处理完后再进行  两结果的处理 返回最终结果
        //twoResultToOneFinalResult();
        //List流处理
        //listStreamHandle();
        //listStreamHandleForMap();
        test1();
    }

    public static void test1(){
        long start = System.currentTimeMillis();
        // 结果集
        List<String> list = new ArrayList<>();

        ExecutorService executorService = Executors.newFixedThreadPool(10);
        Integer.toString(5);
        List<Integer> taskList = Arrays.asList(2, 1, 3, 4, 5, 6, 7, 8, 9, 10);
        // 全流式处理转换成CompletableFuture[]+组装成一个无返回值CompletableFuture，join等待执行完毕。返回结果whenComplete获取
        CompletableFuture[] cfs = taskList.stream()
                .map(integer -> CompletableFuture.supplyAsync(() -> calc(integer), executorService)
                        .thenApply(h->Integer.toString(h))
                        .whenComplete((s, e) -> {
                            System.out.println("任务"+s+"完成!result="+s+"，异常 e="+e+","+new Date());
                            list.add(s);
                        })
                ).toArray(CompletableFuture[]::new);
        // 封装后无返回值，必须自己whenComplete()获取
        CompletableFuture.allOf(cfs).join();
        System.out.println("list="+list+",耗时="+(System.currentTimeMillis()-start));

    }

    public static int calc(Integer i) {
        try {
            if (i == 1) {
                Thread.sleep(3000);//任务1耗时3秒
            } else if (i == 5) {
                Thread.sleep(5000);//任务5耗时5秒
            } else {
                Thread.sleep(1000);//其它任务耗时1秒
            }
            System.out.println("task线程：" + Thread.currentThread().getName()
                    + "任务i=" + i + ",完成！+" + new Date());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return i;
    }

    public static  void listStreamHandleForMap(){

        Map<Integer,List<String>> aList = new HashMap<>();
        aList.put(1,new ArrayList<>(Arrays.asList("xyz", "abc")));
        aList.put(2,new ArrayList<>(Arrays.asList("qwe", "poi")));

        List<Integer> lista = new ArrayList<>(aList.keySet());

        List<CompletableFuture<List<Boolean>>> futureList = lista.stream()
                .map(strings -> CompletableFuture.supplyAsync(() -> processListForMap(strings,aList), threadPoolExecutor))
                .collect(toList());

        //Wait for them all to complete
        CompletableFuture.allOf(futureList.toArray(new CompletableFuture[0])).join();
        List<List<Boolean>> collect = futureList.stream()
                .map(CompletableFuture::join).collect(toList());
        System.out.println(collect);

        List<Boolean> list = collect.stream().flatMap(List::stream).collect(toList());
        System.out.println(list);
        threadPoolExecutor.shutdown();
    }
    public static  List<Boolean> processListForMap(Integer temp,Map<Integer,List<String>> lists) {
        List<String> tempList = lists.get(temp);
        for (String string : tempList) {
            System.out.println("Output: " + string);
        }
        List<Boolean> list = new ArrayList<>();
        list.add(true);
        list.add(false);
        return list;
    }



    public static  void listStreamHandle(){

        List<List<String>> aList = new ArrayList<>();
        aList.add(new ArrayList<>(Arrays.asList("xyz", "abc")));
        aList.add(new ArrayList<>(Arrays.asList("qwe", "poi")));
        ExecutorService executor = Executors.newFixedThreadPool(aList.size());
        //List<String> collect = aList.stream().flatMap(List::stream).collect(Collectors.toList());
        //System.out.println(collect);
        List<CompletableFuture<List<Boolean>>> futureList = aList.stream()
                .map(strings -> CompletableFuture.supplyAsync(() -> processList(strings), threadPoolExecutor))
                .collect(toList());

        //Wait for them all to complete
        CompletableFuture.allOf(futureList.toArray(new CompletableFuture[0])).join();
        List<List<Boolean>> collect = futureList.stream()
                .map(CompletableFuture::join).collect(toList());
        System.out.println(collect);

        List<Boolean> list = collect.stream().flatMap(List::stream).collect(toList());
        System.out.println(list);
        threadPoolExecutor.shutdown();

    }
    public static  List<Boolean> processList(List<String> tempList) {
        for (String string : tempList) {
            System.out.println("Output: " + string);
        }
        List<Boolean> list = new ArrayList<>();
        list.add(true);
        list.add(false);
        return list;
    }





    public static void twoResultToOneFinalResult() throws ExecutionException, InterruptedException {
        String original = "Message";
        CompletableFuture cf = CompletableFuture.completedFuture(original).thenApply(s -> delayedUpperCase(s))
                .thenCompose(upper -> CompletableFuture.completedFuture(original).thenApply(s -> delayedLowerCase(s))
                        .thenApply(s -> upper + s));
        System.out.println(cf.join());
        //输出: MESSAGEmessage
    }

    private static String delayedLowerCase(String s) {
        return s.toLowerCase();
    }

    private static String delayedUpperCase(String s) {
        return s.toUpperCase();
    }


    public static void runAfterBoth(){
        String original = "Message";
        StringBuilder result = new StringBuilder();
        AtomicInteger a = new AtomicInteger();
//        CompletableFuture.completedFuture(original).thenApply(result::append).runAfterBoth(
//                CompletableFuture.completedFuture(original).thenApply(result::append),
//                () -> result.append("done"));
        CompletableFuture.completedFuture(original).thenApply(result::append).runAfterBoth(
                CompletableFuture.completedFuture(original).thenApply(result::append), ()->{
                    int length = result.length();
                    a.set(length);
                });
        System.out.println(result.toString());
        System.out.println(a.get());

        System.out.println(111111111);

//        输出: MessageMessage
//                14
//                111111111
    }


    public static void rightOffReturnFalseIfHasFalse()throws Exception{
        CompletableFuture.supplyAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(3);
                System.out.println(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return true;
        }).thenAccept(i -> res(i));

        CompletableFuture.supplyAsync(()->{
            try {
                TimeUnit.SECONDS.sleep(8);
                System.out.println(8);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return true;
        }).thenAccept(i->res(i));

        CompletableFuture.supplyAsync(()->{
            try {
                TimeUnit.SECONDS.sleep(20);
                System.out.println(20);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return true;
        }).thenAccept(i->res(i));
        System.in.read();
    }

    private static void res(boolean flag){
        if(false == flag){
            //处理结束流程
            //通知其他线程结束(回滚)
            //超时处理
            System.exit(0);
        }
    }

    public static void performMerge(){
        CompletableFuture<List<EntityDemo>> a = CompletableFuture.supplyAsync(CompletableFutureDemo::getNotAll);

        CompletableFuture<List<EntityDemo>> b = CompletableFuture.supplyAsync(CompletableFutureDemo::getAll).thenApply(CompletableFutureDemo::buildName);
        CompletableFuture<List<EntityDemo>> c = CompletableFuture.supplyAsync(CompletableFutureDemo::getAll).thenApply(CompletableFutureDemo::buildName);

        List<EntityDemo> join = a.thenCombine(b, (aa, bb) -> {
            bb.stream().forEach(aa::add);
            return aa;
        }).thenCombine(c,(aa,bb)->{
            aa.addAll(bb);
            return aa;
        }).join();
        join.forEach(System.out::println);
        System.out.println(1);
    }

    @Data
    static class EntityDemo{
        private Integer id;
        private String name;
    }

    public static List<EntityDemo> getNotAll() {
        List<EntityDemo> list = new ArrayList<>();
        EntityDemo entityDemo = new EntityDemo();
        entityDemo.setId(1);
        list.add(entityDemo);
        EntityDemo entityDemo1 = new EntityDemo();
        entityDemo1.setId(2);
        list.add(entityDemo1);
        try{
            TimeUnit.SECONDS.sleep(2);
        }catch (Exception e){

        }

        return list;
    }
    public static List<EntityDemo> getAll() {
        List<EntityDemo> list = new ArrayList<>();
        EntityDemo entityDemo = new EntityDemo();
        entityDemo.setId(3);
        list.add(entityDemo);
        try{
            TimeUnit.SECONDS.sleep(2);
        }catch (Exception e){

        }
        return list;
    }
    public static List<EntityDemo> buildName(List<EntityDemo> list){
        list.forEach(i->{
            i.setName("你好");
        });
        return list;
    }


}
