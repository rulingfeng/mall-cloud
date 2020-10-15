package com.rlf.module.design.builder.easyBuilder;

/**
 * @author 茹凌丰
 * @Description: 直接链式编程构建对象及属性
 * @date 2020/10/15-21:24
 */
public class Man {

    private String name;
    private Integer age;
    private Integer weight;

    public String getName() {
        return name;
    }

    public Man setName(String name) {
        this.name = name;
        return this;
    }

    public Integer getAge() {
        return age;
    }

    public Man setAge(Integer age) {
        this.age = age;
        return this;
    }

    public Integer getWeight() {
        return weight;
    }

    public Man setWeight(Integer weight) {
        this.weight = weight;
        return this;
    }
}
