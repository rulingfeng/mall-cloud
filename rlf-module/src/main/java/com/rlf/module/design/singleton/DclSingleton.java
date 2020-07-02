package com.rlf.module.design.singleton;

public class DclSingleton {

    /**
     * volatile 有内存可见性 和 防止指令重排
     * 避免指令重排可以防止有半初始化状态的对象产生  singleton = null的情况    指针没有指向堆空间  只在栈定义了变量
     *
     * DCL : double check lock 双重效验
     */
    private static volatile DclSingleton singleton = null;

    private DclSingleton(){}

    public static DclSingleton getInstants(){
        if(null == singleton ){
            synchronized (DclSingleton.class){
                if(null == singleton){
                    singleton = new DclSingleton();
                }
            }
        }
        return singleton;
    }


}
