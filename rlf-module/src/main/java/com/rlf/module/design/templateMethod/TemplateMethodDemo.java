package com.rlf.module.design.templateMethod;

/**
 * @author 茹凌丰
 * @Description: 模板方法具体调用
 * @date 2020/10/16-20:13
 */
public class TemplateMethodDemo {

    public static void main(String[] args) {
        TemplateMethod templateMethod = new TemplateMethodImpl();
        templateMethod.impl();
    }
}
