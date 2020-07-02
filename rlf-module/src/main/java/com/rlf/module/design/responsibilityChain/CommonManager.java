package com.rlf.module.design.responsibilityChain;

/**
 * @Description:
 * @Author: RU
 * @Date: 2020/4/18 16:41
 */
//经理类如下，只可批准两天以内的假期，其余请求将继续申请上级。
public class CommonManager extends Manager {

    public CommonManager(String name) {
        super(name);
    }


    @Override
    public void handlerRequest(Request request) {
        System.out.println("common");
        if (request.getRequestType().equals("请假") && request.getNumber() <= 2) {    //只能批准两天内的假期
            System.out.println(name + ":" + request.getRequestContent() + "，时长：" + request.getNumber() + "天，被批准");
        } else {    //其余请求申请上级
            if (superior != null) {
                superior.handlerRequest(request);
            }
        }
    }
}
