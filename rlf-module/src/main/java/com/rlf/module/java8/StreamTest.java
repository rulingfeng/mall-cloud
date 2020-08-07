package com.rlf.module.java8;

import com.rlf.module.entity.User;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.*;

/**
 * @author RU
 * @date 2020/8/1
 * @Desc
 */
public class StreamTest {

    public static void main(String[] args) {
        List<User> list = new ArrayList<>();
        for (int i = 1; i < 11; i++) {
            User user = new User();
            user.setId(i);
            user.setUserName(i+"");
            list.add(user);
        }
        List<Integer> collect = list.stream().filter(i -> null != i.getId()).map(i -> i.getId()).collect(Collectors.toList());
        Integer min = Collections.min(collect);
        System.out.println(min);
//        for (Iterator<User> iterator = list.iterator();iterator.hasNext();){
//            iterator.next();
//        }
//        List<User> users = Collections.synchronizedList(new ArrayList<>());
//
//        Map<String,String> map = new HashMap<>();
//        //map.put()
//        list.forEach(System.out::println);
//        Integer reduce = Stream.iterate(0, i -> i+1).limit(101).parallel().reduce(0, Integer::sum);
//        Map<Integer, List<Integer>> collect = list.stream().collect(Collectors.groupingBy(Function.identity()));
//        IntStream longStream = list.stream().sequential().flatMapToInt(i-> IntStream.of(Integer.valueOf(i)));
        //longStream.forEach(System.out::println);



    }
}
