package com.rlf.module.design.singleton;

/**
 * @author 茹凌丰
 * @Description: 静态内部类订单
 * @date 2020/9/24-20:25
 */
public class StaticInnerSingleton {

    private StaticInnerSingleton(){}

    private static class StaticInnerHolder{
        private final static  StaticInnerSingleton INSTATNS= new StaticInnerSingleton();
    }
    public StaticInnerSingleton getInstans(){
        return StaticInnerHolder.INSTATNS;
    }

}
