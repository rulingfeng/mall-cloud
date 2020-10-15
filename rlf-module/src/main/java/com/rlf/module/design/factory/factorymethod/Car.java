package com.rlf.module.design.factory.factorymethod;

/**
 * @author 茹凌丰
 * @Description: 工厂方法,可以任意定制生产的过程
 * @date 2020/10/15-19:53
 */
public class Car implements Moveable{


    @Override
    public void go() {
        System.out.println("car go");
    }
}
