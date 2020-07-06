package com.rlf.module.algorithm;

import com.alibaba.fastjson.JSONObject;

import java.util.LinkedList;
import java.util.Set;

/**
 * @author RU
 * @date 2020/7/4
 * @Desc
 */
public class LRU {
    public static void main(String[] args) {
        int[] num = new int[]{2,4,3,2,1,1,6,4};
        LinkedList<Integer> list = new LinkedList<>();
        for (int i : num) {
            if (list.contains(i)) {
                int index = list.lastIndexOf(i);
                list.remove(index);
                list.addLast(i);
            }else {
                list.add(i);
            }
        }
//        String a = "{\"生活用品数量\": \"4层80g*24卷\"}";
//        JSONObject json = JSONObject.parseObject(a);
//        Set<String> strings = json.keySet();
//        Object o = json.get(strings.toArray()[0]);
//        System.out.println(o);
    }
}
