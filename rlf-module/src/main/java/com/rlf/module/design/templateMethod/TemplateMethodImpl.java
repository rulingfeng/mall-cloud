package com.rlf.module.design.templateMethod;

/**
 * @author 茹凌丰
 * @Description: 模板方法的具体实现
 * @date 2020/10/16-20:11
 */
public class TemplateMethodImpl extends TemplateMethod{
    @Override
    void do1() {
        System.out.println("do1");
    }

    @Override
    void do2() {
        System.out.println("do2");
    }
}
