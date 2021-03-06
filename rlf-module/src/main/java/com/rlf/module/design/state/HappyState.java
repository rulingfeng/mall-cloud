package com.rlf.module.design.state;

/**
 * @author 茹凌丰
 * @Description: 开心的时候是如何 做饭 开车 吃饭 的
 * @date 2020/10/16-20:24
 */
public class HappyState extends AbstractState{
    @Override
    void drive() {
        System.out.println("开心的开车");
    }

    @Override
    void eat() {
        System.out.println("开心的吃饭");
    }

    @Override
    void cook() {
        System.out.println("开心的做饭");
    }
}
