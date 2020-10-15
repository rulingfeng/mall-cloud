package com.rlf.module.design.factory.abstractFactory.foodEntity;

import com.rlf.module.design.factory.abstractFactory.Food;

/**
 * @author 茹凌丰
 * @Description: 米饭 (中国人吃的)
 * @date 2020/10/15-20:27
 */
public class Rice extends Food {
    @Override
    public void eat() {
        System.out.println("Rice eat");
    }
}
