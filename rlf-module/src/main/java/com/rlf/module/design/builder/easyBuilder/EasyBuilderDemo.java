package com.rlf.module.design.builder.easyBuilder;

import com.rlf.module.design.builder.hardBuilder.People;

/**
 * @author 茹凌丰
 * @Description: 简单构建模式演示
 * @date 2020/10/15-21:10
 */
public class EasyBuilderDemo {
    public static void main(String[] args) {

        Man nihao = new Man().setAge(1).setName("nihao").setWeight(2);
        System.out.println(nihao.getAge());
        System.out.println(nihao.getWeight());
        System.out.println(nihao.getName());
    }
}
