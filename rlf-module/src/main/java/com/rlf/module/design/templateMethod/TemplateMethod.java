package com.rlf.module.design.templateMethod;

/**
 * @author 茹凌丰
 * @Description: 模板方法
 * @date 2020/10/16-20:09
 */
public abstract class TemplateMethod {

    public void impl(){
        do1();
        do2();
    }
    abstract void do1();
    abstract void do2();
}
