package com.rlf.module.design.builder.hardBuilder;

/**
 * @author 茹凌丰
 * @Description: 人 构建器
 * @date 2020/10/15-21:05
 */

public class People {

    private String name;
    private Integer age;
    private Integer weight;


    public static PeopleBuilder builder() {
        return new People.PeopleBuilder();
    }

    public People(PeopleBuilder builder) {
        this.name = builder.name;
        this.age = builder.age;
        this.weight = builder.weight;
    }

    //静态内部类
    public static class PeopleBuilder{
        private String name;
        private Integer age;
        private Integer weight;

        public PeopleBuilder name(String name){
            this.name = name;
            return this;
        }
        public PeopleBuilder age(Integer age){
            this.age = age;
            return this;
        }
        public PeopleBuilder weight(Integer weight){
            this.weight = weight;
            return this;
        }
        public People build() {
            return new People(this);
        }

    }




    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

}
