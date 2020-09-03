package com.rlf.module.design.singleton;

public class DclSingleton {

    /**
     * volatile 有内存可见性 和 防止指令重排
     *  可见性通过(MESI协议实现 )  :
     *          当CPU写数据时，如果发现操作的变量是共享变量，即在其他CPU中也存在该变量的副本，
     *          会发出信号通知其他CPU将该变量的缓存行置为无效状态，因此当其他CPU需要读取这个变量时，发现自己缓存中缓存该变量的缓存行是无效的，
     *          那么它就会从内存重新读取。
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
