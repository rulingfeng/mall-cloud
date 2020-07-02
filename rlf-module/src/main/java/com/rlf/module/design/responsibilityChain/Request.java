package com.rlf.module.design.responsibilityChain;

/**
 * @Description:
 * @Author: RU
 * @Date: 2020/4/18 16:39
 */
public class Request {
    private String requestType;    //申请类别
    private String requestContent;    //申请内容
    private int number;    //数量

    public String getRequestType() {
        return requestType;
    }

    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }

    public String getRequestContent() {
        return requestContent;
    }

    public void setRequestContent(String requestContent) {
        this.requestContent = requestContent;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }
}
