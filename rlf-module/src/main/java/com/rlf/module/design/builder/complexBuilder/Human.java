package com.rlf.module.design.builder.complexBuilder;

/**
 * @author 茹凌丰
 * @Description: 人类
 * @date 2020/10/15-20:51
 */
public class Human {
    private Head head;
    private Hand hand;
    private Foot foot;

    public Head getHead() {
        return head;
    }

    public void setHead(Head head) {
        this.head = head;
    }

    public Hand getHand() {
        return hand;
    }

    public void setHand(Hand hand) {
        this.hand = hand;
    }

    public Foot getFoot() {
        return foot;
    }

    public void setFoot(Foot foot) {
        this.foot = foot;
    }
}
