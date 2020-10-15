package com.rlf.module.design.builder.hardBuilder;

import com.rlf.module.design.builder.easyBuilder.Man;

/**
 * @author 茹凌丰
 * @Description: 简单构建模式演示
 * @date 2020/10/15-21:10
 */
public class HardBuilderDemo {
    public static void main(String[] args) {
        People build = People.builder().age(1).build();

        System.out.println(build.getAge());
        System.out.println(build.getName());
        System.out.println(build.getWeight());

        People build1 = People.builder().age(2).build();

        System.out.println(build1.getAge());
        System.out.println(build1.getName());
        System.out.println(build1.getWeight());

    }
}
