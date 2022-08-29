package com.rlf.module.threadDispatch;

import cn.hutool.core.collection.CollectionUtil;
import com.google.common.collect.Lists;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.rlf.module.entity.Car;
import com.rlf.module.entity.User;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.concurrent.*;
import java.util.function.Function;

import static java.util.stream.Collectors.toList;

/**
 * @author: 茹凌丰
 * @date: 2022/8/26
 * @description: CompletableFuture处理工具类
 */
@Slf4j
public class CompletableFutureBlockUtils {

    private static final int coreThreads = Runtime.getRuntime().availableProcessors();

    private static final ThreadFactory namedThread =
            new ThreadFactoryBuilder().setNameFormat("activity-subscribe-thread-%d")
                    .setUncaughtExceptionHandler((thread, throwable)-> log.error("activity subscribe ThreadPool {} got exception", thread,throwable))
                    .build();

    private static final ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(coreThreads << 1, coreThreads << 2 + 1,
            300, TimeUnit.SECONDS, new LinkedBlockingQueue<>(2 << 10), namedThread,new ThreadPoolExecutor.CallerRunsPolicy());


    public static void main(String[] args) throws ExecutionException, InterruptedException {
        //示例1
        //原数据
        List<User> userList = Lists.newArrayList(new User("名称1"),new User("名称2"),new User("名称3"));
        List<CompletableFuture<Car>> collect1 = userList.stream().map(i -> hanleFunction(i, (val) -> CompletableFuture.supplyAsync(() -> {

            System.out.println(val.getUserName());
            Car car = new Car();
            car.setModel(val.getUserName());
            return car;
        }, threadPoolExecutor))).collect(toList());

        List<Car> integers = blockThreadAndGet(collect1);
        System.out.println(integers);

        //示例2
        List<User> userList2 = Lists.newArrayList(new User("名称4"),new User("名称5"),new User("名称6"));
        List<Car> resList = asyncHandleListAndSyncReturn(userList2, (val) -> {
            //异步处理逻辑
            return CompletableFuture.supplyAsync(() -> {
                //处理逻辑
                        System.out.println(val.getUserName());
                        Car car = new Car();
                        car.setModel(val.getUserName());
                        return car;
                    }, threadPoolExecutor);
            }
        );
        System.out.println(resList);
    }


    /**
     * 多线程处理列表,实现方式CompletableFuture.supplyAsync,并且同步返回结果
     * @param sourceList 需要处理的原列表
     * @param function 处理逻辑
     * @param <T> 原数据类型
     * @param <F> 返回的数据类型
     * @return 所有执行后返回对象的列表
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public static <T,F> List<F> asyncHandleListAndSyncReturn(List<T> sourceList,Function<T,CompletableFuture<F>> function) throws ExecutionException, InterruptedException {
        if(CollectionUtil.isEmpty(sourceList)){
            return null;
        }
        List<CompletableFuture<F>> collect = sourceList.stream().map(i -> hanleFunction(i, function)).collect(toList());
        return blockThreadAndGet(collect);

    }

    /**
     * 执行function
     * @param value 原数据对象
     * @param function 处理逻辑
     * @param <T> 原数据类型
     * @param <R> 返回的数据类型
     * @return 线程执行对象
     */
    public static <T,R> CompletableFuture<R> hanleFunction(T value, Function<T,CompletableFuture<R>> function)  {
        return function.apply(value);
    }

    /**
     * 阻塞,并且拿到返回值
     * @param completableFutureList 线程执行对象
     * @param <F> 返回的数据类型
     * @return 所有返回对象的列表
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public static <F> List<F> blockThreadAndGet(List<CompletableFuture<F>> completableFutureList) throws ExecutionException, InterruptedException {
        List<F> resList = Lists.newArrayList();
        for (CompletableFuture<F> future : completableFutureList) {
            resList.add(future.get());
        }
        return resList;
    }


}
