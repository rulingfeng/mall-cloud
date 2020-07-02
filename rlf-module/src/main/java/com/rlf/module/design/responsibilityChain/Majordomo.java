package com.rlf.module.design.responsibilityChain;

/**
 * @Description:
 * @Author: RU
 * @Date: 2020/4/18 16:42
 */
//总监类如下，只可批准五天以内的假期，其余请求将继续申请上级。
public class Majordomo extends Manager {
    public Majordomo(String name) {
        super(name);
    }

    @Override
    public void handlerRequest(Request request) {
        System.out.println("Major");
        if (request.getRequestType().equals("请假") && request.getNumber() <= 5) {    //只能批准五天内的假期
            System.out.println(name + ":" + request.getRequestContent() + "，时长：" + request.getNumber() + "天，被批准");
        } else {    //其余请求申请上级
            if (superior != null) {
                superior.handlerRequest(request);
            }
        }
    }
}
