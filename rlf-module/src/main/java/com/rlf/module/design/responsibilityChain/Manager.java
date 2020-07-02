package com.rlf.module.design.responsibilityChain;

/**
 * @Description:
 * @Author: RU
 * @Date: 2020/4/18 16:38
 */
//通过Manager抽象类管理所有管理者，setSuperior()方法用于定义职责链的下一级，即定义当前管理者的上级。
public abstract class Manager {
    protected String name;
    protected Manager superior;    //管理者的上级

    public Manager(String name) {
        this.name = name;
    }

    //设置管理者的上级
    public void setSuperior(Manager superior) {
        this.superior = superior;
    }

    //申请请求
    public abstract void handlerRequest(Request request);
}
