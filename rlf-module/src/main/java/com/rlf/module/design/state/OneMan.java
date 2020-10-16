package com.rlf.module.design.state;

/**
 * @author 茹凌丰
 * @Description: 一个男人
 * @date 2020/10/16-20:26
 */
public class OneMan {
    private String name;
    AbstractState state ;
    public OneMan(AbstractState state){
        this.state = state;
    }

    public void drive() {
        state.drive();
    }


    public void eat() {
        state.eat();
    }


    public void cook() {
        state.cook();
    }
}
