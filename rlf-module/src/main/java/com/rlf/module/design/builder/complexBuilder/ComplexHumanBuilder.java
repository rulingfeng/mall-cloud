package com.rlf.module.design.builder.complexBuilder;

/**
 * @author 茹凌丰
 * @Description: 创建人类的复杂构造类
 * @date 2020/10/15-20:58
 */
public class ComplexHumanBuilder implements HumanBuilder{
    private Human human  = new Human();
    @Override
    public HumanBuilder buildHead() {
        human.setHead(new Head("head"));
        return  this;
    }

    @Override
    public HumanBuilder buildHand() {
        human.setHand(new Hand("hand"));
        return  this;
    }

    @Override
    public HumanBuilder buildFoot() {
        human.setFoot(new Foot("foot"));
        return  this;
    }

    @Override
    public Human build() {
        return human;
    }
}
