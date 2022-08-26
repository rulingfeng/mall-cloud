package com.rlf.module.threadDispatch;

import cn.hutool.core.collection.CollectionUtil;
import com.google.common.collect.Lists;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.concurrent.*;
import java.util.function.Function;

import static java.util.stream.Collectors.toList;

/**
 * @author: 茹凌丰
 * @date: 2022/8/26
 * @description:
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
        //原数据
        List<Integer> userList = Lists.newArrayList(1, 2, 3);


        List<Integer> resList = asyncHandleListAndSyncReturn(userList, (val) -> {
            //异步处理逻辑
                CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> {
                    try {
                        TimeUnit.SECONDS.sleep(3);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println(val);
                    Integer res = val;
                    return res;
                }, threadPoolExecutor);
                return future;
            }
        );
        System.out.println(resList);
    }


    /**
     * 实现方式CompletableFuture.supplyAsync,并且同步返回结果
     * @param com
     * @param function
     * @param <T>
     * @param <F>
     * @return
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public static <T,F> List<T> asyncHandleListAndSyncReturn(List<T> com,Function<T,F> function) throws ExecutionException, InterruptedException {
        if(CollectionUtil.isEmpty(com)){
            return null;
        }
        List<CompletableFuture<F>> collect = com.stream().map(i -> hanleFunction(i, function)).collect(toList());
        return (List<T>) blockThreadAndGet(collect);

    }

    public static <T,R>CompletableFuture<R> hanleFunction(T value, Function<T,R> function)  {
        return (CompletableFuture<R>) function.apply(value);
    }


    public static <T> List<T> blockThreadAndGet(List<CompletableFuture<T>> completableFutureList) throws ExecutionException, InterruptedException {
        List<T> resList = Lists.newArrayList();
        for (CompletableFuture<T> future : completableFutureList) {
            resList.add(future.get());
        }
        return resList;
    }



}
