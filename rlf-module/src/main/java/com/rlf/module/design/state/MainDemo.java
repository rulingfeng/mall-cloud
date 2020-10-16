package com.rlf.module.design.state;

/**
 * @author 茹凌丰
 * @Description: 状态模式调用演示
 * @date 2020/10/16-20:28
 */
public class MainDemo {
    public static void main(String[] args) {
        OneMan oneMan = new OneMan(new HappyState());
        oneMan.drive();
        oneMan.eat();
        oneMan.cook();

        OneMan oneMan1 = new OneMan(new SadState());
        oneMan1.drive();
        oneMan1.eat();
        oneMan1.cook();
    }
}
