package com.rlf.module.design.state;

/**
 * @author 茹凌丰
 * @Description: 状态模式     抽象状态类
 *              eg:每个人会开车  会吃饭 会做饭, 但是在不同的心情下,开车吃饭做饭的细节是不一样的
 * @date 2020/10/16-20:21
 */
public abstract class AbstractState {
    abstract void drive();
    abstract void eat();
    abstract void cook();

}
