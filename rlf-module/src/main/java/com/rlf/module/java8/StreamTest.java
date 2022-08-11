package com.rlf.module.java8;

import com.google.common.collect.Lists;
import com.rlf.module.entity.User;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
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
//        Integer reduce = Stream.iterate(0, i -> i+1).limit(101).parallel().reduce(0, Integer::sum);
//        Map<Integer, List<Integer>> collect = list.stream().collect(Collectors.groupingBy(Function.identity()));
//        IntStream longStream = list.stream().flatMapToInt(i-> IntStream.of(Integer.valueOf(i)));
        //longStream.forEach(System.out::println);

        //写filter(Predicate)过滤方法
        listStreamFilter();
        //通过对象中的一个字段去重, stream流的distinct()方法是通过对象里面所以信息都相等才会去重
        listStreamDistinctFilter();
    }

    private final static String MERCHATCODE = "MAOCHAO";
    public static void listStreamFilter(){
        List<User> list = Lists.newArrayList(new User("1"), new User("2")
                , new User("3"), new User("4"), new User("5"));
        List<User> res = list.stream().filter(filterByMerchatCode("TMGJZY")).collect(Collectors.toList());
        res.forEach(System.out::println);
    }

    public static void listStreamDistinctFilter(){
        User user = new User();
        user.setId(1);
        user.setAge("3");
        User user1 = new User();
        user1.setId(1);
        user1.setAge("4");
        User user2 = new User();
        user2.setId(2);
        user2.setAge("5");
        List<User> userList = Lists.newArrayList(user,user1,user2);
        List<User> collect = userList.stream().filter(distinctByKey(User::getId)).collect(Collectors.toList());
        System.out.println(collect);
    }


    /**
     * 过滤逻辑
     * @param merchatCode
     * @return
     */
    private static Predicate<User> filterByMerchatCode(String merchatCode) {
        if(MERCHATCODE.equals(merchatCode)){
            return user -> true;
        }
        return user -> {
            if("4".equals(user.getUserName())){
                return false;
            }
            return true;
        };
    }

    /**
     * 过滤去重
     * @param keyExtractor
     * @return
     * @param <T>
     */
    private static  <T> Predicate<T> distinctByKey(Function<? super T, Object> keyExtractor) {
        ConcurrentHashMap<Object, Boolean> map = new ConcurrentHashMap<>();
        return t -> map.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }

}
