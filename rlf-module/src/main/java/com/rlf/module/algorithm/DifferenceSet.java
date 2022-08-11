package com.rlf.module.algorithm;

import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author: 茹凌丰
 * @date: 2022/8/11
 * @description: 差集
 */
public class DifferenceSet {
    public static void main(String[] args) {
        //第2个最好
        chaji1();
        chaji2();
        chaji3();


    }


    /**
     * 差集(基于API解法) 适用于小数据量
     * 求List1中有的但是List2中没有的元素
     * 时间复杂度 O(list1.size() * list2.size())
     */
    public static void chaji1(){
        List<Integer> srouce = Lists.newArrayList();
        for (int i = 1; i < 300000; i++) {
            srouce.add(i);
        }
        List<Integer> target = new ArrayList<>(srouce);

        target.add(30001);
        srouce.add(0);
        long l = System.currentTimeMillis();
        //差集  source有的 target没有的
        srouce.removeAll(target);
        System.out.println(srouce);
        //耗时
        System.out.println(System.currentTimeMillis()-l);
        System.out.println("----------------------------");
    }

    /**
     * 差集(基于常规解法）优化解法1 适用于中等数据量
     * 求List1中有的但是List2中没有的元素
     * 空间换时间降低时间复杂度
     * 时间复杂度O(Max(list1.size(),list2.size()))
     */
    public static void chaji2(){
        List<Integer> srouce = Lists.newArrayList();
        for (int i = 1; i < 300000; i++) {
            srouce.add(i);
        }

        List<Integer> target = new ArrayList<>(srouce);
        target.add(300000);
        srouce.add(0);

        long l = System.currentTimeMillis();
        //差集  source有的 target没有的
        //空间换时间 降低时间复杂度
        Map<Integer, Integer> tempMap = new HashMap<>();
        for(Integer str:target){
            tempMap.put(str,str);
        }
        //LinkedList 频繁添加删除 也可以ArrayList容量初始化为List1.size(),防止数据量过大时频繁扩容以及数组复制
        List<Integer> resList = new ArrayList<>(srouce.size());
        for(Integer str:srouce){
            if(!tempMap.containsKey(str)){
                resList.add(str);
            }
        }
        System.out.println(resList);
        //耗时
        System.out.println(System.currentTimeMillis()-l);
        System.out.println("----------------------------");
    }

    /**
     * 差集(基于java8新特性)优化解法2 适用于大数据量
     * 求List1中有的但是List2中没有的元素
     */
    public static void chaji3(){
        List<Integer> srouce = Lists.newArrayList();
        for (int i = 1; i < 300000; i++) {
            srouce.add(i);
        }
        List<Integer> target = new ArrayList<>(srouce);

        target.add(30001);
        srouce.add(0);
        long l = System.currentTimeMillis();
        //差集  source有的 target没有的
        Map<Integer, Integer> tempMap = target.parallelStream().collect(Collectors.toMap(Function.identity(), Function.identity(), (oldData, newData) -> newData));
        List<Integer> collect = srouce.parallelStream().filter(str -> {
            return !tempMap.containsKey(str);
        }).collect(Collectors.toList());
        System.out.println(collect);
        //耗时
        System.out.println(System.currentTimeMillis()-l);
        System.out.println("----------------------------");
    }





    /**
     * 根据2个list，一个老 一个新，  取删除的元素和新增的元素
     */
    public static void aa(){
        List<Integer> srouce = Lists.newArrayList();
        for (int i = 1; i < 100000; i++) {
            srouce.add(i);
        }
        List<Integer> target = new ArrayList<>(srouce);
        target.add(20000);
        srouce.add(0);
        long l = System.currentTimeMillis();
        //删除的
        List<Integer> delete = srouce.stream().filter(i -> !target.contains(i)).collect(Collectors.toList());
        //新增的
        List<Integer> add = target.stream().filter(i -> !srouce.contains(i)).collect(Collectors.toList());
        //删除[0]
        System.out.println(delete);
        //新增[20000]
        System.out.println(add);
        System.out.println(System.currentTimeMillis()-l);
    }

}
