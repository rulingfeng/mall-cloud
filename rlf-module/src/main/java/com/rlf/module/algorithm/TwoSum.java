package com.rlf.module.algorithm;

import io.swagger.models.auth.In;

import java.util.HashMap;

/**
 * @author RU
 * @date 2020/7/4
 * @Desc  two sum 算法
 */
public class TwoSum {
    /**
     * 给定一个数组，给定一个数字。返回数组中可以相加得到指定数字的两个索引。
     * 比如：给定nums = [2, 7, 11, 15], target = 9
     * 那么要返回 [0, 1]，因为2 + 7 = 9
     */
    public static void main(String[] args) {
        int[] arr = new int[]{3,6,43,7,2,54};
        int tar = 97;
        HashMap<Integer, Integer> map = new HashMap<>();
        for (int i = 0; i < arr.length; i++) {
            int temp = tar - arr[i];
            if(map.containsKey(temp)){
                System.out.println( i + "+" + map.get(temp));
            }
            map.put(arr[i],i);
        }
    }

}
