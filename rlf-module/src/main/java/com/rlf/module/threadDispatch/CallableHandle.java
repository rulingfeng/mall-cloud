package com.rlf.module.threadDispatch;

import com.google.common.collect.Lists;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.rlf.module.entity.User;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * @author: 茹凌丰
 * @date: 2022/6/2
 * @description:
 */
@Slf4j
public class CallableHandle<T> {
    private static final int coreThreads = Runtime.getRuntime().availableProcessors();

    private static final ThreadFactory namedThread =
            new ThreadFactoryBuilder().setNameFormat("worker-thread-%d")
                    .setUncaughtExceptionHandler((thread, throwable)-> log.error("ThreadPool {} got exception", thread,throwable))
                    .build();

    private static final ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(coreThreads, coreThreads << 1 + 1,
            300, TimeUnit.SECONDS, new LinkedBlockingQueue<>(1000), namedThread);

    private final List<Callable<T>> callableList = Lists.newArrayList();

    public CallableHandle<T> add(Callable<T> callable){
        callableList.add(callable);
        return this;
    }

    public static Callable buildCallable(String str){
        return ()->{
            //处理逻辑
            User user = new User();
            user.setUserName(str);
            System.out.println(str);
            TimeUnit.SECONDS.sleep(3);
            return user;
        };
    }
    public static Callable buildCallable(Integer num){
        return ()->{
            //处理逻辑
            System.out.println(num);
            User user = new User();
            user.setAge(String.valueOf(num));
            return user;
        };
    }
    public  <T> List<T> handle() throws ExecutionException, InterruptedException {
        List<T> resList = Lists.newArrayList();
        List<Future<T>> futureList = Lists.newArrayList();
        for (Callable callable : callableList) {
            futureList.add(threadPoolExecutor.submit(callable));
        }
        for (Future<T> future : futureList) {
            resList.add( future.get());
        }
        return resList;
    }
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        List<User> res = new CallableHandle<User>().add(buildCallable("处理")).add(buildCallable(5)).handle();
        for (User re : res) {
            System.out.println(re);
        }
    }





}
