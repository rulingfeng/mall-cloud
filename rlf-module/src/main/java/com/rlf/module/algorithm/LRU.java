package com.rlf.module.algorithm;

import com.alibaba.fastjson.JSONObject;
import io.swagger.models.auth.In;

import java.math.BigDecimal;
import java.util.*;

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
    }
}
