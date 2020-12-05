package com.rlf.module.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.google.common.collect.Lists;
import com.rlf.module.entity.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * @author 茹凌丰
 * @Description 函数式接口设计
 * @date 2020/12/5-7:43
 */
public class FunctionInterfaceImpl {

    public static void main(String[] args) {
        ArrayList<User> users = Lists.newArrayList(new User("1"), new User("2"), new User("3"));
        List<User> convert = convert(users, s -> filter(s));
        convert.forEach(System.out::println);
    }

    public static List<User>  filter(User user){
        if("1".equals(user.getUserName())){
            return null;
        }
        return Lists.newArrayList(user);
    }

    public static <T> List<T> convert(List<T> list ,Function<T,List<T>> function){
        return list.stream().filter(Objects::nonNull).flatMap( s ->{
            List<T> apply = function.apply(s);
            if(CollectionUtil.isEmpty(apply)){
                return Lists.<T>newArrayList().stream();
            }
            return apply.stream();
        }).filter(Objects::nonNull).collect(Collectors.toList());
    }
}