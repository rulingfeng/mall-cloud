package com.rlf.module.threadDispatch;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author 茹凌丰
 * @Title:
 * @Package
 * @Description:
 * @date 2020/7/1113:20
 */
public class CompletableFutureDemo {
    public static void main(String[] args)throws Exception {
        //3个CompletableFuture  只要有一个返回false 其他2个线程立马停止 不在执行.
        //rightOffReturnFalseIfHasFalse();
        //同时执行两个不相关任务，然后合并
        //performMerge();
        vagf();

    }

    public static void vagf(){
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
