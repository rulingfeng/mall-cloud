package com.rlf.module.threadDispatch;

import com.alibaba.fastjson.JSONObject;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.rlf.module.entity.User;
import io.swagger.models.auth.In;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.implementation.bytecode.Throw;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BiFunction;
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
@Slf4j
public class CompletableFutureDemo {

    private static int coreThreads = Runtime.getRuntime().availableProcessors();

    private static ThreadFactory namedThread =
            new ThreadFactoryBuilder().setNameFormat("worker-thread-%d")
                    .setUncaughtExceptionHandler((thread, throwable)-> log.error("ThreadPool {} got exception", thread,throwable))
                    .build();

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
        //遍历list各自执行
        //listStreamSeparately();
        //如果执行中后异常,可以用handle方法进行捕捉,并处理
        //exceptionHandle();


    }



    public static void exceptionHandle()throws Exception{

        CompletableFuture<String> maturityFuture = CompletableFuture.supplyAsync(() -> {
            int a = 1/0;
            return "aaa";
        }).thenApply(data->data+"bbb")
                .handle((data,exception)->{
                    System.out.println(data);
                    System.out.println(exception.getMessage());
                    return "return";

                });
        System.out.println(maturityFuture.get());

    }

    public static void listStreamSeparately(){
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

        //Wait for them all to complete
        List<CompletableFuture<List<Boolean>>> futureList = aList.stream()
                .map(strings -> CompletableFuture.supplyAsync(() -> processList(strings), threadPoolExecutor))
                .collect(toList());

//        CompletableFuture.allOf(futureList.toArray(new CompletableFuture[0])).join();
        CompletableFuture.allOf(futureList.toArray(new CompletableFuture[futureList.size()])).whenComplete((x,y)->{
            System.out.println(x);
            System.out.println(y);
        });
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
//                CompletableFuture.completedFuture(original).thenAsupplyAsyncpply(result::append),
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
                TimeUnit.SECONDS.sleep(2);
                System.out.println(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return true;
        }).thenAccept(i -> res(i));

        CompletableFuture.supplyAsync(()->{
            try {
                TimeUnit.SECONDS.sleep(4);
                System.out.println(4);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return true;
        }).thenAccept(i->res(i));

        CompletableFuture.supplyAsync(()->{
            try {
                TimeUnit.SECONDS.sleep(6);
                System.out.println(6);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return true;
        }).thenAccept(i->res(i));
        System.in.read();
    }

    private static void res(boolean flag){
        if(!flag){
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

    /**
     * 来自https://javadoop.com/post/completable-future
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public static void study() throws ExecutionException, InterruptedException {
        //thenRun任务 A 执行完执行 B，并且 B 不需要 A 的结果。
        CompletableFuture.supplyAsync(() -> "resultA").thenRun(() -> {});
        //thenAccept，任务 A 执行完执行 B，B 需要 A 的结果，但是任务 B 不返回值。
        CompletableFuture.supplyAsync(() -> "resultA").thenAccept(resultA -> {});

        //thenApply，任务 A 执行完执行 B，B 需要 A 的结果，同时任务 B 有返回值。
        CompletableFuture.supplyAsync(() -> "resultA").thenApply(resultA -> resultA + " resultB");

        CompletableFuture<String> cfA = CompletableFuture.supplyAsync(() -> "resultA");
        CompletableFuture<String> cfB = CompletableFuture.supplyAsync(() -> "resultB");

        ExecutorService executorService = Executors.newCachedThreadPool();
        //指的是两个任务中的其中一个执行完成，就执行指定的操作 （参考以上 accept、apply、run）

        //不是同步的，它由任务 A 或任务 B 所在的执行线程来执行，取决于哪个任务先结束。
        cfA.acceptEither(cfB, result -> {});
        //加了 Async 后缀的方法，代表将需要执行的任务放到 ForkJoinPool.commonPool() 中执行(非完全严谨)
        cfA.acceptEitherAsync(cfB, result -> {});
        //将任务放到指定线程池中执行；
        cfA.acceptEitherAsync(cfB, result -> {}, executorService);

        cfA.applyToEither(cfB, result -> {return result;});
        cfA.applyToEitherAsync(cfB, result -> {return result;});
        cfA.applyToEitherAsync(cfB, result -> {return result;}, executorService);

        cfA.runAfterEither(cfB, () -> {});
        cfA.runAfterEitherAsync(cfB, () -> {});
        cfA.runAfterEitherAsync(cfB, () -> {}, executorService);








        //thenAcceptBoth表示后续的处理不需要返回值
        cfA.thenAcceptBoth(cfB, (resultA, resultB) -> {});
        cfA.thenAcceptBoth(CompletableFuture.supplyAsync(() -> "resultB"),
                (resultA, resultB) -> {});
        //thenCombine表示后续的处理需要返回值
        cfA.thenCombine(cfB, (resultA, resultB) -> "result A + B");
        //runAfterBoth 不需要 resultA 和 resultB的结果
        cfA.runAfterBoth(cfB, () -> {});


        //thenCompose 和上面的thenCombine很相似，形成链：cfA -> cfB -> cfC。
        //这里有个隐藏的点：cfA、cfB、cfC 它们完全没有数据依赖关系，我们只不过是聚合了它们的结果。
        CompletableFuture<String> thenCompose = CompletableFuture.supplyAsync(() -> {
            // 第一个实例的结果
            return "hello";
        }).thenCompose(resultA -> CompletableFuture.supplyAsync(() -> {
            // 把上一个实例的结果传递到这里
            return resultA + " world";
        })).thenCompose(resultAB -> CompletableFuture.supplyAsync(() -> {
            // 到这里大家应该很清楚了
            return resultAB + ", I'm robot";
        }));// hello world, I'm robot



        //allOf 所以这里的 join() 将阻塞，直到所有的任务执行结束
        CompletableFuture<Void> allOf = CompletableFuture.allOf(cfA, cfB);
        allOf.join();
        //anyOf 返回最先完成的任务的结果
        CompletableFuture<Object> anyOf = CompletableFuture.anyOf(cfA, cfB);
        Object anyOfResult = anyOf.join();




        //exceptionally捕捉到上一个方法的异常,返回新的结果,继续下面的执行,如果不用exceptionally则直接报错
        CompletableFuture<String> exceptionally = CompletableFuture.supplyAsync(() -> {
            throw new RuntimeException();
        })
                .exceptionally(ex -> "errorResultA")
                .thenApply(resultA -> resultA + " resultB");

        //handle捕捉到异常，exception参数为异常信息，res为上一个thenApply返回的结果
        CompletableFuture<String> handle = CompletableFuture.supplyAsync(() -> "resultA")
                .thenApply(resultA -> resultA + " resultB")
                // 任务 C 抛出异常
                .thenApply(resultB -> {throw new RuntimeException();})
                // 处理任务 C 的返回值或异常
                .handle((res,exception) -> {
                    if (exception != null) {
                        return "errorResultC";
                    }
                    return res;
                })
                .thenApply(resultC -> resultC + " resultD");
    }





}
