package com.rlf.module.threadDispatch;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * @author 茹凌丰
 * @Title:
 * @Package
 * @Description:
 * @date 2020/7/1113:20
 */
public class CompletableFutureDemo {
    public static void main(String[] args)throws Exception {

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
            //超市处理
            System.exit(0);
        }
    }

}
