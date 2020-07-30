package com.rlf.module.design.proxy;

import com.rlf.module.entity.People;
import com.rlf.module.entity.User;

/**
 * @author RU
 * @date 2020/7/30
 * @Desc
 */
/**cglib静态代理*/
public class UserStaticProxy implements People {

    private People people;


    public UserStaticProxy(People people) {
        this.people = people;
    }


    @Override
    public void speak() {
        System.out.println("执行前");
        people.speak();
        System.out.println("执行后");
    }

    public static void main(String[] args) {
        User user = new User();
        UserStaticProxy userStaticProxy = new UserStaticProxy(user);
        userStaticProxy.speak();
    }
}
