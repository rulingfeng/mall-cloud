package com.rlf.module.design.factory.abstractFactory.foodEntity;

import com.rlf.module.design.factory.abstractFactory.Food;

/**
 * @author 茹凌丰
 * @Description: 牛排 (美国人吃的)
 * @date 2020/10/15-20:27
 */
public class Steak extends Food {
    @Override
    public void eat() {
        System.out.println("Steak eat");
    }
}
