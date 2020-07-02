package com.rlf.module.design.strategy;


import java.util.HashMap;
import java.util.Map;

/**
 * @Description: 策略模式存储类
 * @Author: RU
 * @Date: 2020/4/18 15:45
 */
public class StrategyObject {
    private static final Map<String, StrategyService> map = new HashMap<>();

    public static void putObj(String key,StrategyService strategyService){
        map.put(key,strategyService);
    }

    public static StrategyService getMap(String key) {
        return map.get(key);
    }

}
