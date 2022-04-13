package com.rlf.module.threadDispatch;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.*;

/**
 * @author: 茹凌丰
 * @date: 2022/4/13
 * @description: 线程池封装的方法
 */
@Slf4j
public class ThreadPoolUtils {
    private static ThreadPoolExecutor threadPool = null;
    private static final String POOL_NAME = "myPool";

    // 等待队列长度
    private static final int BLOCKING_QUEUE_LENGTH = 1000;
    // 闲置线程存活时间
    private static final int KEEP_ALIVE_TIME = 60 * 1000;

    private ThreadPoolUtils() {
        throw new IllegalStateException("utility class");
    }


    /**
     * 无返回值直接执行
     *
     * @param runnable 需要运行的任务
     */
    public static void execute(Runnable runnable) {
        getThreadPool().execute(runnable);
    }

    /**
     * 有返回值执行
     * 主线程中使用Future.get()获取返回值时，会阻塞主线程，直到任务执行完毕
     *
     * @param callable 需要运行的任务
     */
    public static <T> Future<T> submit(Callable<T> callable) {
        return getThreadPool().submit(callable);
    }

    private static synchronized ThreadPoolExecutor getThreadPool() {
        if (threadPool == null) {
            // 获取处理器数量
            int cpuNum = Runtime.getRuntime().availableProcessors();
            // 根据cpu数量,计算出合理的线程并发数
            int maximumPoolSize = cpuNum * 2 + 1;
            // 核心线程数、最大线程数、闲置线程存活时间、时间单位、线程队列、线程工厂、当前线程数已经超过最大线程数时的异常处理策略
            threadPool = new ThreadPoolExecutor(maximumPoolSize - 1,
                    maximumPoolSize,
                    KEEP_ALIVE_TIME,
                    TimeUnit.MILLISECONDS,
                    new LinkedBlockingDeque<>(BLOCKING_QUEUE_LENGTH),
                    new ThreadFactoryBuilder().setNameFormat(POOL_NAME + "-%d").build(),
                    new ThreadPoolExecutor.AbortPolicy() {
                        @Override
                        public void rejectedExecution(Runnable r, ThreadPoolExecutor e) {
                            log.warn("线程爆炸了，当前运行线程总数：{}，活动线程数：{}。等待队列已满，等待运行任务数：{}",
                                    e.getPoolSize(),
                                    e.getActiveCount(),
                                    e.getQueue().size());
                        }
                    });

        }

        return threadPool;
    }


    public static void test1() {
        Future<String> future = ThreadPoolUtils.submit(() -> {
            return "我有返回值哦";
        });
        try {
            log.info(future.get());
        } catch (InterruptedException | ExecutionException e) {
            log.error("任务超过指定时间未返回值，线程超时退出");
        }
    }
    public static void test2() {
        Future<String> futureTimeout = ThreadPoolUtils.submit(() -> {
            Thread.sleep(99999999);
            return "我有返回值，但是超时了";
        });
        try {
            // 建议使用该方式执行任务，不会导致线程因为某写原因一直占用线程，
            // 从而导致未知问题
            // 注意使用局部try避免主线程异常，导致主线程无法继续执行
            log.info(futureTimeout.get(3, TimeUnit.SECONDS));
        } catch (InterruptedException | ExecutionException e) {
            log.error("任务执行异常");
        } catch (TimeoutException e) {
            log.error("任务超过指定时间未返回值，线程超时退出");
        }
    }

}
