package com.rlf.module.design.responsibilityChain;

/**
 * @Description:
 * @Author: RU
 * @Date: 2020/4/18 16:43
 */
//总经理类，可以批准任意时常的假期，并且可以批准是否加薪。
public class GeneralManager extends Manager {
    public GeneralManager(String name) {
        super(name);
    }

    @Override
    public void handlerRequest(Request request) {
        System.out.println("General");
        if (request.getRequestType().equals("请假")) {    //能批准任意时长的假期
            System.out.println(name + ":" + request.getRequestContent() + "，时长：" + request.getNumber() + "天，被批准");
        } else if (request.getRequestType().equals("加薪") && request.getNumber() <= 500) {
            System.out.println(name + ":" + request.getRequestContent() + "，金额：￥" + request.getNumber() + "，被批准");
        } else if (request.getRequestType().equals("加薪") && request.getNumber() > 500) {
            System.out.println(name + ":" + request.getRequestContent() + "，金额：￥" + request.getNumber() + "，再说吧");
        }
    }
}
